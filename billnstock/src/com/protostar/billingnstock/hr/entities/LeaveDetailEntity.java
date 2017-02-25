package com.protostar.billingnstock.hr.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class LeaveDetailEntity extends BaseEntity {

	@Index
	private Ref<UserEntity> user;
	private float openingBalance;
	private float mothLeave;
	private float takenmothLeave;
	private float withoutpay;
	private float overtimeDays;
	private float nextOpeningBalance;
	@Index
	private String currentMonth;

	public UserEntity getUser() {
		return user == null ? null : user.get();
	}

	public void setUser(UserEntity user) {
		this.user = Ref.create(user);
	}

	public String getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}

	public float getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(float openingBalance) {
		this.openingBalance = openingBalance;
	}

	public float getMothLeave() {
		return mothLeave;
	}

	public void setMothLeave(float mothLeave) {
		this.mothLeave = mothLeave;
	}

	public float getTakenmothLeave() {
		return takenmothLeave;
	}

	public void setTakenmothLeave(float takenmothLeave) {
		this.takenmothLeave = takenmothLeave;
	}

	public float getWithoutpay() {
		return withoutpay;
	}

	public void setWithoutpay(float withoutpay) {
		this.withoutpay = withoutpay;
	}

	public float getNextOpeningBalance() {
		return nextOpeningBalance;
	}

	public void setNextOpeningBalance(float nextOpeningBalance) {
		this.nextOpeningBalance = nextOpeningBalance;
	}

	public float getOvertimeDays() {
		return overtimeDays;
	}

	public void setOvertimeDays(float overtimeDays) {
		this.overtimeDays = overtimeDays;
	}

}
