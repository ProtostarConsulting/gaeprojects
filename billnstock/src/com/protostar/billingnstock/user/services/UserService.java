package com.protostar.billingnstock.user.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.protostar.billingnstock.account.entities.CurrentFinancialYear;
import com.protostar.billingnstock.hr.entities.HREntityUtil;
import com.protostar.billingnstock.hr.entities.HRSettingsEntity;
import com.protostar.billingnstock.hr.services.HrService;
import com.protostar.billingnstock.proadmin.entities.BusinessPlanType;
import com.protostar.billingnstock.proadmin.services.ProtostarAdminService;
import com.protostar.billingnstock.purchase.entities.SupplierEntity;
import com.protostar.billingnstock.stock.entities.StockItemProductTypeEntity;
import com.protostar.billingnstock.stock.entities.StockItemTypeCategory;
import com.protostar.billingnstock.stock.entities.StockItemUnit;
import com.protostar.billingnstock.stock.services.StockManagementService;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billingnstock.tax.services.TaxService;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.BusinessSettingsEntity;
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
import com.protostar.billnstock.until.data.UserAuthenticator;
import com.protostar.billnstock.until.data.WebUtil;

@Api(name = "userService", version = "v0.1", clientIds = { Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
		Constants.API_EXPLORER_CLIENT_ID }, audiences = { Constants.ANDROID_AUDIENCE }, scopes = {
				Constants.EMAIL_SCOPE }, namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.user.services", ownerName = "com.protostar.billingnstock.user.services", packagePath = ""))
public class UserService {

	private final Logger logger = Logger.getLogger(UserService.class.getName());

	@ApiMethod(name = "addUser", path = "addUser")
	public UserEntity addUser(UserEntity usr) {

		if (usr.getEmployeeDetail().getDepartment() == null)
			setDefaultDepartment(usr);

		if (usr.getId() == null) {
			usr = ofy().execute(TxnType.REQUIRED, new Work<UserEntity>() {
				private UserEntity usr;

				private Work<UserEntity> init(UserEntity usr) {
					this.usr = usr;
					return this;
				}

				public UserEntity run() {
					logger.info("Adding new user: " + usr.getEmail_id());
					// Number of users should be calculated value....
					// BusinessEntity businessEntity = usr.getBusiness();
					// businessEntity.setTotalUser(businessEntity.getTotalUser()
					// + 1);
					// ofy().save().entity(businessEntity);

					ofy().save().entity(usr);
					return usr;
				}
			}.init(usr));
		} else {
			ofy().save().entity(usr).now();
			return usr;
		}

		// Test Code to Send email to registered user

		/*
		 * User user = UserServiceFactory.getUserService().getCurrentUser();
		 * String recipientAddress = user.getEmail();
		 */
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String messageBody = "Welcome to ProERP! Your account has been created. "
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

		return usr;
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
				UserLoginService userLoginService = new UserLoginService();
				userLoginService.recordUserLogin(foundUser);
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

	@ApiMethod(name = "login", path = "login")
	public List<UserEntity> login(@Named("email_id") String email, @Named("password") String pass) {
		logger.info("Calling userLoginService.login...");
		UserLoginService userLoginService = new UserLoginService();
		return userLoginService.login(email, pass);
	}

	@ApiMethod(name = "logout", path = "logout")
	public void logout() {
		CurrentUserSession currentUserSession = WebUtil.getCurrentUser();
		logger.info("Logging out... : " + currentUserSession);
		if (currentUserSession != null) {
			logger.info("Logging out... currentUserSession.getUser().getEmail_id(): "
					+ currentUserSession.getUser().getEmail_id());
			UserLoginService userLoginService = new UserLoginService();
			userLoginService.recordUserLogout(EntityUtil.getUserWebSafeKey(currentUserSession));
		}
	}

	@ApiMethod(name = "addBusiness")
	public BusinessEntity addBusiness(BusinessEntity business) {

		boolean isFreshBusiness = business.getId() == null;
		business = ofy().execute(TxnType.REQUIRED, new Work<BusinessEntity>() {

			private BusinessEntity business;

			private Work<BusinessEntity> init(BusinessEntity business) {
				this.business = business;
				return this;
			}

			public BusinessEntity run() {
				Date date = new Date();
				String DATE_FORMAT = "dd/MM/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				boolean newBusiness = false;
				if (business.getId() == null) {
					newBusiness = true;
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

				if (newBusiness) {
					
					//Add default Taxes
					TaxService taxService = new TaxService();

					TaxEntity serviceTax = new TaxEntity();
					serviceTax.setBusiness(business);
					serviceTax.setTaxCodeName("Service Tax");
					serviceTax.setTaxPercenatge(15.00f);
					taxService.addTax(serviceTax);

					TaxEntity gst18PerTax = new TaxEntity();
					gst18PerTax.setBusiness(business);
					gst18PerTax.setTaxCodeName("GST-18.00%");
					gst18PerTax.setTaxPercenatge(18.00f);
					taxService.addTax(gst18PerTax);

					StockManagementService stockManagementService = new StockManagementService();

					//Add default Suppliers
					SupplierEntity supplierEntity = new SupplierEntity();
					supplierEntity.setBusiness(business);
					supplierEntity.setSupplierName("Protostar Consulting Services");
					supplierEntity.setContactFName("Ganesh");
					supplierEntity.setContactLName("Lawande");
					supplierEntity.getAddress().setLine1("E101, Manimangal Apt, Kasarwadi");
					supplierEntity.getAddress().setCity("Pune");
					supplierEntity.getAddress().setPin("311034");
					supplierEntity.setPhone1("9922923988");
					supplierEntity.setPhone1("9922923988");
					supplierEntity.setPhone1("9922923988");
					supplierEntity.setEmail("info@protostar.co.in");					
					stockManagementService.addSupplier(supplierEntity);
					
					//Add default Stock Units of Measure
					StockItemUnit stockItemUnitNos = new StockItemUnit();
					stockItemUnitNos.setBusiness(business);
					stockItemUnitNos.setUnitName("nos");
					stockItemUnitNos.setNote("Used where items are count by numbers.");
					stockManagementService.addStockItemUnit(stockItemUnitNos);
					
					StockItemUnit stockItemUnitLiters = new StockItemUnit();
					stockItemUnitLiters.setBusiness(business);
					stockItemUnitLiters.setUnitName("liters");
					stockItemUnitLiters.setNote("Used where liquid items are count by liters.");					
					stockManagementService.addStockItemUnit(stockItemUnitLiters);
					
					//Add default Product Types
					StockItemProductTypeEntity productTypeEntityRM = new StockItemProductTypeEntity();
					productTypeEntityRM.setBusiness(business);
					productTypeEntityRM.setType("Raw Material");
					stockManagementService.addStockItemProductType(productTypeEntityRM);
					
					StockItemProductTypeEntity productTypeEntityFP = new StockItemProductTypeEntity();
					productTypeEntityFP.setBusiness(business);
					productTypeEntityFP.setType("Finished Product");
					stockManagementService.addStockItemProductType(productTypeEntityFP);
					
					StockItemProductTypeEntity productTypeEntitySP = new StockItemProductTypeEntity();
					productTypeEntitySP.setBusiness(business);
					productTypeEntitySP.setType("Spare Part");
					stockManagementService.addStockItemProductType(productTypeEntitySP);
					
					//Add default Stock Item Categories
					StockItemTypeCategory stockItemTypeCategoryEG = new StockItemTypeCategory();
					stockItemTypeCategoryEG.setBusiness(business);
					stockItemTypeCategoryEG.setCatName("Electronic Goods");;
					stockManagementService.addStockItemTypeCategory(stockItemTypeCategoryEG);
					
					StockItemTypeCategory stockItemTypeCategoryFG = new StockItemTypeCategory();
					stockItemTypeCategoryFG.setBusiness(business);
					stockItemTypeCategoryFG.setCatName("Furniture");;
					stockManagementService.addStockItemTypeCategory(stockItemTypeCategoryFG);
					
					
				}

				return business;
			}
		}.init(business));

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
			hrSettingsEntity.setMonthlySalaryStructureRules(HREntityUtil.getStandardMonthlySalaryStructureRules());
			hrSettingsEntity.setMonthlySalaryDeductionRules(HREntityUtil.getStandardMonthlySalaryDeductionRules());
			hrservice.addHRSettings(hrSettingsEntity);

			WarehouseService warehouseService = new WarehouseService();
			WarehouseEntity defaultWH = new WarehouseEntity();
			defaultWH.setBusiness(business);
			defaultWH.setWarehouseName(Constants.DEFAULT_STOCK_WAREHOUSE);
			warehouseService.addWarehouse(defaultWH);

			Calendar fromCalendar = Calendar.getInstance();
			Date today = new Date();
			fromCalendar.setTime(today);
			fromCalendar.set(Calendar.MONTH, Calendar.APRIL);
			fromCalendar.set(Calendar.DAY_OF_MONTH, 0);

			Calendar toCalendar = Calendar.getInstance();
			toCalendar.setTime(today);
			toCalendar.set(Calendar.MONTH, Calendar.MARCH);
			toCalendar.set(Calendar.DAY_OF_MONTH, 30);

			CurrentFinancialYear currentFinancialYear = new CurrentFinancialYear();
			currentFinancialYear.setFromDate(fromCalendar.getTime());
			currentFinancialYear.setToDate(toCalendar.getTime());

			ProtostarAdminService adminService = new ProtostarAdminService();
			adminService.createAccountingGroups(business.getId());

		}

		return business;
	}

	@ApiMethod(name = "getUsersByLoginAllowed", path = "getUsersByLoginAllowed")
	public List<UserEntity> getUsersByLoginAllowed(@Named("busId") Long id,
			@Named("loginAllowed") Boolean isLoginAllowed) {
		List<UserEntity> list = ofy().load().type(UserEntity.class).ancestor(Key.create(BusinessEntity.class, id))
				.filter("isLoginAllowed", isLoginAllowed).list();

		return list;
	}

	@ApiMethod(name = "getUsersByBusinessId", path = "getUsersByBusinessId", authenticators = {
			UserAuthenticator.class })
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

	@ApiMethod(name = "addBusinessSettingsEntity", path = "addBusinessSettingsEntity")
	public BusinessSettingsEntity addBusinessSettingsEntity(BusinessSettingsEntity settingsEntity) {
		ofy().save().entity(settingsEntity).now();
		return settingsEntity;

	}

	@ApiMethod(name = "getBusinessSettingsEntity", path = "getBusinessSettingsEntity")
	public BusinessSettingsEntity getBusinessSettingsEntity(@Named("id") Long busId) {
		BusinessSettingsEntity settingsEntity = ofy().load().type(BusinessSettingsEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).first().now();
		return settingsEntity;

	}
}