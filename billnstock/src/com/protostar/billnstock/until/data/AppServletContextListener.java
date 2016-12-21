package com.protostar.billnstock.until.data;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;
import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.account.entities.AccountEntryEntity;
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.account.entities.AccountingFYEntity;
import com.protostar.billingnstock.account.entities.GeneralEntryEntity;
import com.protostar.billingnstock.account.entities.GeneralJournalEntity;
import com.protostar.billingnstock.account.entities.PayableEntity;
import com.protostar.billingnstock.account.entities.PurchaseVoucherEntity;
import com.protostar.billingnstock.account.entities.ReceiptVoucherEntity;
import com.protostar.billingnstock.account.entities.ReceivableEntity;
import com.protostar.billingnstock.account.entities.SalesVoucherEntity;
import com.protostar.billingnstock.account.entities.VoucherEntity;
import com.protostar.billingnstock.assetmanagement.entities.Asset;
import com.protostar.billingnstock.assetmanagement.entities.AssetAssign;
import com.protostar.billingnstock.crm.entities.Contact;
import com.protostar.billingnstock.crm.entities.Lead;
import com.protostar.billingnstock.crm.entities.Opportunity;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.hr.entities.Employee;
import com.protostar.billingnstock.hr.entities.LeaveDetailEntity;
import com.protostar.billingnstock.hr.entities.MonthlyPaymentDetailEntity;
import com.protostar.billingnstock.hr.entities.SalSlip;
import com.protostar.billingnstock.hr.entities.SalStruct;
import com.protostar.billingnstock.hr.entities.TimeSheet;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.invoice.entities.InvoiceSettingsEntity;
import com.protostar.billingnstock.invoice.entities.QuotationEntity;
import com.protostar.billingnstock.invoice.entities.StockItemsReceiptEntity;
import com.protostar.billingnstock.invoice.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.proadmin.entities.AccountType;
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billingnstock.purchase.entities.SupplierEntity;
import com.protostar.billingnstock.sales.entities.SalesOrderEntity;
import com.protostar.billingnstock.stock.entities.StockItemEntity;
import com.protostar.billingnstock.stock.entities.StockItemTxnEntity;
import com.protostar.billingnstock.taskmangement.TaskEntity;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.EmpDepartment;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;

public class AppServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// Notification that the servlet context is about to be shut down.
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("###Inside AppServletContextListener###");
		// register all your entities here

		ObjectifyService.register(Employee.class);
		ObjectifyService.register(SalStruct.class);
		ObjectifyService.register(SalSlip.class);
		ObjectifyService.register(Lead.class);
		ObjectifyService.register(TimeSheet.class);
		ObjectifyService.register(Asset.class);
		ObjectifyService.register(AssetAssign.class);

		ObjectifyService.register(AccountType.class);

		ObjectifyService.register(TimeSheet.class);
		ObjectifyService.register(Opportunity.class);
		ObjectifyService.register(Contact.class);
		ObjectifyService.register(UserEntity.class);
		ObjectifyService.register(EmpDepartment.class);
		ObjectifyService.register(BusinessEntity.class);
		
		ObjectifyService.register(Customer.class);
		ObjectifyService.register(StockItemEntity.class);
		ObjectifyService.register(StockItemsReceiptEntity.class);
		ObjectifyService.register(StockItemsShipmentEntity.class);
		ObjectifyService.register(StockItemTxnEntity.class);
		
		ObjectifyService.register(InvoiceEntity.class);
		ObjectifyService.register(QuotationEntity.class);
		ObjectifyService.register(TaxEntity.class);
		ObjectifyService.register(PurchaseOrderEntity.class);
		ObjectifyService.register(SalesOrderEntity.class);
		ObjectifyService.register(AccountEntity.class);
		ObjectifyService.register(AccountGroupEntity.class);
		ObjectifyService.register(AccountEntryEntity.class);
		ObjectifyService.register(AccountingFYEntity.class);
		ObjectifyService.register(GeneralEntryEntity.class);		
		ObjectifyService.register(GeneralJournalEntity.class);
		ObjectifyService.register(VoucherEntity.class);
		
		ObjectifyService.register(PayableEntity.class);
		ObjectifyService.register(ReceivableEntity.class);
		ObjectifyService.register(WarehouseEntity.class);
		ObjectifyService.register(SupplierEntity.class);
		ObjectifyService.register(InvoiceSettingsEntity.class);
		
		ObjectifyService.register(SalesVoucherEntity.class);
		ObjectifyService.register(ReceiptVoucherEntity.class);
		ObjectifyService.register(PurchaseVoucherEntity.class);
		ObjectifyService.register(TaskEntity.class);
		ObjectifyService.register(LeaveDetailEntity.class);
		ObjectifyService.register(MonthlyPaymentDetailEntity.class);


	}

}