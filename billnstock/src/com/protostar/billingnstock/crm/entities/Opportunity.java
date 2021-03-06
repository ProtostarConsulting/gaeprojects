package com.protostar.billingnstock.crm.entities;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class Opportunity extends BaseEntity{

	private Ref<UserEntity> loggedInUser;

	/*@Id
	private Long id;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}*/

	@Index
	private String oid;
	private String opName;
	private String from;
	private Date date;
	private String description;
	private List<OTask> tasks;

	/*@Index
	private Ref<BusinessEntity> business;
	
	
	public BusinessEntity getBusiness() {
		return business.get();
	}
	public void setBusiness(BusinessEntity business) {
		this.business = Ref.create(business);
	}
	*/
	public UserEntity getLoggedInUser() {
		return loggedInUser.get();
	}

	public void setLoggedInUser(UserEntity loggedInUser) {
		this.loggedInUser = Ref.create(loggedInUser);
	}

	public List<OTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<OTask> tasks) {
		this.tasks = tasks;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}


	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


}
