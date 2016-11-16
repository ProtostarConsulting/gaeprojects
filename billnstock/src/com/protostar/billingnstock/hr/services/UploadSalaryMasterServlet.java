package com.protostar.billingnstock.hr.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
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
import com.protostar.billnstock.servlets.UploadMonthlySalaryMasterServlet;


public class UploadSalaryMasterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final Logger log = Logger
			.getLogger(UploadMonthlySalaryMasterServlet.class.getName());
       
   
    public UploadSalaryMasterServlet() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		List<UserEntity> userlist= userService.getUsersByBusinessId(businessId);
		System.out.println("******^^userlist^^^^"+userlist.size());
		//SalStruct salaryMaster=new SalStruct();
		BusinessEntity businessEntity = userService.getBusinessById(businessId);
		HrService hrService=new HrService();
		
		
		
		
		Date todaysDate = new Date();

		for (int row = 1; row < split2.length; row++) {

			try {
				String[] split = split2[row].split(",");
				if (split == null || split.length <11) {
					continue;
				}
				System.out.println("******^^Long.parseLong(split[0].trim()^^^^"+Double.valueOf(split[0].trim()).longValue());
				long userID =Long.parseLong(split[0].trim());//parseFloat(res);// Double.valueOf(split[0].trim()).longValue();
				UserEntity currRowUser = userService.getUserByID(userID);
				if(currRowUser == null){
					continue;
				}
				SalStruct salentity = hrService.getSalStructByUser(currRowUser);
				
				if(salentity == null){
					SalStruct salEntity=new  SalStruct();
					salEntity.setEmpAccount(currRowUser);
				}
				
				  long val =  Double.valueOf(split[0].trim()).longValue();
				System.out.println("******^^num^^^^"+val);
				
				System.out.println("******^^userlist.get(row-1).getEmpId()^^^^"+userlist.get(row-1).getEmpId());
				for(int j=userlist.size();j<row;j--){
				if(userlist.get(j).getEmpId()== val){
					salentity.setEmpAccount(userlist.get(j));}
				}
				salentity.setBusiness(businessEntity);
				salentity.setCreatedDate(todaysDate);
				//salentity.setId(Long.parseLong(split[0].trim()));
				//salentity.setEmpAccount(userlist);
				/*salentity.getEmpAccount().setFirstName(split[1].trim());*/
				System.out.println("666666666666^^^^^^^"+split[2].trim());
			//	System.out.println("******^^^^^^^"+salentity);
			//	salentity.getEmpAccount().getDepartment().setName(split[2].trim());
				salentity.setGrosssal(Float.parseFloat(split[3].trim()));
				salentity.setBasic(Float.parseFloat(split[4].trim()));
				salentity.setHramonthly(Float.parseFloat(split[5].trim()));
				salentity.setConvence(Float.parseFloat(split[6].trim()));
				salentity.setMedical(Float.parseFloat(split[7].trim()));
				salentity.setEducation(Float.parseFloat(split[8].trim()));
				salentity.setAdhocAllow(Float.parseFloat(split[9].trim()));
				salentity.setSpecialAllow(Float.parseFloat(split[10].trim()));		     
				ofy().save().entity(salentity).now();
				
			
			}catch (Exception e) {
				log.warning(e.getMessage());
				e.printStackTrace();}}
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
		log.info("In suide UploadUsersServlet....");
		// upload csv user into DB
		this.doGet(request, response);	
		
		
	}

}
