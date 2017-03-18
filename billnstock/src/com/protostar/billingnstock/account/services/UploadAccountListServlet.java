package com.protostar.billingnstock.account.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.hr.entities.SalStruct;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billnstock.service.UtilityService;

public class UploadAccountListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final Logger log = Logger.getLogger(UploadAccountListServlet.class
			.getName());

	public UploadAccountListServlet() {
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
		AccountingService accServices=new AccountingService();

		Date todaysDate = new Date();

		for (int row = 1; row < split2.length; row++) {

			try {
				String[] split = split2[row].split(",");
				if (split == null || split.length < 11) {
					log.info("Not good Data for current row. Jumping to new row.");
					continue;
				}

				
				AccountEntity  accEntity =  (AccountEntity) accServices.getAccountList(businessId);
				
				if (accEntity == null) {
					accEntity = new AccountEntity();
				
					accEntity.setBusiness(businessEntity);
					accEntity.setCreatedDate(todaysDate);
				}

				accEntity.setModifiedDate(todaysDate);
			/*	
				accEntity.setAccountName((split[3].trim()));
				accEntity.setAccountType((split[4].trim()));
				accEntity.setMonthlyHra(Float.parseFloat(split[5].trim()));
				accEntity.setMonthlyConvence(Float.parseFloat(split[6].trim()));
				accEntity.setMonthlyMedical(Float.parseFloat(split[7].trim()));
				accEntity
						.setMonthlyEducation(Float.parseFloat(split[8].trim()));
				accEntity.setMonthlyAdhocAllow(Float.parseFloat(split[9].trim()));
				accEntity.setMonthlySpecialAllow(Float.parseFloat(split[10].trim()));*/
				ofy().save().entity(accEntity).now();
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
