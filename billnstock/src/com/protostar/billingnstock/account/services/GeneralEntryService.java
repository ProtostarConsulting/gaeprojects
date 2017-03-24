package com.protostar.billingnstock.account.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.protostar.billingnstock.account.entities.AccountEntryEntity;
import com.protostar.billingnstock.account.entities.GeneralEntryEntity;


@Api(name = "generalEntryService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.services", ownerName = "com.protostar.billingnstock.services", packagePath = ""))
public class GeneralEntryService {
	@ApiMethod(name = "addGeneralEntry")
	public void addGeneralEntry(GeneralEntryEntity entryEntity) {		
		
		AccountEntryEntity debitAcc= new AccountEntryEntity();
		debitAcc.setDate(new Date());
		debitAcc.setNarration(entryEntity.getNarration());
		debitAcc.setDebit(entryEntity.getAmount());
		debitAcc.setAccountEntity(entryEntity.getDebitAccount());
		debitAcc.setCreatedDate(new Date());
		debitAcc.setModifiedBy(entryEntity.getModifiedBy());
		debitAcc.setBusiness(entryEntity.getBusiness());
		AccountEntryService aes=new AccountEntryService();
		aes.addAccountEntry(debitAcc);
		
		AccountEntryEntity creditAcc= new AccountEntryEntity();
		creditAcc.setDate(new Date());
		creditAcc.setNarration(entryEntity.getNarration());
		creditAcc.setCredit(entryEntity.getAmount());
		creditAcc.setAccountEntity(entryEntity.getCreditAccount());
		creditAcc.setCreatedDate(new Date());
		creditAcc.setModifiedBy(entryEntity.getModifiedBy());
		creditAcc.setBusiness(entryEntity.getBusiness());
		aes.addAccountEntry(creditAcc);
		entryEntity.setCreatedDate(new Date());
		entryEntity.setModifiedDate(new Date());
		ofy().save().entity(entryEntity).now();
	}
	  }
