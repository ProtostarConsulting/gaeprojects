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
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.invoice.entities.InvoiceSettingsEntity;
import com.protostar.billingnstock.invoice.entities.QuotationEntity;
import com.protostar.billingnstock.stock.services.StockItemService;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Api(name = "invoiceService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.stock.services", ownerName = "com.protostar.billingnstock.stock.services", packagePath = ""))
public class InvoiceService {

	@ApiMethod(name = "addInvoice", path = "addInvoice")
	public void saveInvoice(InvoiceEntity invoiceEntity) {

		if (invoiceEntity.getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(invoiceEntity.getBusiness()),
					Constants.INVOICE_NO_COUNTER);
			invoiceEntity.setItemNumber(sequenceGenService
					.getNextSequenceNumber());
		}

		StockItemService.adjustStockItems(invoiceEntity.getBusiness(),
				invoiceEntity.getProductLineItemList());

		ofy().save().entity(invoiceEntity).now();

	}

	@ApiMethod(name = "getAllInvoice")
	public List<InvoiceEntity> getAllInvoice(@Named("id") Long busId) {

		List<InvoiceEntity> filteredinvoice = ofy().load()
				.type(InvoiceEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		System.out.println("filteredinvoice:" + filteredinvoice.size());
		return filteredinvoice;

	}

	@ApiMethod(name = "getinvoiceByID")
	public InvoiceEntity getinvoiceByID(@Named("id") Long invoiceId) {

		/*
		 * InvoiceEntity invoiceByID = ofy().load().type(InvoiceEntity.class)
		 * .id(invoiceId).now();
		 */
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
	public void addQuotation(QuotationEntity quotationEntity) {

		if (quotationEntity.getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(quotationEntity.getBusiness()),
					Constants.QUOTATION_NO_COUNTER);
			quotationEntity.setItemNumber(sequenceGenService
					.getNextSequenceNumber());
		}

		ofy().save().entity(quotationEntity).now();

	}

	@ApiMethod(name = "getAllQuotation")
	public List<QuotationEntity> getAllQuotation(@Named("id") Long busId) {

		List<QuotationEntity> filteredquotation = ofy().load()
				.type(QuotationEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredquotation;

	}

}
