package com.protostar.billingnstock.setup.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.sun.corba.se.spi.servicecontext.UEInfoServiceContext;

//import com.protostar.prostudy.entity.BookEntity;

@Api(name = "setupService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.setup.services", ownerName = "com.protostar.billingnstock.setup.services", packagePath = ""))
public class setupService {

	@ApiMethod(name = "getCurUserByEmailId", path="Somepath_realted_to_your_service")
	public List<UserEntity> getCurUserByEmailId(@Named("email_id") String email) {
		List<UserEntity> list = ofy().load().type(UserEntity.class)
				.filter("email_id", email).list();
		return list;
	}

	@ApiMethod(name = "updateBusiness")
	public BusinessEntity updateBusiness(BusinessEntity business) {
		Key<BusinessEntity> now = ofy().save().entity(business).now();
		return business;
	}
	
	@ApiMethod(name = "updateUserStatus")
	public void updateUserStatus(UserEntity userEntity) {
	
		ofy().save().entity(userEntity).now();

	}

	@ApiMethod(name = "getAllUserOfOrg")
	public List<UserEntity> getAllUserOfOrg(@Named("id") Long id) {
		List<UserEntity> user = ofy().load().type(UserEntity.class).list();
		List<UserEntity> filtereduser = new ArrayList<UserEntity>();

		for (int i = 0; i < user.size(); i++) {
			if (user.get(i).getBusiness().getId().equals(id)) {
						filtereduser.add(user.get(i));
			} 
		}
		return filtereduser;
	}
	
	@ApiMethod(name = "getuser")
	public UserEntity getuser(@Named("id") Long id) {
			return ofy().load().type(UserEntity.class).id(id).now();
		}
	
	/*@ApiMethod(name ="adduser")	
	public void adduser(UserEntity user) {
		
		Key<UserEntity> now = ofy().save().entity(user).now();

	}
	*/
	

}