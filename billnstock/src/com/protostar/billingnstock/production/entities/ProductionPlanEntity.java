package com.protostar.billingnstock.production.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Cache
@Entity
public class ProductionPlanEntity extends BaseEntity {

	@Index
	private Ref<BomEntity> bomEntity;

	private String title;

	private int plannedQty;

	// use desc note from BaseEntity
	private String salesOrderNumber;

	@Index
	private DocumentStatus status = DocumentStatus.DRAFT;

	private Date fromDateTime;
	private Date toDateTime;

	private Ref<Customer> customer;

	private List<Ref<ProductionRequisitionEntity>> prodRequisitionList = new ArrayList<Ref<ProductionRequisitionEntity>>();
	private List<Ref<ProductionShipmentEntity>> prodShipmentList = new ArrayList<Ref<ProductionShipmentEntity>>();
	private List<Ref<ProductionPlanDailyReport>> planDailyReport = new ArrayList<Ref<ProductionPlanDailyReport>>();

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.PROD_PLAN_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public BomEntity getBomEntity() {
		return bomEntity == null ? null : bomEntity.get();
	}

	public void setBomEntity(BomEntity bomEntity) {
		if (bomEntity != null)
			this.bomEntity = Ref.create(bomEntity);

	}

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

	public List<ProductionRequisitionEntity> getProdRequisitionList() {
		if (prodRequisitionList == null || prodRequisitionList.isEmpty()) {
			return null;
		}
		List<ProductionRequisitionEntity> tempList = new ArrayList<ProductionRequisitionEntity>(
				prodRequisitionList.size());
		for (Ref<ProductionRequisitionEntity> catRef : prodRequisitionList) {
			tempList.add(catRef.get());
		}
		return tempList;
	}

	public void setProdRequisitionList(List<ProductionRequisitionEntity> prodRequisitionList) {
		if (prodRequisitionList == null || prodRequisitionList.isEmpty()) {
			this.prodRequisitionList = null;
			return;
		}

		this.prodRequisitionList = new ArrayList<Ref<ProductionRequisitionEntity>>(prodRequisitionList.size());
		for (ProductionRequisitionEntity requisition : prodRequisitionList) {
			this.prodRequisitionList.add(Ref.create(requisition));
		}
	}

	public List<ProductionShipmentEntity> getProdShipmentList() {
		if (prodShipmentList == null || prodShipmentList.isEmpty()) {
			return null;
		}
		List<ProductionShipmentEntity> tempList = new ArrayList<ProductionShipmentEntity>(prodShipmentList.size());
		for (Ref<ProductionShipmentEntity> entityRef : prodShipmentList) {
			tempList.add(entityRef.get());
		}
		return tempList;
	}

	public void setProdShipmentList(List<ProductionShipmentEntity> prodShipmentList) {
		if (prodShipmentList == null || prodShipmentList.isEmpty()) {
			this.prodShipmentList = null;
			return;
		}

		this.prodShipmentList = new ArrayList<Ref<ProductionShipmentEntity>>(prodShipmentList.size());
		for (ProductionShipmentEntity entity : prodShipmentList) {
			this.prodShipmentList.add(Ref.create(entity));
		}
	}

	public int getPlannedQty() {
		return plannedQty;
	}

	public void setPlannedQty(int plannedQty) {
		this.plannedQty = plannedQty;
	}

	public List<ProductionPlanDailyReport> getPlanDailyReport() {
		if (planDailyReport == null || planDailyReport.isEmpty()) {
			return null;
		}
		List<ProductionPlanDailyReport> tempList = new ArrayList<ProductionPlanDailyReport>(planDailyReport.size());
		for (Ref<ProductionPlanDailyReport> entityRef : planDailyReport) {
			tempList.add(entityRef.get());
		}
		return tempList;
	}

	public void setPlanDailyReport(List<ProductionPlanDailyReport> planDailyReport) {
		if (planDailyReport == null || planDailyReport.isEmpty()) {
			this.planDailyReport = null;
			return;
		}

		this.planDailyReport = new ArrayList<Ref<ProductionPlanDailyReport>>(planDailyReport.size());
		for (ProductionPlanDailyReport entity : planDailyReport) {
			this.planDailyReport.add(Ref.create(entity));
		}
	}

}
