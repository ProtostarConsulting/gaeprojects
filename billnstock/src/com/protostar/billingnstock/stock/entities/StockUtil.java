package com.protostar.billingnstock.stock.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.protostar.billingnstock.production.entities.QCParameter;
import com.protostar.billingnstock.production.entities.QCParameterRecord;

public class StockUtil {
	public StockReceiptQCDailyRecordEntity createNewStockReceiptQCDailyReceord(StockReceiptQCEntity stockReceiptQc, Date recordDate) {
		
		StockReceiptQCDailyRecordEntity stockReceiptQcDailyRecord = new StockReceiptQCDailyRecordEntity();
		List<QCParameterRecord> paramRecordedValues = new ArrayList<QCParameterRecord>();
		
		List<QCParameter> parameterList  = stockReceiptQc.getParameterList();
		
		if(parameterList.size() > 0) {
			for (QCParameter qcParameter : parameterList) {
				QCParameterRecord qcParameterRecord = new QCParameterRecord();
				qcParameterRecord.setParameterName(qcParameter.getName());
				qcParameterRecord.setRecordedValue("");
				paramRecordedValues.add(qcParameterRecord);
			}
			stockReceiptQcDailyRecord.setParamRecordedValues(paramRecordedValues);
		}
		return stockReceiptQcDailyRecord;
	}
}
