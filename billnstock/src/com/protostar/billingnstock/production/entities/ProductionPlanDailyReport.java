package com.protostar.billingnstock.production.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class ProductionPlanDailyReport extends BaseEntity {

	@JsonBackReference
	@Index
	private Ref<ProductionPlanEntity> prodPlan;

	@Index
	private Ref<BomEntity> bomEntity;

	private Date reportDate;
	private int bomProducedQty;

	public BomEntity getBomEntity() {
		return bomEntity == null ? null : bomEntity.get();
	}

	public void setBomEntity(BomEntity bomEntity) {
		if (bomEntity != null)
			this.bomEntity = Ref.create(bomEntity);

	}

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

	public ProductionPlanEntity getProdPlan() {
		return prodPlan == null ? null : prodPlan.get();
	}

	public void setProdPlan(ProductionPlanEntity prodPlan) {
		if (prodPlan != null)
			this.prodPlan = Ref.create(prodPlan);
	}

}