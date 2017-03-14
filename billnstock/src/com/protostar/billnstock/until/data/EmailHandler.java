package com.protostar.billnstock.until.data;

import java.io.IOException;

import javax.mail.MessagingException;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billingnstock.purchase.entities.EmailPOTask;
import com.protostar.billingnstock.stock.entities.EmailStockReceiptTask;
import com.protostar.billingnstock.stock.entities.StockItemsReceiptEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.stock.entities.StockSettingsEntity;
import com.protostar.billingnstock.stock.entities.EmailStockShipmentTask;
import com.protostar.billingnstock.stock.services.StockManagementService;
import com.protostar.billingnstock.taskmangement.TaskAssignedEmail;
import com.protostar.billingnstock.taskmangement.TaskEntity;

public class EmailHandler {
	public void sendPurchaseOrderEmail(PurchaseOrderEntity purchaseOrder) throws MessagingException, IOException {

		StockSettingsEntity stockSettings = new StockManagementService()
				.getStockSettingsByBiz(purchaseOrder.getBusiness().getId());
		if (!stockSettings.isEmailNotification() || stockSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}
		String messageBody = new EmailHtmlTemplateService().purchaseOrderFinalizedEmail(purchaseOrder);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(
				new EmailPOTask("ganesh.lawande@protostar.co.in", stockSettings.getBusiness().getBusinessName(),
						messageBody, purchaseOrder.getItemNumber(), stockSettings.getEmailNotificationDL())));
	}

	public void sendStockShipmentEmail(StockItemsShipmentEntity stockShipment) throws MessagingException, IOException {

		StockSettingsEntity stockSettings = new StockManagementService()
				.getStockSettingsByBiz(stockShipment.getBusiness().getId());
		if (!stockSettings.isEmailNotification() || stockSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}
		String messageBody = new EmailHtmlTemplateService().stockShipmentFinalizedEmail(stockShipment);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new EmailStockShipmentTask("ganesh.lawande@protostar.co.in",
				stockSettings.getBusiness().getBusinessName(), messageBody, stockShipment.getItemNumber(),
				stockSettings.getEmailNotificationDL())));
	}

	public void sendStockReceiptEmail(StockItemsReceiptEntity stockReceipt) throws MessagingException, IOException {

		StockSettingsEntity stockSettings = new StockManagementService()
				.getStockSettingsByBiz(stockReceipt.getBusiness().getId());
		if (!stockSettings.isEmailNotification() || stockSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}
		String messageBody = new EmailHtmlTemplateService().stockReceiptFinalizedEmail(stockReceipt);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new EmailStockReceiptTask("ganesh.lawande@protostar.co.in",
				stockSettings.getBusiness().getBusinessName(), messageBody, stockReceipt.getItemNumber(),
				stockSettings.getEmailNotificationDL())));
	}

	public void sendTaskAssignedEmail(TaskEntity taskEntity) throws MessagingException, IOException {

		String messageBody = new EmailHtmlTemplateService().taskAssignedEmail(taskEntity);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new TaskAssignedEmail(taskEntity.getAssignedBy().getEmail_id(),
				taskEntity.getBusiness().getBusinessName(), messageBody, taskEntity.getItemNumber(),
				taskEntity.getAssignedTo().getEmail_id())));
	}
}
