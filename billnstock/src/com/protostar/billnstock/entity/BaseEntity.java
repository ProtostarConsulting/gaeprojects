package com.protostar.billnstock.entity;

import java.util.Calendar;
import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import com.googlecode.objectify.annotation.Parent;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;

public abstract class BaseEntity {
	@Id
	private Long id;
	@Index
	private int itemNumber;

	@Parent
	private Ref<BusinessEntity> business;
	@Index
	private Date createdDate;
	private Date modifiedDate;
	private String modifiedBy;
	private String note;

	@Index
	private Ref<UserEntity> createdBy;
	@Index
	private Ref<UserEntity> approvedBy;

	@Index
	private boolean isDeleted = false;

	@Index
	private boolean starred = false;

	public BaseEntity() {
		super();
	}

	@OnSave
	public void beforeSave() {
		if (getBusiness() == null) {
			throw new RuntimeException("Business entity is not set on: "
					+ this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.YEAR, -1); 
		Date dummyDate = cal.getTime();
		if (getId() == null) {
			setCreatedDate(new Date());
			setModifiedDate(new Date());
		} else {
			setModifiedDate(new Date());
		}

	}

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

	public BusinessEntity getBusiness() {
		return business == null ? null : business.get();
	}

	public void setBusiness(BusinessEntity business) {
		if (business != null)
			this.business = Ref.create(business);
	}

	public UserEntity getCreatedBy() {
		return createdBy == null ? null : createdBy.get();
	}

	public void setCreatedBy(UserEntity createdBy) {
		if (createdBy != null)
			this.createdBy = Ref.create(createdBy);
	}

	public UserEntity getApprovedBy() {
		return approvedBy == null ? null : approvedBy.get();
	}

	public void setApprovedBy(UserEntity approvedBy) {
		if (approvedBy != null)
			this.approvedBy = Ref.create(approvedBy);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isStarred() {
		return starred;
	}

	public void setStarred(boolean starred) {
		this.starred = starred;
	}
}