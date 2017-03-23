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
				ofy().save().entity(accountEntry).now();
	}
	@ApiMethod(name = "getAccountViewEntryByAccountId", path = "getAccountViewEntryByAccountId")
	public List<AccountEntryEntity> getAccountViewEntryByAccountId(
			@Named("actualFromDate") long actualFromDate,
			@Named("actualtoDate") long actualtoDate, @Named("id") Long accId,
			@Named("bid") Long bid) {
	    Date fromDate=new Date(actualFromDate);
		Date toDate=new Date(actualtoDate);
		if (accId == null)
			return new ArrayList<AccountEntryEntity>();
		List<AccountEntryEntity> filteredEntries = ofy()
				.load()
				.type(AccountEntryEntity.class)
				.ancestor(Key.create(BusinessEntity.class, bid))
				.filter("accountEntity", Key.create(Key.create(BusinessEntity.class, bid), AccountEntity.class, accId))
				.filter("date >=", fromDate)
				.filter("date <=", toDate).list();
				return filteredEntries;
	}
	@ApiMethod(name = "getAccountEntryByAccountId", path = "getAccountEntryByAccountId")
	public List<AccountEntryEntity> getAccountEntryByAccountId(
			@Named("id") Long AccId,@Named("bid") Long bid) {
		if (AccId == null)
			return new ArrayList<AccountEntryEntity>();
		List<AccountEntryEntity> filteredEntries = new ArrayList<AccountEntryEntity>();

		List<AccountEntryEntity> accountEntries = ofy().load()
				.type(AccountEntryEntity.class)
				.ancestor(Key.create(BusinessEntity.class, bid))
				.filter("accountEntity", Key.create(Key.create(BusinessEntity.class, bid), AccountEntity.class, AccId))
				.list();
			return accountEntries;
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