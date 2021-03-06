package com.protostar.billingnstock.stock.entities;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class StockItemInstanceEntity extends BaseEntity {
	public enum StatusType {
		INSTOCK, SOLD, RETURNED
	}

	@Index
	private String serialNumber;
	@Index
	private String stockItemId;

	@Index
	private StatusType status = StatusType.INSTOCK;
	@Index
	private int invoiceNumber;
	@Index
	private int stockShipmentNumber;
	@Index
	private int stockReceiptNumber;

	
	@Override
	public void beforeSave() {
		if (getStockItemId() == null || getStockItemId().isEmpty()) {
			throw new RuntimeException("StockItem id is not set on: "
					+ this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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

	public String getStockItemId() {
		return stockItemId;
	}

	public void setStockItemId(String stockItemId) {
		this.stockItemId = stockItemId;
	}
	
}
