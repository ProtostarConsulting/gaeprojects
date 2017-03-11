package com.protostar.billingnstock.stock.entities;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.protostar.billnstock.until.data.Sendgrid;

public class EmailStockShipmentTask implements DeferredTask {

	private static final long serialVersionUID = 1L;
	private static final String SENDGRID_USERNAME = "ganesh.lawande@protostar.co.in";
	private static final String SENDGRID_PWD = "sangram12";
	private static final String EMAIL_SUBJECT = "Stock Shipment Approved: ";

	private String fromEmail;
	private String fromName;
	private int shipmentNumber;
	private String emailDLList;
	private String messageBody;

	public EmailStockShipmentTask(String fromEmail, String fromName,
			String messageBody, int shipmentNumber, String emailDLList) {

		this.fromEmail = fromEmail;
		this.fromName = fromName;
		this.shipmentNumber = shipmentNumber;
		this.emailDLList = emailDLList;
		this.messageBody = messageBody;
	}

	@Override
	public void run() {

		try {
			// Now using SendGrid API below;
			// Send grid email
			Sendgrid sendGridMail = new Sendgrid(SENDGRID_USERNAME,
					SENDGRID_PWD);
			sendGridMail.setTo(getEmailDLList()).setFrom(getFromEmail())
					.setReplyTo(getFromEmail()).setFromName(getFromName())
					.setSubject(EMAIL_SUBJECT + shipmentNumber)
					.setText(getMessageBody()).setHtml(getMessageBody());
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

	public int getShipmentNumber() {
		return shipmentNumber;
	}

	public void setShipmentNumber(int shipmentNumber) {
		this.shipmentNumber = shipmentNumber;
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
