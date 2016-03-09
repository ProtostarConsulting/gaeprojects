package com.protostar.billingnstock.document;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

/**
 * Servlet implementation class Serve
 */
public class Serve extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
     public Serve() {
        super();
    }

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	protected void  doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    BlobKey blobKey = new BlobKey(request.getParameter("blob-key"));
	    String user=request.getParameter("user");
	    System.out.println("user="+user);
        blobstoreService.serve(blobKey, response);
        
	}

}