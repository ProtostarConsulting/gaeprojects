package com.protostar.billnstock.until.data;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class BusinessNSFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		// Move to namespace based datastore once moved to angular 2/4 +
		// endpoint 2.0
		System.out.println("####BusinessNSFilter filter is invoked before");
		String url = null;
		String queryString = null;
		if (request instanceof HttpServletRequest) {
			url = ((HttpServletRequest) request).getRequestURL().toString();
			queryString = ((HttpServletRequest) request).getQueryString();
		}
		System.out.println(url + "?" + queryString);
		User currentUser = UserServiceFactory.getUserService().getCurrentUser();
		if (currentUser != null) {
			System.out.println("currentUser.getEmail():" + currentUser.getEmail());
			System.out.println("currentUser.getNickname():" + currentUser.getNickname());
		}

		chain.doFilter(request, resp);// sends request to next resource
		System.out.println("#####BusinessNSFilter filter is invoked after.....................");
	}

	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("####BusinessNSFilter init......");
	}
}
