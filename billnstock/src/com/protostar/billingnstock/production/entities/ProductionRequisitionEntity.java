package com.protostar.billingnstock.production.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.stock.entities.StockLineItemsByCategory;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

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

	private List<StockLineItemsByCategory> catList = new ArrayList<StockLineItemsByCategory>();
	private List<Ref<StockShipmentAgainstProductionRequisition>> stockShipmentList = new ArrayList<Ref<StockShipmentAgainstProductionRequisition>>();

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.PROD_REQUISITION_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public List<StockLineItemsByCategory> getCatList() {
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

	public void setCatList(List<StockLineItemsByCategory> catList) {
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

	public List<StockShipmentAgainstProductionRequisition> getStockShipmentList() {
		if (stockShipmentList == null || stockShipmentList.isEmpty()) {
			return null;
		}
		List<StockShipmentAgainstProductionRequisition> tempList = new ArrayList<StockShipmentAgainstProductionRequisition>(
				stockShipmentList.size());
		for (Ref<StockShipmentAgainstProductionRequisition> entityRef : stockShipmentList) {
			tempList.add(entityRef.get());
		}
		return tempList;
	}

	public void setStockShipmentList(List<StockShipmentAgainstProductionRequisition> stockShipmentList) {
		if (stockShipmentList == null || stockShipmentList.isEmpty()) {
			this.stockShipmentList = null;
			return;
		}

		this.stockShipmentList = new ArrayList<Ref<StockShipmentAgainstProductionRequisition>>(
				stockShipmentList.size());
		for (StockShipmentAgainstProductionRequisition entity : stockShipmentList) {
			this.stockShipmentList.add(Ref.create(entity));
		}
	}

}
