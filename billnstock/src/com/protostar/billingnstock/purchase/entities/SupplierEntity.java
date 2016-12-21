package com.protostar.billingnstock.purchase.entities;

import com.googlecode.objectify.annotation.Entity;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class SupplierEntity extends BaseEntity{

	private String supplierName;
	private String contactFName;
	private String contactLName;
	private String email;
	private String phone1 ;
	private String mobile;
	private Address address;
	
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getContactFName() {
		return contactFName;
	}
	public void setContactFName(String contactFName) {
		this.contactFName = contactFName;
	}
	public String getContactLName() {
		return contactLName;
	}
	public void setContactLName(String contactLName) {
		this.contactLName = contactLName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	

}
