package com.protostar.billingnstock.hr.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Cache
@Entity
public class Employee {

	@Id
	private Long id;
	

	private Ref<BusinessEntity> business;
	
	public BusinessEntity getbusiness() {
		return business.get();
	}

	public void setbusiness(BusinessEntity business) {
		this.business = Ref.create(business);
	}

	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	//	private String cust_id;
	private String empName;
	private String email ;
	private String compemail ;
	private String empAddress ;
	private String designation;
	@Index
	private String empid ;
	
	
	public String getEmpid() {
		return empid;
	}
	public void setEmpid(String empid) {
		this.empid = empid;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCompemail() {
		return compemail;
	}
	public void setCompemail(String compemail) {
		this.compemail = compemail;
	}
	public String getEmpAddress() {
		return empAddress;
	}
	public void setEmpAddress(String empAddress) {
		this.empAddress = empAddress;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String Designation) {
		designation = Designation;
	}



	
	

	
	
}

