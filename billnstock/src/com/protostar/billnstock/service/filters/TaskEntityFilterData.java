package com.protostar.billnstock.service.filters;

import java.io.Serializable;
import java.util.Date;

import com.protostar.billingnstock.taskmangement.TaskEntity.TaskStatus;
import com.protostar.billingnstock.user.entities.UserEntity;

public class TaskEntityFilterData implements Serializable{

	private static final long serialVersionUID = 3018130194846920766L;
	private Long businessId;
	private Date sinceAssignedDate;
	private TaskStatus taskStatus;
	private UserEntity assignedBy;
	private UserEntity assignedTo;
	

	public TaskStatus getTaskStatus() {
		return taskStatus;
	}

	

	public Date getSinceAssignedDate() {
		return sinceAssignedDate;
	}

	public void setSinceAssignedDate(Date sinceAssignedDate) {
		this.sinceAssignedDate = sinceAssignedDate;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}



	public UserEntity getAssignedBy() {
		return assignedBy;
	}



	public void setAssignedBy(UserEntity assignedBy) {
		this.assignedBy = assignedBy;
	}



	public UserEntity getAssignedTo() {
		return assignedTo;
	}



	public void setAssignedTo(UserEntity assignedTo) {
		this.assignedTo = assignedTo;
	}

}
