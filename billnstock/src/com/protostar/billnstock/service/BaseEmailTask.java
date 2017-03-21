package com.protostar.billnstock.service;

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

public class BaseEmailTask implements DeferredTask {

	private static final long serialVersionUID = 1L;

	private String fromEmail;
	private String emailDLList;
	private String emailSubject;
	private String messageBody;
	private String sendGridAPIKey;

	public BaseEmailTask(String sendGridAPIKey, String fromEmail, String emailDLList, String emailSubject,
			String messageBody) {
		this.sendGridAPIKey = sendGridAPIKey;
		this.fromEmail = fromEmail;
		this.emailDLList = emailDLList;
		this.emailSubject = emailSubject;
		this.messageBody = messageBody;
	}

	@Override
	public void run() {
		// expensive operation to be in the background goes here
		try {
			SendGrid sg = new SendGrid(this.sendGridAPIKey);
			sg.addRequestHeader("X-Mock", "true");

			Request request = new Request();
			request.method = Method.POST;
			request.endpoint = "mail/send";
			request.body = buildEmail().build();
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

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
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

		Email selfTo = new Email();
		// cc.setName("Example User CC1");
		selfTo.setEmail(this.fromEmail);
		personalization.addTo(selfTo);

		if (this.emailDLList != null && !this.emailDLList.isEmpty()) {
			String[] emailIds = this.emailDLList.split(",");
			for (String emailId : emailIds) {
				// Can't have same email id in to, cc or bcc
				String trimedTo = emailId.trim();
				if (this.fromEmail.equalsIgnoreCase(trimedTo))
					continue;

				Email cc = new Email();
				// to.setName("Example User To1");
				cc.setEmail(trimedTo);
				personalization.addCc(cc);
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

		// // Add PO PDF attachment
		// StockManagementService stockManagementService = new
		// StockManagementService();
		// PurchaseOrderEntity poObject =
		// stockManagementService.getPOByItemNumber(this.itemNumber);
		// PrintPdfPurchaseOrder printPdfPurchaseOrder = new
		// PrintPdfPurchaseOrder();
		//
		// ByteArrayOutputStream outputStream = new
		// ByteArrayOutputStream(Constants.DOCUMENT_DEFAULT_MAX_SIZE);
		// printPdfPurchaseOrder.generatePdf(poObject, outputStream);
		// String base64Content =
		// BaseEncoding.base64().encode(outputStream.toByteArray());
		//
		// Attachments attachments = new Attachments();
		// attachments.setContent(base64Content);
		// attachments.setType("application/pdf");
		// attachments.setFilename("PurchaseOrder_" + this.itemNumber + ".pdf");
		// attachments.setDisposition("attachment");
		// mail.addAttachments(attachments);

		Email replyTo = new Email();
		// replyTo.setName("ProERP Notification");
		replyTo.setEmail(this.fromEmail);
		mail.setReplyTo(replyTo);

		return mail;
	}

	public String getSendGridAPIKey() {
		return sendGridAPIKey;
	}

	public void setSendGridAPIKey(String sendGridAPIKey) {
		this.sendGridAPIKey = sendGridAPIKey;
	}

}
