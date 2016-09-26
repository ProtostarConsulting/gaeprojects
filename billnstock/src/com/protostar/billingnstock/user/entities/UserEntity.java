package com.protostar.billingnstock.user.entities;

import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.BankDetail;

@Entity
public class UserEntity extends BaseEntity {

	private String firstName;
	private String lastName;

	private String status = "active";

	@Index
	private String email_id;
	private List<String> authority;

	private Boolean isGoogleUser = true;

	/* private Address address; */
	private BankDetail bankDetail;

	private String authorizations;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(String authorizations) {
		this.authorizations = authorizations;
	}

	public BankDetail getBankDetail() {
		return bankDetail == null ? null : bankDetail;
	}

	public void setBankDetail(BankDetail bankDetail) {
		this.bankDetail = bankDetail;

	}

	public Boolean getIsGoogleUser() {
		return isGoogleUser;
	}

	public void setIsGoogleUser(Boolean isGoogleUser) {
		this.isGoogleUser = isGoogleUser;
	}

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getAuthority() {
		return authority;
	}

	public void setAuthority(List<String> authority) {
		this.authority = authority;
	}

	/*
	 * public void setId(Long id) { this.id = id; }
	 */

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/*
	 * public Long getId() { return id; }
	 */

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	/*
	 * public BusinessEntity getBusiness() { return business.get(); }
	 * 
	 * public void setBusiness(BusinessEntity business) { this.business =
	 * Ref.create(business); }
	 */

	/*
	 * public Address getAddress() { return address; }
	 * 
	 * public void setAddress(Address address) { this.address = address; }
	 */

}
