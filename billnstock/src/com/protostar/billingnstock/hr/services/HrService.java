package com.protostar.billingnstock.hr.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.hr.entities.Employee;
import com.protostar.billingnstock.hr.entities.LeaveAppEntity;
import com.protostar.billingnstock.hr.entities.LeaveDetailEntity;
import com.protostar.billingnstock.hr.entities.LeaveMasterEntity;
import com.protostar.billingnstock.hr.entities.MonthlyPaymentDetailEntity;
import com.protostar.billingnstock.hr.entities.SalSlip;
import com.protostar.billingnstock.hr.entities.SalStruct;
import com.protostar.billingnstock.hr.entities.TimeSheet;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.LeaveDetailEntityList;
import com.protostar.billnstock.until.data.MonthlyPaymentDetailEntityList;
import com.protostar.billnstock.until.data.SalStructEntityList;

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

	@ApiMethod(name = "getSalStructByUser")
	public SalStruct getSalStructByUser(UserEntity currRowUser) {
		List<SalStruct> list = ofy().load().type(SalStruct.class)
				.ancestor(Key.create(BusinessEntity.class, currRowUser.getBusiness().getId()))
				.filter("empAccount", currRowUser).list();
		SalStruct salStruct = list.size() > 0 ? list.get(0) : null;
		return salStruct;
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
		// System.out.println("salStructList#busId:" + busId);
		List<SalStruct> salStructList = ofy().load().type(SalStruct.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		// System.out.println("salStructList:" + salStructList.size());
		return salStructList;

	}

	@ApiMethod(name = "findsalstruct")
	public SalStruct findsalstruct(@Named("id") Long structId) {
		// System.out.println("findsalstruct#structId:" + structId);
		// SalStruct salstruct =
		// ofy().load().type(SalStruct.class).filterKey(Key.create(SalStruct.class,
		// structId)).first().now();

		SalStruct salstruct = null;
		List<SalStruct> list = ofy().load().type(SalStruct.class).list();
		for (SalStruct struct : list) {
			if (struct.getId().longValue() == structId.longValue()) {
				salstruct = struct;
			}
		}
		// System.out.println("!findsalstruct#salstruct:" + salstruct);
		return salstruct;

	}

	@ApiMethod(name = "findsalstructfromemp")
	public SalStruct findsalstructfromemp(@Named("id") Long id) {

		SalStruct filteredsalstruct = ofy().load().type(SalStruct.class)
				.filter("empAccount", Ref.create(Key.create(UserEntity.class, id))).first().now();
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
		return ofy().load().type(SalSlip.class).ancestor(Key.create(BusinessEntity.class, busId)).list();
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
	public List<SalSlip> displyOnlySelected(@Named("month") String mon, @Named("id") Long busId) {

		return ofy().load().type(SalSlip.class).ancestor(Key.create(BusinessEntity.class, busId)).filter("month", mon)
				.list();

	}

	@ApiMethod(name = "printslip")
	public SalSlip printslip(@Named("id") Long salslipid) {

		// SalSlip sals = ofy().load().type(SalSlip.class).id(salslipid).now();
		SalSlip sals = null;
		List<SalSlip> list = ofy().load().type(SalSlip.class).list();
		for (SalSlip slip : list) {
			if (slip.getId().longValue() == salslipid.longValue()) {
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

		TimeSheet weekdata = ofy().load().type(TimeSheet.class).filter("week", weekNumber).first().now();

		return weekdata;
	}

	@ApiMethod(name = "getallsalslip")
	public List<SalSlip> getallsalslip(@Named("year") String curryear, @Named("id") Long busId) {

		List<SalSlip> filteredSalslip = ofy().load().type(SalSlip.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).filter("year", curryear).list();
		return filteredSalslip;

	}

	@ApiMethod(name = "getAllcompany")
	public List<Customer> getAllcompany(@Named("id") Long busId) {

		List<Customer> filteredcontact = ofy().load().type(Customer.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredcontact;

	}

	@ApiMethod(name = "saveLeaveDetailList", path = "saveLeaveDetailList")
	public void saveLeaveDetailList(LeaveDetailEntityList saveleaveDetailList) {
		@SuppressWarnings("unchecked")
		List<LeaveDetailEntity> list = (List<LeaveDetailEntity>) EntityUtil
				.updateCreatedModifiedDate(saveleaveDetailList.getList());
		ofy().save().entities(list).now();
		// saveleaveDetail.setCurrentMonth();
	}

	public LeaveDetailEntity saveLeaveDetail(LeaveDetailEntity leaveDetailEntity) {
		ofy().save().entity(leaveDetailEntity).now();
		return leaveDetailEntity;
	}

	@ApiMethod(name = "saveSalaryMasterDetail", path = "saveSalaryMasterDetail")
	public void saveSalaryMasterDetail(SalStruct salStruct) {
		ofy().save().entities(salStruct).now();
	}

	@ApiMethod(name = "saveSalaryMasterDetailList", path = "saveSalaryMasterDetailList")
	public void saveSalaryMasterDetailList(SalStructEntityList salStructList) {
		@SuppressWarnings("unchecked")
		List<SalStruct> list = (List<SalStruct>) EntityUtil.updateCreatedModifiedDate(salStructList.getList());
		ofy().save().entities(list).now();
	}

	@ApiMethod(name = "getSalaryMasterlist", path = "getSalaryMasterlist")
	public List<SalStruct> getSalaryMasterlist(@Named("id") Long busId) {
		List<SalStruct> salStructlist = ofy().load().type(SalStruct.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		List<SalStruct> salStructlistToReturn = new ArrayList<SalStruct>();
		UserService userS = new UserService();
		List<UserEntity> userList = userS.getUsersByBusinessId(busId);
		if (userList.size() == salStructlist.size()) {
			return salStructlist;
		}

		for (UserEntity usere : userList) {
			SalStruct foundSalMaster = null;
			for (SalStruct salStruct : salStructlist) {
				if (salStruct.getEmpAccount() != null && usere.getId() == salStruct.getEmpAccount().getId()) {
					foundSalMaster = salStruct;
				}
			}

			if (foundSalMaster == null) {
				foundSalMaster = new SalStruct();
				foundSalMaster.setBusiness(usere.getBusiness());
				foundSalMaster.setEmpAccount(usere);
			}

			salStructlistToReturn.add(foundSalMaster);
		}

		return salStructlistToReturn;

	}

	@ApiMethod(name = "getLeaveListEmp")
	public List<LeaveDetailEntity> getLeaveListEmp(@Named("id") Long busId, @Named("month") String month,
			@Named("prevMonth") String prevMonth) {

		List<LeaveDetailEntity> empLeaveListToReturn = new ArrayList<LeaveDetailEntity>();
		List<LeaveDetailEntity> empLeaveListCurrentMonth = ofy().load().type(LeaveDetailEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).filter("currentMonth", month.trim()).list();

		UserService userService = new UserService();
		List<UserEntity> userList = userService.getUsersByBusinessId(busId);

		/*
		 * if (userList.size() == empLeaveListToReturn.size()) { // Meaning all
		 * employee leaves are updated. No need to process // further, hence
		 * returning. return empLeaveListCurrentMonth; }
		 */

		List<LeaveDetailEntity> empLeaveListPrevMonth = ofy().load().type(LeaveDetailEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).filter("currentMonth", prevMonth).list();

		for (UserEntity userEntity : userList) {

			LeaveDetailEntity foundLeaveDetail = null;
			if (empLeaveListCurrentMonth != null & !empLeaveListCurrentMonth.isEmpty()) {

				for (LeaveDetailEntity leaveDetail : empLeaveListCurrentMonth) {
					if (leaveDetail.getUser().getId() == userEntity.getId()) {
						foundLeaveDetail = leaveDetail;
					}
				}
			}

			if (foundLeaveDetail == null) {
				foundLeaveDetail = new LeaveDetailEntity();
				foundLeaveDetail.setCurrentMonth(month.trim());
				foundLeaveDetail.setUser(userEntity);
				foundLeaveDetail.setBusiness(userEntity.getBusiness());
			}

			for (LeaveDetailEntity prevMonthleaveDetail : empLeaveListPrevMonth) {
				if (prevMonthleaveDetail.getUser().getId() == userEntity.getId()) {
					foundLeaveDetail.setOpeningBalance(prevMonthleaveDetail.getNextOpeningBalance());

				}
			}

			saveLeaveDetail(foundLeaveDetail);

			empLeaveListToReturn.add(foundLeaveDetail);

		}
		// System.out.println("id********************"+empLeaveListToReturn.get(0).getId());
		return empLeaveListToReturn;
	}

	@ApiMethod(name = "saveMonthlyPaymentDetailList", path = "saveMonthlyPaymentDetailList")
	public Collection<MonthlyPaymentDetailEntity> saveMonthlyPaymentDetailList(
			MonthlyPaymentDetailEntityList monthlyPaymentDetailEntityList) {
		@SuppressWarnings("unchecked")
		List<MonthlyPaymentDetailEntity> list = (List<MonthlyPaymentDetailEntity>) EntityUtil
				.updateCreatedModifiedDate(monthlyPaymentDetailEntityList.getList());
		Map<Key<MonthlyPaymentDetailEntity>, MonthlyPaymentDetailEntity> now = ofy().save().entities(list).now();
		return now.values();
	}

	@ApiMethod(name = "getMonthlyPayment", path = "getMonthlyPayment")
	public List<MonthlyPaymentDetailEntity> getMonthlyPayment(@Named("id") Long busId,
			@Named("currentmonth") String currentmonth) {

		List<MonthlyPaymentDetailEntity> monthlyPaymentDetailEntityListToReturn = new ArrayList<MonthlyPaymentDetailEntity>();
		List<MonthlyPaymentDetailEntity> monthlyPaymentDetailEntityList = ofy().load()
				.type(MonthlyPaymentDetailEntity.class).ancestor(Key.create(BusinessEntity.class, busId))
				.filter("currentMonth", currentmonth).list();

		List<SalStruct> salaryMasterlist = getSalaryMasterlist(busId);
		String prevMonth = null;
		List<LeaveDetailEntity> leaveListEmp = getLeaveListEmp(busId, currentmonth, prevMonth);
		for (SalStruct salStruct : salaryMasterlist) {
			if (salStruct.getMonthlyGrossSal() < 100) {
				continue;
				// This is to ignore the employees for whom salary structure is
				// not set
			}
			MonthlyPaymentDetailEntity foundSalEntity = null;
			for (MonthlyPaymentDetailEntity salEntity : monthlyPaymentDetailEntityList) {
				if (salStruct.getEmpAccount().getId() == salEntity.getSalStruct().getEmpAccount().getId()) {
					foundSalEntity = salEntity;
					break;
				}
			}
			if (foundSalEntity == null || (foundSalEntity != null && !foundSalEntity.isFinalized())) {
				LeaveDetailEntity empLeaveDetail = getEmpLeaveDetail(salStruct.getEmpAccount(), leaveListEmp);
				foundSalEntity = new MonthlyPaymentDetailEntity(salStruct.getEmpAccount().getBusiness(), empLeaveDetail,
						salStruct, currentmonth);
			}
			monthlyPaymentDetailEntityListToReturn.add(foundSalEntity);
		}

		return monthlyPaymentDetailEntityListToReturn;

	}

	private static LeaveDetailEntity getEmpLeaveDetail(UserEntity user, List<LeaveDetailEntity> leaveList) {
		for (LeaveDetailEntity leaveEntity : leaveList) {
			if (leaveEntity.getUser().getId() == user.getId()) {
				return leaveEntity;
			}
		}
		LeaveDetailEntity emptyLeaveDetail = new LeaveDetailEntity();
		emptyLeaveDetail.setUser(user);
		return emptyLeaveDetail;
	}

	@ApiMethod(name = "addLeaveApp", path = "addLeaveApp")
	public void addLeaveApp(LeaveAppEntity leaveApp) {
		ofy().save().entity(leaveApp).now();

	}

	@ApiMethod(name = "approveLeaveApp", path = "approveLeaveApp")
	public void approveLeaveApp(LeaveAppEntity leaveApp) {
		List<LeaveMasterEntity> leaveMastList = ofy().load().type(LeaveMasterEntity.class)
				.filter("user", leaveApp.getUser()).list();

		LeaveMasterEntity leaveMast = leaveMastList.size() > 0 ? leaveMastList.get(0) : null;
		if (leaveMast == null) {
			throw new RuntimeException("Leave Master record not found. Please contact HR department.");
		}

		leaveMast.setBalance(leaveMast.getBalance() - leaveApp.getTotalDays());
		updateLeaveMaster(leaveMast);
		leaveApp.setApproved(true);
		updateLeaveApp(leaveApp);
	}

	@ApiMethod(name = "updateLeaveApp", path = "updateLeaveApp")
	public void updateLeaveApp(LeaveAppEntity leaveApp) {
		leaveApp.setModifiedDate(new Date());
		Key<LeaveAppEntity> now = ofy().save().entity(leaveApp).now();
	}

	@ApiMethod(name = "getLeaveAppList")
	public List<LeaveAppEntity> getLeaveAppList() {

		List<LeaveAppEntity> leaveAppList = ofy().load().type(LeaveAppEntity.class).list();

		return leaveAppList;

	}

	@ApiMethod(name = "getLeaveAppListByUser", path = "getLeaveAppListByUser")
	public List<LeaveAppEntity> getLeaveAppListByUser(@Named("busId") Long busId, @Named("userId") Long userId) {
		UserService userService = new UserService();

		UserEntity user = userService.getUserByID(busId, userId);

		List<LeaveAppEntity> leaveList = ofy().load().type(LeaveAppEntity.class).filter("user", user).list();

		return leaveList;
	}

	@ApiMethod(name = "getLeaveAppListByManager", path = "getLeaveAppListByManager")
	public List<LeaveAppEntity> getLeaveAppListByManager(@Named("busId") Long busId, @Named("userId") Long userId) {
		UserService userService = new UserService();

		UserEntity user = userService.getUserByID(busId, userId);

		List<LeaveAppEntity> empLeaveAppList = ofy().load().type(LeaveAppEntity.class).filter("manager", user).list();

		return empLeaveAppList;
	}

	// LeaveMaster methods

	@ApiMethod(name = "addLeaveMaster", path = "addLeaveMaster")
	public void addLeaveMaster(LeaveMasterEntity leaveMasterEntity) {
		ofy().save().entity(leaveMasterEntity).now();

	}

	@ApiMethod(name = "getLeaveMasterList", path = "getLeaveMasterList")
	public List<LeaveMasterEntity> getLeaveMasterList() {

		List<LeaveMasterEntity> leaveMasterList = ofy().load().type(LeaveMasterEntity.class).list();

		return leaveMasterList;

	}

	@ApiMethod(name = "getLeaveMasterListByUser", path = "getLeaveMasterListByUser")
	public List<LeaveMasterEntity> getLeaveMasterListByUser(@Named("busId") Long busId, @Named("userId") Long userId) {
		UserService userService = new UserService();

		UserEntity user = userService.getUserByID(busId, userId);

		List<LeaveMasterEntity> leaveMasterListByUser = ofy().load().type(LeaveMasterEntity.class).filter("user", user)
				.list();

		return leaveMasterListByUser;
	}

	@ApiMethod(name = "updateLeaveMaster", path = "updateLeaveMaster")
	public void updateLeaveMaster(LeaveMasterEntity leaveMaster) {
		leaveMaster.setModifiedDate(new Date());
		Key<LeaveMasterEntity> now2 = ofy().save().entity(leaveMaster).now();
	}

	@ApiMethod(name = "getMonthlyPaymentByID")
	public MonthlyPaymentDetailEntity getMonthlyPaymentByID(@Named("bid") Long busId,
			@Named("month") String currentmonth, @Named("id") Long empid) {
		MonthlyPaymentDetailEntity monthlyPaymen = new MonthlyPaymentDetailEntity();
		List<MonthlyPaymentDetailEntity> monthlyPaymentDetailEntity = ofy().load()
				.type(MonthlyPaymentDetailEntity.class).ancestor(Key.create(BusinessEntity.class, busId))
				.filter("currentMonth", currentmonth).list();

		for (MonthlyPaymentDetailEntity monthlyPaymentDetailEntityId : monthlyPaymentDetailEntity) {
			if (monthlyPaymentDetailEntityId.getId().equals(empid)) {
				monthlyPaymen = monthlyPaymentDetailEntityId;
			}

			// System.out.println("monthlyPaymentDetailEntity" + monthlyPaymen);
		}

		return monthlyPaymen;

	}

	@ApiMethod(name = "fecthMonthlyPaymentByUser", path = "fecthMonthlyPaymentByUser")
	public List<MonthlyPaymentDetailEntity> fecthMonthlyPaymentByUser(UserEntity user) {

		List<MonthlyPaymentDetailEntity> monthlyPaymentDetailEntity = ofy().load()
				.type(MonthlyPaymentDetailEntity.class)
				.ancestor(Key.create(BusinessEntity.class, user.getBusiness().getId())).filter("empAccount", user)
				.list();

		return monthlyPaymentDetailEntity;

	}

	@ApiMethod(name = "getpayRollReport", path = "getpayRollReport")
	public List<PayRollMonthlyData> getpayRollReport(@Named("id") Long busId, @Named("year") String year) {

		List<PayRollMonthlyData> payrolldatalist = new ArrayList<PayRollMonthlyData>();

		String monthList[] = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		// int year = Calendar.getInstance().get(Calendar.YEAR);

		HrService hrService = new HrService();
		for (int i = 0; i < 12; i++) {
			String month = monthList[i] + "-" + year;
			List<MonthlyPaymentDetailEntity> monthlyPaymentDetailEntity = hrService.getMonthlyPayment(busId,
					month.trim());

			if (monthlyPaymentDetailEntity.size() > 0) {
				float totalSal = 0, totalPF = 0, totalPT = 0, totalCanteen = 0, totalIT = 0, totalESI = 0,
						totalOther = 0;
				for (int j = 0; j < monthlyPaymentDetailEntity.size(); j++) {
					totalSal += monthlyPaymentDetailEntity.get(j).getNetSalaryAmt();
					totalPF += monthlyPaymentDetailEntity.get(j).getPfDeductionAmt();
					totalPT += monthlyPaymentDetailEntity.get(j).getPtDeductionAmt();
					totalCanteen += monthlyPaymentDetailEntity.get(j).getCanteenDeductionAmt();
					totalIT += monthlyPaymentDetailEntity.get(j).getItDeductionAmt();
					totalESI += monthlyPaymentDetailEntity.get(j).getEsiDeductionAmt();
					totalOther += monthlyPaymentDetailEntity.get(j).getOtherDeductionAmt();
				}

				PayRollMonthlyData payData = new PayRollMonthlyData();
				payData.month = month.trim();
				payData.totalSal = totalSal;
				payData.totalPF = totalPF;
				payData.totalPT = totalPT;
				payData.totalCanteen = totalCanteen;
				payData.totalIT = totalIT;
				payData.totalESI = totalESI;
				payData.totalOther = totalOther;
				payrolldatalist.add(payData);
			}

		}
		return payrolldatalist;
	}

	@ApiMethod(name = "getpayRollReportByMonth", path = "getpayRollReportByMonth")
	public List<PayRollMonthlyData> getpayRollReportByMonth(@Named("id") Long busId, @Named("month") String month) {
		List<PayRollMonthlyData> payrolldatalist = new ArrayList<PayRollMonthlyData>();

		int year = Calendar.getInstance().get(Calendar.YEAR);

		HrService hrService = new HrService();

		String selectedMonth = month + "-" + year;
		List<MonthlyPaymentDetailEntity> monthlyPaymentDetailEntity = hrService.getMonthlyPayment(busId,
				selectedMonth.trim());

		if (monthlyPaymentDetailEntity.size() > 0) {
			float totalSal = 0, totalPF = 0, totalPT = 0, totalCanteen = 0, totalIT = 0, totalOther = 0, totalESI = 0;
			for (int j = 0; j < monthlyPaymentDetailEntity.size(); j++) {
				totalSal += monthlyPaymentDetailEntity.get(j).getCalculatedGrossSalary();
				totalPF += monthlyPaymentDetailEntity.get(j).getPfDeductionAmt();
				totalPT += monthlyPaymentDetailEntity.get(j).getPtDeductionAmt();
				totalCanteen += monthlyPaymentDetailEntity.get(j).getCanteenDeductionAmt();
				totalIT += monthlyPaymentDetailEntity.get(j).getItDeductionAmt();
				totalOther += monthlyPaymentDetailEntity.get(j).getOtherDeductionAmt();
				totalESI += monthlyPaymentDetailEntity.get(j).getEsiDeductionAmt();
			}

			PayRollMonthlyData payData = new PayRollMonthlyData();
			payData.month = selectedMonth.trim();
			payData.totalSal = totalSal;
			payData.totalPF = totalPF;
			payData.totalPT = totalPT;
			payData.totalCanteen = totalCanteen;
			payData.totalIT = totalIT;
			payData.totalOther = totalOther;
			payData.totalESI = totalESI;
			payrolldatalist.add(payData);

		}

		return payrolldatalist;
	}
}

class PayRollMonthlyData implements Serializable {
	String month;
	float totalSal;
	float totalPF;
	float totalPT;
	float totalCanteen;
	float totalIT;
	float totalOther;
	float totalESI;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public float getTotalPF() {
		return totalPF;
	}

	public void setTotalPF(float totalPF) {
		this.totalPF = totalPF;
	}

	public float getTotalPT() {
		return totalPT;
	}

	public void setTotalPT(float totalPT) {
		this.totalPT = totalPT;
	}

	public float getTotalCanteen() {
		return totalCanteen;
	}

	public void setTotalCanteen(float totalCanteen) {
		this.totalCanteen = totalCanteen;
	}

	public float getTotalIT() {
		return totalIT;
	}

	public void setTotalIT(float totalIT) {
		this.totalIT = totalIT;
	}

	public float getTotalOther() {
		return totalOther;
	}

	public void setTotalOther(float totalOther) {
		this.totalOther = totalOther;
	}

	public float getTotalESI() {
		return totalESI;
	}

	public void setTotalESI(float totalESI) {
		this.totalESI = totalESI;
	}

	public float getTotalSal() {
		return totalSal;
	}

	public void setTotalSal(float totalSal) {
		this.totalSal = totalSal;
	}

}
