package com.protostar.billingnstock.stock.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class StockItemEntity extends BaseEntity {

	@Index
	private String itemName;
	@Index
	private long stockItemNumber;
	
	@Index
	private String category;
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

	private Ref<WarehouseEntity> warehouse;
	private Ref<TaxEntity> selectedTaxItem;

	public TaxEntity getSelectedTaxItem() {
		return selectedTaxItem == null ? null : selectedTaxItem.get();
	}

	public void setSelectedTaxItem(TaxEntity selectedTaxItem) {
		this.selectedTaxItem = Ref.create(selectedTaxItem);
	}

	public WarehouseEntity getWarehouse() {
		return warehouse == null ? null : warehouse.get();
	}

	public void setWarehouse(WarehouseEntity warehouse) {
		this.warehouse = warehouse == null ? null : Ref.create(warehouse);
	}

	public String getCategory() {
		return category;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public void setCategory(String category) {
		this.category = category;
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

	public long getStockItemNumber() {
		return stockItemNumber;
	}

	public void setStockItemNumber(long stockItemNumber) {
		this.stockItemNumber = stockItemNumber;
	}

	public double getMovingAvgCost() {
		return movingAvgCost;
	}

	public void setMovingAvgCost(double movingAvgCost) {
		this.movingAvgCost = movingAvgCost;
	}

}// end of StockServicesEntity
