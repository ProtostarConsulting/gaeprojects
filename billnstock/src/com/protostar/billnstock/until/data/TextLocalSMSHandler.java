package com.protostar.billnstock.until.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.user.entities.BusinessSettingsEntity;
import com.protostar.billingnstock.user.services.UserService;

public class TextLocalSMSHandler {

	private static final Logger logger = Logger
			.getLogger(TextLocalSMSHandler.class.getName());
	static final String TEXTLOCAL_API_URL = "http://api.textlocal.in/send/?";
	static final String SMS_SENDERNAME = "TXTLCL";

	public void sendInvoiceTextMsg(InvoiceEntity invoice)
			throws UnsupportedEncodingException {

		DecimalFormat decimalFormat = new DecimalFormat("#");
		String contactPhoneNum = decimalFormat.format(invoice.getCustomer()
				.getMobile());
		if (contactPhoneNum.length() < 10) {
			return ;
		}
		
		UserService userService = new UserService();
		BusinessSettingsEntity businessSettings = userService.getBusinessSettingsEntity(invoice.getBusiness().getId());
		String TxtLocalApiKey = businessSettings.getTextLocalAPIKey();
		
		Customer customer = invoice.getCustomer();
		
		String custName = "";

		if (customer.getIsCompany()) {
			custName = customer.getCompanyName();
		} else {
			custName = customer.getFirstName() + " " + customer.getLastName();
		}
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
		
		String invoiceDueDate = sdfDate.format(invoice.getInvoiceDueDate());
		
		DecimalFormat decimalFormat2 = new DecimalFormat("#.00");
		
		String invoiceFinalTotalStr = decimalFormat2.format(invoice.getFinalTotal());
		
		String msg = "Dear"+" "+custName+",invoice no"+" "+invoice.getItemNumber()+" "+"with final total Rs."+invoiceFinalTotalStr+" "+ "is due on"+" "+invoiceDueDate+".";
		
		

		String apiKey = "&apiKey="
				+ URLEncoder.encode(TxtLocalApiKey, "UTF-8");
		String sender = "&sender=" + URLEncoder.encode(SMS_SENDERNAME, "UTF-8");
		String numbers = "&numbers="
				+ URLEncoder.encode(contactPhoneNum, "UTF-8");

		String message = "&message=" + URLEncoder.encode(msg, "UTF-8");

		// Send data
		String data = apiKey + numbers + message + sender;
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new SendSMSAsyncOperation(
				data)));

	}

}

class SendSMSAsyncOperation implements DeferredTask {
	private static final long serialVersionUID = 1L;
	String data;

	public SendSMSAsyncOperation(String data) {
		this.data = data;
	}

	@Override
	public void run() {

		try {
			// Send data
			System.out.println("Sending SMS Async--------");
			HttpURLConnection conn = (HttpURLConnection) new URL(
					TextLocalSMSHandler.TEXTLOCAL_API_URL).openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length",
					Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			System.out.println("data:" + data);
			System.out.println("stringBuffer.toString():"
					+ stringBuffer.toString());
			System.out.println("DoneSending SMS Async------");
		} catch (Exception e) {
			System.out.println("Error SMS: " + e);
		}
	}
}
