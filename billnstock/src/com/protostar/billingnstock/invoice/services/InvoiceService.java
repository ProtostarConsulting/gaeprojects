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
import com.protostar.billingnstock.stock.services.StockManagementService;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billingnstock.warehouse.services.WarehouseService;
import com.protostar.billnstock.service.BaseService;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EmailHandler;
import com.protostar.billnstock.until.data.EntityPagingInfo;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Api(name = "invoiceService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.stock.services", ownerName = "com.protostar.billingnstock.stock.services", packagePath = ""))
public class InvoiceService extends BaseService {

	@ApiMethod(name = "addInvoice", path = "addInvoice")
	public InvoiceEntity saveInvoice(InvoiceEntity invoiceEntity) throws MessagingException, IOException {
		if (invoiceEntity.getStatus() == DocumentStatus.FINALIZED && invoiceEntity.isStatusAlreadyFinalized()) {
			throw new RuntimeException("Save not allowed. InvoiceEntity is already FINALIZED: "
					+ this.getClass().getSimpleName() + " Finalized entity can't be altered.");
		}

		if (invoiceEntity.getStatus() == DocumentStatus.FINALIZED) {
			StockManagementService.adjustStockItems(invoiceEntity.getBusiness(),
					invoiceEntity.getProductLineItemList());
			new EmailHandler().sendInvoiceEmail(invoiceEntity);
		}
		if (invoiceEntity.getStatus() == DocumentStatus.REJECTED) {
			new EmailHandler().sendInvoiceEmail(invoiceEntity);
		}

		ofy().save().entity(invoiceEntity).now();
		return invoiceEntity;

	}

	@ApiMethod(name = "getAllInvoice", path = "getAllInvoice")
	public List<InvoiceEntity> getAllInvoice(@Named("id") Long busId) {

		List<InvoiceEntity> filteredinvoice = ofy().load().type(InvoiceEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		System.out.println("filteredinvoice:" + filteredinvoice.size());
		return filteredinvoice;

	}

	@ApiMethod(name = "fetchInvoiceListByPaging", path = "fetchInvoiceListByPaging")
	public EntityPagingInfo fetchInvoiceListByPaging(@Named("id") Long busId, @Named("status") String status,
			EntityPagingInfo pagingInfo) {
		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId, InvoiceEntity.class, pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId, InvoiceEntity.class, pagingInfo);
		}
	}

	@ApiMethod(name = "fetchQuotationListByPaging", path = "fetchQuotationListByPaging")
	public EntityPagingInfo fetchQuotationListByPaging(@Named("id") Long busId, @Named("status") String status,
			EntityPagingInfo pagingInfo) {
		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId, QuotationEntity.class, pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId, QuotationEntity.class, pagingInfo);
		}
	}

	@ApiMethod(name = "getInvoiceByID", path = "getInvoiceByID")
	public InvoiceEntity getInvoiceByID(@Named("busId") Long busId, @Named("id") Long invoiceId) {

		List<InvoiceEntity> list = ofy().load().type(InvoiceEntity.class)
				.filterKey(Key.create(Key.create(BusinessEntity.class, busId), InvoiceEntity.class, invoiceId)).list();
		InvoiceEntity foundInvoice = list.size() > 0 ? list.get(0) : null;
		System.out.println("getInvoiceByID Recored is:" + foundInvoice);

		return foundInvoice;
	}

	@ApiMethod(name = "getReportByTaxReceived", path = "getReportByTaxReceived")
	public List<InvoiceEntity> getReportByTaxReceived(@Named("id") Long busId) {

		List<InvoiceEntity> filteredInvoice = ofy().load().type(InvoiceEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		List<InvoiceEntity> invList = new ArrayList<InvoiceEntity>();

		return filteredInvoice;

	}

	@ApiMethod(name = "getInvoiceListByCustId", path = "getInvoiceListByCustId")
	public List<InvoiceEntity> getInvoiceListByCustId(@Named("id") Long custId) {

		List<InvoiceEntity> filteredinvoice = ofy().load().type(InvoiceEntity.class)
				.filter("customer", Ref.create(Key.create(Customer.class, custId))).list();

		return filteredinvoice;
	}

	/*
	 * ====================================INVOICE
	 * SETTINGS================================================
	 */
	@ApiMethod(name = "addInvoiceSettings")
	public InvoiceSettingsEntity addInvoiceSettings(InvoiceSettingsEntity invoiceSettingsEntity) {

		if (invoiceSettingsEntity.getId() == null) {
			invoiceSettingsEntity.setCreatedDate(new Date());
		}
		invoiceSettingsEntity.setModifiedDate(new Date());

		ofy().save().entity(invoiceSettingsEntity).now();
		return invoiceSettingsEntity;
	}

	@ApiMethod(name = "getInvoiceSettingsByBiz", path = "getInvoiceSettingsByBiz")
	public InvoiceSettingsEntity getInvoiceSettingsByBiz(@Named("id") Long busId) {

		InvoiceSettingsEntity filteredSettings = ofy().load().type(InvoiceSettingsEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).first().now();

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
				return quotationEntity;
			}
		}.init(quotationEntity));
	}

	@ApiMethod(name = "getQuotationByID", path = "getQuotationByID")
	public QuotationEntity getQuotationByID(@Named("busId") Long busId, @Named("id") Long quotnId) {

		List<QuotationEntity> list = ofy().load().type(QuotationEntity.class)
				.filterKey(Key.create(Key.create(BusinessEntity.class, busId), QuotationEntity.class, quotnId)).list();
		QuotationEntity foundQuotation = list.size() > 0 ? list.get(0) : null;
		System.out.println("getQuotationByID Record is:" + foundQuotation);

		return foundQuotation;
	}

	@ApiMethod(name = "getAllQuotation")
	public List<QuotationEntity> getAllQuotation(@Named("id") Long busId) {

		List<QuotationEntity> filteredquotation = ofy().load().type(QuotationEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredquotation;

	}

}
