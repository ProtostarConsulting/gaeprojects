package com.protostar.billingnstock.invoice.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.purchase.entities.SupplierEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class StockItemsReceiptEntity extends BaseEntity {
	@Index
	private int receiptNumber;
	@Index
	private long poNumber;
	private Date receiptDate;
	private List<StockLineItem> productLineItemList = new ArrayList<StockLineItem>();
	private List<StockLineItem> serviceLineItemList = new ArrayList<StockLineItem>();
	// store other costs in serviceLineItems

	private Ref<SupplierEntity> supplier;

	public SupplierEntity getSupplier() {
		return supplier == null ? null : supplier.get();
	}

	public void setSupplier(SupplierEntity supplier) {
		if (supplier != null)
			this.supplier = Ref.create(supplier);
	}

	public int getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(int receiptNumber) {
		this.receiptNumber = receiptNumber;
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

}// end of InvoiceEntity
