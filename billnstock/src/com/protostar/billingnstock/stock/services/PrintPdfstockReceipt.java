package com.protostar.billingnstock.stock.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
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
import com.protostar.billingnstock.stock.entities.StockItemsReceiptEntity;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.NumberToRupees;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

public class PrintPdfstockReceipt extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfstockReceipt() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long stRcptId = Long.parseLong(request.getParameter("stRcptId"));
		Long bid = Long.parseLong(request.getParameter("bid"));

		StockManagementService stockMgmtService = new StockManagementService();
		StockItemsReceiptEntity stockItemsReceipt = stockMgmtService.getStockReceiptByID(bid, stRcptId);

		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "StockReceipt_" + stockItemsReceipt.getItemNumber() + "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='ProERP_" + fileNameAppend + ".pdf'");

		this.generatePdf(stockItemsReceipt, outputStream);

	}

	public void generatePdf(StockItemsReceiptEntity stockReceiptEntity,OutputStream outputStream) {
		try {

			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			PDFHtmlTemplateService.addDocumentHeaderLogo(stockReceiptEntity, document, root);

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			String createdDateStr = sdfDate.format(stockReceiptEntity.getCreatedDate());
			String modifiedDateStr = sdfDate.format(stockReceiptEntity.getModifiedDate());
			root.put("poNum", stockReceiptEntity.getPoNumber());
			root.put("createdDate", createdDateStr);
			root.put("modifiedDate", modifiedDateStr);
			root.put("receiptNo", stockReceiptEntity.getItemNumber());
			root.put("docStatus", stockReceiptEntity.getStatus());
			root.put("createdBy", stockReceiptEntity.getCreatedBy().getFirstName() + " "
					+ stockReceiptEntity.getCreatedBy().getLastName());
			UserEntity approvedBy = stockReceiptEntity.getApprovedBy();
			root.put("approvedBy",
					approvedBy == null ? "" : approvedBy.getFirstName() + " " + approvedBy.getLastName());
			root.put("supplier", stockReceiptEntity.getSupplier().getSupplierName());
			root.put("warehouse", stockReceiptEntity.getWarehouse().getWarehouseName());

			StringBuffer buffer = new StringBuffer();
			Address warehouseAdd = stockReceiptEntity.getWarehouse().getAddress();
			if (warehouseAdd != null) {
				if (warehouseAdd.getLine1() != null && !warehouseAdd.getLine1().isEmpty())
					buffer.append(warehouseAdd.getLine1());
				if (warehouseAdd.getLine2() != null && !warehouseAdd.getLine2().isEmpty())
					buffer.append(", " + warehouseAdd.getLine2());
				if (warehouseAdd.getCity() != null && !warehouseAdd.getCity().isEmpty())
					buffer.append(", " + warehouseAdd.getCity());
				if (warehouseAdd.getState() != null && !warehouseAdd.getState().isEmpty())
					buffer.append(", " + warehouseAdd.getState());
			}

			String warehouseAddress = buffer.toString();
			root.put("warehouseAddress", warehouseAddress);

			StringBuffer addressBuffer = new StringBuffer();
			Address supplierAddress = stockReceiptEntity.getSupplier().getAddress();
			if (supplierAddress != null) {
				if (supplierAddress.getLine1() != null && !supplierAddress.getLine1().isEmpty())
					addressBuffer.append(supplierAddress.getLine1());
				if (supplierAddress.getLine2() != null && !supplierAddress.getLine2().isEmpty())
					addressBuffer.append(", " + supplierAddress.getLine2());
				if (supplierAddress.getCity() != null && !supplierAddress.getCity().isEmpty())
					addressBuffer.append(", " + supplierAddress.getCity());
				if (supplierAddress.getState() != null && !supplierAddress.getState().isEmpty())
					addressBuffer.append(", " + supplierAddress.getState());
			}

			String suplAdrs = addressBuffer.toString();
			root.put("supplierAddress", suplAdrs);

			root.put("receiptNote", stockReceiptEntity.getNote());

			List<StockLineItem> serviceLineItemList = stockReceiptEntity.getServiceLineItemList();
			List<StockLineItem> productLineItemList = stockReceiptEntity.getProductLineItemList();

			if (serviceLineItemList != null && serviceLineItemList.size() > 0) {
				root.put("serviceItemList", serviceLineItemList);
			}
			if (productLineItemList != null && productLineItemList.size() > 0) {
				root.put("productItemList", productLineItemList);
			}

			double finalTotal = stockReceiptEntity.getFinalTotal();
			root.put("finalTotal", finalTotal);

			NumberToRupees numberToRupees = new NumberToRupees(Math.round(finalTotal));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("finalTotalInWords", netInWords);

			Template temp = PDFHtmlTemplateService.getConfiguration()
					.getTemplate("pdf_templates/stock_receipt_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					Constants.DOCUMENT_DEFAULT_MAX_SIZE);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			PDFHtmlTemplateService.addDocumentFooter(stockReceiptEntity, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
