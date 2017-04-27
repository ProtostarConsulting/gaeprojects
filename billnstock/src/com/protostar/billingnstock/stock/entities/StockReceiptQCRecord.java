package com.protostar.billingnstock.stock.entities;

import java.util.List;

import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.production.entities.QCParameterRecord;

public class StockReceiptQCRecord extends QCRecord {

	private Ref<StockReceiptQCEntity> qcStockReceipt;
	private List<QCParameterRecord> paramRecordedValues;

	public StockReceiptQCEntity getQcStockReceipt() {
		return qcStockReceipt == null ? null : qcStockReceipt.get();
	}

	public void setQcStockReceipt(StockReceiptQCEntity qcStockReceipt) {
		this.qcStockReceipt = Ref.create(qcStockReceipt);
	}

	public List<QCParameterRecord> getParamRecordedValues() {
		return paramRecordedValues;
	}

	public void setParamRecordedValues(
			List<QCParameterRecord> paramRecordedValues) {
		this.paramRecordedValues = paramRecordedValues;
	}
}
