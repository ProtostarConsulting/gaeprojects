package com.protostar.billingnstock.production.entities;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class ProductionSetting extends BaseEntity {
	
	private String productionTermsAndConditions;
	private String productionShipTermsAndConditions;
	private boolean needModuleDocumentApproval = false;
	private boolean needPOApproval = false;
	private boolean linkPOToBudget = false;
	private boolean needStockShipmentApproval = false;
	private boolean emailNotification = false;	
	private String emailNotificationDL = "";
	
	public String getProductionTermsAndConditions() {
		return productionTermsAndConditions;
	}
	public void setProductionTermsAndConditions(String productionTermsAndConditions) {
		this.productionTermsAndConditions = productionTermsAndConditions;
	}
	public String getProductionShipTermsAndConditions() {
		return productionShipTermsAndConditions;
	}
	public void setProductionShipTermsAndConditions(
			String productionShipTermsAndConditions) {
		this.productionShipTermsAndConditions = productionShipTermsAndConditions;
	}
	public boolean isNeedModuleDocumentApproval() {
		return needModuleDocumentApproval;
	}
	public void setNeedModuleDocumentApproval(boolean needModuleDocumentApproval) {
		this.needModuleDocumentApproval = needModuleDocumentApproval;
	}
	public boolean isNeedPOApproval() {
		return needPOApproval;
	}
	public void setNeedPOApproval(boolean needPOApproval) {
		this.needPOApproval = needPOApproval;
	}
	public boolean isLinkPOToBudget() {
		return linkPOToBudget;
	}
	public void setLinkPOToBudget(boolean linkPOToBudget) {
		this.linkPOToBudget = linkPOToBudget;
	}
	public boolean isNeedStockShipmentApproval() {
		return needStockShipmentApproval;
	}
	public void setNeedStockShipmentApproval(boolean needStockShipmentApproval) {
		this.needStockShipmentApproval = needStockShipmentApproval;
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
