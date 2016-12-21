package com.protostar.billingnstock.stock.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class StockItemTxnEntity extends BaseEntity {

	public static enum StockTxnType {
		CR, DR
	};

	private StockTxnType txnType;
	private int qty;
	private double price;	
	private Ref<StockItemEntity> stockItem;	

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
}// end of StockServicesEntity
