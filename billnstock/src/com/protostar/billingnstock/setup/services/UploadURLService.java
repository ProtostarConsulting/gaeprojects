package com.protostar.billingnstock.setup.services;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.protostar.billnstock.until.data.ServerMsg;

//import com.protostar.prostudy.entity.BookEntity;

@Api(name = "uploadUrlService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.setup.services", ownerName = "com.protostar.billingnstock.setup.services", packagePath = ""))
public class UploadURLService {

	
	@ApiMethod(name = "getLogUploadURL",path="getLogUploadURL")
	public ServerMsg getLogUploadURL() {
		 BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		 String createUploadUrl = blobstoreService.createUploadUrl("/UploadLogo");
		 ServerMsg serverMsg = new ServerMsg();
		 serverMsg.setMsg(createUploadUrl);		 
		return serverMsg;
	}
	
	@ApiMethod(name = "getLogUploadFooterURL",path="getLogUploadFooterURL")
	public ServerMsg getLogUploadFooterURL() {
		 BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		 String createUploadUrl = blobstoreService.createUploadUrl("/UploadFooter");
		 ServerMsg serverMsg = new ServerMsg();
		 serverMsg.setMsg(createUploadUrl);		 
		return serverMsg;
	}
	@ApiMethod(name = "getExcelUploadURL",path="getExcelUploadURL")
	public ServerMsg getExcelUploadURL() {
		 BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		 String createUploadUrl = blobstoreService.createUploadUrl("/UploadExcelSheetAddUsers");
		 ServerMsg serverMsg = new ServerMsg();
		 serverMsg.setMsg(createUploadUrl);		 
		return serverMsg;
	}
	@ApiMethod(name = "getExcelStockUploadURL",path="getExcelStockUploadURL")
	public ServerMsg getExcelStockUploadURL() {
		 BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		 String createUploadUrl = blobstoreService.createUploadUrl("/ExcelStockUpload");
		 ServerMsg serverMsg = new ServerMsg();
		 serverMsg.setMsg(createUploadUrl);		 
		return serverMsg;
	}
	
	@ApiMethod(name = "getCustomerExcelUploadURL",path="getCustomerExcelUploadURL")
	public ServerMsg getCustomerExcelUploadURL() {
		 BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		 String createUploadUrl = blobstoreService.createUploadUrl("/ExcelCustomerUpload");
		 ServerMsg serverMsg = new ServerMsg();
		 serverMsg.setMsg(createUploadUrl);		 
		return serverMsg;
	}

	
	
	@ApiMethod(name = "getAccountExcelUploadURL",path="getAccountExcelUploadURL")
	public ServerMsg getAccountExcelUploadURL() {
		 BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		 String createUploadUrl = blobstoreService.createUploadUrl("/ExcelAccountsUpload");
		 ServerMsg serverMsg = new ServerMsg();
		 serverMsg.setMsg(createUploadUrl);		 
		return serverMsg;
	}

	
}