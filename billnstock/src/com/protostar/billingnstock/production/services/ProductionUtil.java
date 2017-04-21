package com.protostar.billingnstock.production.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.protostar.billingnstock.production.entities.QCMachineDailyRecordEntity;
import com.protostar.billingnstock.production.entities.QCMachineEntity;
import com.protostar.billingnstock.production.entities.QCParameter;
import com.protostar.billingnstock.production.entities.QCParameterRecord;
import com.protostar.billingnstock.production.entities.QCTimeParameterValue;
import com.protostar.billnstock.until.data.Constants.SchedulingFrequecyType;
import com.protostar.billnstock.until.data.Constants.SchedulingTimeFrequecyType;

public class ProductionUtil {
	public QCMachineDailyRecordEntity createNewQCMachineDailyRecordEntity(
			QCMachineEntity qcMachine, Date date) {

		QCMachineDailyRecordEntity qcMachineDailyRecordEntity = new QCMachineDailyRecordEntity();
		List<QCTimeParameterValue> parameterValueList = new ArrayList<QCTimeParameterValue>();
		qcMachineDailyRecordEntity.setParameterValueList(parameterValueList);

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

				for (QCParameter qcParameter : parameterList) {
					QCParameterRecord qcParameterRecord = new QCParameterRecord();
					qcParameterRecord.setParameterName(qcParameter.getName());
					qcParameterRecord.setRecordedValue("");
					paramRecordedValues.add(qcParameterRecord);
				}

				
				tempTime.setHours(pValue.getTime().getHours() + 1);
			} while (tempTime.compareTo(tillTime) <= 0);

		}

		return qcMachineDailyRecordEntity;
	}

}
