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


public class DownloadAccountListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(DownloadAccountListServlet.class.getName());   
    
    public DownloadAccountListServlet() {
        super();
        
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
				Long businessId = Long.parseLong(request.getParameter("id"));
			//	AccountEntity accEntity=new AccountEntity();
				AccountingService accService=new AccountingService();
				
				Date date = new Date();
				
				String DATE_FORMAT = "dd/MMM/yyyy";
				
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				
				List<AccountEntity> accEntity = accService.getAccountList(businessId);
				
				OutputStream out = null;
				try {

					response.setContentType("text/csv");

					response.setHeader("Content-Disposition",
							"attachment; filename=AccountListData_" + sdf.format(date)
									+ ".csv");

					ServletOutputStream outputStream = response.getOutputStream();
					OutputStreamWriter writer = new OutputStreamWriter(outputStream);
					writer.append("\t Account No  ");
					writer.append("\t");
					writer.append(',');
					writer.append("\t Account Name  ");
					writer.append("\t");
					writer.append(',');
					writer.append("\t Business Account No ");
					writer.append("\t");
					writer.append(',');
					writer.append("\t Account Type");
					writer.append(',');
					writer.append("\t Order No");
					writer.append(',');
					writer.append("\t Description ");
					writer.append(',');
					writer.append("\t Contra ");
					writer.append(',');
					writer.append("\t Account Group ");
					writer.append(',');
					writer.append(System.lineSeparator());

					for (int i = 0; i < accEntity.size(); i++) {

						try {
							writer.append("\t  "+accEntity.get(i).getItemNumber());
							writer.append("\t");
							writer.append(',');

							writer.append("\t  "+accEntity.get(i).getAccountName().toString());
							writer.append("\t");
							writer.append(',');
							
							writer.append("\t  "+accEntity.get(i).getAccountNo().toString());
							writer.append("\t");
							writer.append(',');
							
							writer.append("\t"+accEntity.get(i).getAccountType().toString());
							writer.append(',');
							
							writer.append("\t"+accEntity.get(i).getDisplayOrderNo().toString());
							writer.append(',');
							
							writer.append("\t"+accEntity.get(i).getDescription().toString());
							writer.append(',');
							writer.append("\t"+accEntity.get(i).getContra().toString());
							writer.append(',');
							writer.append("\t"+accEntity.get(i).getAccountGroup().getGroupName().toString());
							
							writer.append(',');
							writer.append(System.lineSeparator());
							} 
						
							catch (Exception e) {
									
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

			}}