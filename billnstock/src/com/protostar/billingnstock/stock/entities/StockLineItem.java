package com.protostar.billingnstock.stock.entities;

import java.util.List;

import javax.persistence.Embedded;

import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.tax.entities.TaxEntity;

public class StockLineItem {

	private String itemName;
	private int qty;
	private double cost;
	private double price;

	private boolean isProduct = false;

	private int stockIssuedQty;
	private double currentBudgetBalance;

	private int stockMaintainedQty;
	private StockItemEntity stockItem;
	private Ref<StockItemTypeEntity> stockItemType;
	Ref<TaxEntity> selectedTaxItem;

	@Embedded
	private List<StockItemInstanceEntity> stockItemInstanceList;

	public TaxEntity getSelectedTaxItem() {
		return selectedTaxItem == null ? null : selectedTaxItem.get();
	}

	public void setSelectedTaxItem(TaxEntity selectedTaxItem) {
		if (selectedTaxItem != null)
			this.selectedTaxItem = Ref.create(selectedTaxItem);
	}

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

	public StockItemEntity getStockItem() {
		return stockItem;
	}

	public void setStockItem(StockItemEntity stockItem) {
		this.stockItem = stockItem;
	}

	public double getCurrentBudgetBalance() {
		return currentBudgetBalance;
	}

	public void setCurrentBudgetBalance(double currentBudgetBalance) {
		this.currentBudgetBalance = currentBudgetBalance;
	}

	public int getStockIssuedQty() {
		return stockIssuedQty;
	}

	public void setStockIssuedQty(int stockIssuedQty) {
		this.stockIssuedQty = stockIssuedQty;
	}

	public static StockLineItem getCopy(StockLineItem fromCopy) {
		StockLineItem toCopy = new StockLineItem();
		toCopy.setCost(fromCopy.cost);
		toCopy.setItemName(fromCopy.itemName);
		toCopy.setPrice(fromCopy.price);
		toCopy.setProduct(fromCopy.isProduct);
		toCopy.setQty(fromCopy.qty);
		if (fromCopy.selectedTaxItem != null)
			toCopy.setSelectedTaxItem(fromCopy.selectedTaxItem.get());
		toCopy.setStockItem(fromCopy.stockItem);
		return toCopy;
	}

	public List<StockItemInstanceEntity> getStockItemInstanceList() {
		return this.stockItemInstanceList;
	}

	public void setStockItemInstanceList(List<StockItemInstanceEntity> list) {
		this.stockItemInstanceList = list;
	}
}
