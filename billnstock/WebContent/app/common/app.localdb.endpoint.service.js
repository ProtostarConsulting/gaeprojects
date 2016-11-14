angular.module("stockApp").factory('localDBServiceFactory',
		localDBServiceFactory);

function localDBServiceFactory($log, $q, $timeout, $localStorage) {

	var serviceFactory = {};

	
	//---------------------------------------userlogin --------------------
	
	var UserService = {};

	 serviceFactory.getUserService = function() {
	  return UserService;
	 }

	 UserService.addUser = function(user) {

	  var deferred = $q.defer();
	  $timeout(function() {

	   $log.debug("In side local DB addLogin...");
	   var userList = angular.fromJson($localStorage.dbUser);
	   if (typeof userList === 'undefined')
	    userList = [];
	   user.userId = userList.length + 1;
	   userList.push(user);
	   $localStorage.dbUser = angular.toJson(userList);
	   deferred.resolve({
	    "msg" : "User added Successfully."
	   });

	  }, 1000);

	  return deferred.promise;
	 }

	 UserService.getUsers = function() {
	  var deferred = $q.defer();
	  $timeout(function() {
	   $log.debug("In side local DB getLogin...");
	   var userList = angular.fromJson($localStorage.dbUser);
	   if (typeof userList === 'undefined')
	    userList = [];
	   deferred.resolve(userList);
	  }, 1000);

	  return deferred.promise;

	 }

	 UserService.login = function(userName, pwd) {
	  var deferred = $q.defer();
	  $timeout(function() {
	   var loggedin = false;
	   var userList = angular.fromJson($localStorage.dbUser);
	   if (typeof userList === 'undefined')
	    userList = [];

	   for (i = 0; i < userList.length; i++) {
	    if (userList[i].userName === userName
	      && userList[i].pwd === pwd) {
	     $localStorage.loggedinUser = userList[i];
	     deferred.resolve(true);
	    }
	   }
	   deferred.resolve(false);
	  }, 1000);

	  return deferred.promise;

	 }

	 UserService.saveLoggedInUser = function(user) {
	  $localStorage.loggedinUser = user;

	 }
	 
	 UserService.logout = function() {
	  $localStorage.loggedinUser = null;
	 }

	 UserService.getLoggedinUser = function() {
	  var user = $localStorage.loggedinUser;
	  if (user == 'undefined' || user == null)
	   return null;
	  else
	   return $localStorage.loggedinUser;
	 }

	 UserService.getUserById = function(userId) {
	  var foundUser;
	  var userList = angular.fromJson($localStorage.dbUser);
	  for (i = 0; i < userList.length; i++) {
	   if (userId == userList[i].userId) {
	    foundUser = userList[i];
	    break;
	   }

	  }

	  return foundUser;
	 }

	 UserService.updateProfile = function(editProfile) {
	  var deferred = $q.defer();
	  $timeout(function() {

	   $log.debug("In side updated local DB ...");
	   var userList = angular.fromJson($localStorage.dbUser);
	   if (typeof userList === 'undefined')
	    userList = [];
	   for (var i = 0; i < userList.length; i++) {
	    if (editProfile.userId == userList[i].userId)
	     userList[i] = editProfile;
	   }
	   $localStorage.dbUser = angular.toJson(userList);
	   deferred.resolve({
	    "msg" : "User data Updated Successfully."
	   });

	  }, 1000);

	  return deferred.promise;
	 }
	
	//-------------------------------------------------------------------
	
	
	// Add Customer Service
	var CustomerService = {};

	
	serviceFactory.getCustomerService = function() {
		return CustomerService;
	}

	CustomerService.addCustomer = function(cust) {

		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side local DB addCustomer...");
			var custList = angular.fromJson($localStorage.dbCustomers);
			if (typeof custList === 'undefined')
				custList = [];
			cust.customerId = custList.length + 1;
			custList.push(cust);
			$localStorage.dbCustomers = angular.toJson(custList);
			deferred.resolve({
				"msg" : "Customer Added Successfully."
			});

		}, 1000);

		return deferred.promise;
	}

	CustomerService.getCustomerByID = function(selectedCustomerId) {

		var deferred = $q.defer();
		$timeout(function() {
			var tempItem = [];

			var custList = angular.fromJson($localStorage.dbCustomers);

			if (typeof custList === 'undefined')
				custList = [];

			for (i = 0; i < custList.length; i++) {
				if (selectedCustomerId == custList[i].customerId) {

					// selectedBillNo = invoiceList[i];
					tempItem.push(custList[i]);

					// $log.debug("TEMP===" + tempItem[i]);
				}
			}
			deferred.resolve(tempItem);

		}, 1000);
		return deferred.promise;
	}

	CustomerService.getAllCustomers = function() {
		var deferred = $q.defer();
		$timeout(function() {
			$log.debug("In side local DB getCustomers...");
			var custList = angular.fromJson($localStorage.dbCustomers);
			if (typeof custList === 'undefined')
				custList = [];
			deferred.resolve(custList);
		}, 1000);

		return deferred.promise;

	} // End of CustomerService

	//*************************************************************************************************************************		
	// Add hr Service
	var hrService = {};

	serviceFactory.gethrService = function() {
		return hrService;
	}

	hrService.addemp = function(emp) {

		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side local DB addemp...");
			var empList = angular.fromJson($localStorage.dbemp);
			if (typeof empList === 'undefined')
				empList = [];
			empList.push(emp);
			$localStorage.dbemp = angular.toJson(empList);
			deferred.resolve({
				"msg" : "employee Added Successfully."
			});

		}, 1000);

		return deferred.promise;
	}

	hrService.getAllemp = function() {
		var deferred = $q.defer();
		$timeout(function() {
			$log.debug("In side local DB getemp...");
			var empList = angular.fromJson($localStorage.dbemp);
			if (typeof empList === 'undefined')
				empList = [];
			deferred.resolve(empList);
		}, 1000);

		return deferred.promise;

	}

	hrService.updateemp = function(editProfile) {
		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side updated local DB updateuser...");
			var empList = angular.fromJson($localStorage.dbemp);
			if (typeof empList === 'undefined')
				empList = [];

			for (var i = 0; i < empList.length; i++) {
				if (editProfile.empid == empList[i].empid)
					empList[i] = editProfile;
			}

			$localStorage.dbemp = angular.toJson(empList);
			deferred.resolve({
				"msg" : "User data Updated Successfully."
			});

		}, 1000);

		return deferred.promise;
	}

	hrService.getempByID = function(selectedempNo) {

		var deferred = $q.defer();
		$timeout(function() {
			var tempItem = [];
			var empList = angular.fromJson($localStorage.dbemp);

			if (typeof empList === 'undefined')
				empList = [];

			for (i = 0; i < empList.length; i++) {
				if (selectedempNo == empList[i].empid) {
					tempItem.push(empList[i]);

				}
			}
			deferred.resolve(tempItem);

		}, 1000);
		return deferred.promise;
	}

	hrService.addsalstruct = function(salstruct) {

		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side local DB addemp...");
			var salstructList = angular.fromJson($localStorage.dbsalstruct);
			if (typeof salstructList === 'undefined')
				salstructList = [];
			salstructList.push(salstruct);
			$localStorage.dbsalstruct = angular.toJson(salstructList);
			deferred.resolve({
				"msg" : "employee Salary Structure Added Successfully."
			});

		}, 1000);

		return deferred.promise;
	}

	hrService.findsalstruct = function(empid) {

		var deferred = $q.defer();
		$timeout(function() {
			var tempItem = [];
			var structList = angular.fromJson($localStorage.dbsalstruct);

			if (typeof structList === 'undefined')
				structList = [];

			for (i = 0; i < structList.length; i++) {
				if (empid == structList[i].empid) {
					tempItem.push(structList[i]);

				}
			}
			deferred.resolve(tempItem);

		}, 1000);
		return deferred.promise;
	}

	hrService.updatesalinfo = function(editsalstruct) {
		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side updated local DB updateuser...");
			var salstructList = angular.fromJson($localStorage.dbsalstruct);
			if (typeof salstructList === 'undefined')
				salstructList = [];

			for (var i = 0; i < salstructList.length; i++) {
				if (editsalstruct.empid == salstructList[i].empid)
					salstructList[i] = editsalstruct;
			}

			$localStorage.dbsalstruct = angular.toJson(salstructList);
			deferred.resolve({
				"msg" : "User salary structure List Updated Successfully."
			});

		}, 1000);

		return deferred.promise;
	}

	hrService.adddoc = function(document) {

		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side local DB adddocumentr...");
			var documentList = angular.fromJson($localStorage.dbdocument);
			if (typeof documentList === 'undefined')
				documentList = [];
			documentList.push(document);
			$localStorage.dbdocument = angular.toJson(documentList);
			deferred.resolve({
				"msg" : "document Added Successfully."
			});

		}, 1000);

		return deferred.promise;
	}

	hrService.getstructByID = function(selectedempNo) {

		var deferred = $q.defer();
		$timeout(function() {
			var tempItem = [];
	
			var empstructList = angular.fromJson($localStorage.dbsalstruct);
		

			if (typeof empstructList === 'undefined')
				empstructList = [];
		

			for (i = 0; i < empstructList.length; i++) {
				if (selectedempNo == empstructList[i].empid) {
					tempItem.push(empstructList[i]);

				}
			}
			
			deferred.resolve(tempItem);
			

		}, 1000);
		return deferred.promise;
	}

	hrService.getAllempsSalStruct = function() {
		var deferred = $q.defer();
		$timeout(function() {
			$log.debug("In side get local DB dbsalstruct...");
			var empstructList = angular.fromJson($localStorage.dbsalstruct);
			if (typeof empstructList === 'undefined')
				empstructList = [];
			deferred.resolve(empstructList);
		}, 1000);

		return deferred.promise;

	}

	hrService.viewfindsalstruct = function(empid) {

		var deferred = $q.defer();
		$timeout(function() {
			var tempItem = [];
			var structList = angular.fromJson($localStorage.dbsalstruct);

			if (typeof structList === 'undefined')
				structList = [];

			for (i = 0; i < structList.length; i++) {
				if (empid == structList[i].empid) {
					tempItem.push(structList[i]);
				}
			}
			deferred.resolve(tempItem);

		}, 1000);
		return deferred.promise;
	}

	hrService.addgsalslip = function(salslip) {

		var deferred = $q.defer();
		//$timeout(function() {

			$log.debug("In side local DB addgsalslip...");
			var salslipList = angular.fromJson($localStorage.dbsalslip);
			if (typeof salslipList === 'undefined')
				salslipList = [];
			
			salslip.salslip_id = salslipList.length + 100;
			salslipList.push(salslip);
			$localStorage.dbsalslip = angular.toJson(salslipList);

		//}, 1000);

		return deferred.promise;
	}
	
	hrService.countOfRecordsiInganeratedslip = function() {
		var deferred = $q.defer();
		$timeout(function() {
			$log.debug("In side get local DB printganeratesalslip...");
			var empsalslipList = angular.fromJson($localStorage.dbsalslip);
			if (typeof empsalslipList === 'undefined')
				empsalslipList = [];
			deferred.resolve(empsalslipList);
		}, 1000);

		return deferred.promise;

	}
	hrService.displyOnlySelected= function(currmionth) {
		
		var deferred = $q.defer();
	//	$timeout(function() {
			var tempItem = [];
			var seletedtList = angular.fromJson($localStorage.dbsalslip);

			if (typeof seletedtList === 'undefined')
				seletedtList = [];

			for (i = 0; i < seletedtList.length; i++) {
				if (currmionth == seletedtList[i].month) {
					tempItem.push(seletedtList[i]);
				}
			}
			deferred.resolve(tempItem);

	//	}, 1000);
		return deferred.promise;
	}

	
	hrService.printslip = function(empid) {

		var deferred = $q.defer();
		$timeout(function() {
			var tempItem = [];
			var salslipList = angular.fromJson($localStorage.dbsalslip);

			if (typeof salslipList === 'undefined')
				salslipList = [];

			for (i = 0; i < salslipList.length; i++) {
				if (empid == salslipList[i].salslip_id) {
					tempItem.push(salslipList[i]);
				}
			}
			deferred.resolve(tempItem);

		}, 1000);
		return deferred.promise;
	}
	
	
	
	hrService.getallsalslip = function(empid,curryear) {

		var deferred = $q.defer();
		$timeout(function() {
			var tempItem = [];
			var salslipList = angular.fromJson($localStorage.dbsalslip);

			if (typeof salslipList === 'undefined')
				salslipList = [];

			for (i = 0; i < salslipList.length; i++) {
				if (empid == salslipList[i].salarystruct.empid && curryear == salslipList[i].year) {
					tempItem.push(salslipList[i]);
				}
			}
			deferred.resolve(tempItem);

		}, 1000);
		return deferred.promise;
	}

	
	// End of hrService

	//*************************************************************************************************************************		

	//start leadService
	var leadService = {};

	serviceFactory.getleadService = function() {
		return leadService;
	}

	leadService.addlead = function(lead) {

		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side local DB leademp...");
			var leadList = angular.fromJson($localStorage.dblead);
			if (typeof leadList === 'undefined')
				leadList = [];
			leadList.push(lead);
			$localStorage.dblead = angular.toJson(leadList);
			deferred.resolve({
				"msg" : "lead Added Successfully."
			});

		}, 1000);

		return deferred.promise;
	}
	
	leadService.getAllleads = function() {
		var deferred = $q.defer();
		$timeout(function() {
			$log.debug("In side local DB getlead...");
			var leadList = angular.fromJson($localStorage.dblead);
			if (typeof leadList === 'undefined')
				leadList = [];
			deferred.resolve(leadList);
		}, 1000);

		return deferred.promise;

	}
	
	
	leadService.getLeadById= function(selectedleadNo) {

		var deferred = $q.defer();
		$timeout(function() {
			var tempItem = [];
	
			var leadList = angular.fromJson($localStorage.dblead);
		

			if (typeof leadList === 'undefined')
				leadList = [];
		

			for (i = 0; i < leadList.length; i++) {
				if (selectedleadNo == leadList[i].id) {
					tempItem.push(leadList[i]);

				}
			}
			
			deferred.resolve(tempItem);
			

		}, 1000);
		return deferred.promise;
	}
	
	
	
	leadService.addupdatetask = function(taskobj,leadid) {
		var deferred = $q.defer();
		$timeout(
				function() {

					$log.debug("In side local DB updateStock...");
					var leadList = angular.fromJson($localStorage.dblead);

					if (typeof leadList === 'undefined')
						stockList = [];

					for (var i = 0; i < leadList.length; i++) {
						if (leadid == leadList[i].id)
							leadList[i].tasks.push(taskobj);
				
					}

					$localStorage.dblead = angular.toJson(leadList);
					deferred.resolve({
						"msg" : "LeadItem Updated Successfully."
					});

				}, 1000);

		return deferred.promise;
	}
	
	leadService.deletelead = function(leadid) {
		var deferred = $q.defer();
		$timeout(
				function() {

					$log.debug("In side local DB updateStock...");
					var leadList = angular.fromJson($localStorage.dblead);

					if (typeof leadList === 'undefined')
						stockList = [];

					for (var i = 0; i < leadList.length; i++) {
						if (leadid == leadList[i].id)
							//delete leadList[i];
							leadList.splice(i, 1);
				
					}

					$localStorage.dblead = angular.toJson(leadList);
					deferred.resolve({
						"msg" : "LeadItem Converted Successfully."
					});

				}, 1000);

		return deferred.promise;
	}
	//contact services
	leadService.addcontact = function(contact) {

		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side local DB leademp...");
			var contactList = angular.fromJson($localStorage.dbcontact);
			if (typeof contactList === 'undefined')
				contactList = [];
			contactList.push(contact);
			$localStorage.dbcontact = angular.toJson(contactList);
			deferred.resolve({
				"msg" : "contact Added Successfully."
			});

		}, 1000);

		return deferred.promise;
	}
	
	leadService.getAllcontact = function() {
		var deferred = $q.defer();
		$timeout(function() {
			$log.debug("In side local DB getlead...");
			var contactList = angular.fromJson($localStorage.dbcontact);
			if (typeof contactList === 'undefined')
				contactList = [];
			deferred.resolve(contactList);
		}, 1000);

		return deferred.promise;

	}

	
	leadService.getContactById= function(selectedleadNo) {

		var deferred = $q.defer();
		$timeout(function() {
			var tempItem = [];
	
			var contactList = angular.fromJson($localStorage.dbcontact);
		

			if (typeof contactList === 'undefined')
				contactList = [];
		

			for (i = 0; i < contactList.length; i++) {
				if (selectedleadNo == contactList[i].cid) {
					tempItem.push(contactList[i]);

				}
			}
			
			deferred.resolve(tempItem);
			

		}, 1000);
		return deferred.promise;
	}
	
	
	
	leadService.updatecontact = function(contactobj) {
		var deferred = $q.defer();
		$timeout(
				function() {

					$log.debug("In side local DB updateStock...");
					var contactList = angular.fromJson($localStorage.dbcontact);

					if (typeof contactList === 'undefined')
						contactList = [];

					for (var i = 0; i < contactList.length; i++) {
						if (contactobj.cid == contactList[i].cid)
							contactList[i]=(contactobj);
				
					}

					$localStorage.dbcontact = angular.toJson(contactList);
					deferred.resolve({
						"msg" : "Contact Updated Successfully."
					});

				}, 1000);

		return deferred.promise;
	}
	
	
	//end of contact
	
	// End of leadService

	//*************************************************************************************************************************		
//start opportunity services
	
	var opportunityService = {};

	serviceFactory.getopportunityService = function() {
		return opportunityService;
	}
	
	opportunityService.addopportunity = function(opportunity) {
		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side local DB addStock...");
			var opportunityList = angular.fromJson($localStorage.dbopportunity);
			if (typeof opportunityList === 'undefined')
				opportunityList = [];
			opportunityList.push(opportunity);
			$localStorage.dbopportunity = angular.toJson(opportunityList);
			deferred.resolve({
				"msg" : "opportunityItem Added Successfully."
			});

		}, 1000);

		return deferred.promise;
	}

	opportunityService.getAllopportunity = function() {
		var deferred = $q.defer();
		$timeout(function() {
			$log.debug("In side local DB getAllopportunity...");
			var opportunityList = angular.fromJson($localStorage.dbopportunity);
			if (typeof opportunityList === 'undefined')
				opportunityList = [];
			deferred.resolve(opportunityList);
		}, 1000);

		return deferred.promise;

	}

	opportunityService.getopportunityById= function(selectedleadNo) {

		var deferred = $q.defer();
		$timeout(function() {
			var tempItem = [];
				var opportunityList = angular.fromJson($localStorage.dbopportunity);
					if (typeof opportunityList === 'undefined')
				opportunityList = [];
				for (i = 0; i < opportunityList.length; i++) {
				if (selectedleadNo == opportunityList[i].id) {
					tempItem.push(opportunityList[i]);
				}
			}
			deferred.resolve(tempItem);
			}, 1000);
		return deferred.promise;
	}
	
	
	opportunityService.updateopportunity = function(opportunityobj) {
		var deferred = $q.defer();
		$timeout(
				function() {

					$log.debug("In side local DB updateStock...");
					var opportunityList = angular.fromJson($localStorage.dbopportunity);

					if (typeof opportunityList === 'undefined')
						opportunityList = [];

					for (var i = 0; i < opportunityList.length; i++) {
						if (opportunityobj.id == opportunityList[i].id)
							opportunityList[i]=(opportunityobj);
				
					}

					$localStorage.dbopportunity = angular.toJson(opportunityList);
					deferred.resolve({
						"msg" : "opportunity Updated Successfully."
					});

				}, 1000);

		return deferred.promise;
	}
	

	opportunityService.addupdatetask = function(oppobj,oppid) {
		var deferred = $q.defer();
		$timeout(
				function() {

					$log.debug("In side local DB updateStock...");
					var opportunityList = angular.fromJson($localStorage.dbopportunity);

					if (typeof opportunityList === 'undefined')
						stockList = [];

					for (var i = 0; i < opportunityList.length; i++) {
						if (oppid == opportunityList[i].id)
							opportunityList[i].tasks.push(oppobj);
				
					}

					$localStorage.dbopportunity = angular.toJson(opportunityList);
					deferred.resolve({
						"msg" : "opportunityList Updated Successfully."
					});

				}, 1000);

		return deferred.promise;
	}
	
	
	//end apportunity service
	//*************************************************************************************************************************		
	
	
	//*************************************************************************************************************************	
	//start login
	var loginService = {};

	serviceFactory.getloginService = function() {
		return loginService;
	}

	
	loginService.getuserById= function(uid) {
		
		var deferred = $q.defer();
		$timeout(function() {
			var tempItem = [];
				var userList = angular.fromJson($localStorage.dbuser);
					if (typeof userList === 'undefined')
						userList = [];
				for (i = 0; i < userList.length; i++) {
				if (uid == userList[i].email) {
					tempItem.push(userList[i]);
				}
			}
			deferred.resolve(tempItem);
			}, 1000);
		return deferred.promise;
	}
	
	//*************************************************************************************************************************	
	// Start of StockService
	var StockService = {};

	serviceFactory.getStockService = function() {
		return StockService;
	}

	StockService.addStock = function(stock) {
		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side local DB addStock...");
			var stockList = angular.fromJson($localStorage.dbStocks);
			if (typeof stockList === 'undefined')
				stockList = [];
			stockList.push(stock);
			$localStorage.dbStocks = angular.toJson(stockList);
			deferred.resolve({
				"msg" : "StockItem Added Successfully."
			});

		}, 1000);

		return deferred.promise;
	}

	StockService.updateStock = function(invoiceObj) {
		var deferred = $q.defer();
		$timeout(
				function() {

					$log.debug("In side local DB updateStock...");
					var stockList = angular.fromJson($localStorage.dbStocks);

					if (typeof stockList === 'undefined')
						stockList = [];

					for (var i = 0; i < stockList.length; i++) {
						if (invoiceObj.invoiceLineItemList.itemName == stockList[i].itemName)
							stockList[i] = invoiceObj;
					}

					$localStorage.dbStocks = angular.toJson(stockList);
					deferred.resolve({
						"msg" : "StockItem Updated Successfully."
					});

				}, 1000);

		return deferred.promise;
	}

	StockService.getAllStock = function() {
		var deferred = $q.defer();
		$timeout(function() {
			var stockList = angular.fromJson($localStorage.dbStocks);
			if (typeof stockList === 'undefined')
				stockList = [];
			deferred.resolve(stockList);

		}, 1000);

		return deferred.promise;
	} // End of StockService

	StockService.getstockByThreshold = function() {
		var deferred = $q.defer();
		$timeout(function() {
			var stockByThreshold = [];

			var stockList = angular.fromJson($localStorage.dbStocks);
			//	if (typeof stockList === 'undefined')
			//		stockList = [];

			for (i = 0; i < stockList.length; i++) {
				if (stockList[i].qty <= stockList[i].thresholdValue) {

					stockByThreshold.push(stockList[i]);

					$log.debug("TEMP===" + stockByThreshold);
				}
			}

			deferred.resolve(stockList);

		}, 1000);

		return deferred.promise;
	} // End of StockService

	//*************************************************************************************************************************		
	// Start of TaxService
	var TaxService = {};

	serviceFactory.getTaxService = function() {
		return TaxService;
	}

	TaxService.addTax = function(tax) {
		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side local DB addTax...");
			var taxList = angular.fromJson($localStorage.dbTaxes);
			if (typeof taxList === 'undefined')
				taxList = [];
			taxList.push(tax);
			$localStorage.dbTaxes = angular.toJson(taxList);
			deferred.resolve({
				"msg" : "Tax Added Successfully."
			});

		}, 1000);

		return deferred.promise;
	}

	TaxService.getAllTaxes = function() {
		var deferred = $q.defer();
		$timeout(function() {
			var taxList = angular.fromJson($localStorage.dbTaxes);
			if (typeof taxList === 'undefined')
				taxList = [];
			deferred.resolve(taxList);

		}, 1000);

		return deferred.promise;
	} // End of TaxService

	//*************************************************************************************************************************	
	// Start of InvoiceService
	var InvoiceService = {};

	serviceFactory.getInvoiceService = function() {
		return InvoiceService;
	}

	InvoiceService.addInvoice = function(invoice) {
		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side local DB addInvoice...");
			var invoiceList = angular.fromJson($localStorage.dbinvoice);

			if (typeof invoiceList === 'undefined')
				invoiceList = [];

			//		invoice.invoiceId = invoiceList.length + 100;

			invoiceList.push(invoice);
			$localStorage.dbinvoice = angular.toJson(invoiceList);
			deferred.resolve({
				"msg" : "StockItem Updated Successfully."
			});

			deferred.resolve({
				"msg" : "Invoice Added Successfully."

			});

		}, 1000);

		return deferred.promise;
	}

	InvoiceService.getAllInvoice = function(invoiceId) {
		var deferred = $q.defer();
		$timeout(function() {
			var invoiceList = angular.fromJson($localStorage.dbinvoice);
			if (typeof invoiceList === 'undefined')
				invoiceList = [];
			deferred.resolve(invoiceList);

		}, 1000);

		return deferred.promise;
	} // End of TaxService

	InvoiceService.getinvoiceByID = function(selectedBillNo) {

		var deferred = $q.defer();
		$timeout(function() {
			var tempItem = [];
			var invoiceList = angular.fromJson($localStorage.dbinvoice);

			if (typeof invoiceList === 'undefined')
				invoiceList = [];

			for (i = 0; i < invoiceList.length; i++) {
				if (selectedBillNo == invoiceList[i].invoiceId) {

					// selectedBillNo = invoiceList[i];
					tempItem.push(invoiceList[i]);

					// $log.debug("TEMP===" + tempItem[i]);
				}
			}
			deferred.resolve(tempItem);

		}, 1000);
		return deferred.promise;
	}

	InvoiceService.getAllInvoiceByCustId = function(selectedCustomerId) {

		var deferred = $q.defer();
		$timeout(
				function() {
					var tempInvoice = [];
					var custInvoiveList = angular
							.fromJson($localStorage.dbinvoice);

					if (typeof custInvoiveList === 'undefined')
						custInvoiveList = [];

					for (i = 0; i < custInvoiveList.length; i++) {
						if (selectedCustomerId == custInvoiveList[i].customer.customerId) {

							// selectedBillNo = invoiceList[i];
							tempInvoice.push(custInvoiveList[i]);

							// $log.debug("TEMP===" + tempItem[i]);
						}
					}
					deferred.resolve(tempInvoice);

				}, 1000);
		return deferred.promise;
	}
	//*************************************************************************************************************************	

	// Add Sales Service
	var SalesService = {};

	serviceFactory.getSalesService = function() {
		return SalesService;
	}

	SalesService.addSalesOrder = function(salesOrder) {

		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side local DB addSalesOrder...");
			var salesOrderList = angular.fromJson($localStorage.dbSalesOrder);
			if (typeof salesOrderList === 'undefined')
				salesOrderList = [];
			salesOrderList.push(salesOrder);
			$localStorage.dbSalesOrder = angular.toJson(salesOrderList);
			deferred.resolve({
				"msg" : "Sales Orde Added Successfully."
			});

		}, 1000);

		return deferred.promise;
	}

	SalesService.getAllSalesOrder = function() {
		var deferred = $q.defer();
		$timeout(function() {
			$log.debug("In side local DB getAllSalesOrder...");
			var salesOrderList = angular.fromJson($localStorage.dbSalesOrder);
			if (typeof salesOrderList === 'undefined')
				salesOrderList = [];
			deferred.resolve(salesOrderList);
		}, 1000);

		return deferred.promise;

	}

	SalesService.getSOByID = function(selectedSOId) {

		var deferred = $q.defer();
		$timeout(function() {
			var tempSOItem = [];

			var sOList = angular.fromJson($localStorage.dbSalesOrder);

			if (typeof sOList === 'undefined')
				sOList = [];

			for (i = 0; i < sOList.length; i++) {
				if (selectedSOId == sOList[i].salesOrderId) {

					// selectedBillNo = invoiceList[i];
					tempSOItem.push(sOList[i]);

					$log.debug("TEMP===" + tempSOItem[i]);
				}
			}
			deferred.resolve(tempSOItem);

		}, 1000);
		return deferred.promise;
	}
	/*
	 SalesService.updateemp = function(editProfile) {
	 var deferred = $q.defer();
	 $timeout(function() {

	 $log.debug("In side updated local DB updateuser...");
	 var empList = angular.fromJson($localStorage.dbemp);
	 if (typeof empList === 'undefined')
	 empList = [];

	 for (var i = 0; i < empList.length; i++) {
	 if (editProfile.empid == empList[i].empid)
	 empList[i] = editProfile;
	 }

	 $localStorage.dbemp = angular.toJson(empList);
	 deferred.resolve({
	 "msg" : "User data Updated Successfully."
	 });

	 }, 1000);

	 return deferred.promise;
	 }

	 SalesService.getempByID = function(selectedempNo) {

	 var deferred = $q.defer();
	 $timeout(function() {
	 var tempItem = [];
	 var empList = angular.fromJson($localStorage.dbemp);

	 if (typeof empList === 'undefined')
	 empList = [];

	 for (i = 0; i < empList.length; i++) {
	 if (selectedempNo == empList[i].empid) {

	 $log.debug("************TEMP===" + empList[i].empid);
	 tempItem.push(empList[i]);

	 }
	 }
	 deferred.resolve(tempItem);

	 }, 1000);
	 return deferred.promise;
	 }
	 */

	//*************************************************************************************************************************	
	// Add Purchase Service
	var PurchaseService = {};

	serviceFactory.getPurchaseService = function() {
		return PurchaseService;
	}

	PurchaseService.addPurchaseOrder = function(purchaseOrderObj) {

		var deferred = $q.defer();
		$timeout(function() {

			$log.debug("In side local DB addPurchaseOrder...");
			var purchaseOrderList = angular
					.fromJson($localStorage.dbPurchaseOrder);
			if (typeof purchaseOrderList === 'undefined')
				purchaseOrderList = [];
			purchaseOrderList.push(purchaseOrderObj);
			$localStorage.dbPurchaseOrder = angular.toJson(purchaseOrderList);
			deferred.resolve({
				"msg" : "Purchase Orde Added Successfully."
			});

		}, 1000);

		return deferred.promise;
	}

	PurchaseService.getAllPurchaseOrder = function() {
		var deferred = $q.defer();
		$timeout(function() {
			$log.debug("In side local DB getAllPurchaseOrder...");
			var purchaseOrderList = angular
					.fromJson($localStorage.dbPurchaseOrder);
			if (typeof purchaseOrderList === 'undefined')
				purchaseOrderList = [];
			deferred.resolve(purchaseOrderList);
		}, 1000);

		return deferred.promise;

	}

	PurchaseService.getPOByID = function(selectedPurchaseOrderNo) {

		var deferred = $q.defer();
		$timeout(function() {
			var tempPOItem = [];

			var pOList = angular.fromJson($localStorage.dbPurchaseOrder);

			if (typeof pOList === 'undefined')
				pOList = [];

			for (i = 0; i < pOList.length; i++) {
				if (selectedPurchaseOrderNo == pOList[i].purchaseOrderNo) {

					// selectedBillNo = invoiceList[i];
					tempPOItem.push(pOList[i]);

					// $log.debug("TEMP===" + tempItem[i]);
				}
			}
			deferred.resolve(tempPOItem);

		}, 1000);
		return deferred.promise;
	}
	//*************************************************************************************************************************				

	return serviceFactory;
}
