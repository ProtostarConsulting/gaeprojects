package com.protostar.billingnstock.taskmangement;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Sendgrid;

public class TaskAssignedEmail implements DeferredTask {
	private static final long serialVersionUID = 1L;

	private String fromEmail;
	private String fromName;
	private String messageBody;
	private String emailTo;
	private String emailSubject;

	public TaskAssignedEmail(String fromEmail, String fromName,
			String messageBody, String emailTo, String emailSubject) {

		this.fromEmail = fromEmail;
		this.fromName = fromName;
		this.messageBody = messageBody;
		this.emailTo = emailTo;
		this.emailSubject = emailSubject;

	}

	@Override
	public void run() {
		try {
			// Now using SendGrid API below;
			// Send grid email
			Sendgrid sendGridMail = new Sendgrid(Constants.SENDGRID_USERNAME,
					Constants.SENDGRID_PWD);
			sendGridMail.setTo(getEmailTo()).setFrom(getFromEmail())
					.setReplyTo(getFromEmail()).setFromName(getFromName())
					.setSubject(getEmailSubject()).setText(getMessageBody())
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

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

}
