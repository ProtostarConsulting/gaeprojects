package com.protostar.prostudy.gf.service;
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.protostar.prostudy.gf.entity.GFStudentEntity;
import com.protostar.prostudy.until.data.UtilityService;

@Api(name = "gfStudentService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.prostudy.gf.service", ownerName = "com.protostar.prostudy.gf.service", packagePath = ""))
public class GFStudentService {

	@ApiMethod(name="addGFStudent",path="addGFStudent")
	public void addGFStudent(GFStudentEntity gfStudentEntity){
		
		String nextPRN = UtilityService.getNextPRN(gfStudentEntity.getRole());
		gfStudentEntity.setPrn(nextPRN);
		
		 ofy().save().entity(gfStudentEntity).now();
		 
	}
	
	@ApiMethod(name="getGFStudentsByInstitute", path="getGFStudentsByInstitute")
	public List<GFStudentEntity> getGFStudentsByInstitute (@ Named("instituteID") long instituteID){
		
		List<GFStudentEntity> list = ofy().load().type(GFStudentEntity.class).list();
		
		return list;
		
	}
	
	@ApiMethod(name="getGFStudentById", path="getGFStudentById")
	public GFStudentEntity getGFStudentById (@ Named("id") long studID){
		
		GFStudentEntity stud = ofy().load().type(GFStudentEntity.class).id(studID).now();
		
		return stud;
		
	}
}
