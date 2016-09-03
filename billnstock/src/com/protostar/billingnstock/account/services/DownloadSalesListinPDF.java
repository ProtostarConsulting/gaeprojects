//package com.protostar.billingnstock.account.services;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//import javax.servlet.ServletException;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.google.appengine.api.datastore.Text;
//import com.google.apphosting.api.search.DocumentPb.Document;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.protostar.billingnstock.account.entities.AccountGroupEntity;
//import com.protostar.billingnstock.account.entities.SalesVoucherEntity;
//
//import com.itextpdf.*;
//import com.itextpdf.text.pdf.PdfDocument;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.text.Paragraph;
//
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//
//
//public class DownloadSalesListinPDF extends HttpServlet  {	
//		
//	private static final long serialVersionUID = 1L;
//       
//   
//    public DownloadSalesListinPDF() {
//        super();
//       
//    }
//protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
////	log.info("hi i am download servlet");
//	System.out.println("i am Groupservlet");
//	
//	VoucherService  voucherService= new VoucherService();
//	
//	
//
//	List<SalesVoucherEntity> salesEntity = voucherService.listvoucher();
//	
//	//PdfWriter writer = PdfWriter.getInstance(null, null);
//	
//	PrintWriter out1 = null;
//	try {
//
//		response.setContentType("text/pdf");
//
//		
//		PdfDocument doc=new PdfDocument(); 
//		ServletOutputStream outputStream = response.getOutputStream();
//		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
//		PdfWriter.getInstance(doc,outputStream); 
//		
//
//	   
//	        
//	        doc.addAuthor("betterThanZero");
//	        doc.add( new Paragraph("Life Expectancy: "));
//	        doc.add( new Paragraph("Life Expectancy: "));
//	        doc.add( new Paragraph("Life Expectancy: "));
//	        doc.add( new Paragraph("Life Expectancy: "));
//	        doc.add( new Paragraph("Life Expectancy: "));
//	        
//	        
//	        
//	        
//	        
//	  
//
//	} catch (Exception e) {
//		
//		e.printStackTrace();
//		throw new ServletException(
//				"Error Occurred while downloading the csv file.", e);
//	} finally {}
//
//}
//
//
//
//
//}
//	
//
//	
//
//
//
//
//
