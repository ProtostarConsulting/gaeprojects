package com.protostar.billingnstock.user.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.taskmangement.TaskManagementService;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.ServerMsg;

@Api(name = "userService", version = "v0.1", clientIds = {
		Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
		Constants.API_EXPLORER_CLIENT_ID }, audiences = { Constants.ANDROID_AUDIENCE }, scopes = { Constants.EMAIL_SCOPE }, namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.user.services", ownerName = "com.protostar.billingnstock.user.services", packagePath = ""))
public class UserService {

	private final Logger logger = Logger.getLogger(TaskManagementService.class
			.getName());
	
	@ApiMethod(name = "addUser")
	public void addUser(UserEntity usr) throws MessagingException, IOException {
		usr.setCreatedDate(new Date());
		Key<UserEntity> now = ofy().save().entity(usr).now();
		int count;
		List<UserEntity> filtereduser = ofy()
				.load()
				.type(UserEntity.class)
				.ancestor(
						Key.create(BusinessEntity.class, usr.getBusiness()
								.getId())).list();
		count = filtereduser.size() + 1;

		BusinessEntity businessEntity = new BusinessEntity();
		businessEntity = usr.getBusiness();
		businessEntity.setTotalUser(count);
		ofy().save().entity(businessEntity).now();

		// Test Code to Send email to registered user

		/*
		 * User user = UserServiceFactory.getUserService().getCurrentUser();
		 * String recipientAddress = user.getEmail();
		 */
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String messageBody = "Welcome to Example! Your account has been created. "
				+ "You can edit your user profile by clicking the "
				+ "following link:\n\n"
				+ "http://www.example.com/profile/\n\n"
				+ "Let us know if you have any questions.\n\n"
				+ "The Example Team\n";
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(
					"ganesh.lawande@protostar.co.in", "ProERP"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					usr.getEmail_id()));
			message.setSubject("Welcome to Example.com!");
			message.setText(messageBody);
			Transport.send(message);
		} catch (AddressException e) {
			// An email address was invalid.
			// ...
			e.printStackTrace();
		} catch (MessagingException e) {
			// There was an error contacting the Mail service.
			// ...
			e.printStackTrace();
		}

		// Email Send Code Ends

	}

	@ApiMethod(name = "updateBusiStatus", path = "updateBusiStatus")
	public void updateBusiStatus(BusinessEntity businessEntity) {
		ofy().save().entity(businessEntity).now();
	}

	@ApiMethod(name = "getBusinessById")
	public BusinessEntity getBusinessById(@Named("id") Long id) {
		return ofy().load().type(BusinessEntity.class).id(id.longValue()).now();
	}

	@ApiMethod(name = "updateUser")
	public void updateUser(UserEntity usr) {
		usr.setModifiedDate(new Date());
		Key<UserEntity> now = ofy().save().entity(usr).now();
	}

	@ApiMethod(name = "getUserList")
	public List<UserEntity> getUserList() {
		return ofy().load().type(UserEntity.class).list();
	}

	@ApiMethod(name = "getUserByEmailID", path = "getUserByEmailID")
	public UserEntity getUserByEmailID(@Named("email_id") String email) {
		List<UserEntity> list = ofy().load().type(UserEntity.class)
				.filter("email_id", email).list();
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

	@ApiMethod(name = "getUserByID", path = "getUserByID")
	public UserEntity getUserByID(@Named("id") Long id) {
		UserEntity userE = ofy().load().type(UserEntity.class).id(id.longValue()).now();
		return userE;
	}

	@ApiMethod(name = "getBusinessByEmailID", path = "Somepath_realted_to_your_service")
	public BusinessEntity getBusinessByEmailID(
			@Named("adminEmailId") String emailid) {
		List<BusinessEntity> list = ofy().load().type(BusinessEntity.class)
				.filter("adminEmailId", emailid).list();
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

	@ApiMethod(name = "login")
	public UserEntity login(@Named("email_id") String email,
			@Named("password") String pass) {
		List<UserEntity> list = ofy().load().type(UserEntity.class)
				.filter("email_id", email).list();

		UserEntity foundUser = (list == null || list.size() == 0) ? null : list
				.get(0);
		if (foundUser != null) {
			if (foundUser.getPassword().equals(pass)) {
				return foundUser;
			} else {
				return null;

			}
		} else {
			return null;

		}

	}

	@ApiMethod(name = "addBusiness")
	public BusinessEntity addBusiness(BusinessEntity business) {
		Date date = new Date();
		String DATE_FORMAT = "dd/MM/yyyy";

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		business.setRegisterDate(sdf.format(date));

		Key<BusinessEntity> now = ofy().save().entity(business).now();
		return business;
	}

	@ApiMethod(name = "getUsersByBusinessId")
	public List<UserEntity> getUsersByBusinessId(@Named("id") Long id) {
		/*
		 * List<UserEntity> list = ofy().load().type(UserEntity.class)
		 * .ancestor(Key.create(UserEntity.class, id)).list();
		 */
		List<UserEntity> list = ofy().load().type(UserEntity.class).list();
		for (UserEntity user : list) {
			if (user.getId().longValue() == id.longValue()) {
				list.add(user);
			}
		}
		//logger.info("list:" + list);
		return list;
	}

	@ApiMethod(name = "isUserExists")
	public ServerMsg isUserExists(@Named("email_id") String emailID) {
		ServerMsg serverMsg = new ServerMsg();
		List<UserEntity> list = ofy().load().type(UserEntity.class)
				.filter("email_id", emailID).list();

		/* if(list.get(0).equals(null)){ */
		if (list.size() == 0) {
			serverMsg.setReturnBool(false);
		} else {
			serverMsg.setReturnBool(true);
		}

		return serverMsg;
	}

	@ApiMethod(name = "getBusinessList")
	public List<BusinessEntity> getBusinessList() {
		return ofy().load().type(BusinessEntity.class).list();
	}

	@ApiMethod(name = "getLogUploadURL")
	public ServerMsg getLogUploadURL() {
		BlobstoreService blobstoreService = BlobstoreServiceFactory
				.getBlobstoreService();
		String createUploadUrl = blobstoreService
				.createUploadUrl("/UploadServlet");
		ServerMsg serverMsg = new ServerMsg();
		serverMsg.setMsg(createUploadUrl);
		return serverMsg;
	}

}