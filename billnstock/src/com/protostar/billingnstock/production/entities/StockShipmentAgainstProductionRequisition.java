package com.protostar.billingnstock.production.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.stock.entities.StockLineItemsByWarehouse;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Cache
@Entity
public class StockShipmentAgainstProductionRequisition extends BaseEntity {

	private List<StockLineItemsByWarehouse> fromWarehouseList = new ArrayList<StockLineItemsByWarehouse>();

	private DocumentStatus status = DocumentStatus.DRAFT;

	private Date deliveryDateTime;

	@Index
	private int reqItemNumber;

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.PROD_STOCKISSUE_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
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

	public int getReqItemNumber() {
		return reqItemNumber;
	}

	public void setReqItemNumber(int reqItemNumber) {
		this.reqItemNumber = reqItemNumber;
	}

	public List<StockLineItemsByWarehouse> getFromWarehouseList() {
		return fromWarehouseList;
	}

	public void setFromWarehouseList(List<StockLineItemsByWarehouse> fromWarehouseList) {
		this.fromWarehouseList = fromWarehouseList;
	}
}
