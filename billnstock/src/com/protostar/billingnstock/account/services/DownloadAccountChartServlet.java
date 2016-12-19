package com.protostar.billingnstock.account.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.account.services.AccountGroupService.TypeInfo;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

/**
 * Servlet implementation class DownloadAccountChartServlet
 */
public class DownloadAccountChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String HTML = "app/accounting/accountChart.html";
	public class GroupTypeObject
	{
		String groupType;
		ArrayList<AccountGroupEntity> groupList= new ArrayList<AccountGroupEntity>();
		public String getGroupType() {
			return groupType;
		}
		public void setGroupType(String groupType) {
			this.groupType = groupType;
		}		
	
		public ArrayList<AccountGroupEntity> getGroupList() {
			return groupList;
		}
		public void setGroupList(ArrayList<AccountGroupEntity> groupList) {
			this.groupList = groupList;
		}
	}
	public class GroupAccObject
	{
		AccountGroupEntity groupObj;
		ArrayList<AccountEntity> accountList=new ArrayList<AccountEntity>();
		public AccountGroupEntity getGroupObj() {
			return groupObj;
		}
		public ArrayList<AccountEntity> getAccountList() {
			return accountList;
		}
		public void setGroupObj(AccountGroupEntity groupObj) {
			this.groupObj = groupObj;
		}
		
		public void setAccountList(ArrayList<AccountEntity> accountList) {
			this.accountList = accountList;
		}
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadAccountChartServlet() {
		super();
		// TODO Auto-generated constructor stub

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Document document = new Document();

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

List<TypeInfo> accountChart = accountgroup.getChartSheet(bid);
pdfHtmlTemplateService.generatePdfAccountChart(accountChart,
		outputStream,bid);
		
		
		
	
	}
}
