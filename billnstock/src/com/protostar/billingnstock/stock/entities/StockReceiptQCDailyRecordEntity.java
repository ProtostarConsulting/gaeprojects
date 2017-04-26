package com.protostar.billingnstock.stock.entities;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.production.entities.QCParameterRecord;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class StockReceiptQCDailyRecordEntity extends BaseEntity {
	
	@Index
	private Ref<StockReceiptQCEntity> qcStockReceipt;
	@Index
	private Date recordDate;
	private List<QCParameterRecord> paramRecordedValues;

	public StockReceiptQCEntity getQcStockReceipt() {
		return qcStockReceipt == null ? null : qcStockReceipt.get();
	}

	public void setQcStockReceipt(StockReceiptQCEntity qcStockReceipt) {
		this.qcStockReceipt = Ref.create(qcStockReceipt);
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public List<QCParameterRecord> getParamRecordedValues() {
		return paramRecordedValues;
	}

	public void setParamRecordedValues(List<QCParameterRecord> paramRecordedValues) {
		this.paramRecordedValues = paramRecordedValues;
	}
}
