package com.protostar.billingnstock.production.entities;

public class QCParameterRecord {
	private QCParameter qcParameter;
	private String recordedValue;

	public String getRecordedValue() {
		return recordedValue;
	}

	public void setRecordedValue(String recordedValue) {
		this.recordedValue = recordedValue;
	}

	public QCParameter getQcParameter() {
		return qcParameter;
	}

	public void setQcParameter(QCParameter qcParameter) {
		this.qcParameter = qcParameter;
	}
}