package com.protostar.billingnstock.account.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class ReceivableEntity extends BaseEntity {

	@Index
	private Long invoiceId;
	private Date receivableDate;
	private Date invoiceDate;
	private Date invoiceDueDate;
	private Date quotationDate;
	private Date quotationDueDate;
	private String finalTotal;
	private String status = "NotPaid";

	@Index
	Ref<Customer> customer;

	public Customer getCustomer() {
		return customer == null ? null : customer.get();
	}

	public void setCustomer(Customer customer) {
		this.customer = Ref.create(customer);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date date) {
		this.invoiceDate = date;
	}

	public Date getReceivableDate() {
		return receivableDate;
	}

	public void setReceivableDate(Date receivableDate) {
		this.receivableDate = receivableDate;
	}

	public Date getInvoiceDueDate() {
		return invoiceDueDate;
	}

	public void setInvoiceDueDate(Date invoiceDueDate) {
		this.invoiceDueDate = invoiceDueDate;
	}

	public String getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(String finalTotal) {
		this.finalTotal = finalTotal;
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
	
}
