package com.protostar.billingnstock.invoice.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.account.entities.ReceivableEntity;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.hr.entities.SalStruct;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.invoice.entities.InvoiceSettingsEntity;
import com.protostar.billingnstock.invoice.entities.QuotationEntity;
import com.protostar.billingnstock.stock.entities.StockItemEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "quotationService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.stock.services", ownerName = "com.protostar.billingnstock.stock.services", packagePath = ""))
public class QuotationService {

	@ApiMethod(name = "addQuotation")
	public void addInvoice(QuotationEntity quotationEntity) {

		if (quotationEntity.getId() != null) {

			ReceivableEntity receiveByID = ofy().load()
					.type(ReceivableEntity.class).id(quotationEntity.getId())
					.now();

			receiveByID.setCustomer(quotationEntity.getCustomer());
			receiveByID.setFinalTotal(quotationEntity.getFinalTotal());
			receiveByID.setQuotationDate(quotationEntity.getQuotationDate());
			receiveByID.setQuotationDueDate(quotationEntity.getQuotationDueDate());
			receiveByID.setInvoiceId(quotationEntity.getId());
			receiveByID.setBusiness(quotationEntity.getBusiness());
			receiveByID.setCreatedDate(quotationEntity.getCreatedDate());
			receiveByID.setModifiedDate(quotationEntity.getModifiedDate());
			receiveByID.setModifiedBy(quotationEntity.getModifiedBy());
			ofy().save().entity(receiveByID).now();

		} else {
			ReceivableEntity receivableEntity = new ReceivableEntity();

			receivableEntity.setCustomer(quotationEntity.getCustomer());
			receivableEntity.setFinalTotal(quotationEntity.getFinalTotal());
			receivableEntity.setQuotationDate(quotationEntity.getQuotationDate());
			receivableEntity.setQuotationDueDate(quotationEntity.getQuotationDueDate());
			receivableEntity.setInvoiceId(quotationEntity.getId());
			receivableEntity.setBusiness(quotationEntity.getBusiness());
			receivableEntity.setCreatedDate(quotationEntity.getCreatedDate());
			receivableEntity.setModifiedDate(quotationEntity.getModifiedDate());
			receivableEntity.setModifiedBy(quotationEntity.getModifiedBy());
			ofy().save().entity(receivableEntity).now();
		}

		if (quotationEntity.getId() == null) {
			quotationEntity.setCreatedDate(new Date());
			// stockItemEntity.setModifiedDate(new Date());
		} else {
			quotationEntity.setModifiedDate(new Date());
		}

		ofy().save().entity(quotationEntity).now();

		System.out.println(quotationEntity.getInvoiceLineItemList());

		List<StockItemEntity> stockItemEntity = ofy().load()
				.type(StockItemEntity.class).list();

		/* For Reduce the Stock Quantity */

		for (int i = 0; i < quotationEntity.getInvoiceLineItemList().size(); i++) {
			for (int j = 0; j < stockItemEntity.size(); j++) {
				if (quotationEntity.getInvoiceLineItemList().get(i).getItemName()
						.equals(stockItemEntity.get(j).getItemName())) {
					StockItemEntity a = stockItemEntity.get(j);

					a.setQty((stockItemEntity.get(j).getQty())
							- (Integer.valueOf((quotationEntity
									.getInvoiceLineItemList().get(i).getQty()))));
					ofy().save().entity(a).now();
				}
			}
		}

		/* For Add in ReceivableEntity */

	}

	

	@ApiMethod(name = "getAllQuotation")
	public List<QuotationEntity> getAllQuotation(@Named("id") Long busId) {

		List<QuotationEntity> filteredquotation = ofy().load()
				.type(QuotationEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		System.out.println("filteredquotation:" + filteredquotation.size());
		return filteredquotation;

	}

	@ApiMethod(name = "getquotationByID")
	public QuotationEntity getquotationByID(@Named("id") Long quotationId) {
		QuotationEntity foundQuotation = null;
		List<QuotationEntity> list = ofy().load().type(QuotationEntity.class)
				.list();
		for (QuotationEntity quotation : list) {
			if (quotation.getId().longValue() == quotationId.longValue()) {
				foundQuotation = quotation;
			}
		}
		System.out.println("getquotationByID Recored is:" + foundQuotation);

		return foundQuotation;
	
	}
}

		/*
		 * InvoiceEntity invoiceByID = ofy().load().type(InvoiceEntity.class)
		 * .id(invoiceId).now();
		
		InvoiceEntity foundInvoice = null;
		List<InvoiceEntity> list = ofy().load().type(InvoiceEntity.class)
				.list();
		for (InvoiceEntity invoice : list) {
			if (invoice.getId().longValue() == invoiceId.longValue()) {
				foundInvoice = invoice;
			}
		}
		System.out.println("getinvoiceByID Recored is:" + foundInvoice);

		return foundInvoice;
	}


/*	@ApiMethod(name = "getReportByTaxReceived", path = "getReportByTaxReceived")
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
*/
	/*
	 * ====================================INVOICE
	 * SETTINGS================================================
	 */
	/*@ApiMethod(name = "addInvoiceSettings")
	public InvoiceSettingsEntity addInvoiceSettings(
			InvoiceSettingsEntity invoiceSettingsEntity) {

		if (invoiceSettingsEntity.getId() == null) {
			invoiceSettingsEntity.setCreatedDate(new Date());
			invoiceSettingsEntity.setModifiedDate(new Date());
		} else {
			invoiceSettingsEntity.setModifiedDate(new Date());
		}
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
*/

