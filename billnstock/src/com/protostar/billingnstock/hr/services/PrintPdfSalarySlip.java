package com.protostar.billingnstock.hr.services;

import java.io.IOException;
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// TODO Auto-generated method stub
		Long id = Long.parseLong(request.getParameter("id"));
		Long bid = Long.parseLong(request.getParameter("bid"));
		String entity = String.valueOf(request.getParameter("entityname"));
		String entityId = String.valueOf(request.getParameter("entityId"));
		String month=String.valueOf(request.getParameter("month"));
		String str1 = String.valueOf(request.getParameter("str"));

		PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();
		response.setContentType("application/PDF");
		ServletOutputStream outputStream = response.getOutputStream();
		Date today = new Date();
		String fileNameAppend = today.getDay() + "_" + today.getMonth() + "_"
				+ today.getYear();
		response.setHeader("Content-disposition",
				"inline; filename='Downloaded_" + fileNameAppend + ".pdf'");

				System.out.println("entity" + entity);
				HrService hrService=new HrService();
				MonthlyPaymentDetailEntity monthlyPaymentDetailEntity=null;

		
		if (entity.toString().equals("MonthlyPaymentDetailEntity"))

		{
			monthlyPaymentDetailEntity = hrService.getMonthlyPaymentByID(bid,month,id);
			
			
			System.out.println("monthlyPaymentDetailEntity*********ii**" + monthlyPaymentDetailEntity.getNetSalaryAmt());
			pdfHtmlTemplateService.generateHrPDF(monthlyPaymentDetailEntity,outputStream);
		}
		
		

	}

	
}
