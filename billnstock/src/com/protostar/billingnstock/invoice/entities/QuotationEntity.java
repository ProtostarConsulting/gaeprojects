package com.protostar.billingnstock.invoice.entities;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.sales.entities.SalesOrderEntity;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class QuotationEntity extends BaseEntity {
	@Index
	private Date quotationDate;
	@Index
	private Date quotationDueDate;
	private float productSubTotal;

	private float serviceSubTotal;
	private float productTaxTotal;
	private float serviceTaxTotal;
	private float serviceTotal;
	private Long productTotal;
	private String finalTotal;
	private String noteToCustomer;
	private String status = "NotPaid";

	private String discount;
	private float discAmount;
	private float discValue;

	private Long pOrder;

	private List<InvoiceLineItem> invoiceLineItemList;
	private List<ServiceLineItemList> serviceLineItemList;

	Ref<SalesOrderEntity> salesOrderId;
	Ref<AccountEntity> account;
	Ref<TaxEntity> selectedTaxItem;
	Ref<TaxEntity> selectedServiceTax;

	@Index
	Ref<Customer> customer;

	public Customer getCustomer() {
		return customer.get();
	}

	public void setCustomer(Customer customer) {
		this.customer = Ref.create(customer);
	}

	public TaxEntity getSelectedTaxItem() {
		return selectedTaxItem == null ? null : selectedTaxItem.get();
	}

	public void setSelectedTaxItem(TaxEntity selectedTaxItem) {
		if (selectedTaxItem == null) {
			this.selectedTaxItem = null;
		} else {
			this.selectedTaxItem = Ref.create(selectedTaxItem);
		}
	}

	public TaxEntity getSelectedServiceTax() {
		return selectedServiceTax == null ? null : selectedServiceTax.get();
	}

	public void setSelectedServiceTax(TaxEntity selectedServiceTax) {
		if (selectedServiceTax == null) {
			this.selectedServiceTax = null;
		} else {
			this.selectedServiceTax = Ref.create(selectedServiceTax);
		}
	}

	public SalesOrderEntity getSalesOrderId() {
		return salesOrderId == null ? null : salesOrderId.get();
	}

	public void setSalesOrderId(SalesOrderEntity salesOrderId) {
		if (salesOrderId == null) {
			this.salesOrderId = null;
		} else {
			this.salesOrderId = Ref.create(salesOrderId);
		}
	}

	public AccountEntity getAccount() {
		return account == null ? null : account.get();
	}

	public void setAccount(AccountEntity account) {
		if (account == null) {
			this.account = null;
		} else {
			this.account = Ref.create(account);
		}
	}

	public List<InvoiceLineItem> getInvoiceLineItemList() {
		return invoiceLineItemList;
	}

	public void setInvoiceLineItemList(List<InvoiceLineItem> invoiceLineItemList) {
		this.invoiceLineItemList = invoiceLineItemList;
	}

	public List<ServiceLineItemList> getServiceLineItemList() {
		return serviceLineItemList;
	}

	public void setServiceLineItemList(
			List<ServiceLineItemList> serviceLineItemList) {
		this.serviceLineItemList = serviceLineItemList;
	}

	public float getProductSubTotal() {
		return productSubTotal;
	}

	public void setProductSubTotal(float productSubTotal) {
		this.productSubTotal = productSubTotal;
	}

	public float getProductTaxTotal() {
		return productTaxTotal;
	}

	public void setProductTaxTotal(float productTaxTotal) {
		this.productTaxTotal = productTaxTotal;
	}

	public float getServiceTaxTotal() {
		return serviceTaxTotal;
	}

	public void setServiceTaxTotal(float serviceTaxTotal) {
		this.serviceTaxTotal = serviceTaxTotal;
	}

	public String getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(String finalTotal) {
		this.finalTotal = finalTotal;
	}

	public String getNoteToCustomer() {
		return noteToCustomer;
	}

	public void setNoteToCustomer(String noteToCustomer) {
		this.noteToCustomer = noteToCustomer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public Date getQuotationDate() {
		return quotationDate;
	}

	public void setQuotationDate(Date quotationDate) {
		this.quotationDate = quotationDate;
	}

	public Date getQuotationDueDate() {
		return quotationDueDate;
	}

	public void setQuotationDueDate(Date quotationDueDate) {
		this.quotationDueDate = quotationDueDate;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public float getDiscAmount() {
		return discAmount;
	}

	public void setDiscAmount(float discAmount) {
		this.discAmount = discAmount;
	}

	public Long getpOrder() {
		return pOrder;
	}

	public void setpOrder(Long pOrder) {
		this.pOrder = pOrder;
	}

	public float getServiceSubTotal() {
		return serviceSubTotal;
	}

	public void setServiceSubTotal(float serviceSubTotal) {
		this.serviceSubTotal = serviceSubTotal;
	}

	public float getDiscValue() {
		return discValue;
	}

	public void setDiscValue(float discValue) {
		this.discValue = discValue;
	}

	public float getServiceTotal() {
		return serviceTotal;
	}

	public void setServiceTotal(float serviceTotal) {
		this.serviceTotal = serviceTotal;
	}

	public Long getProductTotal() {
		return productTotal;
	}

	public void setProductTotal(Long productTotal) {
		this.productTotal = productTotal;
	}

}// end of QuotationEntity
