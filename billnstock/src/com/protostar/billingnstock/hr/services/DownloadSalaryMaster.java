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

import com.protostar.billingnstock.hr.entities.SalStruct;

public class DownloadSalaryMaster extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DownloadSalaryMaster() {
		super();

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HrService hrService = new HrService();
		System.out.println("***************"
				+ Long.parseLong(request.getParameter("id").toString()));
		Long id = Long.parseLong(request.getParameter("id"));
		System.out.println("***************"
				+ Long.parseLong(request.getParameter("id").toString()));
		List<SalStruct> salMasterList = hrService.getSalaryMasterlist(id);

		OutputStream out = null;

		try {

			response.setContentType("text/csv");

			response.setHeader("Content-Disposition",
					"attachment; filename=SalaryMaster" + ".csv");

			ServletOutputStream outputStream = response.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);

			writer.append("SR. NO");
			writer.append(',');
			writer.append("Name Of Employee");
			writer.append(',');
			writer.append("Dept");
			writer.append(',');
			writer.append("Gross");
			writer.append(',');
			writer.append("Basic");
			writer.append(',');
			writer.append("HRA");
			writer.append(',');
			writer.append("Convyence Allow");
			writer.append(',');
			writer.append("Medical");
			writer.append(',');
			writer.append("Education");
			writer.append(',');
			writer.append("Adhoc Allow  ");
			writer.append(',');
			writer.append("SpecialAllow  ");
			writer.append(',');

			writer.append(System.lineSeparator());

			for (int i = 0; i < salMasterList.size(); i++) {

				try {

					writer.append("" + (i + 1));
					writer.append(',');
					writer.append(salMasterList.get(i).getEmpAccount()
							.getFirstName()
							+ ""
							+ salMasterList.get(i).getEmpAccount()
									.getLastName());
					writer.append(',');
					writer.append("-");
					writer.append(',');
					writer.append(salMasterList.get(i).getGrosssal().toString());
					writer.append(',');
					writer.append(salMasterList.get(i).getBasic().toString());
					writer.append(',');
					// if(salMasterList.get(i).getHRA().toString()==null)

					writer.append("  ");
					writer.append(',');
					writer.append(salMasterList.get(i).getConvence().toString());
					writer.append(',');
					writer.append(salMasterList.get(i).getMedical().toString());
					writer.append(',');
					writer.append(salMasterList.get(i).getEducation()
							.toString());
					writer.append(',');
					writer.append(salMasterList.get(i).getAdhocAllow()
							.toString());
					writer.append(',');
					writer.append(salMasterList.get(i).getSpecialAllow()
							.toString());
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
