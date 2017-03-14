package com.protostar.billingnstock.taskmangement;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Sendgrid;

public class TaskAssignedEmail implements DeferredTask {
	private static final long serialVersionUID = 1L;
	private static final String EMAIL_SUBJECT = "Task Assigned/Updated, Task No: ";

	private String fromEmail;
	private String fromName;
	private String messageBody;
	private int taskItemNumber;
	private String emailTo;

	public TaskAssignedEmail(String fromEmail, String fromName, String messageBody, int taskItemNumber,
			String emailTo) {

		this.fromEmail = fromEmail;
		this.fromName = fromName;
		this.messageBody = messageBody;
		this.taskItemNumber = taskItemNumber;
		this.emailTo = emailTo;

	}

	@Override
	public void run() {
		try {
			// Now using SendGrid API below;
			// Send grid email
			Sendgrid sendGridMail = new Sendgrid(Constants.SENDGRID_USERNAME, Constants.SENDGRID_PWD);
			sendGridMail.setTo(getEmailTo()).setFrom(getFromEmail()).setReplyTo(getFromEmail())
					.setFromName(getFromName()).setSubject(EMAIL_SUBJECT + taskItemNumber).setText(getMessageBody())
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

	public int getTaskItemNumber() {
		return taskItemNumber;
	}

	public void setTaskItemNumber(int taskItemNumber) {
		this.taskItemNumber = taskItemNumber;
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

}
