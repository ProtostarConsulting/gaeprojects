package com.protostar.billingnstock.cust.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.cust.entities.AccountEntity;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.twilio.sdk.resource.instance.Account;

@Api(name = "accountService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.stock.cust.services", ownerName = "com.protostar.billingnstock.stock.cust.services", packagePath = ""))
public class AccountService {
	
	@ApiMethod(name = "addAccount")
	public void addAccount(AccountEntity accountEntity) {
			
		
		Key<AccountEntity> cust = ofy().save().entity(accountEntity).now();
	
	}

	@ApiMethod(name = "getAllAccountsByBusiness")
	public List<AccountEntity> getAllAccountsByBusiness(@Named("id") Long id) {
		
		List<AccountEntity> accountList=ofy().load().type(AccountEntity.class).list();
		List<AccountEntity> filteredAccounts = new ArrayList<AccountEntity>();
		
		
		for(int i=0;i<accountList.size();i++)
		{				
			 if(accountList.get(i).getLoggedInUser().getBusinessAccount().getId().equals(id))
			 {
				 System.out.println("Got the record:" + accountList.get(i) );
				 filteredAccounts.add(accountList.get(i));
			 }
			 System.out.println("Recored found:" + accountList.get(i).getLoggedInUser().getId());
		}
		
		return filteredAccounts;

	}

	@ApiMethod(name = "getCustomerByID")
	public Customer getCustomerByID(@Named("Id") Long Id) {

		Customer customerById = ofy().load().type(Customer.class).id(Id).now();

		System.out.println("Searched Recored is:"
				+ customerById.getCustomerName());

		return customerById;
	}

	@ApiMethod(name = "searchCustomerByName")
	public Customer searchCustomerByName(
			@Named("customerName") String customerName) {

		Customer customer = ofy().load().type(Customer.class)
				.filter("customerName", customerName).first().now();

		System.out.println("Searched Recored is:" + customer.getCustomerName());

		return customer;
	}

	/*
	 * @ApiMethod(name="addCustomer") public ServerMsg addCustomer(Customer
	 * customer) { ServerMsg msgBean=new ServerMsg(); EntityManager em=null; try
	 * { Customer customer2=new Customer(); if(customer.getId()!=null) {
	 * customer2.setId(customer.getId());
	 * 
	 * }
	 * 
	 * customer2.setCustomerName(customer.getCustomerName());
	 * customer2.setMobile(customer.getMobile());
	 * customer2.setEmail(customer.getEmail());
	 * customer2.setCustomerAddress(customer.getCustomerAddress()); //
	 * msgBean.setToken("S");
	 * 
	 * em=EMF.get().createEntityManager(); em.persist(customer2);
	 * msgBean.setMsg(
	 * "Customer Records Added successfully"+" "+customer2.getCustomerName()); }
	 * catch (Exception e) { e.printStackTrace(); } finally { em.close(); }
	 * return msgBean; }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @ApiMethod(name="getAllCustomers") public List<Customer>
	 * getAllCustomers() { System.out.println("In side getAllCustomers " );
	 * List<Customer> customerList= new ArrayList<Customer>(); EntityManager em=
	 * null; try { em = EMF.get().createEntityManager();
	 * 
	 * Query q= em.createQuery("select c from Customer c");
	 * customerList=q.getResultList();
	 * System.out.println("Got getAllCustomers: " + customerList.size() );
	 * 
	 * } catch (Exception e)
	 * 
	 * { // TODO Auto-generated catch block e.printStackTrace(); } finally {
	 * em.close(); }
	 * 
	 * return customerList;
	 * 
	 * }//end of getAllStockServices
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @ApiMethod(name="getCustomerByID") public Customer
	 * getCustomerByID(@Named("Id") Long Id) {
	 * System.out.println("In side getCustomerById "); ServerMsg msgBean=new
	 * ServerMsg(); List<Customer> customerList = new ArrayList<Customer>();
	 * EntityManager em=null;
	 * 
	 * try { em = EMF.get().createEntityManager(); Query q =
	 * em.createQuery("select c from Customer c where c.id =" + Id);
	 * customerList = q.getResultList();
	 * System.out.println("Got AllCustomerList: " + customerList.size());
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } finally { em.close(); } if(customerList.size() >
	 * 0) return customerList.get(0); else return null; }
	 */
}// end of CustomerService