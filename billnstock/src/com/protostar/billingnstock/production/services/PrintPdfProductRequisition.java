package com.protostar.billingnstock.production.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

public class PrintPdfProductRequisition extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfProductRequisition() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long proId = Long.parseLong(request.getParameter("proId"));
		Long bid = Long.parseLong(request.getParameter("bid"));

		ProductionService productionService = new ProductionService();
		ProductionRequisitionEntity productionRequisitionEntity = productionService.getRequisitionEntityByID(bid,
				proId);

		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "Bom" + "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='ProERP_" + fileNameAppend + ".pdf'");

		this.generatePdf(productionRequisitionEntity, outputStream);

	}

	public void generatePdf(ProductionRequisitionEntity productionRequisitionEntity, OutputStream outputStream) {

		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			Map<String, Object> root = new HashMap<String, Object>();
			PDFHtmlTemplateService.addDocumentHeaderLogo(productionRequisitionEntity, document, root);

			root.put("productItemList", productionRequisitionEntity.getCatList());

			DecimalFormat df = new DecimalFormat("#0.00");

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a");
			TimeZone timeZone = TimeZone.getTimeZone("IST");
			sdfTime.setTimeZone(timeZone);
			Date createdDate = productionRequisitionEntity.getCreatedDate();
			Date modifiedDate = productionRequisitionEntity.getModifiedDate();
			String createdDateStr = sdfDate.format(createdDate);
			String modifiedDateStr = sdfDate.format(modifiedDate);
			root.put("createdDateStr", createdDateStr);
			root.put("modifiedDateStr", modifiedDateStr);
			UserEntity createdBy = productionRequisitionEntity.getCreatedBy();
			root.put("createdBy", createdBy == null ? "" : createdBy.getFirstName() + " " + createdBy.getLastName());
			root.put("productName",
					"" + productionRequisitionEntity.getBomEntity().getStockItemType().getItemName().toString());
			root.put("deliveryDate", sdfDate.format(productionRequisitionEntity.getDeliveryDateTime()));
			root.put("deliveryTime", sdfTime.format(productionRequisitionEntity.getDeliveryDateTime()));
			Template temp = PDFHtmlTemplateService.getConfiguration()
					.getTemplate("pdf_templates/production_requisition_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					Constants.DOCUMENT_DEFAULT_MAX_SIZE);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			String pdfXMLContent = byteArrayOutputStream.toString();
			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			PDFHtmlTemplateService.addDocumentFooter(productionRequisitionEntity, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
