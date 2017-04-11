package com.protostar.billingnstock.stock.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Cache
@Entity
public class StockItemTypeEntity extends BaseEntity {

	@Index
	private String itemName;

	@Index
	private String tags;
	// comma separated list of tags

	@Index
	private Ref<StockItemTypeCategory> cat;
	
	@Index
	private Ref<StockItemUnit> unitOfMeasure;
	
	/*@Index
	private String unit;*/
	private boolean maintainStockBySerialNumber = false;
	private Ref<TaxEntity> selectedTaxItem;

	@Override
	public void beforeSave() {
		super.beforeSave();
		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.STOCKITEMTYPE_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public TaxEntity getSelectedTaxItem() {
		return selectedTaxItem == null ? null : selectedTaxItem.get();
	}

	public void setSelectedTaxItem(TaxEntity selectedTaxItem) {
		if (selectedTaxItem != null)
			this.selectedTaxItem = Ref.create(selectedTaxItem);
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public boolean isMaintainStockBySerialNumber() {
		return maintainStockBySerialNumber;
	}

	public void setMaintainStockBySerialNumber(boolean maintainStockBySerialNumber) {
		this.maintainStockBySerialNumber = maintainStockBySerialNumber;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public StockItemTypeCategory getCat() {
		return cat == null ? null : cat.get();
	}

	public void setCat(StockItemTypeCategory cat) {
		if (cat != null)
			this.cat = Ref.create(cat);
	}

}
