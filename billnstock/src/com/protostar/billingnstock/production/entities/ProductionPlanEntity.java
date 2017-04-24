package com.protostar.billingnstock.production.entities;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;

@Cache
@Entity
public class ProductionPlanEntity extends BaseEntity {

	private String title;
	// use desc note from BaseEntity
	private String salesOrderNumber;

	@Index
	private DocumentStatus status = DocumentStatus.DRAFT;
	@Index
	private boolean isStatusAlreadyFinalized = false;

	private Date fromDateTime;
	private Date toDateTime;

	@Index
	private Ref<Customer> customer;

	private List<ProductionRequisitionEntity> prodRequisitionList;
	private List<ProductionShipmentEntity> prodShipmentList;

	public Customer getCustomer() {
		return customer == null ? null : customer.get();
	}

	public void setCustomer(Customer customer) {
		if (customer != null)
			this.customer = Ref.create(customer);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSalesOrderNumber() {
		return salesOrderNumber;
	}

	public void setSalesOrderNumber(String salesOrderNumber) {
		this.salesOrderNumber = salesOrderNumber;
	}

	public List<ProductionRequisitionEntity> getProdRequisitionList() {
		return prodRequisitionList;
	}

	public void setProdRequisitionList(List<ProductionRequisitionEntity> prodRequisitionList) {
		this.prodRequisitionList = prodRequisitionList;
	}

	public List<ProductionShipmentEntity> getProdShipmentList() {
		return prodShipmentList;
	}

	public void setProdShipmentList(List<ProductionShipmentEntity> prodShipmentList) {
		this.prodShipmentList = prodShipmentList;
	}

	public Date getFromDateTime() {
		return fromDateTime;
	}

	public void setFromDateTime(Date fromDateTime) {
		this.fromDateTime = fromDateTime;
	}

	public Date getToDateTime() {
		return toDateTime;
	}

	public void setToDateTime(Date toDateTime) {
		this.toDateTime = toDateTime;
	}

}
