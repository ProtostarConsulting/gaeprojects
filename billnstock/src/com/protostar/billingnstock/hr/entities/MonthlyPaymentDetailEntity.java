package com.protostar.billingnstock.hr.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity

public class MonthlyPaymentDetailEntity extends BaseEntity{
	
	Ref <LeaveDetailEntity> leaveDetailEntity;		
	private int payableDays;
	private int monthlyGrossSalary;
	private int calculatedGrossSalary;
	private int pfDeductionAmt;
	private int ptDeductionAmt;
	private int canteenDeductionAmt;
	private int itDeductionAmt;
	private int otherDeductionAmt;
	private int netSalaryAmt;
	
	
	@Index
	private String currentMonth;
	
	public LeaveDetailEntity getleaveDetailEntity() {
		return leaveDetailEntity.get();
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
	public int getMonthlyGrossSalary() {
		return monthlyGrossSalary;
	}
	public void setMonthlyGrossSalary(int monthlyGrossSalary) {
		this.monthlyGrossSalary = monthlyGrossSalary;
	}
	public int getCalculatedGrossSalary() {
		return calculatedGrossSalary;
	}
	public void setCalculatedGrossSalary(int calculatedGrossSalary) {
		this.calculatedGrossSalary = calculatedGrossSalary;
	}
	public int getPfDeductionAmt() {
		return pfDeductionAmt;
	}
	public void setPfDeductionAmt(int pfDeductionAmt) {
		this.pfDeductionAmt = pfDeductionAmt;
	}
	public int getPtDeductionAmt() {
		return ptDeductionAmt;
	}
	public void setPtDeductionAmt(int ptDeductionAmt) {
		this.ptDeductionAmt = ptDeductionAmt;
	}
	public int getCanteenDeductionAmt() {
		return canteenDeductionAmt;
	}
	public void setCanteenDeductionAmt(int canteenDeductionAmt) {
		this.canteenDeductionAmt = canteenDeductionAmt;
	}
	public int getItDeductionAmt() {
		return itDeductionAmt;
	}
	public void setItDeductionAmt(int itDeductionAmt) {
		this.itDeductionAmt = itDeductionAmt;
	}
	public int getOtherDeductionAmt() {
		return otherDeductionAmt;
	}
	public void setOtherDeductionAmt(int otherDeductionAmt) {
		this.otherDeductionAmt = otherDeductionAmt;
	}
	public int getNetSalaryAmt() {
		return netSalaryAmt;
	}
	public void setNetSalaryAmt(int netSalaryAmt) {
		this.netSalaryAmt = netSalaryAmt;
	}

}
