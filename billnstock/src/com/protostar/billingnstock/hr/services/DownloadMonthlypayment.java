package com.protostar.billingnstock.hr.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.protostar.billingnstock.hr.entities.MonthlyPaymentDetailEntity;
import com.protostar.billingnstock.hr.entities.SalStruct;

/**
 * Servlet implementation class DownloadMonthlypayment
 */
public class DownloadMonthlypayment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public DownloadMonthlypayment() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HrService hrService = new HrService();
		Long businessId = Long.parseLong(request.getParameter("id"));
		String currentMonth=request.getParameter("month");
		List<MonthlyPaymentDetailEntity> monthlypay = hrService
				.getMonthlyPayment(businessId,currentMonth);
		OutputStream out = null;
		
		try {
			
			response.setContentType("text/csv");

			response.setHeader("Content-Disposition",
					"attachment; filename=Monthlypayment" + ".csv");
			ServletOutputStream outputStream = response.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);
			writer.append("User Key (Do not change this value)");
			writer.append(',');
			writer.append("Name Of Employee");
			writer.append(',');
			writer.append("Dept");
			writer.append(',');
			writer.append("Monthly Gross");
			writer.append(',');
			writer.append("Pay Days");
			writer.append(',');
			writer.append("Monthly Salary");
			writer.append(',');
			writer.append("Onetime Payment");
			writer.append(',');
			writer.append("P F");
			writer.append(',');
			writer.append("P T");
			writer.append(',');
			writer.append("Canteen");
			writer.append(',');
			writer.append("IT Dduction");
			writer.append(',');
			writer.append("Other Deduction");
			writer.append(',');
			writer.append(System.lineSeparator());
			for (int i = 0; i < monthlypay.size(); i++) {
				
				try {
					
					writer.append(""+monthlypay.get(i).getleaveDetailEntity().getUser().getId());
					writer.append(',');
					
					writer.append(""+monthlypay.get(i).getleaveDetailEntity().getUser().getFirstName()+""+monthlypay.get(i).getleaveDetailEntity().getUser().getLastName());
					writer.append(',');
					writer.append(""+monthlypay.get(i).getleaveDetailEntity().getUser().getDepartment().getName());
					writer.append(',');
					writer.append(""+monthlypay.get(i).getMonthlyGrossSalary());
					writer.append(',');
					writer.append(""+monthlypay.get(i).getPayableDays());
					writer.append(',');
					writer.append(""+monthlypay.get(i).getCalculatedGrossSalary());
					writer.append(',');
					writer.append("");
					writer.append(',');
					writer.append(""+monthlypay.get(i).getPfDeductionAmt());
					writer.append(',');
					writer.append(""+monthlypay.get(i).getPtDeductionAmt());
					writer.append(',');
					writer.append(""+monthlypay.get(i).getCanteenDeductionAmt());
					writer.append(',');
					writer.append(""+monthlypay.get(i).getItDeductionAmt());
					writer.append(',');
					writer.append(""+monthlypay.get(i).getOtherDeductionAmt());
					writer.append(',');
					writer.append(System.lineSeparator());
					
				} catch (Exception e) {					
					e.printStackTrace();
				}
				
					
				
			}
			writer.close();
			
			
		}catch (Exception e) {

			e.printStackTrace();
			throw new ServletException(
					"Error Occurred while downloading the csv file.", e);
		} finally {
			if (out != null)
				out.close();
		
		
		
		
	}

	
	
	}}
