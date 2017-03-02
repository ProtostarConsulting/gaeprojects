package com.protostar.billingnstock.stock.entities;

import com.googlecode.objectify.annotation.Entity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class StockSettingsEntity extends BaseEntity {	
	private String poTermsAndConditions;
	private String stockShipTermsAndConditions;
	private boolean needPOApproval = false;
	private boolean linkPOToBudget = false;
	private boolean needStockShipmentApproval = false;
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

	public String getPoTermsAndConditions() {
		return poTermsAndConditions;
	}

	public void setPoTermsAndConditions(String poTermsAndConditions) {
		this.poTermsAndConditions = poTermsAndConditions;
	}

	public String getStockShipTermsAndConditions() {
		return stockShipTermsAndConditions;
	}

	public void setStockShipTermsAndConditions(String stockShipTermsAndConditions) {
		this.stockShipTermsAndConditions = stockShipTermsAndConditions;
	}

	public boolean isNeedPOApproval() {
		return needPOApproval;
	}

	public void setNeedPOApproval(boolean needPOApproval) {
		this.needPOApproval = needPOApproval;
	}

	public boolean isNeedStockShipmentApproval() {
		return needStockShipmentApproval;
	}

	public void setNeedStockShipmentApproval(boolean needStockShipmentApproval) {
		this.needStockShipmentApproval = needStockShipmentApproval;
	}

	public boolean isLinkPOToBudget() {
		return linkPOToBudget;
	}

	public void setLinkPOToBudget(boolean linkPOToBudget) {
		this.linkPOToBudget = linkPOToBudget;
	}

}
