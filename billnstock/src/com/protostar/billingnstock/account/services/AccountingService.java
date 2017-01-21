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
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.account.entities.PayableEntity;
import com.protostar.billingnstock.account.entities.ReceivableEntity;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.until.data.Constants.AccountGroupType;
import com.protostar.billnstock.until.data.Constants.AccountingAccountType;
import com.protostar.billnstock.until.data.ServerMsg;

@Api(name = "accountService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.stock.cust.services", ownerName = "com.protostar.billingnstock.stock.cust.services", packagePath = ""))
public class AccountingService {

	@ApiMethod(name = "addAccount")
	public AccountEntity addAccount(AccountEntity accountEntity) {
		ofy().save().entity(accountEntity).now();
		return accountEntity;
	}

	@ApiMethod(name = "checkAccountAlreadyExist")
	public ServerMsg checkAccountAlreadyExist(
			@Named("accountName") String accountName) {
		ServerMsg serverMsg = new ServerMsg();
		List<AccountEntity> list = ofy().load().type(AccountEntity.class)
				.filter("accountName", accountName).list();

		if (list == null || list.size() == 0) {
			serverMsg.setReturnBool(false);
		} else {
			serverMsg.setReturnBool(true);
		}

		return serverMsg;
	}

	@ApiMethod(name = "getAccountList", path = "getAccountList")
	public List<AccountEntity> getAccountList(@Named("id") Long busId) {
		if (busId == null)
			return new ArrayList<AccountEntity>();

		List<AccountEntity> accountList = ofy().load()
				.type(AccountEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return accountList;

	}

	@ApiMethod(name = "updateAccount")
	public AccountEntity updateAccount(AccountEntity update) {
		AccountEntity now = update;
		ofy().save().entity(update).now();
		System.out.println("inside update details now" + now);
		return now;
	}

	@ApiMethod(name = "getAccountListByGroupId", path = "getAccountListByGroupId")
	public List<AccountEntity> getAccountListByGroupId(@Named("id") Long groupId) {

		List<AccountEntity> filteredAccounts = new ArrayList<AccountEntity>();
		List<AccountEntity> accountList = ofy().load()
				.type(AccountEntity.class).list();// ancestor(Key.create(AccountGroupEntity.class,
													// groupId)).list();
		for (AccountEntity ss : accountList) {
			if (ss.getAccountGroup() != null
					&& ss.getAccountGroup().getId().equals(groupId)) {
				filteredAccounts.add(ss);
			}
		}

		return filteredAccounts;
	}

	// *************************************************************************

	@ApiMethod(name = "getAccountBalance", path = "getAccountBalance")
	public ServerMsg getAccountBalance(@Named("id") Long id,
			@Named("bid") Long busId) {
		System.out.println(" acc id" + id);

		Double accBalance = 0.0D;
		Double totalCredit = 0.0D;
		Double totalDebit = 0.0D;

		List<AccountEntryEntity> filteredEntries = new ArrayList<AccountEntryEntity>();

		AccountEntryService accountEntryService = new AccountEntryService();
		filteredEntries = accountEntryService.getAccountEntryByAccountId(id);// ,busId);

		for (AccountEntryEntity entry : filteredEntries) {
			if (entry.getCredit() != null) {
				totalCredit = totalCredit + entry.getCredit();
			} else if (entry.getDebit() != null) {
				totalDebit = totalDebit + entry.getDebit();
			}
		}

		if (!(filteredEntries.isEmpty())) {
			AccountEntryEntity accountEntryEntity = filteredEntries.get(0);

			AccountEntity accountEntity = accountEntryEntity.getAccountEntity();
			AccountGroupEntity accountGroup = accountEntity.getAccountGroup();
			boolean isDebitBalanceAcc = accountGroup.getAccountGroupType() == AccountGroupType.ASSETS
					|| accountGroup.getAccountGroupType() == AccountGroupType.EXPENSES;

			boolean isCreditBalanceAcc = accountGroup.getAccountGroupType() == AccountGroupType.LIABILITIES
					|| accountGroup.getAccountGroupType() == AccountGroupType.INCOME
					|| accountGroup.getAccountGroupType() == AccountGroupType.EQUITY;

			if (isDebitBalanceAcc) {
				if (accountEntity.getContra()) {
					accBalance = totalCredit - totalDebit;
				} else {
					accBalance = totalDebit - totalCredit;
				}
			} else if (isCreditBalanceAcc) {
				if (accountEntity.getContra()) {
					accBalance = totalDebit - totalCredit;
				} else {
					accBalance = totalCredit - totalDebit;
				}
			}
		}
		ServerMsg serverMsg = new ServerMsg();
		serverMsg.setReturnBalance(accBalance);
		return serverMsg;
	}

	@ApiMethod(name = "getAccountById", path = "getAccountById")
	public AccountEntity getAccountById(@Named("id") Long accountId) {
		if (accountId == null)
			new ArrayList<AccountEntity>();

		AccountEntity accountById = ofy().load().type(AccountEntity.class)
				.id(accountId).now();

		return accountById;
	}

	@ApiMethod(name = "deleteaccByid", path = "deleteaccByid")
	public void deleteaccByid(@Named("id") Long accountId) {

		ofy().delete().type(AccountEntity.class).id(accountId).now();

	}

	@ApiMethod(name = "getAllAccountsByBusiness")
	public List<AccountEntity> getAllAccountsByBusiness(@Named("id") Long busId) {

		List<AccountEntity> filteredAccounts = ofy().load()
				.type(AccountEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return filteredAccounts;
	}

	@ApiMethod(name = "getCustomerByID", path = "getCustomerByID")
	public Customer getCustomerByID(@Named("Id") Long Id) {

		Customer customerById = ofy().load().type(Customer.class).id(Id).now();

		return customerById;
	}

	/*
	 * =============================================Account Payable
	 * Methods=================================================
	 */

	@ApiMethod(name = "addPayable")
	public void addPayable(PayableEntity payableEntity) {

		if (payableEntity.getId() == null) {
			payableEntity.setCreatedDate(new Date());
			payableEntity.setModifiedDate(new Date());
		} else {
			payableEntity.setModifiedDate(new Date());
		}

		ofy().save().entity(payableEntity).now();

	}

	@ApiMethod(name = "getAllPayablesByBusiness")
	public List<PayableEntity> getAllPayablesByBusiness(@Named("id") Long busId) {

		List<PayableEntity> filteredPayables = ofy().load()
				.type(PayableEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return filteredPayables;
	}

	@ApiMethod(name = "getPayableByID", path = "getPayableByID")
	public PayableEntity getPayableByID(@Named("id") Long payableId) {

		PayableEntity payableById = ofy().load().type(PayableEntity.class)
				.id(payableId).now();

		return payableById;
	}

	/*
	 * =========================Account Receivable Methods======================
	 */

	@ApiMethod(name = "addReceivable")
	public void addReceivable(ReceivableEntity receivableEntity) {

		if (receivableEntity.getId() == null) {
			receivableEntity.setCreatedDate(new Date());
			receivableEntity.setModifiedDate(new Date());
		} else {
			receivableEntity.setModifiedDate(new Date());
		}
		ofy().save().entity(receivableEntity).now();

	}

	@ApiMethod(name = "getAllReceivablesByBusiness", path = "getAllReceivablesByBusiness")
	public List<ReceivableEntity> getAllReceivablesByBusiness(
			@Named("id") Long busId) {

		List<ReceivableEntity> filteredReceivables = ofy().load()
				.type(ReceivableEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return filteredReceivables;
	}

	@ApiMethod(name = "getReceivableByID", path = "getReceivableByID")
	public ReceivableEntity getReceivableByID(@Named("id") Long receivableId) {

		ReceivableEntity receivableById = ofy().load()
				.type(ReceivableEntity.class).id(receivableId).now();

		return receivableById;
	}
}