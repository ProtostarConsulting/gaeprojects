package com.protostar.billingnstock.invoice.entities;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants.PaymentModeType;
import com.protostar.billnstock.until.data.Constants.PaymentTxnType;

@Cache
@Entity
public class PaymentTxnTypeEntity extends BaseEntity {
	private PaymentModeType paymentMode;
	private PaymentTxnType paymentTxnType;
	private Date receivedDate;

	private float receivedAmt;
	private String receivedVia;
	private String chequeNo;
	private String bankName;
	
	@Index
	private int invoiceItemNumber;
	
	public PaymentModeType getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(PaymentModeType paymentMode) {
		this.paymentMode = paymentMode;
	}
	public PaymentTxnType getPaymentTxnType() {
		return paymentTxnType;
	}
	public void setPaymentTxnType(PaymentTxnType paymentTxnType) {
		this.paymentTxnType = paymentTxnType;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public float getReceivedAmt() {
		return receivedAmt;
	}
	public void setReceivedAmt(float receivedAmt) {
		this.receivedAmt = receivedAmt;
	}
	public String getReceivedVia() {
		return receivedVia;
	}
	public void setReceivedVia(String receivedVia) {
		this.receivedVia = receivedVia;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public int getInvoiceItemNumber() {
		return invoiceItemNumber;
	}
	public void setInvoiceItemNumber(int invoiceItemNumber) {
		this.invoiceItemNumber = invoiceItemNumber;
	}

}
