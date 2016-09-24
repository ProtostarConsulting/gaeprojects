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
import com.protostar.billingnstock.stock.entities.StockItemEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "invoiceService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.stock.services", ownerName = "com.protostar.billingnstock.stock.services", packagePath = ""))
public class InvoiceService {

	@ApiMethod(name = "addInvoice")
	public void addInvoice(InvoiceEntity invoiceEntity) {

		if (invoiceEntity.getId() != null) {

			ReceivableEntity receiveByID = ofy().load()
					.type(ReceivableEntity.class).id(invoiceEntity.getId())
					.now();

			receiveByID.setCustomer(invoiceEntity.getCustomer());
			receiveByID.setFinalTotal(invoiceEntity.getFinalTotal());
			receiveByID.setInvoiceDate(invoiceEntity.getInvoiceDate());
			receiveByID.setInvoiceDueDate(invoiceEntity.getInvoiceDueDate());
			receiveByID.setInvoiceId(invoiceEntity.getId());
			receiveByID.setBusiness(invoiceEntity.getBusiness());
			receiveByID.setCreatedDate(invoiceEntity.getCreatedDate());
			receiveByID.setModifiedDate(invoiceEntity.getModifiedDate());
			receiveByID.setModifiedBy(invoiceEntity.getModifiedBy());
			ofy().save().entity(receiveByID).now();

		} else {
			ReceivableEntity receivableEntity = new ReceivableEntity();

			receivableEntity.setCustomer(invoiceEntity.getCustomer());
			receivableEntity.setFinalTotal(invoiceEntity.getFinalTotal());
			receivableEntity.setInvoiceDate(invoiceEntity.getInvoiceDate());
			receivableEntity.setInvoiceDueDate(invoiceEntity
					.getInvoiceDueDate());
			receivableEntity.setInvoiceId(invoiceEntity.getId());
			receivableEntity.setBusiness(invoiceEntity.getBusiness());
			receivableEntity.setCreatedDate(invoiceEntity.getCreatedDate());
			receivableEntity.setModifiedDate(invoiceEntity.getModifiedDate());
			receivableEntity.setModifiedBy(invoiceEntity.getModifiedBy());
			ofy().save().entity(receivableEntity).now();
		}

		if (invoiceEntity.getId() == null) {
			invoiceEntity.setCreatedDate(new Date());
			// stockItemEntity.setModifiedDate(new Date());
		} else {
			invoiceEntity.setModifiedDate(new Date());
		}

		ofy().save().entity(invoiceEntity).now();

		System.out.println(invoiceEntity.getInvoiceLineItemList());

		List<StockItemEntity> stockItemEntity = ofy().load()
				.type(StockItemEntity.class).list();

		/* For Reduce the Stock Quantity */

		for (int i = 0; i < invoiceEntity.getInvoiceLineItemList().size(); i++) {
			for (int j = 0; j < stockItemEntity.size(); j++) {
				if (invoiceEntity.getInvoiceLineItemList().get(i).getItemName()
						.equals(stockItemEntity.get(j).getItemName())) {
					StockItemEntity a = stockItemEntity.get(j);

					a.setQty((stockItemEntity.get(j).getQty())
							- (Integer.valueOf((invoiceEntity
									.getInvoiceLineItemList().get(i).getQty()))));
					ofy().save().entity(a).now();
				}
			}
		}

		/* For Add in ReceivableEntity */

	}

	@ApiMethod(name = "updateInvoiceStatus")
	public void updateInvoiceStatus(InvoiceEntity valueToUpdateStatus) {

		ofy().save().entity(valueToUpdateStatus).now();

		long invoiceId = valueToUpdateStatus.getId();

		ReceivableEntity fetchedReceivableEntity = ofy().load()
				.type(ReceivableEntity.class).filter("invoiceId = ", invoiceId)
				.first().now();

		if (fetchedReceivableEntity != null) {
			fetchedReceivableEntity.setStatus(valueToUpdateStatus.getStatus());
			ofy().save().entity(fetchedReceivableEntity).now();
		}
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

}
