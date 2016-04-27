package com.protostar.billingnstock.hr.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.BaseEntity;



@Entity
public class SalStruct extends BaseEntity{

/*	@Id
	private Long id;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}*/
	
	@Index
	private Ref<UserEntity> empAccount;
	/*@Index
	private Ref<BusinessEntity> business;
	
	
	public BusinessEntity getBusiness() {
		return business.get();
	}
	public void setBusiness(BusinessEntity business) {
		this.business = Ref.create(business);
	}*/
	
	public UserEntity getEmpAccount() {
		return empAccount.get();
	}
	
	public void setEmpAccount(UserEntity empAccount) {
			this.empAccount = Ref.create(empAccount);
	}
	
	
	//	private String cust_id;
	private String empName; 
	private Float grosssal; 
	private Float monthly ;
	private Float byearly ;
	private Float bmonthly ;
	private Float hrayearly ;
	private Float hramonthly ;
	private Float ccayearly ;
	private Float ccamonthly ;
	private Float ec12Byearly ;
	private Float convyearly ;
	private Float convmonthly ;
	private Float sayearly ;
	private Float grandtotal; 
	private Float samonthly ;
	private Float bgrandtotal; 
	private Float ptaxyearly ;
	private Float pf1 ;
	private Float pf2 ;
	private Float ptaxgrandtotal ;
	private Float netsalgrandtotalmonthly; 
	private Float netsalgrandtotal ;
	private Float addprobonus ;
	private Float ctc ;
	private Float mctc ;
	private String ldother1dis;
	private String ldother2dis;
	private Float ldother1amt;
	private Float ldother2amt;
	@Index
	private String empid; 

	public Float getPf1() {
		return pf1;
	}
	public void setPf1(Float pf1) {
		this.pf1 = pf1;
	}
	public Float getPf2() {
		return pf2;
	}
	public void setPf2(Float pf2) {
		this.pf2 = pf2;
	}
	public Float getAddprobonus() {
		return addprobonus;
	}
	public void setAddprobonus(Float addprobonus) {
		this.addprobonus = addprobonus;
	}
	public String getEmpid() {
		return empid;
	}
	public void setEmpid(String empid) {
		this.empid = empid;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public Float getGrosssal() {
		return grosssal;
	}
	public void setGrosssal(Float grosssal) {
		this.grosssal = grosssal;
	}
	public Float getMonthly() {
		return monthly;
	}
	public void setMonthly(Float monthly) {
		this.monthly = monthly;
	}
	public Float getByearly() {
		return byearly;
	}
	public void setByearly(Float byearly) {
		this.byearly = byearly;
	}
	public Float getBmonthly() {
		return bmonthly;
	}
	public void setBmonthly(Float bmonthly) {
		this.bmonthly = bmonthly;
	}
	public Float getHRAyearly() {
		return hrayearly;
	}
	public void setHRAyearly(Float hRAyearly) {
		hrayearly = hRAyearly;
	}
	public Float getHRAmonthly() {
		return hramonthly;
	}
	public void setHRAmonthly(Float hRAmonthly) {
		hramonthly = hRAmonthly;
	}
	public Float getCCAyearly() {
		return ccayearly;
	}
	public void setCCAyearly(Float cCAyearly) {
		ccayearly = cCAyearly;
	}
	public Float getCCAmonthly() {
		return ccamonthly;
	}
	public void setCCAmonthly(Float cCAmonthly) {
		ccamonthly = cCAmonthly;
	}
	public Float getEC12Byearly() {
		return ec12Byearly;
	}
	public void setEC12Byearly(Float eC12Byearly) {
		ec12Byearly = eC12Byearly;
	}
	public Float getConvyearly() {
		return convyearly;
	}
	public void setConvyearly(Float convyearly) {
		this.convyearly = convyearly;
	}
	public Float getConvmonthly() {
		return convmonthly;
	}
	public void setConvmonthly(Float convmonthly) {
		this.convmonthly = convmonthly;
	}
	public Float getSAyearly() {
		return sayearly;
	}
	public void setSAyearly(Float sAyearly) {
		sayearly = sAyearly;
	}
	public Float getGrandtotal() {
		return grandtotal;
	}
	public void setGrandtotal(Float grandtotal) {
		this.grandtotal = grandtotal;
	}
	public Float getSAmonthly() {
		return samonthly;
	}
	public void setSAmonthly(Float sAmonthly) {
		samonthly = sAmonthly;
	}
	public Float getBgrandtotal() {
		return bgrandtotal;
	}
	public void setBgrandtotal(Float bgrandtotal) {
		this.bgrandtotal = bgrandtotal;
	}
	public Float getPtaxyearly() {
		return ptaxyearly;
	}
	public void setPtaxyearly(Float ptaxyearly) {
		this.ptaxyearly = ptaxyearly;
	}
	
	public Float getPtaxgrandtotal() {
		return ptaxgrandtotal;
	}
	public void setPtaxgrandtotal(Float ptaxgrandtotal) {
		this.ptaxgrandtotal = ptaxgrandtotal;
	}
	public Float getNetsalgrandtotalmonthly() {
		return netsalgrandtotalmonthly;
	}
	public void setNetsalgrandtotalmonthly(Float netsalgrandtotalmonthly) {
		this.netsalgrandtotalmonthly = netsalgrandtotalmonthly;
	}
	public Float getNetsalgrandtotal() {
		return netsalgrandtotal;
	}
	public void setNetsalgrandtotal(Float netsalgrandtotal) {
		this.netsalgrandtotal = netsalgrandtotal;
	}

	public Float getCTC() {
		return ctc;
	}
	public void setCTC(Float cTC) {
		ctc = cTC;
	}
	public Float getMCTC() {
		return mctc;
	}
	public void setMCTC(Float mCTC) {
		mctc = mCTC;
	}
	public String getLDOther1dis() {
		return ldother1dis;
	}
	public void setLDOther1dis(String lDOther1dis) {
		ldother1dis = lDOther1dis;
	}
	public String getLDOther2dis() {
		return ldother2dis;
	}
	public void setLDOther2dis(String lDOther2dis) {
		ldother2dis = lDOther2dis;
	}
	public Float getLDOther1amt() {
		return ldother1amt;
	}
	public void setLDOther1amt(Float lDOther1amt) {
		ldother1amt = lDOther1amt;
	}
	public Float getLDOther2amt() {
		return ldother2amt;
	}
	public void setLDOther2amt(Float lDOther2amt) {
		ldother2amt = lDOther2amt;
	}

}

