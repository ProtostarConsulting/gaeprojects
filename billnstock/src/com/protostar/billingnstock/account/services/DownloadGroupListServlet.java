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

import com.protostar.billingnstock.account.entities.AccountGroupEntity;


public class DownloadGroupListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(DownloadAccountsServlet.class.getName());   
    
    public DownloadGroupListServlet() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
				log.info("hi i am download servlet");
				System.out.println("i am Groupservlet");
				
				AccountGroupService accGrpService= new AccountGroupService();
				
				Date date = new Date();
				
				String DATE_FORMAT = "dd/MMM/yyyy";
				
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				
				List<AccountGroupEntity> accEntity = accGrpService.getAccountGroupList();
				
				OutputStream out = null;
				try {

					response.setContentType("text/csv");

					response.setHeader("Content-Disposition",
							"attachment; filename=GroupListData_" + sdf.format(date)
									+ ".csv");

					ServletOutputStream outputStream = response.getOutputStream();
					OutputStreamWriter writer = new OutputStreamWriter(outputStream);

					writer.append("groupName");
					writer.append(',');
					writer.append("description");
					writer.append(',');
					writer.append("displayOrderNo");
					writer.append(',');
				
				
					writer.append(System.lineSeparator());

					for (int i = 0; i < accEntity.size(); i++) {

						try {

							
							writer.append(accEntity.get(i).getGroupName());
							writer.append(',');
							
							writer.append(accEntity.get(i).getDescription());
							writer.append(',');
							
							writer.append(accEntity.get(i).getDisplayOrderNo().toString());
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

			}

	
	

}
