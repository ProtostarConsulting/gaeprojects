var app = angular.module("stockApp", [ 'ngMaterial', 'ngMessages', "xeditable",
		"ui.bootstrap", "ui.router", 'md.data.table', 'ngResource',
		'ngStorage', 'ngRoute', 'ngFileUpload', 'ngAnimate', 'ui.grid',
		'ui.grid.selection', 'ui.grid.exporter', 'directive.g+signin' ]);


app.constant('monthList', ["January", "February", "March", "April",
            				"May", "June", "July", "Augast", "September",
           				"October", "November", "December"]);

app.config(function($mdThemingProvider) {
	$mdThemingProvider.theme('default').primaryPalette('light-blue')
			.accentPalette('pink');
});
app.config(function($logProvider) {
	// $logProvider.debugEnabled(false);
	$logProvider.debugEnabled(true);// this is default
});

app.config(function($stateProvider, $urlRouterProvider) {
	// This adds config 2
	// For any unmatched url, redirect to /state1
	$urlRouterProvider.otherwise("/home");
	// Now set up the states
	$stateProvider.state('state1', {
		url : "/state1",
		templateUrl : "/app/demo/state1.html",
		controller : 'statesPageCtr'
	}).state('drivefiles', {
		url : "/drivefiles",
		templateUrl : "/app/demo/drivefiles.html",
		controller : 'driveFilesCtr'
	}).state('state1.list', {
		url : "/list/:someVal",
		templateUrl : "/app/demo/state1.list.html",
		controller : 'statesPageCtr'
	}).state('home', {
		url : "/home",
		templateUrl : '/home.html',
		controller : 'homeCtr'

	}).state('stock', {
		url : "/stock",
		templateUrl : '/app/stock/stock_module.html',
		controller : 'stockModuleCtr'
	}).state('stock.stockItemAdd', {
		url : "/stockItemAdd",
		templateUrl : '/app/stock/stockItem_add.html',
		controller : 'stockAddCtr'
	}).state('stock.stockItemList', {
		url : "/stockItemList",
		templateUrl : '/app/stock/stockItem_list.html',
		controller : 'stockListCtr'
	}).state('stock.reportByThreshold', {
		url : "/reportByThreshold",
		templateUrl : '/app/stock/stock_reportByThreshold.html',
		controller : 'stockReportByThresholdCtr'
	}).state('stock.taxadd', {
		url : "/tax/taxadd",
		templateUrl : '/app/tax/tax_add.html',
		controller : 'taxCtr'
	}).state('stock.taxlist', {
		url : "/tax/taxlist",
		templateUrl : '/app/tax/tax_list.html',
		controller : 'taxCtr'
	}).state('invoice', {
		url : "/invoice",
		templateUrl : '/app/invoice/invoice_module.html',
		controller : 'invoiceModuleCtr'
	}).state('invoice.add', {
		url : "/add",
		templateUrl : '/app/invoice/invoice_add.html',
		controller : 'invoiceAddCtr'
	}).state('invoice.list', {
		url : "/list",
		templateUrl : '/app/invoice/invoice_list.html',
		controller : 'invoiceListCtr',
	}).state('invoice.view', {
		url : "/view/:selectedInvoiceNo",
		templateUrl : '/app/invoice/invoice_view.html',
		controller : 'invoiceViewCtr',
	}).state('customer', {
		url : "/customer",
		templateUrl : '/app/customer/customer_module.html',
		controller : 'customerModuleCtr'
	}).state('customer.add', {
		url : "/add",
		templateUrl : '/app/customer/customer_add.html',
		controller : 'customerAddCtr'
	}).state('customer.view', {
		url : "/view/:selectedCustomerId",
		templateUrl : '/app/customer/customer_view.html',
		controller : 'customerViewCtr'
	}).state('customer.list', {
		url : "/list",
		templateUrl : '/app/customer/customer_list.html',
		controller : 'customerListCtr'
	}).state('customer.uplodFile', {
		url : "/uplodFile",
		templateUrl : '/app/customer/fileUpload.jsp',
	//	controller : 'customerListCtr'
	}).state('account', {
		url : "/account",
		templateUrl : '/app/account/account_module.html',
		controller : 'accountModuleCtr'
	}).state('account.accountAdd', {
		url : "/accountAdd",
		templateUrl : '/app/account/account_add.html',
		controller : 'accountAddCtr'
	}).state('account.accountList', {
		url : "/accountList",
		templateUrl : '/app/account/account_list.html',
		controller : 'accountAddCtr'
	}).state('account.accountIncome', {
		url : "/accountIncome",
		templateUrl : '/app/account/account_income.html',
		controller : 'accountIncomeCtr'
	}).state('account.accountPayable', {
		url : "/accountPayable",
		templateUrl : '/app/account/account_payable.html',
		controller : 'accountPayableCtr'
	}).state('account.accountPayableList', {
		url : "/accountPayableList",
		templateUrl : '/app/account/account_payableList.html',
		controller : 'accountPayableListCtr'
	}).state('account.accountReceivable', {
		url : "/accountReceivable",
		templateUrl : '/app/account/account_receivable.html',
		controller : 'accountReceivableCtr'
	}).state('account.accountReceivableList', {
		url : "/accountReceivableList",
		templateUrl : '/app/account/account_receivableList.html',
		controller : 'accountReceivableListCtr'
	}).state('report', {
		url : "/report",
		templateUrl : '/app/report/report_module.html',
		controller : 'reportCtr'
	}).state('report.byThreshhold', {
		url : "/byThreshhold",
		templateUrl : '/app/report/report_bythreshold.html',
		controller : 'reportCtr'
	}).state('report.allcustomer', {
		url : "/allcustomer",
		templateUrl : '/app/report/customer_list.html',
		controller : 'reportCtr'

	}).state('salesOrder', {
		url : "/salesOrder",
		templateUrl : '/app/sales/salesOrder_module.html',
		controller : 'salesOrderCtr'

	}).state('salesOrder.SalesOrderAdd', {
		url : "/SalesOrderAdd",
		templateUrl : '/app/sales/salesOrder_add.html',
		controller : 'salesOrderAddCtr'

	}).state('salesOrder.SalesOrderList', {
		url : "/SalesOrderList",
		templateUrl : '/app/sales/salesOrder_list.html',
		controller : 'salesOrderListCtr'

	}).state('salesOrder.view', {
		url : "/SalesOrderview/:selectedSOId",
		templateUrl : '/app/sales/salesOder_view.html',
		controller : 'salesOrderViewCtr'

	}).state('purchaseOrder', {
		url : "/purchaseOrder",
		templateUrl : '/app/purchase/purchaseOrder_module.html',
		controller : 'purchaseOrderCtr'

	}).state('purchaseOrder.PurchaseOrderAdd', {
		url : "/PurchaseOrderAdd",
		templateUrl : '/app/purchase/purchaseOrder_add.html',
		controller : 'purchaseOrderAddCtr'

	}).state('purchaseOrder.PurchaseOrderList', {
		url : "/PurchaseOrderList",
		templateUrl : '/app/purchase/purchaseOrder_list.html',
		controller : 'purchaseOrderListCtr'

	}).state('purchaseOrder.POview', {
		url : "/POview/:selectedPONo",
		templateUrl : '/app/purchase/purchaseOrder_view.html',
		controller : 'purchaseOrderViewCtr'

	}).state('hr', {
		url : "/hr",
		templateUrl : '/app/hr/hr_module.html',
		controller : 'hrCtr'
	}).state('hr.employeeadd', {
		url : "/empadd",
		templateUrl : '/app/hr/employee_add.html',
		controller : 'hrCtr.add'
	}).state('hr.employeelist', {
		url : "/emplist",
		templateUrl : '/app/hr/employee_list.html',
		controller : 'hrCtr.emplist'
	}).state('hr.empview', {
		url : "/view/:selectedempNo",
		templateUrl : '/app/hr/employee_view.html',
		controller : 'hrCtr.empview',
	}).state('hr.docuplode', {
		url : "/document",
		templateUrl : '/app/hr/employee_doc.html',
		controller : 'hrCtr.empdoc',
	}).state('hr.salarystru', {
		url : "/SalaryStructure/:selectedUserId/:selectedUserName",
		templateUrl : '/app/hr/employee_salstruct.html',
		controller : 'hrCtr.addupdatesalstruct',
	}).state('hr.salslip', {
		url : "/Salaryslip",
		templateUrl : '/app/hr/employee_salslip.html',
		controller : 'hrCtr.empsalslipstruct',
	}).state('hr.gSalSlip', {
		url : "/gSalSlip/:selectedempstructno",
		templateUrl : '/app/hr/ganerate_salaryslip.html',
		controller : 'hrCtr.empsalslipstruct',
	}).state('hr.generatesalslip', {
		url : "/generatesalslip",
		templateUrl : '/app/hr/ganerate_multiple_salaryslip.html',
		controller : 'hrCtr.emplist_to_ganeratesalslip',
	}).state('hr.printgeneratesalslip', {
		url : "/selectedlist",
		templateUrl : '/app/hr/slected_Employeesalaryslip.html',
		controller : 'hrCtr.emplist_to_ganeratesalslip',
		params : {
			ganeratedsalslip : ""
		}
	}).state('hr.print', {
		url : "/print/:printempidsalslip",
		templateUrl : '/app/hr/print_salaryslip.html',
		controller : 'hrCtr.emplist_to_ganeratesalslip',
	}).state('crm', {
		url : "/crm",
		templateUrl : '/app/crm/crm_module.html',
		controller : 'hrCtr.empview',
	}).state('crm.customer', {
		url : "/customer",
		templateUrl : '/app/customer/customer_module.html',
		controller : 'customerCtr',
	}).state('crm.lead', {
		url : "/lead",
		templateUrl : '/app/crm/lead_list.html',
		controller : 'lead',
	}).state('crm.lead_add', {
		url : "/add",
		templateUrl : '/app/crm/crm_lead.html',
		controller : 'lead',
	}).state('crm.personview', {
		url : "/view/:selectedleadNo",
		templateUrl : '/app/crm/crm_lead_view.html',
		controller : 'lead_view',
	}).state('crm.contacts', {
		url : "/contact",
		templateUrl : '/app/crm/contact_list.html',
		controller : 'contacts',
	}).state('crm.addcontact', {
		url : "/addContact",
		templateUrl : '/app/crm/crm_contacts.html',
		controller : 'contacts',
	}).state('crm.viewContact', {
		url : "/viewContact/:selectedcontactNo",
		templateUrl : '/app/crm/crm_contacts_view.html',
		controller : 'contacts',

	}).state('crm.opportunity', {
		url : "/opportunity",
		templateUrl : '/app/crm/opportunity_list.html',
		controller : 'opportunity',

	}).state('crm.addopprtunity', {
		url : "/addopportunity",
		templateUrl : '/app/crm/crm_opportunity.html',
		controller : 'opportunity',

	}).state('crm.opportunityView', {
		url : "/opportunityView/:selectedopportunityNo",
		templateUrl : '/app/crm/crm_opportunity_view.html',
		controller : 'opportunity',

	}).state('setup', {
		url : "/setup",
		templateUrl : '/app/setup/setup_module.html',
		controller : 'setup',

	}).state('setup.user', {
		url : "/user",
		templateUrl : '/app/setup/setup_userlist.html',
		controller : 'setup',

	}).state('setup.useradd', {
		url : "/useradd",
		templateUrl : '/app/setup/setup_adduser.html',
		controller : 'setup.adduser',

	}).state('setup.userview', {
		url : "/userview/:selecteduserNo",
		templateUrl : '/app/setup/setup_viewuser.html',
		controller : 'setup.adduser',

	}).state('setup.printheaderfooter', {
		url : "/printheaderfooter",
		templateUrl : '/app/setup/setup_headerfooter.html',
		controller : 'setup_headerfooter',

	}).state('setup.businesssetup', {
		url : "/businesssetup",
		templateUrl : '/app/setup/businesssetup.html',
		controller : 'setup',

	}).state('setup.userlist', {
		url : "/userlist",
		templateUrl : '/app/setup/userlist.html',
		controller : 'setup',

	})/*
		 * .state('login', { url : "/login", templateUrl :
		 * '/app/login/login.html', controller : 'login',
		 *  })
		 */.state('homecall', {
		url : "/home/:userauthoritys",
		templateUrl : '/home.html',
		controller : 'AppCtrl'

	}).state('internet', {
		url : "/internet",
		templateUrl : '/app/demo/internet.html',
		controller : 'internet'

	}).state('hr.timeSheet', {
		url : "/timeSheet",
		templateUrl : '/app/hr/timesheet.html',
		controller : 'timesheet'

	}).state('login', {
		url : "/login",
		templateUrl : '/app/login/login_module.html',
		controller : 'indexCtr'
	}).state('needBusinessAccount', {
		url : "/NeedBusinessAccount",
		templateUrl : '/app/login/needBusinessAccount.html',
		controller : 'needBusinessAccountCtr'
	}).state('newBusinessAccount', {
		url : "/NewBusinessAccount",
		templateUrl : '/app/login/newBusinessAccount.html',
		controller : 'newBusinessAccountCtr'
	}).state('createBusinessAccount', {
		url : "/CreateBusiness",
		templateUrl : '/app/login/createBusinessAccount.html',
		controller : 'needBusinessAccountCtr'
	}).state('newUserTeacher', {
		url : "/newUserTeacher",
		templateUrl : '/app/login/newUser.html',
		controller : 'loginModuleCtr'
	}).state('newUserStudent', {
		url : "/newUserStudent",
		templateUrl : '/app/login/newUser.html',
		controller : 'newUserStudentCtr'
	}).state('store', {
		url : "/store",
		templateUrl : '/app/store/store_module.html',
		controller : 'storeModuleCtr'
	}).state('store.add', {
		url : "/add",
		templateUrl : '/app/store/store_add.html',
		controller : 'storeAddCtr'
	}).state('store.search', {
		url : "/search",
		templateUrl : '/app/store/search_neareststore.html',
		controller : 'storeAddCtr'
	}).state('probusiness', {
		url : "/probusiness",
		templateUrl : '/app/probusiness/proBusinessModule.html',
		controller : 'probusinessCtr'
	}).state('probusiness.list', {
		url : "/businesslist",
		templateUrl : '/app/probusiness/probusiness.html',
		controller : 'probusinessCtr'
	}).state('probusiness.editBusiness', {
		url : "/editBusiness/:businessNo/:businessName",
		templateUrl : '/app/probusiness/editProBusiness.html',
		controller : 'probusinessCtr'
	}).state('probusiness.userview', {
		url : "/userview/:selecteduserNo",
		templateUrl : '/app/probusiness/userview.html',
		controller : 'probusinessCtr'
	}).state('probusiness.adduser', {
		url : "/adduser/:BNo",
		templateUrl : '/app/probusiness/adduser.html',
		controller : 'probusinessCtr'
	}).state('user_prof_detail', {
		url : "/profile",
		templateUrl : '/app/profile/profileModule.html',
		controller : 'profileCtr'
	}).state('user_prof_detail.viewprofile', {
		url : "/viewprofile",
		templateUrl : '/app/profile/viewprofile.html',
		controller : 'profileCtr'
	}).state('user_prof_detail.changepassword', {
		url : "/changepassword",
		templateUrl : '/app/profile/changepassword.html',
		controller : 'profileCtr'
	}).state('user_prof_detail.getAllSlip', {
		url : "/getAllSlip/:viewsalslips",
		templateUrl : '/app/profile/getAllSalSlip.html',
		controller : 'profileCtr'
	}).state('user_prof_detail.print', {
		url : "/print/:printempidsalslip",
		templateUrl : '/app/profile/print_salaryslip.html',
		controller : 'profileCtr',
	}).state('document', {
		url : "/document",
		templateUrl : '/app/Document/index.jsp',
		controller : 'document',
	}).state('email', {
		url : "/email",
		templateUrl : '/app/Email/email.html',
		controller : 'email',
	}).state('AssetMangement', {
		url : "/AssetMangement",
		templateUrl : '/app/AssetMangement/AssetMgmtModule.html',
		controller : 'AssetMangementCtr',
	}).state('AssetMangement.add', {
		url : "/AddAsset",
		templateUrl : '/app/AssetMangement/AddAsset.html',
		controller : 'AssetMangementCtr',
	}).state('AssetMangement.list', {
		url : "/ListAsset",
		templateUrl : '/app/AssetMangement/ListAsset.html',
		controller : 'AssetMangementCtr',
	}).state('AssetMangement.editasset', {
		url : "/editasset/:selectedasetNo",
		templateUrl : '/app/AssetMangement/assignupdateuser.html',
		controller : 'AssignupdateAsset',
	}).state('AssetMangement.assignuser', {
		url : "/assignuser/:selectedasetNo",
		templateUrl : '/app/AssetMangement/assignuser.html',
		controller : 'AssignupdateAsset',
	});

});
