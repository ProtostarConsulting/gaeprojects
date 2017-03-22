package com.protostar.billnstock.until.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.invoice.entities.QuotationEntity;
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billingnstock.stock.entities.StockItemsReceiptEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity.ShipmentType;
import com.protostar.billingnstock.taskmangement.TaskEntity;
import com.protostar.billingnstock.taskmangement.TaskEntity.TaskStatus;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.service.UtilityService;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;

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
			String poModifiedDate = sdfDate.format(purchaseOrder
					.getModifiedDate());

			String createdBy = purchaseOrder.getCreatedBy().getFirstName()
					+ " " + purchaseOrder.getCreatedBy().getLastName();
			String approverName = "NA";
			if (purchaseOrder.getApprovedBy() != null)
				approverName = purchaseOrder.getApprovedBy().getFirstName()
						+ " " + purchaseOrder.getApprovedBy().getLastName();

			root.put("createdBy", createdBy);
			root.put("stockModuleApprover", approverName);
			root.put("poModifiedDate", poModifiedDate);
			root.put("supplier", purchaseOrder.getSupplier().getSupplierName());
			root.put("finalTotal", purchaseOrder.getFinalTotal());

			String documentStatus = "";
			String docStatusLable = "";

			DocumentStatus status = purchaseOrder.getStatus();

			if (status == DocumentStatus.REJECTED) {
				documentStatus = "Rejected";
				docStatusLable = "Rejected By";
			} else {
				documentStatus = "Approved";
				docStatusLable = "Approved By";
			}
			root.put("documentStatus", documentStatus);
			root.put("docStatusLable", docStatusLable);

			addFooterParams(purchaseOrder, root);

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

	private void addFooterParams(BaseEntity documentEntity,
			Map<String, Object> root) {
		BusinessEntity business = documentEntity.getBusiness();
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

		String currentAppURL = UtilityService.getCurrentAppURL();
		root.put("currentAppURL", currentAppURL);
	}

	public String stockShipmentFinalizedEmail(
			StockItemsShipmentEntity documentEntity) {

		try {

			Map<String, Object> root = new HashMap<String, Object>();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			String shipmentModifiedDate = sdfDate.format(documentEntity
					.getModifiedDate());

			String approverName = "NA";
			if (documentEntity.getApprovedBy() != null)
				approverName = documentEntity.getApprovedBy().getFirstName()
						+ " " + documentEntity.getApprovedBy().getLastName();

			root.put("createdBy", documentEntity.getCreatedBy().getFirstName()
					+ " " + documentEntity.getCreatedBy().getLastName());
			root.put("stockModuleApprover", approverName);
			root.put("shipmentModifiedDate", shipmentModifiedDate);
			root.put("finalTotal", documentEntity.getFinalTotal());
			root.put("fromWH", documentEntity.getFromWH().getWarehouseName());

			String documentStatus = "";

			String docStatusLable = "";

			DocumentStatus status = documentEntity.getStatus();

			if (status == DocumentStatus.REJECTED) {
				documentStatus = "Rejected";
				docStatusLable = "Rejected By";
			} else if (status == DocumentStatus.SUBMITTED) {
				documentStatus = "Submitted";
				docStatusLable = "Approved By";
			} else {
				documentStatus = "Approved";
				docStatusLable = "Approved By";
			}
			root.put("documentStatus", documentStatus);
			root.put("docStatusLable", docStatusLable);

			ShipmentType shipmentType = documentEntity.getShipmentType();
			if (shipmentType.equals(ShipmentType.TO_OTHER_WAREHOUSE)) {
				root.put("toWH", documentEntity.getToWH().getWarehouseName());
			}

			else if (shipmentType.equals(ShipmentType.TO_CUSTOMER)) {

				Customer customer = documentEntity.getCustomer();

				String custName = "";

				if (customer.getIsCompany()) {
					custName = customer.getCompanyName();
				} else {
					custName = customer.getFirstName() + " "
							+ customer.getLastName();
				}

				root.put("customerName", custName);
			} else if (shipmentType.equals(ShipmentType.TO_PARTNER)) {

				Customer partner = documentEntity.getCustomer();
				String partnerName = "";

				if (partner.getIsCompany()) {
					partnerName = partner.getCompanyName();
				} else {
					partnerName = partner.getFirstName() + " "
							+ partner.getLastName();
				}

				root.put("partnerName", partnerName);
			}

			addFooterParams(documentEntity, root);

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
			StockItemsReceiptEntity documentEntity) {

		try {

			Map<String, Object> root = new HashMap<String, Object>();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			String receiptModifiedDate = sdfDate.format(documentEntity
					.getModifiedDate());

			String approverName = "NA";
			if (documentEntity.getApprovedBy() != null)
				approverName = documentEntity.getApprovedBy().getFirstName()
						+ " " + documentEntity.getApprovedBy().getLastName();

			String createdBy = documentEntity.getCreatedBy().getFirstName()
					+ " " + documentEntity.getCreatedBy().getLastName();

			root.put("createdBy", createdBy);
			root.put("stockModuleApprover", approverName);
			root.put("receiptModifiedDate", receiptModifiedDate);
			root.put("supplier", documentEntity.getSupplier().getSupplierName());
			root.put("warehouse", documentEntity.getWarehouse()
					.getWarehouseName());
			root.put("finalTotal", documentEntity.getFinalTotal());

			String documentStatus = "";

			String docStatusLable = "";

			DocumentStatus status = documentEntity.getStatus();

			if (status == DocumentStatus.FINALIZED) {
				documentStatus = "Approved";
				docStatusLable = "Approved By";
			} else if (status == DocumentStatus.SUBMITTED) {
				documentStatus = "Submitted";
				docStatusLable = "Approved By";
			} else if (status == DocumentStatus.REJECTED) {
				documentStatus = "Rejected";
				docStatusLable = "Rejected By";
			}
			root.put("documentStatus", documentStatus);
			root.put("docStatusLable", docStatusLable);

			addFooterParams(documentEntity, root);

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

	public String taskAssignedEmail(TaskEntity documentEntity) {

		try {

			Map<String, Object> root = new HashMap<String, Object>();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			String taskAssignedDate = sdfDate.format(documentEntity
					.getAssignedDate());

			String estCompletionDate = "";
			if (documentEntity.getEstCompletionDate() != null) {

				estCompletionDate = sdfDate.format(documentEntity
						.getEstCompletionDate());

			}

			String completionDate = "";
			if (documentEntity.getTaskStatus() == TaskStatus.COMPLETED) {

				completionDate = sdfDate.format(documentEntity
						.getCompletionDate());
			}

			root.put("assignedBy", ""
					+ documentEntity.getAssignedBy().getFirstName() + " "
					+ documentEntity.getAssignedBy().getLastName());
			root.put("assignedTo", documentEntity.getAssignedTo()
					.getFirstName()
					+ " "
					+ documentEntity.getAssignedTo().getLastName());

			root.put("assignedDate", taskAssignedDate);
			root.put("taskNo", documentEntity.getItemNumber());
			root.put("estCompletionDate", estCompletionDate);
			root.put("completionDate", completionDate);

			root.put("taskTitle", documentEntity.getTaskTitle());
			root.put("taskDesciption", documentEntity.getTaskDesc());

			TaskStatus taskStatus = documentEntity.getTaskStatus();
			root.put("taskStatus", taskStatus);

			addFooterParams(documentEntity, root);

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

	public String invoiceFinalizedEmail(InvoiceEntity documentEntity) {

		try {

			Map<String, Object> root = new HashMap<String, Object>();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			String modifiedDate = sdfDate.format(documentEntity
					.getModifiedDate());

			String approverName = "NA";
			if (documentEntity.getApprovedBy() != null)
				approverName = documentEntity.getApprovedBy().getFirstName()
						+ " " + documentEntity.getApprovedBy().getLastName();

			String createdBy = documentEntity.getCreatedBy().getFirstName()
					+ " " + documentEntity.getCreatedBy().getLastName();

			root.put("createdBy", createdBy);
			root.put("invoiceModuleApprover", approverName);
			root.put("modifiedDate", modifiedDate);
			root.put("fromWH", documentEntity.getFromWH().getWarehouseName());
			root.put("finalTotal", documentEntity.getFinalTotal());

			String documentStatus = "";

			String docStatusLable = "";

			DocumentStatus status = documentEntity.getStatus();

			if (status == DocumentStatus.FINALIZED) {
				documentStatus = "Approved";
				docStatusLable = "Approved By";
			}
			if (status == DocumentStatus.SUBMITTED) {
				documentStatus = "Submitted";
				docStatusLable = "Approved By";
			}
			if (status == DocumentStatus.REJECTED) {
				documentStatus = "Rejected";
				docStatusLable = "Rejected By";
			}
			root.put("documentStatus", documentStatus);
			root.put("docStatusLable", docStatusLable);

			Customer customer = documentEntity.getCustomer();

			String custName = "";

			if (customer.getIsCompany()) {
				custName = customer.getCompanyName();
			} else {
				custName = customer.getFirstName() + " "
						+ customer.getLastName();
			}

			root.put("customerName", custName);

			addFooterParams(documentEntity, root);

			Template temp = getConfiguration().getTemplate(
					"email_templates/invoice_tmpl.ftlh");

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

	public String quotationEmail(QuotationEntity documentEntity) {

		try {

			Map<String, Object> root = new HashMap<String, Object>();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			String modifiedDate = sdfDate.format(documentEntity
					.getModifiedDate());

			String approverName = "NA";
			if (documentEntity.getApprovedBy() != null)
				approverName = documentEntity.getApprovedBy().getFirstName()
						+ " " + documentEntity.getApprovedBy().getLastName();

			String createdBy = documentEntity.getCreatedBy().getFirstName()
					+ " " + documentEntity.getCreatedBy().getLastName();

			root.put("createdBy", createdBy);
			root.put("invoiceModuleApprover", approverName);
			root.put("modifiedDate", modifiedDate);
			root.put("fromWH", documentEntity.getFromWH().getWarehouseName());
			root.put("finalTotal", documentEntity.getFinalTotal());

			String documentStatus = "";

			String docStatusLable = "";

			DocumentStatus status = documentEntity.getStatus();

			if (status == DocumentStatus.FINALIZED) {
				documentStatus = "Approved";
				docStatusLable = "Approved By";
			}
			if (status == DocumentStatus.SUBMITTED) {
				documentStatus = "Submitted";
				docStatusLable = "Approved By";
			}
			if (status == DocumentStatus.REJECTED) {
				documentStatus = "Rejected";
				docStatusLable = "Rejected By";
			}
			root.put("documentStatus", documentStatus);
			root.put("docStatusLable", docStatusLable);

			Customer customer = documentEntity.getCustomer();

			String custName = "";

			if (customer.getIsCompany()) {
				custName = customer.getCompanyName();
			} else {
				custName = customer.getFirstName() + " "
						+ customer.getLastName();
			}

			root.put("customerName", custName);

			addFooterParams(documentEntity, root);

			Template temp = getConfiguration().getTemplate(
					"email_templates/quotation_tmpl.ftlh");

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
