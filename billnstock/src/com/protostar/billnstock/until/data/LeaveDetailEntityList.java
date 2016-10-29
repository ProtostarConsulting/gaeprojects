package com.protostar.billnstock.until.data;

import java.io.Serializable;
import java.util.List;

import com.protostar.billingnstock.hr.entities.LeaveDetailEntity;

public class LeaveDetailEntityList implements Serializable {

	private static final long serialVersionUID = -5926790662677749063L;
	private List<LeaveDetailEntity> list;

	public List<LeaveDetailEntity> getList() {
		return list;
	}

	public void setList(List<LeaveDetailEntity> list) {
		this.list = list;
	}

}
