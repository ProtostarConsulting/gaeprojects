package com.protostar.billingnstock.user.entities;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class BusinessSettingsEntity extends BaseEntity {

	private boolean emailNotification = false;
	private boolean smsNotification = false;
	private String SENDGRID_API_KEY;
	private String TEXTLOCAL_API_KEY;

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

	public String getSENDGRID_API_KEY() {
		return SENDGRID_API_KEY;
	}

	public void setSENDGRID_API_KEY(String sENDGRID_API_KEY) {
		this.SENDGRID_API_KEY = sENDGRID_API_KEY;
	}

	public String getTEXTLOCAL_API_KEY() {
		return TEXTLOCAL_API_KEY;
	}

	public void setTEXTLOCAL_API_KEY(String tEXTLOCAL_API_KEY) {
		TEXTLOCAL_API_KEY = tEXTLOCAL_API_KEY;
	}

}
