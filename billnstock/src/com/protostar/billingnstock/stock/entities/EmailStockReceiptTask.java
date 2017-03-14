package com.protostar.billingnstock.stock.entities;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Sendgrid;

public class EmailStockReceiptTask implements DeferredTask {

	private static final long serialVersionUID = 1L;
	private static final String EMAIL_SUBJECT = "Stock Receipt Approved: ";

	private String fromEmail;
	private String fromName;
	private int stockReceiptNumber;
	private String emailDLList;
	private String messageBody;

	public EmailStockReceiptTask(String fromEmail, String fromName, String messageBody, int stockReceiptNumber,
			String emailDLList) {

		this.fromEmail = fromEmail;
		this.fromName = fromName;
		this.messageBody = messageBody;
		this.stockReceiptNumber = stockReceiptNumber;
		this.emailDLList = emailDLList;
	}

	@Override
	public void run() {
		try {
			// Now using SendGrid API below;
			// Send grid email
			Sendgrid sendGridMail = new Sendgrid(Constants.SENDGRID_USERNAME, Constants.SENDGRID_PWD);
			sendGridMail.setTo(getEmailDLList()).setFrom(getFromEmail()).setReplyTo(getFromEmail())
					.setFromName(getFromName()).setSubject(EMAIL_SUBJECT + stockReceiptNumber).setText(getMessageBody())
					.setHtml(getMessageBody());
			sendGridMail.send();

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public int getStockReceiptNumber() {
		return stockReceiptNumber;
	}

	public void setStockReceiptNumber(int stockReceiptNumber) {
		this.stockReceiptNumber = stockReceiptNumber;
	}

	public String getEmailDLList() {
		return emailDLList;
	}

	public void setEmailDLList(String emailDLList) {
		this.emailDLList = emailDLList;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

}
