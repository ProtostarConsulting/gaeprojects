package com.protostar.billingnstock.production.entities;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class ProductionPlanDailyReport extends BaseEntity {

	@Index
	private int prodPlanItemNumber;

	private Date reportDate;
	private int bomProducedQty;

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public int getBomProducedQty() {
		return bomProducedQty;
	}

	public void setBomProducedQty(int bomProducedQty) {
		this.bomProducedQty = bomProducedQty;
	}

	public int getProdPlanItemNumber() {
		return prodPlanItemNumber;
	}

	public void setProdPlanItemNumber(int prodPlanItemNumber) {
		this.prodPlanItemNumber = prodPlanItemNumber;
	}

}