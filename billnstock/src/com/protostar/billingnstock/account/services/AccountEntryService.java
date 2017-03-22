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
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;
import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.account.entities.AccountEntryEntity;
import com.protostar.billingnstock.taskmangement.TaskEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "accountEntryService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.services", ownerName = "com.protostar.billingnstock.services", packagePath = ""))
public class AccountEntryService {
	int flag = 0;

	@ApiMethod(name = "addAccountEntry")
	public void addAccountEntry(AccountEntryEntity accountEntry) {
		// System.out.println("accountEntry"+accountEntry.getBusiness().getBusinessName());
		ofy().save().entity(accountEntry).now();
	}

	@ApiMethod(name = "getAccountEntryList")
	public List<AccountEntryEntity> getAccountEntryList() {
		List<AccountEntryEntity> list = ofy().load()
				.type(AccountEntryEntity.class).list();
		System.out.println("list credit" + list.get(0).getCredit().toString());
		return list;
	}
	@ApiMethod(name = "getAccountViewEntryByAccountId", path = "getAccountViewEntryByAccountId")
	public List<AccountEntryEntity> getAccountViewEntryByAccountId(
			@Named("actualFromDate") long actualFromDate,
			@Named("actualtoDate") long actualtoDate, @Named("id") Long accId,
			@Named("bid") Long bid) {
	    Date fromDate=new Date(actualFromDate);
		Date toDate=new Date(actualtoDate);
		
		
		System.out.println("actualFromDate:"+actualFromDate);
		System.out.println("actualtoDate:"+actualtoDate);
		System.out.println("id:"+accId);
		System.out.println("bid:"+bid);
		if (accId == null)
			return new ArrayList<AccountEntryEntity>();

		List<AccountEntryEntity> filteredEntries = ofy()
				.load()
				.type(AccountEntryEntity.class)
				.ancestor(Key.create(BusinessEntity.class, bid))
				.filter("accountEntity", Key.create(Key.create(BusinessEntity.class, bid), AccountEntity.class, accId))
				.filter("date >=", fromDate)
				.filter("date <=", toDate).list();
		System.out.println("filteredEntries:"+filteredEntries.size());

		return filteredEntries;
	}@ApiMethod(name = "getAccountEntryByAccountId", path = "getAccountEntryByAccountId")
	public List<AccountEntryEntity> getAccountEntryByAccountId(
			@Named("id") Long AccId) {
		if (AccId == null)
			return new ArrayList<AccountEntryEntity>();
		List<AccountEntryEntity> filteredEntries = new ArrayList<AccountEntryEntity>();

		List<AccountEntryEntity> accountEntries = ofy().load()
				.type(AccountEntryEntity.class).list();
		
		
		
		
		
		for (AccountEntryEntity ss : accountEntries) {
			if (ss.getAccountEntity().getId().equals(AccId)) {
				filteredEntries.add(ss);
			}
		}
		if (filteredEntries.size() > 0) {

			System.out.println("filteredEntries.sie:"
					+ filteredEntries.get(0).getCredit()
					+ "debit"
					+ filteredEntries.get(0).getDebit()
					+ "account type"
					+ filteredEntries.get(0).getAccountEntity()
							.getAccountType());
		} else {
			System.out.println("filteredEntries.sieis empty:");
		}
		return filteredEntries;
	}

	@ApiMethod(name = "getAccountById1")
	public AccountEntryEntity getAccountById1(@Named("bid") Long busId,
			@Named("id") Long accountId) {
		AccountEntryEntity getAccountById1 = ofy()
				.load()
				.key(Key.create(Key.create(BusinessEntity.class, busId),
						AccountEntryEntity.class, accountId)).now();
		return getAccountById1;
	}

}
