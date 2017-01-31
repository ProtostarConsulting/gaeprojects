package com.protostar.billingnstock.account.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.account.services.AccountGroupService.TypeInfo;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

public class PandL extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PandL() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Long bid = Long.parseLong(request.getParameter("bid"));
		PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();
		response.setContentType("application/PDF");
		ServletOutputStream outputStream = response.getOutputStream();
		Date today = new Date();
		String fileNameAppend = today.getDay() + "_" + today.getMonth() + "_"
				+ today.getYear();
		response.setHeader("Content-disposition",
				"inline; filename='Downloaded_" + fileNameAppend + ".pdf'");
AccountGroupService accountgroup=new AccountGroupService();
AccountGroupEntity accGroupEntity=new AccountGroupEntity();

List<TypeInfo> PandL = accountgroup.getProfitAndLossAcc(bid);//BalanceSheet(bid);
pdfHtmlTemplateService.getProfitAndLossAcc(PandL,
		outputStream,bid);
		
		
		
	}
	
}
