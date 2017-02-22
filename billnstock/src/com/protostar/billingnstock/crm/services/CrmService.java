package com.protostar.billingnstock.crm.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.crm.entities.Contact;
import com.protostar.billingnstock.crm.entities.Lead;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.until.data.ServerMsg;

@Api(name = "crmService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.crm.services", ownerName = "com.protostar.billingnstock.crm.services", packagePath = ""))
public class CrmService {

	@SuppressWarnings("unused")
	@ApiMethod(name = "addlead")
	public void addlead(Lead lead) {
		lead.setCreatedDate(new Date());
		Key<Lead> now = ofy().save().entity(lead).now();

	}

	@ApiMethod(name = "getAllleads")
	public List<Lead> getAllleads(@Named("id") Long busId) {
		List<Lead> filteredlead = ofy().load().type(Lead.class).ancestor(Key.create(BusinessEntity.class, busId))
				.list();
		return filteredlead;

	}

	@ApiMethod(name = "getLeadById")
	public Lead getLeadById(@Named("id") Long selectedid) {

		Lead lead = ofy().load().type(Lead.class).id(selectedid).now();

		return lead;
	}

	@ApiMethod(name = "addupdatetask")
	public void addupdatetask(Lead lead) {
		// lead.setModifiedBy(lead.getLoggedInUser().getEmail_id());
		lead.setModifiedDate(new Date());
		ofy().save().entity(lead).now();

	}

	@ApiMethod(name = "addcontact")
	public void addcontact(Contact contact) {
		contact.setCreatedDate(new Date());
		ofy().save().entity(contact).now();

	}

	@ApiMethod(name = "updatecontact")
	public void updatecontact(Contact contact) {
		contact.setModifiedDate(new Date());
		ofy().save().entity(contact).now();

	}

	@ApiMethod(name = "getAllcontact")
	public List<Contact> getAllcontact(@Named("id") Long busId) {

		List<Contact> filteredContact = ofy().load().type(Contact.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredContact;

	}

	@ApiMethod(name = "getContactById")
	public Contact getContactById(@Named("id") Long contactNo) {
		Contact contact = ofy().load().type(Contact.class).id(contactNo).now();
		return contact;
	}

	@ApiMethod(name = "getContactByCustomerId", path = "getContactByCustomerId")
	public List<Contact> getContactByCustomerId(@Named("id") Long CustId) {
		List<Contact> contact = ofy().load().type(Contact.class)
				.filter("customer", Ref.create(Key.create(Customer.class, CustId))).list();

		return contact;
	}

	@ApiMethod(name = "getContactByEmailID", path = "getContactByEmailID")
	public Contact getContactByEmailID(@Named("email") String email) {
		List<Contact> contact = ofy().load().type(Contact.class).filter("email", email).list();

		return (contact == null || contact.size() == 0) ? null : contact.get(0);
	}

	public Contact getCustomerPrimaryContact(Customer cust) {
		List<Contact> contact = ofy().load().type(Contact.class).ancestor(cust.getBusiness())
				.filter("customer", Ref.create(cust)).filter("email", cust.getEmail()).list();
		return (contact == null || contact.size() == 0) ? null : contact.get(0);
	}

	@ApiMethod(name = "isContactExists")
	public ServerMsg isContactExists(@Named("email") String email) {
		ServerMsg serverMsg = new ServerMsg();
		List<Contact> customer = ofy().load().type(Contact.class).filter("email", email).list();

		if (customer.size() == 0) {
			serverMsg.setReturnBool(false);
		} else {
			serverMsg.setReturnBool(true);
		}

		return serverMsg;
	}

}// end of InternetService
