package com.protostar.billingnstock.stock.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class StockItemInstanceEntity extends BaseEntity {
	public enum StatusType {
		INSTOCK, SOLD, RETURNED
	}

	@Index
	private String serialNumber;
	@Index
	private Ref<StockItemEntity> stockItem;

	@Index
	private StatusType status = StatusType.INSTOCK;
	@Index
	private int invoiceNumber;
	@Index
	private int stockShipmentNumber;
	@Index
	private int stockReceiptNumber;

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public StockItemEntity getStockItem() {
		return stockItem == null ? null : stockItem.get();
	}

	public void setStockItem(StockItemEntity stockItem) {
		this.stockItem = Ref.create(stockItem);
	}

	@OnSave
	public void beforeSave() {
		if (getStockItem() == null) {
			throw new RuntimeException("StockItem entity is not set on: "
					+ this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public int getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(int invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public int getStockShipmentNumber() {
		return stockShipmentNumber;
	}

	public void setStockShipmentNumber(int stockShipmentNumber) {
		this.stockShipmentNumber = stockShipmentNumber;
	}

	public int getStockReceiptNumber() {
		return stockReceiptNumber;
	}

	public void setStockReceiptNumber(int stockReceiptNumber) {
		this.stockReceiptNumber = stockReceiptNumber;
	}
}
