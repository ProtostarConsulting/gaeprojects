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
import com.protostar.billingnstock.user.entities.EmpDepartment;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.until.data.EmployeeDetail;

/**
 * Servlet implementation class DownloadMonthlypayment
 */
public class DownloadMonthlypayment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DownloadMonthlypayment() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HrService hrService = new HrService();
		Long businessId = Long.parseLong(request.getParameter("id"));
		String currentMonth = request.getParameter("month");
		List<MonthlyPaymentDetailEntity> monthlypay = hrService.getMonthlyPayment(businessId, currentMonth);
		OutputStream out = null;

		try {

			response.setContentType("text/csv");

			response.setHeader("Content-Disposition", "attachment; filename=SalarySheet_" + currentMonth + ".csv");
			ServletOutputStream outputStream = response.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);
			writer.append("Emp. No.");
			writer.append(',');
			writer.append("Employee Name");
			writer.append(',');
			writer.append("Dept");
			writer.append(',');
			writer.append("Monthly Gross");
			writer.append(',');
			writer.append("Monthly Basic");
			writer.append(',');
			writer.append("Pay Days");
			writer.append(',');
			writer.append("Calulated Gross");
			writer.append(',');
			writer.append("Calulated Basic");
			writer.append(',');
			writer.append("PF");
			writer.append(',');
			writer.append("PT");
			writer.append(',');
			writer.append("ESI");
			writer.append(',');
			writer.append("IT");
			writer.append(',');
			writer.append("Canteen");
			writer.append(',');
			writer.append("OtherDeduction");
			writer.append(',');
			writer.append("Net Salary");
			writer.append(',');
			writer.append(System.lineSeparator());
			for (int i = 0; i < monthlypay.size(); i++) {

				try {

					MonthlyPaymentDetailEntity salaryObj = monthlypay.get(i);
					UserEntity empAccount = salaryObj.getEmpAccount();
					if (empAccount == null)
						continue;
					EmployeeDetail employeeDetail = empAccount.getEmployeeDetail();
					writer.append("" + (employeeDetail == null ? "" : employeeDetail.getEmpId()));
					writer.append(',');

					writer.append("" + empAccount.getFirstName() + " " + empAccount.getLastName());
					writer.append(',');
					EmpDepartment department = employeeDetail.getDepartment();
					writer.append((department == null ? "" : department.getName()));
					writer.append(',');
					writer.append("" + salaryObj.getSalStruct().getMonthlyGrossSal());
					writer.append(',');
					writer.append("" + salaryObj.getSalStruct().getMonthlyBasic());
					writer.append(',');
					writer.append("" + salaryObj.getPayableDays());
					writer.append(',');
					writer.append("" + salaryObj.getCalculatedGrossSalary());
					writer.append(',');
					float calBasicSal = 0;
					if(salaryObj.getTotalDays() >0)
						calBasicSal = salaryObj.getSalStruct().getMonthlyBasic() / salaryObj.getTotalDays()
							* salaryObj.getPayableDays();
					writer.append("" + calBasicSal);
					writer.append(',');
					writer.append("" + salaryObj.getPfDeductionAmt());
					writer.append(',');
					writer.append("" + salaryObj.getPtDeductionAmt());
					writer.append(',');
					writer.append("" + salaryObj.getEsiDeductionAmt());
					writer.append(',');
					writer.append("" + salaryObj.getItDeductionAmt());
					writer.append(',');
					writer.append("" + salaryObj.getCanteenDeductionAmt());
					writer.append(',');
					writer.append("" + salaryObj.getOtherDeductionAmt());
					writer.append(',');
					writer.append("" + salaryObj.getNetSalaryAmt());
					writer.append(',');
					writer.append(System.lineSeparator());

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			writer.close();

		} catch (Exception e) {

			e.printStackTrace();
			throw new ServletException("Error Occurred while downloading the csv file.", e);
		} finally {
			if (out != null)
				out.close();

		}

	}
}
