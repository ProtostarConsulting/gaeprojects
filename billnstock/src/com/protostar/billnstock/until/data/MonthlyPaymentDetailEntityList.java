package com.protostar.billnstock.until.data;

import java.io.Serializable;
import java.util.List;

import com.protostar.billingnstock.hr.entities.MonthlyPaymentDetailEntity;

public class MonthlyPaymentDetailEntityList implements Serializable {

	private static final long serialVersionUID = -5926790662677749063L;
	private List<MonthlyPaymentDetailEntity> list;

	public List<MonthlyPaymentDetailEntity> getList() {
		return list;
	}

	public void setList(List<MonthlyPaymentDetailEntity> list) {
		this.list = list;
	}

}
