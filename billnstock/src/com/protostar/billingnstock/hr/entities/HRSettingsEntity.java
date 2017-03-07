package com.protostar.billingnstock.hr.entities;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class HRSettingsEntity extends BaseEntity {

	private List<SalaryStructureRule> monthlySalaryStructureRules = new ArrayList<SalaryStructureRule>();
	private List<SalaryStructureRule> monthlySalaryDeductionRules = new ArrayList<SalaryStructureRule>();

	private boolean emailNotification = false;
	private String emailNotificationDL = "";

	public boolean isEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	public String getEmailNotificationDL() {
		return emailNotificationDL;
	}

	public void setEmailNotificationDL(String emailNotificationDL) {
		this.emailNotificationDL = emailNotificationDL;
	}

	public List<SalaryStructureRule> getMonthlySalaryStructureRules() {
		return monthlySalaryStructureRules;
	}

	public void setMonthlySalaryStructureRules(
			List<SalaryStructureRule> monthlySalaryStructureRules) {
		this.monthlySalaryStructureRules = monthlySalaryStructureRules;
	}

	public List<SalaryStructureRule> getMonthlySalaryDeductionRules() {
		return monthlySalaryDeductionRules;
	}

	public void setMonthlySalaryDeductionRules(
			List<SalaryStructureRule> monthlySalaryDeductionRules) {
		this.monthlySalaryDeductionRules = monthlySalaryDeductionRules;
	}
}
