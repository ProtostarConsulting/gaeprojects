package com.protostar.billingnstock.tax.services;

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
import com.protostar.billingnstock.invoice.services.InvoiceService;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billingnstock.tax.entities.TaxReport;
import com.protostar.billingnstock.tax.entities.TaxReportItem;
import com.protostar.billnstock.until.data.NumberToRupees;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

public class PrintPdfTaxCollectionReport extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfTaxCollectionReport() {
		super();

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Long bid = Long.parseLong(request.getParameter("bid"));

		Long fromDate = Long.parseLong(request.getParameter("fromDate"));

		Long toDate = Long.parseLong(request.getParameter("toDate"));

		Long taxId = Long.parseLong(request.getParameter("taxId"));

		TaxService taxService = new TaxService();

		TaxEntity taxEntity = taxService.getTaxByID(bid, taxId);

		InvoiceService invoiceService = new InvoiceService();

		TaxReport taxReport = invoiceService.getTaxReport(taxEntity, bid,
				fromDate, toDate);

		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String dateFormat = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		String fileNameAppend = "Tax Collection Report" + "_"
				+ sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='ProERP_"
				+ fileNameAppend + ".pdf'");

		this.generatePdf(taxReport, outputStream);

	}

	public void generatePdf(TaxReport taxReport, OutputStream outputStream) {

		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();

			PDFHtmlTemplateService.addDocumentHeaderLogo(
					taxReport.getTaxEntity(), document, root);

			root.put("taxName", taxReport.getTaxEntity().getTaxCodeName());

			Date fromDate = taxReport.getFromDate();
			Date toDate = taxReport.getToDate();

			double taxTotal = taxReport.getTaxTotal();

			root.put("fromDate", fromDate);
			root.put("toDate", toDate);
			root.put("taxTotal", taxTotal);

			NumberToRupees numberToRupees = new NumberToRupees(
					Math.round(taxTotal));

			String taxTotalInWords = numberToRupees.getAmountInWords();

			root.put("taxTotalInWords", taxTotalInWords);

			if (taxReport.getItemList() != null
					&& !taxReport.getItemList().isEmpty()) {

				List<TaxReportItem> taxItemList = taxReport.getItemList();
				root.put("taxItemList", taxItemList);
			}

			Template temp = PDFHtmlTemplateService.getConfiguration()
					.getTemplate("pdf_templates/tax_collection_report.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));

			PDFHtmlTemplateService.addDocumentFooter(taxReport.getTaxEntity(),
					writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
