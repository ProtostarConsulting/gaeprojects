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
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.proadmin.entities.BusinessPlanType;
import com.protostar.billingnstock.proadmin.entities.BusinessPlanType.PlanType;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.EmpDepartment;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.AccountGroupType;

@Api(name = "proadminService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.proadmin.services", ownerName = "com.protostar.billingnstock.proadmin.services", packagePath = ""))
public class ProtostarAdminService {

	private static final Logger log = Logger.getLogger(ProtostarAdminService.class.getName());

	@ApiMethod(name = "addBusinessPlan", path = "addBusinessPlan")
	public void addBusinessPlan(BusinessPlanType account) {
		ofy().save().entity(account).now();

	}

	@ApiMethod(name = "getBusinessPlans", path = "getBusinessPlans")
	public List<BusinessPlanType> getBusinessPlans() {
		log.info("Inside getBusinessPlans.");
		return ofy().load().type(BusinessPlanType.class).list();
	}

	@ApiMethod(name = "getBusinessList", path = "getBusinessList")
	public List<BusinessEntity> getBusinessList() {
		log.info("Inside getBusinessList.");
		List<BusinessEntity> list = ofy().load().type(BusinessEntity.class).list();
		return list;
	}

	@ApiMethod(name = "createDefaultBusiness", path = "createDefaultBusiness")
	public void createDefaultBusiness() {

		createDefaultBusinessPlans();
		List<BusinessEntity> bizList = ofy().load().type(BusinessEntity.class).list();
		if (bizList.size() > 0) {
			return;
		}

		Date date = new Date();
		String DATE_FORMAT = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		List<BusinessPlanType> accountt = ofy().load().type(BusinessPlanType.class).list();
		BusinessPlanType businessPlan = new BusinessPlanType();
		for (int i = 0; i < accountt.size(); i++) {
			if (accountt.get(i).getPlanName().equals("Platinum")) {
				businessPlan = accountt.get(i);
			}
		}

		BusinessEntity businessEntity = new BusinessEntity();
		/*
		 * businessEntity.setAdminFirstName("ganesh");
		 * businessEntity.setAdminEmailId("ganesh.lawande@protostar.co.in");
		 * businessEntity.setAdminLastName("Lawande");
		 */
		businessEntity.setBusinessName("Protostar Consulting Services");
		businessEntity.setBusinessPlan(businessPlan);
		businessEntity.setRegisterDate(sdf.format(date));
		String authorizations = Constants.PROTOSTAR_DEFAULT_AUTHS;
		System.out.println("authorizations********************" + authorizations);
		businessEntity.setAuthorizations(authorizations);

		Address address = new Address();
		businessEntity.setAddress(address);
		address.setLine1("E101, Manaimangal Apt, Behind Sharda Motor, Old Mumbai Highway");
		address.setLine2("Kasarwadi");
		address.setCity("Pune");
		address.setState("MH");
		address.setPin("411034");

		UserService userService = new UserService();
		// ofy().save().entity(businessEntity).now();
		userService.addBusiness(businessEntity);

		createDefaultDepartments(businessEntity.getId());

		UserEntity userEntity = new UserEntity();
		userEntity.setBusiness(businessEntity);
		userEntity.setEmail_id("ganesh.lawande@protostar.co.in");
		userEntity.setFirstName("Ganesh");
		userEntity.setLastName("Lawande");
		userEntity.setIsGoogleUser(true);
		userEntity.setAuthority(Arrays.asList("admin"));
		userEntity.setAuthorizations(authorizations);
		// ofy().save().entity(userEntity).now();
		userService.addUser(userEntity);

		// ------------------------------

		UserEntity userEntity1 = new UserEntity();
		userEntity1.setBusiness(businessEntity);
		userEntity1.setEmail_id("shantanu@protostar.co.in");
		userEntity1.setFirstName("Shantanu");
		userEntity1.setLastName("Lawande");
		userEntity1.setIsGoogleUser(true);
		userEntity1.setAuthority(Arrays.asList("admin"));
		userEntity1.setAuthorizations(authorizations);
		// ofy().save().entity(userEntity1).now();
		userService.addUser(userEntity1);

		UserEntity userEntity2 = new UserEntity();
		userEntity2.setBusiness(businessEntity);
		userEntity2.setEmail_id("sagar@protostar.co.in");
		userEntity2.setFirstName("Sagar");
		userEntity2.setLastName("Sadawatre");
		userEntity2.setIsGoogleUser(true);
		userEntity2.setAuthority(Arrays.asList("admin"));
		userEntity2.setAuthorizations(authorizations);
		// ofy().save().entity(userEntity2).now();
		userService.addUser(userEntity2);

		UserEntity userEntity3 = new UserEntity();
		userEntity3.setBusiness(businessEntity);
		userEntity3.setEmail_id("deepali@protostar.co.in");
		userEntity3.setFirstName("Deepali");
		userEntity3.setLastName("Mate");
		userEntity3.setIsGoogleUser(true);
		userEntity3.setAuthority(Arrays.asList("admin"));
		userEntity3.setAuthorizations(authorizations);
		// ofy().save().entity(userEntity3).now();
		userService.addUser(userEntity3);
		
		
		UserEntity userEntity4 = new UserEntity();
		userEntity4.setBusiness(businessEntity);
		userEntity4.setEmail_id("matedeepali48@gmail.com");
		userEntity4.setFirstName("Deepali");
		userEntity4.setLastName("Mate");
		userEntity4.setIsGoogleUser(true);
		userEntity4.setAuthority(Arrays.asList("admin"));
		userEntity4.setAuthorizations(authorizations);
		// ofy().save().entity(userEntity3).now();
		userService.addUser(userEntity4);
		
	}

	@ApiMethod(name = "createDefaultDepartments", path = "createDefaultDepartments")
	public void createDefaultDepartments(@Named("businessId") Long id) {

		UserService userService = new UserService();

		BusinessEntity businessEntity = userService.getBusinessById(id);

		EmpDepartment department = new EmpDepartment();
		department.setName("Default");
		department.setBusiness(businessEntity);
		userService.addEmpDepartment(department);

		department = new EmpDepartment();
		department.setName("Staff");
		department.setBusiness(businessEntity);
		userService.addEmpDepartment(department);

		department = new EmpDepartment();
		department.setName("Sales");
		department.setBusiness(businessEntity);
		userService.addEmpDepartment(department);
	}

	@ApiMethod(name = "createDefaultBusinessPlans", path = "createDefaultBusinessPlans")
	public void createDefaultBusinessPlans() {
		// try{

		List<BusinessPlanType> accountList = ofy().load().type(BusinessPlanType.class).list();
		if (accountList.size() > 0) {
			return;
		}

		BusinessPlanType freePlan = new BusinessPlanType();
		freePlan.setPlanName("Free");
		freePlan.setDescription("Free life time, with upto 1000 Records.");
		freePlan.setPlanType(PlanType.FREE);
		freePlan.setMaxuser(20);
		freePlan.setMaxRecords(1000l);
		freePlan.setBaseCost(0f);
		freePlan.setPaymentDesc("Free Plan");
		ofy().save().entity(freePlan).now();

		BusinessPlanType basicPlan = new BusinessPlanType();
		basicPlan.setPlanName("Basic");
		basicPlan.setDescription("Rs. 12,000 Per Year + tax, upto 1 Lakh Records.");
		basicPlan.setPlanType(PlanType.STANDARD);
		basicPlan.setMaxuser(100);
		basicPlan.setMaxRecords(100000l);
		basicPlan.setBaseCost(12000f);
		basicPlan.setPaymentDesc("Rs. 12000 Per Year + Tax");
		ofy().save().entity(basicPlan).now();

		BusinessPlanType silverPlan = new BusinessPlanType();
		silverPlan.setPlanName("Silver");
		silverPlan.setDescription("24,000 Per Year + tax, upto 10 Lakh Records.");
		silverPlan.setPlanType(PlanType.STANDARD);
		silverPlan.setMaxuser(100);
		silverPlan.setMaxRecords(1000000l);
		silverPlan.setBaseCost(24000f);
		silverPlan.setPaymentDesc("Rs. 24000 Per Year + Tax");
		ofy().save().entity(silverPlan).now();

		BusinessPlanType goldPlan = new BusinessPlanType();
		goldPlan.setPlanName("Gold");
		goldPlan.setDescription("Rs. 36,000 Per Year + tax, upto 100 Lakh Records.");
		goldPlan.setPlanType(PlanType.STANDARD);
		goldPlan.setMaxuser(500);
		goldPlan.setMaxRecords(10000000l);
		goldPlan.setBaseCost(36000f);
		goldPlan.setPaymentDesc("Rs. 36000 Per Year + Tax");
		ofy().save().entity(goldPlan).now();

		BusinessPlanType platinumPlan = new BusinessPlanType();
		platinumPlan.setPlanName("Platinum");
		platinumPlan.setDescription("1,00,000 Per Year + tax, umlimited Records.");
		platinumPlan.setPlanType(PlanType.STANDARD);
		platinumPlan.setMaxuser(10000);
		platinumPlan.setMaxRecords(1000000000l);
		platinumPlan.setBaseCost(100000f);
		platinumPlan.setPaymentDesc("Rs. 100000 Per Year + Tax");
		ofy().save().entity(platinumPlan).now();

	}

	public BusinessPlanType getFreeBusinessPlan() {
		List<BusinessPlanType> accountList = ofy().transactionless().load().type(BusinessPlanType.class)
				.filter("planType", PlanType.FREE).list();
		if (accountList.size() > 0) {
			return accountList.get(0);
		}

		return null;
	}

	@ApiMethod(name = "createAccountingGroups", path = "createAccountingGroups")
	public void createAccountingGroups(@Named("id") Long bizId) {

		List<AccountGroupEntity> acList = ofy().load().type(AccountGroupEntity.class)
				.ancestor(Key.create(BusinessEntity.class, bizId)).list();

		if (acList != null && acList.size() > 0) {
			return;
		}

		UserService userService = new UserService();

		BusinessEntity business = userService.getBusinessById(bizId);

		AccountGroupEntity fixedAsetAccountGroupEntity = new AccountGroupEntity();
		// BusinessEntity business= new BusinessEntity();

		fixedAsetAccountGroupEntity.setBusiness(business);
		fixedAsetAccountGroupEntity.setCreatedDate(new Date());
		fixedAsetAccountGroupEntity.setModifiedDate(new Date());
		fixedAsetAccountGroupEntity.setGroupName("Fixed Assets");
		fixedAsetAccountGroupEntity.setIsPrimary(true);
		fixedAsetAccountGroupEntity.setAccountGroupType(AccountGroupType.ASSETS);
		ofy().save().entity(fixedAsetAccountGroupEntity).now();

		AccountGroupEntity suspenseAcGroupEntity = new AccountGroupEntity();
		suspenseAcGroupEntity.setBusiness(business);
		suspenseAcGroupEntity.setCreatedDate(new Date());
		suspenseAcGroupEntity.setModifiedDate(new Date());
		suspenseAcGroupEntity.setGroupName("Suspense A/c");
		suspenseAcGroupEntity.setIsPrimary(true);
		suspenseAcGroupEntity.setAccountGroupType(AccountGroupType.LIABILITIES);
		ofy().save().entity(suspenseAcGroupEntity).now();

		AccountGroupEntity salesAccountsGroupEntity = new AccountGroupEntity();
		salesAccountsGroupEntity.setBusiness(business);
		salesAccountsGroupEntity.setCreatedDate(new Date());
		salesAccountsGroupEntity.setModifiedDate(new Date());
		salesAccountsGroupEntity.setGroupName("Sales Accounts");
		salesAccountsGroupEntity.setIsPrimary(true);
		salesAccountsGroupEntity.setAccountGroupType(AccountGroupType.INCOME);
		ofy().save().entity(salesAccountsGroupEntity).now();

		AccountGroupEntity purchaseAccountsGroupEntity = new AccountGroupEntity();
		purchaseAccountsGroupEntity.setBusiness(business);
		purchaseAccountsGroupEntity.setCreatedDate(new Date());
		purchaseAccountsGroupEntity.setModifiedDate(new Date());
		purchaseAccountsGroupEntity.setGroupName("Purchase Accounts");
		purchaseAccountsGroupEntity.setIsPrimary(true);
		purchaseAccountsGroupEntity.setAccountGroupType(AccountGroupType.EXPENSES);

		ofy().save().entity(purchaseAccountsGroupEntity).now();

		AccountGroupEntity miscExpensesASSETGroupEntity = new AccountGroupEntity();
		miscExpensesASSETGroupEntity.setBusiness(business);
		miscExpensesASSETGroupEntity.setCreatedDate(new Date());
		miscExpensesASSETGroupEntity.setModifiedDate(new Date());
		miscExpensesASSETGroupEntity.setGroupName("Misc.Expenses (ASSET)");
		miscExpensesASSETGroupEntity.setIsPrimary(true);
		miscExpensesASSETGroupEntity.setAccountGroupType(AccountGroupType.ASSETS);

		ofy().save().entity(miscExpensesASSETGroupEntity).now();

		AccountGroupEntity loansLiabilityGroupEntity = new AccountGroupEntity();
		loansLiabilityGroupEntity.setBusiness(business);
		loansLiabilityGroupEntity.setCreatedDate(new Date());
		loansLiabilityGroupEntity.setModifiedDate(new Date());
		loansLiabilityGroupEntity.setGroupName("Loans (Liability)");
		loansLiabilityGroupEntity.setIsPrimary(true);
		loansLiabilityGroupEntity.setAccountGroupType(AccountGroupType.LIABILITIES);

		ofy().save().entity(loansLiabilityGroupEntity).now();

		AccountGroupEntity investmentsGroupEntity = new AccountGroupEntity();
		investmentsGroupEntity.setBusiness(business);
		investmentsGroupEntity.setCreatedDate(new Date());
		investmentsGroupEntity.setModifiedDate(new Date());
		investmentsGroupEntity.setGroupName("Investments");
		investmentsGroupEntity.setIsPrimary(true);
		investmentsGroupEntity.setAccountGroupType(AccountGroupType.ASSETS);

		ofy().save().entity(investmentsGroupEntity).now();

		AccountGroupEntity indirectIncomesGroupEntity = new AccountGroupEntity();
		indirectIncomesGroupEntity.setBusiness(business);
		indirectIncomesGroupEntity.setCreatedDate(new Date());
		indirectIncomesGroupEntity.setModifiedDate(new Date());
		indirectIncomesGroupEntity.setGroupName("Indirect  Incomes");
		indirectIncomesGroupEntity.setIsPrimary(true);
		indirectIncomesGroupEntity.setAccountGroupType(AccountGroupType.OTHERINCOMES);

		ofy().save().entity(indirectIncomesGroupEntity).now();

		AccountGroupEntity indirectExpensesGroupEntity = new AccountGroupEntity();
		indirectExpensesGroupEntity.setBusiness(business);
		indirectExpensesGroupEntity.setCreatedDate(new Date());
		indirectExpensesGroupEntity.setModifiedDate(new Date());
		indirectExpensesGroupEntity.setGroupName("Indirect Expenses");
		indirectExpensesGroupEntity.setIsPrimary(true);
		indirectExpensesGroupEntity.setAccountGroupType(AccountGroupType.OTHEREXPENCES);

		ofy().save().entity(indirectExpensesGroupEntity).now();

		AccountGroupEntity directIncomesGroupEntity = new AccountGroupEntity();
		directIncomesGroupEntity.setBusiness(business);
		directIncomesGroupEntity.setCreatedDate(new Date());
		directIncomesGroupEntity.setModifiedDate(new Date());
		directIncomesGroupEntity.setGroupName("Direct Incomes");
		directIncomesGroupEntity.setIsPrimary(true);
		directIncomesGroupEntity.setAccountGroupType(AccountGroupType.INCOME);

		ofy().save().entity(directIncomesGroupEntity).now();

		AccountGroupEntity directExpensesGroupEntity = new AccountGroupEntity();
		directExpensesGroupEntity.setBusiness(business);
		directExpensesGroupEntity.setCreatedDate(new Date());
		directExpensesGroupEntity.setModifiedDate(new Date());
		directExpensesGroupEntity.setGroupName("Direct Expenses");
		directExpensesGroupEntity.setIsPrimary(true);
		directExpensesGroupEntity.setAccountGroupType(AccountGroupType.EXPENSES);

		ofy().save().entity(directExpensesGroupEntity).now();

		AccountGroupEntity currentLiabilitiesGroupEntity = new AccountGroupEntity();
		currentLiabilitiesGroupEntity.setBusiness(business);
		currentLiabilitiesGroupEntity.setCreatedDate(new Date());
		currentLiabilitiesGroupEntity.setModifiedDate(new Date());
		currentLiabilitiesGroupEntity.setGroupName("Current Liabilities");
		currentLiabilitiesGroupEntity.setIsPrimary(true);
		currentLiabilitiesGroupEntity.setAccountGroupType(AccountGroupType.LIABILITIES);

		ofy().save().entity(currentLiabilitiesGroupEntity).now();

		AccountGroupEntity currentAssetsGroupEntity = new AccountGroupEntity();
		currentAssetsGroupEntity.setBusiness(business);
		currentAssetsGroupEntity.setCreatedDate(new Date());
		currentAssetsGroupEntity.setModifiedDate(new Date());
		currentAssetsGroupEntity.setGroupName("Current Assets");
		currentAssetsGroupEntity.setIsPrimary(true);
		currentAssetsGroupEntity.setAccountGroupType(AccountGroupType.ASSETS);

		ofy().save().entity(currentAssetsGroupEntity).now();

		AccountGroupEntity capitalAccountGroupEntity = new AccountGroupEntity();
		capitalAccountGroupEntity.setBusiness(business);
		capitalAccountGroupEntity.setCreatedDate(new Date());
		capitalAccountGroupEntity.setModifiedDate(new Date());
		capitalAccountGroupEntity.setGroupName("Capital Account");
		capitalAccountGroupEntity.setIsPrimary(true);
		capitalAccountGroupEntity.setAccountGroupType(AccountGroupType.LIABILITIES);

		ofy().save().entity(capitalAccountGroupEntity).now();

		AccountGroupEntity branchDivisionsGroupEntity = new AccountGroupEntity();
		branchDivisionsGroupEntity.setBusiness(business);
		branchDivisionsGroupEntity.setCreatedDate(new Date());
		branchDivisionsGroupEntity.setModifiedDate(new Date());
		branchDivisionsGroupEntity.setGroupName("Branch Divisions");
		branchDivisionsGroupEntity.setIsPrimary(true);
		branchDivisionsGroupEntity.setAccountGroupType(AccountGroupType.LIABILITIES);

		ofy().save().entity(branchDivisionsGroupEntity).now();

		// ***********************************SUB
		// GROUP***********************************

		AccountGroupEntity bankAccountsAccountGroupEntity = new AccountGroupEntity();
		bankAccountsAccountGroupEntity.setBusiness(business);
		bankAccountsAccountGroupEntity.setCreatedDate(new Date());
		bankAccountsAccountGroupEntity.setModifiedDate(new Date());
		bankAccountsAccountGroupEntity.setGroupName("Bank Accounts");
		bankAccountsAccountGroupEntity.setIsPrimary(false);
		bankAccountsAccountGroupEntity.setParent(currentAssetsGroupEntity);
		ofy().save().entity(bankAccountsAccountGroupEntity).now();

		AccountGroupEntity bankODA_c = new AccountGroupEntity();
		bankODA_c.setBusiness(business);
		bankODA_c.setCreatedDate(new Date());
		bankODA_c.setModifiedDate(new Date());
		bankODA_c.setGroupName("Bank OD A/c");
		bankODA_c.setIsPrimary(false);
		bankODA_c.setParent(loansLiabilityGroupEntity);
		ofy().save().entity(bankODA_c).now();

		AccountGroupEntity Cashinhand = new AccountGroupEntity();
		Cashinhand.setBusiness(business);
		Cashinhand.setCreatedDate(new Date());
		Cashinhand.setModifiedDate(new Date());
		Cashinhand.setGroupName("Cash-in-hand");
		Cashinhand.setIsPrimary(false);
		Cashinhand.setParent(currentAssetsGroupEntity);
		ofy().save().entity(Cashinhand).now();

		AccountGroupEntity depositsAsset = new AccountGroupEntity();
		depositsAsset.setBusiness(business);
		depositsAsset.setCreatedDate(new Date());
		depositsAsset.setModifiedDate(new Date());
		depositsAsset.setGroupName("Deposits (Asset)");
		depositsAsset.setIsPrimary(false);
		depositsAsset.setParent(currentAssetsGroupEntity);
		ofy().save().entity(depositsAsset).now();

		AccountGroupEntity DutiesandTaxesAccountGroupEntity1 = new AccountGroupEntity();
		DutiesandTaxesAccountGroupEntity1.setBusiness(business);
		DutiesandTaxesAccountGroupEntity1.setCreatedDate(new Date());
		DutiesandTaxesAccountGroupEntity1.setModifiedDate(new Date());
		DutiesandTaxesAccountGroupEntity1.setGroupName("Duties & Taxes");
		DutiesandTaxesAccountGroupEntity1.setIsPrimary(false);
		DutiesandTaxesAccountGroupEntity1.setParent(currentLiabilitiesGroupEntity);
		ofy().save().entity(DutiesandTaxesAccountGroupEntity1).now();

		AccountGroupEntity loansandAdvancesAssetAccountGroupEntity = new AccountGroupEntity();
		loansandAdvancesAssetAccountGroupEntity.setBusiness(business);
		loansandAdvancesAssetAccountGroupEntity.setCreatedDate(new Date());
		loansandAdvancesAssetAccountGroupEntity.setModifiedDate(new Date());
		loansandAdvancesAssetAccountGroupEntity.setGroupName("Loans & Advances (Asset)");
		loansandAdvancesAssetAccountGroupEntity.setIsPrimary(false);
		loansandAdvancesAssetAccountGroupEntity.setParent(currentAssetsGroupEntity);
		ofy().save().entity(loansandAdvancesAssetAccountGroupEntity).now();

		AccountGroupEntity provisionsAccountGroupEntity1 = new AccountGroupEntity();
		provisionsAccountGroupEntity1.setBusiness(business);
		provisionsAccountGroupEntity1.setCreatedDate(new Date());
		provisionsAccountGroupEntity1.setModifiedDate(new Date());
		provisionsAccountGroupEntity1.setGroupName("Provisions");
		provisionsAccountGroupEntity1.setIsPrimary(false);
		provisionsAccountGroupEntity1.setParent(currentLiabilitiesGroupEntity);
		ofy().save().entity(provisionsAccountGroupEntity1).now();

		AccountGroupEntity reservesandSurplus = new AccountGroupEntity();
		reservesandSurplus.setBusiness(business);
		reservesandSurplus.setCreatedDate(new Date());
		reservesandSurplus.setModifiedDate(new Date());
		reservesandSurplus.setGroupName("Reserves & Surplus");
		reservesandSurplus.setIsPrimary(false);
		reservesandSurplus.setParent(capitalAccountGroupEntity);
		ofy().save().entity(reservesandSurplus).now();

		AccountGroupEntity securedLoans = new AccountGroupEntity();
		securedLoans.setBusiness(business);
		securedLoans.setCreatedDate(new Date());
		securedLoans.setModifiedDate(new Date());
		securedLoans.setGroupName("Secured Loans");
		securedLoans.setIsPrimary(false);
		securedLoans.setParent(loansLiabilityGroupEntity);
		ofy().save().entity(securedLoans).now();

		AccountGroupEntity Stockinhand = new AccountGroupEntity();
		Stockinhand.setBusiness(business);
		Stockinhand.setCreatedDate(new Date());
		Stockinhand.setModifiedDate(new Date());
		Stockinhand.setGroupName("Stock-in-Hand");
		Stockinhand.setIsPrimary(false);
		Stockinhand.setParent(currentAssetsGroupEntity);
		ofy().save().entity(Stockinhand).now();

		AccountGroupEntity sundryCreditors = new AccountGroupEntity();
		sundryCreditors.setBusiness(business);
		sundryCreditors.setCreatedDate(new Date());
		sundryCreditors.setModifiedDate(new Date());
		sundryCreditors.setGroupName("Sundry Creditors");
		sundryCreditors.setIsPrimary(false);
		sundryCreditors.setParent(currentLiabilitiesGroupEntity);
		ofy().save().entity(sundryCreditors).now();

		AccountGroupEntity sundryDebtors = new AccountGroupEntity();
		sundryDebtors.setBusiness(business);
		sundryDebtors.setCreatedDate(new Date());
		sundryDebtors.setModifiedDate(new Date());
		sundryDebtors.setGroupName("Sundry Debtors");
		sundryDebtors.setIsPrimary(false);
		sundryDebtors.setParent(currentAssetsGroupEntity);
		ofy().save().entity(sundryDebtors).now();

		AccountGroupEntity unsecuredLoans = new AccountGroupEntity();
		unsecuredLoans.setBusiness(business);
		unsecuredLoans.setCreatedDate(new Date());
		unsecuredLoans.setModifiedDate(new Date());
		unsecuredLoans.setGroupName("Unsecured Loans");
		unsecuredLoans.setIsPrimary(false);
		unsecuredLoans.setParent(loansLiabilityGroupEntity);
		ofy().save().entity(unsecuredLoans).now();
		//////// Profit and Loss

		AccountGroupEntity pAndL = new AccountGroupEntity();
		pAndL.setBusiness(business);
		pAndL.setCreatedDate(new Date());
		pAndL.setModifiedDate(new Date());
		pAndL.setGroupName("Profit & Loss Acc");
		pAndL.setIsPrimary(true);
		pAndL.setAccountGroupType(AccountGroupType.PANDL);

		ofy().save().entity(pAndL).now();

	}

}// end of InternetService
