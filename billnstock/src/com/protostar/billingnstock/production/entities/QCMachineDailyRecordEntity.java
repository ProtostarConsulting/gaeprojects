package com.protostar.billingnstock.production.entities;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Cache
@Entity
public class QCMachineDailyRecordEntity extends BaseEntity {

	@Index
	private Ref<QCMachineEntity> machineQc;
	@Index
	private Date recordDate;
	private List<QCTimeParameterValue> parameterValueList;

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.PROD_MACHINEQC_RECORD_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public QCMachineEntity getMachineQc() {
		return machineQc == null ? null : machineQc.get();
	}

	public void setMachineQc(QCMachineEntity machineQc) {
		this.machineQc = Ref.create(machineQc);
	}

	public List<QCTimeParameterValue> getParameterValueList() {
		return parameterValueList;
	}

	public void setParameterValueList(List<QCTimeParameterValue> parameterValueList) {
		this.parameterValueList = parameterValueList;
	}

}
