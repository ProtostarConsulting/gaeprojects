package com.protostar.billnstock.until.data;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.protostar.billingnstock.user.services.UserLoginService;

public class BusinessNSFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;

		// Move to namespace based datastore once moved to angular 2/4 +
		// endpoint 2.0
		// System.out.println("####BusinessNSFilter filter is invoked before");
		String accessToken = WebUtil.getAccessToken(request);
		UserLoginService userLoginService = new UserLoginService();
		if (accessToken != null && userLoginService.getCurrentUser(accessToken) != null) {
			//Temp disabling loign check
			WebUtil.setCurrentUser(userLoginService.getCurrentUserSession(accessToken));
		} else {
			// throw new RuntimeException("User is not logged in. Please login
			// first.");
			System.out.println("ERROR: RuntimeException- User is not logged in. Please login first.");
		}

		

		chain.doFilter(req, resp);// sends request to next resource
		// System.out.println("#####BusinessNSFilter filter is invoked
		// after.....................");
	}

	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("####BusinessNSFilter init......");
	}
}
