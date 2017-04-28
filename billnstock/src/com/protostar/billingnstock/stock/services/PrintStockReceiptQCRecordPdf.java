package com.protostar.billingnstock.stock.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.protostar.billingnstock.production.entities.QCParameter;
import com.protostar.billingnstock.production.entities.QCParameterRecord;
import com.protostar.billingnstock.stock.entities.StockItemsReceiptEntity;
import com.protostar.billingnstock.stock.entities.StockReceiptQCEntity;
import com.protostar.billingnstock.stock.entities.StockReceiptQCRecord;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

/**
 * Servlet implementation class PrintStockReceiptQCRecordPdf
 */
public class PrintStockReceiptQCRecordPdf extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PrintStockReceiptQCRecordPdf() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Long receiptId = Long.parseLong(request.getParameter("receiptId"));
		Long bid = Long.parseLong(request.getParameter("bid"));
		
		StockManagementService stockService = new StockManagementService();
		StockItemsReceiptEntity receiptEntity = stockService.getStockReceiptByID(bid, receiptId);
		response.setContentType("application/PDF");
		
		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "StockReceiptQCRecord" + "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='ProERP_" + fileNameAppend + ".pdf'");

		this.generatePdf(receiptEntity, outputStream);
	}
	private void generatePdf(StockItemsReceiptEntity receiptEntity,
			ServletOutputStream outputStream) {
		// TODO Auto-generated method stub
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			Map<String, Object> root = new HashMap<String, Object>();
			PDFHtmlTemplateService.addDocumentHeaderLogo(receiptEntity, document, root);

			DecimalFormat df = new DecimalFormat("#0.00");

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			TimeZone timeZone=TimeZone.getTimeZone("IST");
			sdfDate.setTimeZone(timeZone);
			Date createdDate = receiptEntity.getCreatedDate();
			Date modifiedDate = receiptEntity.getModifiedDate();
			String createdDateStr = sdfDate.format(createdDate);
			String modifiedDateStr = sdfDate.format(modifiedDate);
			root.put("createdDateStr", createdDateStr);
			root.put("modifiedDateStr", modifiedDateStr);
			UserEntity createdBy = receiptEntity.getCreatedBy();
			root.put("createdBy", createdBy == null ? "" : createdBy.getFirstName() + " " + createdBy.getLastName());
			int itemNumber = receiptEntity.getItemNumber();
			String receiptNo = Integer.toString(itemNumber);
			root.put("receiptNo", receiptNo);
			List<StockReceiptQCRecord> receiptQCList = receiptEntity.getRecepitQCList();
			List parameterRecordList = new ArrayList();
			
			for (int i = 0; i < receiptQCList.size(); i++) {
				parameterRecordList.add(receiptQCList.get(i).getParamRecordedValues());				
			}
			root.put("parameterRecordList", parameterRecordList);
			
			Template temp = PDFHtmlTemplateService.getConfiguration()
					.getTemplate("/pdf_templates/stock_receipt_qcrecord_tmpl.ftlh");
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					Constants.DOCUMENT_DEFAULT_MAX_SIZE);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			String pdfXMLContent = byteArrayOutputStream.toString();
			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}}
		
}
