package com.protostar.billingnstock.document;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.protostar.billingnstock.stock.entities.StockItemTypeEntity;
import com.protostar.billingnstock.stock.services.StockManagementService;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billingnstock.warehouse.services.WarehouseService;

/**
 * Servlet implementation class ExcelStockUpload
 */
public class ExcelStockUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExcelStockUpload() {
		super();
		// TODO Auto-generated constructor stub
	}

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("hi i m in servlet ");
		this.doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
		List<BlobKey> blobKeys = blobs.get("myFile");

		Long WareHouseID = null;
		Long bizID = null;
		System.out.println("blobKeys:" + blobKeys);
		blobKeys.get(0).getKeyString();
		String[] split2 = null;
		try {
			ServletFileUpload upload = new ServletFileUpload();
			// res.setContentType("text/plain");

			FileItemIterator iterator = upload.getItemIterator(request);
			while (iterator.hasNext()) {

				FileItemStream item = iterator.next();
				// InputStream stream = item.openStream();
				BlobstoreInputStream stream = new BlobstoreInputStream(new BlobKey(blobKeys.get(0).getKeyString()));
				if (item.isFormField()) {
					System.out.println("Got a form field: " + item.getFieldName());
					if (item.getFieldName().equals("WareHouseID")) {
						System.out.println("warehouse id==" + request.getParameter(item.getFieldName()));
						if (!request.getParameter(item.getFieldName()).equals(""))
							WareHouseID = Long.parseLong(request.getParameter(item.getFieldName()));
					}
					if (item.getFieldName().equals("bizID")) {
						System.out.println("bizID id==" + request.getParameter(item.getFieldName()));
						if (!request.getParameter(item.getFieldName()).equals(""))
							bizID = Long.parseLong(request.getParameter(item.getFieldName()));
					}
				} else {

					int len;
					byte[] fileContent = new byte[2000000]; // Can handle files
															// upto 20 MB

					int read = stream.read(fileContent);
					// System.out.println("No of bytes read:" + read);
					while ((len = stream.read(fileContent, 0, fileContent.length)) != -1) {
						// res.getOutputStream().write(fileContent, 0, len);
					}
					System.out.println("File content is : " + new String(fileContent));
					System.out.println("File Read is Done!!");
					// Write code here to parse sheet of patients and upload to
					// database

					String fileAsString = new String(fileContent);
					// System.out.println("fileContent:" + fileAsString);
					// List<PatientInfo> patientInfoList = new
					// ArrayList<PatientInfo>();

					split2 = fileAsString.split("\n");
					// Start with 1 not 0, zero holds column headings

				}

			}

			try {
				// gte business entity
				UserService us = new UserService();
				WarehouseService wc = new WarehouseService();
				WarehouseEntity we;
				if (WareHouseID != null) {
					we = wc.getWarehouseById(WareHouseID);
				} else {
					// check default is allready exits
					we = wc.getDefaultWarehouseByBizId(bizID);
				}
				StockManagementService ss = new StockManagementService();

				for (int row = 1; row < split2.length; row++) {

					String[] split = split2[row].split(",");
					if (split == null || split.length < 6) {
						continue;
					}
					System.out.println(" Row: " + row);
					System.out.println(" Col1: " + split[0]);
					System.out.println(" Col2: " + split[1]);
					System.out.println(" Col3: " + split[2]);
					System.out.println(" Col4: " + split[3]);
					System.out.println(" Col5: " + split[4]);

					// insert stocks

					StockItemTypeEntity stockItemType = new StockItemTypeEntity();

					stockItemType.setItemName(split[0]);
					stockItemType.setTags(split[1]);
					/*
					 * si.setQty(Integer.parseInt(split[2]));
					 * si.setPrice(Double.parseDouble(split[3]));
					 * si.setThresholdValue(Integer.parseInt(split[4]));
					 * si.setBusiness(getbusinessById); si.setWarehouse(we);
					 */
					ss.addStockItemType(stockItemType);
					Thread.sleep(2000);

				}

				response.sendRedirect("/#/stock/stockItemList");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			response.getOutputStream().print("File Uploading Failed!" + e.getMessage());
		}

	}

	private int parse(String string) {
		// TODO Auto-generated method stub
		return 0;
	}

}
