package com.protostar.billingnstock.hr.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class LeaveDetailEntity extends BaseEntity {

	private int openingBalance;
	private Ref<UserEntity> user;

	public UserEntity getUser() {
		return user == null ? null : user.get();
	}

	public void setUser(UserEntity user) {
		this.user = Ref.create(user);

	}

	private int mothLeave;
	private int takenmothLeave;
	private int withoutpay;
	private int nextOpeningBalance;
	@Index
	private String currentMonth;

	public String getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}

	public int getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(int openingBalance) {
		this.openingBalance = openingBalance;
	}

	public int getMothLeave() {
		return mothLeave;
	}

	public void setMothLeave(int mothLeave) {
		this.mothLeave = mothLeave;
	}

	public int getTakenmothLeave() {
		return takenmothLeave;
	}

	public void setTakenmothLeave(int takenmothLeave) {
		this.takenmothLeave = takenmothLeave;
	}

	public int getWithoutpay() {
		return withoutpay;
	}

	public void setWithoutpay(int withoutpay) {
		this.withoutpay = withoutpay;
	}

	public int getNextOpeningBalance() {
		return nextOpeningBalance;
	}

	public void setNextOpeningBalance(int nextOpeningBalance) {
		this.nextOpeningBalance = nextOpeningBalance;
	}

}
