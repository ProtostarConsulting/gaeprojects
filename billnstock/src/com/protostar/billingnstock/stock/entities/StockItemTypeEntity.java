package com.protostar.billingnstock.stock.entities;

import java.util.ArrayList;
import java.util.List;

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
	private Ref<StockItemBrand> brand;
	@Index
	private Ref<StockItemProductTypeEntity> productType;

	@Index
	private List<Ref<StockItemTypeCategory>> categoryList;
	@Index
	private Ref<StockItemUnit> unitOfMeasure;

	/*
	 * @Index private String unit;
	 */
	@Index
	private boolean maintainAsProductionItem = false;
	private boolean maintainStockBySerialNumber = false;

	@Index
	private Ref<TaxEntity> selectedTaxItem;

	@Override
	public void beforeSave() {
		super.beforeSave();
		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()),
					Constants.STOCKITEMTYPE_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public StockItemUnit getUnitOfMeasure() {
		return unitOfMeasure == null ? null : unitOfMeasure.get();
	}

	public void setUnitOfMeasure(StockItemUnit unitOfMeasure) {
		if (unitOfMeasure != null) {
			this.unitOfMeasure = Ref.create(unitOfMeasure);
		}
	}

	public TaxEntity getSelectedTaxItem() {
		return selectedTaxItem == null ? null : selectedTaxItem.get();
	}

	public void setSelectedTaxItem(TaxEntity selectedTaxItem) {
		if (selectedTaxItem != null)
			this.selectedTaxItem = Ref.create(selectedTaxItem);
	}

	public boolean isMaintainStockBySerialNumber() {
		return maintainStockBySerialNumber;
	}

	public void setMaintainStockBySerialNumber(
			boolean maintainStockBySerialNumber) {
		this.maintainStockBySerialNumber = maintainStockBySerialNumber;
	}

	public StockItemBrand getBrand() {
		return brand == null ? null : brand.get();
	}

	public void setBrand(StockItemBrand brand) {
		if (brand != null)
			this.brand = Ref.create(brand);
	}

	public StockItemProductTypeEntity getProductType() {
		return productType == null ? null : productType.get();
	}

	public void setProductType(StockItemProductTypeEntity productType) {
		if (productType != null)
			this.productType = Ref.create(productType);
	}

	public List<StockItemTypeCategory> getCategoryList() {
		if (categoryList == null || categoryList.isEmpty()) {
			return null;
		}

		List<StockItemTypeCategory> tempList = new ArrayList<StockItemTypeCategory>(
				categoryList.size());
		for (Ref<StockItemTypeCategory> catRef : categoryList) {
			tempList.add(catRef.get());
		}
		return tempList;
	}

	public void setCategoryList(List<StockItemTypeCategory> categoryList) {
		if (categoryList == null || categoryList.isEmpty()) {
			this.categoryList = null;
			return;
		}

		this.categoryList = new ArrayList<Ref<StockItemTypeCategory>>(
				categoryList.size());
		for (StockItemTypeCategory cat : categoryList) {
			this.categoryList.add(Ref.create(cat));
		}
	}

	public boolean isMaintainAsProductionItem() {
		return maintainAsProductionItem;
	}

	public void setMaintainAsProductionItem(boolean maintainAsProductionItem) {
		this.maintainAsProductionItem = maintainAsProductionItem;
	}
}
