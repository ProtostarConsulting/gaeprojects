package com.protostar.billingnstock.purchase.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.stock.entities.StockItemOrderType;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.stock.entities.StockLineItemCategory;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Cache
@Entity
public class PurchaseOrderEntity extends InvoiceEntity {
	private String billTo;
	private String shipTo;
	private Date poDate;
	@Index
	private Date poDueDate;
	private String requisitioner;
	private String shippedVia;
	private String fOBPoint;
	private String terms;
	private String termsAndConditions;

	@Index
	private Ref<SupplierEntity> supplier;
	@Index
	private Ref<WarehouseEntity> warehouse;
	@Index
	private Ref<BudgetEntity> budget;
	@Index
	private Ref<StockItemOrderType> orderType;

	private StockLineItemCategory budgetLineItemCategory;
	private StockLineItem budgetLineItem;

	@Override
	public void beforeSave() {
		if (getBusiness() == null) {
			throw new RuntimeException("Business entity is not set on: " + this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}

		if (getWarehouse() == null) {
			throw new RuntimeException("Warehouse entity is not set on: " + this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}

		if (getId() == null) {
			setCreatedDate(new Date());
			setModifiedDate(new Date());
		} else {
			setModifiedDate(new Date());
		}

		if (getStatus() == DocumentStatus.FINALIZED) {
			setStatusAlreadyFinalized(true);
		}

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.PURCHASE_ORDER_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public WarehouseEntity getWarehouse() {
		return warehouse.get();
	}

	public void setWarehouse(WarehouseEntity warehouse) {
		this.warehouse = Ref.create(warehouse);
	}

	public SupplierEntity getSupplier() {
		return supplier == null ? null : supplier.get();
	}

	public void setSupplier(SupplierEntity supplier) {
		if (supplier != null)
			this.supplier = Ref.create(supplier);
	}

	public BudgetEntity getBudget() {
		return budget == null ? null : budget.get();
	}

	public void setBudget(BudgetEntity budget) {
		if (budget != null)
			this.budget = Ref.create(budget);
	}

	public StockItemOrderType getOrderType() {
		return orderType == null ? null : orderType.get();
	}

	public void setOrderType(StockItemOrderType orderType) {
		if (orderType != null) {
			this.orderType = Ref.create(orderType);
		}
	}

	public String getBillTo() {
		return billTo;
	}

	public void setBillTo(String billTo) {
		this.billTo = billTo;
	}

	public String getShipTo() {
		return shipTo;
	}

	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

	public String getRequisitioner() {
		return requisitioner;
	}

	public void setRequisitioner(String requisitioner) {
		this.requisitioner = requisitioner;
	}

	public String getShippedVia() {
		return shippedVia;
	}

	public void setShippedVia(String shippedVia) {
		this.shippedVia = shippedVia;
	}

	public String getfOBPoint() {
		return fOBPoint;
	}

	public void setfOBPoint(String fOBPoint) {
		this.fOBPoint = fOBPoint;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public Date getPoDate() {
		return poDate;
	}

	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}

	public Date getPoDueDate() {
		return poDueDate;
	}

	public void setPoDueDate(Date poDueDate) {
		this.poDueDate = poDueDate;
	}

	public StockLineItemCategory getBudgetLineItemCategory() {
		return budgetLineItemCategory;
	}

	public void setBudgetLineItemCategory(StockLineItemCategory budgetLineItemCategory) {
		this.budgetLineItemCategory = budgetLineItemCategory;
	}

	public StockLineItem getBudgetLineItem() {
		return budgetLineItem;
	}

	public void setBudgetLineItem(StockLineItem budgetLineItem) {
		this.budgetLineItem = budgetLineItem;
	}

	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

}
