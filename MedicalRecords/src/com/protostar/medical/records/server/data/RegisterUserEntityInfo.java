package com.protostar.medical.records.server.data;


public class RegisterUserEntityInfo {

	

	private long id;
	
	private String firstName;

	private String lastName;
	
	private String emailID;
	
	private Number mobileNumber;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public Number getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Number mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	
}
