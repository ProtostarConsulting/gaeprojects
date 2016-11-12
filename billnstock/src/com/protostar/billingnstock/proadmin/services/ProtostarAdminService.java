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
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.proadmin.entities.AccountType;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.EmpDepartment;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billnstock.entity.Address;

@Api(name = "proadminService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.proadmin.services", ownerName = "com.protostar.billingnstock.proadmin.services", packagePath = ""))
public class ProtostarAdminService {

	private static final Logger log = Logger
			.getLogger(ProtostarAdminService.class.getName());

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

	@ApiMethod(name = "creatAccountAndGroup")
	public void creatAccountAndGroup() {

		List<AccountGroupEntity> acList = ofy().load()
				.type(AccountGroupEntity.class).list();
		if (acList.size() > 0) {
			return;
		}

		AccountGroupEntity fixedAsetAccountGroupEntity = new AccountGroupEntity();

		fixedAsetAccountGroupEntity.setCreatedDate(new Date());
		fixedAsetAccountGroupEntity.setModifiedDate(new Date());
		fixedAsetAccountGroupEntity.setGroupName("Fixed Assets");
		fixedAsetAccountGroupEntity.setIsPrimary(true);
		fixedAsetAccountGroupEntity.setPrimaryType("ASSETS");
		ofy().save().entity(fixedAsetAccountGroupEntity).now();

		AccountGroupEntity suspenseAcGroupEntity = new AccountGroupEntity();

		suspenseAcGroupEntity.setCreatedDate(new Date());
		suspenseAcGroupEntity.setModifiedDate(new Date());
		suspenseAcGroupEntity.setGroupName("SuspenseA/c");
		suspenseAcGroupEntity.setIsPrimary(true);
		suspenseAcGroupEntity.setPrimaryType("SuspenseA/c");
		ofy().save().entity(suspenseAcGroupEntity).now();

		AccountGroupEntity salesAccountsGroupEntity = new AccountGroupEntity();

		salesAccountsGroupEntity.setCreatedDate(new Date());
		salesAccountsGroupEntity.setModifiedDate(new Date());
		salesAccountsGroupEntity.setGroupName("SalesAccounts");
		salesAccountsGroupEntity.setIsPrimary(true);
		salesAccountsGroupEntity.setPrimaryType("SalesAccounts");
		ofy().save().entity(salesAccountsGroupEntity).now();

		AccountGroupEntity purchaseAccountsGroupEntity = new AccountGroupEntity();

		purchaseAccountsGroupEntity.setCreatedDate(new Date());
		purchaseAccountsGroupEntity.setModifiedDate(new Date());
		purchaseAccountsGroupEntity.setGroupName("PurchaseAccounts");
		purchaseAccountsGroupEntity.setIsPrimary(true);
		purchaseAccountsGroupEntity.setPrimaryType(" PurchaseAccounts");
		ofy().save().entity(purchaseAccountsGroupEntity).now();

		AccountGroupEntity miscExpensesASSETGroupEntity = new AccountGroupEntity();

		miscExpensesASSETGroupEntity.setCreatedDate(new Date());
		miscExpensesASSETGroupEntity.setModifiedDate(new Date());
		miscExpensesASSETGroupEntity.setGroupName(" MiscExpensesASSET");
		miscExpensesASSETGroupEntity.setIsPrimary(true);
		miscExpensesASSETGroupEntity.setPrimaryType(" MiscExpensesASSET");
		ofy().save().entity(miscExpensesASSETGroupEntity).now();

		AccountGroupEntity loansLiabilityGroupEntity = new AccountGroupEntity();

		loansLiabilityGroupEntity.setCreatedDate(new Date());
		loansLiabilityGroupEntity.setModifiedDate(new Date());
		loansLiabilityGroupEntity.setGroupName("Loans(Liability)");
		loansLiabilityGroupEntity.setIsPrimary(true);
		loansLiabilityGroupEntity.setPrimaryType(" Loans(Liability)");
		ofy().save().entity(loansLiabilityGroupEntity).now();

		AccountGroupEntity investmentsGroupEntity = new AccountGroupEntity();

		investmentsGroupEntity.setCreatedDate(new Date());
		investmentsGroupEntity.setModifiedDate(new Date());
		investmentsGroupEntity.setGroupName(" Investments");
		investmentsGroupEntity.setIsPrimary(true);
		investmentsGroupEntity.setPrimaryType(" Investments");
		ofy().save().entity(investmentsGroupEntity).now();

		AccountGroupEntity indirectIncomesGroupEntity = new AccountGroupEntity();

		indirectIncomesGroupEntity.setCreatedDate(new Date());
		indirectIncomesGroupEntity.setModifiedDate(new Date());
		indirectIncomesGroupEntity.setGroupName(" IndirectIncomes");
		indirectIncomesGroupEntity.setIsPrimary(true);
		indirectIncomesGroupEntity.setPrimaryType(" IndirectIncomes");
		ofy().save().entity(indirectIncomesGroupEntity).now();

		AccountGroupEntity indirectExpensesGroupEntity = new AccountGroupEntity();

		indirectExpensesGroupEntity.setCreatedDate(new Date());
		indirectExpensesGroupEntity.setModifiedDate(new Date());
		indirectExpensesGroupEntity.setGroupName(" IndirectExpenses");
		indirectExpensesGroupEntity.setIsPrimary(true);
		indirectExpensesGroupEntity.setPrimaryType(" IndirectExpenses");
		ofy().save().entity(indirectExpensesGroupEntity).now();

		AccountGroupEntity directIncomesGroupEntity = new AccountGroupEntity();

		directIncomesGroupEntity.setCreatedDate(new Date());
		directIncomesGroupEntity.setModifiedDate(new Date());
		directIncomesGroupEntity.setGroupName(" DirectIncomes");
		directIncomesGroupEntity.setIsPrimary(true);
		directIncomesGroupEntity.setPrimaryType(" DirectIncomes");
		ofy().save().entity(directIncomesGroupEntity).now();

		AccountGroupEntity directExpensesGroupEntity = new AccountGroupEntity();

		directExpensesGroupEntity.setCreatedDate(new Date());
		directExpensesGroupEntity.setModifiedDate(new Date());
		directExpensesGroupEntity.setGroupName(" DirectExpenses");
		directExpensesGroupEntity.setIsPrimary(true);
		directExpensesGroupEntity.setPrimaryType(" DirectExpenses");
		ofy().save().entity(directExpensesGroupEntity).now();

		AccountGroupEntity currentLiabilitiesGroupEntity = new AccountGroupEntity();

		currentLiabilitiesGroupEntity.setCreatedDate(new Date());
		currentLiabilitiesGroupEntity.setModifiedDate(new Date());
		currentLiabilitiesGroupEntity.setGroupName(" CurrentLiabilities");
		currentLiabilitiesGroupEntity.setIsPrimary(true);
		currentLiabilitiesGroupEntity.setPrimaryType(" CurrentLiabilities");
		ofy().save().entity(currentLiabilitiesGroupEntity).now();

		AccountGroupEntity currentAssetsGroupEntity = new AccountGroupEntity();

		currentAssetsGroupEntity.setCreatedDate(new Date());
		currentAssetsGroupEntity.setModifiedDate(new Date());
		currentAssetsGroupEntity.setGroupName(" CurrentAssets");
		currentAssetsGroupEntity.setIsPrimary(true);
		currentAssetsGroupEntity.setPrimaryType("CurrentAssets");
		ofy().save().entity(currentAssetsGroupEntity).now();

		AccountGroupEntity capitalAccountGroupEntity = new AccountGroupEntity();

		capitalAccountGroupEntity.setCreatedDate(new Date());
		capitalAccountGroupEntity.setModifiedDate(new Date());
		capitalAccountGroupEntity.setGroupName(" CapitalAccount");
		capitalAccountGroupEntity.setIsPrimary(true);
		capitalAccountGroupEntity.setPrimaryType("CapitalAccount");
		ofy().save().entity(capitalAccountGroupEntity).now();

		AccountGroupEntity branchDivisionsGroupEntity = new AccountGroupEntity();

		branchDivisionsGroupEntity.setCreatedDate(new Date());
		branchDivisionsGroupEntity.setModifiedDate(new Date());
		branchDivisionsGroupEntity.setGroupName(" BranchDivisions");
		branchDivisionsGroupEntity.setIsPrimary(true);
		branchDivisionsGroupEntity.setPrimaryType("BranchDivisions");
		ofy().save().entity(branchDivisionsGroupEntity).now();

		// ***********************************SUB
		// GROUP***********************************

		AccountGroupEntity bankAccountsAccountGroupEntity = new AccountGroupEntity();

		bankAccountsAccountGroupEntity.setCreatedDate(new Date());
		bankAccountsAccountGroupEntity.setModifiedDate(new Date());
		bankAccountsAccountGroupEntity.setGroupName("Bank Accounts");
		bankAccountsAccountGroupEntity.setIsPrimary(false);
		bankAccountsAccountGroupEntity.setParent(currentAssetsGroupEntity);
		ofy().save().entity(bankAccountsAccountGroupEntity).now();

		AccountGroupEntity bankODA_c = new AccountGroupEntity();

		bankODA_c.setCreatedDate(new Date());
		bankODA_c.setModifiedDate(new Date());
		bankODA_c.setGroupName("Bank OD A/c");
		bankODA_c.setIsPrimary(false);
		bankODA_c.setParent(loansLiabilityGroupEntity);
		ofy().save().entity(bankODA_c).now();

		AccountGroupEntity Cashinhand = new AccountGroupEntity();

		Cashinhand.setCreatedDate(new Date());
		Cashinhand.setModifiedDate(new Date());
		Cashinhand.setGroupName("Cash-in-hand");
		Cashinhand.setIsPrimary(false);
		Cashinhand.setParent(currentAssetsGroupEntity);
		ofy().save().entity(Cashinhand).now();

		AccountGroupEntity depositsAsset = new AccountGroupEntity();

		depositsAsset.setCreatedDate(new Date());
		depositsAsset.setModifiedDate(new Date());
		depositsAsset.setGroupName("Deposits (Asset)");
		depositsAsset.setIsPrimary(false);
		depositsAsset.setParent(currentAssetsGroupEntity);
		ofy().save().entity(depositsAsset).now();

		AccountGroupEntity DutiesandTaxesAccountGroupEntity1 = new AccountGroupEntity();

		DutiesandTaxesAccountGroupEntity1.setCreatedDate(new Date());
		DutiesandTaxesAccountGroupEntity1.setModifiedDate(new Date());
		DutiesandTaxesAccountGroupEntity1.setGroupName("Duties & Taxes");
		DutiesandTaxesAccountGroupEntity1.setIsPrimary(false);
		DutiesandTaxesAccountGroupEntity1
				.setParent(currentLiabilitiesGroupEntity);
		ofy().save().entity(DutiesandTaxesAccountGroupEntity1).now();

		AccountGroupEntity loansandAdvancesAssetAccountGroupEntity = new AccountGroupEntity();

		loansandAdvancesAssetAccountGroupEntity.setCreatedDate(new Date());
		loansandAdvancesAssetAccountGroupEntity.setModifiedDate(new Date());
		loansandAdvancesAssetAccountGroupEntity
				.setGroupName("Loans & Advances (Asset)");
		loansandAdvancesAssetAccountGroupEntity.setIsPrimary(false);
		loansandAdvancesAssetAccountGroupEntity
				.setParent(currentAssetsGroupEntity);
		ofy().save().entity(loansandAdvancesAssetAccountGroupEntity).now();

		AccountGroupEntity provisionsAccountGroupEntity1 = new AccountGroupEntity();

		provisionsAccountGroupEntity1.setCreatedDate(new Date());
		provisionsAccountGroupEntity1.setModifiedDate(new Date());
		provisionsAccountGroupEntity1.setGroupName("Provisions");
		provisionsAccountGroupEntity1.setIsPrimary(false);
		provisionsAccountGroupEntity1.setParent(currentLiabilitiesGroupEntity);
		ofy().save().entity(provisionsAccountGroupEntity1).now();

		AccountGroupEntity reservesandSurplus = new AccountGroupEntity();

		reservesandSurplus.setCreatedDate(new Date());
		reservesandSurplus.setModifiedDate(new Date());
		reservesandSurplus.setGroupName("Reserves & Surplus");
		reservesandSurplus.setIsPrimary(false);
		reservesandSurplus.setParent(capitalAccountGroupEntity);
		ofy().save().entity(reservesandSurplus).now();

		AccountGroupEntity securedLoans = new AccountGroupEntity();

		securedLoans.setCreatedDate(new Date());
		securedLoans.setModifiedDate(new Date());
		securedLoans.setGroupName("Secured Loans");
		securedLoans.setIsPrimary(false);
		securedLoans.setParent(loansLiabilityGroupEntity);
		ofy().save().entity(securedLoans).now();

		AccountGroupEntity Stockinhand = new AccountGroupEntity();

		Stockinhand.setCreatedDate(new Date());
		Stockinhand.setModifiedDate(new Date());
		Stockinhand.setGroupName("Stock-in-Hand");
		Stockinhand.setIsPrimary(false);
		Stockinhand.setParent(currentAssetsGroupEntity);
		ofy().save().entity(Stockinhand).now();

		AccountGroupEntity sundryCreditors = new AccountGroupEntity();

		sundryCreditors.setCreatedDate(new Date());
		sundryCreditors.setModifiedDate(new Date());
		sundryCreditors.setGroupName("Sundry Creditors");
		sundryCreditors.setIsPrimary(false);
		sundryCreditors.setParent(currentLiabilitiesGroupEntity);
		ofy().save().entity(sundryCreditors).now();

		AccountGroupEntity sundryDebtors = new AccountGroupEntity();

		sundryDebtors.setCreatedDate(new Date());
		sundryDebtors.setModifiedDate(new Date());
		sundryDebtors.setGroupName("Sundry Debtors");
		sundryDebtors.setIsPrimary(false);
		sundryDebtors.setParent(currentLiabilitiesGroupEntity);
		ofy().save().entity(sundryDebtors).now();

		AccountGroupEntity unsecuredLoans = new AccountGroupEntity();

		unsecuredLoans.setCreatedDate(new Date());
		unsecuredLoans.setModifiedDate(new Date());
		unsecuredLoans.setGroupName("Unsecured Loans");
		unsecuredLoans.setIsPrimary(false);
		unsecuredLoans.setParent(loansLiabilityGroupEntity);
		ofy().save().entity(unsecuredLoans).now();

	}

	@ApiMethod(name = "initsetupnext")
	public void initsetupnext() {

		List<BusinessEntity> bizList = ofy().load().type(BusinessEntity.class)
				.list();
		if (bizList.size() > 0) {
			return;
		}

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
		businessEntity.setBusinessName("Protostar Consulting Services");
		businessEntity.setAccounttype(filteredaccount);
		businessEntity.setRegisterDate(sdf.format(date));
		String authorizations = "{\"authorizations\":[{\"authName\":\"More Actions\",\"authorizations\":[{\"authName\":\"updatemyprofile\",\"authorizations\":[]},{\"authName\":\"proadmin\",\"authorizations\":[]},{\"authName\":\"setup\",\"authorizations\":[]}]}]}";
		businessEntity.setAuthorizations(authorizations);

		Address address = new Address();
		businessEntity.setAddress(address);
		address.setLine1("E101, Manaimangal Apt, Behind Sharda Motor, Old Mumbai Highway");
		address.setLine2("Kasarwadi");
		address.setCity("Pune");
		address.setState("MH");
		address.setPin("411034");

		ofy().save().entity(businessEntity).now();
		
		createDefaultDepartments(businessEntity.getId());
		

		UserEntity userEntity = new UserEntity();
		userEntity.setBusiness(businessEntity);
		userEntity.setEmail_id("ganesh.lawande@protostar.co.in");
		userEntity.setFirstName("Ganesh");
		userEntity.setLastName("Lawande");
		userEntity.setIsGoogleUser(true);
		userEntity.setAuthority(Arrays.asList("admin"));
		userEntity.setAuthorizations(authorizations);
		ofy().save().entity(userEntity).now();

		// ------------------------------

		UserEntity userEntity1 = new UserEntity();
		userEntity1.setBusiness(businessEntity);
		userEntity1.setEmail_id("shantanu@protostar.co.in");
		userEntity1.setFirstName("Shantanu");
		userEntity1.setLastName("Lawande");
		userEntity1.setIsGoogleUser(true);
		userEntity1.setAuthority(Arrays.asList("admin"));
		userEntity1.setAuthorizations(authorizations);
		ofy().save().entity(userEntity1).now();

		UserEntity userEntity2 = new UserEntity();
		userEntity2.setBusiness(businessEntity);
		userEntity2.setEmail_id("sagar@protostar.co.in");
		userEntity2.setFirstName("Sagar");
		userEntity2.setLastName("Sadawatre");
		userEntity2.setIsGoogleUser(true);
		userEntity2.setAuthority(Arrays.asList("admin"));
		userEntity2.setAuthorizations(authorizations);
		ofy().save().entity(userEntity2).now();

		UserEntity userEntity3 = new UserEntity();
		userEntity3.setBusiness(businessEntity);
		userEntity3.setEmail_id("deepali@protostar.co.in");
		userEntity3.setFirstName("Deepali");
		userEntity3.setLastName("Mate");
		userEntity3.setIsGoogleUser(true);
		userEntity3.setAuthority(Arrays.asList("admin"));
		userEntity3.setAuthorizations(authorizations);
		ofy().save().entity(userEntity3).now();

		// ///////////////////

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
		department.setName("Workmen");
		department.setBusiness(businessEntity);
		userService.addEmpDepartment(department);
	}

	@ApiMethod(name = "initsetup")
	public void initsetup() {
		// try{

		List<AccountType> accountList = ofy().load().type(AccountType.class)
				.list();
		if (accountList.size() > 0) {
			return;
		}

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
