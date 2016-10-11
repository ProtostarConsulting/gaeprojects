package com.protostar.billnstock.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billnstock.service.UtilityService;

public class UploadUsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(UploadUsersServlet.class
			.getName());

	public UploadUsersServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		log.info("In suide UploadUsersServlet....");
		this.doGet(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String[] split2 = null;

		Map<String, String> items  = UtilityService
				.getMultiPartFileItemsWithFileAsString(request);
		if(items == null || items.isEmpty()){
			return;
		}		
		
	
		String fileAsString = items.get("file").toString();
		log.info("fileAsString: " + fileAsString);
		String businessIdRequestParamValue = items.get("businessId").toString();
		Long businessId = businessIdRequestParamValue == null ? null : Long
				.parseLong(businessIdRequestParamValue.trim());

		log.info("businessId: " + businessId);
		String usernameRequestParamValue = items.get("username").toString();
		String loggedInUser = usernameRequestParamValue == null ? null
				: usernameRequestParamValue.trim();
		log.info("loggedInUser: " + loggedInUser);

		

		split2 = fileAsString.split("\n");
		log.info("split2.length: " + split2.length);
		UserService userService = new UserService();
		BusinessEntity businessEntity = userService.getBusinessById(businessId);
		Date todaysDate = new Date();
		// Start from 1 so that column headers are scriped.
		for (int row = 1; row < split2.length; row++) {

			try {
				String[] split = split2[row].split(",");
				if (split == null || split.length < 5) {
					continue;
				}

				UserEntity userEntity = new UserEntity();
				userEntity.setBusiness(businessEntity);
				userEntity.setCreatedDate(todaysDate);
				userEntity.setModifiedBy(loggedInUser);

				userEntity.setFirstName(split[0].trim());
				userEntity.setLastName(split[1].trim());
				userEntity.setEmail_id(split[2].trim());
				userEntity
						.setIsGoogleUser("1".equalsIgnoreCase(split[3].trim()));
				userEntity.setPassword(split[4].trim());
				ofy().save().entity(userEntity).now();

				log.info("Processed userEntity.getFirstName(): "
						+ userEntity.getFirstName());
			} catch (Exception e) {
				log.warning(e.getMessage());
				e.printStackTrace();
			}

		}
	}

	protected void doGetBackup(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			if (request.getHeader("Content-Type") != null
					&& request.getHeader("Content-Type").startsWith(
							"multipart/form-data")) {
				ServletFileUpload upload = new ServletFileUpload();

				FileItemIterator iterator = upload.getItemIterator(request);
				String[] split2 = null;
				Long businessId = 0L;
				String loggedInUser = "";
				while (iterator.hasNext()) {
					FileItemStream item = iterator.next();
					log.fine("item.getFieldName(): " + item.getFieldName());

					if (item.getName() == null) {
						// It is form field not file.

						if ("businessId".equalsIgnoreCase(item.getFieldName())) {
							businessId = Long.parseLong(UtilityService
									.readAsString(item.openStream()));

						}

						if ("username".equalsIgnoreCase(item.getFieldName())) {
							loggedInUser = UtilityService.readAsString(item
									.openStream());

						}

						continue;
					}
					InputStream openStream = item.openStream();
					int len = 0;
					byte[] fileContent = new byte[2000000];
					// Can handle files upto 20 MB

					int read = openStream.read(fileContent);
					// log.fine("No of bytes read:" + read);
					while ((len = openStream.read(fileContent, 0,
							fileContent.length)) != -1) {
						// res.getOutputStream().write(fileContent, 0, len);
					}
					openStream.close();
					log.info("File Read is Done!!");
					// Write code here to parse sheet of patients and upload to
					// database

					String fileAsString = new String(fileContent);

					split2 = fileAsString.split("\n");

				}

				UserService userService = new UserService();
				BusinessEntity businessEntity = userService
						.getBusinessById(businessId);
				Date todaysDate = new Date();
				// Start from 1 so that column headers are scriped.
				for (int row = 1; row < split2.length; row++) {

					try {
						String[] split = split2[row].split(",");
						if (split == null || split.length < 5) {
							continue;
						}

						UserEntity userEntity = new UserEntity();
						userEntity.setBusiness(businessEntity);
						userEntity.setCreatedDate(todaysDate);
						userEntity.setModifiedBy(loggedInUser);

						userEntity.setFirstName(split[0].trim());
						userEntity.setLastName(split[1].trim());
						userEntity.setEmail_id(split[2].trim());
						userEntity.setIsGoogleUser("1"
								.equalsIgnoreCase(split[3].trim()));
						userEntity.setPassword(split[4].trim());
						ofy().save().entity(userEntity).now();

						log.fine("Processed userEntity.getFirstName(): "
								+ userEntity.getFirstName());
					} catch (Exception e) {
						log.warning(e.getMessage());
						e.printStackTrace();
					}

				}
			}
		} catch (Exception e) {
			log.severe(e.getMessage());
			e.printStackTrace();
			throw new ServletException(
					"Error Occurred while uploading the csv file.", e);
		}
	}

}
