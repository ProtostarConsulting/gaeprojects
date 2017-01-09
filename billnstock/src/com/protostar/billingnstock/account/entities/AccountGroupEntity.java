package com.protostar.billingnstock.account.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Entity
public class AccountGroupEntity extends BaseEntity {
	
	
	/*public static enum AccountGroupType {
		ASSETS, EQUITY, LIABILITIES,INCOME,EXPENSES,OTHERINCOMES,OTHEREXPENCES
	};*/
	@Index
	private String groupName;
	private String description;
	@Index
	private String accountGroupType;
	private Boolean isPrimary ;
	@Index
	private String primaryType;
	@OnSave
	public void beforeSave() {
		super.beforeSave();
		
		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()),
					Constants.AGE_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}
	Ref<AccountGroupEntity> parent;
	private Integer displayOrderNo;
	
	public AccountGroupEntity() {

	}

	
	public Boolean getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary  = isPrimary;
	}

	
	

	public AccountGroupEntity getParent() {
		return parent == null?null:parent.get();
	}

	public void setParent(AccountGroupEntity parent) {
		this.parent =Ref.create(parent);
	}


	public String getAccountGroupType() {
		return accountGroupType;
	}

	public void setAccountGroupType(String accountGroupType) {
		this.accountGroupType = accountGroupType;
	}

	
	public AccountGroupEntity(String groupName, Ref<AccountGroupEntity> parent) {
		this.groupName = groupName;
		this.parent = parent;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*public List<AccountEntity> getAccountList() {		
		List<AccountEntity> accountListTemp = new ArrayList<AccountEntity>();
		for (Ref<AccountEntity> accountEntityRef : this.accountList) {
			accountListTemp.add(accountEntityRef.get());
		}
		return accountListTemp;
	}

	public void setAccountList(List<AccountEntity> accountList) {
		this.accountList = new ArrayList<Ref<AccountEntity>>();
		for (AccountEntity accountEntity : accountList) {
			this.accountList.add(Ref.create(accountEntity));
		}
	}*/

	public Integer getDisplayOrderNo() {
		return displayOrderNo;
	}

	public void setDisplayOrderNo(Integer displayOrderNo) {
		this.displayOrderNo = displayOrderNo;
	}


	public String getPrimaryType() {
		return primaryType;
	}


	public void setPrimaryType(String primaryType) {
		this.primaryType = primaryType;
	}

	/*public AccountGroupEntity getParent() {
		return (parent == null) ? null : parent.get();
	}

	public void setParent(AccountGroupEntity parent) {
		this.parent = Ref.create(parent);
	}*/

}
