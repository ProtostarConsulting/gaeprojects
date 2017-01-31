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
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

public class PrintPdfstockShipment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfstockShipment() {
		super();

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Long stShipId = Long.parseLong(request.getParameter("stShipId"));
		Long bid = Long.parseLong(request.getParameter("bid"));

		StockManagementService stockMgmtService = new StockManagementService();

		StockItemsShipmentEntity stockShipmentEntity = stockMgmtService.getStockShipmentByID(bid, stShipId);
		PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();
		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "StockShipment_"
				+ stockShipmentEntity.getItemNumber() + "_" + sdf.format(date);
		response.setHeader("Content-disposition",
				"inline; filename='StockShipment" + fileNameAppend + ".pdf'");

		pdfHtmlTemplateService.generateStockShipmentPdf(stockShipmentEntity, outputStream);

	}

}
