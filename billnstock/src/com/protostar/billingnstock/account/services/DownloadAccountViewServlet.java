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

import com.protostar.billingnstock.account.entities.AccountEntryEntity;

public class DownloadAccountViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(DownloadAccountsServlet.class.getName());

	public DownloadAccountViewServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		AccountEntryService accountEntryService = new AccountEntryService();
		Date date = new Date();
		String DATE_FORMAT = "dd-MMM-yyyyy  hh:mm:ss a";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		Long businessId = Long.parseLong(request.getParameter("id"));
		Long accountId = Long.parseLong(request.getParameter("searchAccId"));
		TimeZone timeZone = TimeZone.getTimeZone("IST");
		sdf.setTimeZone(timeZone);

		String d1 = request.getParameter("fromDate");
		String d2 = request.getParameter("toDate");
		System.out.println("fromdate-------" + d1);
		System.out.println("todate-------" + d2);
		Date actualFromDate = new Date(Long.parseLong(d1));
		Date actualtoDate = new Date(Long.parseLong(d2));
		
		System.out.println("dates--------"+actualFromDate);
		AccountEntryEntity bb=new AccountEntryEntity();
		List<AccountEntryEntity> accEntryEntityList= accountEntryService.getAccountViewEntryByAccountId(actualFromDate,actualtoDate,accountId,businessId);
	
		
		OutputStream out = null;
		try {
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition",
					"attachment; filename=Account_View_Data" + sdf.format(date) + ".csv");
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
				if (accEntryEntityList.get(i).getDate().after(actualFromDate)
						&& accEntryEntityList.get(i).getDate().before(actualtoDate))
					try {
						writer.append("" + (i + 1));
						writer.append(',');
						writer.append("" + sdf.format(accEntryEntityList.get(i).getDate()));
						writer.append(',');
						writer.append(accEntryEntityList.get(i).getNarration());
						writer.append(',');

						if (accEntryEntityList.get(i).getDebit() != null) {
							writer.append(accEntryEntityList.get(i).getDebit().toString());
						} else {
							writer.append("0");
						}
						writer.append(',');
						if (accEntryEntityList.get(i).getCredit() != null) {
							writer.append(accEntryEntityList.get(i).getCredit().toString());
						} else {
							writer.append("0");
						}
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
