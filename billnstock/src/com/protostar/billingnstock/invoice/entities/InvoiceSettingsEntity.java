package com.protostar.billingnstock.invoice.entities;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class InvoiceSettingsEntity extends BaseEntity {

	private boolean showDefaultServiceItems = true;
	private boolean showDefaultProductItems = false;

	private boolean needModuleDocumentApproval = false;
	private boolean emailNotification = false;
	private String emailNotificationDL = "";

	private String noteToCustomer;
	private String paymentNotes;
	private String termsAndConditions;

	public String getNoteToCustomer() {
		return noteToCustomer;
	}

	public void setNoteToCustomer(String noteToCustomer) {
		this.noteToCustomer = noteToCustomer;
	}

	public boolean isShowDefaultServiceItems() {
		return showDefaultServiceItems;
	}

	public void setShowDefaultServiceItems(boolean showDefaultServiceItems) {
		this.showDefaultServiceItems = showDefaultServiceItems;
	}

	public boolean isShowDefaultProductItems() {
		return showDefaultProductItems;
	}

	public void setShowDefaultProductItems(boolean showDefaultProductItems) {
		this.showDefaultProductItems = showDefaultProductItems;
	}

	public boolean isEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	public String getEmailNotificationDL() {
		return emailNotificationDL;
	}

	public void setEmailNotificationDL(String emailNotificationDL) {
		this.emailNotificationDL = emailNotificationDL;
	}

	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

	public String getPaymentNotes() {
		return paymentNotes;
	}

	public void setPaymentNotes(String paymentNotes) {
		this.paymentNotes = paymentNotes;
	}

	public boolean isNeedModuleDocumentApproval() {
		return needModuleDocumentApproval;
	}

	public void setNeedModuleDocumentApproval(boolean needModuleDocumentApproval) {
		this.needModuleDocumentApproval = needModuleDocumentApproval;
	}

}
