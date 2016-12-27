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

					$scope.getEmptyInvoiceObj = function() {
						return {
							customer : null,
							invoiceDueDate : new Date(),
							noteToCustomer : '',
							createdDate : new Date(),
							modifiedDate : new Date(),
							modifiedBy : '',
							discountType : '',
							discountPercent : 0,
							discAmount : 0,
							pOrder : null,
							serviceLineItemList : [],
							productLineItemList : [],
							selectedServiceTax : null,
							selectedProductTax : null,
							finalTotal : 0,
							isPaid : false,
							isDraft : false,
							paidDate : null,
							business : null
						};
					}

					$scope.invoiceObj = $stateParams.invoiceObj ? $stateParams.invoiceObj
							: $scope.getEmptyInvoiceObj();

					$scope.addInvoice = function() {
						$scope.loading = true;
						if (!$scope.invoiceObj.serviceLineItemList
								&& !$scope.invoiceObj.productLineItemList) {

							$scope.errorMsg = "Please select atleast one item.";
						} else {
							var InvoiceService = appEndpointSF
									.getInvoiceService();
							$scope.invoiceObj.business = $scope.curUser.business;
							$scope.invoiceObj.modifiedBy = $scope.curUser.email_id;
							$scope.invoiceObj.discAmount = $scope.discAmount;
							InvoiceService.addInvoice($scope.invoiceObj).then(
									function(msgBean) {
										if ($scope.invoiceObj.id) {
											// for edit
											$scope.showUpdateToast();
										} else {
											// for new add
											$scope.showAddToast();
											$scope.invoiceObj = $scope
													.getEmptyInvoiceObj();
										}
										$scope.loading = false;

										$scope.invoiceAdd.$setPristine();
										$scope.invoiceAdd.$setValidity();
										$scope.invoiceAdd.$setUntouched();
									});

						}
					}

					$scope.addServiceLineItem = function() {
						var item = {
							isProduct : false,
							itemName : "",
							qty : 1,
							price : "",
							stockItem : null,
							selectedTaxItem : null
						};
						if (!$scope.invoiceObj.serviceLineItemList) {
							$scope.invoiceObj.serviceLineItemList = [];
						}
						$scope.invoiceObj.serviceLineItemList.push(item);
					};

					$scope.addProductLineItem = function() {
						var item = {
							isProduct : true,
							itemName : "",
							qty : 1,
							price : "",
							stockItem : null,
							selectedTaxItem : null
						};
						if (!$scope.invoiceObj.productLineItemList) {
							$scope.invoiceObj.productLineItemList = [];
						}
						$scope.invoiceObj.productLineItemList.push(item);
					};

					$scope.removeServices = function(toAddRemove) {
						if (toAddRemove) {
							$scope.settingsObj.showDefaultServiceItems = true;
							$scope.addServiceLineItem();
						} else {
							$scope.settingsObj.showDefaultServiceItems = false;
							$scope.invoiceObj.serviceLineItemList = [];
						}
					};
					$scope.removeProducts = function(toAddRemove) {
						if (toAddRemove) {
							$scope.settingsObj.showDefaultProductItems = true;
							$scope.addProductLineItem();
						} else {
							$scope.settingsObj.showDefaultProductItems = false;
							$scope.invoiceObj.productLineItemList = [];
						}
					};

					$scope.productLineItemChanged = function(selectedLineItem) {
						selectedLineItem.price = selectedLineItem.stockItem.price;
						$scope.calProductSubTotal();
					};

					$scope.calProductSubTotal = function() {
						$scope.invoiceObj.productSubTotal = 0;
						if ($scope.invoiceObj.productLineItemList) {
							for (var i = 0; i < $scope.invoiceObj.productLineItemList.length; i++) {
								var lineItem = $scope.invoiceObj.productLineItemList[i];
								$scope.invoiceObj.productSubTotal += (lineItem.qty * lineItem.price);
							}

							$scope.productTaxChanged();
						}
					}

					$scope.serviceLineItemChanged = function(selectedLineItem) {
						selectedLineItem.price = selectedLineItem.stockItem.price;
						$scope.calServiceSubTotal();
					};

					$scope.calServiceSubTotal = function() {
						$scope.invoiceObj.serviceSubTotal = 0;
						if ($scope.invoiceObj.serviceLineItemList) {
							for (var i = 0; i < $scope.invoiceObj.serviceLineItemList.length; i++) {
								var lineItem = $scope.invoiceObj.serviceLineItemList[i];
								$scope.invoiceObj.serviceSubTotal += (lineItem.qty * lineItem.price);
							}

							$scope.serviceTaxChanged();
						}
					}

					$scope.serviceTaxChanged = function() {
						if (!$scope.invoiceObj.selectedServiceTax) {
							$scope.invoiceObj.serviceTaxTotal = 0;
						} else {
							$scope.invoiceObj.serviceTaxTotal = parseFloat(($scope.invoiceObj.selectedServiceTax.taxPercenatge / 100)
									* ($scope.invoiceObj.serviceSubTotal));
						}
						$scope.invoiceObj.serviceTotal = $scope.invoiceObj.serviceSubTotal
								+ $scope.invoiceObj.serviceTaxTotal;
						$scope.calfinalTotal();
					};

					$scope.productTaxChanged = function() {
						if (!$scope.invoiceObj.selectedProductTax) {
							$scope.invoiceObj.productTaxTotal = 0;

						} else {
							$scope.invoiceObj.productTaxTotal = parseFloat(($scope.invoiceObj.selectedProductTax.taxPercenatge / 100)
									* ($scope.invoiceObj.productSubTotal));
						}
						$scope.invoiceObj.productTotal = $scope.invoiceObj.productSubTotal
								+ $scope.invoiceObj.productTaxTotal;
						$scope.calfinalTotal();
					}

					$scope.calfinalTotal = function() {
						$scope.tempDiscAmount = 0;
						var finalTotal = 0;
						var disc = 0;
						$scope.invoiceObj.finalTotal = 0;

						if ($scope.invoiceObj.serviceTotal)
							finalTotal += parseFloat($scope.invoiceObj.serviceTotal);

						if ($scope.invoiceObj.productTotal)
							finalTotal += parseFloat($scope.invoiceObj.productTotal);

						$scope.invoiceObj.finalTotal = finalTotal;

						return;

						if ($scope.lineSelectedDiscount != undefined) {
							if ($scope.lineSelectedDiscount == "Fixed") {

								$scope.tempDiscAmount = $scope.discAmount;
								$scope.invoiceObj.discAmount = $scope.tempDiscAmount;

								if ($scope.invoiceObj.productTotal == undefined) {
									$scope.invoiceObj.finalTotal = parseFloat($scope.invoiceObj.serviceTotal)
											- parseFloat($scope.discAmount);
								} else {
									$scope.invoiceObj.finalTotal = (parseFloat($scope.invoiceObj.productTotal) + parseFloat($scope.invoiceObj.serviceTotal))
											- parseFloat($scope.discAmount);
								}

							} else {
								disc = parseInt($scope.discAmount);
								finalTotal = parseFloat($scope.invoiceObj.productSubTotal)
										+ parseFloat($scope.invoiceObj.productTaxTotal)
										+ parseFloat($scope.invoiceObj.serviceSubTotal);

								$scope.tempDiscAmount = ((disc / 100) * finalTotal);

								$scope.invoiceObj.discAmount = $scope.tempDiscAmount;

							}
							if ($scope.invoiceObj.productTotal == undefined) {
								$scope.invoiceObj.finalTotal = (parseFloat($scope.invoiceObj.serviceTotal))
										- parseFloat($scope.tempDiscAmount);
							} else {
								$scope.invoiceObj.finalTotal = (parseFloat($scope.invoiceObj.productTotal) + parseFloat($scope.invoiceObj.serviceTotal))
										- parseFloat($scope.tempDiscAmount);
							}
						}
					}

					$scope.removeServiceItem = function(index) {
						$scope.invoiceObj.serviceLineItemList.splice(index, 1);

						if ($scope.invoiceObj.serviceLineItemList.length == 0) {
							$scope.invoiceObj.serviceSubTotal = 0;
							$scope.invoiceObj.serviceTotal = 0;
							$scope.invoiceObj.serviceTaxTotal = 0;
							$scope.invoiceObj.selectedServiceTax = null;
						}

						$scope.calServiceSubTotal();
						$scope.calfinalTotal();
					};

					$scope.removeProductItem = function(index) {
						$scope.invoiceObj.productLineItemList.splice(index, 1);

						if ($scope.invoiceObj.productLineItemList.length == 0) {
							$scope.invoiceObj.productSubTotal = 0;
							$scope.invoiceObj.productTotal = 0;
							$scope.invoiceObj.productTaxTotal = 0;
							$scope.invoiceObj.selectedProductTax = null;
						}

						$scope.calProductSubTotal();
						$scope.calfinalTotal();
					};

					$scope.discountType = [ "%", "Fixed" ];
					$scope.lineItemDiscountChange = function(index,
							selectedDiscount) {
						$log.debug("##Came to lineItemStockChange...");
						$scope.lineSelectedDiscount = selectedDiscount;
						$scope.invoiceObj.discount = selectedDiscount;
						// $scope.calSubTotal();
						// $scope.calfinalTotal();
					};

					$scope.printInvoice = function(invoiceId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfInvoice?bid=" + bid
								+ "&invoiceId=" + invoiceId);
					}
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
						$scope.loading = true;
						var stockService = appEndpointSF.getStockService();
						stockService.getAllStock($scope.curUser.business.id)
								.then(function(stockList) {
									$scope.stockItemList = stockList;
									$scope.loading = false;
								});
					}

					$scope.checkStock = function(item, $event) {
						for (var i = 0; i <= $scope.stockItemList.length; i++) {
							if ($scope.stockItemList[i].itemName == item.itemName) {
								$scope.qtyErrorMsg = "";
								if ($scope.stockItemList[i].qty < item.qty) {
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
						var taxService = appEndpointSF.getTaxService();

						taxService.getTaxesByVisibility(
								$scope.curUser.business.id).then(
								function(taxList) {
									$scope.taxforinvoice = taxList;
								});
					}
					$scope.taxData = [];

					$scope.getInvoiceSettingsByBiz = function() {
						var invoiceService = appEndpointSF.getInvoiceService();
						invoiceService
								.getInvoiceSettingsByBiz(
										$scope.curUser.business.id)
								.then(
										function(settingsList) {
											$scope.settingsObj = settingsList;
											$scope.invoiceObj.noteToCustomer = $scope.settingsObj.noteToCustomer;
											if ($scope.settingsObj.showDefaultServiceItems
													&& !$scope.invoiceObj.id) {
												$scope.addServiceLineItem();
											}
											if ($scope.settingsObj.showDefaultProductItems
													&& !$scope.invoiceObj.id) {
												$scope.addProductLineItem();
											}
										});
					}

					$scope.querySearch = function(query) {
						var results = query ? $scope.customerList
								.filter(createFilterFor(query))
								: $scope.customerList;
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
						customerService.getAllCustomersByBusiness(
								$scope.curUser.business.id).then(
								function(custList) {
									$scope.customerList = custList.items;
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

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							loadAllCustomers();
							$scope.getAllStock();
							$scope.getTaxesByVisibility();
							$scope.getInvoiceSettingsByBiz();
							$scope.calServiceSubTotal();
							$scope.calProductSubTotal();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					// For Add Customer from Invoice Page through popup
					$scope.addCustomer = function(ev, invoiceObj) {
						var useFullScreen = $mdMedia('xs');
						$mdDialog
								.show(
										{
											controller : addCustDialogController,
											templateUrl : '/app/crm/customer_add_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
												customer : $scope.customer,
												invoiceObj : invoiceObj,
												customerList : $scope.customerList
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

					function addCustDialogController($scope, $mdDialog,
							curUser, customer, invoiceObj, customerList) {

						$scope.addCustomer = function() {
							$scope.customer.business = curUser.business;
							$scope.customer.createdDate = new Date();
							$scope.customer.modifiedBy = curUser.email_id;

							var customerService = appEndpointSF
									.getCustomerService();

							customerService.addCustomer($scope.customer).then(
									function(customerObj) {
										invoiceObj.customer = customerObj;
										customerList.push(customerObj);
									});
							$scope.hide();
						}

						$scope.hide = function() {
							$mdDialog.hide();
						};

						$scope.cancel = function() {
							$mdDialog.cancel();
						};
					}

					// For Add Stock from Invoice Page through popup
					$scope.addStock = function(ev, lineItem) {
						var getAllWarehouseByBusiness = function() {
							var warehouseService = appEndpointSF
									.getWarehouseManagementService();

							warehouseService.getAllWarehouseByBusiness(
									$scope.curUser.business.id).then(
									function(warehouseList) {
										$scope.warehouses = warehouseList;
									});
						}

						getAllWarehouseByBusiness();

						var useFullScreen = $mdMedia('xs');
						$mdDialog
								.show(
										{
											controller : addStockItemDialogController,
											templateUrl : '/app/stock/stockitem_add_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
												stock : $scope.stock,
												warehouses : $scope.warehouses,
												stockItemList : $scope.stockItemList,
												lineItem : lineItem,
												calProductSubTotalFn : $scope.calProductSubTotal
											}
										})
								.then(
										function(answer) {
											$scope.status = 'You said the information was'
													+ answer + '".';
										},
										function() {
											$scope.status = 'You cancelled the dialog.';
										});
					};

					function addStockItemDialogController($scope, $mdDialog,
							curUser, stock, warehouses, stockItemList,
							lineItem, calProductSubTotalFn) {
						$scope.warehouses = warehouses;
						$scope.addStock = function() {
							$scope.stock.business = curUser.business;
							$scope.stock.createdDate = new Date();
							$scope.stock.modifiedBy = curUser.email_id;
							var stockService = appEndpointSF.getStockService();
							stockService.addStock($scope.stock).then(
									function(addedItem) {
										if (addedItem.id) {
											lineItem.stockItem = addedItem;
											lineItem.price = addedItem.price;
											stockItemList.push(addedItem);
											calProductSubTotalFn();
										}
									});
							$scope.cancel();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};
					}

				});
