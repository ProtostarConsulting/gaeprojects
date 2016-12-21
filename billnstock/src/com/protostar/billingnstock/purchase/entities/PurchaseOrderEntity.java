package com.protostar.billingnstock.purchase.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;

@Entity
public class PurchaseOrderEntity extends InvoiceEntity {
	@Index
	private int itemNumber;
	private String to;
	private String shipTo;
	private Date poDate;
	private Date poDueDate;
	private String requisitioner;
	private String shippedVia;
	private String fOBPoint;
	private String terms;

	Ref<SupplierEntity> supplier;

	public SupplierEntity getSupplier() {
		return supplier.get();
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

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}
}
