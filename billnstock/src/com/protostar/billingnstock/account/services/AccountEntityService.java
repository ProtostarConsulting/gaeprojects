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
import com.protostar.billingnstock.account.entities.AccountEntryEntity;
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
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

	
	@ApiMethod(name = "getAccountById1")
	public AccountEntity getAccountById1(@Named("bid") Long busId,@Named("id") Long accountId) {
		
		AccountEntity getAccountById1 = ofy().load()
				.key(Key.create(Key.create(BusinessEntity.class, busId), AccountEntity.class, accountId)).now();
			return getAccountById1;
	}

}