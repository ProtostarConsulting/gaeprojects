package com.protostar.billingnstock.purchase.entities;

import java.util.Date;

public class PurchaseOrderFilterWrapper {

	private Long businessId;
	private Date fromDate;
	private Date toDate;

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
