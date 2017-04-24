package com.protostar.billingnstock.purchase.entities;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderReport {

	private List<PurchaseOrderReportItem> pOReportItems = new ArrayList<PurchaseOrderReportItem>();

	public List<PurchaseOrderReportItem> getpOReportItems() {
		return pOReportItems;
	}

	public void setpOReportItems(List<PurchaseOrderReportItem> pOReportItems) {
		this.pOReportItems = pOReportItems;
	}

}
