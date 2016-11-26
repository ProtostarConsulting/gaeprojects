package com.protostar.billingnstock.hr.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.hr.entities.SalStruct;
import com.protostar.billingnstock.user.entities.EmpDepartment;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billnstock.servlets.UploadMonthlySalaryMasterServlet;
import com.protostar.billnstock.until.data.Constants;

public class DownloadSalaryMaster extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(DownloadSalaryMaster.class
			.getName());

	public DownloadSalaryMaster() {
		super();

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HrService hrService = new HrService();
		System.out.println("businessId: "
				+ Long.parseLong(request.getParameter("id").toString()));
		Long businessId = Long.parseLong(request.getParameter("id"));

		List<SalStruct> salMasterList = hrService
				.getSalaryMasterlist(businessId);
		UserService userService = new UserService();

		List<UserEntity> userList = userService
				.getUsersByBusinessId(businessId);

		OutputStream out = null;

		try {

			response.setContentType("text/csv");

			response.setHeader("Content-Disposition",
					"attachment; filename=SalaryMaster" + ".csv");

			ServletOutputStream outputStream = response.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);

			writer.append("User Key (Do not change this value)");
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

			for (int i = 0; i < userList.size(); i++) {
				try {
					UserEntity userEntity = userList.get(i);
					String userKey = Ref.create(userEntity).key().toWebSafeString();
					log.info("strLong: " + userKey);
					writer.append(userKey);
					writer.append(',');
					SalStruct userSalMaster = null;
					for (SalStruct salMaster : salMasterList) {
						if (salMaster.getEmpAccount().getId() == userEntity
								.getId()) {
							userSalMaster = salMaster;
						}
					}

					if (userSalMaster == null) {
						userSalMaster = new SalStruct();
						userSalMaster.setEmpAccount(userEntity);
					}

					writer.append(userSalMaster.getEmpAccount().getFirstName()
							+ " " + userSalMaster.getEmpAccount().getLastName());

					writer.append(',');

					EmpDepartment department = userSalMaster.getEmpAccount()
							.getEmployeeDetail().getDepartment();
					if (department != null)
						writer.append(department.getName());
					else
						writer.append(Constants.DEFAULT_EMP_DEPT);

					writer.append(',');

					if (salMasterList.size() != 0) {

						Float grosssal = userSalMaster.getMonthlyGrossSal();
						writer.append(grosssal == null ? "" : grosssal
								.toString());
						writer.append(',');
						writer.append(""+userSalMaster.getMonthlyBasic());
						writer.append(',');

						// if(salMasterList.get(i).getHRA().toString()==null)

						writer.append(""+userSalMaster.getMonthlyHra());
						// if(salMasterList.get(i).getHRA().toString()==null)

						writer.append(',');
						writer.append(""+userSalMaster.getMonthlyConvence());
						writer.append(',');
						writer.append(""+userSalMaster.getMonthlyMedical());
						writer.append(',');
						writer.append(""+userSalMaster.getMonthlyEducation());

						writer.append(',');
						writer.append(""+userSalMaster.getMonthlyAdhocAllow());

						writer.append(',');
						writer.append(""+userSalMaster.getMonthlySpecialAllow());

						writer.append(',');
					}

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
