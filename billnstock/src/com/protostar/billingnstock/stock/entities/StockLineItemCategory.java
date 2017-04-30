package com.protostar.billingnstock.stock.entities;

import java.util.List;

public class StockLineItemCategory{
	private String categoryName;
	private List<StockLineItem> items;	
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<StockLineItem> getItems() {
		return items;
	}
	public void setItems(List<StockLineItem> items) {
		this.items = items;
	}
}
