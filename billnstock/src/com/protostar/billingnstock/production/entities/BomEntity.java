package com.protostar.billingnstock.production.entities;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.stock.entities.BomLineItemCategory;
import com.protostar.billingnstock.stock.entities.StockItemTypeEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class BomEntity extends BaseEntity {
	@Index
	private Ref<StockItemTypeEntity> stockItemType;
	private List<BomLineItemCategory> catList = new ArrayList<BomLineItemCategory>();

	public List<BomLineItemCategory> getCatList() {
		return catList;
	}

	public void setCatList(List<BomLineItemCategory> catList) {
		this.catList = catList;
	}

	public StockItemTypeEntity getStockItemType() {
		if (stockItemType != null)
			return stockItemType.get();
		else
			return null;
	}

	public void setStockItemType(StockItemTypeEntity stockItemType) {
		if (stockItemType != null)
			this.stockItemType = Ref.create(stockItemType);
	}

}
