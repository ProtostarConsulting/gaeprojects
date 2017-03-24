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
import com.protostar.billingnstock.purchase.entities.RequisitionEntity;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

public class PrintPdfRequisition extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfRequisition() {
		super();

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Long bid = Long.parseLong(request.getParameter("bid"));
		Long id = Long.parseLong(request.getParameter("id"));

		StockManagementService stockMgmtService = new StockManagementService();

		RequisitionEntity requisitionEntity = stockMgmtService
				.getRequisitionByID(bid, id);
		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "Requisition"
				+ requisitionEntity.getItemNumber() + "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='ProERP_"
				+ fileNameAppend + ".pdf'");

		this.generatePdf(requisitionEntity, outputStream);
	}

	public void generatePdf(RequisitionEntity requisitionEntity,
			OutputStream outputStream) {

		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			PDFHtmlTemplateService.addDocumentHeaderLogo(requisitionEntity,
					document, root);

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");

			Date createdDate = requisitionEntity.getCreatedDate();
			Date modifiedDate = requisitionEntity.getModifiedDate();
			String createdDateStr = sdfDate.format(createdDate);
			String modifiedDateStr = sdfDate.format(modifiedDate);

			root.put("itemNumber", requisitionEntity.getItemNumber());
			root.put("docStatus", requisitionEntity.getStatus());
			root.put("createdDateStr", createdDateStr);
			root.put("modifiedDateStr", modifiedDateStr);

			UserEntity createdBy = requisitionEntity.getCreatedBy();
			root.put("createdBy",
					createdBy == null ? "" : createdBy.getFirstName() + " "
							+ createdBy.getLastName());

			UserEntity approvedBy = requisitionEntity.getApprovedBy();
			root.put("approvedBy",
					approvedBy == null ? "" : approvedBy.getFirstName() + " "
							+ approvedBy.getLastName());

			if (requisitionEntity.getOnBehalfOf() != null
					&& !requisitionEntity.getOnBehalfOf().trim().isEmpty()) {
				root.put("onBehalfOf", requisitionEntity.getOnBehalfOf());
			}

			if (requisitionEntity.getNote() != null
					&& !requisitionEntity.getNote().trim().isEmpty()) {
				root.put("docNotes", requisitionEntity.getNote());
			}

			List<StockLineItem> serviceLineItemListForPO = requisitionEntity
					.getServiceLineItemList();

			if (serviceLineItemListForPO != null
					&& serviceLineItemListForPO.size() > 0) {
				root.put("serviceItemList", serviceLineItemListForPO);
			}
			Template temp = PDFHtmlTemplateService.getConfiguration()
					.getTemplate("pdf_templates/requisition_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			PDFHtmlTemplateService.addDocumentFooter(requisitionEntity, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
