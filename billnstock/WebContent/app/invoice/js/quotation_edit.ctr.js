app = angular.module("stockApp");
app
		.controller(
				"quotationEditCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $mdMedia, $mdDialog, $q,
						objectFactory, appEndpointSF) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					$log.debug("$scope.curUser++++++++"
							+ angular.toJson($scope.curUser));

					$scope.quotationObjEdit = {
						salesOrderId : null,
						customer : '',
						quotationDate : new Date(),
						quotationDueDate : '',
						invoiceLineItemList : [],
						productSubTotal : '',
						serviceSubTotal : '',
						serviceTotal : '',
						taxCodeName : '',
						taxPercenatge : '',
						productTaxTotal : 0,
						serviceTaxTotal : 0,
						finalTotal : '',
						noteToCustomer : '',
						account : "",
						createdDate : new Date(),
						modifiedDate : new Date(),
						modifiedBy : '',

						serviceName : '',
						discount : '',
						discValue : '',
						discAmount : '0',
						pOrder : '',
						serviceSubTotal : 0,
						serviceLineItemList : [],
						selectedServiceTax :'',
						business : ""
					};

					$log.debug("$stateParams:", $stateParams);
					$log.debug("$stateParams.selectedQuotationNo:",
							$stateParams.selectedQuotationNo);

					$scope.selectedBillNo = $stateParams.selectedQuotationNo;

					$scope.selectedBillNo = $stateParams.selectedQuotationNo;

					$scope.showQuotation = function() {
						var quotationService = appEndpointSF.getQuotationService();
						
						quotationService
								.getquotationByID($scope.selectedBillNo)
								.then(
										function(quotationList) {
											$scope.quotationObjEdit = quotationList;
											$scope.quotationObjEdit.quotationDueDate = new Date($scope.quotationObjEdit.quotationDueDate);
											$scope.quotationObjEdit.subTotal = $scope.quotationObjEdit.subTotal;

											var tempArray = $scope.quotationObjEdit.invoiceLineItemList;

											if (tempArray != undefined) {
												if ($scope.quotationObjEdit.invoiceLineItemList.length > 0) {
													for (var i = 0; i < $scope.quotationObjEdit.invoiceLineItemList.length; i++) {
														var a = parseInt($scope.quotationObjEdit.invoiceLineItemList[i].qty);
														$scope.quotationObjEdit.invoiceLineItemList[i].qty = a;
													}
												}
											}
											
//											$log.debug("$scope.invoiceObjEdit.selectedServiceTax:" + $scope.invoiceObjEdit.selectedServiceTax);
//											$scope.invoiceObjEdit.selectedServiceTax = $scope.getTaxObjByID($scope.invoiceObjEdit.selectedServiceTax.id);
//											$log.debug("$scope.invoiceObjEdit.selectedServiceTax:" + $scope.invoiceObjEdit.selectedServiceTax);
										});

					}
					
					$scope.getTaxObjByID = function(id) {						
						for(taxObj in $scope.taxforquotation){
							if(taxObj.id === id)
								return taxObj;
						}
						
						return null;
					}
					
					$scope.quotationObjEdit = [];

					$scope.updateQuotation = function() {
						if ($scope.quotationObjEdit.invoiceLineItemList.length == 0
								|| $scope.quotationObjEdit.invoiceLineItemList.itemName == "") {
							console.log("Please select atleast one item");
							$scope.errorMsg = "Please select atleast one item.";
						} else {
							var QuotationService = appEndpointSF
									.getQuotationService();
							$scope.quotationObjEdit.business = $scope.curUser.business;
							$scope.quotationObjEdit.modifiedBy = $scope.curUser.email_id;

							QuotationService.addQuotation($scope.quotationObjEdit).then(
									function(msgBean) {
									});
							$scope.showUpdateToast();
							$scope.quotationEdit.$setPristine();
							$scope.quotationEdit.$setValidity();
							$scope.quotationEdit.$setUntouched();
						
							$scope.quotationObjEdit = {};

						}
					}

					invoiceLineItemList = [];
					$scope.addItem = function() {
						var tempArray = $scope.quotationObjEdit.invoiceLineItemList;
						if (tempArray == undefined) {

							$scope.quotationObjEdit['invoiceLineItemList'] = invoiceLineItemList;

							var item = {
								srNo : $scope.quotationObjEdit.invoiceLineItemList.length + 1,
								itemName : "",
								qty : 1,
								price : "",
								subTotal : ""
							};
						} else {
							var item = {
								srNo : $scope.quotationObjEdit.invoiceLineItemList.length + 1,
								itemName : "",
								qty : 1,
								price : "",
								subTotal : ""
							}
						}
						;

						$scope.quotationObjEdit.invoiceLineItemList.push(item);
					};

					$scope.lineItemStockChange = function(index, stockItem) {
						$log.debug("##Came to lineItemStockChange...");
						var lineSelectedItem = $scope.quotationObjEdit.invoiceLineItemList[index];
						lineSelectedItem.price = stockItem.price;
						lineSelectedItem.itemName = stockItem.itemName;
						lineSelectedItem.subTotal = stockItem.subTotal;
						$scope.reCalculateQuotation();
					};

/*					$scope.calSubTotal = function() {
						$log.debug("##Came to calSubTotal...");
						$scope.invoiceObjEdit.subTotal = 0;

						for (var i = 0; i < $scope.invoiceObjEdit.invoiceLineItemList.length; i++) {
							var line = $scope.invoiceObjEdit.invoiceLineItemList[i];
							$scope.invoiceObjEdit.subTotal += (line.qty * line.price);

						}

						$scope.invoiceObjEdit.subTotal = parseFloat(
								Math.round(($scope.invoiceObjEdit.subTotal) * 100) / 100)
								.toFixed(2);

						$scope.calfinalTotal();

						return $scope.invoiceObjEdit.subTotal;
					}
*/
					
					$scope.calProductSubTotal = function() {
						$log.debug("##Came to calSubTotal...");
						$scope.quotationObjEdit.productSubTotal = 0;

						for (var i = 0; i < $scope.quotationObj.invoiceLineItemList.length; i++) {
							var line = $scope.quotationObjEdit.invoiceLineItemList[i];
							$scope.quotationObjEdit.productSubTotal += (line.qty * line.price);
							$scope.quotationObjEdit.productSubTotal = $scope.quotationObjEdit.productSubTotal;
						}

						$scope.quotationObjEdit.productSubTotal = parseFloat(
								Math.round(($scope.quotationObjEdit.productSubTotal) * 100) / 100)
								.toFixed(2);

						$scope.quotationObjEdit.productTotal = parseFloat($scope.quotationObjEdit.productSubTotal) + $scope.quotationObjEdit.productTaxTotal;
						
						
						return $scope.quotationObjEdit.productSubTotal;
					}
					
					$scope.calfinalTotal = function() {
						$log.debug("##Came to calSubTotal...");

						$scope.tempDiscAmount = 0;
						var finalTotal = 0;
						var disc = 0;
						if ($scope.lineSelectedDiscount == "Fixed") {

							$scope.tempDiscAmount = ($scope.quotationObjEdit.discAmount)
									.toFixed(2);
							

						} else {
							disc = parseInt($scope.quotationObjEdit.discAmount);
							finalTotal = parseFloat($scope.quotationObjEdit.subTotal)
									+ parseFloat($scope.quotationObjEdit.taxTotal)
									+ parseFloat($scope.quotationObjEdit.serviceSubTotal);

							$scope.tempDiscAmount = ((disc / 100) * finalTotal)
									.toFixed(2);
							;
							$scope.quotationObjEdit.discAmount = $scope.tempDiscAmount;

						}
						$scope.quotationObjEdit.finalTotal = (parseFloat($scope.quotationObjEdit.subTotal)
								+ parseFloat($scope.quotationObjEdit.taxTotal) + parseFloat($scope.quotationObjEdit.serviceSubTotal))
								- parseFloat($scope.tempDiscAmount).toFixed(2);
					}

					$scope.lineItemTaxChange = function(index, selectedTaxItem) {
						$log.debug("##Came to lineItemTaxChange...");

						var tempArray = $scope.quotationObjEdit.selectedTaxItem;
						if (tempArray != undefined) {
							$scope.quotationObjEdit.taxTotal = parseFloat(($scope.quotationObjEdit.selectedTaxItem.taxPercenatge / 100)
									* ($scope.quotationObjEdit.subTotal));													
						}
						
						$scope.quotationObjEdit['selectedTaxItem'] = selectedTaxItem;
						
						$scope.quotationObjEdit.taxTotal = parseFloat(($scope.quotationObjEdit.selectedTaxItem.taxPercenatge / 100)
								* ($scope.quotationObjEdit.subTotal));
						$scope.reCalculateQuotation();
					};

					$scope.removeItem = function(index) {
						$scope.quotationObjEdit.invoiceLineItemList.splice(index, 1);
						$scope.reCalculateQuotation();
					};

					/*
					 * ====================================Services
					 * Provided===================================
					 */
					$scope.addService = function() {

						var tempArray = $scope.quotationObjEdit.serviceLineItemList;
						if (tempArray == undefined) {

							$scope.quotationObjEdit['serviceLineItemList'] = serviceLineItemList;

							var service = {
								srNo : $scope.quotationObjEdit.serviceLineItemList.length + 1,
								serviceName : "",
								sQty : 1,
								sPrice : "",
								serviceSubTotal : 0
							};
						} else {
							var service = {
								srNo : $scope.quotationObjEdit.serviceLineItemList.length + 1,
								serviceName : "",
								sQty : 1,
								sPrice : "",
								serviceSubTotal : 0
							};
						}
						$scope.quotationObjEdit.serviceLineItemList
								.push(service);
					};

					$scope.removeService = function(index) {
						$scope.quotationObjEdit.serviceLineItemList.splice(
								index, 1);
						$scope.reCalculateQuotation();
						
					};
					
					$scope.reCalculateQuotation = function() {
						$scope.calServiceSubTotal();
						$scope.calSubTotal();
						$scope.calfinalTotal();						
					};

					$scope.calServiceSubTotal = function() {
						$log.debug("##Came to calSubTotal...");
						$scope.quotationObjEdit.serviceSubTotal = 0;

						for (var i = 0; i < $scope.quotationObjEdit.serviceLineItemList.length; i++) {
							var line = $scope.quotationObjEdit.serviceLineItemList[i];
							$scope.quotationObjEdit.serviceSubTotal += (line.sQty * line.sPrice);

							$log.debug("subTotal :"
									+ $scope.quotationObjEdit.serviceSubTotal);
						}

						$scope.quotationObjEdit.subTotal = parseFloat(
								Math.round(($scope.quotationObjEdit.subTotal) * 100) / 100)
								.toFixed(2);

						
						return $scope.quotationObjEdit.subTotal;
					}

					$scope.discountType = [ "%", "Fixed" ];
					$scope.lineItemDiscountChange = function(index,
							selectedDiscount) {
						$log.debug("##Came to lineItemStockChange...");
						$scope.lineSelectedDiscount = selectedDiscount;

						// $scope.calSubTotal();
						// $scope.calfinalTotal();
					};

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

					$scope.showSimpleToastError = function() {
						$mdToast.show($mdToast.simple().content(
								'Stock not sufficient!').position("right")
								.hideDelay(10000));
					};

					$scope.getAllStock = function() {
						$log.debug("Inside Ctr $scope.getAllStock");
						var stockService = appEndpointSF.getStockService();

						stockService.getAllStock($scope.curUser.business.id)
								.then(function(stockList) {
									$log.debug("Inside Ctr getAllStock");
									$scope.stockforquotation = stockList;
								});
					}

					$scope.checkStock = function(item, $event) {
						for (var i = 0; i <= $scope.stockforquotation.length; i++) {
							if ($scope.stockforquotation[i].itemName == item.itemName) {
								$scope.qtyErrorMsg = "";
								if ($scope.stockforquotation[i].qty < item.qty) {
									$scope.qtyErrorMsg = "Quantity entered is not available in stock";
									// $scope.showSimpleToastError();
									$scope.dialogBox();
								}
							}
						}
					}

					$scope.dialogBox = function(ev) {
						$mdDialog
								.show($mdDialog
										.alert()
										.targetEvent(ev)
										.clickOutsideToClose(true)
										.parent('body')
										.title('Error')
										.textContent(
												'Quantity entered is not available in stock!')
										.ok('OK'));
						ev = null;
					};

					$scope.getTaxesByVisibility = function() {
						$log.debug("Inside Ctr $scope.getAllTaxes");
						var taxService = appEndpointSF.getTaxService();

						taxService.getTaxesByVisibility(
								$scope.curUser.business.id).then(
								function(taxList) {									
									$scope.taxforquotation = taxList;
								});
					}
					$scope.taxforquotation = [];

					$scope.getAllSalesOrder = function() {
						$log.debug("Inside Ctr $scope.getAllSalesOrder");
						var salesService = appEndpointSF.getSalesOrderService();

						salesService
								.getAllSalesOrder($scope.curUser.business.id)
								.then(
										function(salesOrderList) {
											$log
													.debug("Inside Ctr getAllSalesOrder");
											$scope.SOforquotation = salesOrderList;
											$log
													.debug("@@@@@@@getAllSalesOrder"
															+ angular
																	.toJson($scope.SOforquotation));

										});
					}

					$scope.SOforquotation = [];

					$scope.getAllAccountsByBusiness = function() {
						var accountService = appEndpointSF.getAccountService();

						accountService
								.getAllAccountsByBusiness(
										$scope.curUser.business.id)
								.then(
										function(accountList) {
											$log
													.debug("Inside Ctr getAllAccountsByBusiness");
											$scope.accountforquotation = accountList;
											$log
													.debug("Inside Ctr $scope.accounts:"
															+ angular
																	.toJson($scope.account));
										});
					}
					$scope.accountforquotation = [];

					var printDivCSS = new String(
							'<link href="/lib/base/css/angular-material.min.css"" rel="stylesheet" type="text/css">'
									+ '<link href="/lib/base/css/bootstrap.min.css"" rel="stylesheet" type="text/css">')
					$scope.printDiv = function(divId) {
						// window.frames["print_frame"].document.body.innerHTML
						// = printDivCSS
						// + document.getElementById(divId).innerHTML;
						window.frames["print_frame"].document.body.innerHTML = document
								.getElementById(divId).innerHTML;
						window.frames["print_frame"].window.focus();
						window.frames["print_frame"].window.print();
					}

					// FOR CUSTOMER

					$scope.quotationObjEdit.customer = null;
					$scope.searchTextInput = null;

					$scope.querySearch = function(query) {
						var results = query ? $scope.customersforquotation
								.filter(createFilterFor(query))
								: $scope.customersforquotation;
						var deferred = $q.defer();
						$timeout(function() {
							deferred.resolve(results);
						}, Math.random() * 1000, false);
						return deferred.promise;
					}
					/**
					 * Build `states` list of key/value pairs
					 */
					function loadAllCustomers() {

						var customerService = appEndpointSF
								.getCustomerService();
						customerService
								.getAllCustomersByBusiness(
										$scope.curUser.business.id)
								.then(
										function(custList) {
											$scope.customersforquotation = custList.items;
										});
						$scope.customersforquotation = [];
					}
					/**
					 * Create filter function for a query string
					 */
					function createFilterFor(query) {
						var lowercaseQuery = angular.lowercase(query);
						return function filterFn(cus) {
							return (angular.lowercase(cus.firstName).indexOf(
									lowercaseQuery) === 0);
						};
					}

					// FOR STOCK

					/*
					 * // $scope.invoiceObjEdit.customer = null;
					 * $scope.searchTextInput = null;
					 * 
					 * $scope.querySearch = function(query) { var results =
					 * query ? $scope.stockforinvoice
					 * .filter(createFilterFor(query)) : $scope.stockforinvoice;
					 * var deferred = $q.defer(); $timeout(function() {
					 * deferred.resolve(results); }, Math.random() * 1000,
					 * false); return deferred.promise; }
					 *//**
						 * Build `states` list of key/value pairs
						 */
					/*
					 * function loadAllStock() {
					 * 
					 * var stockService = appEndpointSF.getStockService();
					 * 
					 * stockService.getAllStock($scope.curUser.business.id)
					 * .then(function(stockList) {
					 * 
					 * $scope.stockforinvoice = stockList; });
					 * $scope.stockforinvoice = []; }
					 *//**
						 * Create filter function for a query string
						 */
					/*
					 * function createFilterFor(query) { var lowercaseQuery =
					 * angular.lowercase(query); return function filterFn(stk) {
					 * return (angular.lowercase(stk.itemName).indexOf(
					 * lowercaseQuery) === 0); }; }
					 * 
					 */
					/*
					 * $scope.getInvoiceSettingsByBiz = function() {
					 * 
					 * var invoiceService = appEndpointSF.getInvoiceService();
					 * 
					 * invoiceService.getInvoiceSettingsByBiz(
					 * $scope.curUser.business.id) .then( function(settingsList) {
					 * 
					 * $scope.invoiceObjEdit = settingsList; $log .debug("Inside
					 * Ctr $scope.settingsList:" + $scope.invoiceObjEdit); //
					 * return $scope.settingsObj; }); }
					 */
					
					$scope.serviceTaxChange = function(index, selectedServiceTax,
							$event) {
						$log.debug("##Came to lineItemTaxChange...");

						$scope.quotationObjEdit.serviceTaxTotal = parseFloat(($scope.quotationObjEdit.selectedServiceTax.taxPercenatge / 100)
								* ($scope.quotationObjEdit.serviceSubTotal));

						$scope.quotationObjEdit.serviceTotal = $scope.quotationObjEdit.serviceSubTotal + $scope.quotationObjEdit.serviceTaxTotal;
						$scope.calfinalTotal();
					};
					
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							loadAllCustomers();
							// loadAllStock();
							$scope.getAllStock();
							$scope.getAllSalesOrder();
							$scope.getTaxesByVisibility();
							$scope.getAllAccountsByBusiness();
							// $scope.getInvoiceSettingsByBiz();
							$scope.showQuotation();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					$scope.addCustomer = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show({
									controller : DialogController,
									templateUrl : '/app/crm/customer_add.html',
									parent : angular.element(document.body),
									targetEvent : ev,
									clickOutsideToClose : true,
									fullscreen : useFullScreen,
									locals : {
										curUser : $scope.curUser,
										customer : $scope.customer
									}
								})
								.then(
										function(answer) {
											$scope.status = 'You said the information was "'
													+ answer + '".';
										},
										function() {
											$scope.status = 'You cancelled the dialog.';
										});

					};

					function DialogController($scope, $mdDialog, curUser,
							customer) {

						$scope.addCustomer = function() {
							$scope.customer.business = curUser.business;
							$scope.customer.createdDate = new Date();
							$scope.customer.modifiedBy = curUser.email_id;

							var customerService = appEndpointSF
									.getCustomerService();

							customerService.addCustomer($scope.customer).then(
									function(msgBean) {

									});
							$scope.hide();
						}

						$scope.hide = function() {
							$mdDialog.hide();
						};
					}

				});
