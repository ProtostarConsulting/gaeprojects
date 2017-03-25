package com.protostar.billingnstock.account.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.account.entities.AccountEntryEntity;
import com.protostar.billingnstock.account.entities.CurrentFinancialYear;
import com.protostar.billingnstock.account.entities.GeneralEntryEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "accountEntryService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.services", ownerName = "com.protostar.billingnstock.services", packagePath = ""))
public class AccountEntryService {
	int flag = 0;

	@ApiMethod(name = "addAccountEntry")
	public void addAccountEntry(AccountEntryEntity accountEntry) {
		ofy().save().entity(accountEntry).now();
	}

	@ApiMethod(name = "getAccountViewEntryByAccountId", path = "getAccountViewEntryByAccountId")
	public List<AccountEntryEntity> getAccountViewEntryByAccountId(@Named("actualFromDate") long actualFromDate,
			@Named("actualtoDate") long actualtoDate, @Named("id") Long accId, @Named("bid") Long bid) {
		Date fromDate = new Date(actualFromDate);
		Date toDate = new Date(actualtoDate);
		if (accId == null)
			return new ArrayList<AccountEntryEntity>();

		AccountingService accountingService = new AccountingService();
		CurrentFinancialYear currentFinancialYear = accountingService.getCurrentFinancialYear(bid);

		List<AccountEntryEntity> filteredEntries = ofy().load().type(AccountEntryEntity.class)
				.ancestor(Key.create(BusinessEntity.class, bid))
				.filter("accountEntity", Key.create(Key.create(BusinessEntity.class, bid), AccountEntity.class, accId))
				.filter("date >=", fromDate).filter("date <=", toDate)
				.filter("createdDate >=", currentFinancialYear.getFromDate())
				.filter("createdDate <=", currentFinancialYear.getToDate()).list();
		return filteredEntries;
	}

	@ApiMethod(name = "getAccountEntryByAccountId", path = "getAccountEntryByAccountId")
	public List<AccountEntryEntity> getAccountEntryByAccountId(@Named("id") Long AccId, @Named("bid") Long bid) {
		if (AccId == null)
			return new ArrayList<AccountEntryEntity>();

		AccountingService accountingService = new AccountingService();
		CurrentFinancialYear currentFinancialYear = accountingService.getCurrentFinancialYear(bid);
System.out.println("getAccountEntryByAccountId--id"+bid);
		List<AccountEntryEntity> accountEntries = ofy().load().type(AccountEntryEntity.class)
				.ancestor(Key.create(BusinessEntity.class, bid))
				.filter("accountEntity", Key.create(Key.create(BusinessEntity.class, bid), AccountEntity.class, AccId))
				.filter("createdDate >=", currentFinancialYear.getFromDate())
				.filter("createdDate <=", currentFinancialYear.getToDate()).list();
		return accountEntries;
	}

	@ApiMethod(name = "getAccountById1")
	public AccountEntryEntity getAccountById1(@Named("bid") Long busId, @Named("id") Long accountId) {
		AccountEntryEntity getAccountById1 = ofy().load()
				.key(Key.create(Key.create(BusinessEntity.class, busId), AccountEntryEntity.class, accountId)).now();
		return getAccountById1;
	}
	@ApiMethod(name = "addGeneralEntry")
	public void addGeneralEntry(GeneralEntryEntity entryEntity) {

		AccountEntryEntity debitAcc = new AccountEntryEntity();
		debitAcc.setDate(new Date());
		debitAcc.setNarration(entryEntity.getNarration());
		debitAcc.setDebit(entryEntity.getAmount());
		debitAcc.setAccountEntity(entryEntity.getDebitAccount());
		debitAcc.setCreatedDate(new Date());
		debitAcc.setModifiedBy(entryEntity.getModifiedBy());
		debitAcc.setBusiness(entryEntity.getBusiness());
		AccountEntryService aes = new AccountEntryService();
		aes.addAccountEntry(debitAcc);

		AccountEntryEntity creditAcc = new AccountEntryEntity();
		creditAcc.setDate(new Date());
		creditAcc.setNarration(entryEntity.getNarration());
		creditAcc.setCredit(entryEntity.getAmount());
		creditAcc.setAccountEntity(entryEntity.getCreditAccount());
		creditAcc.setCreatedDate(new Date());
		creditAcc.setModifiedBy(entryEntity.getModifiedBy());
		creditAcc.setBusiness(entryEntity.getBusiness());
		aes.addAccountEntry(creditAcc);
		entryEntity.setCreatedDate(new Date());
		entryEntity.setModifiedDate(new Date());
		ofy().save().entity(entryEntity).now();
	}
}