package com.protostar.billingnstock.crm.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.crm.entities.Opportunity;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "opportunityService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.crm.services", ownerName = "com.protostar.billingnstock.crm.services", packagePath = ""))
public class OpportunityService {

	@ApiMethod(name = "addopportunity")
	public void addopportunity(Opportunity opportunity) {
		opportunity.setCreatedDate(new Date());
		ofy().save().entity(opportunity).now();

	}

	@ApiMethod(name = "updateopportunity")
	public void updateopportunity(Opportunity opportunity) {
		opportunity.setModifiedDate(new Date());
		ofy().save().entity(opportunity).now();

	}

	@ApiMethod(name = "getAllopportunity")
	public List<Opportunity> getAllopportunity(@Named("id") Long id) {
		List<Opportunity> filteredopportunity = ofy().load().type(Opportunity.class)
				.ancestor(Ref.create(Key.create(BusinessEntity.class, id))).list();
		return filteredopportunity;

	}

	@ApiMethod(name = "getopportunityById")
	public Opportunity getopportunityById(@Named("id") Long selectedid) {
		Opportunity opportunity = ofy().load().type(Opportunity.class).id(selectedid).now();
		return opportunity;
	}

}// end of InternetService
