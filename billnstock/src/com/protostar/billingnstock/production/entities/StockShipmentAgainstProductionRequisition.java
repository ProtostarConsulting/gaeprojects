package com.protostar.billingnstock.production.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.stock.entities.StockLineItemsByWarehouse;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;
import com.protostar.billnstock.until.data.WebUtil;

@Cache
@Entity
public class StockShipmentAgainstProductionRequisition extends StockItemsShipmentEntity {

	private List<StockLineItemsByWarehouse> fromWarehouseList = new ArrayList<StockLineItemsByWarehouse>();

	private DocumentStatus status = DocumentStatus.DRAFT;

	private Date deliveryDateTime;

	@Index
	private int reqItemNumber;

	@Override
	public void beforeSave() {
		// super.beforeSave();

		if (getBusiness() == null) {
			throw new RuntimeException("Business entity is not set on: " + this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}
		/*
		 * Calendar cal = Calendar.getInstance(); Date today = cal.getTime();
		 * cal.add(Calendar.YEAR, -1); Date dummyDate = cal.getTime();
		 */
		if (getId() == null) {
			setCreatedDate(new Date());
			if (WebUtil.getCurrentUser() != null)
				setCreatedBy(WebUtil.getCurrentUser().getUser());
		} else {
			setModifiedDate(new Date());
			if (WebUtil.getCurrentUser() != null)
				setModifiedByUser(WebUtil.getCurrentUser().getUser());
		}

		if (this.getStatus() == DocumentStatus.FINALIZED) {
			this.setStatusAlreadyFinalized(true);
		}

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
