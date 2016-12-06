app = angular.module("stockApp");
app
		.controller(
				"invoiceViewCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, objectFactory, appEndpointSF) {			
					
					$scope.invoiceObj = {

						invoiceId : '',
						purchaseOrderNo : '',
						salesOrderId : '',
						customer : '',
						customerAddress : '',
						invoiceDate : $filter("date")(Date.now(), 'dd-MM-yyyy'),
						invoiceLineItemList : [],
						subTotal : '',
						taxCodeName : '',
						taxPercenatge : '',
						taxTotal : 0,
						finalTotal : ''
					};
					$scope.selected = [];

					$log.debug("$stateParams:", $stateParams);
					$log.debug("$stateParams.selectedInvoiceNo:",
							$stateParams.selectedInvoiceNo);

					$scope.selectedBillNo = $stateParams.selectedInvoiceNo;

					$scope.showBill = function() {
						var invoiceService = appEndpointSF.getInvoiceService();

						invoiceService
								.getinvoiceByID($scope.selectedBillNo)
								.then(
										function(invoiceList) {
											$scope.invoiceDetail = invoiceList;
											$scope.invoiceDetail.finalTotal = Math.round($scope.invoiceDetail.finalTotal);
											$scope.finalTotalInWord = NumToWord($scope.invoiceDetail.finalTotal);
											
											if($scope.invoiceDetail.invoiceLineItemList == undefined){
												
												$("#productDiv").hide();
											}
											else if($scope.invoiceDetail.serviceLineItemList == undefined){
												
												$("#serviceDiv").hide();
											}
										});

					}
					$scope.invoiceDetail = [];
					

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							if ($scope.selectedBillNo != undefined) {
								$scope.showBill();
							}
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();
					
					/* Setup menu */
					$scope.toggleRight = buildToggler('right');
					/**
					 * Build handler to open/close a SideNav; when animation
					 * finishes report completion in console
					 */
					function buildToggler(navID) {
						var debounceFn = $mdUtil.debounce(function() {
							$mdSidenav(navID).toggle().then(function() {
								$log.debug("toggle " + navID + " is done");
							});
						}, 200);
						return debounceFn;
					}

					$scope.close = function() {
						$mdSidenav('right').close().then(function() {
							$log.debug("close RIGHT is done");
						});
					};

					var printDivCSS = new String(
							'<link href="/lib/base/css/angular-material.min.css"" rel="stylesheet" type="text/css">'
									+ '<link href="/lib/base/css/bootstrap.min.css"" rel="stylesheet" type="text/css">')
					
					
					
					$scope.printDiv = function(invoiceObj){
						var InvoiceEntity = "InvoiceEntity";
						window.open("PrintPdfInvoice?InvoiceNo="+$scope.selectedBillNo+"&entityname="+InvoiceEntity+"&PurchaseOrderNo="+$scope.invoiceObj.purchaseOrderNo);
						
					}
					/*$scope.printDiv = function(divId) {
						// window.frames["print_frame"].document.body.innerHTML
						// = printDivCSS
						// + document.getElementById(divId).innerHTML;
						window.frames["print_frame"].document.body.innerHTML = document
								.getElementById(divId).innerHTML;
						window.frames["print_frame"].window.focus();
						window.frames["print_frame"].window.print();
					}*/
					
					$scope.back = function() {
						 window.history.back();
					}
				});
