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
import com.googlecode.objectify.LoadResult;
import com.protostar.billingnstock.account.entities.AccountEntryEntity;
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
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

	private AccountGroupEntity getTemoAccountGroupEntity(BusinessEntity business) {

		AccountGroupEntity acGroupEntity = new AccountGroupEntity();
		acGroupEntity.setBusiness(business);
		acGroupEntity.setCreatedDate(new Date());
		acGroupEntity.setModifiedDate(new Date());
		return acGroupEntity;
	}

	@ApiMethod(name = "creatAccountAndGroup")
	public void creatAccountAndGroup(BusinessEntity forBiz) {

		List<AccountGroupEntity> acList = ofy().load()
				.type(AccountGroupEntity.class).list();

		if (acList.size() > 0)
			return;

		BusinessEntity business = null;
		if (forBiz == null || forBiz.getId() == null) {
			List<BusinessEntity> bizList = ofy().load()
					.type(BusinessEntity.class).list();
			business = bizList.get(0);
		} else {
			business = forBiz;
		}

		AccountGroupEntity acGroupEntity = getTemoAccountGroupEntity(business);

		acGroupEntity.setGroupName("Fixed Assets");
		acGroupEntity.setIsPrimary(true);
		acGroupEntity.setPrimaryType("Assets");
		ofy().save().entity(acGroupEntity).now();

		AccountGroupEntity suspenseAcGroupEntity = getTemoAccountGroupEntity(business);

		suspenseAcGroupEntity.setGroupName("Suspense A/c");
		suspenseAcGroupEntity.setIsPrimary(true);
		suspenseAcGroupEntity.setPrimaryType("Liabilities");
		ofy().save().entity(suspenseAcGroupEntity).now();

		AccountGroupEntity salesAccountsGroupEntity = getTemoAccountGroupEntity(business);

		salesAccountsGroupEntity.setGroupName("Sales Accounts");
		salesAccountsGroupEntity.setIsPrimary(true);
		salesAccountsGroupEntity.setPrimaryType("Income");
		ofy().save().entity(salesAccountsGroupEntity).now();

		AccountGroupEntity purchaseAccountsGroupEntity = getTemoAccountGroupEntity(business);

		purchaseAccountsGroupEntity.setGroupName("Purchase Accounts");
		purchaseAccountsGroupEntity.setIsPrimary(true);
		purchaseAccountsGroupEntity.setPrimaryType("Expenses");
		ofy().save().entity(purchaseAccountsGroupEntity).now();

		AccountGroupEntity miscExpensesASSETGroupEntity = getTemoAccountGroupEntity(business);

		miscExpensesASSETGroupEntity.setGroupName(" Misc.Expenses ASSET");
		miscExpensesASSETGroupEntity.setIsPrimary(true);
		miscExpensesASSETGroupEntity.setPrimaryType("Assets");
		ofy().save().entity(miscExpensesASSETGroupEntity).now();

		AccountGroupEntity loansLiabilityGroupEntity = getTemoAccountGroupEntity(business);

		loansLiabilityGroupEntity.setGroupName("Loans (Liability)");
		loansLiabilityGroupEntity.setIsPrimary(true);
		loansLiabilityGroupEntity.setPrimaryType("Liabilities");
		ofy().save().entity(loansLiabilityGroupEntity).now();

		AccountGroupEntity investmentsGroupEntity = getTemoAccountGroupEntity(business);

		investmentsGroupEntity.setGroupName("Investments");
		investmentsGroupEntity.setIsPrimary(true);
		investmentsGroupEntity.setPrimaryType("Assets");
		ofy().save().entity(investmentsGroupEntity).now();

		AccountGroupEntity indirectIncomesGroupEntity = getTemoAccountGroupEntity(business);

		indirectIncomesGroupEntity.setGroupName("Indirect Incomes");
		indirectIncomesGroupEntity.setIsPrimary(true);
		indirectIncomesGroupEntity.setPrimaryType("Incomes");
		ofy().save().entity(indirectIncomesGroupEntity).now();

		AccountGroupEntity indirectExpensesGroupEntity = getTemoAccountGroupEntity(business);
		

		indirectExpensesGroupEntity.setGroupName("Indirect Expenses");
		indirectExpensesGroupEntity.setIsPrimary(true);
		indirectExpensesGroupEntity.setPrimaryType("Expenses");
		ofy().save().entity(indirectExpensesGroupEntity).now();

		AccountGroupEntity directIncomesGroupEntity = getTemoAccountGroupEntity(business);

		directIncomesGroupEntity.setGroupName("Direct Incomes");
		directIncomesGroupEntity.setIsPrimary(true);
		directIncomesGroupEntity.setPrimaryType("Incomes");
		ofy().save().entity(directIncomesGroupEntity).now();

		AccountGroupEntity directExpensesGroupEntity = getTemoAccountGroupEntity(business);

		directExpensesGroupEntity.setGroupName("Direct Expenses");
		directExpensesGroupEntity.setIsPrimary(true);
		directExpensesGroupEntity.setPrimaryType("Expenses");
		ofy().save().entity(directExpensesGroupEntity).now();

		AccountGroupEntity currentLiabilitiesGroupEntity = getTemoAccountGroupEntity(business);

		currentLiabilitiesGroupEntity.setGroupName("Current Liabilities");
		currentLiabilitiesGroupEntity.setIsPrimary(true);
		currentLiabilitiesGroupEntity.setPrimaryType("Liabilities");
		ofy().save().entity(currentLiabilitiesGroupEntity).now();

		AccountGroupEntity currentAssetsGroupEntity = getTemoAccountGroupEntity(business);

		currentAssetsGroupEntity.setGroupName("Current Assets");
		currentAssetsGroupEntity.setIsPrimary(true);
		currentAssetsGroupEntity.setPrimaryType("Assets");
		ofy().save().entity(currentAssetsGroupEntity).now();

		AccountGroupEntity capitalAccountGroupEntity = getTemoAccountGroupEntity(business);

		capitalAccountGroupEntity.setGroupName("Capital Account");
		capitalAccountGroupEntity.setIsPrimary(true);
		capitalAccountGroupEntity.setPrimaryType("Liabilities");
		ofy().save().entity(capitalAccountGroupEntity).now();

		AccountGroupEntity branchDivisionsGroupEntity = getTemoAccountGroupEntity(business);

		branchDivisionsGroupEntity.setGroupName(" Branch / Divisions");
		branchDivisionsGroupEntity.setIsPrimary(true);
		branchDivisionsGroupEntity.setPrimaryType("Liabilities");
		ofy().save().entity(branchDivisionsGroupEntity).now();

		// ***********************************SUB
		// GROUP***********************************

		AccountGroupEntity bankAccountsAccountGroupEntity = getTemoAccountGroupEntity(business);

		bankAccountsAccountGroupEntity.setGroupName("Bank Accounts");
		bankAccountsAccountGroupEntity.setIsPrimary(false);
		bankAccountsAccountGroupEntity.setParent(currentAssetsGroupEntity);
		ofy().save().entity(bankAccountsAccountGroupEntity).now();

		AccountGroupEntity bankODA_c = getTemoAccountGroupEntity(business);
		bankODA_c.setGroupName("Bank OD A/c");
		bankODA_c.setIsPrimary(false);
		bankODA_c.setParent(loansLiabilityGroupEntity);
		ofy().save().entity(bankODA_c).now();

		AccountGroupEntity Cashinhand = getTemoAccountGroupEntity(business);
		Cashinhand.setGroupName("Cash-in-hand");
		Cashinhand.setIsPrimary(false);
		Cashinhand.setParent(currentAssetsGroupEntity);
		ofy().save().entity(Cashinhand).now();

		AccountGroupEntity depositsAsset = getTemoAccountGroupEntity(business);

		depositsAsset.setGroupName("Deposits (Asset)");
		depositsAsset.setIsPrimary(false);
		depositsAsset.setParent(currentAssetsGroupEntity);
		ofy().save().entity(depositsAsset).now();

		AccountGroupEntity DutiesandTaxesAccountGroupEntity1 = getTemoAccountGroupEntity(business);

		DutiesandTaxesAccountGroupEntity1.setGroupName("Duties & Taxes");
		DutiesandTaxesAccountGroupEntity1.setIsPrimary(false);
		DutiesandTaxesAccountGroupEntity1.setParent(currentLiabilitiesGroupEntity);
		ofy().save().entity(DutiesandTaxesAccountGroupEntity1).now();

		AccountGroupEntity loansandAdvancesAssetAccountGroupEntity = getTemoAccountGroupEntity(business);

		loansandAdvancesAssetAccountGroupEntity
				.setGroupName("Loans & Advances (Asset)");
		loansandAdvancesAssetAccountGroupEntity.setIsPrimary(false);
		loansandAdvancesAssetAccountGroupEntity
				.setParent(currentAssetsGroupEntity);
		ofy().save().entity(loansandAdvancesAssetAccountGroupEntity).now();

		AccountGroupEntity provisionsAccountGroupEntity1 = getTemoAccountGroupEntity(business);

		provisionsAccountGroupEntity1.setGroupName("Provisions");
		provisionsAccountGroupEntity1.setIsPrimary(false);
		provisionsAccountGroupEntity1.setParent(currentLiabilitiesGroupEntity);
		ofy().save().entity(provisionsAccountGroupEntity1).now();

		AccountGroupEntity reservesandSurplus = getTemoAccountGroupEntity(business);

		reservesandSurplus.setGroupName("Reserves & Surplus");
		reservesandSurplus.setIsPrimary(false);
		reservesandSurplus.setParent(capitalAccountGroupEntity);
		ofy().save().entity(reservesandSurplus).now();

		AccountGroupEntity securedLoans = getTemoAccountGroupEntity(business);

		securedLoans.setGroupName("Secured Loans");
		securedLoans.setIsPrimary(false);
		securedLoans.setParent(loansLiabilityGroupEntity);
		ofy().save().entity(securedLoans).now();

		AccountGroupEntity Stockinhand = getTemoAccountGroupEntity(business);

		Stockinhand.setGroupName("Stock-in-Hand");
		Stockinhand.setIsPrimary(false);
		Stockinhand.setParent(currentAssetsGroupEntity);
		ofy().save().entity(Stockinhand).now();

		AccountGroupEntity sundryCreditors = getTemoAccountGroupEntity(business);

		sundryCreditors.setGroupName("Sundry Creditors");
		sundryCreditors.setIsPrimary(false);
		sundryCreditors.setParent(currentLiabilitiesGroupEntity);
		ofy().save().entity(sundryCreditors).now();

		AccountGroupEntity sundryDebtors = getTemoAccountGroupEntity(business);

		sundryDebtors.setGroupName("Sundry Debtors");
		sundryDebtors.setIsPrimary(false);
		sundryDebtors.setParent(currentLiabilitiesGroupEntity);
		ofy().save().entity(sundryDebtors).now();

		AccountGroupEntity unsecuredLoans = getTemoAccountGroupEntity(business);

		unsecuredLoans.setGroupName("Unsecured Loans");
		unsecuredLoans.setIsPrimary(false);
		unsecuredLoans.setParent(loansLiabilityGroupEntity);
		ofy().save().entity(unsecuredLoans).now();

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
		List<BusinessEntity> bizList = ofy().load().type(BusinessEntity.class)
				.list();

		if (bizList.size() > 0)
			return;

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
		userEntity2.setEmail_id("sagar@protostar.co.in");
		userEntity2.setFirstName("Sagar");
		userEntity2.setLastName("sadawatre");
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

		List<AccountType> actList = ofy().load().type(AccountType.class).list();

		if (actList.size() > 0)
			return;

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
