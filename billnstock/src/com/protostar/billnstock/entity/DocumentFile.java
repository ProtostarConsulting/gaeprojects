package com.protostar.billnstock.entity;

import com.protostar.billnstock.until.data.Constants.DocumentType;

public class DocumentFile {

	private String documentName;
	private String documentDesc;
	private DocumentType docType = null;
	private String gcsUrl;

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getGcsUrl() {
		return gcsUrl;
	}

	public void setGcsUrl(String gcsUrl) {
		this.gcsUrl = gcsUrl;
	}

	public DocumentType getDocType() {
		return docType;
	}

	public void setDocType(DocumentType docType) {
		this.docType = docType;
	}

	public String getDocumentDesc() {
		return documentDesc;
	}

	public void setDocumentDesc(String documentDesc) {
		this.documentDesc = documentDesc;
	}
}
