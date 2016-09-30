package com.protostar.billingnstock.taskmangement;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class TaskEntity extends BaseEntity {
	public enum TaskStatus {
		OPEN, INPROGRESS, COMPLETED
	};

	@Index
	private Long taskNo;
	@Index
	private TaskStatus taskStatus = TaskStatus.OPEN;
	@Index
	private Ref<UserEntity> assignedBy;
	@Index
	private Ref<UserEntity> assignedTo;

	private Date assignedDate;
	private Date estCompletionDate;
	private String taskTitle;
	private String taskDesc;
	private List<Comment> taskComments;

	public Long getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(Long taskNo) {
		this.taskNo = taskNo;
	}

	public UserEntity getAssignedBy() {
		return assignedBy == null ? null : assignedBy.get();
	}

	public void setAssignedBy(UserEntity assignedBy) {
		this.assignedBy = Ref.create(assignedBy);
	}

	public UserEntity getAssignedTo() {
		return assignedTo == null ? null : assignedTo.get();
	}

	public void setAssignedTo(UserEntity assignedTo) {
		this.assignedTo = Ref.create(assignedTo);
	}

	public Date getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}

	public Date getEstCompletionDate() {
		return estCompletionDate;
	}

	public void setEstCompletionDate(Date estCompletionDate) {
		this.estCompletionDate = estCompletionDate;
	}

	public String getTaskTitle() {
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public TaskStatus getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}

	public List<Comment> getTaskComments() {
		return taskComments;
	}

	public void setTaskComments(List<Comment> taskComments) {
		this.taskComments = taskComments;
	}

}