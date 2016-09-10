package com.protostar.billingnstock.account.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.account.entities.AccountEntryEntity;
import com.protostar.billingnstock.account.entities.GeneralJournalEntity;

@Api(name = "accountEntryService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.services", ownerName = "com.protostar.billingnstock.services", packagePath = ""))
public class AccountEntryService {
	@ApiMethod(name = "addAccountEntry")
	public void addAccountEntry(AccountEntryEntity accountEntry) {

		ofy().save().entity(accountEntry).now();

	}
	
	
	@ApiMethod(name = "getAccountEntryList")
	public List<AccountEntryEntity> getAccountEntryList() {
		return ofy().load().type(AccountEntryEntity.class).list();
	}
	
	
	
	
	@ApiMethod(name = "getAccountEntryByAccountId", path = "getAccountEntryByAccountId")
	public List<AccountEntryEntity> getAccountEntryByAccountId(@Named("id") Long AccId) {
		
		List<AccountEntryEntity> filteredEntries = new ArrayList<AccountEntryEntity>();
		List<AccountEntryEntity> accountEntries = ofy().load().type(AccountEntryEntity.class).list();

		for (AccountEntryEntity ss : accountEntries) {
			if (ss.getAccountEntity().getId().equals(AccId)) {
				filteredEntries.add(ss);
			}
			
		}
		System.out.println("filteredEntries.sie:" + filteredEntries.size());
		return filteredEntries;
	}
	
	

}
