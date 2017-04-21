package com.protostar.billingnstock.production.entities;

import java.util.Date;
import java.util.List;

public class QCTimeParameterValue {
	private Date time;
	private List<QCParameterRecord> paramRecordedValues;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public List<QCParameterRecord> getParamRecordedValues() {
		return paramRecordedValues;
	}

	public void setParamRecordedValues(List<QCParameterRecord> paramRecordedValues) {
		this.paramRecordedValues = paramRecordedValues;
	}

	
}

