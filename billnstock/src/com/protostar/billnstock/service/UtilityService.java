package com.protostar.billnstock.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UtilityService {

	private final static Logger log = Logger.getLogger(UtilityService.class.getName());

	public static String getNextPRN(String role) {
		Calendar rightNow = Calendar.getInstance();

		if (role == null || role.isEmpty()) {
			throw new RuntimeException("Role must not be null or empty");
		}

		int cy = rightNow.get(Calendar.YEAR);
		return role.toUpperCase().charAt(0) + "-" + cy + "-" + String.format("%05d", getCurrentYearNextCounter(cy));
	}

	private static Long getCurrentYearNextCounter(final long cy) {

		/*
		 * final Key<YearCounterEntity> eKey;
		 * 
		 * List<YearCounterEntity> list = ofy().load()
		 * .type(YearCounterEntity.class).filter("year", cy).list();
		 * 
		 * if (list == null || list.isEmpty()) { YearCounterEntity yc = new
		 * YearCounterEntity(); yc.setYear(cy); yc.setCurrentCounter(1L); eKey =
		 * ofy().save().entity(yc).now(); } else { YearCounterEntity yc =
		 * list.get(0); eKey = Key.create(yc); }
		 * 
		 * // If you don't need to return a value, you can use VoidWork
		 * YearCounterEntity yc = ofy().transact(new Work<YearCounterEntity>() {
		 * public YearCounterEntity run() { YearCounterEntity yc =
		 * ofy().load().key(eKey).now();
		 * yc.setCurrentCounter(yc.getCurrentCounter() + 1);
		 * Key<YearCounterEntity> now = ofy().save().entity(yc).now(); return
		 * yc; } });
		 * 
		 * return yc.getCurrentCounter();
		 */

		return 0L;
	}

	public static Map<String, String> getMultiPartFileItemsWithFileAsString(HttpServletRequest request)
			throws IOException {
		try {
			if (request.getHeader("Content-Type") != null
					&& request.getHeader("Content-Type").startsWith("multipart/form-data")) {

				ServletFileUpload upload = new ServletFileUpload();
				FileItemIterator iterator = upload.getItemIterator(request);
				Map<String, String> items = new HashMap<String, String>();
				while (iterator.hasNext()) {
					FileItemStream next = iterator.next();
					items.put(next.getFieldName(), UtilityService.readAsString(next.openStream()));

					/*
					 * if (next.getName() == null)
					 * items.put(next.getFieldName(), next.openStream()); else
					 * items.put(next.getName(), next.openStream());
					 */

				}
				return items;
			}
		} catch (Exception e) {
			log.severe(e.getMessage());
			e.printStackTrace();
			throw new IOException("Error Occurred while uploading the csv file.", e);
		}

		return null;

	}

	public static Map<String, Object> getMultiPartFileItemsWithFileAsBytes(HttpServletRequest request)
			throws IOException {
		try {

			if (request.getHeader("Content-Type") != null
					&& request.getHeader("Content-Type").startsWith("multipart/form-data")) {

				ServletFileUpload upload = new ServletFileUpload();
				FileItemIterator iterator = upload.getItemIterator(request);
				Map<String, Object> items = new HashMap<String, Object>();
				while (iterator.hasNext()) {
					FileItemStream next = iterator.next();

					if ("file".equalsIgnoreCase(next.getFieldName())) {
						UploadFileInfo uploadFileInfo = new UploadFileInfo(next.getName(),
								UtilityService.readAsBytes(next.openStream()));
						uploadFileInfo.setContentType(next.getContentType());
						items.put(next.getFieldName(), uploadFileInfo);
					} else
						items.put(next.getFieldName(), UtilityService.readAsString(next.openStream()));

				}
				return items;
			}
		} catch (Exception e) {
			log.severe(e.getMessage());
			e.printStackTrace();
			throw new IOException("Error Occurred while uploading the csv file.", e);
		}

		return null;

	}

	public static byte[] readAsBytes(InputStream stream) throws IOException {
		byte[] fileContent = new byte[2000000];
		// Can handle files upto 2 MB

		// int read = stream.read(fileContent);
		// log.fine("No of bytes read:" + read);
		while (stream.read(fileContent, 0, fileContent.length) != -1) {
			// res.getOutputStream().write(fileContent, 0, len);
		}
		stream.close();
		log.info("File Read is Done!!. Bytes read: " + fileContent.length);
		// Write code here to parse sheet of patients and upload to
		// database

		return fileContent;
	}

	public static String readAsString(InputStream stream) throws IOException {
		byte[] fileContent = new byte[2000000];
		// Can handle files upto 2 MB

		// int read = stream.read(fileContent);
		// log.fine("No of bytes read:" + read);
		while (stream.read(fileContent, 0, fileContent.length) != -1) {
			// res.getOutputStream().write(fileContent, 0, len);
		}
		stream.close();
		log.info("File Read is Done!!. Bytes read: " + fileContent.length);
		// Write code here to parse sheet of patients and upload to
		// database

		String fileAsString = new String(fileContent);
		return fileAsString;
	}

	public static String trimForCSV(String val) {
		if (val == null)
			return "";

		val = val.replace("\n", "").replace("\r", "").trim();
		val = val.replace(',', '-');
		return val.trim();
	}

	public static String getCurrentAppURL() {
		String hostUrl;
		String environment = System.getProperty("com.google.appengine.runtime.environment");
		if ("Production".equalsIgnoreCase(environment)) {
			String applicationId = System.getProperty("com.google.appengine.application.id");
			//String version = System.getProperty("com.google.appengine.application.version");
			hostUrl = "https://" + applicationId + ".appspot.com/";
		} else {
			hostUrl = "http://localhost:8888";
		}
		return hostUrl;
	}
}
