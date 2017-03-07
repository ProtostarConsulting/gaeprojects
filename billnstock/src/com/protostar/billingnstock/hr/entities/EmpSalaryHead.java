package com.protostar.billingnstock.hr.entities;

public class EmpSalaryHead {
	private SalaryStructureRule salaryStructureRule;
	private float amount = 0;

	public SalaryStructureRule getSalaryStructureRule() {
		return salaryStructureRule;
	}

	public void setSalaryStructureRule(SalaryStructureRule salaryStructureRule) {
		this.salaryStructureRule = salaryStructureRule;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
}
