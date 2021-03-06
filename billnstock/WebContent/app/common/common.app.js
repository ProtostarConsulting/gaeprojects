var app = angular.module("stockApp", [ 'ngResource', 'ngAnimate', 'ngStorage',
		'ngRoute', 'ngAria', 'angularCSS', 'mdPickers', 'ngMaterial', 'ngMessages',
		"ui.bootstrap", "ui.router", 'md.data.table', 'ngFileUpload',
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
app.directive('stringToNumber', function() {
	return {
		require : 'ngModel',
		link : function(scope, element, attrs, ngModel) {
			ngModel.$parsers.push(function(value) {
				return '' + value;
			});
			ngModel.$formatters.push(function(value) {
				return parseFloat(value);
			});
		}
	}
});
app.directive('stringToDate', function() {
	return {
		require : 'ngModel',
		link : function(scope, element, attrs, ngModel) {
			ngModel.$parsers.push(function(value) {
				return value;
			});
			ngModel.$formatters.push(function(value) {
				return new Date(value);
			});
		}
	}
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
app.filter('proOrderObjectBySubTextField', function() {
	return function(input, subObject, attribute) {
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
			var subObA = a[subObject];
			var subObB = a[subObject];
			a = String(subObA[attribute]);
			b = String(subObB[attribute]);
			return a.localeCompare(b)
		});
		return array;
	}
});

app.filter('proOrderObjectByTextField', function() {
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
			a = String(a[attribute]);
			b = String(b[attribute]);
			return a.localeCompare(b)
		});
		return array;
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

app.directive('scrollPosition', function($window, $rootScope) {
	return {
		scope : {
			scroll : '=scrollPosition'
		},
		link : function(scope, element, attrs) {
			var windowEl = angular.element(element);
			var handler = function() {
				scope.scroll = windowEl[0].scrollTop;
				$rootScope.scroll = windowEl[0].scrollTop;
			}
			windowEl.on('scroll', scope.$apply.bind(scope, handler));
			handler();
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
	$logProvider.debugEnabled(true); // this is default
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
		controller : 'multiSelectBusinessCtr'
	}).state('home', {
		url : "/home",
		templateUrl : '/home.html',
		controller : 'homeCtr'
	}).state('stock', {
		url : "/stock",
		templateUrl : '/app/stock/stock_module.html',
		controller : 'stockModuleCtr',
		css: '/app/stock/css/module.css'
	}).state('stock.settings', {
		url : "/stocksettings",
		templateUrl : '/app/stock/stock_settings.html',
		controller : 'stockSettingsCtr'
	}).state('stock.stockItemTypeAdd', {
		url : "/stockItemType",
		templateUrl : '/app/stock/stock_item_type_add.html',
		controller : 'stockItemTypeAddCtr',
		params : {
			selectedStockItem : null
		}
	}).state('stock.stockItemTypeList', {
		url : "/stockItemTypeList",
		templateUrl : '/app/stock/stock_item_type_list.html',
		controller : 'stockItemTypeListCtr'
	}).state('stock.stockItemAdd', {
		url : "/stockitem",
		templateUrl : '/app/stock/stockItem_add.html',
		controller : 'stockAddCtr',
		params : {
			selectedStockItem : null
		}
	}).state('stock.stockItemList', {
		url : "/stockItemList",
		templateUrl : '/app/stock/stockItem_list.html',
		controller : 'stockListCtr'
	}).state('stock.stockReceiptAdd', {
		url : "/stockreceiptadd",
		templateUrl : '/app/stock/stock_receipt_add.html',
		controller : 'stockReceiptAddCtr',
		params : {
			stockReceiptObj : null
		}
	}).state('stock.stockReceiptList', {
		url : "/stockreceiptlist",
		templateUrl : '/app/stock/stock_receipt_list.html',
		controller : 'stockReceiptListCtr'
			
	}).state('stock.stockReceiptQCAdd', {
		url : "/stockreceipt_qc_add",
		templateUrl : '/app/stock/stock_receipt_qc_add.html',
		controller : 'stockReceiptQCAddCtr',
		params : {
			qcstockReceiptObj : null
		}
	}).state('stock.stockReceiptQCList', {
		url : "/stockreceipt_qc_list",
		templateUrl : '/app/stock/stock_receipt_qc_list.html',
		controller : 'qcstockReceiptListCtr',
	}).state('stock.addStockReceiptQCRecord', {
		url : "/stockreceipt_qcrecord_add",
		templateUrl : '/app/stock/stockreceipt_qcrecord_add.html',
		controller : 'addStockReceiptQCRecordCtr',
		params : {
			stockReceiptQCRecordObj : null,
			stockReceiptObj : null,
			tempReceiptObj : null
		}
	}).state('stock.listStockReceiptQCRecord', {
		url : "/stockreceipt_qcrecord_list",
		templateUrl : '/app/stock/stockreceipt_qcrecord_list.html',
		controller : 'listStockReceiptQCRecordCtr',
			params : {
				stockReceiptObj : null,
				tempObj : null
			}
	}).state('stock.stockShipmentAdd', {
		url : "/stockshipmentadd",
		templateUrl : '/app/stock/stock_shipment_add.html',
		controller : 'stockShipmentAddCtr',
		params : {
			stockShipmentObj : null
		}
	}).state('stock.stockShipmentList', {
		url : "/stockshipmentlist",
		templateUrl : '/app/stock/stock_shipment_list.html',
		controller : 'stockShipmentListCtr'
	}).state('stock.warehouseAdd', {
		url : "/warehouse",
		templateUrl : '/app/stock/warehouse_add.html',
		controller : 'warehouseAddCtr',
		params : {
			selectedWarehouse : null
		}
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
	}).state('stock.reportStockTransaction', {
		url : "/StockTransactionReport",
		templateUrl : '/app/report/stock_report_stocktxn.html',
		controller : 'stockTxnReportCtr'
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
		controller : 'taxCtr',
		params : {
			selectedTax : null
		}
	}).state('stock.taxlist', {
		url : "/tax/taxlist",
		templateUrl : '/app/tax/tax_list.html',
		controller : 'taxCtr'
	}).state('stock.invoice_add', {
		url : "/add",
		templateUrl : '/app/invoice/invoice_add.html',
		controller : 'invoiceAddCtr',
		params : {
			invoiceObj : null
		}
	}).state('stock.invoice_list', {
		url : "/list/:selectedCustomerId",
		templateUrl : '/app/invoice/invoice_list.html',
		controller : 'invoiceListCtr',
	}).state('stock.quotation_add', {
		url : "/addQuotation",
		templateUrl : '/app/invoice/quotation_add.html',
		controller : 'quotationAddCtr',
		params : {
			invoiceObj : null
		}
	}).state('stock.quotation_list', {
		url : "/listQuotation",
		templateUrl : '/app/invoice/quotation_list.html',
		controller : 'quotationListCtr',
	}).state('stock.invoice_settings', {
		url : "/settings",
		templateUrl : '/app/invoice/invoice_settings.html',
		controller : 'invoiceSettingsCtr',
	}).state('invoice', {
		url : "/invoice",
		templateUrl : '/app/invoice/invoice_module.html',
		controller : 'invoiceModuleCtr'
	}).state('customer', {
		url : "/customer",
		templateUrl : '/app/customer/customer_module.html',
		controller : 'customerModuleCtr'
	}).state('customer.add', {
		url : "/add/:selectedCustomerId",
		templateUrl : '/app/customer/customer_add.html',
		controller : 'customerAddCtr'
	}) /*
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
		controller : 'bizPlanAddCtr'
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
	}).state('accounting', {
		url : "/accounting",
		templateUrl : '/app/accounting/accounting_module.html',
		controller : 'accountModuleCtr'
	}).state('accounting.settings', {
		url : "/settings",
		templateUrl : '/app/accounting/accounting_settings.html',
		controller : 'accountingSettingsCtr',
	}).state('accounting.account_add', {
		url : "/account_add",
		templateUrl : '/app/accounting/account_add.html',
		controller : 'accountAddCtr',
		params : {
			selectedAccount : null
		}
	}).state('accounting.accountlist', {
		url : "/accountlist",
		templateUrl : '/app/accounting/accountlist.html',
		controller : 'accountlistCtr',
	}).state('accounting.accountGroupAdd', {
		url : "/accountGroupAdd",
		templateUrl : '/app/accounting/accountGroupAdd.html',
		controller : 'accountGroupCtr',
		params : {
			selectedAccountGroup : null
		}
	}).state('accounting.accountGroupList', {
		url : "/accountGroupList",
		templateUrl : '/app/accounting/accountGroupList.html',
		controller : 'accountGrpListCtr'
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
	}).state('accounting.accountPAndL', {
		url : "/ProfitAndLoss",
		templateUrl : '/app/accounting/accountPAndL.html',
		controller : 'accountPAndL',
		params : {
			record : null
		}
	})

	.state('accounting.trialBalance', {
		url : "/trialBalance",
		templateUrl : '/app/accounting/accountTrialBalance.html',
		controller : 'trialBalanceCtr'
	}).state('accounting.accountGroupView', {
		url : "/accountGroupView",
		templateUrl : '/app/accounting/accountGroupView.html',
		controller : 'accountGroupViewCtr',
		params : {
			flag : null,
			selectdAccount : null,
			fromDate : null,
			toDate : null
		}
	}).state('accounting.addGeneralEntry', {
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
	}).state('accounting.voucher', {
		url : "/voucher",
		templateUrl : '/app/accounting/voucher.html',
		controller : 'voucherCtr',
	}).state('accounting.voucherSales', {
		url : "/SalesVoucher",
		templateUrl : '/app/accounting/voucherSales.html',
		controller : 'voucherSalesCtr',

		params : {
			Account : ""
		}
	}).state('accounting.voucherList', {
		url : "/voucherList",
		templateUrl : '/app/accounting/voucherList.html',
		controller : 'voucherListCtr'
	}).state('accounting.voucherSalesList', {
		url : "/SalesVoucherList",
		templateUrl : '/app/accounting/voucherSalesList.html',
		controller : 'voucherSalesListCtr'
	}).state('accounting.voucherRecieptList', {
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
	}).state('accounting.voucherPurches', {
		url : "/PurchesVoucher",
		templateUrl : '/app/accounting/voucherPurches.html',
		controller : 'voucherPurchesCtr',
		params : {
			Account : ""
		}
	}).state('accounting.voucherPurchesList', {
		url : "/PurchesVoucherList",
		templateUrl : '/app/accounting/voucherPurchesList.html',
		controller : 'voucherPurchesListCtr'
	}).state('accounting.dayBook', {
		url : "/dayBook",
		templateUrl : '/app/accounting/dayBook.html',
		controller : 'dayBook',

	}).state('production', {
		url : "/production",
		templateUrl : '/app/production/prod_module.html',
		controller : 'prodModuleCtr'
	}).state('production.view_prod_requisition', {
		url : "/view_prod_requisition",
		templateUrl : '/app/production/view_prod_requisition.html',
		controller : 'view_prod_requisition',
		params : {
			productionRequisition : null
		}
	}).state('production.list_prod_requisition', {
		url : "/list_prod_requisition",
		templateUrl : '/app/production/list_prod_requisition.html',
		controller : 'list_prod_requisition',

	}).state('production.add_bom', {
		url : "/addBillOfMaterial",
		templateUrl : '/app/production/add_bom.html',
		controller : 'add_bom',
		params : {
			bomEntity: null
		}
	}).state('production.list_bom', {
		url : "/list_bom",
		templateUrl : '/app/production/list_bom.html',
		controller : 'list_bom'
	}).state('production.add_prod_plan', {
		url : "/add_prod_plan",
		templateUrl : '/app/production/add_prod_plan.html',
		controller : 'addProdPlanCtr',
		params : {
			prodPlan : null
		}
	}).state('production.list_prod_plan', {
		url : "/list_prod_plan",
		templateUrl : '/app/production/list_prod_plan.html',
		controller : 'listProdPlanCtr'
	}).state('production.add_machine', {
		url : "/add_machine",
		templateUrl : '/app/production/add_machine.html',
		controller : 'machineAddCtr',
		params : {
			machineObj : null
		}
	}).state('production.list_machine', {
		url : "/list_machine",
		templateUrl : '/app/production/list_machine.html',
		controller : 'machineListCtr'

	}).state('production.add_qcmachine', {
		url : "/add_qcmachine",
		templateUrl : '/app/production/add_qcmachine.html',
		controller : 'qcmachineAddCtr',
		params : {
			qcmachineObj : null
		}
	}).state('production.list_qcmachine', {
		url : "/list_qcmachine",
		templateUrl : '/app/production/list_qcmachine.html',
		controller : 'qcmachineListCtr'
	}).state('production.add_qcmachine_record', {
		url : "/add_qcmachine_record",
		templateUrl : '/app/production/add_qcmachine_record.html',
		controller : 'qcmachineAddRecordCtr',
		params : {
			qcmachineRecordObj : null
		}
	}).state('production.list_qcmachine_record', {
		url : "/list_qcmachine_record",
		templateUrl : '/app/production/list_qcmachine_record.html',
		controller : 'qcmachineRecordListCtr'
	}).state('production.setting', {
		url : "/production_setting",
		templateUrl : '/app/production/production_setting.html',
		controller : 'productionSettingsCtr'
	})

	// //////////////////////

	.state('accounting.voucherPayment', {

		url : "/PaymentVoucher",
		templateUrl : '/app/accounting/voucherPayment.html',
		controller : 'voucherPaymentCtr',
		params : {
			Account : "",
			stockReceiptObj : null
		}
	}).state('accounting.voucherPaymentList', {
		url : "/PaymentVoucherList",
		templateUrl : '/app/accounting/voucherPaymentList.html',
		controller : 'voucherPaymentListCtr'
	}).state('accounting.accountChart', {
		url : "/accountChart",
		templateUrl : '/app/accounting/accountChart.html',
		controller : 'accountChartCtr'
	}).state('journal', {
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
	}).state('report', {
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
	}).state('stock.requisitionAdd', {
		url : "/RequisitionAdd",
		templateUrl : '/app/stock/requisition_add.html',
		controller : 'requisitionAddCtr',
		params : {
			selectedRequisition : null
		}
	}).state('stock.requisitionList', {
		url : "/RequisitionList",
		templateUrl : '/app/stock/requisition_list.html',
		controller : 'requisitionListCtr',
	}).state('stock.addBudget', {
		url : "/BudgetAdd",
		templateUrl : '/app/stock/budget_add.html',
		controller : 'budgetAddCtr',
		params : {
			selectedBudgetObj : null
		}
	}).state('stock.budgetList', {
		url : "/BudgetList",
		templateUrl : '/app/stock/budget_list.html',
		controller : 'budgetListCtr',
	}).state('stock.purchaseOrderAdd', {
		url : "/PurchaseOrderAdd",
		templateUrl : '/app/stock/purchaseOrder_add.html',
		controller : 'purchaseOrderAddCtr',
		params : {
			purchaseOrderObj : null
		}
	}).state('stock.purchaseOrderList', {
		url : "/PurchaseOrderList",
		templateUrl : '/app/stock/purchaseOrder_list.html',
		controller : 'purchaseOrderListCtr'
	}).state('stock.supplierAdd', {
		url : "/supplierAdd",
		templateUrl : '/app/stock/supplier_add.html',
		controller : 'supplierAddCtr',
		params : {
			selectedSupplier : null
		}
	}).state('stock.supplierList', {
		url : "supplierList",
		templateUrl : '/app/stock/supplier_list.html',
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
	}).state('hr.hrSettings', {
		url : "/hrSettings",
		templateUrl : '/app/hr/hr_settings.html',
		controller : 'hrSettingsCtr',
	}).state('hr.LeaveDetail', {
		url : "/LeaveDetail",
		templateUrl : '/app/hr/LeaveDetail.html',
		controller : 'LeaveDetails',
	}).state('hr.payRollRepo', {
		url : "/payRollReport",
		templateUrl : '/app/hr/payRollReports.html',
		controller : 'payRollReports',
		params : {
			payRollReportList : null
		}
	}).state('hr.payrollReportDetails', {
		url : "/payrollReportDetails",
		templateUrl : '/app/hr/payrollReportDetails.html',
		controller : 'payrollReportDetailsCtr',
		params : {
			selectedMonth : null,
			toShow : '',
			payRollReportList : null
		}
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
		params : {
			selectedleadNo : null
		}
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
		url : "/viewContact/:selectedContactNo",
		templateUrl : '/app/crm/crm_contacts_view.html',
		controller : 'contactsList',
		params : {
			selectedContactNo : null
		}
	}).state('crm.crmSettings', {
		url : "/crmSettings",
		templateUrl : '/app/crm/crm_settings.html',
		controller : 'crmSettingsCtr',
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
		params : {
			selectedopportunityNo : null
		}
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
	}).state('setup.counterlist', {
		url : "/counterlist",
		templateUrl : '/app/setup/counterlist.html',
		controller : 'counterList'
	}).state('setup.disclaimer', {
		url : "/disclaimer",
		templateUrl : '/app/setup/disclaimer.html',
		controller : 'disclaimer',
	}).state('setup.departmentList', {
		url : "/departmentList",
		templateUrl : '/app/setup/setup_departmentList.html',
		controller : 'setup.departmentList',
	}).state('setup.settings', {
		url : "/bizsettings",
		templateUrl : '/app/setup/biz_settings.html',
		controller : 'bizSettingsCtr'
	}).state('setup.userauth', {
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
		controller : 'loginModuleCtr'
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
	}).state('user_prof_detail.viewLeaves', {
		url : "/MyMonthlyLeaves",
		templateUrl : '/app/profile/my_monthlyleaves.html',
		controller : 'viewMyLeaveAppsCtr',
		params : {
			selectedUser : null
		}
	}).state('user_prof_detail.addLeaves', {
		url : "/AddMonthlyLeaves",
		templateUrl : '/app/profile/add_leaveapp.html',
		controller : 'addViewEmployeeLeavesCtr',
		params : {
			selectedLeaveAppObj : null,
			selectedUser : null
		}
	}).state('user_prof_detail.employeeLeaves', {
		url : "/EmployeeLeaves",
		templateUrl : '/app/profile/employee_leaveapps.html',
		controller : 'addViewEmployeeLeavesCtr',
	}).state('user_prof_detail.editLeaves', {
		url : "/EditLeaves",
		templateUrl : '/app/profile/edit_employeeleaves.html',
		controller : 'editEmployeeLeaveAppsCtr',
		params : {
			selectedLeaveAppObj : null
		}
	}).state('user_prof_detail.leaveMaster', {
		url : "/LeaveMaster",
		templateUrl : '/app/profile/leavemaster.html',
		controller : 'leavemasterCtr',
	}).state('user_prof_detail.addLeaveMaster', {
		url : "/AddLeaveMaster",
		templateUrl : '/app/profile/add_leavemaster.html',
		controller : 'leavemasterCtr',
		params : {
			selectedLeaveMasterObj : null
		}
	}).state('user_prof_detail.print', {
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
	}).state('taskmanagement.taskSettings', {
		url : "/taskSettings",
		templateUrl : '/app/taskmanagement/task_settings.html',
		controller : 'taskModuleCtr',
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
		params : {
			selectedAccountType : null
		}
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