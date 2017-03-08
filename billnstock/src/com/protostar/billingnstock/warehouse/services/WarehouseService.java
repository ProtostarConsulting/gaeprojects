package com.protostar.billingnstock.warehouse.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billnstock.until.data.Constants;

@Api(name = "warehouseManagementService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.warehouse.entities", ownerName = "com.protostar.billingnstock.warehouse.services", packagePath = ""))
public class WarehouseService {

	@ApiMethod(name = "addWarehouse")
	public WarehouseEntity addWarehouse(WarehouseEntity warehouseEntity) {

		if (warehouseEntity.getId() == null) {
			warehouseEntity.setCreatedDate(new Date());
			// stockItemEntity.setModifiedDate(new Date());
		} else {
			warehouseEntity.setModifiedDate(new Date());
		}
		ofy().save().entity(warehouseEntity).now();
		return warehouseEntity;
	}

	@ApiMethod(name = "getWarehouseById")
	public WarehouseEntity getWarehouseById(@Named("id") Long id) {
		WarehouseEntity warehouseEntity2 = ofy().load().type(WarehouseEntity.class).id(id).now();
		return warehouseEntity2;
	}

	@ApiMethod(name = "getAllWarehouseByBusiness")
	public List<WarehouseEntity> getAllWarehouseByBusiness(@Named("id") Long busId) {
		List<WarehouseEntity> filteredWarehouses = ofy().load().type(WarehouseEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredWarehouses;
	}

	@ApiMethod(name = "updateWarehouse")
	public void updateWarehouse(WarehouseEntity warehouseEntity) {
		ofy().save().entity(warehouseEntity).now();
	}

	@ApiMethod(name = "getDefaultWarehouseByBizId", path = "getDefaultWarehouseByBizId")
	public WarehouseEntity getDefaultWarehouseByBizId(@Named("id") Long busId) {
		WarehouseEntity filteredWarehouses = ofy().load().type(WarehouseEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("warehouseName", Constants.DEFAULT_STOCK_WAREHOUSE).first().now();

		return filteredWarehouses;
	}
}
