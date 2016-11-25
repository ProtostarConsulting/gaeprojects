package com.protostar.billingnstock.hr.entities;

import javax.persistence.Embedded;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class MonthlyPaymentDetailEntity extends BaseEntity {

	@Index
	private Ref<LeaveDetailEntity> leaveDetailEntity;
	private int totalDays;
	private int payableDays;
	private float monthlyGrossSalary;
	private float overtimeAmt;
	private float calculatedGrossSalary;
	private float specialAllow;
	private String specialAllowNote;
	private float pfDeductionAmt;
	private float ptDeductionAmt;
	private float canteenDeductionAmt;
	private float itDeductionAmt;
	private float otherDeductionAmt;
	private String otherDeductionAmtNote;
	private float netSalaryAmt;

	@Embedded
	private SalStruct salStruct;

	@Index
	private String currentMonth;
	@Index
	private Ref<UserEntity> empAccount;
	private boolean finalized = false;

	public MonthlyPaymentDetailEntity() {
		this.monthlyGrossSalary = 0;
	}

	public MonthlyPaymentDetailEntity(BusinessEntity business,
			LeaveDetailEntity leaveDetailEntity, SalStruct salStruct,
			String currentMonth) {
		setBusiness(business);
		setEmpAccount(salStruct.getEmpAccount());
		this.currentMonth = currentMonth;
		this.leaveDetailEntity = (leaveDetailEntity != null && leaveDetailEntity
				.getId() != null) ? Ref.create(leaveDetailEntity) : null;
		this.salStruct = salStruct;
		this.monthlyGrossSalary = salStruct.getMonthlyGrossSal();
	}

	public UserEntity getEmpAccount() {
		return empAccount == null ? null : empAccount.get();
	}

	public void setEmpAccount(UserEntity empAccount) {
		this.empAccount = Ref.create(empAccount);
	}

	public LeaveDetailEntity getleaveDetailEntity() {
		return leaveDetailEntity != null ? leaveDetailEntity.get() : null;
	}

	public void setleaveDetailEntity(LeaveDetailEntity leaveDetailEntity) {
		this.leaveDetailEntity = (leaveDetailEntity != null && leaveDetailEntity
				.getId() != null) ? Ref.create(leaveDetailEntity) : null;
	}

	public String getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}

	public int getPayableDays() {
		return payableDays;
	}

	public void setPayableDays(int payableDays) {
		this.payableDays = payableDays;
	}

	public float getMonthlyGrossSalary() {
		return monthlyGrossSalary;
	}

	public void setMonthlyGrossSalary(float monthlyGrossSalary) {
		this.monthlyGrossSalary = monthlyGrossSalary;
	}

	public float getCalculatedGrossSalary() {
		return calculatedGrossSalary;
	}

	public void setCalculatedGrossSalary(float calculatedGrossSalary) {
		this.calculatedGrossSalary = calculatedGrossSalary;
	}

	public float getPfDeductionAmt() {
		return pfDeductionAmt;
	}

	public void setPfDeductionAmt(float pfDeductionAmt) {
		this.pfDeductionAmt = pfDeductionAmt;
	}

	public float getPtDeductionAmt() {
		return ptDeductionAmt;
	}

	public void setPtDeductionAmt(float ptDeductionAmt) {
		this.ptDeductionAmt = ptDeductionAmt;
	}

	public float getCanteenDeductionAmt() {
		return canteenDeductionAmt;
	}

	public void setCanteenDeductionAmt(float canteenDeductionAmt) {
		this.canteenDeductionAmt = canteenDeductionAmt;
	}

	public float getItDeductionAmt() {
		return itDeductionAmt;
	}

	public void setItDeductionAmt(float itDeductionAmt) {
		this.itDeductionAmt = itDeductionAmt;
	}

	public float getOtherDeductionAmt() {
		return otherDeductionAmt;
	}

	public void setOtherDeductionAmt(float otherDeductionAmt) {
		this.otherDeductionAmt = otherDeductionAmt;
	}

	public float getNetSalaryAmt() {
		return netSalaryAmt;
	}

	public void setNetSalaryAmt(float netSalaryAmt) {
		this.netSalaryAmt = netSalaryAmt;
	}

	public float getSpecialAllow() {
		return specialAllow;
	}

	public void setSpecialAllow(float specialAllow) {
		this.specialAllow = specialAllow;
	}

	public SalStruct getSalStruct() {
		return salStruct;
	}

	public void setSalStruct(SalStruct salStruct) {
		this.salStruct = salStruct;
	}

	public int getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}

	public String getSpecialAllowNote() {
		return specialAllowNote;
	}

	public void setSpecialAllowNote(String specialAllowNote) {
		this.specialAllowNote = specialAllowNote;
	}

	public String getOtherDeductionAmtNote() {
		return otherDeductionAmtNote;
	}

	public void setOtherDeductionAmtNote(String otherDeductionAmtNote) {
		this.otherDeductionAmtNote = otherDeductionAmtNote;
	}

	public float getOvertimeAmt() {
		return overtimeAmt;
	}

	public void setOvertimeAmt(float overtimeAmt) {
		this.overtimeAmt = overtimeAmt;
	}

	public boolean isFinalized() {
		return finalized;
	}

	public void setFinalized(boolean finalized) {
		this.finalized = finalized;
	}

}
