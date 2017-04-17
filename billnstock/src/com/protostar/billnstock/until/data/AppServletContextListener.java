package com.protostar.billnstock.until.data;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;
import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.account.entities.AccountEntryEntity;
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.account.entities.AccountingSettingsEntity;
import com.protostar.billingnstock.account.entities.CurrentFinancialYear;
import com.protostar.billingnstock.account.entities.GeneralEntryEntity;
import com.protostar.billingnstock.account.entities.GeneralJournalEntity;
import com.protostar.billingnstock.account.entities.PayableEntity;
import com.protostar.billingnstock.account.entities.PaymentVoucherEntity;
import com.protostar.billingnstock.account.entities.PurchaseVoucherEntity;
import com.protostar.billingnstock.account.entities.ReceiptVoucherEntity;
import com.protostar.billingnstock.account.entities.ReceivableEntity;
import com.protostar.billingnstock.account.entities.SalesVoucherEntity;
import com.protostar.billingnstock.account.entities.VoucherEntity;
import com.protostar.billingnstock.assetmanagement.entities.Asset;
import com.protostar.billingnstock.assetmanagement.entities.AssetAssign;
import com.protostar.billingnstock.crm.entities.CRMSettingsEntity;
import com.protostar.billingnstock.crm.entities.Contact;
import com.protostar.billingnstock.crm.entities.Lead;
import com.protostar.billingnstock.crm.entities.Opportunity;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.hr.entities.Employee;
import com.protostar.billingnstock.hr.entities.HRSettingsEntity;
import com.protostar.billingnstock.hr.entities.LeaveAppEntity;
import com.protostar.billingnstock.hr.entities.LeaveDetailEntity;
import com.protostar.billingnstock.hr.entities.LeaveMasterEntity;
import com.protostar.billingnstock.hr.entities.MonthlyPaymentDetailEntity;
import com.protostar.billingnstock.hr.entities.SalSlip;
import com.protostar.billingnstock.hr.entities.SalStruct;
import com.protostar.billingnstock.hr.entities.TimeSheet;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.invoice.entities.InvoiceSettingsEntity;
import com.protostar.billingnstock.invoice.entities.QuotationEntity;
import com.protostar.billingnstock.proadmin.entities.BusinessPlanType;
import com.protostar.billingnstock.production.entities.BomEntity;
import com.protostar.billingnstock.production.entities.ProductionMachineEntity;
import com.protostar.billingnstock.purchase.entities.BudgetEntity;
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billingnstock.purchase.entities.RequisitionEntity;
import com.protostar.billingnstock.purchase.entities.SupplierEntity;
import com.protostar.billingnstock.sales.entities.SalesOrderEntity;
import com.protostar.billingnstock.stock.entities.StockItemBrand;
import com.protostar.billingnstock.stock.entities.StockItemEntity;
import com.protostar.billingnstock.stock.entities.StockItemInstanceEntity;
import com.protostar.billingnstock.stock.entities.StockItemOrderType;
import com.protostar.billingnstock.stock.entities.StockItemProductTypeEntity;
import com.protostar.billingnstock.stock.entities.StockItemTxnEntity;
import com.protostar.billingnstock.stock.entities.StockItemTypeCategory;
import com.protostar.billingnstock.stock.entities.StockItemTypeEntity;
import com.protostar.billingnstock.stock.entities.StockItemUnit;
import com.protostar.billingnstock.stock.entities.StockItemsReceiptEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.stock.entities.StockSettingsEntity;
import com.protostar.billingnstock.taskmangement.TaskEntity;
import com.protostar.billingnstock.taskmangement.TaskSettingsEntity;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.BusinessSettingsEntity;
import com.protostar.billingnstock.user.entities.EmpDepartment;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billingnstock.user.services.CurrentUserSession;
import com.protostar.billingnstock.user.services.UserLoginRecord;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService.CounterEntity;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService.CounterShard;

public class AppServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// Notification that the servlet context is about to be shut down.
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("###Inside AppServletContextListener###");
		// register all your entities here

		ObjectifyService.register(CounterEntity.class);
		ObjectifyService.register(CounterShard.class);
		ObjectifyService.register(Employee.class);
		ObjectifyService.register(SalStruct.class);
		ObjectifyService.register(SalSlip.class);
		ObjectifyService.register(Lead.class);
		ObjectifyService.register(TimeSheet.class);
		ObjectifyService.register(Asset.class);
		ObjectifyService.register(AssetAssign.class);

		ObjectifyService.register(BusinessPlanType.class);

		ObjectifyService.register(TimeSheet.class);
		ObjectifyService.register(Opportunity.class);
		ObjectifyService.register(Contact.class);
		ObjectifyService.register(UserEntity.class);
		ObjectifyService.register(CurrentUserSession.class);
		ObjectifyService.register(UserLoginRecord.class);

		ObjectifyService.register(EmpDepartment.class);
		ObjectifyService.register(BusinessEntity.class);

		ObjectifyService.register(Customer.class);
		ObjectifyService.register(StockItemTypeEntity.class);
		ObjectifyService.register(StockItemEntity.class);
		ObjectifyService.register(StockItemInstanceEntity.class);
		ObjectifyService.register(StockItemsReceiptEntity.class);
		ObjectifyService.register(StockItemsShipmentEntity.class);
		ObjectifyService.register(StockItemTxnEntity.class);
		ObjectifyService.register(StockSettingsEntity.class);
		ObjectifyService.register(StockItemUnit.class);
		ObjectifyService.register(StockItemOrderType.class);
		ObjectifyService.register(StockItemTypeCategory.class);
		ObjectifyService.register(StockItemBrand.class);
		ObjectifyService.register(StockItemProductTypeEntity.class);
		
		
		
		ObjectifyService.register(InvoiceEntity.class);
		ObjectifyService.register(QuotationEntity.class);
		ObjectifyService.register(TaxEntity.class);
		ObjectifyService.register(PurchaseOrderEntity.class);
		ObjectifyService.register(SalesOrderEntity.class);
		ObjectifyService.register(AccountEntity.class);
		ObjectifyService.register(AccountGroupEntity.class);
		ObjectifyService.register(AccountEntryEntity.class);
		ObjectifyService.register(GeneralEntryEntity.class);
		ObjectifyService.register(GeneralJournalEntity.class);
		ObjectifyService.register(VoucherEntity.class);

		ObjectifyService.register(PayableEntity.class);
		ObjectifyService.register(ReceivableEntity.class);
		ObjectifyService.register(WarehouseEntity.class);
		ObjectifyService.register(SupplierEntity.class);
		ObjectifyService.register(InvoiceSettingsEntity.class);

		ObjectifyService.register(SalesVoucherEntity.class);
		ObjectifyService.register(PaymentVoucherEntity.class);
		ObjectifyService.register(ReceiptVoucherEntity.class);
		ObjectifyService.register(PurchaseVoucherEntity.class);
		ObjectifyService.register(TaskEntity.class);
		ObjectifyService.register(TaskSettingsEntity.class);
		ObjectifyService.register(CRMSettingsEntity.class);
		ObjectifyService.register(LeaveDetailEntity.class);
		ObjectifyService.register(LeaveAppEntity.class);
		ObjectifyService.register(LeaveMasterEntity.class);
		ObjectifyService.register(MonthlyPaymentDetailEntity.class);
		ObjectifyService.register(HRSettingsEntity.class);
		ObjectifyService.register(RequisitionEntity.class);
		ObjectifyService.register(BudgetEntity.class);
		ObjectifyService.register(BusinessSettingsEntity.class);
		ObjectifyService.register(AccountingSettingsEntity.class);
		ObjectifyService.register(CurrentFinancialYear.class);
		ObjectifyService.register(BomEntity.class);
		ObjectifyService.register(ProductionMachineEntity.class);

	}

}