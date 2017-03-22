package com.protostar.billingnstock.account.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.account.entities.AccountEntryEntity;

public class DownloadGroupViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(DownloadAccountsServlet.class.getName());

	public DownloadGroupViewServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		AccountEntryService accountEntryService = new AccountEntryService();
		AccountingService accountEntityService =new AccountingService();
		Date date = new Date();
		String DATE_FORMAT = "dd-MMM-yyyyy  hh:mm:ss a";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Long actualFromDate= Long.parseLong(request.getParameter("fromDate"));
		Long actualtoDate= Long.parseLong(request.getParameter("toDate"));
		Long businessId = Long.parseLong(request.getParameter("id"));
		Long groupId = Long.parseLong(request.getParameter("groupId"));
		TimeZone timeZone = TimeZone.getTimeZone("IST");
		sdf.setTimeZone(timeZone);
		
	
	List<AccountEntity> list=accountEntityService.getGroupViewtByGroupId(businessId,groupId);
	
	
		OutputStream out = null;
		try {
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition",
					"attachment; filename=Group_View_Data" + sdf.format(date) + ".csv");
		
			
			ServletOutputStream outputStream = response.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);
			writer.append("No");
			writer.append(',');
			writer.append("account Name");
			writer.append(',');
			writer.append("	Total Debit");
			writer.append(',');
			writer.append("	Total Credit");
			writer.append(',');
			writer.append("Balance");
			writer.append(',');
			writer.append(System.lineSeparator());
			for(int j=0;j<list.size();j++){
				double totalDebit=0;
				double totalCredit=0;
				List<AccountEntryEntity> accEntryEntityList= accountEntryService.getAccountViewEntryByAccountId(actualFromDate,actualtoDate,list.get(j).getId(),businessId);

			for (int i = 0; i < accEntryEntityList.size(); i++) {
				
				if (accEntryEntityList.get(i).getDebit() != null) {

					totalDebit=totalDebit+accEntryEntityList.get(i).getDebit();
				} else {
					totalDebit=totalDebit+0;
				}
				if (accEntryEntityList.get(i).getCredit() != null) {

					totalCredit=totalCredit+accEntryEntityList.get(i).getCredit();
				} else {
					totalCredit=totalCredit+0;
				}
			}
								
					try {
						writer.append("" + (j + 1));
						writer.append(',');
						writer.append(accEntryEntityList.get(j).getAccountEntity().getAccountName().toString());
						writer.append(',');
						writer.append(""+totalDebit);
						writer.append(',');
						writer.append(""+totalCredit);
						writer.append(',');
						writer.append(System.lineSeparator());
						
					} catch (Exception e) {
						log.warning(e.getMessage());
						e.printStackTrace();
					}
					}
			writer.close();
		} catch (Exception e) {
			log.severe(e.getMessage());
			e.printStackTrace();
			throw new ServletException("Error Occurred while downloading the csv file.", e);
		} finally {
			if (out != null)
				out.close();
		}
		
	}
	}


