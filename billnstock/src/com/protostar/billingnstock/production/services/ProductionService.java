package com.protostar.billingnstock.production.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.protostar.billingnstock.production.entities.BomEntity;

@Api(name = "productionService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.production.services", ownerName = "com.protostar.billingnstock.production.services", packagePath = ""))
public class ProductionService {

	@ApiMethod(name = "addProduction",path="addProduction")
	public void addProduction(BomEntity addProduction) {
		

		ofy().save().entity(addProduction).now();
		System.out.println("saved");

	}


}
