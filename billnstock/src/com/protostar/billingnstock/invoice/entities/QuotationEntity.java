package com.protostar.billingnstock.invoice.entities;

import javax.persistence.Embedded;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;

@Cache
@Entity
public class QuotationEntity extends BaseEntity {

	@Index
	private DocumentStatus status = DocumentStatus.DRAFT;
	
	@Embedded
	private InvoiceEntity invoiceObj;

	public InvoiceEntity getInvoiceObj() {
		return invoiceObj;
	}

	public void setInvoiceObj(InvoiceEntity invoiceObj) {
		this.invoiceObj = invoiceObj;
	}

}// end of QuotationEntity
