package com.protostar.billingnstock.purchase.entities;

import java.io.ByteArrayOutputStream;

import com.google.common.io.BaseEncoding;
import com.protostar.billingnstock.stock.services.PrintPdfPurchaseOrder;
import com.protostar.billingnstock.stock.services.StockManagementService;
import com.protostar.billnstock.service.BaseEmailTask;
import com.protostar.billnstock.until.data.Constants;
import com.sendgrid.Attachments;
import com.sendgrid.Mail;

public class EmailPOTask extends BaseEmailTask {

	private static final long serialVersionUID = 1L;
	private int itemNumber;

	public EmailPOTask(String SENDGRID_API_KEY, String fromEmail, String emailDLList, String emailSubject,
			String messageBody, int itemNumber) {
		super(SENDGRID_API_KEY, fromEmail, emailDLList, emailSubject, messageBody);
		this.itemNumber = itemNumber;
		setTaskEmail(buildEmail());
	}

	@Override
	public Mail buildEmail() {
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
		getTaskEmail().addAttachments(attachments);

		return getTaskEmail();
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}
}