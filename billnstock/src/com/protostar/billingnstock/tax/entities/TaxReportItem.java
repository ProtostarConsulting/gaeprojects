package com.protostar.billingnstock.tax.entities;

import java.util.Date;

public class TaxReportItem {

	private int invoiceItemNumber;
	private Date invoiceDate;
	private double taxAmt;

	public int getInvoiceItemNumber() {
		return invoiceItemNumber;
	}

	public void setInvoiceItemNumber(int invoiceItemNumber) {
		this.invoiceItemNumber = invoiceItemNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public double getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(double taxAmt) {
		this.taxAmt = taxAmt;
	}

}
