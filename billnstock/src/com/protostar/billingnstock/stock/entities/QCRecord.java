package com.protostar.billingnstock.stock.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.until.data.Constants.QCRecordResultType;

abstract class QCRecord {
		
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

	public UserEntity getCreatedBy() {
		return createdBy == null ? null : createdBy.get();
	}

	public void setCreatedBy(UserEntity createdBy) {
		this.createdBy = Ref.create(createdBy);
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
