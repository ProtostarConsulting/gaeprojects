package com.protostar.billingnstock.user.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.TxnType;
import com.googlecode.objectify.Work;
import com.protostar.billingnstock.hr.entities.HREntityUtil;
import com.protostar.billingnstock.hr.entities.HRSettingsEntity;
import com.protostar.billingnstock.hr.services.HrService;
import com.protostar.billingnstock.proadmin.entities.BusinessPlanType;
import com.protostar.billingnstock.proadmin.services.ProtostarAdminService;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.EmpDepartment;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billingnstock.warehouse.services.WarehouseService;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService.CounterEntity;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService.CounterShard;
import com.protostar.billnstock.until.data.ServerMsg;

@Api(name = "userService", version = "v0.1", clientIds = { Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
		Constants.API_EXPLORER_CLIENT_ID }, audiences = { Constants.ANDROID_AUDIENCE }, scopes = {
				Constants.EMAIL_SCOPE }, namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.user.services", ownerName = "com.protostar.billingnstock.user.services", packagePath = ""))
public class UserService {

	private final Logger logger = Logger.getLogger(UserService.class.getName());

	@ApiMethod(name = "addUser", path = "addUser")
	public void addUser(UserEntity usr) {
		if (usr.getEmployeeDetail().getDepartment() == null)
			setDefaultDepartment(usr);

		if (usr.getId() == null) {
			logger.info("Adding New User:" + usr.getFirstName());
			usr.setCreatedDate(new Date());
			if (usr.getAuthorizations() == null || usr.getAuthorizations().isEmpty()) {
				if (usr.getAuthority().contains("admin"))
					usr.setAuthorizations(Constants.NEW_BIZ_DEFAULT_AUTHS);
				else
					usr.setAuthorizations(Constants.NEW_BIZ_USER_DEFAULT_AUTHS);
			}
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(usr.getBusiness()), Constants.EMP_NO_COUNTER);
			usr.getEmployeeDetail().setEmpId(sequenceGenService.getNextSequenceNumber());
		} else {
			usr.setModifiedDate(new Date());
			logger.info("Updating User:" + usr.getFirstName());
			ofy().save().entity(usr).now();
			return;
		}

		ofy().save().entity(usr).now();
		int count;
		List<UserEntity> filtereduser = ofy().load().type(UserEntity.class)
				.ancestor(Key.create(BusinessEntity.class, usr.getBusiness().getId())).list();
		count = filtereduser.size() + 1;

		BusinessEntity businessEntity = new BusinessEntity();
		businessEntity = usr.getBusiness();
		businessEntity.setTotalUser(count);
		ofy().save().entity(businessEntity).now();

		// Test Code to Send email to registered user

		/*
		 * User user = UserServiceFactory.getUserService().getCurrentUser();
		 * String recipientAddress = user.getEmail();
		 */
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String messageBody = "Welcome to Example! Your account has been created. "
				+ "You can edit your user profile by clicking the " + "following link:\n\n"
				+ "http://www.example.com/profile/\n\n" + "Let us know if you have any questions.\n\n"
				+ "The Example Team\n";
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ganesh.lawande@protostar.co.in", "ProERP"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(usr.getEmail_id()));
			message.setSubject("Welcome to Example.com!");
			message.setText(messageBody);
			Transport.send(message);
		} catch (AddressException e) {
			// An email address was invalid.
			// ...
			e.printStackTrace();
		} catch (MessagingException e) {
			// There was an error contacting the Mail service.
			// ...
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Email Send Code Ends

	}

	private void setDefaultDepartment(UserEntity usr) {
		List<EmpDepartment> empDepartments = getEmpDepartments(usr.getBusiness().getId());
		for (EmpDepartment empDepartment : empDepartments) {
			if ("Default".equalsIgnoreCase(empDepartment.getName())) {
				usr.getEmployeeDetail().setDepartment(empDepartment);
			}
		}
	}

	@ApiMethod(name = "addEmpDepartment", path = "addEmpDepartment")
	public void addEmpDepartment(EmpDepartment department) {
		ofy().save().entity(department).now();
	}

	@ApiMethod(name = "getEmpDepartments", path = "getEmpDepartments")
	public List<EmpDepartment> getEmpDepartments(@Named("businessId") Long id) {
		List<EmpDepartment> list = ofy().load().type(EmpDepartment.class).ancestor(Key.create(BusinessEntity.class, id))
				.list();
		return list;
	}

	@ApiMethod(name = "updateBusiness", path = "updateBusiness")
	public void updateBusiness(BusinessEntity businessEntity) {
		addBusiness(businessEntity);
	}

	@ApiMethod(name = "getBusinessById")
	public BusinessEntity getBusinessById(@Named("id") Long id) {
		return ofy().load().type(BusinessEntity.class).id(id.longValue()).now();
	}

	/*
	 * Such Should not be used. Don't uncommnet
	 * 
	 * @ApiMethod(name = "getUserList") public List<UserEntity> getUserList() {
	 * return ofy().load().type(UserEntity.class).list(); }
	 */

	@ApiMethod(name = "getUserByEmailID", path = "getUserByEmailID")
	public List<UserEntity> getUserByEmailID(@Named("email_id") String email, @Named("forLogin") boolean forLogin) {
		List<UserEntity> list = ofy().load().type(UserEntity.class).filter("email_id", email).list();
		if (list == null || list.size() == 0) {
			return null;
		} else {
			if (forLogin) {
				UserEntity foundUser = list.get(0);
				foundUser.setLastLoginDate(new Date());
				ofy().save().entity(foundUser);
				// This is save async.
				return list;
			}
			return list;
		}
	}

	@ApiMethod(name = "getUserByID", path = "getUserByID")
	public UserEntity getUserByID(@Named("busId") Long busId, @Named("id") Long id) {
		List<UserEntity> list = ofy().load().type(UserEntity.class)
				.filterKey(Key.create(Key.create(BusinessEntity.class, busId), UserEntity.class, id)).list();
		UserEntity userE = list.size() > 0 ? list.get(0) : null;
		return userE;
	}

	@ApiMethod(name = "getUserByWebSafeKey", path = "getUserByWebSafeKey")
	public UserEntity getUserByWebSafeKey(@Named("keyStr") String webSafeString) {
		UserEntity userE = ofy().load().type(UserEntity.class).filterKey(Key.create(webSafeString)).first().now();
		return userE;
	}

	@ApiMethod(name = "getBusinessByEmailID", path = "getBusinessByEmailID")
	public BusinessEntity getBusinessByEmailID(@Named("adminEmailId") String emailid) {
		List<BusinessEntity> list = ofy().load().type(BusinessEntity.class).filter("adminEmailId", emailid).list();
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

	@ApiMethod(name = "login")
	public List<UserEntity> login(@Named("email_id") String email, @Named("password") String pass) {
		List<UserEntity> list = null;
		if (email.contains("@")) {
			list = ofy().load().type(UserEntity.class).filter("email_id", email).order("createdDate").list();
		} else if (email.length() == 10) {
			list = ofy().load().type(UserEntity.class).filter("employeeDetail.phone1", email).list();
		} else if (isInteger(email)) {
			list = ofy().load().type(UserEntity.class).filter("employeeDetail.empId", Integer.parseInt(email)).list();
		}

		if (list != null & list.size() > 0) {
			// Login will be checked against first/the created first. Notice
			// order by clause above
			UserEntity foundUser = list.get(0);
			if (foundUser.getIsLoginAllowed() && foundUser.getPassword() != null
					&& foundUser.getPassword().equalsIgnoreCase(pass)) {
				foundUser.setLastLoginDate(new Date());
				ofy().save().entity(foundUser);
				// This is save async
				return list;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private static boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@ApiMethod(name = "addBusiness")
	public BusinessEntity addBusiness(BusinessEntity business) {

		return ofy().execute(TxnType.REQUIRED, new Work<BusinessEntity>() {

			private BusinessEntity business;

			private Work<BusinessEntity> init(BusinessEntity business) {
				this.business = business;
				return this;
			}

			public BusinessEntity run() {
				Date date = new Date();
				String DATE_FORMAT = "dd/MM/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				boolean isFreshBusiness = business.getId() == null;
				if (isFreshBusiness) {
					// Seth Basic Auths
					if (business.getAuthorizations() == null || business.getAuthorizations().isEmpty())
						business.setAuthorizations(Constants.NEW_BIZ_DEFAULT_AUTHS);
					business.setCreatedDate(new Date());
					business.setRegisterDate(sdf.format(date));
					ProtostarAdminService protostarAdminService = new ProtostarAdminService();
					BusinessPlanType freeBusinessPlan = protostarAdminService.getFreeBusinessPlan();
					business.setBusinessPlan(freeBusinessPlan);

				} else {
					business.setModifiedDate(new Date());
				}

				ofy().save().entity(business).now();

				if (isFreshBusiness) {
					// this is being created. Perform basic configs
					// Set default department
					EmpDepartment defaultDepartment = new EmpDepartment();
					defaultDepartment.setName(Constants.DEFAULT_EMP_DEPT);
					defaultDepartment.setBusiness(business);
					defaultDepartment.setCreatedDate(new Date());
					addEmpDepartment(defaultDepartment);

					HrService hrservice = new HrService();
					HRSettingsEntity hrSettingsEntity = new HRSettingsEntity();

					hrSettingsEntity.setBusiness(business);
					hrSettingsEntity
							.setMonthlySalaryStructureRules(HREntityUtil.getStandardMonthlySalaryStructureRules());
					hrSettingsEntity
							.setMonthlySalaryDeductionRules(HREntityUtil.getStandardMonthlySalaryDeductionRules());
					hrservice.addHRSettings(hrSettingsEntity);

					WarehouseService warehouseService = new WarehouseService();
					WarehouseEntity defaultWH = new WarehouseEntity();
					defaultWH.setBusiness(business);
					defaultWH.setWarehouseName(Constants.DEFAULT_STOCK_WAREHOUSE);
					warehouseService.addWarehouse(defaultWH);

				}
				return business;
			}
		}.init(business));
	}

	@ApiMethod(name = "getUsersByLoginAllowed", path = "getUsersByLoginAllowed")
	public List<UserEntity> getUsersByLoginAllowed(@Named("busId") Long id,
			@Named("loginAllowed") Boolean isLoginAllowed) {
		List<UserEntity> list = ofy().load().type(UserEntity.class).ancestor(Key.create(BusinessEntity.class, id))
				.filter("isLoginAllowed", isLoginAllowed).list();

		return list;
	}

	@ApiMethod(name = "getUsersByBusinessId", path = "getUsersByBusinessId")
	public List<UserEntity> getUsersByBusinessId(@Named("id") Long id) {
		List<UserEntity> list = ofy().transactionless().load().type(UserEntity.class)
				.ancestor(Key.create(BusinessEntity.class, id)).filter("isActive", true).list();
		return list;
	}

	@ApiMethod(name = "getInActiveUsersByBusinessId", path = "getInActiveUsersByBusinessId")
	public List<UserEntity> getInActiveUsersByBusinessId(@Named("id") Long id) {
		List<UserEntity> list = ofy().transactionless().load().type(UserEntity.class)
				.ancestor(Key.create(BusinessEntity.class, id)).filter("isActive", false).list();
		return list;
	}

	@ApiMethod(name = "isUserExists")
	public ServerMsg isUserExists(@Named("bizId") Long id, @Named("email_id") String emailID) {
		ServerMsg serverMsg = new ServerMsg();
		List<UserEntity> list = ofy().load().type(UserEntity.class).ancestor(Key.create(BusinessEntity.class, id))
				.filter("email_id", emailID).list();

		/* if(list.get(0).equals(null)){ */
		if (list.size() == 0) {
			serverMsg.setReturnBool(false);
		} else {
			serverMsg.setReturnBool(true);
		}

		return serverMsg;
	}

	@ApiMethod(name = "getBusinessCounterList", path = "getBusinessCounterList")
	public List<CounterEntity> getBusinessCounterList(@Named("id") Long id) {
		List<CounterEntity> counterList = ofy().load().type(CounterEntity.class)
				.ancestor(Key.create(BusinessEntity.class, id)).list();
		for (CounterEntity counterEntity : counterList) {
			int sum = 0;
			List<CounterShard> shardList = ofy().load().type(CounterShard.class).ancestor(counterEntity).list();
			for (CounterShard counterShard : shardList) {
				sum += counterShard.getCount();
			}

			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					Key.create(BusinessEntity.class, id), counterEntity.getCounterName());
			counterEntity.setTempDSCounterValue(sum);
			counterEntity.setTempMCCounterValue(sequenceGenService.getTempMCValue());

		}
		return counterList;
	}

	@ApiMethod(name = "getBusinessList", path = "getBusinessList")
	public List<BusinessEntity> getBusinessList() {
		return ofy().load().type(BusinessEntity.class).list();
	}

	@ApiMethod(name = "getLogUploadURL")
	public ServerMsg getLogUploadURL() {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		String createUploadUrl = blobstoreService.createUploadUrl("/UploadServlet");
		ServerMsg serverMsg = new ServerMsg();
		serverMsg.setMsg(createUploadUrl);
		return serverMsg;
	}

	public static void addUserBatch(List<UserEntity> userList) {
		ofy().save().entities(userList);
	}

}