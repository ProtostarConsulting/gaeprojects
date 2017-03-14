package com.protostar.billingnstock.purchase.entities;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Sendgrid;

public class EmailPOTask implements DeferredTask {

	private static final long serialVersionUID = 1L;
	private static final String EMAIL_SUBJECT = "Purchase Order Approved: ";

	private String fromEmail;
	private String fromName;
	private int poNumber;
	private String emailDLList;
	private String messageBody;

	public EmailPOTask(String fromEmail,
			String fromName, String messageBody, int poNumber,
			String emailDLList) {

		this.fromEmail = fromEmail;
		this.fromName = fromName;
		this.messageBody = messageBody;
		this.poNumber = poNumber;
		this.emailDLList = emailDLList;
	}

	@Override
	public void run() {
		// expensive operation to be in the background goes here

		try {
			// Now using SendGrid API below;
			// Send grid email
			Sendgrid sendGridMail = new Sendgrid(Constants.SENDGRID_USERNAME, Constants.SENDGRID_PWD);
			sendGridMail.setTo(getEmailDLList()).setFrom(getFromEmail())
					.setReplyTo(getFromEmail()).setFromName(getFromName())
					.setSubject(EMAIL_SUBJECT + poNumber)
					.setText(getMessageBody()).setHtml(getMessageBody());
			sendGridMail.send();

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public String getEmailDLList() {
		return emailDLList;
	}

	public void setEmailDLList(String emailDLList) {
		this.emailDLList = emailDLList;
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

}