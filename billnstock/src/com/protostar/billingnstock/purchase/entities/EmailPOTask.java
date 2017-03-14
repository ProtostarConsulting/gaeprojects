package com.protostar.billingnstock.purchase.entities;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Sendgrid;

public class EmailPOTask implements DeferredTask {

	private static final long serialVersionUID = 1L;

	private String fromEmail;
	private String fromName;
	private String emailDLList;
	private String messageBody;
	private String emailSubject;

	public EmailPOTask(String fromEmail, String fromName, String messageBody,
			String emailDLList, String emailSubject) {

		this.fromEmail = fromEmail;
		this.fromName = fromName;
		this.messageBody = messageBody;
		this.emailDLList = emailDLList;
		this.emailSubject = emailSubject;
	}

	@Override
	public void run() {
		// expensive operation to be in the background goes here

		try {
			// Now using SendGrid API below;
			// Send grid email
			System.out.println("Sending Email async");
			Sendgrid sendGridMail = new Sendgrid(Constants.SENDGRID_USERNAME,
					Constants.SENDGRID_PWD);
			sendGridMail.setTo(getEmailDLList()).setFrom(getFromEmail())
					.setReplyTo(getFromEmail()).setFromName(getFromName())
					.setSubject(getEmailSubject()).setText(getMessageBody())
					.setHtml(getMessageBody());
			sendGridMail.send();
			System.out.println("Done Sending Email async...........");

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

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

}