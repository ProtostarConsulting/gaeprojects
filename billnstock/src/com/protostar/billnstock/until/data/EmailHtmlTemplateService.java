package com.protostar.billnstock.until.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billingnstock.stock.entities.StockItemsReceiptEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity.ShipmentType;
import com.protostar.billingnstock.taskmangement.TaskEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public class EmailHtmlTemplateService {

	static Configuration cfg = null;

	public Configuration getConfiguration() {
		if (cfg != null) {
			return cfg;
		}

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		cfg.setClassForTemplateLoading(this.getClass(), "/");
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		return cfg;
	}

	public String purchaseOrderFinalizedEmail(PurchaseOrderEntity purchaseOrder) {

		try {

			Map<String, Object> root = new HashMap<String, Object>();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			String poDate = sdfDate.format(purchaseOrder.getPoDate());

			root.put("createdBy", ""
					+ purchaseOrder.getCreatedBy().getFirstName() + " "
					+ purchaseOrder.getCreatedBy().getLastName());
			root.put("pODate", poDate);
			root.put("supplier", purchaseOrder.getSupplier().getSupplierName());
			root.put("finalTotal", purchaseOrder.getFinalTotal());

			BusinessEntity business = purchaseOrder.getBusiness();
			root.put("businessName", business.getBusinessName());
			root.put("businessAdressLine1", business.getAddress().getLine1());
			String line2 = business.getAddress().getLine2();
			String pin = business.getAddress().getPin();
			String country = business.getAddress().getCountry();
			if (line2 == null || line2.trim().isEmpty()) {
				line2 = null;
			}
			if (pin == null || pin.trim().isEmpty()) {
				pin = null;
			}
			if (country == null || country.trim().isEmpty()) {
				country = null;
			}
			root.put("businessAdressLine2", line2);
			root.put("businessAdressCity", business.getAddress().getCity());
			root.put("businessAdressPin", pin);
			root.put("businessAdressState", business.getAddress().getState());
			root.put("businessAdressCountry", country);

			Template temp = getConfiguration().getTemplate(
					"email_templates/purchase_order_email_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					500);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			return byteArrayOutputStream.toString();

		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}

		return "";
	}

	public String stockShipmentFinalizedEmail(
			StockItemsShipmentEntity stockShipment) {

		try {

			Map<String, Object> root = new HashMap<String, Object>();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			String shipmentDate = sdfDate.format(stockShipment
					.getShipmentDate());

			root.put("createdBy", ""
					+ stockShipment.getCreatedBy().getFirstName() + " "
					+ stockShipment.getCreatedBy().getLastName());
			root.put("shipmentDate", shipmentDate);
			root.put("finalTotal", stockShipment.getFinalTotal());
			root.put("fromWH", stockShipment.getFromWH().getWarehouseName());

			ShipmentType shipmentType = stockShipment.getShipmentType();
			if (shipmentType.equals(ShipmentType.TO_OTHER_WAREHOUSE)) {
				root.put("toWH", stockShipment.getToWH().getWarehouseName());
			}

			else if (shipmentType.equals(ShipmentType.TO_CUSTOMER)) {

				Customer customer = stockShipment.getCustomer();

				String custName = "";

				if (customer.getIsCompany()) {
					custName = customer.getCompanyName();
				} else {
					custName = customer.getFirstName() + " "
							+ customer.getLastName();
				}

				root.put("customerName", custName);
			} else if (shipmentType.equals(ShipmentType.TO_PARTNER)) {

				Customer partner = stockShipment.getCustomer();
				String partnerName = "";

				if (partner.getIsCompany()) {
					partnerName = partner.getCompanyName();
				} else {
					partnerName = partner.getFirstName() + " "
							+ partner.getLastName();
				}

				root.put("partnerName", partnerName);
			}

			BusinessEntity business = stockShipment.getBusiness();
			root.put("businessName", business.getBusinessName());
			root.put("businessAdressLine1", business.getAddress().getLine1());
			String line2 = business.getAddress().getLine2();
			String pin = business.getAddress().getPin();
			String country = business.getAddress().getCountry();
			if (line2 == null || line2.trim().isEmpty()) {
				line2 = null;
			}
			if (pin == null || pin.trim().isEmpty()) {
				pin = null;
			}
			if (country == null || country.trim().isEmpty()) {
				country = null;
			}
			root.put("businessAdressLine2", line2);
			root.put("businessAdressCity", business.getAddress().getCity());
			root.put("businessAdressPin", pin);
			root.put("businessAdressState", business.getAddress().getState());
			root.put("businessAdressCountry", country);

			Template temp = getConfiguration().getTemplate(
					"email_templates/stock_shipment_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					500);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			return byteArrayOutputStream.toString();

		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}

		return "";
	}

	public String stockReceiptFinalizedEmail(
			StockItemsReceiptEntity stockItemsReceipt) {

		try {

			Map<String, Object> root = new HashMap<String, Object>();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			String receiptDate = sdfDate.format(stockItemsReceipt
					.getReceiptDate());

			root.put("createdBy", ""
					+ stockItemsReceipt.getCreatedBy().getFirstName() + " "
					+ stockItemsReceipt.getCreatedBy().getLastName());
			root.put("receiptDate", receiptDate);
			root.put("supplier", stockItemsReceipt.getSupplier()
					.getSupplierName());
			root.put("warehouse", stockItemsReceipt.getWarehouse()
					.getWarehouseName());
			root.put("finalTotal", stockItemsReceipt.getFinalTotal());

			BusinessEntity business = stockItemsReceipt.getBusiness();
			root.put("businessName", business.getBusinessName());
			root.put("businessAdressLine1", business.getAddress().getLine1());
			String line2 = business.getAddress().getLine2();
			String pin = business.getAddress().getPin();
			String country = business.getAddress().getCountry();
			if (line2 == null || line2.trim().isEmpty()) {
				line2 = null;
			}
			if (pin == null || pin.trim().isEmpty()) {
				pin = null;
			}
			if (country == null || country.trim().isEmpty()) {
				country = null;
			}
			root.put("businessAdressLine2", line2);
			root.put("businessAdressCity", business.getAddress().getCity());
			root.put("businessAdressPin", pin);
			root.put("businessAdressState", business.getAddress().getState());
			root.put("businessAdressCountry", country);

			Template temp = getConfiguration().getTemplate(
					"email_templates/stock_receipt_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					500);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			return byteArrayOutputStream.toString();

		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}

		return "";
	}

	public String taskAssignedEmail(TaskEntity taskEntity) {

		try {

			Map<String, Object> root = new HashMap<String, Object>();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			String taskAssignedDate = sdfDate.format(taskEntity
					.getAssignedDate());
			String estCompletionDate = sdfDate.format(taskEntity
					.getEstCompletionDate());

			root.put("assignedBy", ""
					+ taskEntity.getAssignedBy().getFirstName() + " "
					+ taskEntity.getAssignedBy().getLastName());
			root.put("assignedTo", taskEntity.getAssignedTo().getFirstName()
					+ " " + taskEntity.getAssignedTo().getLastName());

			root.put("assignedDate", taskAssignedDate);
			root.put("taskNo", taskEntity.getItemNumber());
			root.put("estCompletionDate", estCompletionDate);

			root.put("taskTitle", taskEntity.getTaskTitle());
			root.put("taskDesciption", taskEntity.getTaskDesc());

			BusinessEntity business = taskEntity.getBusiness();
			root.put("businessName", business.getBusinessName());
			root.put("businessAdressLine1", business.getAddress().getLine1());

			String line2 = business.getAddress().getLine2();
			String pin = business.getAddress().getPin();
			String country = business.getAddress().getCountry();
			if (line2 == null || line2.trim().isEmpty()) {
				line2 = null;
			}
			if (pin == null || pin.trim().isEmpty()) {
				pin = null;
			}
			if (country == null || country.trim().isEmpty()) {
				country = null;
			}
			root.put("businessAdressLine2", line2);
			root.put("businessAdressCity", business.getAddress().getCity());
			root.put("businessAdressPin", pin);
			root.put("businessAdressState", business.getAddress().getState());
			root.put("businessAdressCountry", country);

			Template temp = getConfiguration().getTemplate(
					"email_templates/task_assigned_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					500);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			return byteArrayOutputStream.toString();

		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}

		return "";
	}
}
