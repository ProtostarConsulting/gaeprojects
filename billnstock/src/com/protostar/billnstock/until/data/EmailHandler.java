package com.protostar.billnstock.until.data;

import java.io.IOException;

import javax.mail.MessagingException;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billingnstock.purchase.entities.SendPOFinalizedEmailAsyncOperation;
import com.protostar.billingnstock.stock.entities.StockSettingsEntity;
import com.protostar.billingnstock.stock.services.StockManagementService;

public class EmailHandler {
	public void sendPurchaseOrderEmail(PurchaseOrderEntity purchaseOrder)
			throws MessagingException, IOException {

		StockSettingsEntity stockSettings = new StockManagementService()
				.getStockSettingsByBiz(purchaseOrder.getBusiness().getId());
		if (!stockSettings.isEmailNotification()
				|| stockSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}
		String messageBody = new EmailHtmlTemplateService()
				.purchaseOrderFinalizedEmail(purchaseOrder);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder
				.withPayload(new SendPOFinalizedEmailAsyncOperation(
						"ganesh.lawande@protostar.co.in", stockSettings
								.getBusiness().getBusinessName(), messageBody,
						purchaseOrder.getItemNumber(), stockSettings
								.getEmailNotificationDL())));
	}

}
