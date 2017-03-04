package com.protostar.billingnstock.purchase.entities;

import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.BudgetType;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Cache
@Entity
public class BudgetEntity extends BaseEntity {
	@Index
	private DocumentStatus status = DocumentStatus.DRAFT;
	
	private BudgetType type = BudgetType.NA;
	@Index
	private String period; 
	private List<LineItemCategory> categoryList;
	
	@Override
	public void beforeSave() {
		super.beforeSave();
		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()),
					Constants.BUDGET_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}
	
	public BudgetType getType() {
		return type;
	}
	public void setType(BudgetType type) {
		this.type = type;
	}	
	public List<LineItemCategory> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List<LineItemCategory> categoryList) {
		this.categoryList = categoryList;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public DocumentStatus getStatus() {
		return status;
	}
	public void setStatus(DocumentStatus status) {
		this.status = status;
	}	

}


