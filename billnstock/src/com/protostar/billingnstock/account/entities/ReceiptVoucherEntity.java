package com.protostar.billingnstock.account.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.OnSave;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Entity
public class ReceiptVoucherEntity extends VoucherEntity {
	private Ref<AccountEntity>accountType1;
	private  Ref<AccountEntity>accountType2;
	public Double amount;
	public String narration;

	@Override
	public void beforeSave() {
		super.beforeSave();
		
		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()),
					Constants.RV_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}
		public AccountEntity getAccountType1() {
		return accountType1.get();
	}
	public void setAccountType1(AccountEntity accountType1) {
		this.accountType1 = Ref.create(accountType1);
	}
	public AccountEntity getAccountType2() {
		return accountType2.get();
	}
	public void setAccountType2(AccountEntity accountType2) {
		this.accountType2 = Ref.create(accountType2);
	}
	
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}
		

}
