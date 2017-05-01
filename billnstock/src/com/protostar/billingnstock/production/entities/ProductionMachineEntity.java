package com.protostar.billingnstock.production.entities;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Cache
@Entity
public class ProductionMachineEntity extends BaseEntity {
	@Index
	private String machineName;
	private String manifacturerRefNumber;
	private String manifacturerName;
	private Address manifacturerDetail = new Address();
	
	@Override
	public void beforeSave() {
		super.beforeSave();

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.PROD_MACHINE_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

		
	public String getManifacturerName() {
		return manifacturerName;
	}

	public void setManifacturerName(String manifacturerName) {
		this.manifacturerName = manifacturerName;
	}

	public Address getManifacturerDetail() {
		return manifacturerDetail;
	}

	public void setManifacturerDetail(Address manifacturerDetail) {
		this.manifacturerDetail = manifacturerDetail;
	}

	public String getManifacturerRefNumber() {
		return manifacturerRefNumber;
	}

	public void setManifacturerRefNumber(String manifacturerRefNumber) {
		this.manifacturerRefNumber = manifacturerRefNumber;
	}

}
