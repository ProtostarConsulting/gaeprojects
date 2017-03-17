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
import com.protostar.billingnstock.invoice.entities.QuotationEntity;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.until.data.NumberToRupees;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

public class PrintPdfQuotation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfQuotation() {
		super();

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Long quotnId = Long.parseLong(request.getParameter("quotnId"));
		Long bid = Long.parseLong(request.getParameter("bid"));

		InvoiceService invoiceService = new InvoiceService();

		QuotationEntity quotationEntity = invoiceService.getQuotationByID(bid,
				quotnId);

	response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "Quotation_" + quotationEntity.getItemNumber()
				+ "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='ProERP_"
				+ fileNameAppend + ".pdf'");

		this.generatePdf(quotationEntity,
				outputStream);

	}
	

	private void generatePdf(QuotationEntity quotationEntity, ServletOutputStream outputStream) {

		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			PDFHtmlTemplateService.addDocumentHeaderLogo(quotationEntity, document, root);

			DecimalFormat df = new DecimalFormat("#0.00");

			double discAmt = quotationEntity.getDiscAmount();
			if (discAmt > 0) {
				root.put("Discount", df.format(discAmt));
			}

			TaxEntity servTax = quotationEntity.getSelectedServiceTax();
			TaxEntity prodTax = quotationEntity.getSelectedProductTax();

			List<StockLineItem> serviceLineItemListForQuot = quotationEntity.getServiceLineItemList();
			List<StockLineItem> productLineItemListForQuot = quotationEntity.getProductLineItemList();

			if (serviceLineItemListForQuot != null && serviceLineItemListForQuot.size() > 0) {
				root.put("serviceItemList", serviceLineItemListForQuot);
				root.put("serviceTax", servTax);
			}
			if (productLineItemListForQuot != null && productLineItemListForQuot.size() > 0) {
				root.put("productItemList", productLineItemListForQuot);
				root.put("productTax", prodTax);
			}

			root.put("docStatus", quotationEntity.getStatus());
			root.put("createdBy", quotationEntity.getCreatedBy().getFirstName() + " "
					+ quotationEntity.getCreatedBy().getLastName());
			UserEntity approvedBy = quotationEntity.getApprovedBy();
			root.put("approvedBy",
					approvedBy == null ? "" : approvedBy.getFirstName() + " " + approvedBy.getLastName());

			Customer customer = quotationEntity.getCustomer();
			String custName = "";

			if (customer.getIsCompany()) {
				custName = customer.getCompanyName();
			} else {
				custName = customer.getFirstName() + " " + customer.getLastName();
			}

			root.put("CustomerName", custName);

			StringBuffer custaddressBuffer = new StringBuffer();
			Address customerAddress = customer.getAddress();
			if (customerAddress != null) {
				if (customerAddress.getLine1() != null && !customerAddress.getLine1().isEmpty())
					custaddressBuffer.append(customerAddress.getLine1());
				if (customerAddress.getLine2() != null && !customerAddress.getLine2().isEmpty())
					custaddressBuffer.append(", <br></br>" + customerAddress.getLine2());
				if (customerAddress.getCity() != null && !customerAddress.getCity().isEmpty())
					custaddressBuffer.append(",<br></br>" + customerAddress.getCity());
				if (customerAddress.getState() != null && !customerAddress.getState().isEmpty())
					custaddressBuffer.append(", " + customerAddress.getState());
			}

			String custAddressForQuot = custaddressBuffer.toString();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			Date today = quotationEntity.getCreatedDate();
			Date modifiedDate = quotationEntity.getModifiedDate();
			String quotationDate = sdfDate.format(today);
			String modifiedDateStr= sdfDate.format(modifiedDate);

			String noteToCust = quotationEntity.getNoteToCustomer();
			double finalTotal = quotationEntity.getFinalTotal();

			// Customer Details

			root.put("CustomerAddress", custAddressForQuot);

			root.put("noteToCustomer", "" + noteToCust);

			// Quotation Date and No
			root.put("createdDate", quotationDate);
			root.put("modifiedDate",modifiedDateStr);
			root.put("quotationNumber", quotationEntity.getItemNumber());

			root.put("finalTotal", finalTotal);
			NumberToRupees numberToRupees = new NumberToRupees(Math.round(finalTotal));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("finalTotalInWords", netInWords);

			Template temp = PDFHtmlTemplateService.getConfiguration().getTemplate("pdf_templates/quotation_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			PDFHtmlTemplateService.addDocumentFooter(quotationEntity, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
