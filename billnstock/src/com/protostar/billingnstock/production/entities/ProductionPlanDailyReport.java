package com.protostar.billingnstock.production.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;

@Cache
@Entity
public class ProductionPlanDailyReport extends BaseEntity {

	private int prodPlanItemNumber;

	@Index
	private Ref<BomEntity> bomEntity;

	private DocumentStatus status = DocumentStatus.DRAFT;

	private Date reportDate;
	private int bomProducedQty;

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.PROD_PLAN_DAILYERPORT_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

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

	public int getProdPlanItemNumber() {
		return prodPlanItemNumber;
	}

	public void setProdPlanItemNumber(int prodPlanItemNumber) {
		this.prodPlanItemNumber = prodPlanItemNumber;
	}

	public DocumentStatus getStatus() {
		return status;
	}

	public void setStatus(DocumentStatus status) {
		this.status = status;
	}

}