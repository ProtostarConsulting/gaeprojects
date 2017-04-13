package com.protostar.billnstock.until.async;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadPartnerSchools
 */
public class FlushUserLoginCacheHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FlushUserLoginCacheHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("in side FlushUserLoginCacheHandler");
		// Long insId = Long.parseLong(request.getParameter("InstituteId"));

		/*PartnerSchoolService schoolService = new PartnerSchoolService();
		System.out
				.println("UpdateSchoolStudentCountHandler: started count udpate job: "
						+ new Date());
		schoolService.updateCurrentYearSchoolAndStudentCount();*/
		System.out
				.println("FlushUserLoginCacheHandler: finished count udpate job: "
						+ new Date());
		System.out
				.println("getFinSummayReportData: started job: " + new Date());
		//schoolService.getFinSummayReportData(null);
		System.out.println("getFinSummayReportData: ended job: " + new Date());

	}

}
