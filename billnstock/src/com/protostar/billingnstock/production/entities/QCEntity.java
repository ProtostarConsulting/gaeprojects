package com.protostar.billingnstock.production.entities;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants.SchedulingFrequecyType;
import com.protostar.billnstock.until.data.Constants.SchedulingTimeFrequecyType;

public abstract class QCEntity extends BaseEntity {
	@Index
	private String qcName;
	private List<QCParameter> parameterList;
	private Date validFrom;
	private Date validTill;

	private SchedulingFrequecyType schedule;
	private SchedulingTimeFrequecyType scheduleTime;

	private Date startFromTime;
	public Date getStartFromTime() {
		return startFromTime;
	}

	public void setStartFromTime(Date startFromTime) {
		this.startFromTime = startFromTime;
	}

	public Date getTillTime() {
		return tillTime;
	}

	public void setTillTime(Date tillTime) {
		this.tillTime = tillTime;
	}

	private Date tillTime;

	public SchedulingFrequecyType getSchedule() {
		return schedule;
	}

	public void setSchedule(SchedulingFrequecyType schedule) {
		this.schedule = schedule;
	}

	public SchedulingTimeFrequecyType getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(SchedulingTimeFrequecyType scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

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
