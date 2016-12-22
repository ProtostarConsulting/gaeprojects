package com.protostar.billingnstock.purchase.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Api(name = "purchaseOrderService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.purchase.services", ownerName = "com.protostar.billingnstock.purchase.services", packagePath = ""))
public class PurchaseOrderService {

	@ApiMethod(name = "addPurchaseOrder", path = "addPurchaseOrder")
	public PurchaseOrderEntity addPurchaseOrder(
			PurchaseOrderEntity purchaseOrderEntity) {

		if (purchaseOrderEntity.getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(purchaseOrderEntity
							.getBusiness()),
					Constants.PURCHASE_ORDER_NO_COUNTER);
			purchaseOrderEntity.setItemNumber(sequenceGenService
					.getNextSequenceNumber());
		}

		ofy().save().entity(purchaseOrderEntity).now();
		return purchaseOrderEntity;
	}

	@ApiMethod(name = "getAllPurchaseOrder", path = "getAllPurchaseOrder")
	public List<PurchaseOrderEntity> getAllPurchaseOrder(@Named("id") Long busId) {
		List<PurchaseOrderEntity> filteredPO = ofy().load()
				.type(PurchaseOrderEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredPO;
	}

	@ApiMethod(name = "fetchPOByID", path = "fetchPOByID")
	public PurchaseOrderEntity fetchPOByID(@Named("itemNumber") int itemNumber) {
		List<PurchaseOrderEntity> list = ofy().load()
				.type(PurchaseOrderEntity.class)
				.filter("itemNumber", itemNumber).list();
		return list.size() > 0 ? list.get(0) : null;
	}

	@ApiMethod(name = "getPOByID", path = "getPOByID")
	public PurchaseOrderEntity getPOByID(@Named("id") Long id) {

		PurchaseOrderEntity POById = ofy().load()
				.type(PurchaseOrderEntity.class).id(id).now();
		return POById;
	}
}
