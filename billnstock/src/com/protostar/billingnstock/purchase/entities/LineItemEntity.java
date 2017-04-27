package com.protostar.billingnstock.purchase.entities;

import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.stock.entities.StockItemTypeEntity;

public class LineItemEntity {

	private String itemName;
	private double price;
	private String qty;
	private double currentBudgetBalance;

	private Ref<StockItemTypeEntity> stockItemType;

	public StockItemTypeEntity getStockItemType() {
		if (this.stockItemType == null)
			return null;

		return this.stockItemType.get();
	}

	public void setStockItemType(StockItemTypeEntity stockItemType) {
		if (stockItemType != null)
			this.stockItemType = Ref.create(stockItemType);
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public double getCurrentBudgetBalance() {
		return currentBudgetBalance;
	}

	public void setCurrentBudgetBalance(double currentBudgetBalance) {
		this.currentBudgetBalance = currentBudgetBalance;
	}
}
