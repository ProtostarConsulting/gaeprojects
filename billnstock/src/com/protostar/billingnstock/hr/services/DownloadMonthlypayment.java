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

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HrService hrService = new HrService();
		Long businessId = Long.parseLong(request.getParameter("id"));
		String currentMonth = request.getParameter("month");
		List<MonthlyPaymentDetailEntity> monthlypay = hrService
				.getMonthlyPayment(businessId, currentMonth);
		OutputStream out = null;

		try {

			response.setContentType("text/csv");

			response.setHeader("Content-Disposition",
					"attachment; filename=SalarySheet_" + currentMonth + ".csv");
			ServletOutputStream outputStream = response.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);
			writer.append("Emp. No.");
			writer.append(',');
			writer.append("Employee Name");
			writer.append(',');
			writer.append("Dept");
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
					EmployeeDetail employeeDetail = empAccount
							.getEmployeeDetail();
					writer.append(""
							+ (employeeDetail == null ? "" : employeeDetail
									.getEmpId()));
					writer.append(',');

					writer.append("" + empAccount.getFirstName() + " "
							+ empAccount.getLastName());
					writer.append(',');
					EmpDepartment department = employeeDetail.getDepartment();
					writer.append((department == null ? "" : department
							.getName()));
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
			throw new ServletException(
					"Error Occurred while downloading the csv file.", e);
		} finally {
			if (out != null)
				out.close();

		}

	}
}
