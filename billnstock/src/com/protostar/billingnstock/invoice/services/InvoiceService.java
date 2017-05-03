package com.protostar.billingnstock.invoice.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.TxnType;
import com.googlecode.objectify.Work;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.invoice.entities.InvoiceSettingsEntity;
import com.protostar.billingnstock.invoice.entities.QuotationEntity;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.stock.services.StockManagementService;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billingnstock.tax.entities.TaxReport;
import com.protostar.billingnstock.tax.entities.TaxReportItem;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.service.BaseService;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EmailHandler;
import com.protostar.billnstock.until.data.EntityPagingInfo;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.TextLocalSMSHandler;

@Api(name = "invoiceService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.stock.services", ownerName = "com.protostar.billingnstock.stock.services", packagePath = ""))
public class InvoiceService extends BaseService {

	@ApiMethod(name = "addInvoice", path = "addInvoice")
	public InvoiceEntity saveInvoice(InvoiceEntity documentEntity)
			throws MessagingException, IOException {

		if (documentEntity.getStatus() == DocumentStatus.FINALIZED
				&& documentEntity.isStatusAlreadyFinalized()) {
			throw new RuntimeException(
					"Save not allowed. InvoiceEntity is already FINALIZED: "
							+ this.getClass().getSimpleName()
							+ " Finalized entity can't be altered.");
		}
		documentEntity = ofy().execute(TxnType.REQUIRED,
				new Work<InvoiceEntity>() {
					private InvoiceEntity documentEntity;

					private Work<InvoiceEntity> init(
							InvoiceEntity documentEntity) {
						this.documentEntity = documentEntity;
						return this;
					}

					public InvoiceEntity run() {
						if (documentEntity.getStatus() == DocumentStatus.FINALIZED) {
							StockManagementService.adjustStockItems(
									documentEntity.getBusiness(),
									documentEntity.getProductLineItemList(), 0,
									documentEntity.getItemNumber());
						}
						ofy().save().entity(documentEntity).now();
						return documentEntity;
					}
				}.init(documentEntity));
		if (documentEntity.getStatus() != DocumentStatus.DRAFT) {
			new EmailHandler().sendInvoiceEmail(documentEntity);
		}

		if (documentEntity.getStatus() == DocumentStatus.SENT) {
			new TextLocalSMSHandler().sendInvoiceTextMsg(documentEntity);
		}

		return documentEntity;
	}

	@ApiMethod(name = "getStarredInvoices", path = "getStarredInvoices")
	public List<InvoiceEntity> getStarredInvoices(@Named("busId") Long busId) {
		List<InvoiceEntity> starredInvoices = ofy().load()
				.type(InvoiceEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("starred", true).list();
		return starredInvoices;
	}

	@ApiMethod(name = "getUnpaidInvoices", path = "getUnpaidInvoices")
	public List<InvoiceEntity> getUnpaidInvoices(@Named("busId") Long busId) {
		List<InvoiceEntity> starredInvoices = ofy().load()
				.type(InvoiceEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("status !=", DocumentStatus.PAID).list();
		return starredInvoices;
	}

	@ApiMethod(name = "getAllInvoice", path = "getAllInvoice")
	public List<InvoiceEntity> getAllInvoice(@Named("id") Long busId) {

		List<InvoiceEntity> filteredinvoice = ofy().load()
				.type(InvoiceEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredinvoice;
	}

	@ApiMethod(name = "getInvoicesForTaxCollection", path = "getInvoicesForTaxCollection", httpMethod = HttpMethod.POST)
	public List<InvoiceEntity> getInvoicesForTaxCollection(
			@Named("busId") Long busId, @Named("fromDate") long fromDate,
			@Named("toDate") long toDate) {

		Date date1 = new Date(fromDate);
		Date date2 = new Date(toDate);

		List<InvoiceEntity> filteredInvoices = ofy().load()
				.type(InvoiceEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("isPaid", true).filter("paidDate >=", date1)
				.filter("paidDate <=", date2).list();

		return filteredInvoices;
	}

	@ApiMethod(name = "getTaxReport", path = "getTaxReport", httpMethod = HttpMethod.POST)
	public TaxReport getTaxReport(TaxEntity taxEntity,
			@Named("busId") Long busId, @Named("fromDate") long fromDate,
			@Named("toDate") long toDate) {

		Date date1 = new Date(fromDate);
		Date date2 = new Date(toDate);

		Date beginningDate = EntityUtil.getBeginingOfDay(date1);
		Date endDate = EntityUtil.getEndOfDay(date2);

		TaxReport taxReport = new TaxReport();
		taxReport.setTaxEntity(taxEntity);
		taxReport.setFromDate(date1);
		taxReport.setToDate(date2);
		// fetch all invoices
		// check if it contains given tax? if yes, get list of invoice items
		// do the total of all invoice items
		// if yes, add TaxReportItem
		// else continue
		List<TaxReportItem> taxReportItemList = new ArrayList<TaxReportItem>();

		List<InvoiceEntity> filteredInvoices = ofy().load()
				.type(InvoiceEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("isPaid", true).filter("paidDate >=", beginningDate)
				.filter("paidDate <=", endDate).list();

		for (InvoiceEntity invoiceEntity : filteredInvoices) {

			double productSubTotal = 0;
			double serviceSubTotal = 0;
			double serviceTaxTotal = 0;
			double productTaxTotal = 0;

			TaxReportItem taxReportItem = new TaxReportItem();

			if (invoiceEntity.getServiceLineItemList() == null
					&& invoiceEntity.getServiceLineItemList().isEmpty()) {

				List<StockLineItem> productItemList = invoiceEntity
						.getProductLineItemList();

				if (invoiceEntity.isIndiviualProductLineItemTax()) {
					for (StockLineItem stockLineItem : productItemList) {
						if (stockLineItem.getSelectedTaxItem() != null
								&& stockLineItem.getSelectedTaxItem().getId()
										.equals(taxEntity.getId()))
							productTaxTotal += stockLineItem.getPrice()
									* stockLineItem.getQty()
									* (stockLineItem.getSelectedTaxItem()
											.getTaxPercenatge() / 100);
					}
					taxReportItem.setInvoiceItemNumber(invoiceEntity
							.getItemNumber());
					taxReportItem.setInvoiceDate(invoiceEntity.getPaidDate());
					taxReportItem.setTaxAmt(productTaxTotal);
					taxReportItemList.add(taxReportItem);
				} else {

					if (invoiceEntity.getSelectedProductTax() != null
							&& invoiceEntity.getSelectedProductTax().getId()
									.equals(taxEntity.getId())) {

						for (StockLineItem stockLineItem : productItemList) {
							productSubTotal += (stockLineItem.getQty())
									* (stockLineItem.getPrice());
						}

						productTaxTotal = (taxEntity.getTaxPercenatge() / 100)
								* (productSubTotal - invoiceEntity
										.getDiscAmount());
						taxReportItem.setInvoiceItemNumber(invoiceEntity
								.getItemNumber());
						taxReportItem.setInvoiceDate(invoiceEntity
								.getPaidDate());
						taxReportItem.setTaxAmt(productTaxTotal);

						taxReportItemList.add(taxReportItem);
					}
				}
			} else if (invoiceEntity.getProductLineItemList() == null
					&& invoiceEntity.getProductLineItemList().isEmpty()) {

				List<StockLineItem> serviceItemList = invoiceEntity
						.getProductLineItemList();

				if (invoiceEntity.isIndiviualServiceLineItemTax()) {
					for (StockLineItem stockLineItem : serviceItemList) {
						if (stockLineItem.getSelectedTaxItem() != null
								&& stockLineItem.getSelectedTaxItem().getId()
										.equals(taxEntity.getId()))
							serviceTaxTotal += stockLineItem.getPrice()
									* stockLineItem.getQty()
									* (stockLineItem.getSelectedTaxItem()
											.getTaxPercenatge() / 100);
					}
					taxReportItem.setInvoiceItemNumber(invoiceEntity
							.getItemNumber());
					taxReportItem.setInvoiceDate(invoiceEntity.getPaidDate());
					taxReportItem.setTaxAmt(serviceTaxTotal);
					taxReportItemList.add(taxReportItem);

				} else {

					if (invoiceEntity.getSelectedServiceTax() != null
							&& invoiceEntity.getSelectedServiceTax().getId()
									.equals(taxEntity.getId())) {

						for (StockLineItem stockLineItem : serviceItemList) {
							serviceSubTotal += (stockLineItem.getQty())
									* (stockLineItem.getPrice());
						}

						serviceTaxTotal = (taxEntity.getTaxPercenatge() / 100)
								* (serviceSubTotal - invoiceEntity
										.getDiscAmount());
						taxReportItem.setInvoiceItemNumber(invoiceEntity
								.getItemNumber());
						taxReportItem.setInvoiceDate(invoiceEntity
								.getPaidDate());
						taxReportItem.setTaxAmt(serviceTaxTotal);
						taxReportItemList.add(taxReportItem);

					}

				}

			} else {

				List<StockLineItem> productItemList = invoiceEntity
						.getProductLineItemList();
				List<StockLineItem> serviceItemList = invoiceEntity
						.getServiceLineItemList();

				if (invoiceEntity.getSelectedProductTax() != null
						&& invoiceEntity.getSelectedServiceTax() != null) {

					if (invoiceEntity.getSelectedServiceTax().getId()
							.equals(taxEntity.getId())) {
						for (StockLineItem stockLineItem : serviceItemList) {
							serviceSubTotal += (stockLineItem.getQty())
									* (stockLineItem.getPrice());
						}
						serviceTaxTotal = (taxEntity.getTaxPercenatge() / 100)
								* (serviceSubTotal - invoiceEntity
										.getDiscAmount());
					}
					if (invoiceEntity.getSelectedProductTax().getId()
							.equals(taxEntity.getId())) {
						for (StockLineItem stockLineItem : productItemList) {
							productSubTotal += (stockLineItem.getQty())
									* (stockLineItem.getPrice());
						}
						productTaxTotal = (taxEntity.getTaxPercenatge() / 100)
								* (productSubTotal - invoiceEntity
										.getDiscAmount());

					}
					taxReportItem.setInvoiceItemNumber(invoiceEntity
							.getItemNumber());
					taxReportItem.setInvoiceDate(invoiceEntity.getPaidDate());
					taxReportItem.setTaxAmt(productTaxTotal + serviceTaxTotal);
					taxReportItemList.add(taxReportItem);

				} else {
					if (invoiceEntity.isIndiviualServiceLineItemTax()) {
						for (StockLineItem stockLineItem : serviceItemList) {
							if (stockLineItem.getSelectedTaxItem() != null
									&& stockLineItem.getSelectedTaxItem()
											.getId().equals(taxEntity.getId()))
								serviceTaxTotal += stockLineItem.getPrice()
										* stockLineItem.getQty()
										* (stockLineItem.getSelectedTaxItem()
												.getTaxPercenatge() / 100);
						}

					} else {

						if (invoiceEntity.getSelectedServiceTax() != null
								&& invoiceEntity.getSelectedServiceTax()
										.getId().equals(taxEntity.getId())) {

							for (StockLineItem stockLineItem : serviceItemList) {
								serviceSubTotal += (stockLineItem.getQty())
										* (stockLineItem.getPrice());
							}

							serviceTaxTotal = (taxEntity.getTaxPercenatge() / 100)
									* (serviceSubTotal - invoiceEntity
											.getDiscAmount());

						}

					}
					if (invoiceEntity.isIndiviualProductLineItemTax()) {
						for (StockLineItem stockLineItem : productItemList) {
							if (stockLineItem.getSelectedTaxItem() != null
									&& stockLineItem.getSelectedTaxItem()
											.getId().equals(taxEntity.getId()))
								productTaxTotal += stockLineItem.getPrice()
										* stockLineItem.getQty()
										* (stockLineItem.getSelectedTaxItem()
												.getTaxPercenatge() / 100);
						}
					} else {

						if (invoiceEntity.getSelectedProductTax() != null
								&& invoiceEntity.getSelectedProductTax()
										.getId().equals(taxEntity.getId())) {

							for (StockLineItem stockLineItem : productItemList) {
								productSubTotal += (stockLineItem.getQty())
										* (stockLineItem.getPrice());
							}

							productTaxTotal = (taxEntity.getTaxPercenatge() / 100)
									* (productSubTotal - invoiceEntity
											.getDiscAmount());
						}
					}
					taxReportItem.setInvoiceItemNumber(invoiceEntity
							.getItemNumber());
					taxReportItem.setInvoiceDate(invoiceEntity.getPaidDate());
					taxReportItem.setTaxAmt(productTaxTotal + serviceTaxTotal);

					taxReportItemList.add(taxReportItem);
				}
			}

		}
		taxReport.setItemList(taxReportItemList);
		double taxTotal = 0;
		for (TaxReportItem taxReportItem : taxReportItemList) {
			taxTotal += taxReportItem.getTaxAmt();
		}
		taxReport.setTaxTotal(taxTotal);
		return taxReport;
	}

	@ApiMethod(name = "fetchInvoiceListByPaging", path = "fetchInvoiceListByPaging")
	public EntityPagingInfo fetchInvoiceListByPaging(@Named("id") Long busId,
			@Named("status") String status, EntityPagingInfo pagingInfo) {

		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status
					.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId, InvoiceEntity.class,
					pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId, InvoiceEntity.class,
					pagingInfo);
		}
	}

	@ApiMethod(name = "fetchQuotationListByPaging", path = "fetchQuotationListByPaging")
	public EntityPagingInfo fetchQuotationListByPaging(@Named("id") Long busId,
			@Named("status") String status, EntityPagingInfo pagingInfo) {
		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status
					.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId, QuotationEntity.class,
					pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId, QuotationEntity.class,
					pagingInfo);
		}
	}

	@ApiMethod(name = "getStarredQuotations", path = "getStarredQuotations")
	public List<QuotationEntity> getStarredQuotations(@Named("busId") Long busId) {
		List<QuotationEntity> starredQuotations = ofy().load()
				.type(QuotationEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("starred", true).list();
		return starredQuotations;
	}

	@ApiMethod(name = "getInvoiceByID", path = "getInvoiceByID")
	public InvoiceEntity getInvoiceByID(@Named("busId") Long busId,
			@Named("id") Long invoiceId) {

		List<InvoiceEntity> list = ofy()
				.load()
				.type(InvoiceEntity.class)
				.filterKey(
						Key.create(Key.create(BusinessEntity.class, busId),
								InvoiceEntity.class, invoiceId)).list();
		InvoiceEntity foundInvoice = list.size() > 0 ? list.get(0) : null;
		return foundInvoice;
	}

	@ApiMethod(name = "getInvoiceByItemNumber", path = "getInvoiceByItemNumber")
	public InvoiceEntity getInvoiceByItemNumber(
			@Named("itemNumber") int itemNumber) {
		Object entityByItemNumber = super.getEntityByItemNumber(
				InvoiceEntity.class, itemNumber);
		if (entityByItemNumber != null)
			return (InvoiceEntity) entityByItemNumber;
		else
			return null;
	}

	@ApiMethod(name = "getQuotationByItemNumber", path = "getQuotationByItemNumber")
	public QuotationEntity getQuotationByItemNumber(
			@Named("itemNumber") int itemNumber) {
		Object entityByItemNumber = super.getEntityByItemNumber(
				QuotationEntity.class, itemNumber);
		if (entityByItemNumber != null)
			return (QuotationEntity) entityByItemNumber;
		else
			return null;
	}

	@ApiMethod(name = "getReportByTaxReceived", path = "getReportByTaxReceived")
	public List<InvoiceEntity> getReportByTaxReceived(@Named("id") Long busId) {
		List<InvoiceEntity> filteredInvoice = ofy().load()
				.type(InvoiceEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		List<InvoiceEntity> invList = new ArrayList<InvoiceEntity>();
		return filteredInvoice;
	}

	@ApiMethod(name = "getInvoiceListByCustId", path = "getInvoiceListByCustId")
	public List<InvoiceEntity> getInvoiceListByCustId(@Named("id") Long custId) {
		List<InvoiceEntity> filteredinvoice = ofy()
				.load()
				.type(InvoiceEntity.class)
				.filter("customer",
						Ref.create(Key.create(Customer.class, custId))).list();
		return filteredinvoice;
	}

	/*
	 * ====================================INVOICE
	 * SETTINGS================================================
	 */
	@ApiMethod(name = "addInvoiceSettings")
	public InvoiceSettingsEntity addInvoiceSettings(
			InvoiceSettingsEntity invoiceSettingsEntity) {

		if (invoiceSettingsEntity.getId() == null) {
			invoiceSettingsEntity.setCreatedDate(new Date());
		}
		invoiceSettingsEntity.setModifiedDate(new Date());
		ofy().save().entity(invoiceSettingsEntity).now();
		return invoiceSettingsEntity;
	}

	@ApiMethod(name = "getInvoiceSettingsByBiz", path = "getInvoiceSettingsByBiz")
	public InvoiceSettingsEntity getInvoiceSettingsByBiz(@Named("id") Long busId) {
		InvoiceSettingsEntity filteredSettings = ofy().load()
				.type(InvoiceSettingsEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).first()
				.now();
		return filteredSettings;
	}

	@ApiMethod(name = "addQuotation", path = "addQuotation")
	public QuotationEntity addQuotation(QuotationEntity quotationEntity) {
		return ofy().execute(TxnType.REQUIRED, new Work<QuotationEntity>() {
			private QuotationEntity quotationEntity;

			private Work<QuotationEntity> init(QuotationEntity quotationEntity) {
				this.quotationEntity = quotationEntity;
				return this;
			}

			public QuotationEntity run() {
				ofy().save().entity(quotationEntity).now();
				if (quotationEntity.getStatus() != DocumentStatus.DRAFT) {
					try {
						new EmailHandler().sendQuotationEmail(quotationEntity);
					} catch (MessagingException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return quotationEntity;
			}
		}.init(quotationEntity));
	}

	@ApiMethod(name = "getQuotationByID", path = "getQuotationByID")
	public QuotationEntity getQuotationByID(@Named("busId") Long busId,
			@Named("id") Long quotnId) {

		List<QuotationEntity> list = ofy()
				.load()
				.type(QuotationEntity.class)
				.filterKey(
						Key.create(Key.create(BusinessEntity.class, busId),
								QuotationEntity.class, quotnId)).list();
		QuotationEntity foundQuotation = list.size() > 0 ? list.get(0) : null;
		return foundQuotation;
	}

	@ApiMethod(name = "getAllQuotation")
	public List<QuotationEntity> getAllQuotation(@Named("id") Long busId) {

		List<QuotationEntity> filteredquotation = ofy().load()
				.type(QuotationEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredquotation;

	}

}
