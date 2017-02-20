package com.protostar.billingnstock.account.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Entity
public class PaymentVoucherEntity extends VoucherEntity {
	private Ref<InvoiceEntity> invoiceEntity;
	
	private Ref<AccountEntity> paymentAccount;
	private Ref<AccountEntity> creditAccount;
	
	
	public Double amount;
	public String narration;
	public boolean isCash;
	public String item;
	public String accdetail;
	
	@Override
	public void beforeSave() {
		super.beforeSave();
		
		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()),
					Constants.PayV_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

	public InvoiceEntity getInvoiceEntity() {
		return invoiceEntity == null ? null : invoiceEntity.get();
		
	}

	public void setInvoiceEntity(InvoiceEntity invoiceEntity) {
		if (invoiceEntity != null)
			this.invoiceEntity = Ref.create(invoiceEntity);
	}

	public AccountEntity getPaymentAccount() {
		return paymentAccount == null ? null : paymentAccount.get();
	}

	public void setPaymentAccount(AccountEntity paymentAccount) {
		if (paymentAccount != null)
			this.paymentAccount = Ref.create(paymentAccount);
	}

	public AccountEntity getCreditAccount() {
		return creditAccount == null ? null : creditAccount.get();
	}

	public void setCreditAccount(AccountEntity creditAccount) {
		if (creditAccount != null)
			this.creditAccount = Ref.create(creditAccount);
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

	public boolean isCash() {
		return isCash;
	}

	public void setCash(boolean isCash) {
		this.isCash = isCash;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getAccdetail() {
		return accdetail;
	}

	public void setAccdetail(String accdetail) {
		this.accdetail = accdetail;
	}

	

}
