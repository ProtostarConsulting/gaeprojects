package com.protostar.billingnstock.stock.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class StockItemTypeEntity extends BaseEntity {

	@Index
	private String itemName;
	@Index
	private String category;

	private Ref<TaxEntity> selectedTaxItem;

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
