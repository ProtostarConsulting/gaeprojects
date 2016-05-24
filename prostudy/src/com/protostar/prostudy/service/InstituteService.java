package com.protostar.prostudy.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.prostudy.entity.BookEntity;
import com.protostar.prostudy.entity.InstituteEntity;

@Api(name = "instituteService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.prostudy.service", ownerName = "com.protostar.prostudy.service", packagePath = ""))
public class InstituteService {

	@ApiMethod(name = "addInstitute")
	public InstituteEntity addInstitute(InstituteEntity insti) {
		InstituteEntity now = insti;
		ofy().save().entity(insti).now();
		return now;
	}

	@ApiMethod(name = "getInstitutes")
	public List<InstituteEntity> getInstitutes() {
		return ofy().load().type(InstituteEntity.class).list();
	}

	@ApiMethod(name = "getInstituteById")
	public InstituteEntity getInstituteById(@Named("id") Long id) {
		System.out.println("Inside getInstituteById "+id);
		InstituteEntity selected = ofy().load().type(InstituteEntity.class).id(id).now();
		return selected;
	}
	
	@ApiMethod(name = "editInstitute")
	public void editInstitute(InstituteEntity insti) {
		Key<InstituteEntity> now = ofy().save().entity(insti).now();
	}
	
	@ApiMethod(name = "updateInstitute")
	public void updateInstitute(InstituteEntity insti) {
		
		Key<InstituteEntity> now = ofy().save().entity(insti).now();
		
	}

	
	
}
