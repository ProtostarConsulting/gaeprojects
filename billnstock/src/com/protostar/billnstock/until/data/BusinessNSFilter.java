package com.protostar.billnstock.until.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.protostar.billingnstock.user.services.CurrentUserSession;
import com.protostar.billingnstock.user.services.UserLoginService;

public class BusinessNSFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		List<String> publicURLList = new ArrayList<String>();
		publicURLList.add("/proxy.html");
		publicURLList.add("/discovery/v1/apis");		
		publicURLList.add("/_ah/api/discovery");
		publicURLList.add("/BackendService.getApiConfigs");
		publicURLList.add("/userService/v0.1/getUserByEmailID");
		publicURLList.add("/com.protostar.billingnstock.user.services.UserService.getUserByEmailID");
		publicURLList.add("/encoded_gs_key");
		
		//publicURLList.add("");
		
		//This should be removed. Find better way
		
		
		// Move to namespace based datastore once moved to angular 2/4 +
		// endpoint 2.0
		// System.out.println("####BusinessNSFilter filter is invoked before");
		String accessToken = WebUtil.getAccessToken(request);
		System.out.println("accessToken: " + accessToken);
		String url = request.getPathInfo();
		System.out.println("Url: " + url);
		boolean skipLoginChk = false;

		for (String skipUrl : publicURLList) {
			if (url == null || url.startsWith(skipUrl)) {
				skipLoginChk = true;
				break;
			}
		}

		if (skipLoginChk) {
			// This is public API/Page
			System.out.println("Skipping Login Check...");
		} else {
			UserLoginService userLoginService = new UserLoginService();
			if (accessToken != null
					&& userLoginService.getCurrentUser(accessToken) != null) {
				// Temp disabling loign check
				CurrentUserSession currentUserSession = userLoginService
								.getCurrentUserSession(accessToken);
				System.out.println("currentUserSession.getId(): "+ currentUserSession.getId());
				WebUtil.setCurrentUser(currentUserSession);
			} else {
				// throw new RuntimeException("User is not logged in. Please
				// login
				// first.");
				System.out
						.println("ERROR: RuntimeException- User is not logged in. Please login first.");
			}
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
