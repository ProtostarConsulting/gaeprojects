package com.protostar.billingnstock.tax.entities;

public class TaxLineItem {
	private String taxSubCodeName;
	private double taxPercenatge;

	public String getTaxSubCodeName() {
		return taxSubCodeName;
	}

	public void setTaxSubCodeName(String taxSubCodeName) {
		this.taxSubCodeName = taxSubCodeName;
	}

	public double getTaxPercenatge() {
		return taxPercenatge;
	}

	public void setTaxPercenatge(double taxPercenatge) {
		this.taxPercenatge = taxPercenatge;
	}

}
