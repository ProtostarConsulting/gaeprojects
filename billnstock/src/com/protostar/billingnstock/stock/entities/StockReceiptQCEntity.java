package com.protostar.billingnstock.stock.entities;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billingnstock.production.entities.QCEntity;

@Cache
@Entity
public class StockReceiptQCEntity extends QCEntity {

	private String qcno;

	public String getQcno() {
		return qcno;
	}

	public void setQcno(String qcno) {
		this.qcno = qcno;
	}
}
