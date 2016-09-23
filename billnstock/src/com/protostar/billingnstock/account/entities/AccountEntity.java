package com.protostar.billingnstock.account.entities;

import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Ignore;
import com.protostar.billnstock.entity.BaseEntity;


@Entity
public class AccountEntity extends BaseEntity {

/*	public static enum AccountType {
		PERSONAL, REAL, NOMINAL
	};
*/ 

	@Index
	private String accountName;
//	private AccountType accountType;
	//private Long accountNo;
	private String accountNo;
	private String description;
	private Integer displayOrderNo;
	private Boolean contra = false;
	private String accountType;
	
		
	Ref<AccountGroupEntity> accountgroup;
	

	public AccountGroupEntity getaccountgroup() {
		return accountgroup == null?null:accountgroup.get();
	}

	public void setaccountgroup(AccountGroupEntity accountgroup) {
		this.accountgroup =Ref.create(accountgroup);
	}

	public AccountEntity(){
		
	}



	/*	public AccountEntity(String accountName, AccountType accountType){
		this.accountName = accountName;
		this.accountType = accountType;
		
	}
*/
	@Ignore
	private List<AccountEntryEntity> accountLedgerEntries;

	

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
/*
	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
*/
	
	
	public Integer getDisplayOrderNo() {
		return displayOrderNo;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public void setDisplayOrderNo(Integer displayOrderNo) {
		this.displayOrderNo = displayOrderNo;
	}

	public List<AccountEntryEntity> getAccountLedgerEntries() {
		return accountLedgerEntries;
	}

	public void setAccountLedgerEntries(
			List<AccountEntryEntity> accountLedgerEntries) {
		this.accountLedgerEntries = accountLedgerEntries;
	}

	public Boolean getContra() {
		return contra;
	}

	public void setContra(Boolean contra) {
		this.contra = contra;
	}

}
