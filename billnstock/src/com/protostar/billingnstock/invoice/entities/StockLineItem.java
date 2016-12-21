package com.protostar.billingnstock.invoice.entities;

import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.stock.entities.StockItemEntity;
import com.protostar.billingnstock.tax.entities.TaxEntity;

public class StockLineItem {

	private String itemName;
	private int qty;
	private double cost;
	private double price;
	Ref<TaxEntity> selectedTaxItem;

	private boolean isProduct = false;
	private Ref<StockItemEntity> stockItem;
	private int stockMaintainedQty;

	public TaxEntity getSelectedTaxItem() {
		return selectedTaxItem == null ? null : selectedTaxItem.get();
	}

	public void setSelectedTaxItem(TaxEntity selectedTaxItem) {
		if (selectedTaxItem != null)
			this.selectedTaxItem = Ref.create(selectedTaxItem);
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

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public boolean isProduct() {
		return isProduct;
	}

	public void setProduct(boolean isProduct) {
		this.isProduct = isProduct;
	}

	public StockItemEntity getStockItem() {
		return stockItem == null ? null : stockItem.get();
	}

	public void setStockItem(StockItemEntity stockItem) {
		if (stockItem != null)
			this.stockItem = Ref.create(stockItem);
	}

	public int getStockMaintainedQty() {
		return stockMaintainedQty;
	}

	public void setStockMaintainedQty(int stockMaintainedQty) {
		this.stockMaintainedQty = stockMaintainedQty;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
}
