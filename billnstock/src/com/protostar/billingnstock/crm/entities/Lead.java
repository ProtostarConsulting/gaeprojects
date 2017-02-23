package com.protostar.billingnstock.crm.entities;

import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Entity
public class Lead extends Contact{

	private Ref<UserEntity> loggedInUser;

/*	@Id
	private Long id;
*/
	@Index
	private String lid;
/*	@Index
	private Ref<BusinessEntity> business;
	
	
	public BusinessEntity getBusiness() {
		return business.get();
	}
	public void setBusiness(BusinessEntity business) {
		this.business = Ref.create(business);
	}*/
	
	private String company;
	private Long phone;
	private String email;
	private String designation;
	/*private String address;*/
	
	@Override
	public void beforeSave() {
		super.beforeSave();
		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()),
					Constants.LEAD_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	private List<Task> tasks;

	private String name;

	/*public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
*/
	

	public UserEntity getLoggedInUser() {
		return loggedInUser.get();
	}

	public void setLoggedInUser(UserEntity loggedInUser) {
		this.loggedInUser = Ref.create(loggedInUser);
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/*public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
*/
}
