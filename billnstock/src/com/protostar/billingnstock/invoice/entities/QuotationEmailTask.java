package com.protostar.billingnstock.invoice.entities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.common.io.BaseEncoding;
import com.protostar.billingnstock.invoice.services.InvoiceService;
import com.protostar.billingnstock.invoice.services.PrintPdfQuotation;
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

public class QuotationEmailTask extends InvoiceEmailTask {

	private static final long serialVersionUID = 1L;

	public QuotationEmailTask(long bizID, String fromEmail, String emailSubject, String messageBody, int itemNumber) {
		super(bizID, fromEmail, emailSubject, messageBody, itemNumber);
	}

	@Override
	public void run() {
		// expensive operation to be in the background goes here
		if (this.isSkipEmail())
			return;

		try {
			SendGrid sg = new SendGrid(this.getSENDGRID_API_KEY());
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
		fromEmail.setEmail(this.getFromEmail());
		mail.setFrom(fromEmail);

		Personalization personalization = new Personalization();

		Email selfTo = new Email();
		// cc.setName("Example User CC1");
		selfTo.setEmail(this.getFromEmail());
		personalization.addTo(selfTo);

		if (this.getEmailDLList() != null && !this.getEmailDLList().isEmpty()) {
			String[] emailIds = this.getEmailDLList().split(",");
			for (String emailId : emailIds) {
				// Can't have same email id in to, cc or bcc
				String trimedTo = emailId.trim();
				if (this.getFromEmail().equalsIgnoreCase(trimedTo))
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

		personalization.setSubject(this.getEmailSubject());
		personalization.addHeader("X-Test", "test");
		personalization.addHeader("X-Mock", "true");

		mail.addPersonalization(personalization);

		Content content = new Content();
		content.setType("text/html");
		content.setValue(this.getMessageBody());
		mail.addContent(content);

		InvoiceService invoiceService = new InvoiceService();
		QuotationEntity quotationEntity = invoiceService.getQuotationByItemNumber(this.getItemNumber());

		PrintPdfQuotation printPDFQuotation = new PrintPdfQuotation();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(Constants.DOCUMENT_DEFAULT_MAX_SIZE);
		printPDFQuotation.generatePdf(quotationEntity, outputStream);
		String base64Content = BaseEncoding.base64().encode(outputStream.toByteArray());

		Attachments attachments = new Attachments();
		attachments.setContent(base64Content);
		attachments.setType("application/pdf");
		attachments.setFilename("Quotation_" + this.getItemNumber() + ".pdf");
		attachments.setDisposition("attachment");
		mail.addAttachments(attachments);
		return mail;
	}
}
