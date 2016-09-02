package com.protostar.billingnstock.proadmin.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.protostar.billingnstock.proadmin.entities.AccountType;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;

@Api(name = "proadminService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.proadmin.services", ownerName = "com.protostar.billingnstock.proadmin.services", packagePath = ""))
public class ProAdminService {

	private static final Logger log = Logger.getLogger(ProAdminService.class
			.getName());

	@ApiMethod(name = "addAccountType")
	public void addAccountType(AccountType account) {
		ofy().save().entity(account).now();

	}

	@ApiMethod(name = "getallAccountType")
	public List<AccountType> getallAccountType() {
		log.info("Inside getallAccountType.");
		try {
			return ofy().load().type(AccountType.class).list();
		} catch (Exception e) {
			log.info("Error Ocuured: " + e.getStackTrace());
		}

		return null;

	}

	@ApiMethod(name = "getAccountTypeById")
	public AccountType getAccountTypeById(@Named("id") Long id) {
		return ofy().load().type(AccountType.class).id(id).now();
	}

	@ApiMethod(name = "initsetupnext")
	public void initsetupnext() {
		Date date = new Date();
		String DATE_FORMAT = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		List<AccountType> accountt = ofy().load().type(AccountType.class)
				.list();
		AccountType filteredaccount = new AccountType();
		for (int i = 0; i < accountt.size(); i++) {
			if (accountt.get(i).getAccountName().equals("Platinum")) {
				filteredaccount = accountt.get(i);
			}
		}

		BusinessEntity businessEntity = new BusinessEntity();
		/*
		 * businessEntity.setAdminFirstName("ganesh");
		 * businessEntity.setAdminEmailId("ganesh.lawande@protostar.co.in");
		 * businessEntity.setAdminLastName("Lawande");
		 */
		businessEntity.setBusinessName("Protostar");
		businessEntity.setAccounttype(filteredaccount);
		businessEntity.setRegisterDate(sdf.format(date));

		ofy().save().entity(businessEntity).now();

		UserEntity userEntity = new UserEntity();
		userEntity.setBusiness(businessEntity);
		userEntity.setEmail_id("ganesh.lawande@protostar.co.in");
		userEntity.setFirstName("ganesh");
		userEntity.setLastName("Lawande");
		userEntity.setIsGoogleUser(true);
		userEntity.setAuthority(Arrays.asList("admin"));
		ofy().save().entity(userEntity).now();

		// ------------------------------

		UserEntity userEntity1 = new UserEntity();
		userEntity1.setBusiness(businessEntity);
		userEntity1.setEmail_id("sneha@protostar.co.in");
		userEntity1.setFirstName("Sneha");
		userEntity1.setLastName("Sakhare");
		userEntity1.setIsGoogleUser(true);
		userEntity1.setAuthority(Arrays.asList("admin"));
		ofy().save().entity(userEntity1).now();


		UserEntity userEntity2 = new UserEntity();
		userEntity2.setBusiness(businessEntity);
		userEntity2.setEmail_id("ashvini.gokale@protostarcs.com");
		userEntity2.setFirstName("ashvini");
		userEntity2.setLastName("gokale");
		userEntity2.setIsGoogleUser(true);
		userEntity2.setAuthority(Arrays.asList("admin"));
		ofy().save().entity(userEntity2).now();
		
		
		UserEntity userEntity3 = new UserEntity();
		userEntity3.setBusiness(businessEntity);
		userEntity3.setEmail_id("deepali@protostar.co.in");
		userEntity3.setFirstName("deepali");
		userEntity3.setLastName("mate");
		userEntity3.setIsGoogleUser(true);
		userEntity3.setAuthority(Arrays.asList("admin"));
		ofy().save().entity(userEntity3).now();

		// ///////////////////

	}

	@ApiMethod(name = "initsetup")
	public void initsetup() {
		// try{
		AccountType accounttype = new AccountType();
		accounttype.setAccountName("Free");
		accounttype.setDescription("Free for upto 2 users");
		accounttype.setMaxuser("2");
		accounttype.setPaymentDesc("Free no charges");
		ofy().save().entity(accounttype).now();
		AccountType accounttype1 = new AccountType();
		accounttype1.setAccountName("Silver");
		accounttype1.setDescription("Good for upto 20 users");
		accounttype1.setMaxuser("20");
		accounttype1.setPaymentDesc("Rs. 4000 PM + Tax");
		ofy().save().entity(accounttype1).now();
		AccountType accounttype2 = new AccountType();
		accounttype2.setAccountName("Gold");
		accounttype2.setDescription("20 to 50 users");
		accounttype2.setMaxuser("50");
		accounttype2.setPaymentDesc("Rs. 8000 PM + Tax");
		ofy().save().entity(accounttype2).now();
		AccountType accounttype3 = new AccountType();
		accounttype3.setAccountName("Platinum");
		accounttype3.setDescription("50 to 500 users");
		accounttype3.setMaxuser("500");
		accounttype3.setPaymentDesc("Rs. 25,000 PM + Tax");
		ofy().save().entity(accounttype3).now();

		// Thread.sleep(30000);

		// }
		// catch(Exception e)
		// {

		// }
		// //////////////////////// create 2 protostar user
		// /////////////////////////

	}

	@ApiMethod(name = "getAllemp")
	public List<UserEntity> getAllemp() {
		return ofy().load().type(UserEntity.class).list();
	}

}// end of InternetService
