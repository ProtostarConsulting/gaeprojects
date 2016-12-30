package com.protostar.billingnstock.hr.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class LeaveAppEntity extends BaseEntity {

	@Index
	private Ref<UserEntity> user;
	@Index
	private Ref<UserEntity> manager;
	
	private Date startDate;
	private Date endDate;
	private boolean halfDay = false;
	private float totalDays;
	private boolean approved = false;
	private Date approvedDate;

	public UserEntity getUser() {
		return user == null ? null : user.get();
	}

	public void setUser(UserEntity user) {
		this.user = Ref.create(user);
	}

	public UserEntity getManager() {
		return manager == null ? null : manager.get();
	}

	public void setManager(UserEntity manager) {
		this.manager = Ref.create(manager);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public boolean isHalfDay() {
		return halfDay;
	}

	public void setHalfDay(boolean halfDay) {
		this.halfDay = halfDay;
	}

	public float getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(float totalDays) {
		this.totalDays = totalDays;
	}

}
