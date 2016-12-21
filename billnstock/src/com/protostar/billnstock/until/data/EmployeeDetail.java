package com.protostar.billnstock.until.data;

import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.user.entities.EmpDepartment;
import com.protostar.billnstock.entity.Address;

public class EmployeeDetail {

	private String designation;
	private String panCardNumber;
	private String epfNumber;
	private String phone1;
	private String phone2;
	private String personalEmail;
	private int empId;

	private Address address = new Address();
	private BankDetail bankDetail = new BankDetail();
	private Ref<EmpDepartment> department;

	public BankDetail getBankDetail() {
		return bankDetail == null ? null : bankDetail;
	}

	public void setBankDetail(BankDetail bankDetail) {
		this.bankDetail = bankDetail;

	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public EmpDepartment getDepartment() {
		return department == null ? null : department.get();
	}

	public void setDepartment(EmpDepartment department) {
		this.department = Ref.create(department);
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPanCardNumber() {
		return panCardNumber;
	}

	public void setPanCardNumber(String panCardNumber) {
		this.panCardNumber = panCardNumber;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}

	public String getEpfNumber() {
		return epfNumber;
	}

	public void setEpfNumber(String epfNumber) {
		this.epfNumber = epfNumber;
	}

}
