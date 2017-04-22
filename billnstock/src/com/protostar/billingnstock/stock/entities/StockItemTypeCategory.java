package com.protostar.billingnstock.stock.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class StockItemTypeCategory extends BaseEntity {
	private String catName;

	@Index
	private Ref<StockItemTypeCategory> parent;

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public StockItemTypeCategory getParent() {
		return parent == null ? null : parent.get();
	}

	public void setParent(StockItemTypeCategory parent) {
		if (parent != null)
			this.parent = Ref.create(parent);
	}
}
