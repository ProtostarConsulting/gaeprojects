package com.protostar.billingnstock.user.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EmployeeDetail;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;

@Cache
@Entity
public class UserEntity extends BaseEntity {

	private String firstName;
	private String lastName;
	private String fullName;

	@Index
	private Boolean isActive = true;

	@Index
	private String email_id;
	private List<String> authority = new ArrayList<String>();

	private Boolean isGoogleUser = true;
	@Index
	private Boolean isLoginAllowed = true;
	private Date lastLoginDate;
	String authorizations;
	@Index
	private EmployeeDetail employeeDetail = new EmployeeDetail();

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (getId() == null) {
			if (getAuthorizations() == null || getAuthorizations().isEmpty()) {
				if (getAuthority().contains("admin"))
					setAuthorizations(Constants.NEW_BIZ_DEFAULT_AUTHS);
				else
					setAuthorizations(Constants.NEW_BIZ_USER_DEFAULT_AUTHS);
			}
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.EMP_NO_COUNTER);
			getEmployeeDetail().setEmpId(sequenceGenService.getNextSequenceNumber());
		}
	}

	public String getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(String authorizations) {
		this.authorizations = authorizations;
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

	public Boolean getIsLoginAllowed() {
		return isLoginAllowed;
	}

	public void setIsLoginAllowed(Boolean isLoginAllowed) {
		this.isLoginAllowed = isLoginAllowed;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public EmployeeDetail getEmployeeDetail() {
		return employeeDetail;
	}

	public void setEmployeeDetail(EmployeeDetail employeeDetail) {
		this.employeeDetail = employeeDetail;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
