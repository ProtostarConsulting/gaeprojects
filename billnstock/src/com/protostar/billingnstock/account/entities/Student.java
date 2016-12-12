package com.protostar.billingnstock.account.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity
public class Student {
	
	@Id
	private Long id;
	private String studName;
	private int  studRollNum;
	@Index
	private String studCol;
	private String studBranch;
	public String getStudName() {
		return studName;
	}
	public void setStudName(String studName) {
		this.studName = studName;
	}
	public int getStudRollNum() {
		return studRollNum;
	}
	public void setStudRollNum(int studRollNum) {
		this.studRollNum = studRollNum;
	}
	public String getStudCol() {
		return studCol;
	}
	public void setStudCol(String studCol) {
		this.studCol = studCol;
	}
	public String getStudBranch() {
		return studBranch;
	}
	public void setStudBranch(String studBranch) {
		this.studBranch = studBranch;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
