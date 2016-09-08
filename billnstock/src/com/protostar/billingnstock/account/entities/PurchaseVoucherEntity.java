package com.protostar.billingnstock.account.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;

@Entity
public class PurchaseVoucherEntity extends VoucherEntity {

	private Ref<InvoiceEntity> invoiceEntity;
	private Ref<AccountEntity> accountType1;
	private Ref<AccountEntity> accountType2;
	
	public Double amount;
	public String narration;
	public boolean isCash;
	public String item;
	public String accdetail;
	
	
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

	public InvoiceEntity getInvoiceEntity() {
		return invoiceEntity == null ? null : invoiceEntity.get();
	}

	public void setInvoiceEntity(InvoiceEntity invoiceEntity) {
		this.invoiceEntity = Ref.create(invoiceEntity);
	}


	

	

}
