package com.protostar.billingnstock.purchase.entities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.common.io.BaseEncoding;
import com.protostar.billingnstock.stock.services.PrintPdfPurchaseOrder;
import com.protostar.billingnstock.stock.services.StockManagementService;
import com.protostar.billnstock.service.BaseEmailTask;
import com.protostar.billnstock.until.data.Constants;
import com.sendgrid.Attachments;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

public class EmailPOTask extends BaseEmailTask {

	private static final long serialVersionUID = 1L;
	private int itemNumber;

	public EmailPOTask(long bizID, String fromEmail, String emailSubject, String messageBody, int itemNumber) {
		super(bizID, fromEmail, emailSubject, messageBody);
		this.itemNumber = itemNumber;
	}

	@Override
	public void run() {
		// expensive operation to be in the background goes here
		if (this.isSkipEmail())
			return;
		
		try {		

			SendGrid sg = new SendGrid(this.getSendGridAPIKey());
			sg.addRequestHeader("X-Mock", "true");

			Request request = new Request();
			request.method = Method.POST;
			request.endpoint = "mail/send";

			request.body = updateEmail(super.buildEmail()).build();
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

	public Mail updateEmail(Mail mail) {
		// Add PO PDF attachment
		StockManagementService stockManagementService = new StockManagementService();
		PurchaseOrderEntity poObject = stockManagementService.getPOByItemNumber(this.itemNumber);
		PrintPdfPurchaseOrder printPdfPurchaseOrder = new PrintPdfPurchaseOrder();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(Constants.DOCUMENT_DEFAULT_MAX_SIZE);
		printPdfPurchaseOrder.generatePdf(poObject, outputStream);
		String base64Content = BaseEncoding.base64().encode(outputStream.toByteArray());

		Attachments attachments = new Attachments();
		attachments.setContent(base64Content);
		attachments.setType("application/pdf");
		attachments.setFilename("PurchaseOrder_" + this.itemNumber + ".pdf");
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
}