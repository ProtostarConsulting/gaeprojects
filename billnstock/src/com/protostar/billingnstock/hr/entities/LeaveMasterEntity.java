package com.protostar.billingnstock.hr.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class LeaveMasterEntity extends BaseEntity {
	@Index
	private Ref<UserEntity> user;
	private float balance;
	
	@OnSave
	public void beforeSave() {
		super.beforeSave();
		if (getUser() == null) {
			throw new RuntimeException("User entity is not set on: " + this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}
	}

	public UserEntity getUser() {
		return user == null ? null : user.get();
	}

	public void setUser(UserEntity user) {
		this.user = Ref.create(user);
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}
}
