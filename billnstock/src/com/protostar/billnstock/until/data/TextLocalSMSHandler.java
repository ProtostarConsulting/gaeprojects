package com.protostar.billnstock.until.data;

import java.io.UnsupportedEncodingException;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.invoice.entities.InvoiceSMSTask;

public class TextLocalSMSHandler {

	public void sendInvoiceTextMsg(InvoiceEntity invoice)
			throws UnsupportedEncodingException {

		Customer customer = invoice.getCustomer();

		String custName = "";

		if (customer.getIsCompany()) {
			custName = customer.getCompanyName();
		} else {
			custName = customer.getFirstName() + " " + customer.getLastName();
		}

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withPayload(new InvoiceSMSTask(invoice
				.getBusiness().getId(), invoice.getItemNumber(), invoice
				.getInvoiceDueDate(), invoice.getFinalTotal(), custName,
				invoice.getCustomer().getMobile())));

	}
}
