package com.protostar.billingnstock.production.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Cache
@Entity
public class QCMachineEntity extends QCEntity {

	@Index
	private Ref<ProductionMachineEntity> machine;

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.PROD_MACHINEQC_MASTER_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public ProductionMachineEntity getMachine() {
		return machine == null ? null : machine.get();
	}

	public void setMachine(ProductionMachineEntity machine) {
		this.machine = Ref.create(machine);
	}

}
