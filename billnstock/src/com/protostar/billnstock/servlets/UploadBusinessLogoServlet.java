package com.protostar.billnstock.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billnstock.service.UtilityService;
import com.protostar.billnstock.until.data.Constants;

public class UploadBusinessLogoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(UploadBusinessLogoServlet.class
			.getName());
	

	/**
	 * This is where backoff parameters are configured. Here it is aggressively
	 * retrying with backoff, up to 10 times but taking no more that 15 seconds
	 * total to do so.
	 */
	private final GcsService gcsService = GcsServiceFactory
			.createGcsService(new RetryParams.Builder()
					.initialRetryDelayMillis(10).retryMaxAttempts(10)
					.totalRetryPeriodMillis(15000).build());

	/**
	 * Used below to determine the size of chucks to read in. Should be > 1kb
	 * and < 10MB
	 */
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;

	public UploadBusinessLogoServlet() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * Transfer the data from the inputStream to the outputStream. Then close
	 * both streams.
	 */
	private void copy(InputStream input, OutputStream output)
			throws IOException {
		try {
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = input.read(buffer);
			while (bytesRead != -1) {
				output.write(buffer, 0, bytesRead);
				bytesRead = input.read(buffer);
			}
		} finally {
			input.close();
			output.close();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.info("###Inside UploadBusinessLogoServlet#doPost###");
		try {
			if (request.getHeader("Content-Type") != null
					&& request.getHeader("Content-Type").startsWith(
							"multipart/form-data")) {				
				Long businessId = 0l;
				ServletFileUpload upload = new ServletFileUpload();
				FileItemIterator iterator = upload.getItemIterator(request);
				
				GcsFilename fileName = null;
				GcsOutputChannel outputChannel;
				GcsFileOptions instance;

				while (iterator.hasNext()) {
					FileItemStream next = iterator.next();
					if (next.getName() == null) {
						if ("businessId".equalsIgnoreCase(next.getFieldName())) {
							String businessIdStr = UtilityService
									.readAsString(next.openStream());
							businessId = Long.parseLong(businessIdStr.trim());
						}
						continue;
					}

					/*
					 * if (next.getName() == null)
					 * items.put(next.getFieldName(), next.openStream()); else
					 * items.put(next.getName(), next.openStream());
					 */

					
					SimpleDateFormat sdf = new SimpleDateFormat(
							"YYYY-MM-dd-HHmmssSSS");
					String dtString = sdf.format(new Date());
					final String fileNameStr = dtString + next.getName().trim().toLowerCase();

					log.info("uploadFileInfo.getContentType().toLowerCase(): "
							+ next.getContentType().toLowerCase());
					fileName = new GcsFilename(
							Constants.PROERP_IMAGES_BUCKET, fileNameStr);
					instance = new GcsFileOptions.Builder()
							.mimeType(next.getContentType().toLowerCase())
							.acl("public-read").build();
					outputChannel = gcsService.createOrReplace(fileName,
							instance);
					copy(next.openStream(),
							Channels.newOutputStream(outputChannel));
				}
					UserService userService = new UserService();
					BusinessEntity businessEntity = userService
							.getBusinessById(businessId);

					if (businessEntity != null) {
						ImagesService imagesService = ImagesServiceFactory
								.getImagesService();

						// String servingUrl =
						// imagesService.getServingUrl(blobKey);
						String filename = String.format("/gs/%s/%s",
								fileName.getBucketName(),
								fileName.getObjectName());
						String servingUrl = imagesService
								.getServingUrl(ServingUrlOptions.Builder
										.withGoogleStorageFileName(filename));

						log.info("servingUrl: " + servingUrl);
						businessEntity.setBizLogoGCSURL(servingUrl);
						Key<BusinessEntity> now = ofy().save()
								.entity(businessEntity).now();
						// businessEntity.setModifiedDate(todaysDate);
						log.info("Logo Saved for the given business: " + now);
					}
				}
		

		} catch (Exception e) {
			log.severe(e.getMessage());
			e.printStackTrace();
			throw new ServletException(
					"Error Occurred while saving business Log File", e);
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.info("###Inside UploadBusinessLogoServlet#doGet###Nothing Here...");
		return;
	
	}

}
