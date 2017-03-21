package com.protostar.billingnstock.user.entities;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class BusinessSettingsEntity extends BaseEntity {

	private boolean emailNotification = false;
	private boolean smsNotification = false;
	private String sendGridAPIKey;
	private String textLocalAPIKey;

	public boolean isEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	public boolean isSmsNotification() {
		return smsNotification;
	}

	public void setSmsNotification(boolean smsNotification) {
		this.smsNotification = smsNotification;
	}

	public String getSendGridAPIKey() {
		return sendGridAPIKey;
	}

	public void setSendGridAPIKey(String sendGridAPIKey) {
		this.sendGridAPIKey = sendGridAPIKey;
	}

	public String getTextLocalAPIKey() {
		return textLocalAPIKey;
	}

	public void setTextLocalAPIKey(String textLocalAPIKey) {
		this.textLocalAPIKey = textLocalAPIKey;
	}

}
