package com.protostar.billingnstock.purchase.entities;

import java.util.List;

public class PurchaseOrderReport {

	private List<String> receiptHeadingList;
	private List<PurchaseOrderReportItem> pOReportItems;

	public List<PurchaseOrderReportItem> getpOReportItems() {
		return pOReportItems;
	}

	public void setpOReportItems(List<PurchaseOrderReportItem> pOReportItems) {
		this.pOReportItems = pOReportItems;
	}

	public List<String> getReceiptHeadingList() {
		return receiptHeadingList;
	}

	public void setReceiptHeadingList(List<String> receiptHeadingList) {
		this.receiptHeadingList = receiptHeadingList;
	}
}
