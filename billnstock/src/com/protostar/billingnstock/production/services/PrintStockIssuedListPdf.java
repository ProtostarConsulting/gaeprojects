package com.protostar.billingnstock.production.services;

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
import com.protostar.billingnstock.production.entities.ProductionRequisitionEntity;
import com.protostar.billingnstock.production.entities.StockShipmentAgainstProductionRequisition;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.stock.entities.StockLineItemsByCategory;
import com.protostar.billingnstock.stock.entities.StockLineItemsByWarehouse;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

public class PrintStockIssuedListPdf extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PrintStockIssuedListPdf() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Long prodRequisitionId = Long.parseLong(request.getParameter("prodRequisitionId"));
		Long bid = Long.parseLong(request.getParameter("bid"));
		Long prodStockIssueId = Long.parseLong(request.getParameter("prodStockIssueId"));
		
		ProductionService prodService = new ProductionService();
		ProductionRequisitionEntity prodReqEntity = prodService.getRequisitionEntityByID(bid, prodRequisitionId);
		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "StockIssuedPdf" + "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='ProERP_" + fileNameAppend + ".pdf'");

		this.generatePdf(prodReqEntity, prodStockIssueId, outputStream);
	}

	private void generatePdf(ProductionRequisitionEntity prodReqEntity, Long prodStockIssueId,
			ServletOutputStream outputStream) {
		// TODO Auto-generated method stub
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			Map<String, Object> root = new HashMap<String, Object>();
			PDFHtmlTemplateService.addDocumentHeaderLogo(prodReqEntity, document, root);

			DecimalFormat df = new DecimalFormat("#0.00");
			
			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a");
			TimeZone timeZone=TimeZone.getTimeZone("IST");
			sdfDate.setTimeZone(timeZone);
			sdfTime.setTimeZone(timeZone);
			Date createdDate = prodReqEntity.getCreatedDate();
			Date modifiedDate = prodReqEntity.getModifiedDate();
			String createdDateStr = sdfDate.format(createdDate);
			String modifiedDateStr = sdfDate.format(modifiedDate);
			root.put("createdDateStr", createdDateStr);
			root.put("modifiedDateStr", modifiedDateStr);
			UserEntity createdBy = prodReqEntity.getCreatedBy();
			root.put("createdBy", createdBy == null ? "" : createdBy.getFirstName() + " " + createdBy.getLastName());
			int prodRequisitionNo = prodReqEntity.getItemNumber();
			String requistionNo = Integer.toString(prodRequisitionNo);
			root.put("requistionNo", requistionNo);
			List<StockShipmentAgainstProductionRequisition> stockShipmentProdReqList = prodReqEntity.getStockShipmentList();
			root.put("stockShipmentProdReqList", stockShipmentProdReqList);
			ProductionService pordService = new ProductionService();
			StockShipmentAgainstProductionRequisition stockShipProdReq = pordService.getStockShipmentProdRequisitionByID(prodReqEntity.getBusiness().getId(), prodStockIssueId);
			List<StockLineItemsByWarehouse> warehouseList = stockShipProdReq.getFromWarehouseList();
			root.put("warehouseList", warehouseList);
			Date devliveryDate = stockShipProdReq.getDeliveryDateTime();
			String deliveryDateStr = sdfDate.format(devliveryDate);
			root.put("deliveryDateStr", deliveryDateStr);
			int issueNo = stockShipProdReq.getItemNumber();
			String stockIssueNo = Integer.toString(issueNo);
			root.put("stockIssueNo", stockIssueNo);
			DocumentStatus status = stockShipProdReq.getStatus();
			root.put("status", status.toString());
			
			Template temp = PDFHtmlTemplateService.getConfiguration()
					.getTemplate("pdf_templates/stock_issued_list_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					Constants.DOCUMENT_DEFAULT_MAX_SIZE);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			String pdfXMLContent = byteArrayOutputStream.toString();
			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}