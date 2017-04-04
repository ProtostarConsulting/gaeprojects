package com.protostar.billingnstock.stock.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class StockItemTxnEntity extends BaseEntity {

	public static enum StockTxnType {
		CR, DR
	};

	@Index
	private StockTxnType txnType;
	private int qty;
	private double price;
	@Index
	private Ref<StockItemEntity> stockItem;
	@Index
	private int invoiceNumber;
	@Index
	private int stockShipmentNumber;
	@Index
	private int stockReceiptNumber;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public StockItemEntity getStockItem() {
		return stockItem == null ? null : stockItem.get();
	}

	public void setStockItem(StockItemEntity stockItem) {
		this.stockItem = Ref.create(stockItem);
	}

	public StockTxnType getTxnType() {
		return txnType;
	}

	public void setTxnType(StockTxnType txnType) {
		this.txnType = txnType;
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

}// end of StockServicesEntity
