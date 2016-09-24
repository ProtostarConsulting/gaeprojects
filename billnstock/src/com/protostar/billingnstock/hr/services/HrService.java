package com.protostar.billingnstock.hr.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.hr.entities.Employee;
import com.protostar.billingnstock.hr.entities.SalSlip;
import com.protostar.billingnstock.hr.entities.SalStruct;
import com.protostar.billingnstock.hr.entities.TimeSheet;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;

@Api(name = "hrService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.hr.services", ownerName = "com.protostar.billingnstock.hr.services", packagePath = ""))
public class HrService {

	@SuppressWarnings("unused")
	@ApiMethod(name = "addemp")
	public void addemp(Employee emp) {
		Key<Employee> now = ofy().save().entity(emp).now();

	}

	@ApiMethod(name = "getAllemp")
	public List<UserEntity> getAllemp(@Named("id") Long busId) {

		List<UserEntity> filteredemp = ofy().load().type(UserEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredemp;

	}

	@ApiMethod(name = "getempByID")
	public Employee getempByID(@Named("id") Long selectedid) {

		Employee Emp = ofy().load().type(Employee.class).id(selectedid).now();

		return Emp;
	}

	@ApiMethod(name = "updateemp")
	public Employee updateemp(Employee emp) {
		Key<Employee> now1 = ofy().save().entity(emp).now();
		return emp;

	}

	@ApiMethod(name = "addsalstruct")
	public void addsalstruct(SalStruct struct) {
		struct.setCreatedDate(new Date());
		Key<SalStruct> now = ofy().save().entity(struct).now();
	}

	@ApiMethod(name = "getAllempsSalStruct")
	public List<SalStruct> getAllempsSalStruct(@Named("id") Long busId) {
		System.out.println("salStructList#busId:" + busId);
		List<SalStruct> salStructList = ofy().load().type(SalStruct.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		System.out.println("salStructList:" + salStructList.size());
		return salStructList;

	}

	@ApiMethod(name = "findsalstruct")
	public SalStruct findsalstruct(@Named("id") Long structId) {
		System.out.println("findsalstruct#structId:" + structId);
		//SalStruct salstruct = ofy().load().type(SalStruct.class).filterKey(Key.create(SalStruct.class, structId)).first().now();
		
		SalStruct salstruct = null;
		List<SalStruct> list = ofy().load().type(SalStruct.class).list();
		for(SalStruct struct : list){
			if(struct.getId().longValue() == structId.longValue()){
				salstruct = struct;
			}
		}
		System.out.println("!findsalstruct#salstruct:" + salstruct);
		return salstruct;

	}

	@ApiMethod(name = "findsalstructfromemp")
	public SalStruct findsalstructfromemp(@Named("id") Long id) {

		SalStruct filteredsalstruct = ofy()
				.load()
				.type(SalStruct.class)
				.filter("empAccount",
						Ref.create(Key.create(UserEntity.class, id))).first()
				.now();
		return filteredsalstruct;
	}

	@ApiMethod(name = "updatesalinfo")
	public void updatesalinfo(SalStruct struct) {
		struct.setModifiedDate(new Date());
		Key<SalStruct> now = ofy().save().entity(struct).now();
	}

	@ApiMethod(name = "countofrecord")
	public List<SalSlip> countofrecord(@Named("id") Long busId) {
		// return ofy().load().type(SalSlip.class).list();
		return ofy().load().type(SalSlip.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
	}

	@ApiMethod(name = "addgsalslip")
	public SalSlip addgsalslip(SalSlip salslip) {
		Date date = new Date();
		SalSlip salslips = salslip;
		salslip.setCreatedDate(date);
		@SuppressWarnings("unused")
		Key<SalSlip> now = ofy().save().entity(salslip).now();

		return salslips;

	}

	@ApiMethod(name = "displyOnlySelected")
	public List<SalSlip> displyOnlySelected(@Named("month") String mon,
			@Named("id") Long busId) {

		return ofy().load().type(SalSlip.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("month", mon).list();

	}

	@ApiMethod(name = "printslip")
	public SalSlip printslip(@Named("id") Long salslipid) {

		//SalSlip sals = ofy().load().type(SalSlip.class).id(salslipid).now();
		SalSlip sals = null;
		List<SalSlip> list = ofy().load().type(SalSlip.class).list();
		for(SalSlip slip : list){
			if(slip.getId().longValue() == salslipid.longValue()){
				sals = slip;
			}
		}

		return sals;
	}

	@ApiMethod(name = "addtimesheet")
	public void addtimesheet(TimeSheet timesheet) {
		Key<TimeSheet> now = ofy().save().entity(timesheet).now();

	}

	@ApiMethod(name = "getAlltimesheet")
	public List<TimeSheet> getAlltimesheet() {
		return ofy().load().type(TimeSheet.class).list();
	}

	@ApiMethod(name = "getcurweekdata")
	public TimeSheet getcurweekdata(@Named("week") String weekNumber) {

		TimeSheet weekdata = ofy().load().type(TimeSheet.class)
				.filter("week", weekNumber).first().now();

		return weekdata;
	}

	@ApiMethod(name = "getallsalslip")
	public List<SalSlip> getallsalslip(@Named("year") String curryear,
			@Named("id") Long busId) {

		List<SalSlip> filteredSalslip = ofy().load().type(SalSlip.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("year", curryear).list();
		return filteredSalslip;

	}

	@ApiMethod(name = "getAllcompany")
	public List<Customer> getAllcompany(@Named("id") Long busId) {

		List<Customer> filteredcontact = ofy().load().type(Customer.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredcontact;

	}

}// end of InternetService
