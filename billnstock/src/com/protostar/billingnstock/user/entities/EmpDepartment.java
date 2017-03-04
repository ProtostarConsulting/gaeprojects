package com.protostar.billingnstock.user.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class EmpDepartment extends BaseEntity {

	@Index
	private Ref<EmpDepartment> parentDept;
	
	@Index
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EmpDepartment getParentDept() {
		return parentDept == null ? null : parentDept.get();
	}

	public void setParentDept(EmpDepartment parentDept) {
		if (parentDept != null)
			this.parentDept = Ref.create(parentDept);
		else
			this.parentDept = null;
	}

}
