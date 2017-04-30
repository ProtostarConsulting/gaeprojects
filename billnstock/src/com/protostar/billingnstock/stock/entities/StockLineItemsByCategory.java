package com.protostar.billingnstock.stock.entities;

import java.util.List;

import com.googlecode.objectify.Ref;

public class StockLineItemsByCategory {

	private Ref<StockItemTypeCategory> cat;
	private List<StockLineItem> items;

	public List<StockLineItem> getItems() {
		return items;
	}

	public void setItems(List<StockLineItem> items) {
		this.items = items;
	}

	public StockItemTypeCategory getCat() {
		return cat == null ? null : cat.get();
	}

	public void setCat(StockItemTypeCategory cat) {
		if (cat != null)
			this.cat = Ref.create(cat);
	}
}
