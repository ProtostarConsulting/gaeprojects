package com.protostar.billingnstock.invoice.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

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

		PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();
		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "Invoice_" + invoiceEntity.getItemNumber()
				+ "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='Invoice"
				+ fileNameAppend + ".pdf'");

		pdfHtmlTemplateService.generateInvoiceViewPDF(invoiceEntity,
				outputStream);
	}

}
