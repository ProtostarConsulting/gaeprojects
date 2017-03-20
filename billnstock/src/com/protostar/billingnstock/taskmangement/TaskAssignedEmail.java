package com.protostar.billingnstock.taskmangement;

import java.io.IOException;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.protostar.billnstock.until.data.Constants;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

public class TaskAssignedEmail implements DeferredTask {
	private static final long serialVersionUID = 1L;

	private String fromEmail;
	private String emailTo;
	private String emailDLList;
	private String emailSubject;
	private String messageBody;

	public TaskAssignedEmail(String fromEmail, String emailTo, String emailDLList, String emailSubject,
			String messageBody) {

		this.fromEmail = fromEmail;
		this.emailTo = emailTo;
		this.emailDLList = emailDLList;
		this.emailSubject = emailSubject;
		this.messageBody = messageBody;
	}

	@Override
	public void run() {
		try {

			SendGrid sg = new SendGrid(Constants.SENDGRID_API_KEY);
			sg.addRequestHeader("X-Mock", "true");

			Request request = new Request();
			Mail taskEmail = buildEmail();

			request.method = Method.POST;
			request.endpoint = "mail/send";
			request.body = taskEmail.build();
			Response response = sg.api(request);
			System.out.println(response.statusCode);
			System.out.println(response.body);
			System.out.println(response.headers);
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

	}

	public Mail buildEmail() {
		// See buildKitchenSinkExample() method in EmailHandler for detailed
		// usage.
		Mail mail = new Mail();

		Email fromEmail = new Email();
		fromEmail.setName(Constants.SENDGRID_FROM_EMAIL_NAME);
		fromEmail.setEmail(this.fromEmail);
		mail.setFrom(fromEmail);

		Personalization personalization = new Personalization();

		Email to = new Email();
		// cc.setName("Example User CC1");
		to.setEmail(this.emailTo);
		personalization.addTo(to);
		if (!this.emailTo.equalsIgnoreCase(this.fromEmail)) {
			Email selfCc = new Email();
			// to.setName("Example User To1");
			selfCc.setEmail(this.fromEmail);
			personalization.addCc(selfCc);
		}

		// Add DL list in CC.
		if (this.emailDLList != null && !this.emailDLList.isEmpty()) {
			String[] emailIds = this.emailDLList.split(",");
			for (String emailId : emailIds) {
				// Can't have same email id in to, cc or bcc
				String trimedTo = emailId.trim();
				if (this.emailTo.equalsIgnoreCase(trimedTo) || this.fromEmail.equalsIgnoreCase(trimedTo))
					continue;

				Email cc = new Email();
				// to.setName("Example User To1");
				cc.setEmail(trimedTo);
				personalization.addTo(cc);
			}
		}

		// Email bcc = new Email();
		// bcc.setName("Example User BCC1");
		// bcc.setEmail("info@protostar.co.in");
		// personalization.addBcc(bcc);

		personalization.setSubject(this.emailSubject);
		personalization.addHeader("X-Test", "test");
		personalization.addHeader("X-Mock", "true");

		mail.addPersonalization(personalization);

		Content content = new Content();
		content.setType("text/html");
		content.setValue(this.messageBody);
		mail.addContent(content);

		Email replyTo = new Email();
		//replyTo.setName(Constants.SENDGRID_FROM_EMAIL_NAME);
		replyTo.setEmail(this.fromEmail);
		mail.setReplyTo(replyTo);

		return mail;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
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

	public String getEmailDLList() {
		return emailDLList;
	}

	public void setEmailDLList(String emailDLList) {
		this.emailDLList = emailDLList;
	}

}
