package com.protostar.billnstock.until.data;

import javax.servlet.http.HttpServletRequest;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import com.protostar.billingnstock.user.services.UserLoginService;

public class UserAuthenticator implements Authenticator {

	@Override
	public User authenticate(HttpServletRequest request) {
		String accessToken = WebUtil.getAccessToken(request);
		if (accessToken == null || accessToken.trim().isEmpty())
			return null;

		UserLoginService userLoginService = new UserLoginService();
		return userLoginService.getCurrentUser(accessToken);
	}
}