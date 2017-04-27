package com.protostar.billingnstock.stock.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.user.entities.UserEntity;

abstract class QCRecord {
	public enum QCRecordResultType { PASS, WARN, FAIL }
	
	private Date createdDate;
	private Ref<UserEntity> createdBy;
	private String note;

	private QCRecordResultType qcResult;
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Ref<UserEntity> getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Ref<UserEntity> createdBy) {
		this.createdBy = createdBy;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public QCRecordResultType getQcResult() {
		return qcResult;
	}

	public void setQcResult(QCRecordResultType qcResult) {
		this.qcResult = qcResult;
	}

}
