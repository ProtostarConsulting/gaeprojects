package com.protostar.billingnstock.invoice.entities;

import java.util.Date;

public class PaymentReceivedRecord {
	private Date recordDate;
	private Date receivedDate;

	private float receivedAmt;
	private String receivedVia;
	private String chequeNo;
	private String bankName;

	public enum PaymentTxnType {
		CASH, WIRE, BANKDEPOSIT
	}

}
