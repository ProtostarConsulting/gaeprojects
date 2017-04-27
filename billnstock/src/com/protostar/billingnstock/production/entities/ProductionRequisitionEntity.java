package com.protostar.billingnstock.production.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.stock.entities.BomLineItemCategory;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;

@Cache
@Entity
public class ProductionRequisitionEntity extends BaseEntity {

	@Index
	private Ref<BomEntity> bomEntity;
	private int productQty;
	private Date deliveryDateTime;

	private DocumentStatus status = DocumentStatus.DRAFT;

	@Index
	private int prodPlanItemNumber;

	private List<BomLineItemCategory> catList = new ArrayList<BomLineItemCategory>();

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.PROD_REQUISITION_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public List<BomLineItemCategory> getCatList() {
		return catList;
	}

	public BomEntity getBomEntity() {
		return bomEntity == null ? null : bomEntity.get();
	}

	public void setBomEntity(BomEntity bomEntity) {
		if (bomEntity != null)
			this.bomEntity = Ref.create(bomEntity);

	}

	public int getProductQty() {
		return productQty;
	}

	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}

	public void setCatList(List<BomLineItemCategory> catList) {
		this.catList = catList;
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

	public Date getDeliveryDateTime() {
		return deliveryDateTime;
	}

	public void setDeliveryDateTime(Date deliveryDateTime) {
		this.deliveryDateTime = deliveryDateTime;
	}

}