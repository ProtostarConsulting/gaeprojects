package com.protostar.billingnstock.crm.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Cache
@Entity
public class Contact extends BaseEntity {

	private Ref<UserEntity> loggedInUser;
	@Index
	private Ref<Customer> customer;

	private String fName;
	private String lName;
	private String status;
	private Long phone;
	@Index
	private String email;
	private String uid;
	private String supp;
	private String cust;
	private String salespartner;

	private Address address;

	@Override
	public void beforeSave() {
		super.beforeSave();
		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.CONTACT_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public Address getAddress() {
		return address == null ? null : address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public UserEntity getLoggedInUser() {
		return loggedInUser == null ? null : loggedInUser.get();
	}

	public void setLoggedInUser(UserEntity loggedInUser) {
		this.loggedInUser = Ref.create(loggedInUser);
	}

	public Customer getCustomer() {
		return customer == null ? null : customer.get();
	}

	public void setCustomer(Customer customer) {
		this.customer = customer == null ? null : Ref.create(customer);
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSupp() {
		return supp;
	}

	public void setSupp(String supp) {
		this.supp = supp;
	}

	public String getCust() {
		return cust;
	}

	public void setCust(String cust) {
		this.cust = cust;
	}

	public String getSalespartner() {
		return salespartner;
	}

	public void setSalespartner(String salespartner) {
		this.salespartner = salespartner;
	}

}
