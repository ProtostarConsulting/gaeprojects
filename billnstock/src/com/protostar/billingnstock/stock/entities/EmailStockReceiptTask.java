package com.protostar.billingnstock.stock.entities;

import com.protostar.billnstock.service.BaseEmailTask;

public class EmailStockReceiptTask extends BaseEmailTask {

	private static final long serialVersionUID = 1L;

	public EmailStockReceiptTask(String SENDGRID_API_KEY, String fromEmail, String emailDLList, String emailSubject,
			String messageBody) {
		super(SENDGRID_API_KEY, fromEmail, emailDLList, emailSubject, messageBody);
	}
}
