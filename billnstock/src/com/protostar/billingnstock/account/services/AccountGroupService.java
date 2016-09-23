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
import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.until.data.ServerMsg;

@Api(name = "accountGroupService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.services", ownerName = "com.protostar.billingnstock.services", packagePath = ""))
public class AccountGroupService {

	@ApiMethod(name = "addAccountGroup")
	public AccountGroupEntity addAccountGroup(AccountGroupEntity accountGroupEntity) {

		if (accountGroupEntity.getId() == null) {
			accountGroupEntity.setCreatedDate(new Date());
			accountGroupEntity.setModifiedDate(new Date());
		} else {
			accountGroupEntity.setModifiedDate(new Date());
		}
		ofy().save().entity(accountGroupEntity).now();
		return accountGroupEntity;
	}
	
	@ApiMethod(name = "getAccountGroupList", path="getAccountGroupList")
	public List<AccountGroupEntity> getAccountGroupList(@Named("id") Long busId) {
		System.out.println("busId:" + busId);
		if(busId == null)
			return new ArrayList<AccountGroupEntity>();
		
		return ofy().load().type(AccountGroupEntity.class).ancestor(Key.create(BusinessEntity.class, busId)).list();
	}
	
	
	
/*	@ApiMethod(name = "getAccountGroupListByType",path="getAccountGroupListByType")
	public List<AccountGroupEntity> getAccountGroupListByType(@Named("type") String type,@Named("bid") Long bid) {
		if(bid == null)
			return new ArrayList<AccountGroupEntity>();
		List<AccountGroupEntity> list= ofy().load().type(AccountGroupEntity.class).ancestor(Key.create(BusinessEntity.class, bid)).list();//.filter("PrimaryType",type).list();
		System.out.println("LIST:******"+list.toString());
		
		
		return list;
	}
	*/
	
	
	@ApiMethod(name = "getAccountGroupListByType", path="getAccountGroupListByType")
	public List<AccountGroupEntity> getAccountGroupListByType(@Named("type") String type, @Named("bid") Long bid) {	
		
		System.out.println("type:******"+type);
		System.out.println("bid:******"+bid);
		List<AccountGroupEntity> filteraccount=new ArrayList<AccountGroupEntity>();
		
		List<AccountGroupEntity> list= ofy().load().type(AccountGroupEntity.class).ancestor(Key.create(BusinessEntity.class, bid)).list();
		
		
		
		for (AccountGroupEntity ss : list) {
			
			if(ss.getIsPrimary()){
				System.out.println("ss.getPrimaryType(): "+ss.getPrimaryType());
				if (ss.getPrimaryType().equals(type)) {
					filteraccount.add(ss);
					System.out.println("ss:******"+ss);
					
					
				}
			} else{
				
			}
		}
		return filteraccount;
		
	}
	
	
	
	@ApiMethod(name = "updateAccountGrp")
	public AccountGroupEntity updateAccountGrp(AccountGroupEntity update) {
		return addAccountGroup(update);
	}
	
	@ApiMethod(name = "deleteAccountGrp")
	 public void deleteAccountGrp(@Named("id") Long id) {

		  ofy().delete().type(AccountGroupEntity.class).id(id).now();

		 
		 }
	

	@ApiMethod(name = "checkAccountGrpAlreadyExist")
	public ServerMsg checkAccountGrpAlreadyExist(@Named("groupName") String groupName) {
		ServerMsg serverMsg = new ServerMsg();
		List<AccountGroupEntity> list = ofy().load().type(AccountGroupEntity.class).filter("groupName",groupName).list();

		
		System.out.println("list ######"+list);
		if (list == null || list.size() == 0)
		{
			System.out.println("list888888888"+list);
			serverMsg.setReturnBool(false);
		} 
		else {
			serverMsg.setReturnBool(true);
		}

		return serverMsg;
	}
	


	@ApiMethod(name = "getAllAccountGroupsByBusiness", path="getAllAccountGroupsByBusiness")
	public List<AccountGroupEntity> getAllAccountGroupsByBusiness(
			@Named("id") Long busId) {

		List<AccountGroupEntity> filteredAccounts = ofy()
				.load()
				.type(AccountGroupEntity.class)
				.filter("business",
						Ref.create(Key.create(BusinessEntity.class, busId)))
				.list();

		return filteredAccounts;
	}

	@ApiMethod(name = "getAccountGroupById")
	public AccountGroupEntity getAccountGroupById(@Named("id") Long accountId) {

		AccountGroupEntity accountById = ofy().load()
				.type(AccountGroupEntity.class).id(accountId).now();

		return accountById;
	}


	public void createDefaltAccountGroups() {
		
		//check if not there then only add esle return;
		
		List<AccountGroupEntity> accountGroupEntities = new ArrayList<AccountGroupEntity>(10);  
		accountGroupEntities.add(new AccountGroupEntity("Bank Accounts", null));
		accountGroupEntities.add(new AccountGroupEntity("Capital Accounts", null));
		accountGroupEntities.add(new AccountGroupEntity("Current Assests", null));
		accountGroupEntities.add(new AccountGroupEntity("Direct Incomes", null));
		accountGroupEntities.add(new AccountGroupEntity("Direct Expesnes", null));
		accountGroupEntities.add(new AccountGroupEntity("Current Liabilities", null));
		
		//this.addAccountGroups(accountGroupEntities);
		
		

	}

}