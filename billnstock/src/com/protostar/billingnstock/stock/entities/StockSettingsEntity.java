package com.protostar.billingnstock.stock.entities;

import com.googlecode.objectify.annotation.Entity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class StockSettingsEntity extends BaseEntity {
	private String noteToCustomer;
	private String termsAndConditions;
	
	private boolean emailNotification = false;
	private String emailNotificationDL = "";

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

	public String getNoteToCustomer() {
		return noteToCustomer;
	}

	public void setNoteToCustomer(String noteToCustomer) {
		this.noteToCustomer = noteToCustomer;
	}

	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

}
