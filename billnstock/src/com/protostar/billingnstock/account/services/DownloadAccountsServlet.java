package com.protostar.billingnstock.account.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.protostar.billingnstock.account.entities.AccountEntity;
import com.protostar.billingnstock.account.entities.AccountEntryEntity;

public class DownloadAccountsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(DownloadAccountsServlet.class.getName());
       
   
    public DownloadAccountsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		AccountEntryService accountEntryService=new AccountEntryService();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		
		Long businessId = Long.parseLong(request.getParameter("id"));
		Long searchId=Long.parseLong(request.getParameter("searchAccId"));
		Date fromDate=new Date(request.getParameter("fromDate"));
		Date toDate=new Date(request.getParameter("toDate"));
		List<AccountEntryEntity> accEntryEntityList = accountEntryService.getAccountEntryByAccountId(searchId,businessId);
		OutputStream out = null;
		try {
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition",
					"attachment; filename=Account_View_Data" + sdf.format(date)
							+ ".csv");
			ServletOutputStream outputStream = response.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);
			
			writer.append("No");
			writer.append(',');
			writer.append("Date");
			writer.append(',');
			writer.append("account Narration");
			writer.append(',');
			writer.append("	Debit");
			writer.append(',');
			writer.append("	Credit");
			writer.append(',');
			writer.append("Balance");
			writer.append(',');
			writer.append(System.lineSeparator());
			for (int i = 0; i < accEntryEntityList.size(); i++) {
					if(accEntryEntityList.get(i).getDate().after(fromDate)&&accEntryEntityList.get(i).getDate().before(toDate))
				try {
					writer.append(""+(i+1));
					writer.append(',');
					writer.append(accEntryEntityList.get(i).getDate().toString());
					writer.append(',');
					writer.append(accEntryEntityList.get(i).getNarration());
					writer.append(',');
					writer.append(accEntryEntityList.get(i).getDebit().toString());
					writer.append(',');
					writer.append(accEntryEntityList.get(i).getCredit().toString());
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
			throw new ServletException(
					"Error Occurred while downloading the csv file.", e);
		} finally {
			if (out != null)
				out.close();
		}

	}


	}

