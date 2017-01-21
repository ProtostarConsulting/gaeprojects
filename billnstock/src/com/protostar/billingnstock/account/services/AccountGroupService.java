package com.protostar.billingnstock.account.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
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
import com.protostar.billnstock.until.data.Constants.AccountGroupType;
import com.protostar.billnstock.until.data.ServerMsg;

@Api(name = "accountGroupService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.services", ownerName = "com.protostar.billingnstock.services", packagePath = ""))
public class AccountGroupService {

	String accountGroupTypeList[] = { "Assets", "EQUITY", "Liabilities",
			"Incomes", "Expenses", "OTHERINCOMES", "OTHEREXPENCES" };

	@ApiMethod(name = "addAccountGroup")
	public AccountGroupEntity addAccountGroup(
			AccountGroupEntity accountGroupEntity) {

		if (accountGroupEntity.getId() == null) {
			accountGroupEntity.setCreatedDate(new Date());
			accountGroupEntity.setModifiedDate(new Date());
		} else {
			accountGroupEntity.setModifiedDate(new Date());
		}
		ofy().save().entity(accountGroupEntity).now();
		return accountGroupEntity;
	}

	@ApiMethod(name = "getAccountGroupList", path = "getAccountGroupList")
	public List<AccountGroupEntity> getAccountGroupList(@Named("id") Long busId) {
		System.out.println("busId:4444" + busId);
		if (busId == null)
			return new ArrayList<AccountGroupEntity>();
		List<AccountGroupEntity> list = ofy().load()
				.type(AccountGroupEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		System.out.println("list:" + list);
		return list;
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
	public ServerMsg checkAccountGrpAlreadyExist(
			@Named("groupName") String groupName) {
		ServerMsg serverMsg = new ServerMsg();
		List<AccountGroupEntity> list = ofy().load()
				.type(AccountGroupEntity.class).filter("groupName", groupName)
				.list();

		System.out.println("list ######" + list);
		if (list == null || list.size() == 0) {
			System.out.println("list888888888" + list);
			serverMsg.setReturnBool(false);
		} else {
			serverMsg.setReturnBool(true);
		}

		return serverMsg;
	}

	@ApiMethod(name = "getAllAccountGroupsByBusiness", path = "getAllAccountGroupsByBusiness")
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

	@ApiMethod(name = "getAllBusiness")
	public List<BusinessEntity> getAllBusnes() {

		List<BusinessEntity> businessList = ofy().load()
				.type(BusinessEntity.class).list();

		return businessList;
	}

	@ApiMethod(name = "getAccountGroupById")
	public AccountGroupEntity getAccountGroupById(@Named("id") Long accountId) {

		AccountGroupEntity accountById = ofy().load()
				.type(AccountGroupEntity.class).id(accountId).now();

		return accountById;
	}

	public void createDefaltAccountGroups() {

		List<AccountGroupEntity> accountGroupEntities = new ArrayList<AccountGroupEntity>(
				10);
		accountGroupEntities.add(new AccountGroupEntity("Bank Accounts", null));
		accountGroupEntities.add(new AccountGroupEntity("Capital Accounts",
				null));
		accountGroupEntities
				.add(new AccountGroupEntity("Current Assests", null));
		accountGroupEntities
				.add(new AccountGroupEntity("Direct Incomes", null));
		accountGroupEntities
				.add(new AccountGroupEntity("Direct Expesnes", null));
		accountGroupEntities.add(new AccountGroupEntity("Current Liabilities",
				null));
	}

	@ApiMethod(name = "getAccountGroupListByType", path = "getAccountGroupListByType")
	public List<AccountGroupEntity> getAccountGroupListByType(
			@Named("type") String type, @Named("bid") Long bid) {

		List<AccountGroupEntity> filteraccount = new ArrayList<AccountGroupEntity>();

		List<AccountGroupEntity> list = ofy().load()
				.type(AccountGroupEntity.class)
				.ancestor(Key.create(BusinessEntity.class, bid)).list();

		for (AccountGroupEntity ss : list) {

			if (ss.getIsPrimary() && ss.getAccountGroupType() != null) {
				if (ss.getAccountGroupType().toString().equalsIgnoreCase(type)) {
					filteraccount.add(ss);
				}
			} else {
				if (ss.getParent() != null
						&& ss.getParent().getAccountGroupType() != null
						&& ss.getParent().getAccountGroupType().toString()
								.trim().equalsIgnoreCase(type)) {
					filteraccount.add(ss);
				}

			}
		}

		return filteraccount;

	}

	@ApiMethod(name = "getBalanceSheet", path = "getBalanceSheet")
	public List<TypeInfo> getBalanceSheet(@Named("bid") Long bid) {
		List<TypeInfo> typeList = new ArrayList<TypeInfo>();
		AccountGroupType[] groupTypes = AccountGroupType.values();
		for (int i = 0; i < groupTypes.length; i++) {// get type
			TypeInfo typeInfo = new TypeInfo();
			typeInfo.typeName = groupTypes[i].toString();
			typeInfo.groupList = new ArrayList<GroupInfo>();

			List<AccountGroupEntity> typeAccountList = getAccountGroupListByType(
					typeInfo.typeName, bid);

			double typeTotal = 0;
			for (int j = 0; j < typeAccountList.size(); j++) {
				GroupInfo groupInfo = new GroupInfo();
				groupInfo.groupName = typeAccountList.get(j).getGroupName();
				AccountingService as = new AccountingService();
				List<AccountEntity> accList = as
						.getAccountListByGroupId(typeAccountList.get(j).getId());
				double groupTotal = 0;
				for (AccountEntity accountEntity : accList) {
					System.out.println("getAccountName:"
							+ accountEntity.getAccountName());
					ServerMsg accountBalance = as.getAccountBalance(
							accountEntity.getId(), bid);
					groupTotal += accountBalance.getReturnBalance();					
				}

				if (groupTotal != 0) {
					groupInfo.groupBalance = groupTotal;
					typeTotal += groupTotal;
					typeInfo.groupList.add(groupInfo);
				}

			}

			
			typeInfo.typeBalance = typeTotal;
			typeList.add(typeInfo);

		}

		return typeList;
	}

	@ApiMethod(name = "getBalanceSheetCalculation", path = "getBalanceSheetCalculation")
	public BalanceSheetData getBalanceSheetCalculation() {
		BalanceSheetData BalanceSheetCalculation = new BalanceSheetData();
		List<TypeInfo> list = new ArrayList<TypeInfo>();// global value

		for (int int2 = 0; int2 < list.size(); int2++) {
			if ((list.get(int2).getTypeName().toString() == "Assets")
					&& (list.get(int2).getGroupList() != null)) {
				for (int i = 0; i < list.get(int2).getGroupList().size(); i++) {
					BalanceSheetCalculation.totalAsset = list.get(int2)
							.getGroupList().get(i).getGroupBalance()
							+ BalanceSheetCalculation.totalAsset;
				}
				if (BalanceSheetCalculation.totalAsset < 0) {
					BalanceSheetCalculation.totalAsset = BalanceSheetCalculation.totalAsset
							* (-1);
				}
			}

			if ((list.get(int2).getTypeName().toString() == "Liabilities")
					&& (list.get(int2).getGroupList() != null)) {
				for (int i = 0; i < list.get(int2).getGroupList().size(); i++) {
					BalanceSheetCalculation.totalLiabilities = list.get(int2)
							.getGroupList().get(i).getGroupBalance()
							+ BalanceSheetCalculation.totalLiabilities;
				}

				if (BalanceSheetCalculation.totalLiabilities < 0) {
					BalanceSheetCalculation.totalLiabilities = BalanceSheetCalculation.totalLiabilities
							* (-1);
				}

			}
			if ((list.get(int2).getTypeName().toString() == "EQUITY")
					&& (list.get(int2).getGroupList() != null)) {
				for (int i = 0; i < list.get(int2).getGroupList().size(); i++) {
					BalanceSheetCalculation.totalEQUITY = list.get(int2)
							.getGroupList().get(i).getGroupBalance()
							+ BalanceSheetCalculation.totalEQUITY;
				}

			}
		}
		BalanceSheetCalculation.totalLiabilities2 = BalanceSheetCalculation.totalLiabilities
				+ BalanceSheetCalculation.totalEQUITY;

		return BalanceSheetCalculation;
	}

	@ApiMethod(name = "getChartSheet", path = "getChartSheet")
	public List<TypeInfo> getChartSheet(@Named("bid") Long bid) {

		List<TypeInfo> typeList = new ArrayList<TypeInfo>();
		for (int i = 0; i < accountGroupTypeList.length; i++) {// get type
			TypeInfo typeInfo = new TypeInfo();

			typeInfo.typeName = accountGroupTypeList[i];
			typeInfo.groupList = new ArrayList<GroupInfo>();

			List<AccountGroupEntity> typeAccountList = getAccountGroupListByType(
					typeInfo.typeName, bid);

			for (int j = 0; j < typeAccountList.size(); j++) {
				GroupInfo groupInfo = new GroupInfo();
				groupInfo.groupName = typeAccountList.get(j).getGroupName();
				groupInfo.AccInfoList = new ArrayList<AccInfo>();
				AccountingService as = new AccountingService();
				List<AccountEntity> accList = as
						.getAccountListByGroupId(typeAccountList.get(j).getId());
				double groupTotal = 0;
				for (AccountEntity accountEntity : accList) {
					ServerMsg accountBalance = as.getAccountBalance(
							accountEntity.getId(), bid);
					AccInfo accinfo = new AccInfo();
					accinfo.accName = accountEntity.getAccountName();
					accinfo.accBalance = accountBalance.getReturnBalance();
					groupInfo.AccInfoList.add(accinfo);

				}
				typeInfo.groupList.add(groupInfo);

				/*
				 * if (groupTotal != 0) { groupInfo.groupBalance = groupTotal;
				 * typeTotal += groupTotal; typeInfo.groupList.add(groupInfo); }
				 */
			}

			typeList.add(typeInfo);
		}

		// System.out.println("typeList:******" +
		// typeList.get(0).getGroupList().get(0).getAccInfoList().);

		return typeList;
	}

	public class TypeInfo implements Serializable {
		String typeName;
		double typeBalance;
		List<GroupInfo> groupList;

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public double getTypeBalance() {
			return typeBalance;
		}

		public void setTypeBalance(double typeBalance) {
			this.typeBalance = typeBalance;
		}

		public List<GroupInfo> getGroupList() {
			return groupList;
		}

		public void setGroupList(List<GroupInfo> groupList) {
			this.groupList = groupList;
		}
	}

	public class GroupInfo implements Serializable {
		String groupName;
		double groupBalance;
		List<AccInfo> AccInfoList;

		public String getGroupName() {
			return groupName;
		}

		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}

		public double getGroupBalance() {
			return groupBalance;
		}

		public void setGroupBalance(double groupBalance) {
			this.groupBalance = groupBalance;
		}

		public List<AccInfo> getAccInfoList() {
			return AccInfoList;
		}

		public void setAccInfoList(List<AccInfo> accInfoList) {
			AccInfoList = accInfoList;
		}
	}

	public class AccInfo implements Serializable {
		String accName;
		double accBalance;

		public String getAccName() {
			return accName;
		}

		public void setAccName(String accName) {
			this.accName = accName;
		}

		public double getAccBalance() {
			return accBalance;
		}

		public void setAccBalance(double accBalance) {
			this.accBalance = accBalance;
		}

	}

	public class BalanceSheetData implements Serializable {

		double totalAsset = 0;
		double totalLiabilities2 = 0;
		double totalEQUITY = 0;
		double totalLiabilities = 0;

		public double getTotalAsset() {
			return totalAsset;
		}

		public void setTotalAsset(double totalAsset) {
			this.totalAsset = totalAsset;
		}

		public double getTotalLiabilities2() {
			return totalLiabilities2;
		}

		public void setTotalLiabilities2(double totalLiabilities2) {
			this.totalLiabilities2 = totalLiabilities2;
		}

		public double getTotalEQUITY() {
			return totalEQUITY;
		}

		public void setTotalEQUITY(double totalEQUITY) {
			this.totalEQUITY = totalEQUITY;
		}

		public double getTotalLiabilities() {
			return totalLiabilities;
		}

		public void setTotalLiabilities(double totalLiabilities) {
			this.totalLiabilities = totalLiabilities;
		}

	}

}
