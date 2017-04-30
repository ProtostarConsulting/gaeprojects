package com.protostar.billingnstock.production.entities;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.stock.entities.StockLineItemsByCategory;
import com.protostar.billingnstock.stock.entities.StockItemTypeEntity;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Cache
@Entity
public class BomEntity extends BaseEntity {
	@Index
	private Ref<StockItemTypeEntity> stockItemType;
	private List<StockLineItemsByCategory> catList = new ArrayList<StockLineItemsByCategory>();

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.PROD_BOM_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public StockItemTypeEntity getStockItemType() {

		return stockItemType == null ? null : stockItemType.get();
	}

	public void setStockItemType(StockItemTypeEntity stockItemType) {
		if (stockItemType != null)
			this.stockItemType = Ref.create(stockItemType);

	}

	public List<StockLineItemsByCategory> getCatList() {
		return catList;
	}

	public void setCatList(List<StockLineItemsByCategory> catList) {
		this.catList = catList;
	}

}
