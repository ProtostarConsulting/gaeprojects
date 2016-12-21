package com.protostar.billingnstock.invoice.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class QuotationEntity extends InvoiceEntity {

	@Index
	private long quotationNumber;

	public long getQuotationNumber() {
		return quotationNumber;
	}

	public void setQuotationNumber(long quotationNumber) {
		this.quotationNumber = quotationNumber;
	}

}// end of QuotationEntity
