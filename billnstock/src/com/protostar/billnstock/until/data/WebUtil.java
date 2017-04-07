package com.protostar.billnstock.until.data;

import javax.servlet.http.HttpServletRequest;

import com.protostar.billingnstock.user.services.CurrentUserSession;

public class WebUtil {

	private static final ThreadLocal<CurrentUserSession> currentUser;
	static {
		currentUser = new ThreadLocal<CurrentUserSession>();
	}

	public static synchronized void setCurrentUser(CurrentUserSession user) {
		currentUser.set(user);
	}

	public static synchronized CurrentUserSession getCurrentUser() {
		return currentUser.get();
	}

	public static String getAccessToken(HttpServletRequest request) {
		String accessToken = null;
		String authorizationHeader = request.getHeader("Authorization");

		// System.out.println("authorizationHeader: " + authorizationHeader);
		if (authorizationHeader != null) {
			String[] split = authorizationHeader.split(" ");
			if (split.length > 1) {
				// System.out.println("split[0]: " + split[0]);
				// System.out.println("split[1]: " + split[1]);
				accessToken = split[1];
			}
		}

		return accessToken;
	}
}
