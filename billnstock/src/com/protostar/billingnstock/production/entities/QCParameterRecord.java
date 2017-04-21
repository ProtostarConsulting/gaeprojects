package com.protostar.billingnstock.production.entities;

public class QCParameterRecord {
	private String parameterName;
	private String recordedValue;
	
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getRecordedValue() {
		return recordedValue;
	}
	public void setRecordedValue(String recordedValue) {
		this.recordedValue = recordedValue;
	}
}