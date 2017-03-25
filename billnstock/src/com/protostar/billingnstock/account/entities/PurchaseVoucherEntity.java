package com.protostar.billingnstock.account.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Entity
public class PurchaseVoucherEntity extends VoucherEntity {
	private Ref<InvoiceEntity> invoiceEntity;
	private Ref<AccountEntity> purchaseAccount;
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
					Constants.PV_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}
	public String getAccdetail() {
		return accdetail;
	}

	public void setAccdetail(String accdetail) {
		this.accdetail = accdetail;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
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

	public InvoiceEntity getInvoiceEntity() {
		return invoiceEntity == null ? null : invoiceEntity.get();
	}

	public void setInvoiceEntity(InvoiceEntity invoiceEntity) {
		this.invoiceEntity = Ref.create(invoiceEntity);
	}
	public AccountEntity getPurchaseAccount() {
		return purchaseAccount == null ? null : purchaseAccount.get();
	}
	public void setPurchaseAccount(AccountEntity purchaseAccount) {
		if (purchaseAccount != null)
			this.purchaseAccount = Ref.create(purchaseAccount);
	}
	public AccountEntity getCreditAccount() {
		return creditAccount == null ? null : creditAccount.get();
	}
	public void setCreditAccount(AccountEntity creditAccount) {
		if (purchaseAccount != null)
			this.creditAccount = Ref.create(creditAccount);
	}
	
	}
