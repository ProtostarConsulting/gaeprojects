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

public class DownloadAccountsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(DownloadAccountsServlet.class.getName());
       
   
    public DownloadAccountsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		log.info("hi i am download servlet");
		System.out.println("i am servlet");
		AccountService accountService=new AccountService();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		List<AccountEntity> accEntity = accountService.getAccountList(0l);
		OutputStream out = null;
		try {

			response.setContentType("text/csv");

			response.setHeader("Content-Disposition",
					"attachment; filename=AccountListData_" + sdf.format(date)
							+ ".csv");

			ServletOutputStream outputStream = response.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);

			writer.append("accountName");
			writer.append(',');
			writer.append("accountNo");
			writer.append(',');
			writer.append("description");
			writer.append(',');
			writer.append("displayOrderNo");
			writer.append(',');
			writer.append("accountType");
			writer.append(',');
			writer.append("contra");
			writer.append(',');
		
			writer.append(System.lineSeparator());

			for (int i = 0; i < accEntity.size(); i++) {

				try {

					
					writer.append(accEntity.get(i).getAccountName());
					writer.append(',');

					writer.append(""+accEntity.get(i).getAccountNo());
					writer.append(',');
					writer.append(accEntity.get(i).getDescription());
					writer.append(',');
					writer.append(accEntity.get(i).getDisplayOrderNo().toString());
					writer.append(',');
					writer.append(accEntity.get(i).getAccountType());
					writer.append(',');
					writer.append(accEntity.get(i).getContra().toString());
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

