package com.protostar.billingnstock.hr.entities;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class EmpSalMaster extends BaseEntity {

	private List<EmpSalaryHead> monthlySalaryHeads = new ArrayList<EmpSalaryHead>();
	@Index
	private Ref<UserEntity> user;

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

	public List<EmpSalaryHead> getMonthlySalaryHeads() {
		return monthlySalaryHeads;
	}

	public void setMonthlySalaryHeads(List<EmpSalaryHead> monthlySalaryHeads) {
		this.monthlySalaryHeads = monthlySalaryHeads;
	}
}
