package com.protostar.billingnstock.crm.entities;

import java.util.Date;

/*@Entity*/
public class Task {

	/*
	 * @Id private Long id;
	 * 
	 * public long getId() { return id; } public void setId(long id) { this.id =
	 * id; }
	 */

	// private String id ;
	private String tid;
	private String type;
	private Date date;
	private String note;
	private String status;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

}
