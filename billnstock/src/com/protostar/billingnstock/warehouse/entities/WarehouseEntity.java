package com.protostar.billingnstock.warehouse.entities;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class WarehouseEntity extends BaseEntity{

	@Index
	private String warehouseName; 
	private String description ;
	
	private Address  address = new Address();
	

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	
}
