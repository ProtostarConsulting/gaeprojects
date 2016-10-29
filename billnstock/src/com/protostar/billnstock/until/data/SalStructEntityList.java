package com.protostar.billnstock.until.data;

import java.io.Serializable;
import java.util.List;

import com.protostar.billingnstock.hr.entities.SalStruct;

public class SalStructEntityList implements Serializable {

	private static final long serialVersionUID = -5926790662677749063L;
	private List<SalStruct> list;

	public List<SalStruct> getList() {
		return list;
	}

	public void setList(List<SalStruct> list) {
		this.list = list;
	}

}
