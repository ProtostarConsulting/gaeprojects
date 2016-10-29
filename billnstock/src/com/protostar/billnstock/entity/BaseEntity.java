package com.protostar.billnstock.entity;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Entity
public abstract class BaseEntity {
	@Id
	private Long id;
	@Parent
	private Ref<BusinessEntity> business;
	@Index
	private Date createdDate;
	private Date modifiedDate;
	private String modifiedBy;
	private String note;

	public BaseEntity() {
		super();
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
		this.business = Ref.create(business);
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

}