package com.protostar.billingnstock.hr.entities;

import com.protostar.billnstock.until.data.Constants.SalaryHeadType;

public class SalaryStructureRule {
	private int orderNumber;
	private String headName;
	private SalaryHeadType headType = SalaryHeadType.PERCENTAGE;
	private float fixedValue = 0;
	private float percentageValue = 0;
	private String percentageOfHeadName;
	private boolean active = true;

	public SalaryHeadType getHeadType() {
		return headType;
	}

	public void setHeadType(SalaryHeadType headType) {
		this.headType = headType;
	}

	public float getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(float fixedValue) {
		this.fixedValue = fixedValue;
	}

	public float getPercentageValue() {
		return percentageValue;
	}

	public void setPercentageValue(float percentageValue) {
		this.percentageValue = percentageValue;
	}

	public String getHeadName() {
		return headName;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}

	public String getPercentageOfHeadName() {
		return percentageOfHeadName;
	}

	public void setPercentageOfHeadName(String percentageOfHeadName) {
		this.percentageOfHeadName = percentageOfHeadName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

}
