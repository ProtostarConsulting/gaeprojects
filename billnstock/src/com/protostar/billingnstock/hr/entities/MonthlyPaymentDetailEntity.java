package com.protostar.billingnstock.hr.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class MonthlyPaymentDetailEntity extends BaseEntity {

	@Index
	private Ref<LeaveDetailEntity> leaveDetailEntity;
	private int payableDays;
	private float monthlyGrossSalary;
	private float calculatedGrossSalary;
	private float specialAllow;
	private float pfDeductionAmt;
	private float ptDeductionAmt;
	private float canteenDeductionAmt;
	private float itDeductionAmt;
	private float otherDeductionAmt;
	private float netSalaryAmt;
	
	private SalStruct salStruct;

	@Index
	private String currentMonth;

	public LeaveDetailEntity getleaveDetailEntity() {
		return leaveDetailEntity == null ? null : leaveDetailEntity.get();
	}

	public void setleaveDetailEntity(LeaveDetailEntity leaveDetailEntity) {
		this.leaveDetailEntity = Ref.create(leaveDetailEntity);

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

}
