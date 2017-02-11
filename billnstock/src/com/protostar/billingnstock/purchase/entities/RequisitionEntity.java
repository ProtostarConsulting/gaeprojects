package com.protostar.billingnstock.purchase.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Entity
public class RequisitionEntity extends InvoiceEntity {
	@Index
	private Ref<UserEntity> requester;	
	private String onBehalfOf;
	private Date expectedDate;

	@Override
	public void beforeSave() {
		// super.beforeSave(); No call to supper Before save as it will increase
		// invoice itemNumber counter. Copying content of supper class method
		// here....

		if (getBusiness() == null) {
			throw new RuntimeException("Business entity is not set on: " + this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}
		
		if (getId() == null) {
			setCreatedDate(new Date());
		} else {
			setModifiedDate(new Date());
		}

		if (getStatus() == DocumentStatus.FINALIZED) {
			setStatusAlreadyFinalized(true);
		}

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.REQUISITION_ORDER_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}


	public UserEntity getRequester() {
		return requester == null ? null : requester.get();
	}

	public void setRequester(UserEntity requester) {
		if (requester != null)
			this.requester = Ref.create(requester);
	}	

	public String getOnBehalfOf() {
		return onBehalfOf;
	}

	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}


	public Date getExpectedDate() {
		return expectedDate;
	}


	public void setExpectedDate(Date expectedDate) {
		this.expectedDate = expectedDate;
	}
}
