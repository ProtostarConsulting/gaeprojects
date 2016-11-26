var app = angular.module("stockApp", [ 'ngMaterial', 'ngMessages',
		"ui.bootstrap", "ui.router", 'md.data.table', 'ngResource',
		'ngStorage', 'ngRoute', 'ngFileUpload', 'ngAnimate',
		'directive.g+signin', 'ui.bootstrap', 'textAngular', 'ngMdIcons' ]);

app.factory('ajsCache', function($cacheFactory) {
	return $cacheFactory('browserCache');
});

app.constant('monthList', [ "January", "February", "March", "April", "May",
		"June", "July", "Augast", "September", "October", "November",
		"December" ]);

app.filter('formatDate', function($filter) {
	return function(inputDate) {
		return $filter('date')(inputDate, 'dd-MM-yyyy');
	};
});

app.filter('formatDateWithTime', function($filter) {
	return function(inputDate) {
		return $filter('date')(inputDate, 'dd-MM-yyyy h:mm a');
	};
});

app.filter('htmlToPlainText', function() {
	return function(text) {
		return text ? String(text).replace(/<[^>]+>/gm, '') : '';
	};
});
app.directive('focusOn', function($timeout) {
	return {
		restrict : 'A',
		link : function($scope, $element, $attr) {
			$scope.$watch($attr.focusOn, function(_focusVal) {
				$timeout(function() {
					_focusVal ? $element.focus() : $element.blur();
				});
			});
		}
	}
});
app.filter('proOrderObjectByNumberField', function() {
	return function(input, attribute) {
		if (!angular.isObject(input))
			return input;
		var reverseOrder = attribute.startsWith("-");
		if (reverseOrder)
			attribute = attribute.split("-")[1];
		var array = [];
		for ( var objectKey in input) {
			array.push(input[objectKey]);
		}

		array.sort(function(a, b) {
			a = parseFloat(a[attribute]);
			b = parseFloat(b[attribute]);
			return (a - b) * (reverseOrder ? -1 : 1);
		});
		return array;
	}
});
app.filter('proOrderObjectByDateField', function() {
	return function(input, attribute) {
		if (!angular.isObject(input))
			return input;
		var reverseOrder = attribute.startsWith("-");
		if (reverseOrder)
			attribute = attribute.split("-")[1];
		var array = [];
		for ( var objectKey in input) {
			array.push(input[objectKey]);
		}

		array.sort(function(a, b) {
			a = new Date(a[attribute]);
			b = new Date(b[attribute]);
			return (a - b) * (reverseOrder ? -1 : 1);
		});
		return array;
	}
});
app.directive("proBeforeDateCheck",
		function() {
			return {
				restrict : "A",
				require : "ngModel",
				link : function(scope, element, attributes, ngModel) {
					ngModel.$validators.proBeforeDateCheck = function(
							currentDateValue) {
						var assignedDate = new Date(
								Number(attributes.proBeforeDateCheck));
						if (!currentDateValue) {
							return true;
						}
						currentDateValue.setHours(0, 0, 0, 0);
						assignedDate.setHours(0, 0, 0, 0);
						if (currentDateValue >= assignedDate) {
							return true;
						}
						return false
					}
				}
			};
		});
/*
 * app.filter('formatDate1', function($filter) { return function(inputDate) {
 * return $filter('date')(inputDate, 'dd-MM-yyyy'); }; });
 */
/*
 * app.factory('MathService', function() { var factory = {}; factory.multiply =
 * function(a, b) { return a * b } return factory; });
 * 
 * app.service('CalcService', function(MathService){
 * 
 * this.square = function(a) { return MathService.multiply(a,a); } });
 */
app.config(function($mdThemingProvider) {

	/*
	 * Available palettes: red, pink, purple, deep-purple, indigo, blue,
	 * light-blue, cyan, teal, green, light-green, lime, yellow, amber, orange,
	 * deep-orange, brown, grey, blue-grey
	 */
	$mdThemingProvider.theme('default').primaryPalette('light-blue')
			.accentPalette('pink');
	$mdThemingProvider.theme('red').primaryPalette('red').accentPalette(
			'orange').warnPalette('blue');
	$mdThemingProvider.theme('pink').primaryPalette('pink').accentPalette(
			'orange').warnPalette('blue');
	$mdThemingProvider.theme('purple').primaryPalette('purple').accentPalette(
			'grey').warnPalette('blue');
	$mdThemingProvider.theme('deep-purple').primaryPalette('deep-purple')
			.accentPalette('grey').warnPalette('blue');
	$mdThemingProvider.theme('indigo').primaryPalette('indigo').accentPalette(
			'grey').warnPalette('blue');
	$mdThemingProvider.theme('blue').primaryPalette('blue').accentPalette(
			'grey').warnPalette('blue');
	$mdThemingProvider.theme('light-blue').primaryPalette('light-blue')
			.accentPalette('grey').warnPalette('blue');
	$mdThemingProvider.theme('cyan').primaryPalette('cyan').accentPalette(
			'grey').warnPalette('blue');
	$mdThemingProvider.theme('teal').primaryPalette('teal').accentPalette(
			'grey').warnPalette('blue');
	$mdThemingProvider.theme('green').primaryPalette('green').accentPalette(
			'grey').warnPalette('blue');
	$mdThemingProvider.theme('light-green').primaryPalette('light-green')
			.accentPalette('grey').warnPalette('blue');
	$mdThemingProvider.theme('lime').primaryPalette('lime').accentPalette(
			'grey').warnPalette('blue');
	$mdThemingProvider.theme('yellow').primaryPalette('yellow').accentPalette(
			'grey').warnPalette('blue');
	$mdThemingProvider.theme('amber').primaryPalette('amber').accentPalette(
			'grey').warnPalette('blue');
	$mdThemingProvider.theme('orange').primaryPalette('orange').accentPalette(
			'grey').warnPalette('blue');
	$mdThemingProvider.theme('deep-orange').primaryPalette('deep-orange')
			.accentPalette('grey').warnPalette('blue');
	$mdThemingProvider.theme('brown').primaryPalette('brown').accentPalette(
			'grey').warnPalette('blue');
	$mdThemingProvider.theme('grey').primaryPalette('grey').accentPalette(
			'grey').warnPalette('blue');
	$mdThemingProvider.theme('blue-grey').primaryPalette('blue-grey')
			.accentPalette('grey').warnPalette('blue');

	// This is the absolutely vital part, without this, changes will not cascade
	// down through the DOM.
	$mdThemingProvider.alwaysWatchTheme(true);
});

app.config(function($logProvider) {
	// $logProvider.debugEnabled(false);
	$logProvider.debugEnabled(true);// this is default
});
app.factory('ajsCache', function($cacheFactory) {
	return $cacheFactory('browserCache');
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
	}).state('selectmultibiz', {
		url : "/selectbiz",
		templateUrl : "/app/login/multiselectbusiness.html",
		controller : 'indexCtr'
	}).state('home', {
		url : "/home",
		templateUrl : '/home.html',
		controller : 'homeCtr'

	}).state('stock', {
		url : "/stock",
		templateUrl : '/app/stock/stock_module.html',
		controller : 'stockModuleCtr'
	}).state('stock.stockItemAdd', {
		url : "/stockItemAdd/:selectedStocksId",
		templateUrl : '/app/stock/stockItem_add.html',
		controller : 'stockAddCtr'
	}).state('stock.stockItemList', {
		url : "/stockItemList",
		templateUrl : '/app/stock/stockItem_list.html',
		controller : 'stockListCtr'
	})

	.state('stock.warehouseAdd', {
		url : "/warehouseAdd/:selectedWarehouseId",
		templateUrl : '/app/stock/warehouse_add.html',
		controller : 'warehouseAddCtr'
	}).state('stock.warehouseList', {
		url : "/warehouseList",
		templateUrl : '/app/stock/warehouse_list.html',
		controller : 'warehouseListCtr'

	}).state('stock.reportByThreshold', {
		url : "/reportByThreshold",
		templateUrl : '/app/report/stock_reportByThreshold.html',
		controller : 'stockReportByThresholdCtr'
	}).state('stock.reportByWarehouse', {
		url : "/reportByWarehouse",
		templateUrl : '/app/report/stock_reportByWarehouse.html',
		controller : 'stockReportByWarehouseCtr'

	}).state('stock.reportByTaxPaid', {
		url : "/reportByTaxPaid",
		templateUrl : '/app/report/tax_reportByTaxPaid.html',
		controller : 'ReportByTaxPaidCtr'
	}).state('stock.reportByTaxRecived', {
		url : "/reportByTaxRecived",
		templateUrl : '/app/report/tax_reportByTaxReceived.html',
		controller : 'ReportByTaxReceivedCtr'

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
		url : "/list/:selectedCustomerId",
		templateUrl : '/app/invoice/invoice_list.html',
		controller : 'invoiceListCtr',
	}).state('invoice.view', {
		url : "/view/:selectedInvoiceNo",
		templateUrl : '/app/invoice/invoice_view.html',
		controller : 'invoiceViewCtr',
	}).state('invoice.edit', {
		url : "/edit/:selectedInvoiceNo",
		templateUrl : '/app/invoice/invoice_edit.html',
		controller : 'invoiceEditCtr',
	}).state('invoice.settings', {
		url : "/settings",
		templateUrl : '/app/invoice/invoice_settings.html',
		controller : 'invoiceSettingsCtr',
	}).state('customer', {
		url : "/customer",
		templateUrl : '/app/customer/customer_module.html',
		controller : 'customerModuleCtr'
	}).state('customer.add', {
		url : "/add/:selectedCustomerId",
		templateUrl : '/app/customer/customer_add.html',
		controller : 'customerAddCtr'
	})/*
		 * .state('customer.customerSOList', { url :
		 * "/customerSOList/:selectedCustomerId", templateUrl :
		 * '/app/customer/customer_SO.html', controller : 'customerSOListCtr'
		 * }).state('customer.customerInvoiceList', { url :
		 * "/customerInvoiceList/:selectedCustomerId", templateUrl :
		 * '/app/customer/customer_invoiceList.html', controller :
		 * 'customerInvoiceListCtr' })
		 */
	.state('customer.list', {
		url : "/list",
		templateUrl : '/app/customer/customer_list.html',
		controller : 'customerListCtr'
	}).state('account', {
		url : "/account",
		templateUrl : '/app/account/account_module.html',
		controller : 'accountModuleCtr'
	}).state('account.accountAdd', {
		url : "/accountAdd/:selectedAccountId",
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
		url : "/accountPayable/:selectedPayableId",
		templateUrl : '/app/account/account_payable.html',
		controller : 'accountPayableCtr'
	}).state('account.accountPayableList', {
		url : "/accountPayableList",
		templateUrl : '/app/account/account_payableList.html',
		controller : 'accountPayableListCtr'
	}).state('account.accountReceivable', {
		url : "/accountReceivable/:selectedReceivableId",
		templateUrl : '/app/account/account_receivable.html',
		controller : 'accountReceivableCtr'
	}).state('account.accountReceivableList', {
		url : "/accountReceivableList",
		templateUrl : '/app/account/account_receivableList.html',
		controller : 'accountReceivableListCtr'
	}).state('account.fileUpload', {
		url : "/fileUpload",
		templateUrl : '/app/demo/fileUpload.html',
		controller : 'AppController'
	})

	.state('accounting', {
		url : "/accounting",
		templateUrl : '/app/accounting/accounting_module.html',
		controller : 'accountModuleCtr'
	})

	.state('accounting.account_add2', {
		url : "/account_add/:AccountId",
		templateUrl : '/app/accounting/account_add2.html',
		controller : 'addacountCtr',
	}).state('accounting.accountlist', {
		url : "/accountlist",
		templateUrl : '/app/accounting/accountlist.html',
		controller : 'accountlistCtr',

	})

	.state('accounting.account_Edit', {
		url : "/account_Edit",
		templateUrl : '/app/accounting/account_Edit.html',
		controller : 'accountEditCtr',
		params : {

			selectedAccount : null
		}
	})

	.state('accounting.accountGroupAdd', {
		url : "/accountGroupAdd",
		templateUrl : '/app/accounting/accountGroupAdd.html',
		controller : 'accountGroupCtr'
	}).state('accounting.accountGroupList', {
		url : "/accountGroupList",
		templateUrl : '/app/accounting/accountGroupList.html',
		controller : 'accountGrpListCtr'
	}).state('accounting.accountGroupEdit', {
		url : "/accountGroupEdit",
		templateUrl : '/app/accounting/accountGroupEdit.html',
		controller : 'accountGrpEditCtr',
		params : {

			record : null
		}
	}).state('accounting.accountGroupDisplay', {
		url : "/accountGroupDisplay",
		templateUrl : '/app/accounting/accountGroupDisplay.html',
		controller : 'accountGrpDisplayCtr',
		params : {

			record : null
		}

	}).state('accounting.balanceSheet', {
		url : "/accountBalanceSheet",
		templateUrl : '/app/accounting/accountBalanceSheet.html',
		controller : 'accountBalanceSheetCtr',
		params : {

			record : null
		}

	}).state('accounting.trialBalance', {
		url : "/trialBalance",
		templateUrl : '/app/accounting/accountTrialBalance.html',
		controller : 'trialBalanceCtr'
	})

	.state('accounting.accountGroupView', {
		url : "/accountGroupView",
		templateUrl : '/app/accounting/accountGroupView.html',
		controller : 'accountGroupViewCtr',
		params : {
			flag : null,
			selectdAccount : null,
			fromDate : null,
			toDate : null
		}
	})

	.state('accounting.addGeneralEntry', {
		url : "/addGeneralEntry",
		templateUrl : '/app/accounting/account_addGeneralEntry.html',
		controller : 'addAccountGeneralEntryCtr'
	}).state('accounting.addEntry', {
		url : "/addEntry",
		templateUrl : '/app/accounting/account_addEntry.html',
		controller : 'addAccountEntryController'
	}).state('accounting.entryList', {
		url : "/entryList",
		templateUrl : '/app/accounting/account_entryList.html',
		controller : 'accountEntryListController',
		params : {
			selectdAccount : null,
			fromDate : null,
			toDate : null
		}

	}).state('accounting.generalEntitylist', {
		url : "/generalEntitylist",
		templateUrl : '/app/accounting/generalEntitylist.html',
		controller : 'generalListCtr'
	})

	.state('accounting.voucher', {
		url : "/voucher",
		templateUrl : '/app/accounting/voucher.html',
		controller : 'voucherCtr',
	})

	.state('accounting.voucherSales', {
		url : "/SalesVoucher",
		templateUrl : '/app/accounting/voucherSales.html',
		controller : 'voucherSalesCtr',

		params : {
			Account : ""
		}
	})

	.state('accounting.voucherList', {
		url : "/voucherList",
		templateUrl : '/app/accounting/voucherList.html',
		controller : 'voucherListCtr'
	})

	.state('accounting.voucherSalesList', {
		url : "/SalesVoucherList",
		templateUrl : '/app/accounting/voucherSalesList.html',
		controller : 'voucherSalesListCtr'

	})

	.state('accounting.voucherRecieptList', {
		url : "/RecieptVoucherList",
		templateUrl : '/app/accounting/voucherRecieptList.html',
		controller : 'voucherRecieptListCtr'

	}).state('accounting.voucherReceipt', {
		url : "/ReceiptVoucher",
		templateUrl : '/app/accounting/voucherReceipt.html',
		controller : 'voucherRecieptCtr',
		params : {
			Account : ""
		}

	})

	.state('accounting.voucherPurches', {
		url : "/PurchesVoucher",
		templateUrl : '/app/accounting/voucherPurches.html',
		controller : 'voucherPurchesCtr',
		params : {
			Account : ""
		}

	})

	.state('accounting.voucherPurchesList', {
		url : "/PurchesVoucherList",
		templateUrl : '/app/accounting/voucherPurchesList.html',
		controller : 'voucherPurchesListCtr'
	})

	.state('accounting.accountChart', {
		url : "/accountChart",
		templateUrl : '/app/accounting/accountChart.html',
		controller : 'accountChartCtr'
	})

	.state('journal', {
		url : "/journal",
		templateUrl : '/app/journal/journal_module.html',
		controller : 'journalModuleCtr'
	}).state('journal.add', {
		url : "/addJournal",
		templateUrl : '/app/journal/journal_addEntry.html',
		controller : 'addJournalCtr'
	}).state('journal.journalList', {
		url : "/journalList",
		templateUrl : '/app/journal/journal_entryList.html',
		controller : 'journalEntryListCtr'
	})

	.state('report', {
		url : "/report",
		templateUrl : '/app/report/report_module.html',
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

	}).state('salesOrder.edit', {
		url : "/edit/:selectedSOId",
		templateUrl : '/app/sales/salesOrder_edit.html',
		controller : 'salesOrderEditCtr'

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

	}).state('purchaseOrder.POedit', {
		url : "/POedit/:selectedPONo",
		templateUrl : '/app/purchase/purchaseOrder_edit.html',
		controller : 'purchaseOrderEditCtr'

	}).state('purchaseOrder.supplierAdd', {
		url : "/supplierAdd/:selectedSupplierNo",
		templateUrl : '/app/purchase/supplier_add.html',
		controller : 'supplierAddCtr'
	})

	.state('purchaseOrder.supplierList', {
		url : "supplierList",
		templateUrl : '/app/purchase/supplier_list.html',
		controller : 'supplierListCtr'

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
		templateUrl : '/app/hr/empsalslipstructlist.html',
		controller : 'hrCtr.empsalslipstruct',
	}).state('hr.gSalSlip', {
		url : "/viewSalSlip/:selectedempstructno",
		templateUrl : '/app/hr/view_salarystruct.html',
		controller : 'hrCtr.empsalslipstruct',
	}).state('hr.generatesalslip', {
		url : "/generatesalslip",
		templateUrl : '/app/hr/ganerate_multiple_salaryslip.html',
		controller : 'hrCtr.emplist_to_ganeratesalslip',
	}).state('hr.printgeneratesalslip', {
		url : "/selectedlist",
		templateUrl : '/app/hr/slected_Employeesalaryslip.html',
		controller : 'hrctr.selected_Employeesalaryslip',
		params : {
			ganeratedsalslip : ""
		}
	}).state('hr.print', {
		url : "/print/:printempidsalslip",
		templateUrl : '/app/hr/print_salaryslip.html',
		controller : 'hrctr.selected_Employeesalaryslip',
	}).state('hr.LeaveDetail', {
		url : "/LeaveDetail",
		templateUrl : '/app/hr/LeaveDetail.html',
		controller : 'LeaveDetails',
	}).state('hr.payRollRepo', {
		url : "/payRollReport",
		templateUrl : '/app/hr/payRollReports.html',
		controller : 'payRollReports',
	}).state('hr.monthlyTax', {
		url : "/monthlyTax",
		templateUrl : '/app/hr/monthlyTax.html',
		controller : 'monthlyTax',
	}).state('hr.SalaryMaster', {
		url : "/SalaryMaster",
		templateUrl : '/app/hr/SalaryMaster.html',
		controller : 'SalaryMaster',
	}).state('hr.MonthlyPaymentDetail', {
		url : "/MonthlyPaymentDetail",
		templateUrl : '/app/hr/MonthlyPaymentDetail.html',
		controller : 'MonthlyPaymentDetail',
	}).state('crm', {
		url : "/crm",
		templateUrl : '/app/crm/crm_module.html',
		controller : 'crm',
	}).state('crm.customer', {
		url : "/customer",
		templateUrl : '/app/customer/customer_module.html',
		controller : 'customerModuleCtr',
	}).state('crm.lead', {
		url : "/lead",
		templateUrl : '/app/crm/lead_list.html',
		controller : 'leadList',
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
		controller : 'contactsList',
	}).state('crm.listOfContact', { // view contact in customer form
		url : "/ViewContact/:selectedCustomerId",
		templateUrl : '/app/crm/ViewContact_list.html',
		controller : 'ViewContactsList',
	}).state('crm.addcontact', {
		url : "/addContact",
		templateUrl : '/app/crm/crm_contacts.html',
		controller : 'contacts',
	}).state('crm.viewContact', {
		url : "/viewContact/:selectedcontactNo",
		templateUrl : '/app/crm/crm_contacts_view.html',
		controller : 'contactsList',

	}).state('crm.opportunity', {
		url : "/opportunity",
		templateUrl : '/app/crm/opportunity_list.html',
		controller : 'opportunityList',

	}).state('crm.addopprtunity', {
		url : "/addopportunity",
		templateUrl : '/app/crm/crm_opportunity.html',
		controller : 'opportunity',

	}).state('crm.opportunityView', {
		url : "/opportunityView/:selectedopportunityNo",
		templateUrl : '/app/crm/crm_opportunity_view.html',
		controller : 'opportunityList',

	}).state('crm.customerList', {
		url : "/customerList",
		templateUrl : '/app/crm/customer_list.html',
		controller : 'customerListCtr',

	}).state('crm.addcustomer', {
		url : "/customerAdd/:selectedCustomerId",
		templateUrl : '/app/crm/customer_add.html',
		controller : 'customerAddCtr',

	}).state('crm.customerInvoiceList', {
		url : "/crmCustomerInvoiceList/:selectedCustomerId",
		templateUrl : '/app/crm/customer_invoiceList.html',
		controller : 'customerInvoiceListCtr',

	}).state('crm.customerSOList', {
		url : "/crmCustomerSOList/:selectedCustomerId",
		templateUrl : '/app/crm/customer_SO.html',
		controller : 'customerSOListCtr',

	}).state('setup', {
		url : "/setup",
		templateUrl : '/app/setup/setup_module.html',
		controller : 'setup',

	/*
	 * }).state('setup.user', { url : "/user", templateUrl :
	 * '/app/setup/setup_userlist.html', controller : 'setup',
	 */
	}).state('setup.useradd', {
		url : "/useradd",
		templateUrl : '/app/setup/setup_adduser.html',
		controller : 'setup.adduser',

	}).state('setup.userview', {
		url : "/userview",
		templateUrl : '/app/setup/setup_viewuser.html',
		controller : 'setup.viewuser',
		params : {
			selectedUser : null
		}

	}).state('setup.footer', {
		url : "/addfooter",
		templateUrl : '/app/setup/setup_footer.html',
		controller : 'setup_footer',

	}).state('setup.businesssetup', {
		url : "/businesssetup",
		templateUrl : '/app/setup/EditBusiness.html',
		controller : 'editBusiness',

	}).state('setup.changeplan', {
		url : "/changePaln",
		templateUrl : '/app/setup/changeplan.html',
		controller : 'setup.changeplan',

	}).state('setup.changetheme', {
		url : "/changetheme",
		templateUrl : '/app/setup/setup_changetheme.html',
		controller : 'setup.changetheme',

	}).state('setup.setlogo', {
		url : "/setLogo",
		/* templateUrl : '/app/setup/uplode.jsp', */
		templateUrl : '/app/setup/setup_setLogo.html',
		/* templateUrl : '/app/setup/sendemail.html', */
		controller : 'setup.setLogo',

	}).state('setup.sendmail', {
		url : "/sendmail",
		templateUrl : '/app/setup/sendemail.html',
		controller : 'sendmail',

	}).state('setup.userlist', {
		url : "/userlist",
		templateUrl : '/app/setup/userlist.html',
		controller : 'userlist',
		params : {
			action : 'useradd'
		}
	}).state('setup.disclaimer', {
		url : "/disclaimer",
		templateUrl : '/app/setup/disclaimer.html',
		controller : 'disclaimer',

	})

	.state('setup.departmentList', {
		url : "/departmentList",
		templateUrl : '/app/setup/setup_departmentList.html',
		controller : 'setup.departmentList',

	})

	.state('setup.userauth', {
		url : "/userauth",
		templateUrl : '/app/setup/manage_user_auth.html',
		controller : 'manageUserAuthCtr',
		params : {
			selectedUser : null
		}
	}) /*
		 * .state('login', { url : "/login", templateUrl :
		 * '/app/login/login.html', controller : 'login', })
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
	}).state('needbusiness', {
		url : "/NeedBusinessAccount",
		templateUrl : '/app/login/needBusinessAccount.html',
		controller : 'needbusinessCtr'
	}).state('newBusinessAccount', {
		url : "/NewBusinessAccount",
		templateUrl : '/app/login/newBusinessAccount.html',
		controller : 'newbusinessCtr'
	}).state('createBusinessAccount', {
		url : "/CreateBusiness",
		templateUrl : '/app/login/createBusinessAccount.html',
		controller : 'needbusinessCtr'
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
	}).state('user_prof_detail', {
		url : "/profile",
		templateUrl : '/app/profile/profileModule.html',
		controller : 'profileCtr'
	}).state('user_prof_detail.viewprofile', {
		url : "/viewprofile",
		templateUrl : '/app/profile/viewprofile.html',
		controller : 'viewProfile'
	}).state('user_prof_detail.changepassword', {
		url : "/changepassword",
		templateUrl : '/app/profile/changepassword.html',
		controller : 'changepass'
	}).state('user_prof_detail.getAllSlip', {
		url : "/getAllSlip/:viewsalslips",
		templateUrl : '/app/profile/getAllSalSlip.html',
		controller : 'AllSalslip'
	}).state('user_prof_detail.monthlyTax', {
		url : "/MyMonthlyDeduction",
		templateUrl : '/app/profile/monthlyTax.html',
		controller : 'myMonthlyDeduction',
	})

	.state('user_prof_detail.print', {
		url : "/print/:printempidsalslip",
		/*
		 * templateUrl : '/app/profile/print_salaryslip.html', controller :
		 * 'AllSalslip',
		 */
		templateUrl : '/app/hr/print_salaryslip.html',
		controller : 'hrctr.selected_Employeesalaryslip',
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
		controller : 'AddAsset',
	}).state('AssetMangement.list', {
		url : "/ListAsset",
		templateUrl : '/app/AssetMangement/ListAsset.html',
		controller : 'ListAsset',
	}).state('AssetMangement.editasset', {
		url : "/editasset/:selectedasetNo",
		templateUrl : '/app/AssetMangement/assignupdateuser.html',
		controller : 'AssignupdateAsset',
	}).state('AssetMangement.assignuser', {
		url : "/assignuser/:selectedasetNo",
		templateUrl : '/app/AssetMangement/assignuser.html',
		controller : 'AssignupdateAsset',
	}).state('taskmanagement', {
		url : "/taskmanagement",
		templateUrl : '/app/taskmanagement/task_module.html',
		controller : 'taskModuleCtr',
	}).state('taskmanagement.add', {
		url : "/taskadd",
		templateUrl : '/app/taskmanagement/task_add.html',
		controller : 'taskModuleCtr',
		params : {
			taskObj : null
		}
	}).state('taskmanagement.list', {
		url : "/tasklist",
		templateUrl : '/app/taskmanagement/task_list.html',
		controller : 'taskModuleCtr',
		params : {
			action : 'listall'
		}
	}).state('taskmanagement.mytasklist', {
		url : "/mytasklist",
		templateUrl : '/app/taskmanagement/task_list.html',
		controller : 'taskModuleCtr',
		params : {
			action : 'listmytask'
		}
	}).state('taskmanagement.tasklistreport', {
		url : "/tasklistreport",
		templateUrl : '/app/taskmanagement/task_list_report.html',
		controller : 'taskModuleCtr',
		params : {
			action : 'tasklistreport'
		}
	}).state('proAdmin', {
		url : "/proadmin",
		templateUrl : '/app/ProAdmin/ProAdminModule.html',
		controller : 'proadminctr',
	}).state('proAdmin.addAccount', {
		url : "/addAccountType",
		templateUrl : '/app/ProAdmin/AddAccountType.html',
		controller : 'AddAccountType',
	}).state('proAdmin.listAccount', {
		url : "/listAccountType",
		templateUrl : '/app/ProAdmin/ListAccountType.html',
		controller : 'ListAccountType',
	}).state('proAdmin.editAccoutType', {
		url : "/EditAccoutType/:typeid",
		templateUrl : '/app/ProAdmin/EditAccoutType.html',
		controller : 'ListAccountType',
	}).state('proAdmin.manageauthmaster', {
		url : "/proadmin/manage_auth_master",
		templateUrl : '/app/ProAdmin/manage_auth_master.html',
		controller : 'proAdminManageAuth',
	}).state('initsetup', {
		url : "/initsetup",
		templateUrl : '/app/initsetup/initsetup.html',
		controller : 'initsetup',
	}).state('proAdmin.probusiness', {
		url : "/businesslist",
		templateUrl : '/app/ProAdmin/probusiness.html',
		controller : 'probusinessCtr'
	}).state('proAdmin.managebizauth', {
		url : "/manageinstituteauth",
		templateUrl : '/app/ProAdmin/manage_institute_auth.html',
		controller : 'proAdminManageBizAuth',
		params : {
			selectedBusiness : null
		}
	}).state('proAdmin.editBusiness', {
		url : "/editbusinesssetup",
		templateUrl : '/app/ProAdmin/setup_module.html',
		controller : 'setup',
		params : {
			selectedBusiness : null
		}
	}).state('proAdmin.editBusiness.useradd', {
		url : "/useradd",
		templateUrl : '/app/setup/setup_adduser.html',
		controller : 'setup.adduser',
		params : {
			selectedBusiness : null
		}

	}).state('proAdmin.editBusiness.businesssetup', {
		url : "/businesssetup",
		templateUrl : '/app/setup/EditBusiness.html',
		controller : 'editBusiness',
		params : {
			selectedBusiness : null
		}

	}).state('proAdmin.editBusiness.changeplan', {
		url : "/changePaln",
		templateUrl : '/app/setup/changeplan.html',
		controller : 'setup.changeplan',
		params : {
			selectedBusiness : null
		}

	}).state('proAdmin.editBusiness.userlist', {
		url : "/userlist",
		templateUrl : '/app/setup/userlist.html',
		controller : 'userlist',
		params : {
			selectedBusiness : null
		}
	}).state('proAdmin.editBusiness.userview', {
		url : "/userview/:selecteduserNo",
		templateUrl : '/app/setup/setup_viewuser.html',
		controller : 'setup.viewuser',
		params : {
			selectedUser : null
		}
	})

});
