package com.protostar.billingnstock.invoice.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.until.data.NumberToRupees;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

/**
 * Servlet implementation class PrintPdfInvoice
 */
public class PrintPdfInvoice extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfInvoice() {
		super();

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Long invoiceId = Long.parseLong(request.getParameter("invoiceId"));
		Long bid = Long.parseLong(request.getParameter("bid"));

		InvoiceService invoiceService = new InvoiceService();
		InvoiceEntity invoiceEntity = invoiceService.getInvoiceByID(bid,
				invoiceId);

		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "Invoice_" + invoiceEntity.getItemNumber()
				+ "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='ProERP_"
				+ fileNameAppend + ".pdf'");

		this.generatePdf(invoiceEntity, outputStream);
	}

	private void generatePdf(InvoiceEntity invoiceEntity,
			ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			PDFHtmlTemplateService.addDocumentHeaderLogo(invoiceEntity,
					document, root);

			DecimalFormat df = new DecimalFormat("#0.00");

			double discountAmt = invoiceEntity.getDiscAmount();
			String purchaseOrderNo = invoiceEntity.getpOrder();
			double finalTotal = invoiceEntity.getFinalTotal();

			TaxEntity serviceTax = invoiceEntity.getSelectedServiceTax();
			TaxEntity productTax = invoiceEntity.getSelectedProductTax();

			// Imported Sales entity to get sales order number
			// SalesOrderEntity soe = invoiceEntity.getSalesOrderId();

			// Long salesOrderNo = soe.getId();

			List<StockLineItem> serviceLineItemList = invoiceEntity
					.getServiceLineItemList();
			List<StockLineItem> productLineItemList = invoiceEntity
					.getProductLineItemList();

			root.put("docStatus", invoiceEntity.getStatus());
			root.put("createdBy", invoiceEntity.getCreatedBy().getFirstName()
					+ " " + invoiceEntity.getCreatedBy().getLastName());
			UserEntity approvedBy = invoiceEntity.getApprovedBy();
			root.put("approvedBy",
					approvedBy == null ? "" : approvedBy.getFirstName() + " "
							+ approvedBy.getLastName());

			// Imported Customer entity to get name and address
			Customer cust1 = invoiceEntity.getCustomer();

			String custName = "";

			if (cust1.getIsCompany()) {
				custName = cust1.getCompanyName();
			} else if (!cust1.getIsCompany()) {
				custName = cust1.getFirstName() + " " + cust1.getLastName();
			}
			root.put("CustomerName", custName);

			StringBuffer custaddressBuf = new StringBuffer();
			Address customerAddress = cust1.getAddress();
			if (customerAddress != null) {
				if (customerAddress.getLine1() != null
						&& !customerAddress.getLine1().isEmpty())
					custaddressBuf.append(customerAddress.getLine1());
				if (customerAddress.getLine2() != null
						&& !customerAddress.getLine2().isEmpty())
					custaddressBuf.append(", <br></br>"
							+ customerAddress.getLine2());
				if (customerAddress.getCity() != null
						&& !customerAddress.getCity().isEmpty())
					custaddressBuf.append(",<br></br>"
							+ customerAddress.getCity());
				if (customerAddress.getState() != null
						&& !customerAddress.getState().isEmpty())
					custaddressBuf.append(", " + customerAddress.getState());
			}

			String custAddress = custaddressBuf.toString();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			Date createdDate = invoiceEntity.getCreatedDate();
			Date modifiedDate = invoiceEntity.getModifiedDate();
			String createdDateStr = sdfDate.format(createdDate);
			String modifiedDateStr = sdfDate.format(modifiedDate);

			// Customer Details

			root.put("CustomerAddress", custAddress);
			// Invoice Details
			root.put("createdDate", createdDateStr);
			root.put("modifiedDate", modifiedDateStr);
			root.put("invoiceNumber", invoiceEntity.getItemNumber());

			// root.put("SalesOrderNo", salesOrderNo);

			if (serviceLineItemList != null && serviceLineItemList.size() > 0) {
				root.put("serviceItemList", serviceLineItemList);
				root.put("serviceTax", serviceTax);
			}
			if (productLineItemList != null && productLineItemList.size() > 0) {
				root.put("productItemList", productLineItemList);
				root.put("productTax", productTax);
			}

			root.put("serviceTax", serviceTax);
			// root.put("TaxPercentage", df.format(taxPercentage));
			// root.put("ServiceTaxTotal", df.format(serviceTaxTotal));
			// root.put("ServiceTotal", df.format(serviceTotal));

			// Product Table
			// root.put("productTotal", df.format(productTotal));
			root.put("finalTotal", finalTotal);
			NumberToRupees numberToRupees = new NumberToRupees(
					Math.round(finalTotal));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("finalTotalInWord", netInWords);
			// root.put("FinalInWords", invoiceEntity.getFinalTotal());

			root.put("PurchaseOrderNo", purchaseOrderNo);

			if (invoiceEntity.getNoteToCustomer() != null
					&& !invoiceEntity.getNoteToCustomer().trim().isEmpty())
				root.put("noteToCustomer",
						"" + invoiceEntity.getNoteToCustomer());
			if (invoiceEntity.getPaymentNotes() != null
					&& !invoiceEntity.getPaymentNotes().trim().isEmpty())
				root.put("paymentNotes", "" + invoiceEntity.getPaymentNotes());
			if (invoiceEntity.getTermsAndConditions() != null
					&& !invoiceEntity.getTermsAndConditions().trim().isEmpty())
				root.put("termsAndConditions",
						"" + invoiceEntity.getTermsAndConditions());

			if (discountAmt > 0) {
				root.put("Discount", df.format(discountAmt));
			}

			Template temp = PDFHtmlTemplateService.getConfiguration()
					.getTemplate("pdf_templates/invoice_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			PDFHtmlTemplateService.addDocumentFooter(invoiceEntity, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
