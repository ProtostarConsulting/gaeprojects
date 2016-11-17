package com.protostar.billingnstock.hr.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.protostar.billingnstock.hr.entities.SalStruct;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billnstock.service.UtilityService;

public class UploadSalaryMasterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final Logger log = Logger.getLogger(UploadSalaryMasterServlet.class
			.getName());

	public UploadSalaryMasterServlet() {
		super();

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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

		DecimalFormat dform = new DecimalFormat();

		split2 = fileAsString.split("\n");
		log.info("split2.length: " + split2.length);

		UserService userService = new UserService();
		// SalStruct salaryMaster=new SalStruct();
		BusinessEntity businessEntity = userService.getBusinessById(businessId);
		HrService hrService = new HrService();

		Date todaysDate = new Date();

		for (int row = 1; row < split2.length; row++) {

			try {
				String[] split = split2[row].split(",");
				if (split == null || split.length < 11) {
					log.info("Not good Data for current row. Jumping to new row.");
					continue;
				}

				String userWebSafeKey = split[0].trim();
				log.info("userWebSafeKey:" + userWebSafeKey);
				UserEntity currRowUser = userService
						.getUserByWebSafeKey(userWebSafeKey);
				log.info("currRowUser:" + currRowUser);
				if (currRowUser == null) {
					continue;
				}
				SalStruct salEntity = hrService.getSalStructByUser(currRowUser);

				if (salEntity == null) {
					salEntity = new SalStruct();
					salEntity.setEmpAccount(currRowUser);
					salEntity.setBusiness(businessEntity);
					salEntity.setCreatedDate(todaysDate);
				}

				salEntity.setModifiedDate(todaysDate);
				log.info("Updating Emp: " + currRowUser.getFirstName()
						+ ", Dept: " + split[2]);
				salEntity.setGrosssal(Float.parseFloat(split[3].trim()));
				salEntity.setBasic(Float.parseFloat(split[4].trim()));
				salEntity.setHramonthly(Float.parseFloat(split[5].trim()));
				salEntity.setConvence(Float.parseFloat(split[6].trim()));
				salEntity.setMedical(Float.parseFloat(split[7].trim()));
				salEntity.setEducation(Float.parseFloat(split[8].trim()));
				salEntity.setAdhocAllow(Float.parseFloat(split[9].trim()));
				salEntity.setSpecialAllow(Float.parseFloat(split[10].trim()));
				ofy().save().entity(salEntity).now();
			} catch (Exception e) {
				log.warning(e.getMessage());
				e.printStackTrace();
			}
		}

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.info("In suide UploadUsersServlet....");
		// upload csv user into DB
		this.doGet(request, response);

	}

}
