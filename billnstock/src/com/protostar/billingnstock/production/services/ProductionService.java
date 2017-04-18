package com.protostar.billingnstock.production.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.account.entities.AccountEntryEntity;
import com.protostar.billingnstock.production.entities.BomEntity;
import com.protostar.billingnstock.production.entities.ProductionMachineEntity;
import com.protostar.billingnstock.production.entities.QCMachineEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "productionService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.production.services", ownerName = "com.protostar.billingnstock.production.services", packagePath = ""))
public class ProductionService {

	@ApiMethod(name = "addBomEntity", path = "addBomEntity")
	public void addProduction(BomEntity addProduction) {

		ofy().save().entity(addProduction).now();
		System.out.println("saved");

	}

	@ApiMethod(name = "listBomEntity", path = "listBomEntity")
	public List<BomEntity> listProduction(@Named("bid") Long busId) {

		List<BomEntity> bomEntityList = ofy().load().type(BomEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return bomEntityList;

	}

	@ApiMethod(name = "addMachine", path = "addMachine")
	public ProductionMachineEntity addMachine(ProductionMachineEntity machine) {
		ofy().save().entity(machine).now();		
		return machine;
	}
	
	@ApiMethod(name = "getMachineList", path = "getMachineList")
	public List<ProductionMachineEntity> getMachineList(@Named("busId") Long busId) {
		List<ProductionMachineEntity> machineList =  ofy().load().type(ProductionMachineEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		
		return machineList;
	}
	
	@ApiMethod(name = "addQCMachine", path = "addQCMachine")
	public QCMachineEntity addQCMachine(QCMachineEntity machine) {
		ofy().save().entity(machine).now();		
		return machine;
	}

	@ApiMethod(name = "getQCMachineList", path = "getQCMachineList")
	public List<QCMachineEntity> getQCMachineList(@Named("busId") Long busId) {
		List<QCMachineEntity> machineList =  ofy().load().type(QCMachineEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		
		return machineList;
	}
}
