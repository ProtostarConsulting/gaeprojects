package com.protostar.billingnstock.stock.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.purchase.entities.SupplierEntity;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Entity
public class StockItemsReceiptEntity extends BaseEntity {

	@Index
	private DocumentStatus status = DocumentStatus.DRAFT;
	@Index
	private boolean isStatusAlreadyFinalized = false;
	@Index
	private long poNumber;
	private double finalTotal;
	private Date receiptDate;
	private List<StockLineItem> productLineItemList = new ArrayList<StockLineItem>();
	private List<StockLineItem> serviceLineItemList = new ArrayList<StockLineItem>();
	// store other costs in serviceLineItems

	private Ref<SupplierEntity> supplier;
	@Index
	private Ref<WarehouseEntity> warehouse;

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (getWarehouse() == null) {
			throw new RuntimeException("Warehouse entity is not set on: "
					+ this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}

		if (this.status == DocumentStatus.FINALIZED) {
			this.isStatusAlreadyFinalized = true;
		}

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()),
					Constants.STOCKRECEIPT_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public WarehouseEntity getWarehouse() {
		return warehouse.get();
	}

	public void setWarehouse(WarehouseEntity warehouse) {
		this.warehouse = Ref.create(warehouse);
	}

	public SupplierEntity getSupplier() {
		return supplier == null ? null : supplier.get();
	}

	public void setSupplier(SupplierEntity supplier) {
		if (supplier != null)
			this.supplier = Ref.create(supplier);
	}

	public List<StockLineItem> getServiceLineItemList() {
		return serviceLineItemList;
	}

	public void setServiceLineItemList(List<StockLineItem> serviceLineItemList) {
		this.serviceLineItemList = serviceLineItemList;
	}

	public long getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(long poNumber) {
		this.poNumber = poNumber;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public List<StockLineItem> getProductLineItemList() {
		return productLineItemList;
	}

	public void setProductLineItemList(List<StockLineItem> productLineItemList) {
		this.productLineItemList = productLineItemList;
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
