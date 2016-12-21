package com.protostar.billingnstock.invoice.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class InvoiceEntity extends BaseEntity {

	public static enum DiscountType {
		Fixed, Percentage
	};

	@Index
	private int invoiceNumber;
	@Index
	private Date invoiceDueDate;
	private double finalTotal;
	private String noteToCustomer;

	private String discountType;
	private float discountPercent;
	private double discAmount;
	@Index
	private boolean isPaid = false;
	@Index
	private boolean isDraft = false;
	private Date paidDate;

	private List<StockLineItem> productLineItemList = new ArrayList<StockLineItem>();
	private List<StockLineItem> serviceLineItemList = new ArrayList<StockLineItem>();

	private Ref<TaxEntity> selectedProductTax;
	private Ref<TaxEntity> selectedServiceTax;

	@Index
	private Ref<Customer> customer;
	private String pOrder;

	public Customer getCustomer() {
		return customer == null ? null : customer.get();
	}

	public void setCustomer(Customer customer) {
		if (customer != null)
			this.customer = Ref.create(customer);
	}

	public TaxEntity getSelectedServiceTax() {
		return selectedServiceTax == null ? null : selectedServiceTax.get();
	}

	public void setSelectedServiceTax(TaxEntity selectedServiceTax) {
		if (selectedServiceTax != null)
			this.selectedServiceTax = Ref.create(selectedServiceTax);
	}

	public TaxEntity getSelectedProductTax() {
		return selectedProductTax == null ? null : selectedProductTax.get();
	}

	public void setSelectedProductTax(TaxEntity selectedProductTax) {
		if (selectedProductTax != null)
			this.selectedProductTax = Ref.create(selectedProductTax);
	}

	public double getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(double finalTotal) {
		this.finalTotal = finalTotal;
	}

	public String getNoteToCustomer() {
		return noteToCustomer;
	}

	public void setNoteToCustomer(String noteToCustomer) {
		this.noteToCustomer = noteToCustomer;
	}

	public Date getInvoiceDueDate() {
		return invoiceDueDate;
	}

	public void setInvoiceDueDate(Date invoiceDueDate) {
		this.invoiceDueDate = invoiceDueDate;
	}

	public double getDiscAmount() {
		return discAmount;
	}

	public void setDiscAmount(double discAmount) {
		this.discAmount = discAmount;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getpOrder() {
		return pOrder;
	}

	public void setpOrder(String pOrder) {
		this.pOrder = pOrder;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public boolean isDraft() {
		return isDraft;
	}

	public void setDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}

	public List<StockLineItem> getProductLineItemList() {
		return productLineItemList;
	}

	public void setProductLineItemList(List<StockLineItem> productLineItemList) {
		this.productLineItemList = productLineItemList;
	}

	public List<StockLineItem> getServiceLineItemList() {
		return serviceLineItemList;
	}

	public void setServiceLineItemList(List<StockLineItem> serviceLineItemList) {
		this.serviceLineItemList = serviceLineItemList;
	}

	public int getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(int invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

}// end of InvoiceEntity
