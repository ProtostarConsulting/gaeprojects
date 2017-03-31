package com.protostar.billingnstock.invoice.entities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.protostar.billingnstock.user.entities.BusinessSettingsEntity;
import com.protostar.billingnstock.user.services.UserService;

public class InvoiceSMSTask implements DeferredTask {

	static final String TEXTLOCAL_API_URL = "http://api.textlocal.in/send/?";
	static final String SMS_SENDERNAME = "TXTLCL";

	private static final long serialVersionUID = 1L;
	private boolean skipTxtMsg = false;
	private long bizId;
	private int itemNumber;
	private Date invoiceDueDate;
	private double finalTotal;
	private String custName;
	private long phoneNum;
	private String data;

	public InvoiceSMSTask(long bizId, int itemNumber, Date invoiceDueDate,
			double finalTotal, String custName, long phoneNum)
			throws UnsupportedEncodingException {

		this.bizId = bizId;
		this.itemNumber = itemNumber;
		this.invoiceDueDate = invoiceDueDate;
		this.finalTotal = finalTotal;
		this.custName = custName;
		this.phoneNum = phoneNum;

		DecimalFormat decimalFormat = new DecimalFormat("#");
		String phoneNumStr = decimalFormat.format(this.phoneNum);
		if (phoneNumStr.length() < 10) {
			this.skipTxtMsg = true;
			return;
		}

		UserService userService = new UserService();
		BusinessSettingsEntity businessSettings = userService
				.getBusinessSettingsEntity(bizId);

		if (businessSettings == null || !businessSettings.isSmsNotification()) {
			this.skipTxtMsg = true;
			return;
		}

		String TxtLocalApiKey = businessSettings.getTextLocalAPIKey();

		if (TxtLocalApiKey == null || TxtLocalApiKey.isEmpty()) {
			this.skipTxtMsg = true;
			return;
		}

		SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");

		String invoiceDueDateStr = sdfDate.format(this.invoiceDueDate);

		DecimalFormat decimalFormat2 = new DecimalFormat("#.00");

		String invoiceFinalTotalStr = decimalFormat2.format(this.finalTotal);

		String msg = "Dear" + " " + this.custName + ",invoice no" + " "
				+ this.itemNumber + " " + "with final total Rs."
				+ invoiceFinalTotalStr + " " + "is due on" + " "
				+ invoiceDueDateStr + ".";

		String apiKey = "&apiKey=" + URLEncoder.encode(TxtLocalApiKey, "UTF-8");
		String sender = "&sender=" + URLEncoder.encode(SMS_SENDERNAME, "UTF-8");
		String numbers = "&numbers=" + URLEncoder.encode(phoneNumStr, "UTF-8");
		String message = "&message=" + URLEncoder.encode(msg, "UTF-8");

		this.setData(apiKey + numbers + message + sender);

	}

	@Override
	public void run() {

		try {
			if (this.isSkipTxtMsg()) {
				return;
			}
			System.out.println("Sending SMS Async--------");
			HttpURLConnection conn = (HttpURLConnection) new URL(
					InvoiceSMSTask.TEXTLOCAL_API_URL).openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length",
					Integer.toString(this.getData().length()));
			conn.getOutputStream().write(this.getData().getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			System.out.println("data:" + this.getData());
			System.out.println("stringBuffer.toString():"
					+ stringBuffer.toString());
			System.out.println("DoneSending SMS Async------");
		} catch (Exception e) {
			System.out.println("Error SMS: " + e);
		}
	}

	public long getBizId() {
		return bizId;
	}

	public void setBizId(long bizId) {
		this.bizId = bizId;
	}

	public Date getInvoiceDueDate() {
		return invoiceDueDate;
	}

	public void setInvoiceDueDate(Date invoiceDueDate) {
		this.invoiceDueDate = invoiceDueDate;
	}

	public double getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(double finalTotal) {
		this.finalTotal = finalTotal;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public long getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(long phoneNum) {
		this.phoneNum = phoneNum;
	}

	public boolean isSkipTxtMsg() {
		return skipTxtMsg;
	}

	public void setSkipTxtMsg(boolean skipTxtMsg) {
		this.skipTxtMsg = skipTxtMsg;
	}

}
