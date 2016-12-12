package com.protostar.billingnstock.invoice.entities;

import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.tax.entities.TaxEntity;

public class ServiceLineItemList extends TaxEntity {
	
	private String serviceName;
	private Integer sQty;
	private float sPrice;
	private float serviceSubTotal;
	Ref<TaxEntity> selectedTaxItem;
	private String taxCodeName;
	
	
	public String getTaxCodeName() {
		return taxCodeName;
	}
	public void setTaxCodeName(String taxCodeName) {
		this.taxCodeName = taxCodeName;
	}
	public float getServiceSubTotal() {
		return serviceSubTotal;
	}
	public void setServiceSubTotal(float serviceSubTotal) {
		this.serviceSubTotal = serviceSubTotal;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Integer getsQty() {
		return sQty;
	}
	public void setsQty(Integer sQty) {
		this.sQty = sQty;
	}
	public float getsPrice() {
		return sPrice;
	}
	public void setsPrice(float sPrice) {
		this.sPrice = sPrice;
	}
	public TaxEntity getSelectedTaxItem() {
		return selectedTaxItem == null ? null: selectedTaxItem.get();
	}
	public void setSelectedTaxItem(TaxEntity selectedTaxItem) {
		this.selectedTaxItem = Ref.create(selectedTaxItem);
	}	

}
