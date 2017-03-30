package com.protostar.billingnstock.invoice.entities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.common.io.BaseEncoding;
import com.protostar.billingnstock.invoice.services.InvoiceService;
import com.protostar.billingnstock.invoice.services.PrintPdfInvoice;
import com.protostar.billingnstock.user.entities.BusinessSettingsEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billnstock.until.data.Constants;
import com.sendgrid.Attachments;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

public class InvoiceEmailTask implements DeferredTask {

	private static final long serialVersionUID = 1L;
	private boolean skipEmail = false;
	private int itemNumber;
	private String fromEmail;
	private String emailDLList;
	private String emailSubject;
	private String messageBody;
	private String SENDGRID_API_KEY;

	public InvoiceEmailTask(long bizId, String fromEmail, String emailSubject,
			String messageBody, int itemNumber) {

		InvoiceSettingsEntity invoiceSettings = new InvoiceService()
				.getInvoiceSettingsByBiz(bizId);

		if (invoiceSettings == null || !invoiceSettings.isEmailNotification()
				|| invoiceSettings.getEmailNotificationDL().isEmpty()) {
			this.skipEmail= true;
			return;
		}
		UserService userService = new UserService();
		BusinessSettingsEntity businessSettingsEntity = userService

		.getBusinessSettingsEntity(bizId);
		String sendgrid_API_KEY = businessSettingsEntity.getSendGridAPIKey();
		if (sendgrid_API_KEY == null || sendgrid_API_KEY.isEmpty()) {
			this.skipEmail= true;
			return;
		}
		this.itemNumber = itemNumber;
		this.fromEmail = fromEmail;
		this.emailDLList = invoiceSettings.getEmailNotificationDL();
		this.emailSubject = emailSubject;
		this.messageBody = messageBody;
		this.SENDGRID_API_KEY = sendgrid_API_KEY;
	}

	@Override
	public void run() {
		// expensive operation to be in the background goes here
		if (this.isSkipEmail())
			return;

		try {
			SendGrid sg = new SendGrid(this.SENDGRID_API_KEY);
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

		InvoiceService invoiceService = new InvoiceService();
		InvoiceEntity invoiceEntity = invoiceService
				.getInvoiceByItemNumber(this.itemNumber);

		PrintPdfInvoice printPdfInvoice = new PrintPdfInvoice();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
				Constants.DOCUMENT_DEFAULT_MAX_SIZE);
		printPdfInvoice.generatePdf(invoiceEntity, outputStream);
		String base64Content = BaseEncoding.base64().encode(
				outputStream.toByteArray());

		Attachments attachments = new Attachments();
		attachments.setContent(base64Content);
		attachments.setType("application/pdf");
		attachments.setFilename("Invoice_" + this.itemNumber + ".pdf");
		attachments.setDisposition("attachment");
		mail.addAttachments(attachments);
		return mail;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getEmailDLList() {
		return emailDLList;
	}

	public void setEmailDLList(String emailDLList) {
		this.emailDLList = emailDLList;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public String getSENDGRID_API_KEY() {
		return SENDGRID_API_KEY;
	}

	public void setSENDGRID_API_KEY(String sENDGRID_API_KEY) {
		SENDGRID_API_KEY = sENDGRID_API_KEY;
	}

	public boolean isSkipEmail() {
		return skipEmail;
	}

	public void setSkipEmail(boolean skipEmail) {
		this.skipEmail = skipEmail;
	}
}
