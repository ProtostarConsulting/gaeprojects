package com.protostar.billingnstock.tax.entities;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class TaxEntity extends BaseEntity {
	private String taxCodeName;
	private double taxPercenatge;
	@Index
	private boolean active = true;
	private boolean withSubHeads = true;
	private List<TaxLineItem> subHeads = new ArrayList<TaxLineItem>();

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

	public boolean isWithSubHeads() {
		return withSubHeads;
	}

	public void setWithSubHeads(boolean withSubHeads) {
		this.withSubHeads = withSubHeads;
	}

	public List<TaxLineItem> getSubHeads() {
		return subHeads;
	}

	public void setSubHeads(List<TaxLineItem> subHeads) {
		this.subHeads = subHeads;
	}

}// end of TaxEntity
