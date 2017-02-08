package com.protostar.billingnstock.stock.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Entity
public class StockItemsShipmentEntity extends BaseEntity {

	public enum ShipmentType {
		TO_CUSTOMER, TO_OTHER_WAREHOUSE, TO_PARTNER
	};

	@Index
	private ShipmentType shipmentType = ShipmentType.TO_CUSTOMER;

	@Index
	private DocumentStatus status = DocumentStatus.DRAFT;
	@Index
	private boolean isStatusAlreadyFinalized = false;
	private double finalTotal;
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

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (this.getStatus() == DocumentStatus.FINALIZED) {
			this.setStatusAlreadyFinalized(true);
		}

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()),
					Constants.STOCKSHIPMENT_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public Customer getCustomer() {
		return customer == null ? null : customer.get();
	}

	public void setCustomer(Customer customer) {
		if (customer != null)
			this.customer = Ref.create(customer);
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

	public ShipmentType getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(ShipmentType shipmentType) {
		this.shipmentType = shipmentType;
	}

	public DocumentStatus getStatus() {
		return status;
	}

	public void setStatus(DocumentStatus status) {
		this.status = status;
	}

	public boolean isStatusAlreadyFinalized() {
		return isStatusAlreadyFinalized;
	}

	public void setStatusAlreadyFinalized(boolean isStatusAlreadyFinalized) {
		this.isStatusAlreadyFinalized = isStatusAlreadyFinalized;
	}

	public double getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(double finalTotal) {
		this.finalTotal = finalTotal;
	}

}// end of InvoiceEntity
