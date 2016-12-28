package com.protostar.billingnstock.stock.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.OnSave;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class StockItemEntity extends BaseEntity {
	private int qty;
	private double price;
	private double cost;
	private double movingAvgCost;
	private String notes;
	private int thresholdValue;
	private String section;
	private String rack;
	private String slot;
	private boolean maintainStock = true;
	private boolean maintainStockBySerialNumber = false;

	private Ref<WarehouseEntity> warehouse;
	private Ref<StockItemTypeEntity> stockItemType;

	@OnSave
	public void beforeSave() {
		if (getWarehouse() == null) {
			throw new RuntimeException("Warehouse entity is not set on: "
					+ this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}
		if (getStockItemType() == null) {
			throw new RuntimeException("StockItemType entity is not set on: "
					+ this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}
	}

	public WarehouseEntity getWarehouse() {
		return warehouse.get();
	}

	public void setWarehouse(WarehouseEntity warehouse) {
		this.warehouse = Ref.create(warehouse);
	}

	public StockItemTypeEntity getStockItemType() {
		return this.stockItemType.get();
	}

	public void setStockItemType(StockItemTypeEntity stockItemType) {
		this.stockItemType = Ref.create(stockItemType);
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getThresholdValue() {
		return thresholdValue;
	}

	public void setThresholdValue(int thresholdValue) {
		this.thresholdValue = thresholdValue;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getRack() {
		return rack;
	}

	public void setRack(String rack) {
		this.rack = rack;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public boolean isMaintainStock() {
		return maintainStock;
	}

	public void setMaintainStock(boolean maintainStock) {
		this.maintainStock = maintainStock;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getMovingAvgCost() {
		return movingAvgCost;
	}

	public void setMovingAvgCost(double movingAvgCost) {
		this.movingAvgCost = movingAvgCost;
	}

	public boolean isMaintainStockBySerialNumber() {
		return maintainStockBySerialNumber;
	}

	public void setMaintainStockBySerialNumber(boolean maintainStockBySerialNumber) {
		this.maintainStockBySerialNumber = maintainStockBySerialNumber;
	}
}// end of StockServicesEntity
