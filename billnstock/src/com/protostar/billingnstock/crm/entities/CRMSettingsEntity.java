package com.protostar.billingnstock.crm.entities;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class CRMSettingsEntity extends BaseEntity {

	private boolean needModuleDocumentApproval = false;
	private boolean emailNotification = false;
	private String emailNotificationDL = "";

	public boolean isNeedModuleDocumentApproval() {
		return needModuleDocumentApproval;
	}

	public void setNeedModuleDocumentApproval(boolean needModuleDocumentApproval) {
		this.needModuleDocumentApproval = needModuleDocumentApproval;
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

}
