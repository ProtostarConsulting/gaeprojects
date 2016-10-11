package com.protostar.billnstock.service;

public class UploadFileInfo {
	String fileName;
	byte[] fileContent;
	private String contentType;
	private Long bizID;

	public UploadFileInfo() {

	}
	public UploadFileInfo(String fileName, byte[] fileContent) {
		this.fileName = fileName;
		this.fileContent = fileContent;
	}	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	public Long getBizID() {
		return bizID;
	}
	public void setBizID(Long bizID) {
		this.bizID = bizID;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}