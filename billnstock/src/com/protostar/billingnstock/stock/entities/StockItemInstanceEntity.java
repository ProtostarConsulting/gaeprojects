package com.protostar.billingnstock.stock.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class StockItemInstanceEntity extends BaseEntity {
	@Index
	private String serialNumber;

	private Ref<StockItemEntity> stockItem;

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public StockItemEntity getStockItem() {
		return stockItem.get();
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
}
