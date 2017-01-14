package com.protostar.billingnstock.purchase.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Entity
public class PurchaseOrderEntity extends InvoiceEntity {
	private String to;
	private String shipTo;
	private Date poDate;
	private Date poDueDate;
	private String requisitioner;
	private String shippedVia;
	private String fOBPoint;
	private String terms;

	private Ref<SupplierEntity> supplier;
	@Index
	private Ref<WarehouseEntity> warehouse;

	@OnSave
	@Override
	public void beforeSave() {
		// super.beforeSave(); No call to supper Before save as it will increase
		// invoice itemNumber counter. Copying content of supper class method
		// here....

		if (getBusiness() == null) {
			throw new RuntimeException("Business entity is not set on: "
					+ this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}

		if (getWarehouse() == null) {
			throw new RuntimeException("Warehouse entity is not set on: "
					+ this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}

		if (getId() == null) {
			setCreatedDate(new Date());
		} else {
			setModifiedDate(new Date());
		}

		if (getStatus() == DocumentStatus.FINALIZED) {
			setStatusAlreadyFinalized(true);
		}

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()),
					Constants.PURCHASE_ORDER_NO_COUNTER);
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

	public String getShipTo() {
		return shipTo;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTo() {
		return to;
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
}
