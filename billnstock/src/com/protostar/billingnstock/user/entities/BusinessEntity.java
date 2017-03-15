package com.protostar.billingnstock.user.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.proadmin.entities.BusinessPlanType;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.until.data.Constants.BusinessStatusType;

@Cache
@Entity
public class BusinessEntity {

	@Id
	private Long id;

	@Index
	private String businessName;
	private String registerDate;
	private Integer totalUser = 0;

	private Address address;
	private String status = "active";
	private BusinessStatusType bizStatus = BusinessStatusType.ACTIVE;
	private String theme;
	@Index
	private String bizLogoGCSURL;
	@Index
	private String footerBlobKey;
	private String disclaimer;
	private String authorizations;

	@Index
	private Date createdDate;
	private Date modifiedDate;
	private String modifiedBy;
	private String note;

	@Index
	private Ref<BusinessPlanType> businessPlan;

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

	public String getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(String authorizations) {
		this.authorizations = authorizations;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getFooterBlobKey() {
		return footerBlobKey;
	}

	public void setFooterBlobKey(String footerBlobKey) {
		this.footerBlobKey = footerBlobKey;
	}

	public String getBizLogoGCSURL() {
		return bizLogoGCSURL;
	}

	public void setBizLogoGCSURL(String url) {
		bizLogoGCSURL = url;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public BusinessPlanType getBusinessPlan() {
		return businessPlan == null ? null : businessPlan.get();
	}

	public void setBusinessPlan(BusinessPlanType businessPlan) {
		if (businessPlan == null)
			return;
		this.businessPlan = Ref.create(businessPlan);
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public Integer getTotalUser() {
		return totalUser;
	}

	public void setTotalUser(Integer totalUser) {
		this.totalUser = totalUser;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Address getAddress() {
		return address == null ? null : address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BusinessStatusType getBizStatus() {
		return bizStatus;
	}

	public void setBizStatus(BusinessStatusType bizStatus) {
		this.bizStatus = bizStatus;
	}

}
