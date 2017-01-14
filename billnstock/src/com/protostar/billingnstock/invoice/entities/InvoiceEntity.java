package com.protostar.billingnstock.invoice.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DiscountType;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Entity
public class InvoiceEntity extends BaseEntity {

	@Index
	private DocumentStatus status = DocumentStatus.DRAFT;
	private DiscountType discountType = DiscountType.NA;

	@Index
	private boolean isStatusAlreadyFinalized = false;

	@Index
	private Date invoiceDueDate;
	private double finalTotal;
	private String noteToCustomer;

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

	@OnSave
	public void beforeSave() {
		super.beforeSave();

		if (this.status == DocumentStatus.FINALIZED) {
			this.isStatusAlreadyFinalized = true;
		}

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()),
					Constants.INVOICE_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

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

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
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

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public DocumentStatus getStatus() {
		return status;
	}

	public void setStatus(DocumentStatus status) {
		this.status = status;
	}

	public boolean isStatusAlreadyFinalized() {
		return isStatusAlreadyFinalized;
	}

	public void setStatusAlreadyFinalized(boolean isStatusAlreadyFinalized) {
		this.isStatusAlreadyFinalized = isStatusAlreadyFinalized;
	}

}// end of InvoiceEntity
