package com.protostar.billingnstock.purchase.entities;

import java.util.List;

public class LineItemCategory{
	private String categoryName;
	private List<LineItemEntity> items;	
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<LineItemEntity> getItems() {
		return items;
	}
	public void setItems(List<LineItemEntity> items) {
		this.items = items;
	}
}
