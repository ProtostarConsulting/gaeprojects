package com.protostar.billingnstock.account.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class VoucherEntity extends BaseEntity {

	private Date date;
	@Index
	private Long voucherNumber;
	private Ref<GeneralEntryEntity> journalEntry;

	public GeneralEntryEntity getJournalEntry() {
		return journalEntry == null ? null : journalEntry.get();
	}

	public void setJournalEntry(GeneralEntryEntity journalEntry) {
		this.journalEntry = Ref.create(journalEntry);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getVoucherNumber() {
		return voucherNumber;
	}
  
	public void setVoucherNumber(Long voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

}
