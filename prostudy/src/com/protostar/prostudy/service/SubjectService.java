package com.protostar.prostudy.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;

import com.protostar.prostudy.entity.SubjectEntity;

@Api(name = "subjectService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.prostudy.service", ownerName = "com.protostar.prostudy.service", packagePath = ""))
public class SubjectService {

	@ApiMethod(name = "addSubject")
	public void addSubject(SubjectEntity sub) {
		Key<SubjectEntity> now = ofy().save().entity(sub).now();
	}

	@ApiMethod(name = "getSubject")
	public List<SubjectEntity> getStandard() {
		return ofy().load().type(SubjectEntity.class).list();
	}

	@ApiMethod(name = "getSubjectByDivision")
	 public List<SubjectEntity> getSubjectByDivision(@Named("divisionID") Long divisionID) {
		System.out.println("inside getSubjectByDivision");
	  List<SubjectEntity> divisionList = ofy().load().type(SubjectEntity.class).filter("divisionID", divisionID).list();
	  return divisionList;
	  
	 }
}