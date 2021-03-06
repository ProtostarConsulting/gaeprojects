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
	private final Logger log = Logger.getLogger(DownloadGroupListServlet.class.getName());   
    
    public DownloadGroupListServlet() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
	//			log.info("hi i am download servlet");
				System.out.println("i am Groupservlet");
				Long businessId = Long.parseLong(request.getParameter("id"));
				AccountGroupService accGrpService= new AccountGroupService();
				
				Date date = new Date();
				
				String DATE_FORMAT = "dd/MMM/yyyy";
				
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				
				List<AccountGroupEntity> accEntity = accGrpService.getAccountGroupList(businessId);
				
				OutputStream out = null;
				try {

					response.setContentType("text/csv");

					response.setHeader("Content-Disposition",
							"attachment; filename=GroupListData_" + sdf.format(date)
									+ ".csv");

					ServletOutputStream outputStream = response.getOutputStream();
					OutputStreamWriter writer = new OutputStreamWriter(outputStream);
					writer.append("\t Group No  ");
					writer.append("\t");
					writer.append(',');
					writer.append("\t GroupName  ");
					writer.append("\t");
					writer.append(',');
					writer.append("\tIs Primary  ");
					writer.append(',');
					writer.append("\tNature");
					writer.append(',');
					writer.append("\tParent Group   ");
					writer.append(',');
					
				
				
					writer.append(System.lineSeparator());

					for (int i = 0; i < accEntity.size(); i++) {

						try {
							writer.append("\t  "+accEntity.get(i).getItemNumber());
							writer.append("\t");
							writer.append(',');
							writer.append("\t  "+accEntity.get(i).getGroupName());
							writer.append("\t");
							writer.append(',');
														writer.append("\t"+accEntity.get(i).getIsPrimary().toString());
							writer.append(',');
							writer.append("\t"+accEntity.get(i).getAccountGroupType().toString());
							writer.append(',');
							if(accEntity.get(i).getIsPrimary().equals(true))
							{
							writer.append(" \t");
							writer.append(',');
							}
							else{
								writer.append("\t"+accEntity.get(i).getParent().getGroupName().toString());
								writer.append(',');
								
							}
						
							
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
