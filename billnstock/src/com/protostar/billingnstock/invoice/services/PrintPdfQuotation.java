package com.protostar.billingnstock.invoice.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.protostar.billingnstock.invoice.entities.QuotationEntity;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

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

		PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();

		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "Quotation_" + quotationEntity.getItemNumber()
				+ "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='Quotation"
				+ fileNameAppend + ".pdf'");

		pdfHtmlTemplateService.generateQuotationViewPDF(quotationEntity,
				outputStream);

	}

}
