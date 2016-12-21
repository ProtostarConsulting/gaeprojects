package com.protostar.billingnstock.tax.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class TaxEntity extends BaseEntity{

	private String taxCodeName;
	private double taxPercenatge;
	@Index
	private boolean active=true;
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getTaxCodeName() {
		return taxCodeName;
	}

	public void setTaxCodeName(String taxCodeName) {
		this.taxCodeName = taxCodeName;
	}

	public double getTaxPercenatge() {
		return taxPercenatge;
	}

	public void setTaxPercenatge(double taxPercenatge) {
		this.taxPercenatge = taxPercenatge;
	}

}// end of TaxEntity
