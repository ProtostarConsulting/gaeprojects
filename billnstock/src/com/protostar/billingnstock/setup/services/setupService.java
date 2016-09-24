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

	@ApiMethod(name = "getCurUserByEmailId", path = "Somepath_realted_to_your_service")
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
	public List<UserEntity> getAllUserOfOrg(@Named("id") Long busId) {
		List<UserEntity> userList = ofy().load().type(UserEntity.class).ancestor(Key.create(BusinessEntity.class, busId)).list();
		/*List<UserEntity> filtereduser = new ArrayList<UserEntity>();

		for (int i = 0; i < user.size(); i++) {
			if (user.get(i).getBusiness().getId().equals(id)) {
				filtereduser.add(user.get(i));
			}
		}*/
		return userList;
	}

	@ApiMethod(name = "getuser")
	public UserEntity getuser(@Named("id") Long id) {
		System.out.println("getuser#id.longValue():" + id.longValue());
		//UserEntity now = ofy().load().type(UserEntity.class).id(id.longValue()).now();
		
		UserEntity now = null;
		List<UserEntity> list = ofy().load().type(UserEntity.class).list();
		for(UserEntity user: list){
			//System.out.println("getuser#user.getId():" + user.getId());
			if(user.getId().longValue() == id.longValue()){
				now = user;
			}
		}
		System.out.println("getuser#now:" + now);
		return now;
	}

	/*
	 * @ApiMethod(name ="adduser") public void adduser(UserEntity user) {
	 * 
	 * Key<UserEntity> now = ofy().save().entity(user).now();
	 * 
	 * }
	 */

}