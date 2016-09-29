package com.protostar.billingnstock.taskmangement;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.user.entities.UserEntity;

public class Comment {
	private Ref<UserEntity> addedBy;
	private Date date;
	private String commentText;

	public UserEntity getAddedBy() {
		return addedBy.get();
	}

	public void setAddedBy(UserEntity addedBy) {
		this.addedBy = Ref.create(addedBy);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

}
