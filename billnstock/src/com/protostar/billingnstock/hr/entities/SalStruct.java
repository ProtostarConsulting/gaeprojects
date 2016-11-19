package com.protostar.billingnstock.hr.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class SalStruct extends BaseEntity {
	@Index
	private Ref<UserEntity> empAccount;
	
	private float monthlyGrossSal = 0F;
	private float monthlyBasic = 0F;
	private float monthlyHra = 0F;
	private float monthlyConvence = 0F;	
	private float monthlyMedical = 0F;
	private float monthlyEducation = 0F;
	private float monthlyAdhocAllow = 0F;
	private float monthlySpecialAllow = 0F;
	
	private float calGrossTotal = 0F;
	private float monthly = 0F;
	private float byearly = 0F;
	private float bmonthly = 0F;
	private float hrayearly = 0F;	
	private float ccayearly = 0F;
	private float ccamonthly = 0F;
	private float ec12Byearly = 0F;
	private float convyearly = 0F;
	private float convmonthly = 0F;
	private float sayearly = 0F;
	private float grandtotal = 0F;
	private float samonthly = 0F;
	private float bgrandtotal = 0F;
	private float ptaxyearly = 0F;
	private float pf1 = 0F;
	private float pf2 = 0F;
	private float ptaxgrandtotal = 0F;
	private float netsalgrandtotalmonthly = 0F;
	private float netsalgrandtotal = 0F;
	private float addprobonus = 0F;
	private float ctc = 0F;
	private float mctc = 0F;
	private String ldother1dis;
	private String ldother2dis;
	private float ldother1amt;
	private float ldother2amt;	
	private float HRA = 0F;
	

	public UserEntity getEmpAccount() {
		return empAccount == null ? null : empAccount.get();
	}

	public void setEmpAccount(UserEntity empAccount) {
		this.empAccount = Ref.create(empAccount);
	}

	public float getMonthlyGrossSal() {
		return monthlyGrossSal;
	}

	public void setMonthlyGrossSal(float grosssal) {
		this.monthlyGrossSal = grosssal;
	}

	public float getMonthly() {
		return monthly;
	}

	public void setMonthly(float monthly) {
		this.monthly = monthly;
	}

	public float getByearly() {
		return byearly;
	}

	public void setByearly(float byearly) {
		this.byearly = byearly;
	}

	public float getBmonthly() {
		return bmonthly;
	}

	public void setBmonthly(float bmonthly) {
		this.bmonthly = bmonthly;
	}

	public float getHrayearly() {
		return hrayearly;
	}

	public void setHrayearly(float hrayearly) {
		this.hrayearly = hrayearly;
	}

	public float getMonthlyHra() {
		return monthlyHra;
	}

	public void setMonthlyHra(float hramonthly) {
		this.monthlyHra = hramonthly;
	}

	public float getCcayearly() {
		return ccayearly;
	}

	public void setCcayearly(float ccayearly) {
		this.ccayearly = ccayearly;
	}

	public float getCcamonthly() {
		return ccamonthly;
	}

	public void setCcamonthly(float ccamonthly) {
		this.ccamonthly = ccamonthly;
	}

	public float getEc12Byearly() {
		return ec12Byearly;
	}

	public void setEc12Byearly(float ec12Byearly) {
		this.ec12Byearly = ec12Byearly;
	}

	public float getConvyearly() {
		return convyearly;
	}

	public void setConvyearly(float convyearly) {
		this.convyearly = convyearly;
	}

	public float getConvmonthly() {
		return convmonthly;
	}

	public void setConvmonthly(float convmonthly) {
		this.convmonthly = convmonthly;
	}

	public float getSayearly() {
		return sayearly;
	}

	public void setSayearly(float sayearly) {
		this.sayearly = sayearly;
	}

	public float getGrandtotal() {
		return grandtotal;
	}

	public void setGrandtotal(float grandtotal) {
		this.grandtotal = grandtotal;
	}

	public float getSamonthly() {
		return samonthly;
	}

	public void setSamonthly(float samonthly) {
		this.samonthly = samonthly;
	}

	public float getBgrandtotal() {
		return bgrandtotal;
	}

	public void setBgrandtotal(float bgrandtotal) {
		this.bgrandtotal = bgrandtotal;
	}

	public float getPtaxyearly() {
		return ptaxyearly;
	}

	public void setPtaxyearly(float ptaxyearly) {
		this.ptaxyearly = ptaxyearly;
	}

	public float getPf1() {
		return pf1;
	}

	public void setPf1(float pf1) {
		this.pf1 = pf1;
	}

	public float getPf2() {
		return pf2;
	}

	public void setPf2(float pf2) {
		this.pf2 = pf2;
	}

	public float getPtaxgrandtotal() {
		return ptaxgrandtotal;
	}

	public void setPtaxgrandtotal(float ptaxgrandtotal) {
		this.ptaxgrandtotal = ptaxgrandtotal;
	}

	public float getNetsalgrandtotalmonthly() {
		return netsalgrandtotalmonthly;
	}

	public void setNetsalgrandtotalmonthly(float netsalgrandtotalmonthly) {
		this.netsalgrandtotalmonthly = netsalgrandtotalmonthly;
	}

	public float getNetsalgrandtotal() {
		return netsalgrandtotal;
	}

	public void setNetsalgrandtotal(float netsalgrandtotal) {
		this.netsalgrandtotal = netsalgrandtotal;
	}

	public float getAddprobonus() {
		return addprobonus;
	}

	public void setAddprobonus(float addprobonus) {
		this.addprobonus = addprobonus;
	}

	public float getCtc() {
		return ctc;
	}

	public void setCtc(float ctc) {
		this.ctc = ctc;
	}

	public float getMctc() {
		return mctc;
	}

	public void setMctc(float mctc) {
		this.mctc = mctc;
	}

	public String getLdother1dis() {
		return ldother1dis;
	}

	public void setLdother1dis(String ldother1dis) {
		this.ldother1dis = ldother1dis;
	}

	public String getLdother2dis() {
		return ldother2dis;
	}

	public void setLdother2dis(String ldother2dis) {
		this.ldother2dis = ldother2dis;
	}

	public float getLdother1amt() {
		return ldother1amt;
	}

	public void setLdother1amt(float ldother1amt) {
		this.ldother1amt = ldother1amt;
	}

	public float getLdother2amt() {
		return ldother2amt;
	}

	public void setLdother2amt(float ldother2amt) {
		this.ldother2amt = ldother2amt;
	}

	public float getMonthlyBasic() {
		return monthlyBasic;
	}

	public void setMonthlyBasic(float basic) {
		this.monthlyBasic = basic;
	}

	public float getHRA() {
		return HRA;
	}

	public void setHRA(float hRA) {
		HRA = hRA;
	}

	public float getMonthlyConvence() {
		return monthlyConvence;
	}

	public void setMonthlyConvence(float convence) {
		this.monthlyConvence = convence;
	}

	public float getCalGrossTotal() {
		return calGrossTotal;
	}

	public void setCalGrossTotal(float calGrossTotal) {
		this.calGrossTotal = calGrossTotal;
	}

	public float getMonthlyMedical() {
		return monthlyMedical;
	}

	public void setMonthlyMedical(float medical) {
		this.monthlyMedical = medical;
	}

	public float getMonthlyEducation() {
		return monthlyEducation;
	}

	public void setMonthlyEducation(float education) {
		this.monthlyEducation = education;
	}

	public float getMonthlyAdhocAllow() {
		return monthlyAdhocAllow;
	}

	public void setMonthlyAdhocAllow(float adhocAllow) {
		this.monthlyAdhocAllow = adhocAllow;
	}

	public float getMonthlySpecialAllow() {
		return monthlySpecialAllow;
	}

	public void setMonthlySpecialAllow(float specialAllow) {
		this.monthlySpecialAllow = specialAllow;
	}

	// private String empid;

	/*
	 * public String getEmpid() { return empid; } public void setEmpid(String
	 * empid) { this.empid = empid; }
	 */
	/*
	 * public String getEmpName() { return empName; } public void
	 * setEmpName(String empName) { this.empName = empName; }
	 */

}
