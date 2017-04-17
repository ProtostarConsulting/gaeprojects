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
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "productionService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.production.services", ownerName = "com.protostar.billingnstock.production.services", packagePath = ""))
public class ProductionService {

	@ApiMethod(name = "addProduction",path="addProduction")
	public void addProduction(BomEntity addProduction) {
		

		ofy().save().entity(addProduction).now();
		System.out.println("saved");

	}
	
	
	@ApiMethod(name = "listProduction",path="listProduction")
	public List<BomEntity> listProduction(@Named("bid") Long busId) {

		List<BomEntity> bomEntityList = ofy()
				.load()
				.type(BomEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.list();
				return bomEntityList;
		
		
	}


}
