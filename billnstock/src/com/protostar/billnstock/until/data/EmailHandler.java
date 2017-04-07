package com.protostar.billnstock.until.data;

import java.io.IOException;

import javax.mail.MessagingException;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.protostar.billingnstock.invoice.entities.InvoiceEmailTask;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.invoice.entities.QuotationEmailTask;
import com.protostar.billingnstock.invoice.entities.QuotationEntity;
import com.protostar.billingnstock.purchase.entities.EmailPOTask;
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billingnstock.purchase.entities.RequisitionEntity;
import com.protostar.billingnstock.stock.entities.EmailStockReceiptTask;
import com.protostar.billingnstock.stock.entities.EmailStockShipmentTask;
import com.protostar.billingnstock.stock.entities.RequisitionEmailTask;
import com.protostar.billingnstock.stock.entities.StockItemsReceiptEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.taskmangement.TaskAssignedEmail;
import com.protostar.billingnstock.taskmangement.TaskEntity;
import com.protostar.billingnstock.taskmangement.TaskEntity.TaskStatus;

public class EmailHandler {
	public void sendPurchaseOrderEmail(PurchaseOrderEntity documentEntity)
			throws MessagingException, IOException {

		String emailSubject = "Purchase Order No: "
				+ documentEntity.getItemNumber() + " Submitted/Updated";

		String messageBody = new EmailHtmlTemplateService()
				.purchaseOrderFinalizedEmail(documentEntity);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new EmailPOTask(
				documentEntity.getBusiness().getId(), documentEntity
						.getCreatedBy().getEmail_id(), emailSubject,
				messageBody, documentEntity.getItemNumber())));
	}

	public void sendStockShipmentEmail(StockItemsShipmentEntity documentEntity)
			throws MessagingException, IOException {

		String emailSubject = "Stock Shipment No: "
				+ documentEntity.getItemNumber() + " Submitted/Updated";

		String messageBody = new EmailHtmlTemplateService()
				.stockShipmentFinalizedEmail(documentEntity);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new EmailStockShipmentTask(
				documentEntity.getBusiness().getId(),
				documentEntity.getCreatedBy().getEmail_id(), emailSubject, messageBody,
				documentEntity.getItemNumber())));
	}

	public void sendStockReceiptEmail(StockItemsReceiptEntity documentEntity)
			throws MessagingException, IOException {

		String emailSubject = "Stock Receipt No:"
				+ documentEntity.getItemNumber() + " Submitted/Updated";
		String messageBody = new EmailHtmlTemplateService()
				.stockReceiptFinalizedEmail(documentEntity);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new EmailStockReceiptTask(
				documentEntity.getBusiness().getId(),
				documentEntity.getCreatedBy().getEmail_id(), emailSubject, messageBody,
				documentEntity.getItemNumber())));
	}

	public void sendRequisitionEmail(RequisitionEntity documentEntity) {

		String emailSubject = "Requisition No:"
				+ documentEntity.getItemNumber() + " Submitted/Updated";

		String messageBody = new EmailHtmlTemplateService()
				.requisitionEmail(documentEntity);

		Queue queue = QueueFactory.getDefaultQueue();

		queue.add(TaskOptions.Builder.withPayload(new RequisitionEmailTask(
				documentEntity.getBusiness().getId(),
				documentEntity.getCreatedBy().getEmail_id(), emailSubject, messageBody,
				documentEntity.getItemNumber())));

	}

	public void sendTaskAssignedEmail(TaskEntity documentEntity)
			throws MessagingException, IOException {

		String messageBody = new EmailHtmlTemplateService()
				.taskAssignedEmail(documentEntity);

		String emailSubject = "";

		TaskStatus currentTaskStatus = documentEntity.getTaskStatus();
		if (currentTaskStatus.equals(TaskStatus.COMPLETED)) {
			emailSubject = "Task Completed, Task No: "
					+ documentEntity.getItemNumber();
		} else {
			emailSubject = "Task Assigned/Updated,Task No: "
					+ documentEntity.getItemNumber();
		}

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new TaskAssignedEmail(
				documentEntity.getBusiness().getId(), documentEntity.getAssignedBy().getEmail_id(), documentEntity
						.getAssignedTo().getEmail_id(), emailSubject,
				messageBody)));

	}

	public void sendInvoiceEmail(InvoiceEntity documentEntity)
			throws MessagingException, IOException {

		String emailSubject = "Invoice No: " + documentEntity.getItemNumber()
				+ " Submitted/Updated";
		String messageBody = new EmailHtmlTemplateService()
				.invoiceFinalizedEmail(documentEntity);

		System.out.println("...message body for invoice length.."+messageBody.length());
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new InvoiceEmailTask(
				documentEntity.getBusiness().getId(),
				documentEntity.getCreatedBy().getEmail_id(), emailSubject, messageBody,
				documentEntity.getItemNumber())));
	}

	public void sendQuotationEmail(QuotationEntity documentEntity)
			throws MessagingException, IOException {

		String emailSubject = "Quotation No: " + documentEntity.getItemNumber()
				+ " Submitted/Updated";
		String messageBody = new EmailHtmlTemplateService()
				.quotationEmail(documentEntity);
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new QuotationEmailTask(
				documentEntity.getBusiness().getId(),
				documentEntity.getCreatedBy().getEmail_id(), emailSubject, messageBody,
				documentEntity.getItemNumber())));
	}

}
