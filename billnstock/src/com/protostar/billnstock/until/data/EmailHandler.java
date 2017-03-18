package com.protostar.billnstock.until.data;

import java.io.IOException;

import javax.mail.MessagingException;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.protostar.billingnstock.invoice.entities.InvoiceEmailTask;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.invoice.entities.InvoiceSettingsEntity;
import com.protostar.billingnstock.invoice.services.InvoiceService;
import com.protostar.billingnstock.purchase.entities.EmailPOTask;
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billingnstock.stock.entities.EmailStockReceiptTask;
import com.protostar.billingnstock.stock.entities.EmailStockShipmentTask;
import com.protostar.billingnstock.stock.entities.StockItemsReceiptEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.stock.entities.StockSettingsEntity;
import com.protostar.billingnstock.stock.services.StockManagementService;
import com.protostar.billingnstock.taskmangement.TaskAssignedEmail;
import com.protostar.billingnstock.taskmangement.TaskEntity;
import com.protostar.billingnstock.taskmangement.TaskEntity.TaskStatus;
import com.protostar.billingnstock.taskmangement.TaskManagementService;
import com.protostar.billingnstock.taskmangement.TaskSettingsEntity;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;

public class EmailHandler {
	public void sendPurchaseOrderEmail(PurchaseOrderEntity purchaseOrder)
			throws MessagingException, IOException {

		StockSettingsEntity stockSettings = new StockManagementService()
				.getStockSettingsByBiz(purchaseOrder.getBusiness().getId());
		if (stockSettings == null || !stockSettings.isEmailNotification()
				|| stockSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}

		String emailSubject = "";

		if (purchaseOrder.getStatus() == DocumentStatus.FINALIZED) {
			emailSubject = "Purchase Order No." + purchaseOrder.getItemNumber()
					+ " " + "Approved";
		}

		if (purchaseOrder.getStatus() == DocumentStatus.REJECTED) {
			emailSubject = "Purchase Order No." + purchaseOrder.getItemNumber()
					+ " " + "Rejected";
		}

		String messageBody = new EmailHtmlTemplateService()
				.purchaseOrderFinalizedEmail(purchaseOrder);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new EmailPOTask(
				"ganesh.lawande@protostar.co.in", stockSettings.getBusiness()
						.getBusinessName(), messageBody, stockSettings
						.getEmailNotificationDL(), emailSubject)));
	}

	public void sendStockShipmentEmail(StockItemsShipmentEntity stockShipment)
			throws MessagingException, IOException {

		StockSettingsEntity stockSettings = new StockManagementService()
				.getStockSettingsByBiz(stockShipment.getBusiness().getId());
		if (stockSettings == null || !stockSettings.isEmailNotification()
				|| stockSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}

		String emailSubject = "";

		if (stockShipment.getStatus() == DocumentStatus.FINALIZED) {
			emailSubject = "Stock Shipment No." + stockShipment.getItemNumber()
					+ " " + "Approved";
		}

		if (stockShipment.getStatus() == DocumentStatus.REJECTED) {
			emailSubject = "Stock Shipment No." + stockShipment.getItemNumber()
					+ " " + "Rejected";
		}

		String messageBody = new EmailHtmlTemplateService()
				.stockShipmentFinalizedEmail(stockShipment);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new EmailStockShipmentTask(
				"ganesh.lawande@protostar.co.in", stockSettings.getBusiness()
						.getBusinessName(), messageBody, stockSettings
						.getEmailNotificationDL(), emailSubject)));
	}

	public void sendStockReceiptEmail(StockItemsReceiptEntity stockReceipt)
			throws MessagingException, IOException {

		StockSettingsEntity stockSettings = new StockManagementService()
				.getStockSettingsByBiz(stockReceipt.getBusiness().getId());

		if (stockSettings == null || !stockSettings.isEmailNotification()
				|| stockSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}

		String emailSubject = "";

		if (stockReceipt.getStatus() == DocumentStatus.FINALIZED) {
			emailSubject = "Stock Receipt No." + stockReceipt.getItemNumber()
					+ " " + "Approved";
		}

		if (stockReceipt.getStatus() == DocumentStatus.REJECTED) {
			emailSubject = "Stock Receipt No." + stockReceipt.getItemNumber()
					+ " " + "Rejected";
		}

		String messageBody = new EmailHtmlTemplateService()
				.stockReceiptFinalizedEmail(stockReceipt);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new EmailStockReceiptTask(
				"ganesh.lawande@protostar.co.in", stockSettings.getBusiness()
						.getBusinessName(), messageBody, stockSettings
						.getEmailNotificationDL(), emailSubject)));
	}

	public void sendTaskAssignedEmail(TaskEntity taskEntity)
			throws MessagingException, IOException {

		TaskSettingsEntity taskSettings = new TaskManagementService()
				.getTaskSettingsByBiz(taskEntity.getBusiness().getId());

		if (taskSettings == null || !taskSettings.isEmailNotification()
				|| taskSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}

		String messageBody = new EmailHtmlTemplateService()
				.taskAssignedEmail(taskEntity);

		String emailSubject = "";

		TaskStatus currentTaskStatus = taskEntity.getTaskStatus();
		if (currentTaskStatus.equals(TaskStatus.COMPLETED)) {
			emailSubject = "Task Completed, Task No:"
					+ taskEntity.getItemNumber();
		} else {
			emailSubject = "Task Assigned,Task No: "
					+ taskEntity.getItemNumber();
		}
		
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new TaskAssignedEmail(
				"ganesh.lawande@protostar.co.in", taskEntity.getBusiness()
						.getBusinessName(), messageBody, taskSettings
						.getEmailNotificationDL(), emailSubject)));
	}

	public void sendInvoiceEmail(InvoiceEntity invoice)
			throws MessagingException, IOException {

		InvoiceSettingsEntity invoiceSettings = new InvoiceService()
				.getInvoiceSettingsByBiz((invoice.getBusiness().getId()));

		if (invoiceSettings == null || !invoiceSettings.isEmailNotification()
				|| invoiceSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}

		String emailSubject = "";

		if (invoice.getStatus() == DocumentStatus.FINALIZED) {
			emailSubject = "Invoice No." + invoice.getItemNumber() + " "
					+ "Approved";
		}

		if (invoice.getStatus() == DocumentStatus.REJECTED) {
			emailSubject = "Invoice No." + invoice.getItemNumber() + " "
					+ "Rejected";
		}
		String messageBody = new EmailHtmlTemplateService()
				.invoiceFinalizedEmail(invoice);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new InvoiceEmailTask(
				"ganesh.lawande@protostar.co.in", invoiceSettings.getBusiness()
						.getBusinessName(), messageBody, invoiceSettings
						.getEmailNotificationDL(), emailSubject)));
	}
}
