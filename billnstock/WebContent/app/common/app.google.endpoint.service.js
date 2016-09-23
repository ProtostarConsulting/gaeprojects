angular.module("stockApp").factory('googleEndpointSF', googleEndpointSF);

function googleEndpointSF($log, $q) {

	var serviceFactory = {};

	var LocationService = {};

	serviceFactory.getLocationService = function() {
		return LocationService;
	}

	LocationService.saveLocation = function(store) {
		$log.debug("No2");
		var deferred = $q.defer();
		gapi.client.locationService.saveLocation(store).execute(function() {
			deferred.resolve({
				"msg" : "Location Successfully Added"
			});

		});
		$log.debug("No3");
		return deferred.promise;
	}

	LocationService.getAllLocation = function() {
		var deferred = $q.defer();
		gapi.client.locationService.getAllLocation().execute(function(resp) {
			deferred.resolve(resp.items);
		});
		return deferred.promise;
	}

	// ---------------------------user login------------------------------
	var UserService = {};

	serviceFactory.getUserService = function() {
		return UserService;
	}

	UserService.addUser = function(user) {
		$log.debug("No2");
		var deferred = $q.defer();
		gapi.client.userService.addUser(user).execute(function() {
			deferred.resolve({
				"msg" : "user Successfully Added"
			});

		});
		$log.debug("No3");
		return deferred.promise;
	}

	UserService.updateBusiStatus = function(update) {
		var deferred = $q.defer();
		gapi.client.userService.updateBusiStatus(update).execute(function() {
			deferred.resolve({
				"msg" : "Business Status Successfully Updated"
			});
		});
		return deferred.promise;
	}

	UserService.login = function(email, pass) {
		$log.debug("No2");
		var deferred = $q.defer();
		// gapi.client.userService.login(user).execute(function(resp) {
		gapi.client.userService.login({
			'email_id' : email,
			'password' : pass
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		$log.debug("No3");
		return deferred.promise;
	}

	UserService.getUser = function() {
		var deferred = $q.defer();
		gapi.client.userService.getUser().execute(function(resp) {
			$log.debug("getUser #resp :" + resp);
			deferred.resolve(resp.items);
		});
		return deferred.promise;
	}

	UserService.getUserByEmailID = function(email_id) {
		var deferred = $q.defer();
		gapi.client.userService.getUserByEmailID({
			'email_id' : email_id
		}).execute(function(resp) {
			$log.debug("resp:" + angular.toJson(resp));

			deferred.resolve(resp.result);
		});
		return deferred.promise;
	}
	UserService.getBusinessByEmailID = function(email_id) {
		var deferred = $q.defer();
		gapi.client.userService.getBusinessByEmailID({
			'adminEmailId' : email_id
		}).execute(function(resp) {
			$log.debug("resp:" + angular.toJson(resp));

			deferred.resolve(resp.result);
		});
		return deferred.promise;
	}

	UserService.getBusinessList = function() {
		var deferred = $q.defer();
		gapi.client.userService.getBusinessList().execute(function(resp) {
			$log.debug("getUser #resp :" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	UserService.getUsersByBusinessId = function(id) {
		var deferred = $q.defer();
		gapi.client.userService.getUsersByBusinessId({
			'id' : id
		}).execute(function(resp) {
			$log.debug("getUser #resp :" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	UserService.getbusinessById = function(id) {
		var deferred = $q.defer();
		gapi.client.userService.getBusinessById({
			'id' : id
		}).execute(function(resp) {
			$log.debug("getUser #resp :" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	UserService.updateUser = function(user) {
		$log.debug("No2");
		var deferred = $q.defer();
		gapi.client.userService.updateUser(user).execute(function() {
			deferred.resolve({
				"msg" : "user Successfully Updated"
			});
		});
		return deferred.promise;
	}

	UserService.getLoggedinUser = function() {
		var user = $localStorage.loggedinUser;
		if (user == 'undefined' || user == null)
			return null;
		else
			return $localStorage.loggedinUser;
	}

	UserService.logout = function() {
		$localStorage.loggedinUser = null;
	} // End of UserService

	// start business

	UserService.addBusiness = function(business) {
		var deferred = $q.defer();
		gapi.client.userService.addBusiness(business).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	UserService.isUserExists = function(emailid) {
		var deferred = $q.defer();
		gapi.client.userService.isUserExists({
			'email_id' : emailid
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	/*
	 * UserService.addNewBusiness = function(business) { var deferred =
	 * $q.defer();
	 * gapi.client.userService.addNewBusiness(business).execute(function() {
	 * deferred.resolve({ "msg" : "Business Add Successfully." }); }); return
	 * deferred.promise; }
	 */

	// ===============================start
	// ofaacGroupService======================================================//
	var AccountGroupService = {};
	serviceFactory.getAccountGroupService = function() {
		return AccountGroupService;
	}
	AccountGroupService.addAccountGroup = function(s1) {
		var deferred = $q.defer();
		gapi.client.accountGroupService.addAccountGroup(s1).execute(function() {
			deferred.resolve({
				"msg" : "AccountGroupService Added Successfully."
			});
		});
		return deferred.promise;
	}
	AccountGroupService.getAccountGroupList = function(id) {
		var deferred = $q.defer();
		gapi.client.accountGroupService.getAccountGroupList({
			"id" : id
		}).execute(function(resp) {

			deferred.resolve(resp.items);
			$log.debug("resp :" + resp);
		});
		return deferred.promise;
	}
	AccountGroupService.getAccountGroupListByType = function(type, bid) {
		var deferred = $q.defer();
		gapi.client.accountGroupService.getAccountGroupListByType({
			"type" : type,
			"bid" : bid
		}).execute(
				function(resp) {

					deferred.resolve(resp.items);
					$log.debug("resp getAccountGroupListByType :"
							+ angular.toJson(resp));
				});
		return deferred.promise;
	}

	AccountGroupService.updateAccountGrp = function(updateAccountGrp) {
		var deferred = $q.defer();
		gapi.client.accountGroupService.updateAccountGrp(updateAccountGrp)
				.execute(function(resp) {
					deferred.resolve(resp);
				});
		return deferred.promise;
	}
	AccountGroupService.deleteAccountGrp = function(id) {
		var deferred = $q.defer();
		gapi.client.accountGroupService.deleteAccountGrp({
			"id" : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	AccountGroupService.checkAccountGroupAlreadyExist = function(name) {
		var deferred = $q.defer();
		gapi.client.accountGroupService.checkAccountGrpAlreadyExist({
			"groupName" : name
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	// ==================================================================================================================================//

	// ---------------------------assetService--------------------------------

	var assetService = {};

	serviceFactory.getAssetManagementService = function() {
		return assetService;
	}

	assetService.addAsset = function(asset) {
		var deferred = $q.defer();
		gapi.client.assetService.addAsset(asset).execute(function() {
			deferred.resolve({
				"msg" : "assetService Added Successfully."
			});
		});
		return deferred.promise;
	}

	assetService.getallAsset = function(id) {
		var deferred = $q.defer();
		gapi.client.assetService.getallAsset({
			'id' : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}
	assetService.getselectedasset = function(id) {
		var deferred = $q.defer();
		gapi.client.assetService.getselectedasset({
			'id' : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	assetService.updateAsset = function(asset) {
		var deferred = $q.defer();
		gapi.client.assetService.addAsset(asset).execute(function() {
			deferred.resolve({
				"msg" : "assetService updated Successfully."
			});
		});
		return deferred.promise;
	}

	assetService.addAssignAsset = function(asset) {
		var deferred = $q.defer();
		gapi.client.assetService.addAssignAsset(asset).execute(function() {
			deferred.resolve({
				"msg" : "Asset Assign Successfully."
			});
		});
		return deferred.promise;
	}

	assetService.getselectedassetdetail = function(id) {
		var deferred = $q.defer();
		gapi.client.assetService.getselectedassetdetail({
			'id' : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	assetService.releaseAsset = function(id) {
		var deferred = $q.defer();
		gapi.client.assetService.releaseAsset({
			'id' : id
		}).execute(function() {
			deferred.resolve({
				"msg" : "Asset Release Successfully."
			});
		});
		return deferred.promise;
	}

	// --------------------pro adminservice---------------------

	var proadminService = {};
	serviceFactory.getproadminService = function() {
		return proadminService;
	}

	proadminService.addAccountType = function(account) {
		var deferred = $q.defer();
		gapi.client.proadminService.addAccountType(account).execute(function() {
			deferred.resolve({
				"msg" : "Account Added Successfully."
			});

		});
		return deferred.promise;
	}

	proadminService.updateAccountType = function(account) {
		var deferred = $q.defer();
		gapi.client.proadminService.addAccountType(account).execute(function() {
			deferred.resolve({
				"msg" : "Account Update Successfully."
			});

		});
		return deferred.promise;
	}

	proadminService.getallAccountType = function() {
		var deferred = $q.defer();
		gapi.client.proadminService.getallAccountType().execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	proadminService.getAccountTypeById = function(id) {
		var deferred = $q.defer();
		gapi.client.proadminService.getAccountTypeById({
			'id' : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	proadminService.initsetup = function() {
		var deferred = $q.defer();
		gapi.client.proadminService.initsetup().execute(function(resp) {
			deferred.resolve({
				"msg" : resp
			});
		});
		return deferred.promise;
	}

	proadminService.initsetupnext = function() {
		var deferred = $q.defer();
		gapi.client.proadminService.initsetupnext().execute(function(resp) {
			deferred.resolve({
				"msg" : resp
			});
		});
		return deferred.promise;
	}

	proadminService.creatAccountAndGroup = function(biz) {
		var deferred = $q.defer();
		gapi.client.proadminService.creatAccountAndGroup(biz).execute(
				function(resp) {
					deferred.resolve({
						"done" : resp
					});
				});
		return deferred.promise;
	}

	proadminService.getAllemp = function() {
		var deferred = $q.defer();
		gapi.client.proadminService.getAllemp().execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	/*
	 * proadminService.getfreeAccountTypeRecord=function() { var deferred =
	 * $q.defer();
	 * gapi.client.proadminService.getfreeAccountTypeRecord({'accountName':"Free"}).execute(function(resp) {
	 * deferred.resolve(resp); }); return deferred.promise; }
	 */

	// --------------------setup service----------------------------
	var setupService = {};

	serviceFactory.getsetupService = function() {
		return setupService;
	}

	setupService.getCurUserByEmailId = function(emailid) {
		var deferred = $q.defer();
		gapi.client.setupService.getCurUserByEmailId({
			'email_id' : emailid
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	setupService.updateBusiness = function(business) {
		var deferred = $q.defer();
		gapi.client.setupService.updateBusiness(business).execute(
				function(resp) {
					deferred.resolve(resp);
					/*
					 * deferred.resolve({ "msg" : "Business Updated
					 * Successfully" });
					 */
				});
		return deferred.promise;
	}

	setupService.updateUserStatus = function(update) {
		var deferred = $q.defer();
		gapi.client.setupService.updateUserStatus(update).execute(function() {
			deferred.resolve({
				"msg" : "User Status Successfully Updated"
			});
		});
		return deferred.promise;
	}

	setupService.getAllUserOfOrg = function(id) {
		var deferred = $q.defer();
		gapi.client.setupService.getAllUserOfOrg({
			'id' : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	setupService.getuserById = function(usrid) {
		var deferred = $q.defer();
		gapi.client.setupService.getuser({
			'id' : usrid
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	/*
	 * setupService.adduser= function(user) {
	 * 
	 * var deferred = $q.defer();
	 * gapi.client.setupService.adduser(user).execute(function() {
	 * 
	 * deferred.resolve({ "msg" : "user Added Successfully." });
	 * 
	 * }); return deferred.promise; }
	 */

	// --------------------hr services--------------------------------------
	var hrService = {};

	// Add Customer Service

	serviceFactory.gethrService = function() {
		return hrService;
	}

	hrService.addemp = function(emp) {

		var deferred = $q.defer();
		gapi.client.hrService.addemp(emp).execute(function() {

			deferred.resolve({
				"msg" : "employee Added Successfully."
			});

		});
		return deferred.promise;
	}

	hrService.getAllemp = function(id) {
		var deferred = $q.defer();
		gapi.client.hrService.getAllemp({
			'id' : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	hrService.getempByID = function(selectedempNo) {
		var deferred = $q.defer();
		gapi.client.hrService.getempByID({
			'id' : selectedempNo
		}).execute(function(resp) {
			/* $log.debug("internet cost:=" + resp); */
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	hrService.updateemp = function(emp) {

		var deferred = $q.defer();
		gapi.client.hrService.updateemp(emp).execute(function() {

			deferred.resolve({
				"msg" : "employee Updated Successfully."
			});

		});
		return deferred.promise;
	}

	hrService.addsalstruct = function(struct) {

		var deferred = $q.defer();
		gapi.client.hrService.addsalstruct(struct).execute(function() {

			deferred.resolve({
				"msg" : "struct Added Successfully."
			});

		});
		return deferred.promise;
	}

	hrService.findsalstructure = function(struct) {
		var deferred = $q.defer();
		gapi.client.hrService.findsalstructfromemp({
			'id' : struct
		}).execute(function(resp) {
			/* $log.debug("internet cost:=" + resp); */
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	hrService.getAllempsSalStruct = function(id) {
		var deferred = $q.defer();
		gapi.client.hrService.getAllempsSalStruct({
			'id' : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	hrService.viewfindsalstruct = function(struct) {
		var deferred = $q.defer();
		gapi.client.hrService.findsalstruct({
			'id' : struct
		}).execute(function(resp) {
			/* $log.debug("internet cost:=" + resp); */
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	hrService.updatesalinfo = function(struct) {

		var deferred = $q.defer();
		gapi.client.hrService.updatesalinfo(struct).execute(function() {

			deferred.resolve({
				"msg" : "struct Updated Successfully."
			});

		});
		return deferred.promise;
	}

	hrService.getstructByID = function(struct) {
		var deferred = $q.defer();
		gapi.client.hrService.findsalstruct({
			'id' : struct
		}).execute(function(resp) {
			/* $log.debug("internet cost:=" + resp); */
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	hrService.addgsalslip = function(salslip) {
		var deferred = $q.defer();
		gapi.client.hrService.addgsalslip(salslip).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	hrService.countOfRecordsiInganeratedslip = function(orgid) {
		var deferred = $q.defer();
		gapi.client.hrService.countofrecord({
			'id' : orgid
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	hrService.displyOnlySelected = function(curmonth, id) {
		var deferred = $q.defer();
		gapi.client.hrService.displyOnlySelected({
			'month' : curmonth,
			'id' : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	hrService.printslip = function(salslipid) {
		var deferred = $q.defer();
		gapi.client.hrService.printslip({
			'id' : salslipid
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	hrService.addtimesheet = function(timesheet) {
		var deferred = $q.defer();
		gapi.client.hrService.addtimesheet(timesheet).execute(function() {
			deferred.resolve({
				"msg" : "timesheet Added Successfully."
			});
		});
		return deferred.promise;
	}

	hrService.getcurweekdata = function(weekNumber) {
		var deferred = $q.defer();
		gapi.client.hrService.getcurweekdata({
			'week' : weekNumber
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	hrService.getallsalslip = function(curryear, id) {
		var deferred = $q.defer();
		gapi.client.hrService.getallsalslip({
			'year' : curryear,
			'id' : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	hrService.getAllcompany = function(id) {
		var deferred = $q.defer();
		gapi.client.hrService.getAllcompany({
			'id' : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	// ------------------------- CRM ---------------------------------
	var crmService = {};

	serviceFactory.getleadService = function() {
		return crmService;
	}
	crmService.addlead = function(lead) {

		var deferred = $q.defer();
		gapi.client.crmService.addlead(lead).execute(function() {

			deferred.resolve({
				"msg" : "Lead Added Successfully."
			});

		});
		return deferred.promise;
	}

	crmService.getAllleads = function(id) {
		var deferred = $q.defer();
		gapi.client.crmService.getAllleads({
			'id' : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	crmService.getLeadById = function(lead) {
		var deferred = $q.defer();
		gapi.client.crmService.getLeadById({
			'id' : lead
		}).execute(function(resp) {
			/* $log.debug("internet cost:=" + resp); */
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	crmService.addupdatetask = function(lead) {

		var deferred = $q.defer();
		gapi.client.crmService.addupdatetask(lead).execute(function() {

			deferred.resolve({
				"msg" : "Lead updated Successfully."
			});

		});
		return deferred.promise;
	}

	crmService.addcontact = function(contact) {
		var deferred = $q.defer();
		gapi.client.crmService.addcontact(contact).execute(function() {
			deferred.resolve({
				"msg" : "contact Added Successfully."
			});
		});
		return deferred.promise;
	}

	crmService.getAllcontact = function(id) {
		var deferred = $q.defer();
		gapi.client.crmService.getAllcontact({
			'id' : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	crmService.getContactById = function(contactNo) {
		var deferred = $q.defer();
		gapi.client.crmService.getContactById({
			'id' : contactNo
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}
	crmService.updatecontact = function(contact) {
		var deferred = $q.defer();
		gapi.client.crmService.updatecontact(contact).execute(function() {
			deferred.resolve({
				"msg" : "contact Updated Successfully."
			});
		});
		return deferred.promise;
	}

	crmService.getContactByCustomerId = function(CustId) {
		var deferred = $q.defer();
		gapi.client.crmService.getContactByCustomerId({
			'id' : CustId
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	crmService.isContactExists = function(email) {
		var deferred = $q.defer();
		gapi.client.crmService.isContactExists({
			'email' : email
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	// opportunity service

	var opportunityService = {};

	serviceFactory.getopportunityService = function() {
		return opportunityService;
	}

	opportunityService.addopportunity = function(opportunity) {
		var deferred = $q.defer();
		gapi.client.opportunityService.addopportunity(opportunity).execute(
				function() {
					deferred.resolve({
						"msg" : "opportunity Added Successfully."
					});

				});
		return deferred.promise;
	}
	opportunityService.getAllopportunity = function(id) {
		var deferred = $q.defer();
		gapi.client.opportunityService.getAllopportunity({
			'id' : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}
	opportunityService.getopportunityById = function(opportunityoid) {
		var deferred = $q.defer();
		gapi.client.opportunityService.getopportunityById({
			'id' : opportunityoid
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	opportunityService.addupdatetask = function(opportunity) {
		var deferred = $q.defer();
		gapi.client.opportunityService.addopportunity(opportunity).execute(
				function() {
					deferred.resolve({
						"msg" : "opportunity updated Successfully."
					});

				});
		return deferred.promise;
	}

	opportunityService.updateopportunity = function(opportunity) {
		var deferred = $q.defer();
		gapi.client.opportunityService.updateopportunity(opportunity).execute(
				function() {
					deferred.resolve({
						"msg" : "opportunity updated Successfully."
					});

				});
		return deferred.promise;
	}

	// ---------------------------------------------------------------
	// ADD INTERNET SERVICE

	var internetService = {};

	serviceFactory.getinternetService = function() {
		return internetService;
	}

	internetService.addinternet = function(internet) {
		var deferred = $q.defer();
		gapi.client.internetService.addinternet(internet).execute(
				function(resp) {
					$log.debug("addinternet#resp:" + resp);
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	internetService.findplan = function(rate) {
		var deferred = $q.defer();
		gapi.client.internetService.findplan({
			'rate' : rate
		}).execute(function(resp) {
			/* $log.debug("internet cost:=" + resp); */
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	// Add Customer Service

	var InternetService = {};

	serviceFactory.getInternetService = function() {
		return InternetService;
	}

	InternetService.addInternet = function(internet) {
		var deferred = $q.defer();
		gapi.client.internetService.addInternet(internet).execute(
				function(resp) {
					$log.debug("addInternet##Resp## at enpoint:", +resp);
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	InternetService.getAllInternet = function() {
		var deferred = $q.defer();
		gapi.client.internetService.getAllInternet().execute(function(resp) {
			$log.debug("getAllInternet#resp at enpoint:" + resp);
			deferred.resolve(resp.items);
		});
		return deferred.promise;
	}

	InternetService.searchRecord = function(plan) {
		var deferred = $q.defer();
		gapi.client.internetService.searchRecord({
			'plan' : plan
		}).execute(function(resp) {
			$log.debug("searchRecord at enpoint" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}// End of InternetService

	InternetService.searchByCost = function(cost) {
		var deferred = $q.defer();
		gapi.client.internetService.searchByCost({
			'cost' : cost
		}).execute(function(resp) {
			$log.debug("searchRecord By Cost at enpoint" + resp);
			deffered.resolve(resp);
		})
		return deferred.promise;
	}
	// =====================================================================================================================================
	// Add Customer Service

	var CustomerService = {};

	serviceFactory.getCustomerService = function() {
		return CustomerService;
	}

	CustomerService.addCustomer = function(cust) {
		var deferred = $q.defer();

		gapi.client.customerService.addCustomer(cust).execute(function(resp) {
			$log.debug("addCustomer#resp at enpoint:" + resp);
			deferred.resolve(resp);
		});

		return deferred.promise;
	}

	CustomerService.getAllCustomersByBusiness = function(id) {
		var deferred = $q.defer();
		gapi.client.customerService.getAllCustomersByBusiness({
			"id" : id
		}).execute(
				function(resp) {
					$log.debug("getAllCustomersByBusiness#resp at enpoint:"
							+ angular.toJson(resp));
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	CustomerService.getCustomerByID = function(Id) {
		var deferred = $q.defer();
		gapi.client.customerService.getCustomerByID({
			"Id" : Id
		}).execute(function(resp) {
			$log.debug("getCustomerByID at enpoint" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	CustomerService.updateCustomer = function(updates) {
		var deferred = $q.defer();

		gapi.client.customerService.updateCustomer(updates).execute(
				function(resp) {
					$log.debug("updates#resp at enpoint:" + resp);
					deferred.resolve(resp);
				});

		return deferred.promise;
	}

	CustomerService.isCustomerExists = function(email) {
		var deferred = $q.defer();
		gapi.client.customerService.isCustomerExists({
			"email" : email
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	// End of CustomerService

	// =====================================================================================================================================
	// Add Supplier Service

	var SupplierService = {};

	serviceFactory.getSupplierService = function() {
		return SupplierService;
	}

	SupplierService.addSupplier = function(cust) {
		var deferred = $q.defer();

		gapi.client.supplierService.addSupplier(cust).execute(function(resp) {
			$log.debug("addSupplier#resp at enpoint:" + resp);
			deferred.resolve(resp);
		});

		return deferred.promise;
	}

	SupplierService.getAllSuppliersByBusiness = function(id) {
		var deferred = $q.defer();
		gapi.client.supplierService.getAllSuppliersByBusiness({
			"id" : id
		}).execute(
				function(resp) {
					$log.debug("getAllSuppliersByBusiness#resp at enpoint:"
							+ angular.toJson(resp));
					deferred.resolve(resp.items);
				});
		return deferred.promise;
	}

	SupplierService.getSupplierByID = function(Id) {
		var deferred = $q.defer();
		gapi.client.supplierService.getSupplierByID({
			"id" : Id
		}).execute(function(resp) {
			$log.debug("getSupplierByID at enpoint" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	// End of SupplierService

	// ==*****************************************************************************^^^^^^^^^^^^^^^^^********************====================
	// ==*****************************************************************************^^^^^^^^^^^^^^^^^********************====================
	// ==*****************************************************************************^^^^^^^^^^^^^^^^^********************====================
	// ==*****************************************************************************^^^^^^^^^^^^^^^^^********************====================
	// Add Account Service

	var VoucherService = {};
	serviceFactory.getVoucherService = function() {
		return VoucherService;
	}

	VoucherService.addvoucher = function(vaccount) {
		var deferred = $q.defer();
		gapi.client.voucherService.addvoucher(vaccount).execute(function(resp) {
			$log.debug("addvoucher#resp at enpoint:" + resp);
			deferred.resolve(resp);

		});
		return deferred.promise;
	}

	VoucherService.getlistSalesVoucher = function(id) {
		var deferred = $q.defer();
		gapi.client.voucherService.getlistSalesVoucher({
			"id" : id
		}).execute(function(resp) {
			$log.debug("listvoucher#resp at enpoint:" + resp);
			deferred.resolve(resp.items);

		});
		return deferred.promise;
	}

	VoucherService.listVoucher = function(id) {
		var deferred = $q.defer();
		gapi.client.voucherService.listVoucher({
			"id" : id
		}).execute(function(resp) {
			$log.debug("listvoucher#resp at enpoint:" + resp);
			deferred.resolve(resp.items);

		});
		return deferred.promise;
	}

	VoucherService.getSalesListById = function(id) {
		var deferred = $q.defer();
		gapi.client.voucherService.getSalesListById(id).execute(function(resp) {
			$log.debug("listvoucher#resp at enpoint:" + resp);
			deferred.resolve(resp);

		});
		return deferred.promise;
	}

	VoucherService.getRecieptListById = function(id) {
		var deferred = $q.defer();
		gapi.client.voucherService.getRecieptListById(id).execute(
				function(resp) {
					$log.debug("listvoucher#resp at enpoint:" + resp);
					deferred.resolve(resp);

				});
		return deferred.promise;
	}

	VoucherService.addvoucherPurches = function(vaccount) {
		var deferred = $q.defer();
		gapi.client.voucherService.addvoucherPurches(vaccount).execute(
				function(resp) {
					$log.debug("addvoucherReciept#resp at enpoint:" + resp);
					deferred.resolve(resp);

				});
		return deferred.promise;
	}

	VoucherService.listVoucherPurches = function(id) {
		var deferred = $q.defer();
		gapi.client.voucherService.listVoucherPurches({
			"id" : id
		}).execute(function(resp) {
			$log.debug("listVoucherReciept#resp at enpoint:" + resp);
			deferred.resolve(resp.items);

		});
		return deferred.promise;
	}

	VoucherService.addvoucherReciept = function(vaccount) {
		var deferred = $q.defer();
		gapi.client.voucherService.addvoucherReciept(vaccount).execute(
				function(resp) {
					$log.debug("addvoucherReciept#resp at enpoint:" + resp);
					deferred.resolve(resp);

				});
		return deferred.promise;
	}

	VoucherService.listVoucherReciept = function(id) {
		var deferred = $q.defer();
		gapi.client.voucherService.listVoucherReciept({
			"id" : id
		}).execute(function(resp) {
			$log.debug("listVoucherReciept#resp at enpoint:" + resp);
			deferred.resolve(resp.items);

		});
		return deferred.promise;
	}

	VoucherService.addvoucherPurches = function(vaccount) {
		var deferred = $q.defer();
		gapi.client.voucherService.addvoucherPurches(vaccount).execute(
				function(resp) {
					$log.debug("addvoucherReciept#resp at enpoint:" + resp);
					deferred.resolve(resp);

				});
		return deferred.promise;
	}

	// ___________________________________________________________________________________________________________________________________________________________________________________________________
	var AccountService = {};

	serviceFactory.getAccountService = function() {
		return AccountService;
	}

	AccountService.addAccount = function(account) {
		var deferred = $q.defer();

		gapi.client.accountService.addAccount(account).execute(function(resp) {
			$log.debug("addAccount#resp at enpoint:" + resp);
			deferred.resolve(resp);
		});

		return deferred.promise;
	}

	// --------------------ADDACCOUNT--------------------------------@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@22@@@@@@@@@@@
	AccountService.addAccount1 = function(account) {
		var deferred = $q.defer();

		gapi.client.accountService.addAccount(account).execute(function(resp) {
			$log.debug("addAccount1#resp at enpoint:" + resp);
			deferred.resolve(resp);
		});

		return deferred.promise;
	}
	AccountService.updateAccount = function(updateAccount) {
		var deferred = $q.defer();
		gapi.client.accountService.updateAccount(updateAccount).execute(
				function(resp) {
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	AccountService.checkAccountAlreadyExist = function(accName) {
		var deferred = $q.defer();
		gapi.client.accountService.checkAccountAlreadyExist({
			"accountName" : accName
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	AccountService.getAccountList = function(id) {
		var deferred = $q.defer();
		gapi.client.accountService.getAccountList({
			"id" : id
		}).execute(function(resp) {

			deferred.resolve(resp.items);
		});
		return deferred.promise;
	}

	AccountService.getAccountListByGroupId = function(groupId, bid) {
		var deferred = $q.defer();
		gapi.client.accountService.getAccountListByGroupId({"id" : groupId}).execute(function(resp) {
			$log.debug("xx enpoint" + resp.items);
			deferred.resolve(resp.items);
		});
		return deferred.promise;
	}

	AccountService.getAccountBalance = function(id) {
		var deferred = $q.defer();
		gapi.client.accountService.getAccountBalance({
			"id" : id
		}).execute(function(resp) {
			$log.debug("balance " + angular.toJson(resp.returnBalance));
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	AccountService.getAccountById = function(accountId) {
		var deferred = $q.defer();
		gapi.client.accountService.getAccountById({
			"id" : accountId
		}).execute(function(resp) {
			$log.debug("getAccountById at enpoint" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	AccountService.deleteaccByid = function(id) {
		var deferred = $q.defer();
		gapi.client.accountService.deleteaccByid({
			"id" : id
		}).execute(function(resp) {
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	AccountService.getAllAccountsByBusiness = function(id) {
		var deferred = $q.defer();
		gapi.client.accountService.getAllAccountsByBusiness({
			"id" : id
		}).execute(
				function(resp) {
					$log.debug("getAllAccountsByCurrUser#resp at enpoint:"
							+ angular.toJson(resp));
					deferred.resolve(resp.items);
				});
		return deferred.promise;
	}

	AccountService.addPayable = function(payable) {
		var deferred = $q.defer();

		gapi.client.accountService.addPayable(payable).execute(function(resp) {
			$log.debug("addPayable#resp at enpoint:" + resp);
			deferred.resolve(resp);
		});

		return deferred.promise;
	}

	AccountService.getAllPayablesByBusiness = function(id) {
		var deferred = $q.defer();
		gapi.client.accountService.getAllPayablesByBusiness({
			"id" : id
		}).execute(
				function(resp) {
					$log.debug("getAllPayablesByBusiness#resp at enpoint:"
							+ angular.toJson(resp));
					deferred.resolve(resp.items);
				});
		return deferred.promise;

	}

	AccountService.getPayableByID = function(Id) {
		var deferred = $q.defer();
		gapi.client.accountService.getPayableByID({
			"id" : Id
		}).execute(function(resp) {
			$log.debug("getPayableByID at enpoint" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	AccountService.addReceivable = function(receivable) {
		var deferred = $q.defer();

		gapi.client.accountService.addReceivable(receivable).execute(
				function(resp) {
					$log.debug("addReceivable#resp at enpoint:" + resp);
					deferred.resolve(resp);
				});

		return deferred.promise;
	}

	AccountService.getAllReceivablesByBusiness = function(id) {
		var deferred = $q.defer();
		gapi.client.accountService.getAllReceivablesByBusiness({
			"id" : id
		}).execute(
				function(resp) {
					$log.debug("getAllReceivablesByBusiness#resp at enpoint:"
							+ angular.toJson(resp));
					deferred.resolve(resp.items);
				});
		return deferred.promise;
	}

	AccountService.getReceivableByID = function(Id) {
		var deferred = $q.defer();
		gapi.client.accountService.getReceivableByID({
			"id" : Id
		}).execute(function(resp) {
			$log.debug("getReceivableByID at enpoint" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}
	// End of AccountService

	/* =============================================================================================================================== */
	// Add AccountEntryService Service
	var AccountEntryService = {};

	serviceFactory.getAccountEntryService = function() {
		return AccountEntryService;
	}

	AccountEntryService.addAccountEntry = function(accountEntry) {
		var deferred = $q.defer();
		gapi.client.accountEntryService.addAccountEntry(accountEntry).execute(
				function(resp) {
					$log.debug("accountEntryService  at enpoint:" + resp);
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	AccountEntryService.getAccountEntryList = function() {
		var deferred = $q.defer();
		gapi.client.accountEntryService.getAccountEntryList().execute(
				function(resp) {
					$log.debug("List at enpoint" + resp.items);
					deferred.resolve(resp.items);
				});
		return deferred.promise;
	}

	AccountEntryService.getAccountEntryByAccountId = function(accId) {// },bid)
																		// {
		var deferred = $q.defer();
		gapi.client.accountEntryService.getAccountEntryByAccountId({
			"id" : accId
		}).execute(function(resp) {
			$log.debug("accountEntryService enpoint" + resp.items);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	/* =============================================================================================================================== */

	// Add GeneralEntryService Service
	var GeneralEntryService = {};

	serviceFactory.getGeneralEntryService = function() {
		return GeneralEntryService;
	}

	GeneralEntryService.addGeneralEntry = function(accountEntry) {
		var deferred = $q.defer();
		gapi.client.generalEntryService.addGeneralEntry(accountEntry).execute(
				function(resp) {
					$log.debug("GeneralEntryService  at enpoint:" + resp);
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	GeneralEntryService.getGeneralEntryList = function(id) {
		var deferred = $q.defer();
		gapi.client.generalEntryService.getGeneralEntryList({
			"id" : id
		}).execute(function(resp) {
			$log.debug("List at enpoint" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	GeneralEntryService.getGeneralEntryList = function() {
		var deferred = $q.defer();
		gapi.client.generalEntryService.getGeneralEntryList().execute(
				function(resp) {
					$log.debug("getgenerallist#resp at enpoint:"
							+ angular.toJson(resp));
					deferred.resolve(resp.items);
				});
		return deferred.promise;
	}

	/* =============================================================================================================================== */

	// Add GeneralJournal Service
	var GeneralJournalService = {};

	serviceFactory.getGeneralJournalService = function() {
		return GeneralJournalService;
	}

	GeneralJournalService.addJournal = function(journal) {
		var deferred = $q.defer();

		gapi.client.generalJournalService.addJournal(journal).execute(
				function(resp) {
					$log.debug("generalJournalService  at enpoint:" + resp);
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	GeneralJournalService.getAllJournalList = function() {
		var deferred = $q.defer();
		gapi.client.generalJournalService.getAllJournalList().execute(
				function(resp) {
					$log.debug("List at enpoint" + resp);
					deferred.resolve(resp);
				});
		return deferred.promise;
	}
	/* =============================================================================================================================== */
	// Start of StockService
	var StockService = {};

	serviceFactory.getStockService = function() {
		return StockService;
	}

	StockService.addStock = function(stock) {
		var deferred = $q.defer();
		gapi.client.stockService.addStock(stock).execute(function(resp) {
			$log.debug("addStock#resp at enpoint:" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	StockService.getAllStock = function(id) {
		var deferred = $q.defer();
		gapi.client.stockService.getAllStock({
			"id" : id
		}).execute(function(resp) {
			$log.debug("getAllStock#resp at enpoint:" + resp);
			deferred.resolve(resp.items);
		});
		return deferred.promise;
	}

	StockService.updateStock = function(updateStock) {
		var deferred = $q.defer();
		gapi.client.stockService.updateStock(updateStock).execute(
				function(resp) {
					$log.debug("updateStock#resp at enpoint:" + resp);
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	StockService.getStockById = function(Id) {
		var deferred = $q.defer();
		gapi.client.stockService.getStockById({
			"id" : Id
		}).execute(function(resp) {
			$log.debug("getStockById at enpoint" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	StockService.getReportByThreshold = function(id) {
		var deferred = $q.defer();
		gapi.client.stockService.getReportByThreshold({
			"id" : id
		}).execute(function(resp) {
			$log.debug("getReportByThreshold#resp at enpoint:" + resp);
			deferred.resolve(resp.items);
		});
		return deferred.promise;
	} // End of StockService

	/* =============================================================================================================================== */

	// Start of StockService
	var TaxService = {};

	serviceFactory.getTaxService = function() {
		return TaxService;
	}

	TaxService.addTax = function(tax) {
		var deferred = $q.defer();
		gapi.client.taxService.addTax(tax).execute(function(resp) {
			$log.debug("addTax#resp at enpoint:" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	TaxService.getAllTaxes = function(id) {
		var deferred = $q.defer();
		gapi.client.taxService.getAllTaxes({
			"id" : id
		}).execute(function(resp) {
			$log.debug("getAllTaxes#resp at enpoint:" + resp);
			deferred.resolve(resp.items);
		});
		return deferred.promise;
	}

	TaxService.getTaxesByVisibility = function(id) {
		var deferred = $q.defer();
		gapi.client.taxService.getTaxesByVisibility({
			"id" : id
		}).execute(function(resp) {
			$log.debug("getTaxesByVisibility#resp at enpoint:" + resp);
			deferred.resolve(resp.items);
		});
		return deferred.promise;
	}

	TaxService.updateTax = function(tax) {
		var deferred = $q.defer();
		gapi.client.taxService.updateTax(tax).execute(function(resp) {
			$log.debug("updateTax#resp at enpoint:" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	TaxService.disableTax = function(tax) {
		var deferred = $q.defer();
		gapi.client.taxService.disableTax(tax).execute(function(resp) {
			$log.debug("disableTax#resp at enpoint:" + resp);
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	// End of TaxService

	/* =============================================================================================================================== */

	// Start of StockService
	var InvoiceService = {};

	serviceFactory.getInvoiceService = function() {
		return InvoiceService;
	}

	InvoiceService.updateInvoiceStatus = function(valueToUpdateStatus) {
		var deferred = $q.defer();

		gapi.client.invoiceService.updateInvoiceStatus(valueToUpdateStatus)
				.execute(function(resp) {
					$log.debug("UpdateInvoice#resp at enpoint:" + resp);
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	InvoiceService.addInvoice = function(invoice) {
		var deferred = $q.defer();

		gapi.client.invoiceService.addInvoice(invoice).execute(function(resp) {
			$log.debug("addInvoice#resp at enpoint:" + resp);
			deferred.resolve(resp);
		});

		return deferred.promise;
	}

	InvoiceService.getAllInvoice = function(id) {
		var deferred = $q.defer();
		gapi.client.invoiceService.getAllInvoice({
			"id" : id
		}).execute(function(resp) {
			$log.debug("getAllInvoice#resp at enpoint:" + resp);
			deferred.resolve(resp.items);
		});
		return deferred.promise;
	}

	InvoiceService.getReportByTaxReceived = function(id) {
		var deferred = $q.defer();
		gapi.client.invoiceService.getReportByTaxReceived({
			"id" : id
		}).execute(function(resp) {
			// $log.debug("getinvoiceByID at enpoint" + angular.toJson(resp));
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	InvoiceService.getinvoiceByID = function(id) {
		var deferred = $q.defer();
		gapi.client.invoiceService.getinvoiceByID({
			"id" : id
		}).execute(function(resp) {
			// $log.debug("getinvoiceByID at enpoint" + angular.toJson(resp));
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	InvoiceService.getInvoiceListByCustId = function(id) {
		var deferred = $q.defer();
		gapi.client.invoiceService.getInvoiceListByCustId({
			"id" : id
		}).execute(function(resp) {
			$log.debug("getInvoiceListByCustId at enpoint" + resp.items);
			deferred.resolve(resp.items);
		});
		return deferred.promise;
	}

	InvoiceService.addInvoiceSettings = function(settings) {
		var deferred = $q.defer();

		gapi.client.invoiceService.addInvoiceSettings(settings).execute(
				function(resp) {
					$log.debug("addInvoiceSettings#resp at enpoint:" + resp);
					deferred.resolve(resp);
				});

		return deferred.promise;
	}

	InvoiceService.getInvoiceSettingsByBiz = function(id) {
		var deferred = $q.defer();
		gapi.client.invoiceService.getInvoiceSettingsByBiz({
			"id" : id
		}).execute(
				function(resp) {
					$log.debug("getInvoiceSettingsByBiz#resp at enpoint:"
							+ angular.toJson(resp));
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	InvoiceService.getAllPayableInvoices = function(id) {
		var deferred = $q.defer();
		gapi.client.invoiceService.getAllPayableInvoices({
			"id" : id
		}).execute(
				function(resp) {
					$log.debug("getAllPayableInvoices#resp at enpoint:"
							+ angular.toJson(resp));
					deferred.resolve(resp.items);
				});
		return deferred.promise;
	}// End of InvoiceService

	/* =============================================================================================================================== */

	// Start of SalesOrderService
	var SalesOrderService = {};

	serviceFactory.getSalesOrderService = function() {
		return SalesOrderService;
	}

	SalesOrderService.addSalesOrder = function(salesOrder) {
		var deferred = $q.defer();
		gapi.client.salesOrderService.addSalesOrder(salesOrder).execute(
				function(resp) {
					$log.debug("addSalesOrder#resp at enpoint:" + resp);
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	SalesOrderService.getAllSalesOrder = function(id) {
		var deferred = $q.defer();
		gapi.client.salesOrderService.getAllSalesOrder({
			"id" : id
		}).execute(function(resp) {
			// $log.debug("getAllSalesOrder#resp at enpoint:" +
			// angular.toJson(resp));
			deferred.resolve(resp.items);
		});
		return deferred.promise;
	}

	SalesOrderService.getSOByID = function(id) {
		var deferred = $q.defer();
		gapi.client.salesOrderService.getSOByID({
			"id" : id
		}).execute(function(resp) {
			$log.debug("getSOByID at enpoint" + angular.toJson(resp));
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	SalesOrderService.getSOListByID = function(id) {
		var deferred = $q.defer();
		gapi.client.salesOrderService.getSOListByID({
			"id" : id
		}).execute(function(resp) {
			$log.debug("getSOListByID at enpoint" + angular.toJson(resp));
			deferred.resolve(resp.items);
		});
		return deferred.promise;
	}
	/* =============================================================================================================================== */

	// Start of PurchaseOrderService
	var PurchaseOrderService = {};

	serviceFactory.getPurchaseOrderService = function() {
		return PurchaseOrderService;
	}

	PurchaseOrderService.addPurchaseOrder = function(purchaseOrder) {
		var deferred = $q.defer();
		gapi.client.purchaseOrderService.addPurchaseOrder(purchaseOrder)
				.execute(function(resp) {
					$log.debug("addPurchaseOrder at enpoint:" + resp);
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	PurchaseOrderService.getAllPurchaseOrder = function(id) {
		var deferred = $q.defer();

		gapi.client.purchaseOrderService.getAllPurchaseOrder({
			"id" : id
		}).execute(function(resp) {
			$log.debug("getAllPurchaseOrder at enpoint:" + resp)
			deferred.resolve(resp.items)
		});
		return deferred.promise;
	}

	PurchaseOrderService.getPOByID = function(id) {
		var deferred = $q.defer();
		gapi.client.purchaseOrderService.getPOByID({
			"id" : id
		}).execute(function(resp) {
			$log.debug("getPOByID at enpoint" + angular.toJson(resp));
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	/* =============================================================================================================================== */

	// Start of WarehouseManagement
	var warehouseManagementService = {};

	serviceFactory.getWarehouseManagementService = function() {
		return warehouseManagementService;
	}

	warehouseManagementService.addWarehouse = function(add) {
		var deferred = $q.defer();
		gapi.client.warehouseManagementService.addWarehouse(add).execute(
				function(resp) {
					$log.debug("addWarehouse at enpoint:" + resp);
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	warehouseManagementService.getAllWarehouseByBusiness = function(id) {
		var deferred = $q.defer();

		gapi.client.warehouseManagementService.getAllWarehouseByBusiness({
			"id" : id
		}).execute(
				function(resp) {
					$log.debug("getAllWarehouseByBusiness at enpoint:"
							+ angular.toJson(resp));
					deferred.resolve(resp.items);
				});

		gapi.client.warehouseManagementService.getAllWarehouseByBusiness({
			"id" : id
		}).execute(function(resp) {
			$log.debug("getAllWarehouseByBusiness at enpoint:" + resp);
			deferred.resolve(resp.items);
		});

		return deferred.promise;
	}

	warehouseManagementService.getWarehouseById = function(id) {
		var deferred = $q.defer();
		gapi.client.warehouseManagementService.getWarehouseById({
			"id" : id
		}).execute(function(resp) {
			$log.debug("getWarehouseById at enpoint" + angular.toJson(resp));
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	warehouseManagementService.updateWarehouse = function(updateWarehouse) {
		var deferred = $q.defer();
		gapi.client.warehouseManagementService.updateWarehouse(updateWarehouse)
				.execute(function(resp) {
					$log.debug("updateWarehouse#resp at enpoint:" + resp);
					deferred.resolve(resp);
				});
		return deferred.promise;
	}
	// End of WarehouseService
	/* =============================================================================================================================== */

	// Start of UploddeurlServices

	var uploadUrlService = {};
	serviceFactory.getuploadURLService = function() {
		return uploadUrlService;
	}
	uploadUrlService.getLogUploadURL = function() {
		var deferred = $q.defer();
		gapi.client.uploadUrlService.getLogUploadURL().execute(function(resp) {
			$log.debug("getURL at enpoint" + angular.toJson(resp));
			deferred.resolve(resp);
		});
		return deferred.promise;
	}

	uploadUrlService.getLogUploadFooterURL = function() {
		var deferred = $q.defer();
		gapi.client.uploadUrlService.getLogUploadFooterURL().execute(
				function(resp) {
					deferred.resolve(resp);
				});
		return deferred.promise;
	}
	uploadUrlService.getExcelUploadURL = function() {
		var deferred = $q.defer();
		gapi.client.uploadUrlService.getExcelUploadURL().execute(
				function(resp) {
					deferred.resolve(resp);
				});
		return deferred.promise;
	}
	uploadUrlService.getExcelStockUploadURL = function() {
		var deferred = $q.defer();
		gapi.client.uploadUrlService.getExcelStockUploadURL().execute(
				function(resp) {
					deferred.resolve(resp);
				});
		return deferred.promise;
	}
	uploadUrlService.getCustomerExcelUploadURL = function() {
		var deferred = $q.defer();
		gapi.client.uploadUrlService.getCustomerExcelUploadURL().execute(
				function(resp) {
					deferred.resolve(resp);
				});
		return deferred.promise;
	}
	uploadUrlService.getAccountExcelUploadURL = function() {
		var deferred = $q.defer();
		gapi.client.uploadUrlService.getAccountExcelUploadURL().execute(
				function(resp) {
					deferred.resolve(resp);
				});
		return deferred.promise;
	}

	/* =============================================================================================================================== */

	return serviceFactory;
}
