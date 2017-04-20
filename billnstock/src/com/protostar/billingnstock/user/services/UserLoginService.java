package com.protostar.billingnstock.user.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.server.spi.auth.common.User;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.WebUtil;

public class UserLoginService {

	private final Logger logger = Logger.getLogger(UserLoginService.class.getName());

	public void recordUserLogin(UserEntity user) {

		logger.info("Creating CurrentUserSession and UserLoginRecord Records...");
		CurrentUserSession currentUserSession = new CurrentUserSession();
		currentUserSession.setBusiness(user.getBusiness());
		currentUserSession.setCreatedBy(user);
		currentUserSession.setNote(user.getEmail_id());
		ofy().save().entity(currentUserSession).now();

		user.setAccessToken(EntityUtil.getUserWebSafeKey(currentUserSession));
		WebUtil.setCurrentUser(currentUserSession);

		UserLoginRecord userLoginRecord = new UserLoginRecord();
		userLoginRecord.setAccessToken(user.getAccessToken());
		userLoginRecord.setBusiness(user.getBusiness());
		userLoginRecord.setNote(user.getEmail_id());
		userLoginRecord.setCreatedBy(user);
		// save async
		ofy().save().entity(userLoginRecord).now();

	}

	public void recordUserLogout(String accessToken) {
		CurrentUserSession currentUserSession = getCurrentUserSession(accessToken);
		logger.info("Logging out... : " + currentUserSession);
		if (currentUserSession != null) {
			logger.info("Logging out... currentUserSession.getUser().getEmail_id(): "
					+ currentUserSession.getUser().getEmail_id());
			ofy().delete().entity(currentUserSession).now();

			UserLoginRecord userLoginRecord = (UserLoginRecord) ofy().load().type(UserLoginRecord.class)
					.ancestor(Key.create(BusinessEntity.class, currentUserSession.getBusiness().getId()))
					.filter("accessToken", accessToken).first().now();

			if (userLoginRecord != null) {
				userLoginRecord.setModifiedDate(new Date());
				// treat this as logout datetime
				ofy().save().entity(userLoginRecord).now();
				logger.info("Uupdate out... Updated UserLoginRecord: " + userLoginRecord.getModifiedDate());
			}
		}
	}

	public User getCurrentUser(String accessToken) {
		CurrentUserSession currentUserSession = getCurrentUserSession(accessToken);
		if (currentUserSession != null)
			return new User(accessToken, currentUserSession.getCreatedBy().getEmail_id());
		else
			return null;
	}

	public CurrentUserSession getCurrentUserSession(String accessToken) {
		CurrentUserSession currentUserSession = (CurrentUserSession) ofy().load().key(Key.create(accessToken)).now();

		return currentUserSession;
	}

	public List<UserEntity> login(String email, String pass) {
		List<UserEntity> list = null;
		if (email.contains("@")) {
			list = ofy().load().type(UserEntity.class).filter("email_id", email).filter("isActive", true)
					.order("createdDate").list();
		} else if (email.length() == 10) {
			list = ofy().load().type(UserEntity.class).filter("employeeDetail.phone1", email).filter("isActive", true)
					.order("createdDate").list();
		} else if (isInteger(email)) {
			list = ofy().load().type(UserEntity.class).filter("employeeDetail.empId", Integer.parseInt(email))
					.filter("isActive", true).order("createdDate").list();
		}

		if (list != null & list.size() > 0) {
			// Login will be checked against first/the created first. Notice
			// order by clause above
			UserEntity foundUser = list.get(0);
			if (foundUser.getIsLoginAllowed() && foundUser.getPassword() != null
					&& foundUser.getPassword().equalsIgnoreCase(pass)) {

				// save current session and history record
				foundUser.setLastLoginDate(new Date());
				recordUserLogin(foundUser);
				ofy().save().entity(foundUser);
				if (list.size() > 1) {
					for (UserEntity user : list) {
						user.setAccessToken(foundUser.getAccessToken());
					}
				}
				return list;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private static boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
