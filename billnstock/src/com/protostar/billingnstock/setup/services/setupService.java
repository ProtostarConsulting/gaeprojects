package com.protostar.billingnstock.setup.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;

//import com.protostar.prostudy.entity.BookEntity;

@Api(name = "setupService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.setup.services", ownerName = "com.protostar.billingnstock.setup.services", packagePath = ""))
public class setupService {

	@ApiMethod(name = "getCurUserByEmailId", path="getCurUserByEmailId")
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
		System.out.println("Biz id:" + id);
		List<UserEntity> list = ofy().load().type(UserEntity.class).ancestor(Key.create(BusinessEntity.class, id)).list();
		System.out.println("list.size:" + list.size());
		return list;
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