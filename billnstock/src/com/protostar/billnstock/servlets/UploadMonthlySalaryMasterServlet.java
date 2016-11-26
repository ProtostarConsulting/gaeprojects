package com.protostar.billnstock.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.EmpDepartment;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billnstock.service.UtilityService;

public class UploadMonthlySalaryMasterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger
			.getLogger(UploadMonthlySalaryMasterServlet.class.getName());

	public UploadMonthlySalaryMasterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		log.info("In suide UploadUsersServlet....");
		// upload csv user into DB
		this.doGet(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// create and return csv with user salary master data if any else emptly
		// rows to fill and upload
		String[] split2 = null;

		Map<String, String> items = UtilityService
				.getMultiPartFileItemsWithFileAsString(request);
		if (items == null || items.isEmpty()) {
			return;
		}

		String fileAsString = items.get("file").toString();
		log.info("fileAsString: " + fileAsString);
		String businessIdRequestParamValue = items.get("businessId").toString();
		Long businessId = businessIdRequestParamValue == null ? null : Long
				.parseLong(businessIdRequestParamValue.trim());

		log.info("businessId: " + businessId);
		String usernameRequestParamValue = items.get("username").toString();
		String loggedInUser = usernameRequestParamValue == null ? null
				: usernameRequestParamValue.trim();
		log.info("loggedInUser: " + loggedInUser);

		split2 = fileAsString.split("\n");
		log.info("split2.length: " + split2.length);
		UserService userService = new UserService();
		BusinessEntity businessEntity = userService.getBusinessById(businessId);

		List<EmpDepartment> empDepartments = userService
				.getEmpDepartments(businessId);
		Date todaysDate = new Date();
		// Start from 1 so that column headers are scriped.
		for (int row = 1; row < split2.length; row++) {

			try {
				String[] split = split2[row].split(",");
				if (split == null || split.length < 7) {
					continue;
				}

				UserEntity userEntity = new UserEntity();
				userEntity.setBusiness(businessEntity);
				userEntity.setCreatedDate(todaysDate);
				userEntity.setModifiedBy(loggedInUser);

				userEntity.setFirstName(split[0].trim());
				userEntity.setLastName(split[1].trim());
				userEntity.setEmail_id(split[2].trim());
				String dept = split[3];

				if (dept != null && !dept.isEmpty()) {
					for (EmpDepartment empDepartment : empDepartments) {
						if (dept.trim().equalsIgnoreCase(
								empDepartment.getName())) {
							userEntity.getEmployeeDetail().setDepartment(empDepartment);
						}
					}
				}
				if (userEntity.getEmployeeDetail().getDepartment() == null) {
					for (EmpDepartment empDepartment : empDepartments) {
						if ("Default".equalsIgnoreCase(empDepartment.getName())) {
							userEntity.getEmployeeDetail().setDepartment(empDepartment);
						}
					}
				}

				userEntity.setIsLoginAllowed("1".equalsIgnoreCase(split[4]
						.trim()));

				userEntity
						.setIsGoogleUser("1".equalsIgnoreCase(split[5].trim()));
				userEntity.setPassword(split[6].trim());

				ofy().save().entity(userEntity).now();

				log.info("Processed userEntity.getFirstName(): "
						+ userEntity.getFirstName());
			} catch (Exception e) {
				log.warning(e.getMessage());
				e.printStackTrace();
			}

		}
	}
}
