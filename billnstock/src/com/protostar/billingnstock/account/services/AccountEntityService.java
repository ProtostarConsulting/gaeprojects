package com.protostar.billingnstock.account.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Api(name = "accountEntityService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.services", ownerName = "com.protostar.billingnstock.services", packagePath = ""))
public class AccountEntityService {

	@ApiMethod(name = "addAccount")
	public void addAccount(AccountEntity accountEntity) {

		if (accountEntity.getId() == null) {
			accountEntity.setCreatedDate(new Date());
			accountEntity.setModifiedDate(new Date());
		} else {
			accountEntity.setModifiedDate(new Date());
		}
		
		ofy().save().entity(accountEntity).now();

	}

	/*
	 * @ApiMethod(name = "addAccounts") public void
	 * addAccounts(List<AccountEntity> accountEntities) { for (AccountEntity
	 * accountEntity : accountEntities) { addAccount(accountEntity); }
	 * 
	 * }
	 */

	@ApiMethod(name = "getAllAccountsByBusiness")
	public List<AccountEntity> getAllAccountsByBusiness(@Named("id") Long busId) {

		List<AccountEntity> filteredAccounts = ofy()
				.load()
				.type(AccountEntity.class)
				.filter("business",
						Ref.create(Key.create(BusinessEntity.class, busId)))
				.list();

		return filteredAccounts;
	}

	@ApiMethod(name = "getAccountById")
	public AccountEntity getAccountById(@Named("id") Long accountId) {

		AccountEntity accountById = ofy().load().type(AccountEntity.class)
				.id(accountId).now();

		return accountById;
	}

	/*
	 * public void createDefaltAccounts() {
	 * 
	 * // check if not there then only add esle return;
	 * 
	 * List<AccountEntity> accountEntities = new ArrayList<AccountEntity>(10);
	 * accountEntities.add(new AccountEntity("ICIC Bank Current A/c",
	 * AccountEntity.accountType.PERSONAL)); // Assets Group
	 * accountEntities.add(new AccountEntity("ICIC Bank Personal A/c",
	 * AccountEntity.accountType.PERSONAL)); // Assets Group
	 * accountEntities.add(new AccountEntity("Capital A/c",
	 * AccountEntity.accountType.PERSONAL)); // Capital/Equity Group
	 * accountEntities.add(new AccountEntity("Cash A/c",
	 * AccountEntity.accountType.PERSONAL)); // Assets Group
	 * accountEntities.add(new AccountEntity("Computers A/c",
	 * AccountEntity.accountType.REAL)); // Assets Group accountEntities.add(new
	 * AccountEntity("Furniture A/c", AccountEntity.accountType.REAL)); //
	 * Assets Group accountEntities.add(new AccountEntity("Salary A/c",
	 * AccountEntity.accountType.NOMINAL)); // Expense Group
	 * accountEntities.add(new AccountEntity("Rent A/c",
	 * AccountEntity.accountType.NOMINAL)); // Expense Group
	 * accountEntities.add(new AccountEntity("Utility Expesnes A/c",
	 * AccountEntity.accountType.NOMINAL)); // Expense Group
	 * accountEntities.add(new AccountEntity("Payables A/c",
	 * AccountEntity.accountType.NOMINAL)); // Liabilities Group
	 * accountEntities.add(new AccountEntity("HDFC Biz Loan (Feb-2016) A/c",
	 * AccountEntity.accountType.NOMINAL)); // Liabilities Group
	 * 
	 * 
	 * //this.addAccounts(accountEntities);
	 * 
	 * }
	 */

}