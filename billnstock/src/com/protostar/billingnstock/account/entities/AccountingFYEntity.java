package com.protostar.billingnstock.account.entities;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.OnSave;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Entity
public class AccountingFYEntity extends BaseEntity {

	private Date startsFrom;
	private Date till;
	private Boolean currentFY;
	@OnSave
	public void beforeSave() {
		super.beforeSave();
		
		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()),
					Constants.AFY_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}
	public Date getStartsFrom() {
		return startsFrom;
	}
	public void setStartsFrom(Date startsFrom) {
		this.startsFrom = startsFrom;
	}
	public Date getTill() {
		return till;
	}
	public void setTill(Date till) {
		this.till = till;
	}
	public Boolean getCurrentFY() {
		return currentFY;
	}
	public void setCurrentFY(Boolean currentFY) {
		this.currentFY = currentFY;
	}
}
