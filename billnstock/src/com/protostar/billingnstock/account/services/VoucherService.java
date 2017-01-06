package com.protostar.billingnstock.account.services;
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.account.entities.GeneralEntryEntity;
import com.protostar.billingnstock.account.entities.PurchaseVoucherEntity;
import com.protostar.billingnstock.account.entities.ReceiptVoucherEntity;
import com.protostar.billingnstock.account.entities.SalesVoucherEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "voucherService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.services", ownerName = "com.protostar.billingnstock.services", packagePath = ""))

public class VoucherService {
	
	@ApiMethod(name = "addvoucher")
	public void addvoucher(SalesVoucherEntity salesVoucherEntity )
	{
		ofy().save().entity(salesVoucherEntity).now();
		GeneralEntryEntity generalEntryEntity = new GeneralEntryEntity(); 
		
		generalEntryEntity.setDebitAccount(salesVoucherEntity.getAccountType1());
		generalEntryEntity.setAmount(salesVoucherEntity.getAmount());
		generalEntryEntity.setCreditAccount(salesVoucherEntity.getAccountType2());
		generalEntryEntity.setCreatedDate(new Date());
		generalEntryEntity.setDate(new Date());
		generalEntryEntity.setBusiness(salesVoucherEntity.getBusiness());
		GeneralEntryService generalEntryService = new GeneralEntryService();
		generalEntryService.addGeneralEntry(generalEntryEntity);
		
	}
	
	
	@ApiMethod(name ="getlistSalesVoucher")
		public List<SalesVoucherEntity>getlistSalesVoucher(@Named("id") Long busId)
		{
		System.out.println("id:******"+busId);
		if(busId == null)
			{return new ArrayList<SalesVoucherEntity>();}

		
		List<SalesVoucherEntity> salesvoucher= ofy().load().type(SalesVoucherEntity.class).ancestor(Key.create(BusinessEntity.class, busId)).list();
		System.out.println("salesvoucher:******"+salesvoucher);
		
					return salesvoucher;
		}
	
	
	@ApiMethod(name = "getSalesListById")
	public SalesVoucherEntity  getSalesListById(@Named("id") Long accountId,@Named("bid") Long busId) 
	{
	
	
	SalesVoucherEntity listByid=ofy().cache(false).load().key(Key.create(Key.create(BusinessEntity.class,busId), SalesVoucherEntity.class, accountId)).now();
	
	
	
	return listByid;
	
	
	
	}
	
	
	@ApiMethod(name = "getRecieptListById")
	public ReceiptVoucherEntity  getRecieptListById(@Named("id") Long accountId,@Named("bid") Long busId) 
	{
		ReceiptVoucherEntity relistByid= ofy().cache(false).load().key(Key.create(Key.create(BusinessEntity.class,busId), ReceiptVoucherEntity.class, accountId)).now();
	return relistByid;
	
	
	
	}
	
			
	@ApiMethod(name = "addvoucherReciept", path="addvoucherReciept")
	public void addvoucherReciept(ReceiptVoucherEntity ReceiptVoucherEntity  )
	{
		ReceiptVoucherEntity.setDate(new Date());
		ReceiptVoucherEntity.setCreatedDate(new Date());
		ofy().save().entity(ReceiptVoucherEntity).now();
		
		
		
		GeneralEntryEntity generalEntryEntity = new GeneralEntryEntity(); 
		//Debit ac amt crd ac
		generalEntryEntity.setDebitAccount(ReceiptVoucherEntity.getAccountType2());
		generalEntryEntity.setAmount(ReceiptVoucherEntity.getAmount());
		generalEntryEntity.setCreditAccount(ReceiptVoucherEntity.getAccountType1());
		generalEntryEntity.setCreatedDate(new Date());
		generalEntryEntity.setBusiness(ReceiptVoucherEntity.getBusiness());
		
		System.out.println("***********date:"+new Date());
		generalEntryEntity.setDate(new Date());
		GeneralEntryService generalEntryService = new GeneralEntryService();
		generalEntryService.addGeneralEntry(generalEntryEntity);
		
	}
	
	
	@ApiMethod(name = "listVoucherReciept", path="listVoucherReciept")
		public List<ReceiptVoucherEntity> listVoucherReciept(@Named("id") Long busId) 
		{
		List<ReceiptVoucherEntity> listRecipt= ofy().load().type(ReceiptVoucherEntity.class).list();
		return listRecipt;
		}
			
			
			
	@ApiMethod(name = "addvoucherPurches", path="addvoucherPurches")
	public void addvoucherPurches(PurchaseVoucherEntity PurchaseVoucherEntity  )
	{
		ofy().save().entity(PurchaseVoucherEntity).now();
		
		
		GeneralEntryEntity generalEntryEntity = new GeneralEntryEntity(); 
		//Debit ac amt crd ac
		generalEntryEntity.setDebitAccount(PurchaseVoucherEntity.getAccountType2());
		generalEntryEntity.setAmount(PurchaseVoucherEntity.getAmount());
		generalEntryEntity.setCreditAccount(PurchaseVoucherEntity.getAccountType1());
		generalEntryEntity.setCreatedDate(new Date());
		generalEntryEntity.setBusiness(PurchaseVoucherEntity.getBusiness());
		generalEntryEntity.setDate(new Date());
		GeneralEntryService generalEntryService = new GeneralEntryService();
		generalEntryService.addGeneralEntry(generalEntryEntity);
		
	}
		
	
	
	@ApiMethod(name = "listVoucherPurches", path="listVoucherPurches")
	public List<PurchaseVoucherEntity>listVoucherPurches(@Named("id") Long busId) 
	{
		List<PurchaseVoucherEntity> listPurches= ofy().load().type(PurchaseVoucherEntity.class).list();
		return listPurches;
	}
		
	
	@ApiMethod(name = "getPurchesListById")
	public PurchaseVoucherEntity  getPurchesListById(@Named("id") Long accountId,@Named("bid") Long busId) 
	{
		PurchaseVoucherEntity relistByid= ofy().cache(false).load().key(Key.create(Key.create(BusinessEntity.class,busId), PurchaseVoucherEntity.class, accountId)).now();
	return relistByid;
	
	}
	
	
	
	

}
