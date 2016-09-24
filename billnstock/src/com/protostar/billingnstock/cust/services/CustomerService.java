package com.protostar.billingnstock.cust.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.crm.entities.Contact;
import com.protostar.billingnstock.crm.services.CrmService;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.until.data.ServerMsg;

@Api(name = "customerService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.stock.cust.services", ownerName = "com.protostar.billingnstock.stock.cust.services", packagePath = ""))
public class CustomerService {

	@ApiMethod(name = "addCustomer")
	public Customer addCustomer(Customer customer) {

		if (customer.getId() == null) {
			customer.setCreatedDate(new Date());
			// Customer.setModifiedDate(new Date());
		} else {
			customer.setModifiedDate(new Date());
		}

		ofy().save().entity(customer).now();

		if (customer.getIsCompany() == true) {
			CrmService crmserv = new CrmService(); // use get contact by email
													// id validate email exist
													// in customer and contact
			Contact addcontact = (Contact) crmserv.getContactByEmailID(customer
					.getEmail());
			if (addcontact == null) {
				addcontact = new Contact();
			}
			addcontact.setCustomer(customer);
			addcontact.setfName(customer.getFirstName());
			addcontact.setlName(customer.getLastName());
			addcontact.setPhone(customer.getMobile());
			addcontact.setBusiness(customer.getBusiness());
			addcontact.setEmail(customer.getEmail());
			ofy().save().entity(addcontact).now();
		}
		return customer;

	}

	@ApiMethod(name = "getAllCustomersByBusiness")
	public List<Customer> getAllCustomersByBusiness(@Named("id") Long busId) {

		List<Customer> filteredCustomers = ofy().load().type(Customer.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return filteredCustomers;
	}

	@ApiMethod(name = "getCustomerByID")
	public Customer getCustomerByID(@Named("Id") Long Id) {

		// Customer customerById =
		// ofy().load().type(Customer.class).id(Id).now();

		Customer foundInvoice = null;
		List<Customer> list = ofy().load().type(Customer.class).list();
		for (Customer cust : list) {
			if (cust.getId().longValue() == Id.longValue()) {
				foundInvoice = cust;
			}
		}
		return foundInvoice;
	}

	@ApiMethod(name = "isCustomerExists")
	public ServerMsg isCustomerExists(@Named("email") String email) {
		ServerMsg serverMsg = new ServerMsg();
		List<Customer> customer = ofy().load().type(Customer.class)
				.filter("email", email).list();

		if (customer.size() == 0) {
			serverMsg.setReturnBool(false);
		} else {
			serverMsg.setReturnBool(true);
		}

		return serverMsg;
	}

	@ApiMethod(name = "updateCustomer")
	public void updateCustomer(Customer customer) {

		Key<Customer> cust = ofy().save().entity(customer).now();

	}

}// end of CustomerService
