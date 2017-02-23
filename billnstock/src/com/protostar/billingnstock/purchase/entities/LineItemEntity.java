package com.protostar.billingnstock.purchase.entities;

public class LineItemEntity {

	private String itemName;
	private double price;
	private String qty;
	private double currentBudgetBalance;
	
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
