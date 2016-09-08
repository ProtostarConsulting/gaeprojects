package com.protostar.billingnstock.account.services;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.protostar.billingnstock.account.entities.VoucherEntity;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

public class PdfSales extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PdfSales() {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Long id = Long.parseLong(request.getParameter("id"));
		String entity = String.valueOf(request.getParameter("entityname"));
		String entityId = String.valueOf(request.getParameter("entityId"));

		String str1 = String.valueOf(request.getParameter("str"));

		PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();
		response.setContentType("application/PDF");
		ServletOutputStream outputStream = response.getOutputStream();
		Date today = new Date();
		String fileNameAppend = today.getDay() + "_" + today.getMonth() + "_"
				+ today.getYear();
		response.setHeader("Content-disposition",
				"inline; filename='Downloaded_" + fileNameAppend + ".pdf'");

		VoucherService voucherService = new VoucherService();
		VoucherEntity voucherEntity = null;

		if (entity.toString().equals("SalesVoucherEntity")) {
			voucherEntity = voucherService.getSalesListById(id);
		} 
		if (entity.toString().equals("ReceiptVoucherEntity"))
		
		{
			voucherEntity = voucherService.getRecieptListById(id);
		}
		if (entity.toString().equals("PurchaseVoucherEntity"))
			
		{
			voucherEntity = voucherService.getPurchesListById(id);
		}

		
		
		
		
		
		
		
		
		try {
			pdfHtmlTemplateService.generateVoucherPDF(voucherEntity,
					outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			outputStream.close();
		}
	}

}
