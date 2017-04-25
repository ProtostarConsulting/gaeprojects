package com.protostar.billingnstock.production.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.protostar.billingnstock.production.entities.BomEntity;
import com.protostar.billingnstock.purchase.entities.LineItemEntity;
import com.protostar.billingnstock.stock.entities.BomLineItemCategory;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

public class PrintPdfBOM extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfBOM() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long bomId = Long.parseLong(request.getParameter("bomId"));
		Long bid = Long.parseLong(request.getParameter("bid"));

		ProductionService productionService = new ProductionService();
		BomEntity bomEntityById = productionService.listBomEntityByID(bid, bomId);
		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "Bom" + "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='ProERP_" + fileNameAppend + ".pdf'");

		this.generatePdf(bomEntityById, outputStream);

	}

	public void generatePdf(BomEntity bomEntity, OutputStream outputStream) {

		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			Map<String, Object> root = new HashMap<String, Object>();
			PDFHtmlTemplateService.addDocumentHeaderLogo(bomEntity, document, root);

			List<BomLineItemCategory> productItemList = bomEntity.getCatList();
			root.put("productItemList", productItemList);

			DecimalFormat df = new DecimalFormat("#0.00");

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			Date createdDate = bomEntity.getCreatedDate();
			Date modifiedDate = bomEntity.getModifiedDate();
			String createdDateStr = sdfDate.format(createdDate);
			String modifiedDateStr = sdfDate.format(modifiedDate);
			root.put("createdDateStr", createdDateStr);
			root.put("modifiedDateStr", modifiedDateStr);
			UserEntity createdBy = bomEntity.getCreatedBy();
			root.put("createdBy", createdBy == null ? "" : createdBy.getFirstName() + " " + createdBy.getLastName());
			root.put("productName", "" + bomEntity.getStockItemType().getItemName().toString());
			Template temp = PDFHtmlTemplateService.getConfiguration()
					.getTemplate("pdf_templates/production_bom_tmpl.ftlh");

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
