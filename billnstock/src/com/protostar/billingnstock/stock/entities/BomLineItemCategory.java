package com.protostar.billingnstock.stock.entities;

import java.util.List;

import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.purchase.entities.LineItemEntity;

public class BomLineItemCategory {

	private Ref<StockItemTypeCategory> cat;
	private List<LineItemEntity> items;

	public List<LineItemEntity> getItems() {
		return items;
	}

	public void setItems(List<LineItemEntity> items) {
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
