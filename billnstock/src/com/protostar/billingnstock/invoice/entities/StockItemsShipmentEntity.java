package com.protostar.billingnstock.invoice.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class StockItemsShipmentEntity extends BaseEntity {
	@Index
	private int shipmentNumber;
	private Date shipmentDate;
	private Date deliveredDate;
	private List<StockLineItem> productLineItemList = new ArrayList<StockLineItem>();
	private List<StockLineItem> serviceLineItemList = new ArrayList<StockLineItem>();
	private List<StockLineItem> otherCostsLineItemList = new ArrayList<StockLineItem>();

	private Ref<WarehouseEntity> fromWH;
	private Ref<WarehouseEntity> toWH;
	@Index
	private Ref<Customer> customer;
	@Index
	private long poNumber;

	public int getShipmentNumber() {
		return shipmentNumber;
	}

	public void setShipmentNumber(int shipmentNumber) {
		this.shipmentNumber = shipmentNumber;
	}

	public List<StockLineItem> getProductLineItemList() {
		return productLineItemList;
	}

	public void setProductLineItemList(List<StockLineItem> productLineItemList) {
		this.productLineItemList = productLineItemList;
	}

	public List<StockLineItem> getServiceLineItemList() {
		return serviceLineItemList;
	}

	public void setServiceLineItemList(List<StockLineItem> serviceLineItemList) {
		this.serviceLineItemList = serviceLineItemList;
	}

	public List<StockLineItem> getOtherCostsLineItemList() {
		return otherCostsLineItemList;
	}

	public void setOtherCostsLineItemList(
			List<StockLineItem> otherCostsLineItemList) {
		this.otherCostsLineItemList = otherCostsLineItemList;
	}

	public WarehouseEntity getFromWH() {
		return this.fromWH == null ? null : this.fromWH.get();
	}

	public void setFromWH(WarehouseEntity fromWH) {
		if (fromWH != null)
			this.fromWH = Ref.create(fromWH);
	}

	public WarehouseEntity getToWH() {
		return this.toWH == null ? null : this.toWH.get();
	}

	public void setToWH(WarehouseEntity toWH) {
		if (toWH != null)
			this.toWH = Ref.create(toWH);
	}

	public long getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(long poNumber) {
		this.poNumber = poNumber;
	}

	public Date getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(Date shipmentDate) {
		this.shipmentDate = shipmentDate;
	}

	public Date getDeliveredDate() {
		return deliveredDate;
	}

	public void setDeliveredDate(Date deliveredDate) {
		this.deliveredDate = deliveredDate;
	}

}// end of InvoiceEntity
