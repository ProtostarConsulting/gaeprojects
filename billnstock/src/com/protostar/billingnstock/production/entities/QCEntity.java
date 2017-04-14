package com.protostar.billingnstock.production.entities;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;

public abstract class QCEntity extends BaseEntity {
	@Index
	private String qcName;
	private List<QCParameter> parameterList;
	private Date validFrom; 
	private Date validTill;
	
	public String getQcName() {
		return qcName;
	}
	public void setQcName(String qcName) {
		this.qcName = qcName;
	}
	public List<QCParameter> getParameterList() {
		return parameterList;
	}
	public void setParameterList(List<QCParameter> parameterList) {
		this.parameterList = parameterList;
	}
	public Date getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	public Date getValidTill() {
		return validTill;
	}
	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}
}

