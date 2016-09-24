app = angular.module("stockApp");
app
		.controller(
				"invoiceAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $mdMedia, $mdDialog, $q,
						objectFactory, appEndpointSF) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					$log.debug("$scope.curUser++++++++"
							+ angular.toJson($scope.curUser));

					$scope.getEmptyInvoiceObj = function() {
						return {
							salesOrderId : null,
							customer : '',
							// invoiceDate : $filter("date")(Date.now(),
							// 'dd-MM-yyyy'),
							invoiceDate : new Date(),
							invoiceDueDate : new Date(),
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
							account : null,
							createdDate : new Date(),
							modifiedDate : new Date(),
							modifiedBy : '',
							serviceName : '',
							discount : '',
							discValue : '',
							discAmount : '0',
							pOrder : null,
							serviceSubTotal : 0,
							serviceLineItemList : [],
							selectedServiceTax : null,
							business : null
						};
					}

					$scope.invoiceObj = $scope.getEmptyInvoiceObj();

					$scope.addInvoice = function() {
						if ($scope.invoiceObj.invoiceLineItemList.length == 0
								&& $scope.invoiceObj.serviceLineItemList.length == 0) {
							console.log("Please select atleast one item");
							$scope.errorMsg = "Please select atleast one item.";
						} else {
							var InvoiceService = appEndpointSF
									.getInvoiceService();
							$scope.invoiceObj.business = $scope.curUser.business;
							$scope.invoiceObj.modifiedBy = $scope.curUser.email_id;
							$scope.invoiceObj.discValue = $scope.discAmount;
							InvoiceService.addInvoice($scope.invoiceObj).then(
									function(msgBean) {										
										$scope.showAddToast();
										$scope.invoiceAdd.$setPristine();
										$scope.invoiceAdd.$setValidity();
										$scope.invoiceAdd.$setUntouched();

										$scope.invoiceObj = $scope.getEmptyInvoiceObj();
									});

						}
					}

					$scope.addItem = function() {
						var item = {
							srNo : $scope.invoiceObj.invoiceLineItemList.length + 1,
							itemName : "",
							qty : 1,
							price : "",
							productSubTotal : ""
						};

						$scope.invoiceObj.invoiceLineItemList.push(item);
					};

					$scope.lineItemStockChange = function(index, stockItem) {
						$log.debug("##Came to lineItemStockChange...");
						var lineSelectedItem = $scope.invoiceObj.invoiceLineItemList[index];
						lineSelectedItem.price = stockItem.price;
						lineSelectedItem.itemName = stockItem.itemName;
						lineSelectedItem.productSubTotal = stockItem.productSubTotal;

						$scope.calProductSubTotal();
						// $scope.calSubTotal();
						$scope.calfinalTotal();
					};

					$scope.calProductSubTotal = function() {
						$log.debug("##Came to calSubTotal...");
						$scope.invoiceObj.productSubTotal = 0;

						for (var i = 0; i < $scope.invoiceObj.invoiceLineItemList.length; i++) {
							var line = $scope.invoiceObj.invoiceLineItemList[i];
							$scope.invoiceObj.productSubTotal += (line.qty * line.price);
							$scope.invoiceObj.productSubTotal = $scope.invoiceObj.productSubTotal;
						}

						$scope.invoiceObj.productSubTotal = parseFloat(
								Math
										.round(($scope.invoiceObj.productSubTotal) * 100) / 100)
								.toFixed(2);

						$scope.invoiceObj.productTotal = parseFloat($scope.invoiceObj.productSubTotal)
								+ $scope.invoiceObj.productTaxTotal;

						$scope.calfinalTotal();

						return $scope.invoiceObj.productSubTotal;
					}

					$scope.calfinalTotal = function() {
						$log.debug("##Came to calSubTotal...");

						$scope.tempDiscAmount = 0;
						var finalTotal = 0;
						var disc = 0;
						$scope.invoiceObj.finalTotal = 0;

						if ($scope.lineSelectedDiscount != undefined) {
							if ($scope.lineSelectedDiscount == "Fixed") {

								$scope.tempDiscAmount = $scope.discAmount;
								$scope.invoiceObj.discAmount = parseFloat(
										($scope.tempDiscAmount)).toFixed(2);

								if ($scope.invoiceObj.productTotal == undefined) {
									$scope.invoiceObj.finalTotal = (parseFloat($scope.invoiceObj.serviceTotal))
											- parseFloat($scope.discAmount)
													.toFixed(2);
								} else {
									$scope.invoiceObj.finalTotal = (parseFloat($scope.invoiceObj.productTotal) + parseFloat($scope.invoiceObj.serviceTotal))
											- parseFloat($scope.discAmount)
													.toFixed(2);
								}

							} else {
								disc = parseInt($scope.discAmount);
								finalTotal = parseFloat($scope.invoiceObj.productSubTotal)
										+ parseFloat($scope.invoiceObj.productTaxTotal)
										+ parseFloat($scope.invoiceObj.serviceSubTotal);

								$scope.tempDiscAmount = ((disc / 100) * finalTotal)
										.toFixed(2);

								$scope.invoiceObj.discAmount = $scope.tempDiscAmount;

							}
							if ($scope.invoiceObj.productTotal == undefined) {
								$scope.invoiceObj.finalTotal = (parseFloat($scope.invoiceObj.serviceTotal))
										- parseFloat($scope.tempDiscAmount)
												.toFixed(2);
							} else {
								$scope.invoiceObj.finalTotal = (parseFloat($scope.invoiceObj.productTotal) + parseFloat($scope.invoiceObj.serviceTotal))
										- parseFloat($scope.tempDiscAmount)
												.toFixed(2);
							}
						} else {
							var a = parseInt($scope.invoiceObj.productSubTotal);
							var b = $scope.invoiceObj.productTaxTotal;
							var c = parseFloat($scope.invoiceObj.serviceTotal);

							/*
							 * $scope.invoiceObj.finalTotal = a + b + c;
							 * $scope.invoiceObj.finalTotal =
							 * (parseFloat($scope.invoiceObj.productSubTotal) +
							 * $scope.invoiceObj.productTaxTotal +
							 * parseFloat($scope.invoiceObj.serviceSubTotal))
							 * .toFixed(2);
							 * 
							 * 
							 */
							if ($scope.invoiceObj.serviceLineItemList.length == 0) {
								$scope.invoiceObj.serviceTotal = 0;
								$scope.serviceTaxChange = "";
							}

							$scope.invoiceObj.finalTotal = a + b + c;

							/*
							 * $scope.invoiceObj.finalTotal =
							 * parseFloat($scope.invoiceObj.serviceSubTotal) +
							 * parseInt($scope.invoiceObj.productSubTotal) -
							 * parseFloat($scope.tempDiscAmount);
							 */}
					}
					$scope.lineItemTaxChange = function(index, selectedTaxItem,
							$event) {
						$log.debug("##Came to lineItemTaxChange...");

						$scope.invoiceObj.productTaxTotal = parseFloat(($scope.invoiceObj.selectedTaxItem.taxPercenatge / 100)
								* ($scope.invoiceObj.productSubTotal));

						$scope.calfinalTotal();
					};

					$scope.removeItem = function(index) {
						$scope.invoiceObj.invoiceLineItemList.splice(index, 1);
						$scope.calProductSubTotal();
						// $scope.calSubTotal();
						$scope.calfinalTotal();
					};

					/*
					 * ====================================Services
					 * Provided===================================
					 */
					$scope.addService = function() {
						var service = {
							srNo : $scope.invoiceObj.serviceLineItemList.length + 1,
							serviceName : "",
							sQty : 1,
							sPrice : "",
							serviceSubTotal : 0
						};

						$scope.invoiceObj.serviceLineItemList.push(service);
					};

					$scope.removeService = function(index) {
						$scope.invoiceObj.serviceLineItemList.splice(index, 1);

						if ($scope.invoiceObj.serviceLineItemList.length == 0) {
							$scope.invoiceObj.serviceTotal = 0;
							$scope.serviceTaxChange = "";
						}

						$scope.calServiceSubTotal();
						// $scope.serviceTaxChange();
						$scope.calfinalTotal();

					};

					$scope.serviceTaxChange = function(index,
							selectedServiceTax, $event) {
						$log.debug("##Came to lineItemTaxChange...");

						$scope.invoiceObj.serviceTaxTotal = parseFloat(($scope.invoiceObj.selectedServiceTax.taxPercenatge / 100)
								* ($scope.invoiceObj.serviceSubTotal));

						$scope.invoiceObj.serviceTotal = $scope.invoiceObj.serviceSubTotal
								+ $scope.invoiceObj.serviceTaxTotal;
						$scope.calfinalTotal();
					};

					$scope.calServiceSubTotal = function() {
						$log.debug("##Came to calSubTotal...");
						$scope.invoiceObj.serviceSubTotal = 0;

						for (var i = 0; i < $scope.invoiceObj.serviceLineItemList.length; i++) {
							var line = $scope.invoiceObj.serviceLineItemList[i];
							$scope.invoiceObj.serviceSubTotal += (line.sQty * line.sPrice);
							$scope.invoiceObj.serviceTotal = $scope.invoiceObj.serviceSubTotal;
						}

						$scope.invoiceObj.productSubTotal = parseFloat(
								Math
										.round(($scope.invoiceObj.productSubTotal) * 100) / 100)
								.toFixed(2);

						$scope.calfinalTotal();

						return $scope.invoiceObj.productSubTotal;
					}

					$scope.discountType = [ "%", "Fixed" ];
					$scope.lineItemDiscountChange = function(index,
							selectedDiscount) {
						$log.debug("##Came to lineItemStockChange...");
						$scope.lineSelectedDiscount = selectedDiscount;
						$scope.invoiceObj.discount = selectedDiscount;
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

					$scope.showSimpleToast = function() {
						$mdToast.show($mdToast.simple().content(
								'Customer Data Saved!').position("top")
								.hideDelay(3000));
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
									$scope.stockforinvoice = stockList;
								});
					}

					$scope.checkStock = function(item, $event) {
						for (var i = 0; i <= $scope.stockforinvoice.length; i++) {
							if ($scope.stockforinvoice[i].itemName == item.itemName) {
								$scope.qtyErrorMsg = "";
								if ($scope.stockforinvoice[i].qty < item.qty) {
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
									$log.debug("Inside Ctr getAllTaxes");
									$scope.taxforinvoice = taxList;
								});
					}
					$scope.taxData = [];

					$scope.getAllSalesOrder = function() {
						$log.debug("Inside Ctr $scope.getAllSalesOrder");
						var salesService = appEndpointSF.getSalesOrderService();

						salesService
								.getAllSalesOrder($scope.curUser.business.id)
								.then(
										function(salesOrderList) {
											$log
													.debug("Inside Ctr getAllSalesOrder");
											$scope.SOforinvoice = salesOrderList;
											$log
													.debug("@@@@@@@getAllSalesOrder"
															+ angular
																	.toJson($scope.SOforinvoice));

										});
					}

					$scope.SOforinvoice = [];

					$scope.getAllAccountsByBusiness = function() {
						var accountService = appEndpointSF.getAccountService();

						accountService
								.getAllAccountsByBusiness(
										$scope.curUser.business.id)
								.then(
										function(accountList) {
											$log
													.debug("Inside Ctr getAllAccountsByBusiness");
											$scope.accountforinvoice = accountList;
											$log
													.debug("Inside Ctr $scope.accounts:"
															+ angular
																	.toJson($scope.account));
										});
					}
					$scope.accountforinvoice = [];

					$scope.getInvoiceSettingsByBiz = function() {

						var invoiceService = appEndpointSF.getInvoiceService();

						invoiceService
								.getInvoiceSettingsByBiz(
										$scope.curUser.business.id)
								.then(
										function(settingsList) {

											$scope.settingsObj = settingsList;
											$scope.invoiceObj.noteToCustomer = $scope.settingsObj.noteToCustomer;
											$log
													.debug("Inside Ctr $scope.invoiceObj.noteToCustomer:"
															+ $scope.invoiceObj.noteToCustomer);
											return $scope.settingsObj;
										});
					}

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
					/*
					 * var self = this; var pendingSearch, cancelSearch =
					 * angular.noop; var cachedQuery, lastSearch;
					 * self.allContacts = loadContacts(); self.contacts =
					 * [self.allContacts[0]]; self.asyncContacts = [];
					 * self.filterSelected = true; self.querySearch =
					 * querySearch; self.delayedQuerySearch =
					 * delayedQuerySearch;
					 *//**
						 * Search for contacts; use a random delay to simulate a
						 * remote call
						 */
					/*
					 * function querySearch (criteria) { cachedQuery =
					 * cachedQuery || criteria; return cachedQuery ?
					 * self.allContacts.filter(createFilterFor(cachedQuery)) :
					 * []; }
					 *//**
						 * Async search for contacts Also debounce the queries;
						 * since the md-contact-chips does not support this
						 */
					/*
					 * function delayedQuerySearch(criteria) { cachedQuery =
					 * criteria; if ( !pendingSearch || !debounceSearch() ) {
					 * cancelSearch(); return pendingSearch =
					 * $q(function(resolve, reject) { // Simulate async
					 * search... (after debouncing) cancelSearch = reject;
					 * $timeout(function() { resolve( self.querySearch() );
					 * refreshDebounce(); }, Math.random() * 500, true) }); }
					 * return pendingSearch; } function refreshDebounce() {
					 * lastSearch = 0; pendingSearch = null; cancelSearch =
					 * angular.noop; }
					 *//**
						 * Debounce if querying faster than 300ms
						 */
					/*
					 * function debounceSearch() { var now = new
					 * Date().getMilliseconds(); lastSearch = lastSearch || now;
					 * return ((now - lastSearch) < 300); }
					 *//**
						 * Create filter function for a query string
						 */
					/*
					 * function createFilterFor(query) { var lowercaseQuery =
					 * angular.lowercase(query); return function
					 * filterFn(contact) { return
					 * (contact._lowername.indexOf(lowercaseQuery) != -1);; }; }
					 * function loadContacts() { var contacts = [ 'Marina
					 * Augustine', 'Oddr Sarno', 'Nick Giannopoulos', 'Narayana
					 * Garner', 'Anita Gros', 'Megan Smith', 'Tsvetko Metzger',
					 * 'Hector Simek', 'Some-guy withalongalastaname' ]; return
					 * contacts.map(function (c, index) { var cParts = c.split('
					 * '); var contact = { name: c, email:
					 * cParts[0][0].toLowerCase() + '.' +
					 * cParts[1].toLowerCase() + '@example.com', image:
					 * 'http://lorempixel.com/50/50/people?' + index };
					 * contact._lowername = contact.name.toLowerCase(); return
					 * contact; }); }
					 * 
					 * 
					 */

					$scope.invoiceObj.customer = null;
					// $scope.searchTextInput = null;

					$scope.querySearch = function(query) {
						var results = query ? $scope.customersforinvoice
								.filter(createFilterFor(query))
								: $scope.customersforinvoice;
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
											$scope.customersforinvoice = custList.items;
										});

					}
					/**
					 * Create filter function for a query string
					 */
					function createFilterFor(query) {
						var lowercaseQuery = angular.lowercase(query);
						return function filterFn(cus) {
							var a = cus.isCompany ? cus.companyName
									: (cus.firstName + "" + cus.lastName);
							return (angular.lowercase(a)
									.indexOf(lowercaseQuery) === 0);
						};
					}

					$scope.getAllWarehouseByBusiness = function() {
						$log
								.debug("Inside function $scope.getAllWarehouseByBusiness");
						var warehouseService = appEndpointSF
								.getWarehouseManagementService();

						warehouseService
								.getAllWarehouseByBusiness(
										$scope.curUser.business.id)
								.then(
										function(warehouseList) {
											$scope.warehouses = warehouseList;
											$log
													.debug("$scope.warehouses:"
															+ angular
																	.toJson($scope.warehouses));
										});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							loadAllCustomers();
							// loadContacts();
							$scope.getAllStock();
							$scope.getAllSalesOrder();
							$scope.getTaxesByVisibility();
							$scope.getAllAccountsByBusiness();
							$scope.getInvoiceSettingsByBiz();
							$scope.getAllWarehouseByBusiness();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					// For Add Customer from Invoice Page through popup
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

					// For Add Stock from Invoice Page through popup
					/*
					 * $scope.addStock = function(ev) { var useFullScreen =
					 * ($mdMedia('sm') || $mdMedia('xs')) &&
					 * $scope.customFullscreen; $mdDialog .show({ controller :
					 * DialogController, templateUrl :
					 * '/app/stock/stockItem_add.html', parent :
					 * angular.element(document.body), targetEvent : ev,
					 * clickOutsideToClose : true, fullscreen : useFullScreen,
					 * locals : { curUser : $scope.curUser, stock :
					 * $scope.stock, warehouses : $scope.warehouses } }) .then(
					 * function(answer) { $scope.status = 'You said the
					 * information was "' + answer + '".'; }, function() {
					 * $scope.status = 'You cancelled the dialog.'; }); };
					 * 
					 * function DialogController($scope, $mdDialog, curUser,
					 * stock, warehouses) {
					 * 
					 * $scope.addStock = function() { $scope.stock.business =
					 * curUser.business; $scope.stock.createdDate = new Date();
					 * $scope.stock.modifiedBy = curUser.email_id;
					 * 
					 * var customerService = appEndpointSF
					 * .getCustomerService();
					 * 
					 * customerService.addStock($scope.stock).then(
					 * function(msgBean) {
					 * 
					 * }); $scope.hide(); }
					 * 
					 * $scope.hide = function() { $mdDialog.hide(); }; }
					 */

				});
