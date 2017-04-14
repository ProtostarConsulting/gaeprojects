package com.protostar.billingnstock.production.entities;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class ProductionMachineEntity extends BaseEntity {
	@Index
	private String machineName;
	private long machineNo;
	private Address manifacturerDetail = new Address();

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public long getMachineNo() {
		return machineNo;
	}

	public void setMachineNo(long machineNo) {
		this.machineNo = machineNo;
	}

	public Address getManifacturerDetail() {
		return manifacturerDetail;
	}

	public void setManifacturerDetail(Address manifacturerDetail) {
		this.manifacturerDetail = manifacturerDetail;
	}

}
