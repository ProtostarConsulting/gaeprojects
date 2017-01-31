package com.protostar.billingnstock.stock.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.protostar.billingnstock.stock.entities.StockItemsReceiptEntity;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

public class PrintPdfstockReceipt extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfstockReceipt() {
		super();

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Long stRcptId = Long.parseLong(request.getParameter("stRcptId"));
		Long bid = Long.parseLong(request.getParameter("bid"));

		StockManagementService stockMgmtService = new StockManagementService();
		StockItemsReceiptEntity stockItemsReceipt = stockMgmtService
				.getStockReceiptByID(bid, stRcptId);

		PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();
		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "StockReceipt_"
				+ stockItemsReceipt.getItemNumber() + "_" + sdf.format(date);
		response.setHeader("Content-disposition",
				"inline; filename='StockReceipt_" + fileNameAppend + ".pdf'");

		pdfHtmlTemplateService.generateStockReceiptPdf(stockItemsReceipt,
				outputStream);
		;
	}

}
