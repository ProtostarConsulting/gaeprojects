package com.protostar.billingnstock.stock.entities;

import java.util.List;

import com.protostar.billingnstock.production.entities.BomEntity;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;

public class StockItemTypeFilterWrapper {
	private StockItemBrand brand;
	private StockItemProductTypeEntity productType;
	private StockItemTypeCategory category;
	private WarehouseEntity warehouse;
	private BomEntity bomEntity;
	private List<StockItemTypeCategory> categoryList;

	public List<StockItemTypeCategory> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<StockItemTypeCategory> categoryList) {
		this.categoryList = categoryList;
	}

	public StockItemTypeCategory getCategory() {
		return category;
	}

	public void setCategory(StockItemTypeCategory category) {
		this.category = category;
	}

	public WarehouseEntity getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WarehouseEntity warehouse) {
		this.warehouse = warehouse;
	}

	public StockItemProductTypeEntity getProductType() {
		return productType;
	}

	public void setProductType(StockItemProductTypeEntity productType) {
		this.productType = productType;
	}

	public StockItemBrand getBrand() {
		return brand;
	}

	public void setBrand(StockItemBrand brand) {
		this.brand = brand;
	}

	public BomEntity getBomEntity() {
		return bomEntity;
	}

	public void setBomEntity(BomEntity bomEntity) {
		this.bomEntity = bomEntity;
	}
}
