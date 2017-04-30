package com.protostar.billingnstock.stock.entities;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;

public class StockLineItemsByWarehouse {

	private Ref<WarehouseEntity> fromWH;
	private List<StockLineItemsByCategory> catList = new ArrayList<StockLineItemsByCategory>();

	public WarehouseEntity getFromWH() {
		return this.fromWH == null ? null : this.fromWH.get();
	}

	public void setFromWH(WarehouseEntity fromWH) {
		if (fromWH != null)
			this.fromWH = Ref.create(fromWH);
	}

	public List<StockLineItemsByCategory> getCatList() {
		return catList;
	}

	public void setCatList(List<StockLineItemsByCategory> catList) {
		this.catList = catList;
	}

}
