package com.protostar.billingnstock.stock.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

public class PrintPdfPurchaseOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfPurchaseOrder() {
		super();

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Long poId = Long.parseLong(request.getParameter("poId"));
		Long bid = Long.parseLong(request.getParameter("bid"));

		StockManagementService stockMgmtService = new StockManagementService();

		PurchaseOrderEntity purchaseOrderEntity = stockMgmtService.getPOByID(
				bid, poId);
		PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();
		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "PurchaseOrder_"
				+ purchaseOrderEntity.getItemNumber() + "_" + sdf.format(date);
		response.setHeader("Content-disposition",
				"inline; filename='PurchaseOrder" + fileNameAppend + ".pdf'");

		pdfHtmlTemplateService.generatePurchaseOrderViewPdf(
				purchaseOrderEntity, outputStream);

	}

}
