package com.protostar.billingnstock.account.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Entity
public class AccountEntryEntity extends BaseEntity {

	@Index
	private Date date;
	private String narration;
	private Double debit;
	private Double credit;

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.AEntry_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	@Index
	private Ref<AccountEntity> accountEntity;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public Double getDebit() {
		return debit == null ? null : debit;
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	public Double getCredit() {
		return credit == null ? null : credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public AccountEntity getAccountEntity() {
		return accountEntity == null ? null : accountEntity.get();
	}

	public void setAccountEntity(AccountEntity accountEntity) {
		this.accountEntity = Ref.create(accountEntity);
	}
}
