package com.protostar.billingnstock.proadmin.entities;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class BusinessPlanType {
	@Id
	private Long id;
	
	@Index
	private String planName;

	private String description;
	private int maxuser;
	private float baseCost; 
	private String paymentDesc;
	
	@Index
	private int itemNumber;	
	@Index
	private Date createdDate;
	private Date modifiedDate;
	private String modifiedBy;	

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getPaymentDesc() {
		return paymentDesc;
	}

	public void setPaymentDesc(String paymentDesc) {
		this.paymentDesc = paymentDesc;
	}

	public int getMaxuser() {
		return maxuser;
	}

	public void setMaxuser(int maxuser) {
		this.maxuser = maxuser;
	}

	public float getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(float baseCost) {
		this.baseCost = baseCost;
	}

}
