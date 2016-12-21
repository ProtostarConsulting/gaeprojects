package com.protostar.billingnstock.invoice.entities;

import com.googlecode.objectify.annotation.Entity;
import com.protostar.billnstock.entity.BaseEntity;

@Entity
public class InvoiceSettingsEntity extends BaseEntity {

	private boolean showDefaultServiceItems = true;
	private boolean showDefaultProductItems = false;

	private String noteToCustomer;

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
}
