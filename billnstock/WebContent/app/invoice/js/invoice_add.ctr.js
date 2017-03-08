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
							discountType : 'NA',
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
							business : null,
							fromWH : null,
							status : 'DRAFT'
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
							InvoiceService
									.addInvoice($scope.invoiceObj)
									.then(
											function(invoice) {
												if (invoice.id) {
													// for edit
													$scope.showUpdateToast();
													$scope.invoiceObj.id = invoice.id;
													$scope.invoiceObj.itemNumber = invoice.itemNumber;
												}
												$scope.loading = false;
											});

						}
					}
					$scope.submitInvoice = function(ev) {
						$scope.invoiceObj.status = 'SUBMITTED';
						$scope.addInvoice();
					}
					$scope.finalizeInvoice = function(ev) {
						var confirm = $mdDialog
								.confirm()
								.title(
										'Do you want to finalize this Invoice? Note, after this you will not be able to make any changes in this document.')
								.textContent('').ariaLabel('finalize?')
								.targetEvent(ev).ok('Yes').cancel('No');

						$mdDialog.show(confirm).then(function() {
							$log.debug("Inside Yes, function");
							$scope.invoiceObj.status = 'FINALIZED';
							$scope.invoiceObj.approvedBy = $scope.curUser;
							$scope.addInvoice();
						}, function() {
							$log.debug("Cancelled...");
						});
					}

					$scope.filterStockItemsByWarehouse = function(
							selectedWarehouse) {
						$scope.loading = true;
						$scope.selectedWarehouse = selectedWarehouse;
						var stockService = appEndpointSF.getStockService();

						stockService.filterStockItemsByWarehouse(
								$scope.selectedWarehouse).then(
								function(stockList) {
									$scope.stockItemList = stockList;
									$scope.loading = false;
								});
					}

					$scope.getAllWarehouseByBusiness = function() {
						$log
								.debug("Inside function $scope.getAllWarehouseByBusiness");
						$scope.loading = true;
						var warehouseService = appEndpointSF
								.getWarehouseManagementService();

						warehouseService
								.getAllWarehouseByBusiness(
										$scope.curUser.business.id)
								.then(
										function(warehouseList) {
											$scope.warehouses = warehouseList;
											if ($scope.invoiceObj.fromWH == null
													&& $scope.warehouses.length > 0) {
												$scope.invoiceObj.fromWH = $scope.warehouses[0];
											}
											$scope
													.filterStockItemsByWarehouse($scope.invoiceObj.fromWH);
											$scope.loading = false;
										});
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

					$scope.toggleServices = function() {
						$scope.settingsObj.showDefaultServiceItems = !$scope.settingsObj.showDefaultServiceItems
						$scope.invoiceObj.serviceLineItemList = null;
						$scope.invoiceObj.selectedServiceTax = null;

						if ($scope.settingsObj.showDefaultServiceItems) {
							$scope.addServiceLineItem();
						}
						$scope.reCalculateTotal();
					};

					$scope.reCalculateTotal = function() {
						$scope.serviceTaxChanged();
						$scope.productTaxChanged();
						$scope.calServiceSubTotal();
						$scope.calProductSubTotal();
						// This is needed as tax and sub-totals depend on each
						// other
						$scope.serviceTaxChanged();
						$scope.productTaxChanged();

						$scope.calfinalTotal();
					}

					$scope.toggleProducts = function() {
						$scope.settingsObj.showDefaultProductItems = !$scope.settingsObj.showDefaultProductItems;
						$scope.invoiceObj.productLineItemList = null;
						$scope.invoiceObj.selectedProductTax = null;

						if ($scope.settingsObj.showDefaultProductItems) {
							$scope.addProductLineItem();
						}
						$scope.reCalculateTotal();
					};

					$scope.productLineItemChangedEventFn = function() {
						$scope.productLineItemChangedEvent = true;
					}
					$scope.productLineItemChanged = function(selectedLineItem) {
						if ($scope.productLineItemChangedEvent) {
							selectedLineItem.price = selectedLineItem.stockItem.price;
							$scope.productLineItemChangedEvent = null;
						}
						$scope.reCalculateTotal();
					};

					$scope.calProductSubTotal = function() {
						$scope.invoiceObj.productSubTotal = 0;
						if ($scope.invoiceObj.productLineItemList) {
							for (var i = 0; i < $scope.invoiceObj.productLineItemList.length; i++) {
								var lineItem = $scope.invoiceObj.productLineItemList[i];
								$scope.invoiceObj.productSubTotal += (lineItem.qty * lineItem.price);
							}
						}
					}

					$scope.serviceLineItemChanged = function(selectedLineItem) {
						selectedLineItem.price = selectedLineItem.stockItem.price;
						$scope.reCalculateTotal();
					};

					$scope.calServiceSubTotal = function() {
						$scope.invoiceObj.serviceSubTotal = 0;
						if ($scope.invoiceObj.serviceLineItemList) {
							for (var i = 0; i < $scope.invoiceObj.serviceLineItemList.length; i++) {
								var lineItem = $scope.invoiceObj.serviceLineItemList[i];
								$scope.invoiceObj.serviceSubTotal += (lineItem.qty * lineItem.price);
							}
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
						$scope.reCalculateTotal();
					};

					$scope.removeProductItem = function(index) {
						$scope.invoiceObj.productLineItemList.splice(index, 1);

						if ($scope.invoiceObj.productLineItemList.length == 0) {
							$scope.invoiceObj.productSubTotal = 0;
							$scope.invoiceObj.productTotal = 0;
							$scope.invoiceObj.productTaxTotal = 0;
							$scope.invoiceObj.selectedProductTax = null;
						}
						$scope.reCalculateTotal();
					};

					$scope.discountType = [ "%", "Fixed" ];
					$scope.lineItemDiscountChange = function(index,
							selectedDiscount) {
						$log.debug("##Came to lineItemStockChange...");
						$scope.lineSelectedDiscount = selectedDiscount;
						$scope.invoiceObj.discount = selectedDiscount;
					};

					$scope.printInvoice = function(invoiceId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfInvoice?bid=" + bid
								+ "&invoiceId=" + invoiceId);
					}

					$scope.getAllStockItems = function() {
						$scope.loading = true;
						var stockService = appEndpointSF.getStockService();
						stockService.getAllStockItems(
								$scope.curUser.business.id).then(
								function(stockList) {
									$scope.stockItemList = stockList;
									$scope.loading = false;
								});
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
											if ($scope.invoiceObj.id) {
												if ($scope.invoiceObj.serviceLineItemList
														&& $scope.invoiceObj.serviceLineItemList.length) {
													$scope.settingsObj.showDefaultServiceItems = true;
												}

												if ($scope.invoiceObj.productLineItemList
														&& $scope.invoiceObj.productLineItemList.length) {
													$scope.settingsObj.showDefaultProductItems = true;
												}
											} else {
												$scope.invoiceObj.noteToCustomer = $scope.settingsObj.noteToCustomer;
												$scope.invoiceObj.paymentNotes = $scope.settingsObj.paymentNotes;
												$scope.invoiceObj.termsAndConditions = $scope.settingsObj.termsAndConditions;

												if ($scope.settingsObj.showDefaultServiceItems) {
													$scope.addServiceLineItem();
												}

												if ($scope.settingsObj.showDefaultProductItems) {
													$scope.addProductLineItem();
												}
											}
										});
					}

					$scope.querySearch = function(query) {
						var results = query ? $scope.customerList
								.filter(createFilterFor(query)) : [];
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
									.indexOf(lowercaseQuery) >= 0);
						};
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							loadAllCustomers();
							$scope.getAllWarehouseByBusiness();
							$scope.getTaxesByVisibility();
							$scope.getInvoiceSettingsByBiz();
							$scope.reCalculateTotal();

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