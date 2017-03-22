package com.protostar.billnstock.until.data;

import java.io.IOException;

import javax.mail.MessagingException;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.protostar.billingnstock.invoice.entities.InvoiceEmailTask;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.invoice.entities.InvoiceSettingsEntity;
import com.protostar.billingnstock.invoice.entities.QuotationEmailTask;
import com.protostar.billingnstock.invoice.entities.QuotationEntity;
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
import com.protostar.billingnstock.user.entities.BusinessSettingsEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.sendgrid.Attachments;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Personalization;

public class EmailHandler {
	public void sendPurchaseOrderEmail(PurchaseOrderEntity documentEntity)
			throws MessagingException, IOException {

		StockSettingsEntity stockSettings = new StockManagementService()
				.getStockSettingsByBiz(documentEntity.getBusiness().getId());
		if (stockSettings == null || !stockSettings.isEmailNotification()
				|| stockSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}
		UserService userService = new UserService();
		BusinessSettingsEntity businessSettingsEntity = userService
				.getBusinessSettingsEntity(documentEntity.getBusiness().getId());
		String sendgrid_API_KEY = businessSettingsEntity.getSendGridAPIKey();
		if (sendgrid_API_KEY == null || sendgrid_API_KEY.isEmpty()) {
			return;
		}

		String emailSubject = "Purchase Order No: "
				+ documentEntity.getItemNumber() + " Submited/Updated";
		String messageBody = new EmailHtmlTemplateService()
				.purchaseOrderFinalizedEmail(documentEntity);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new EmailPOTask(
				sendgrid_API_KEY, documentEntity.getCreatedBy().getEmail_id(),
				stockSettings.getEmailNotificationDL(), emailSubject,
				messageBody, documentEntity.getItemNumber())));
	}

	public void sendStockShipmentEmail(StockItemsShipmentEntity documentEntity)
			throws MessagingException, IOException {

		StockSettingsEntity stockSettings = new StockManagementService()
				.getStockSettingsByBiz(documentEntity.getBusiness().getId());
		if (stockSettings == null || !stockSettings.isEmailNotification()
				|| stockSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}

		UserService userService = new UserService();
		BusinessSettingsEntity businessSettingsEntity = userService
				.getBusinessSettingsEntity(documentEntity.getBusiness().getId());
		String sendgrid_API_KEY = businessSettingsEntity.getSendGridAPIKey();
		if (sendgrid_API_KEY == null || sendgrid_API_KEY.isEmpty()) {
			return;
		}
		String emailSubject = "Stock Shipment No: "
				+ documentEntity.getItemNumber() + " Submited/Updated";

		String messageBody = new EmailHtmlTemplateService()
				.stockShipmentFinalizedEmail(documentEntity);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new EmailStockShipmentTask(
				sendgrid_API_KEY, "ganesh.lawande@protostar.co.in",
				stockSettings.getEmailNotificationDL(), emailSubject,
				messageBody, documentEntity.getItemNumber())));
	}

	public void sendStockReceiptEmail(StockItemsReceiptEntity documentEntity)
			throws MessagingException, IOException {

		StockSettingsEntity stockSettings = new StockManagementService()
				.getStockSettingsByBiz(documentEntity.getBusiness().getId());

		if (stockSettings == null || !stockSettings.isEmailNotification()
				|| stockSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}
		UserService userService = new UserService();
		BusinessSettingsEntity businessSettingsEntity = userService
				.getBusinessSettingsEntity(documentEntity.getBusiness().getId());
		String sendgrid_API_KEY = businessSettingsEntity.getSendGridAPIKey();
		if (sendgrid_API_KEY == null || sendgrid_API_KEY.isEmpty()) {
			return;
		}
		String emailSubject = "Stock Receipt No:"
				+ documentEntity.getItemNumber() + " Submited/Updated";
		String messageBody = new EmailHtmlTemplateService()
				.stockReceiptFinalizedEmail(documentEntity);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new EmailStockReceiptTask(
				sendgrid_API_KEY, "ganesh.lawande@protostar.co.in",
				stockSettings.getEmailNotificationDL(), emailSubject,
				messageBody, documentEntity.getItemNumber())));
	}

	public void sendTaskAssignedEmail(TaskEntity documentEntity)
			throws MessagingException, IOException {

		TaskSettingsEntity taskSettings = new TaskManagementService()
				.getTaskSettingsByBiz(documentEntity.getBusiness().getId());

		if (taskSettings == null || !taskSettings.isEmailNotification()) {
			return;
		}
		UserService userService = new UserService();
		BusinessSettingsEntity businessSettingsEntity = userService
				.getBusinessSettingsEntity(documentEntity.getBusiness().getId());
		String sendgrid_API_KEY = businessSettingsEntity.getSendGridAPIKey();
		if (sendgrid_API_KEY == null || sendgrid_API_KEY.isEmpty()) {
			return;
		}
		String messageBody = new EmailHtmlTemplateService()
				.taskAssignedEmail(documentEntity);

		String emailSubject = "";

		TaskStatus currentTaskStatus = documentEntity.getTaskStatus();
		if (currentTaskStatus.equals(TaskStatus.COMPLETED)) {
			emailSubject = "Task Completed, Task No: "
					+ documentEntity.getItemNumber();
		} else {
			emailSubject = "Task Assigned,Task No: "
					+ documentEntity.getItemNumber();
		}

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new TaskAssignedEmail(
				sendgrid_API_KEY, documentEntity.getAssignedBy().getEmail_id(),
				documentEntity.getAssignedTo().getEmail_id(), taskSettings
						.getEmailNotificationDL(), emailSubject, messageBody)));

	}

	public void sendInvoiceEmail(InvoiceEntity documentEntity)
			throws MessagingException, IOException {

		InvoiceSettingsEntity invoiceSettings = new InvoiceService()
				.getInvoiceSettingsByBiz((documentEntity.getBusiness().getId()));

		if (invoiceSettings == null || !invoiceSettings.isEmailNotification()
				|| invoiceSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}
		UserService userService = new UserService();
		BusinessSettingsEntity businessSettingsEntity = userService
				.getBusinessSettingsEntity(documentEntity.getBusiness().getId());
		String sendgrid_API_KEY = businessSettingsEntity.getSendGridAPIKey();
		if (sendgrid_API_KEY == null || sendgrid_API_KEY.isEmpty()) {
			return;
		}
		String emailSubject = "Invoice No: " + documentEntity.getItemNumber()
				+ " Submited/Updated";
		String messageBody = new EmailHtmlTemplateService()
				.invoiceFinalizedEmail(documentEntity);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new InvoiceEmailTask(
				sendgrid_API_KEY, "ganesh.lawande@protostar.co.in",
				invoiceSettings.getEmailNotificationDL(), emailSubject,
				messageBody, documentEntity.getItemNumber())));
	}

	public void sendQuotationEmail(QuotationEntity documentEntity)
			throws MessagingException, IOException {

		InvoiceSettingsEntity invoiceSettings = new InvoiceService()
				.getInvoiceSettingsByBiz((documentEntity.getBusiness().getId()));

		if (invoiceSettings == null || !invoiceSettings.isEmailNotification()
				|| invoiceSettings.getEmailNotificationDL().isEmpty()) {
			return;
		}
		UserService userService = new UserService();
		BusinessSettingsEntity businessSettingsEntity = userService
				.getBusinessSettingsEntity(documentEntity.getBusiness().getId());
		String sendgrid_API_KEY = businessSettingsEntity.getSendGridAPIKey();
		if (sendgrid_API_KEY == null || sendgrid_API_KEY.isEmpty()) {
			return;
		}
		String emailSubject = "Quotation No: " + documentEntity.getItemNumber()
				+ " Submited/Updated";
		String messageBody = new EmailHtmlTemplateService()
				.quotationEmail(documentEntity);
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new QuotationEmailTask(
				sendgrid_API_KEY, "ganesh.lawande@protostar.co.in",
				invoiceSettings.getEmailNotificationDL(), emailSubject,
				messageBody, documentEntity.getItemNumber())));
	}

	public static Mail buildKitchenSinkExample() throws IOException {
		Mail mail = new Mail();

		Email fromEmail = new Email();
		fromEmail.setName("Example User");
		fromEmail.setEmail("test@example.com");
		mail.setFrom(fromEmail);

		mail.setSubject("Hello World from the SendGrid Java Library");

		Personalization personalization = new Personalization();
		Email to = new Email();
		to.setName("Example User");
		to.setEmail("test1@example.com");
		personalization.addTo(to);
		to.setName("Example User");
		to.setEmail("test2@example.com");
		personalization.addTo(to);
		Email cc = new Email();
		cc.setName("Example User");
		cc.setEmail("test3@example.com");
		personalization.addCc(cc);
		cc.setName("Example User");
		cc.setEmail("test4@example.com");
		personalization.addCc(cc);
		Email bcc = new Email();
		bcc.setName("Example User");
		bcc.setEmail("test5@example.com");
		personalization.addBcc(bcc);
		bcc.setName("Example User");
		bcc.setEmail("test6@example.com");
		personalization.addBcc(bcc);
		personalization
				.setSubject("Hello World from the Personalized SendGrid Java Library");
		personalization.addHeader("X-Test", "test");
		personalization.addHeader("X-Mock", "true");
		personalization.addSubstitution("%name%", "Example User");
		personalization.addSubstitution("%city%", "Riverside");
		personalization.addCustomArg("user_id", "343");
		personalization.addCustomArg("type", "marketing");
		personalization.setSendAt(1443636843);
		mail.addPersonalization(personalization);

		Personalization personalization2 = new Personalization();
		Email to2 = new Email();
		to2.setName("Example User");
		to2.setEmail("test1@example.com");
		personalization2.addTo(to2);
		to2.setName("Example User");
		to2.setEmail("test2@example.com");
		personalization2.addTo(to2);
		Email cc2 = new Email();
		cc2.setName("Example User");
		cc2.setEmail("test3@example.com");
		personalization2.addCc(cc2);
		cc2.setName("Example User");
		cc2.setEmail("test4@example.com");
		personalization2.addCc(cc2);
		Email bcc2 = new Email();
		bcc2.setName("Example User");
		bcc2.setEmail("test5@example.com");
		personalization2.addBcc(bcc2);
		bcc2.setName("Example User");
		bcc2.setEmail("test6@example.com");
		personalization2.addBcc(bcc2);
		personalization2
				.setSubject("Hello World from the Personalized SendGrid Java Library");
		personalization2.addHeader("X-Test", "test");
		personalization2.addHeader("X-Mock", "true");
		personalization2.addSubstitution("%name%", "Example User");
		personalization2.addSubstitution("%city%", "Denver");
		personalization2.addCustomArg("user_id", "343");
		personalization2.addCustomArg("type", "marketing");
		personalization2.setSendAt(1443636843);
		mail.addPersonalization(personalization2);

		Content content = new Content();
		content.setType("text/plain");
		content.setValue("some text here");
		mail.addContent(content);
		content.setType("text/html");
		content.setValue("<html><body>some text here</body></html>");
		mail.addContent(content);

		Attachments attachments = new Attachments();
		attachments
				.setContent("TG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdC4gQ3JhcyBwdW12");
		attachments.setType("application/pdf");
		attachments.setFilename("balance_001.pdf");
		attachments.setDisposition("attachment");
		attachments.setContentId("Balance Sheet");
		mail.addAttachments(attachments);

		Attachments attachments2 = new Attachments();
		attachments2.setContent("BwdW");
		attachments2.setType("image/png");
		attachments2.setFilename("banner.png");
		attachments2.setDisposition("inline");
		attachments2.setContentId("Banner");
		mail.addAttachments(attachments2);

		// mail.setTemplateId("13b8f94f-bcae-4ec6-b752-70d6cb59f932");

		mail.addSection("%section1%", "Substitution Text for Section 1");
		mail.addSection("%section2%", "Substitution Text for Section 2");

		mail.addHeader("X-Test1", "1");
		mail.addHeader("X-Test2", "2");

		mail.addCategory("May");
		mail.addCategory("2016");

		mail.addCustomArg("campaign", "welcome");
		mail.addCustomArg("weekday", "morning");

		mail.setSendAt(1443636842);

		Email replyTo = new Email();
		replyTo.setName("Example User");
		replyTo.setEmail("test@example.com");
		mail.setReplyTo(replyTo);

		return mail;
	}
}
