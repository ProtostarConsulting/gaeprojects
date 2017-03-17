package com.protostar.billingnstock.invoice.entities;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Cache
@Entity
public class QuotationEntity extends InvoiceEntity {

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
			setModifiedDate(new Date());
		} else {
			setModifiedDate(new Date());
		}

		if (getStatus() == DocumentStatus.FINALIZED) {
			setStatusAlreadyFinalized(true);
		}

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.QUOTATION_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	// This is one way of reusing. not correct in this case. switched to
	// inheritance to make it same as purchase order
	/*
	 * @Embedded private InvoiceEntity invoiceObj;
	 * 
	 * public InvoiceEntity getInvoiceObj() { return invoiceObj; }
	 * 
	 * public void setInvoiceObj(InvoiceEntity invoiceObj) { this.invoiceObj =
	 * invoiceObj; }
	 */

}// end of QuotationEntity
