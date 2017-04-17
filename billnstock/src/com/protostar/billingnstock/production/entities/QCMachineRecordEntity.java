package com.protostar.billingnstock.production.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Cache
@Entity
public class QCMachineRecordEntity extends QCEntity {

	@Index
	private Ref<ProductionMachineEntity> machine;

	public ProductionMachineEntity getMachine() {
		return machine.get();
	}

	public void setMachine(Ref<ProductionMachineEntity> machine) {
		this.machine = machine;
	}

}
