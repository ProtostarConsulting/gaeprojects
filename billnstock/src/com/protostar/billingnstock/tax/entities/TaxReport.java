package com.protostar.billingnstock.tax.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaxReport {
	private TaxEntity taxEntity;
	private Date fromDate;
	private Date toDate;
	private double taxTotal;
	private List<TaxReportItem> itemList = new ArrayList<TaxReportItem>();

	public TaxEntity getTaxEntity() {
		return taxEntity;
	}

	public void setTaxEntity(TaxEntity taxEntity) {
		this.taxEntity = taxEntity;
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

	public double getTaxTotal() {
		return taxTotal;
	}

	public void setTaxTotal(double taxTotal) {
		this.taxTotal = taxTotal;
	}

	public List<TaxReportItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<TaxReportItem> itemList) {
		this.itemList = itemList;
	}

}
