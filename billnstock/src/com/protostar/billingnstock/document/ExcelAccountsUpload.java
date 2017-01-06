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
import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.account.services.AccountService;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.cust.services.CustomerService;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billnstock.entity.Address;
import com.twilio.sdk.resource.instance.Account;

/**
 * Servlet implementation class ExcelAccountsUpload
 */
public class ExcelAccountsUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExcelAccountsUpload() {
		super();
		// TODO Auto-generated constructor stub
	}

	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("hi i m in servlet get account");
		this.doGet(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

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
				BlobstoreInputStream stream = new BlobstoreInputStream(
						new BlobKey(blobKeys.get(0).getKeyString()));
				if (item.isFormField()) {
					if (item.getFieldName().equals("bizID")) {
						System.out.println("bizID id=="
								+ request.getParameter(item.getFieldName()));
						if (!request.getParameter(item.getFieldName()).equals(
								""))
							bizID = Long.parseLong(request.getParameter(item
									.getFieldName()));
					}
				} else {

					int len;
					byte[] fileContent = new byte[2000000]; // Can handle files
															// upto 20 MB

					int read = stream.read(fileContent);
					// System.out.println("No of bytes read:" + read);
					while ((len = stream.read(fileContent, 0,
							fileContent.length)) != -1) {
						// res.getOutputStream().write(fileContent, 0, len);
					}
					System.out.println("File content is : "
							+ new String(fileContent));
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
				BusinessEntity getbusinessById = us.getBusinessById(bizID);

				for (int row = 1; row < split2.length; row++) {

					String[] split = split2[row].split(",");
					if (split == null || split.length < 4) {
						continue;
					}
					System.out.println(" Row: " + row);
					System.out.println(" Col1: " + split[0]);
					System.out.println(" Col2: " + split[1]);
					System.out.println(" Col3: " + split[2]);

					// insert customer
					AccountEntity acc = new AccountEntity();
					acc.setBusiness(getbusinessById);
					acc.setAccountName(split[0]);
					acc.setAccountNo(split[1].trim());
					acc.setDescription(split[2]);
					AccountService accser = new AccountService();
					accser.addAccount(acc);

				}
				response.sendRedirect("/#/account/accountList");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			response.getOutputStream().print(
					"File Uploading Failed!" + e.getMessage());
		}

	}
}
