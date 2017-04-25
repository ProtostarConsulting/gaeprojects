package com.protostar.billingnstock.purchase.entities;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderReportItem {

	private String itemName;
	private int purchaseOrderQty;
	private List<StockReceiptDetailsForPOReport> stockReceiptDetails = new ArrayList<StockReceiptDetailsForPOReport>();
	private int qtyDiff;

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getPurchaseOrderQty() {
		return purchaseOrderQty;
	}

	public void setPurchaseOrderQty(int purchaseOrderQty) {
		this.purchaseOrderQty = purchaseOrderQty;
	}

	public List<StockReceiptDetailsForPOReport> getStockReceiptDetails() {
		return stockReceiptDetails;
	}

	public void setStockReceiptDetails(
			List<StockReceiptDetailsForPOReport> stockReceiptDetails) {
		this.stockReceiptDetails = stockReceiptDetails;
	}

	public int getQtyDiff() {
		return qtyDiff;
	}

	public void setQtyDiff(int qtyDiff) {
		this.qtyDiff = qtyDiff;
	}

}
