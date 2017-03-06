package com.protostar.billingnstock.hr.entities;

import java.util.ArrayList;
import java.util.List;

import com.protostar.billnstock.until.data.Constants.SalaryHeadType;

public class HREntityUtil {

	public static List<SalaryStructureRule> getStandardMonthlySalaryStructureRules() {
		List<SalaryStructureRule> salaryHeads = new ArrayList<SalaryStructureRule>();
		SalaryStructureRule monthlyGross = new SalaryStructureRule();
		monthlyGross.setHeadName("Monthly Gross");
		monthlyGross.setHeadType(SalaryHeadType.FIXED);
		salaryHeads.add(monthlyGross);

		SalaryStructureRule monthlyBasic = new SalaryStructureRule();
		monthlyBasic.setHeadName("Monthly Basic");
		monthlyBasic.setHeadType(SalaryHeadType.PERCENTAGE);
		monthlyBasic.setPercentageValue(40f);
		monthlyBasic.setPercentageOfHeadName(monthlyGross.getHeadName());
		salaryHeads.add(monthlyBasic);

		SalaryStructureRule monthlyHRA = new SalaryStructureRule();
		monthlyHRA.setHeadName("HRA");
		monthlyHRA.setHeadType(SalaryHeadType.PERCENTAGE);
		monthlyHRA.setPercentageValue(50f);
		monthlyHRA.setPercentageOfHeadName(monthlyBasic.getHeadName());
		salaryHeads.add(monthlyHRA);

		SalaryStructureRule monthlyConveyance = new SalaryStructureRule();
		monthlyConveyance.setHeadName("Conveyance");
		monthlyConveyance.setHeadType(SalaryHeadType.FIXED);
		monthlyConveyance.setPercentageValue(1600f);
		salaryHeads.add(monthlyConveyance);

		SalaryStructureRule monthlyMedical = new SalaryStructureRule();
		monthlyMedical.setHeadName("Medical");
		monthlyMedical.setHeadType(SalaryHeadType.FIXED);
		monthlyMedical.setPercentageValue(1250f);
		salaryHeads.add(monthlyMedical);

		SalaryStructureRule monthlyEducation = new SalaryStructureRule();
		monthlyEducation.setHeadName("Education");
		monthlyEducation.setHeadType(SalaryHeadType.FIXED);
		monthlyEducation.setPercentageValue(200f);
		salaryHeads.add(monthlyEducation);

		SalaryStructureRule monthlyAdhocAllow = new SalaryStructureRule();
		monthlyAdhocAllow.setHeadName("Adhoc Allowance");
		monthlyAdhocAllow.setHeadType(SalaryHeadType.FIXED);
		monthlyAdhocAllow.setPercentageValue(0f);
		salaryHeads.add(monthlyAdhocAllow);

		SalaryStructureRule monthlySpecialAllow = new SalaryStructureRule();
		monthlySpecialAllow.setHeadName("Special Allowance");
		monthlySpecialAllow.setHeadType(SalaryHeadType.FIXED);
		monthlySpecialAllow.setPercentageValue(0f);
		salaryHeads.add(monthlySpecialAllow);

		return salaryHeads;
	}

	public static List<SalaryStructureRule> getStandardMonthlySalaryDeductionRules() {
		List<SalaryStructureRule> deductionHeads = new ArrayList<SalaryStructureRule>();

		SalaryStructureRule monthlyProvidentFund = new SalaryStructureRule();
		monthlyProvidentFund.setHeadName("Provident Fund");
		monthlyProvidentFund.setHeadType(SalaryHeadType.PERCENTAGE);
		monthlyProvidentFund.setPercentageValue(12f);
		monthlyProvidentFund.setPercentageOfHeadName("Monthly Basic");
		deductionHeads.add(monthlyProvidentFund);

		SalaryStructureRule monthlyProfessionalTax = new SalaryStructureRule();
		monthlyProfessionalTax.setHeadName("Professional Tax");
		monthlyProfessionalTax.setHeadType(SalaryHeadType.FIXED);
		monthlyProfessionalTax.setFixedValue(200f);
		monthlyProfessionalTax.setPercentageOfHeadName("Monthly Basic");
		deductionHeads.add(monthlyProfessionalTax);

		SalaryStructureRule monthlyIncomeTax = new SalaryStructureRule();
		monthlyIncomeTax.setHeadName("Income Tax");
		monthlyIncomeTax.setHeadType(SalaryHeadType.PERCENTAGE);
		monthlyIncomeTax.setPercentageValue(20f);
		monthlyIncomeTax.setPercentageOfHeadName("Monthly Basic");
		deductionHeads.add(monthlyIncomeTax);

		SalaryStructureRule monthlyCanteen = new SalaryStructureRule();
		monthlyCanteen.setHeadName("Canteen");
		monthlyCanteen.setHeadType(SalaryHeadType.FIXED);
		monthlyCanteen.setFixedValue(2000f);
		deductionHeads.add(monthlyCanteen);

		SalaryStructureRule monthlyOtherDeduction = new SalaryStructureRule();
		monthlyOtherDeduction.setHeadName("Other Deduction");
		monthlyOtherDeduction.setHeadType(SalaryHeadType.PERCENTAGE);
		monthlyOtherDeduction.setPercentageValue(10f);
		monthlyOtherDeduction.setPercentageOfHeadName("Monthly Basic");
		deductionHeads.add(monthlyOtherDeduction);

		return deductionHeads;
	}

}
