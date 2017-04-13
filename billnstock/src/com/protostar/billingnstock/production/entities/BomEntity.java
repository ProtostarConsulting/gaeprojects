package com.protostar.billingnstock.production.entities;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.stock.entities.BomLineItemCategory;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class BomEntity extends BaseEntity {
	@Index
	private String productName;
	private List<BomLineItemCategory> catList = new ArrayList<BomLineItemCategory>();
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public List<BomLineItemCategory> getCatList() {
		return catList;
	}
	public void setCatList(List<BomLineItemCategory> catList) {
		this.catList = catList;
	}

}
