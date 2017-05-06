package com.protostar.billingnstock.production.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.protostar.billingnstock.production.entities.QCMachineDailyRecordEntity;
import com.protostar.billingnstock.production.entities.QCMachineEntity;
import com.protostar.billingnstock.production.entities.QCParameter;
import com.protostar.billingnstock.production.entities.QCParameterRecord;
import com.protostar.billingnstock.production.entities.QCTimeParameterValue;
import com.protostar.billnstock.until.data.WebUtil;
import com.protostar.billnstock.until.data.Constants.SchedulingFrequecyType;
import com.protostar.billnstock.until.data.Constants.SchedulingTimeFrequecyType;

public class ProductionUtil {
	public QCMachineDailyRecordEntity createNewQCMachineDailyRecordEntity(QCMachineEntity qcMachine, Date date) {

		QCMachineDailyRecordEntity qcRecord = new QCMachineDailyRecordEntity();
		qcRecord.setCreatedBy(WebUtil.getCurrentUser().getUser());
		qcRecord.setCreatedDate(new Date());
		List<QCTimeParameterValue> parameterValueList = new ArrayList<QCTimeParameterValue>();
		qcRecord.setParameterValueList(parameterValueList);

		List<QCParameter> parameterList = qcMachine.getParameterList();

		SchedulingFrequecyType schedule = qcMachine.getSchedule();

		SchedulingTimeFrequecyType scheduleTime = qcMachine.getScheduleTime();
		if (SchedulingTimeFrequecyType.HOURS == scheduleTime) {
			Date fromTime = qcMachine.getStartFromTime();
			Date tillTime = qcMachine.getTillTime();
			Date tempTime = fromTime;
			do {
				QCTimeParameterValue pValue = new QCTimeParameterValue();
				parameterValueList.add(pValue);

				List<QCParameterRecord> paramRecordedValues = new ArrayList<QCParameterRecord>();
				pValue.setTime(new Date(tempTime.getTime()));
				pValue.setParamRecordedValues(paramRecordedValues);
				if (parameterList != null) {
					for (QCParameter qcParameter : parameterList) {
						QCParameterRecord qcParameterRecord = new QCParameterRecord();
						qcParameterRecord.setQcParameter(qcParameter);
						qcParameterRecord.setRecordedValue("");
						paramRecordedValues.add(qcParameterRecord);
					}
				}

				tempTime.setHours(pValue.getTime().getHours() + 1);
			} while (tempTime.compareTo(tillTime) <= 0);

		}

		return qcRecord;
	}

}
