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
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billingnstock.user.services.UserService;

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
		UserService userService = new UserService();

		List<UserEntity> userList = userService.getUsersByBusinessId(id);

		System.out.println("***************"+Long.parseLong(request.getParameter("id").toString()));
		//List<SalStruct>salMasterList =hrService.getSalaryMasterlist(id);
		
		

		System.out.println("***************"
				+ Long.parseLong(request.getParameter("id").toString()));
		//List<SalStruct> salMasterList = hrService.getSalaryMasterlist(id);


		OutputStream out = null;

		try {

			response.setContentType("text/csv");

			response.setHeader("Content-Disposition",
					"attachment; filename=SalaryMaster" + ".csv");

			ServletOutputStream outputStream = response.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);

			writer.append("UserId");
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
					String strLong = Long.toString(userEntity.getId().longValue());
					writer.append(strLong );
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
					writer.append("-");

				
					writer.append(',');
					
if(salMasterList.size()!=0){

					/*writer.append(salMasterList.get(i).getEmpAccount().getFirstName()+""+salMasterList.get(i).getEmpAccount().getLastName());
					writer.append(',');

					writer.append(salMasterList.get(i).getEmpAccount()
							.getFirstName()
							+ ""
							+ salMasterList.get(i).getEmpAccount()
									.getLastName());
					writer.append(',');*/

					
					Float grosssal = userSalMaster.getGrosssal();
					writer.append(grosssal == null ? "" : grosssal.toString());
					writer.append(',');
					writer.append(userSalMaster.getBasic() == null ? ""
							: userSalMaster.getBasic().toString());
					writer.append(',');

					// if(salMasterList.get(i).getHRA().toString()==null)

					writer.append(userSalMaster.getHramonthly() == null ? ""
							: userSalMaster.getHramonthly().toString());
					//if(salMasterList.get(i).getHRA().toString()==null)
					
					writer.append("  ");
					// if(salMasterList.get(i).getHRA().toString()==null)

					writer.append("  ");
					writer.append(',');
					writer.append(userSalMaster.getConvence() == null ? ""
							: userSalMaster.getConvence().toString());
					writer.append(',');
					writer.append(userSalMaster.getMedical() == null ? ""
							: userSalMaster.getMedical().toString());
					writer.append(',');
					writer.append(userSalMaster.getEducation() == null ? ""
							: userSalMaster.getEducation().toString());
				
					writer.append(',');
					writer.append(userSalMaster.getAdhocAllow() == null ? ""
							: userSalMaster.getAdhocAllow().toString());
					
							
					writer.append(',');
					writer.append(userSalMaster.getSpecialAllow() == null ? ""
							: userSalMaster.getSpecialAllow().toString());
				

				

					writer.append(',');

					writer.append(System.lineSeparator());}writer.append(System.lineSeparator());
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
