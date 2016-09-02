package com.protostar.billingnstock.account.services;
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.protostar.billingnstock.account.entities.GeneralEntryEntity;
import com.protostar.billingnstock.account.entities.PurchaseVoucherEntity;
import com.protostar.billingnstock.account.entities.ReceiptVoucherEntity;
import com.protostar.billingnstock.account.entities.SalesVoucherEntity;

@Api(name = "voucherService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.services", ownerName = "com.protostar.billingnstock.services", packagePath = ""))

public class VoucherService {
	
	@ApiMethod(name = "addvoucher")
	public void addvoucher(SalesVoucherEntity salesVoucherEntity )
	{
		ofy().save().entity(salesVoucherEntity).now();
		GeneralEntryEntity generalEntryEntity = new GeneralEntryEntity(); 
		//Debit ac amt crd ac
		generalEntryEntity.setDebitAccount(salesVoucherEntity.getAccountType1());
		generalEntryEntity.setAmount(salesVoucherEntity.getAmount());
		generalEntryEntity.setCreditAccount(salesVoucherEntity.getAccountType2());
		generalEntryEntity.setCreatedDate(new Date());
		
		GeneralEntryService generalEntryService = new GeneralEntryService();
		generalEntryService.addGeneralEntry(generalEntryEntity);
		
	}
	
	
	@ApiMethod(name = "listVoucher")
		public List<SalesVoucherEntity> listvoucher() 
		{
			return ofy().load().type(SalesVoucherEntity.class).list();
		}
			
	@ApiMethod(name = "addvoucherReciept", path="addvoucherReciept")
	public void addvoucherReciept(ReceiptVoucherEntity ReceiptVoucherEntity  )
	{
		ofy().save().entity(ReceiptVoucherEntity).now();
	}
	
	
	@ApiMethod(name = "listVoucherReciept", path="listVoucherReciept")
		public List<ReceiptVoucherEntity> listVoucherReciept() 
		{
			return ofy().load().type(ReceiptVoucherEntity.class).list();
		}
			
			
			
	@ApiMethod(name = "addvoucherPurches", path="addvoucherPurches")
	public void addvoucherPurches(PurchaseVoucherEntity PurchaseVoucherEntity  )
	{
		ofy().save().entity(PurchaseVoucherEntity).now();
	}
		
	
	

}
