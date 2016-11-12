package com.protostar.billingnstock.hr.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.protostar.billingnstock.hr.entities.MonthlyPaymentDetailEntity;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

/**
 * Servlet implementation class PrintPdfSalarySlip
 */
public class PrintPdfSalarySlip extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfSalarySlip() {
		super();
	}

	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String idParam = request.getParameter("id");
		System.out.println("idParam:" + idParam);
		Long id = Long.parseLong(request.getParameter("id"));
		Long bid = Long.parseLong(request.getParameter("bid"));
		String entity = request.getParameter("entityname");
		String entityId = request.getParameter("entityId");
		String month = request.getParameter("month");
		String str1 = request.getParameter("str");

		PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();
		response.setContentType("application/PDF");
		ServletOutputStream outputStream = response.getOutputStream();

		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "_" + month + "_" + sdf.format(date);
		response.setHeader("Content-disposition",
				"inline; filename='SalarySlip" + fileNameAppend + ".pdf'");

		System.out.println("entity" + entity);
		HrService hrService = new HrService();
		MonthlyPaymentDetailEntity monthlyPaymentDetailEntity = null;

		if (entity.toString().equals("MonthlyPaymentDetailEntity"))

		{
			monthlyPaymentDetailEntity = hrService.getMonthlyPaymentByID(bid,
					month, id);

			System.out.println("monthlyPaymentDetailEntity*********ii**"
					+ monthlyPaymentDetailEntity.getNetSalaryAmt());
			pdfHtmlTemplateService.generateHrPDF(monthlyPaymentDetailEntity,
					outputStream);
		}

	}

}
