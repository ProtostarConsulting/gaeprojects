package com.protostar.billingnstock.account.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.account.entities.CurrentFinancialYear;
import com.protostar.billingnstock.account.entities.GeneralEntryEntity;
import com.protostar.billingnstock.account.entities.PaymentVoucherEntity;
import com.protostar.billingnstock.account.entities.PurchaseVoucherEntity;
import com.protostar.billingnstock.account.entities.ReceiptVoucherEntity;
import com.protostar.billingnstock.account.entities.SalesVoucherEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;
@Api(name = "voucherService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.services", ownerName = "com.protostar.billingnstock.services", packagePath = ""))
public class VoucherService {
	
	@ApiMethod(name = "addvoucher")
	public void addvoucher(SalesVoucherEntity salesVoucherEntity) {
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.YEAR, -2); 
		Date dummyDate = cal.getTime();
		System.out.println("-----------++"+dummyDate);
	
		ofy().save().entity(salesVoucherEntity).now();
		GeneralEntryEntity generalEntryEntity = new GeneralEntryEntity();
		generalEntryEntity.setDebitAccount(salesVoucherEntity.getAccountType1());
		generalEntryEntity.setAmount(salesVoucherEntity.getAmount());
		generalEntryEntity.setCreditAccount(salesVoucherEntity.getAccountType2());
		generalEntryEntity.setCreatedDate(dummyDate);
		generalEntryEntity.setDate(dummyDate);
		generalEntryEntity.setBusiness(salesVoucherEntity.getBusiness());
		AccountEntryService AccountEntryService = new AccountEntryService();
		AccountEntryService.addGeneralEntry(generalEntryEntity);
		
	}
	@ApiMethod(name = "getlistSalesVoucher")
	public List<SalesVoucherEntity> getlistSalesVoucher(@Named("id") Long busId) {
		AccountingService accountingService = new AccountingService();
		CurrentFinancialYear currentFinancialYear = accountingService.getCurrentFinancialYear(busId);
		if (busId == null) {
			return new ArrayList<SalesVoucherEntity>();
		}
		List<SalesVoucherEntity> salesvoucher = ofy()
				.load()
				.type(SalesVoucherEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("createdDate >=", currentFinancialYear.getFromDate())
				.filter("createdDate <=", currentFinancialYear.getToDate())
				.list();
		return salesvoucher;
	}

	@ApiMethod(name = "getSalesListById")
	public SalesVoucherEntity getSalesListById(@Named("id") Long accountId, @Named("bid") Long busId) {
		AccountingService accountingService = new AccountingService();
		CurrentFinancialYear currentFinancialYear = accountingService.getCurrentFinancialYear(busId);
		SalesVoucherEntity listByid = ofy()
				.cache(false).load()
				.key(Key.create(Key.create(BusinessEntity.class, busId), SalesVoucherEntity.class, accountId))
				.now();
		return listByid;
   }
	@ApiMethod(name = "getRecieptListById")
	public ReceiptVoucherEntity getRecieptListById(@Named("id") Long accountId, @Named("bid") Long busId) {
		ReceiptVoucherEntity relistByid = ofy().cache(false).load()
		.key(Key.create(Key.create(BusinessEntity.class, busId), ReceiptVoucherEntity.class, accountId)).now();
		return relistByid;
		}
	@SuppressWarnings("deprecation")
	@ApiMethod(name = "addvoucherReciept", path = "addvoucherReciept")
	public void addvoucherReciept(ReceiptVoucherEntity ReceiptVoucherEntity) {
			
		ReceiptVoucherEntity.setDate(new Date());
		ReceiptVoucherEntity.setCreatedDate(new Date());
		ofy().save().entity(ReceiptVoucherEntity).now();
		GeneralEntryEntity generalEntryEntity = new GeneralEntryEntity();
		
	
		generalEntryEntity.setDebitAccount(ReceiptVoucherEntity.getAccountType1());
		generalEntryEntity.setAmount(ReceiptVoucherEntity.getAmount());
		generalEntryEntity.setCreditAccount(ReceiptVoucherEntity.getAccountType2());
		generalEntryEntity.setCreatedDate(new Date());
		generalEntryEntity.setBusiness(ReceiptVoucherEntity.getBusiness());
		generalEntryEntity.setDate(new Date());
		AccountEntryService AccountEntryService = new AccountEntryService();
		AccountEntryService.addGeneralEntry(generalEntryEntity);
	}
	@ApiMethod(name = "listVoucherReciept", path = "listVoucherReciept")
	public List<ReceiptVoucherEntity> listVoucherReciept(@Named("id") Long busId) {
		AccountingService accountingService = new AccountingService();
		CurrentFinancialYear currentFinancialYear = accountingService.getCurrentFinancialYear(busId);
		List<ReceiptVoucherEntity> listRecipt = ofy()
				.load()
				.type(ReceiptVoucherEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("createdDate >=", currentFinancialYear.getFromDate())
				.filter("createdDate <=", currentFinancialYear.getToDate())
				.list();
		return listRecipt;
	}
	@ApiMethod(name = "addvoucherPurches", path = "addvoucherPurches")
	public void addvoucherPurches(PurchaseVoucherEntity purchaseVoucher) {
		ofy().save().entity(purchaseVoucher).now();
		GeneralEntryEntity generalEntryEntity = new GeneralEntryEntity();
		// Debit ac amt crd ac
		generalEntryEntity.setDebitAccount(purchaseVoucher.getPurchaseAccount());
		generalEntryEntity.setAmount(purchaseVoucher.getAmount());
		generalEntryEntity.setCreditAccount(purchaseVoucher.getCreditAccount());
		generalEntryEntity.setCreatedDate(new Date());
		generalEntryEntity.setBusiness(purchaseVoucher.getBusiness());
		generalEntryEntity.setDate(new Date());
		AccountEntryService AccountEntryService = new AccountEntryService();
		AccountEntryService.addGeneralEntry(generalEntryEntity);
		}

	@ApiMethod(name = "listVoucherPurches", path = "listVoucherPurches")
	public List<PurchaseVoucherEntity> listVoucherPurches(@Named("id") Long busId) {
		AccountingService accountingService = new AccountingService();
		CurrentFinancialYear currentFinancialYear = accountingService.getCurrentFinancialYear(busId);
			List<PurchaseVoucherEntity> listPurches = ofy()
				.load()
				.type(PurchaseVoucherEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("createdDate >=", currentFinancialYear.getFromDate())
				.filter("createdDate <=", currentFinancialYear.getToDate())
				.list();
		return listPurches;
	}
	/////////////////// -------Payment--------------------------------

	@ApiMethod(name = "addvoucherPayment", path = "addvoucherPayment")
	public void addvoucherPayment(PaymentVoucherEntity paymentVoucher) {
		
	   ofy().save().entity(paymentVoucher).now();
		GeneralEntryEntity generalEntryEntity = new GeneralEntryEntity();
		// Debit ac amt crd ac
		generalEntryEntity.setDebitAccount(paymentVoucher.getPaymentAccount());
		generalEntryEntity.setAmount(paymentVoucher.getAmount());
		generalEntryEntity.setCreditAccount(paymentVoucher.getCreditAccount());
		generalEntryEntity.setCreatedDate(new Date());
		generalEntryEntity.setBusiness(paymentVoucher.getBusiness());
		generalEntryEntity.setDate(new Date());
		AccountEntryService AccountEntryService = new AccountEntryService();
		AccountEntryService.addGeneralEntry(generalEntryEntity);
	}
	@ApiMethod(name = "listvoucherPayment", path = "listvoucherPayment")
	public List<PaymentVoucherEntity> listvoucherPayment(@Named("id") Long busId) {
		AccountingService accountingService = new AccountingService();
		CurrentFinancialYear currentFinancialYear = accountingService.getCurrentFinancialYear(busId);
		List<PaymentVoucherEntity> listPurches = ofy().load().type(PaymentVoucherEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("createdDate >=", currentFinancialYear.getFromDate())
				.filter("createdDate <=", currentFinancialYear.getToDate())
				.list();
		return listPurches;
	}
	///////////////////// ----------Payment--------------------------------
	@ApiMethod(name = "getPurchesListById")
	public PurchaseVoucherEntity getPurchesListById(@Named("id") Long accountId, @Named("bid") Long busId) {
		PurchaseVoucherEntity relistByid = ofy().cache(false).load()
				.key(Key.create(Key.create(BusinessEntity.class, busId), PurchaseVoucherEntity.class, accountId)).now();
		return relistByid;
	}
}
