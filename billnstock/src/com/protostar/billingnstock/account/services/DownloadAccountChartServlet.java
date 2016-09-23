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
		response.setContentType("pdf");

		response.setHeader("Content-Disposition",
				"attachment; filename=AccountChartData_" + sdf.format(date)
						+ ".pdf");

		ServletOutputStream outputStream = response.getOutputStream();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			document.addTitle("AccountChart");
			document.addAuthor("Protostar");
			document.addCreationDate();				
			
			ArrayList accountGroupTypeList = new ArrayList();
			accountGroupTypeList.add("ASSETS");
			accountGroupTypeList.add("EQUITY");
			accountGroupTypeList.add("LIABILITIES");
			accountGroupTypeList.add("INCOME");
			accountGroupTypeList.add("EXPENSES");
			accountGroupTypeList.add("OTHERINCOMES");
			accountGroupTypeList.add("OTHEREXPENCES");

			Integer totalTypeCount = accountGroupTypeList.size();
			ArrayList<GroupTypeObject> accountGroupTypeGroupList=new ArrayList<GroupTypeObject>();		
			
			AccountGroupService accountGroupService=new AccountGroupService();
			AccountService accountService=new AccountService();
			
			ArrayList<AccountGroupEntity> groupList=new ArrayList<AccountGroupEntity>();
			List gList=new ArrayList<AccountGroupEntity>();
			
			for (int i = 0; i < accountGroupTypeList.size(); i++) {
				
				GroupTypeObject groupTypeObj=new GroupTypeObject ();
				groupTypeObj.setGroupType(accountGroupTypeList.get(i).toString());	
				
				//bid bid=new bid();
				
				
			//	ArrayList<AccountGroupEntity> groupList=new ArrayList<AccountGroupEntity>();
				gList=accountGroupService.getAccountGroupListByType(groupTypeObj.getGroupType(), 45654L);				
			
				
				groupTypeObj.setGroupList(groupList);
				accountGroupTypeGroupList.add(groupTypeObj);
				
				if (groupTypeObj.groupList !=null) {
					for (int j = 0; j < groupTypeObj.groupList.size(); j++) {
						
						System.out.println(groupTypeObj.groupList.get(j));
					//Long groupId=groupTypeObj.groupList.get(j);
						//accountService.getAccountListByGroupId(groupId);
						
								
					}
					
				}

				
			}	
			//System.out.println("accountGroupTypeGroupList"+ accountGroupTypeGroupList.get(0).getGroupType());
		
			document.add(new Paragraph("Account Chart"));
			//document.addHeader("Account Chart", "");
			PdfPTable pdfPTable = new PdfPTable(2);
		
			 // header row:
			pdfPTable.addCell("Accounts");
			pdfPTable.addCell("Value");
			pdfPTable.setHeaderRows(1);
			
			for (int i = 0; i < accountGroupTypeList.size(); i++)
			{
				pdfPTable.addCell(new PdfPCell(new Paragraph(accountGroupTypeList.get(i)+"")));
				pdfPTable.addCell(new PdfPCell(new Paragraph("Value")));
	
			}
			document.add(pdfPTable);

			// XMLWorkerHelper.getInstance().parseXHtml(writer, document,new
			// FileInputStream(HTML));

		} catch (Exception e) {
		}

		document.close();
	}

}
