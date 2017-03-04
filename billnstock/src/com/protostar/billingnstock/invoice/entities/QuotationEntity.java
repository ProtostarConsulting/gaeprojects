package com.protostar.billingnstock.invoice.entities;

import javax.persistence.Embedded;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class QuotationEntity extends BaseEntity {

	@Embedded
	private InvoiceEntity invoiceObj;

	public InvoiceEntity getInvoiceObj() {
		return invoiceObj;
	}

	public void setInvoiceObj(InvoiceEntity invoiceObj) {
		this.invoiceObj = invoiceObj;
	}

}// end of QuotationEntity
