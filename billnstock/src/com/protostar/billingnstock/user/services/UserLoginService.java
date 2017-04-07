package com.protostar.billingnstock.user.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.auth.common.User;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.WebUtil;

public class UserLoginService {
	private final MemcacheService mc = MemcacheServiceFactory.getMemcacheService();

	public void recordUserLogin(UserEntity user) {
		CurrentUserSession currentUserSession = new CurrentUserSession();
		currentUserSession.setBusiness(user.getBusiness());
		currentUserSession.setCreatedBy(user);
		ofy().save().entity(currentUserSession).now();

		user.setAccessToken(EntityUtil.getUserWebSafeKey(currentUserSession));
		mc.put(user.getAccessToken(), currentUserSession);

		WebUtil.setCurrentUser(currentUserSession);

		UserLoginRecord userLoginRecord = new UserLoginRecord();
		userLoginRecord.setAccessToken(user.getAccessToken());
		userLoginRecord.setBusiness(user.getBusiness());
		userLoginRecord.setCreatedBy(user);
		// save async
		ofy().save().entity(userLoginRecord).now();
	}

	public void recordUserLogout(String accessToken) {
		CurrentUserSession currentUserSession = getCurrentUserSession(accessToken);
		if (currentUserSession != null) {
			ofy().delete().entity(currentUserSession).now();

			UserLoginRecord userLoginRecord = (UserLoginRecord) ofy().load().type(UserLoginRecord.class)
					.ancestor(Key.create(BusinessEntity.class, currentUserSession.getBusiness().getId()))
					.filter("accessToken", accessToken).first().now();

			if (userLoginRecord != null) {
				userLoginRecord.setModifiedDate(new Date());
				// treat this as logout datetime
				ofy().save().entity(userLoginRecord).now();
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
		CurrentUserSession currentUserSession = null;
		Object object = mc.get(accessToken);
		if (object == null) {
			// accessToken is websafekey
			currentUserSession = (CurrentUserSession) ofy().load().key(Key.create(accessToken)).now();
			mc.put(accessToken, currentUserSession);
		} else {
			currentUserSession = (CurrentUserSession) object;
		}

		return currentUserSession;
	}

	public List<UserEntity> login(String email, String pass) {
		List<UserEntity> list = null;
		if (email.contains("@")) {
			list = ofy().load().type(UserEntity.class).filter("email_id", email).order("createdDate").list();
		} else if (email.length() == 10) {
			list = ofy().load().type(UserEntity.class).filter("employeeDetail.phone1", email).list();
		} else if (isInteger(email)) {
			list = ofy().load().type(UserEntity.class).filter("employeeDetail.empId", Integer.parseInt(email)).list();
		}

		if (list != null & list.size() > 0) {
			// Login will be checked against first/the created first. Notice
			// order by clause above
			UserEntity foundUser = list.get(0);
			if (foundUser.getIsLoginAllowed() && foundUser.getPassword() != null
					&& foundUser.getPassword().equalsIgnoreCase(pass)) {

				// save current session and history record
				foundUser.setLastLoginDate(new Date());
				ofy().save().entity(foundUser);
				recordUserLogin(foundUser);
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
