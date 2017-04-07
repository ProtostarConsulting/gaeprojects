package com.protostar.billingnstock.user.services;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class CurrentUserSession extends BaseEntity {

	// Using created by field as current user
	public UserEntity getUser() {
		return this.getCreatedBy();
	}

	public void setUser(UserEntity user) {
		setCreatedBy(user);
	}

}