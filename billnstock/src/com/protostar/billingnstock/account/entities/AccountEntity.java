package com.protostar.billingnstock.account.entities;

import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.AccountingAccountType;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Entity
public class AccountEntity extends BaseEntity {

	@Index
	private String accountName;
	private String accountNo;
	private String description;
	private Integer displayOrderNo;
	private Boolean contra = false;
	private AccountingAccountType accountType;
	@Index
	private Ref<AccountGroupEntity> accountGroup;
	@Ignore
	private List<AccountEntryEntity> accountLedgerEntries;

	public AccountEntity() {

	}

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()),
					Constants.AE_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public AccountGroupEntity getAccountGroup() {
		return accountGroup == null ? null : accountGroup.get();
	}

	public void setAccountGroup(AccountGroupEntity accountGroup) {
		if (accountGroup != null)
			this.accountGroup = Ref.create(accountGroup);
	}

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

	public Integer getDisplayOrderNo() {
		return displayOrderNo;
	}

	public AccountingAccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountingAccountType accountType) {
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
