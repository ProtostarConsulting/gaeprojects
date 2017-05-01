package com.protostar.billingnstock.stock.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.protostar.billingnstock.production.entities.QCParameter;
import com.protostar.billingnstock.production.entities.QCParameterRecord;
import com.protostar.billnstock.until.data.WebUtil;

public class StockUtil {
	public StockReceiptQCRecord createNewStockReceiptQCDailyReceord(StockReceiptQCEntity stockReceiptQc,
			List<QCParameter> parameterList) {

		StockReceiptQCRecord stockReceiptQcDailyRecord = new StockReceiptQCRecord();
		List<QCParameterRecord> paramRecordedValues = new ArrayList<QCParameterRecord>();

		if (parameterList.size() > 0) {
			for (QCParameter qcParameter : parameterList) {
				QCParameterRecord qcParameterRecord = new QCParameterRecord();
				qcParameterRecord.setParameterName(qcParameter.getName());
				qcParameterRecord.setRecordedValue("");
				paramRecordedValues.add(qcParameterRecord);

			}
			stockReceiptQcDailyRecord.setParamRecordedValues(paramRecordedValues);
		}
		stockReceiptQcDailyRecord.setCreatedBy(WebUtil.getCurrentUser().getUser());
		stockReceiptQcDailyRecord.setCreatedDate(new Date());
		return stockReceiptQcDailyRecord;
	}
}
