app = angular.module("stockApp");
app
		.controller(
				"quotationAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $mdMedia, $mdDialog, $q,
						objectFactory, appEndpointSF) {

					$scope.loading = true;
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.getEmptyInvoiceObj = function() {
						return {
							customer : null,
							invoiceDueDate : new Date(),
							noteToCustomer : '',
							createdDate : new Date(),
							createdBy : $scope.curUser,
							modifiedDate : new Date(),
							modifiedBy : '',
							discountType : 'NA',
							discountPercent : 0,
							discAmount : 0,
							serviceDiscAmount : 0,
							productDiscAmount : 0,
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

					$scope.documentEntity = $stateParams.invoiceObj ? $stateParams.invoiceObj
							: $scope.getEmptyInvoiceObj();

					$scope.saveDocument = function() {
						$scope.loading = true;
						if (!$scope.documentEntity.serviceLineItemList
								&& !$scope.documentEntity.productLineItemList) {

							$scope.errorMsg = "Please select atleast one item.";
						} else {
							var InvoiceService = appEndpointSF
									.getInvoiceService();
							$scope.documentEntity.business = $scope.curUser.business;
							$scope.documentEntity.modifiedBy = $scope.curUser.email_id;
							InvoiceService
									.addQuotation($scope.documentEntity)
									.then(
											function(invoice) {
												if (invoice.id) {
													// for edit
													$scope.showUpdateToast();
													$scope.documentEntity.id = invoice.id;
													$scope.documentEntity.itemNumber = invoice.itemNumber;
												}
												$scope.loading = false;
											});

						}
					}

					$scope.draftDocumnent = function(ev) {
						$scope.documentEntity.status = 'DRAFT';
						$scope.saveDocument();
					}

					$scope.submitDocumnent = function(ev) {
						$scope.documentEntity.status = 'SUBMITTED';
						$scope.saveDocument();
					}

					$scope.finalizeDocumnent = function(ev) {
						var confirm = $mdDialog
								.confirm()
								.title(
										'Do you want to approve/finalize this Document? Note, after this you will not be able to make any changes in this document.')
								.textContent('').ariaLabel('finalize?')
								.targetEvent(ev).ok('Yes').cancel('No');

						$mdDialog.show(confirm).then(function() {
							$log.debug("Inside Yes, function");
							$scope.documentEntity.status = 'FINALIZED';
							$scope.documentEntity.approvedBy = $scope.curUser;
							$scope.saveDocument();
						}, function() {
							$log.debug("Cancelled...");
						});
					}

					$scope.rejectDocumnent = function(ev) {
						var confirm = $mdDialog
								.confirm()
								.title(
										'Do you want to reject this Document? Note, after this you will not be able to make any changes in this document.')
								.textContent('').ariaLabel('finalize?')
								.targetEvent(ev).ok('Yes').cancel('No');

						$mdDialog.show(confirm).then(function() {
							$log.debug("Inside Yes, function");
							$scope.documentEntity.status = 'REJECTED';
							$scope.documentEntity.approvedBy = $scope.curUser;
							$scope.saveDocument();
						}, function() {
							$log.debug("Cancelled...");
						});
					}

					$scope.filterStockItemsByWarehouse = function(
							selectedWarehouse) {
						$scope.selectedWarehouse = selectedWarehouse;
						var stockService = appEndpointSF.getStockService();
						stockService.filterStockItemsByWarehouse(
								$scope.selectedWarehouse).then(
								function(stockList) {
									$scope.stockItemList = stockList;
								});
					}

					$scope.getAllWarehouseByBusiness = function() {
						var warehouseService = appEndpointSF
								.getWarehouseManagementService();

						warehouseService
								.getAllWarehouseByBusiness(
										$scope.curUser.business.id)
								.then(
										function(warehouseList) {
											$scope.warehouses = warehouseList;
											if ($scope.documentEntity.fromWH == null
													&& $scope.warehouses.length > 0) {
												$scope.documentEntity.fromWH = $scope.warehouses[0];
											}
											$scope
													.filterStockItemsByWarehouse($scope.documentEntity.fromWH);
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
						if (!$scope.documentEntity.serviceLineItemList) {
							$scope.documentEntity.serviceLineItemList = [];
						}
						$scope.documentEntity.serviceLineItemList.push(item);
					};

					$scope.addProductLineItem = function() {
						var item = {
							isProduct : true,
							itemName : "",
							qty : 1,
							price : "",
							stockItem : {
								stockItemType : null
							},
							selectedTaxItem : null
						};
						if (!$scope.documentEntity.productLineItemList) {
							$scope.documentEntity.productLineItemList = [];
						}
						$scope.documentEntity.productLineItemList.push(item);
					};

					$scope.toggleServices = function() {
						$scope.settingsObj.showDefaultServiceItems = !$scope.settingsObj.showDefaultServiceItems
						$scope.documentEntity.serviceLineItemList = null;
						$scope.documentEntity.selectedServiceTax = null;

						if ($scope.settingsObj.showDefaultServiceItems) {
							$scope.addServiceLineItem();
						}
						$scope.reCalculateTotal();
					};

					$scope.toggleProducts = function() {
						$scope.settingsObj.showDefaultProductItems = !$scope.settingsObj.showDefaultProductItems;
						$scope.documentEntity.productLineItemList = null;
						$scope.documentEntity.selectedProductTax = null;

						if ($scope.settingsObj.showDefaultProductItems) {
							$scope.addProductLineItem();
						}
						$scope.reCalculateTotal();
					};

					$scope.reCalculateTotal = function() {
						$scope.serviceTaxChanged();
						$scope.productTaxChanged();
						$scope.calServiceSubTotal();
						$scope.calProductSubTotal();
						calculateDiscountAmount();
						// This is needed as tax and sub-totals depend on each
						// other
						$scope.serviceTaxChanged();
						$scope.productTaxChanged();

						$scope.calfinalTotal();
					}

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
						$scope.documentEntity.productSubTotal = 0;
						if ($scope.documentEntity.productLineItemList) {
							for (var i = 0; i < $scope.documentEntity.productLineItemList.length; i++) {
								var lineItem = $scope.documentEntity.productLineItemList[i];
								$scope.documentEntity.productSubTotal += (lineItem.qty * lineItem.price);
							}
						}
					}

					$scope.serviceLineItemChanged = function(selectedLineItem) {
						selectedLineItem.price = selectedLineItem.stockItem.price;
						$scope.reCalculateTotal();
					};

					$scope.calServiceSubTotal = function() {
						$scope.documentEntity.serviceSubTotal = 0;
						if ($scope.documentEntity.serviceLineItemList) {
							for (var i = 0; i < $scope.documentEntity.serviceLineItemList.length; i++) {
								var lineItem = $scope.documentEntity.serviceLineItemList[i];
								$scope.documentEntity.serviceSubTotal += (lineItem.qty * lineItem.price);
							}
						}
					}

					$scope.serviceTaxChanged = function() {
						if (!$scope.documentEntity.selectedServiceTax) {
							$scope.documentEntity.serviceTaxTotal = 0;
						} else {
							$scope.documentEntity.serviceTaxTotal = parseFloat(($scope.documentEntity.selectedServiceTax.taxPercenatge / 100)
									* ($scope.documentEntity.serviceSubTotal - $scope.documentEntity.serviceDiscAmount));
						}
						$scope.documentEntity.serviceTotal = ($scope.documentEntity.serviceSubTotal - $scope.documentEntity.serviceDiscAmount)
								+ $scope.documentEntity.serviceTaxTotal;
					};

					$scope.productTaxChanged = function() {
						if (!$scope.documentEntity.selectedProductTax) {
							$scope.documentEntity.productTaxTotal = 0;

						} else {
							$scope.documentEntity.productTaxTotal = parseFloat(($scope.documentEntity.selectedProductTax.taxPercenatge / 100)
									* ($scope.documentEntity.productSubTotal - $scope.documentEntity.productDiscAmount));
						}
						$scope.documentEntity.productTotal = ($scope.documentEntity.productSubTotal - $scope.documentEntity.productDiscAmount)
								+ $scope.documentEntity.productTaxTotal;
					}

					$scope.calfinalTotal = function() {
						var finalTotal = 0;
						var disc = 0;
						$scope.documentEntity.finalTotal = 0;

						if ($scope.documentEntity.serviceTotal)
							finalTotal += parseFloat($scope.documentEntity.serviceTotal);

						if ($scope.documentEntity.productTotal)
							finalTotal += parseFloat($scope.documentEntity.productTotal);

						$scope.documentEntity.finalTotal = finalTotal;
					}

					$scope.removeServiceItem = function(index) {
						$scope.documentEntity.serviceLineItemList.splice(index,
								1);

						if ($scope.documentEntity.serviceLineItemList.length == 0) {
							$scope.documentEntity.serviceSubTotal = 0;
							$scope.documentEntity.serviceTotal = 0;
							$scope.documentEntity.serviceTaxTotal = 0;
							$scope.documentEntity.selectedServiceTax = null;
						}
						$scope.reCalculateTotal();
					};

					$scope.removeProductItem = function(index) {
						$scope.documentEntity.productLineItemList.splice(index,
								1);

						if ($scope.documentEntity.productLineItemList.length == 0) {
							$scope.documentEntity.productSubTotal = 0;
							$scope.documentEntity.productTotal = 0;
							$scope.documentEntity.productTaxTotal = 0;
							$scope.documentEntity.selectedProductTax = null;
						}
						$scope.reCalculateTotal();
					};

					$scope.discountTypeList = [ "NA", "PERCENTAGE", "FIXED" ];

					$scope.discountTypeChanged = function() {
						$scope.documentEntity.serviceDiscAmount = 0;
						$scope.documentEntity.productDiscAmount = 0;
						$scope.documentEntity.discountPercent = 0;
						$scope.documentEntity.discAmount = 0;
						$scope.reCalculateTotal();
					};

					function calculateDiscountAmount() {
						$scope.documentEntity.serviceDiscAmount = 0;
						$scope.documentEntity.productDiscAmount = 0;
						if ($scope.documentEntity.discountType == "PERCENTAGE") {
							if ($scope.documentEntity.serviceSubTotal
									&& $scope.documentEntity.discountPercent) {
								$scope.documentEntity.serviceDiscAmount = $scope.documentEntity.serviceSubTotal
										* ($scope.documentEntity.discountPercent / 100);
							}
							if ($scope.documentEntity.productSubTotal
									&& $scope.documentEntity.discountPercent) {
								$scope.documentEntity.productDiscAmount = $scope.documentEntity.productSubTotal
										* ($scope.documentEntity.discountPercent / 100);
							}

						}
						if ($scope.documentEntity.discountType == "FIXED") {
							$scope.documentEntity.discountPercent = 0;
							if ($scope.documentEntity.serviceSubTotal) {
								$scope.documentEntity.serviceDiscAmount = $scope.documentEntity.discAmount;
							}
							if ($scope.documentEntity.productSubTotal) {
								$scope.documentEntity.serviceDiscAmount = $scope.documentEntity.discAmount;
							}

							if ($scope.documentEntity.serviceSubTotal
									&& $scope.documentEntity.productSubTotal) {
								$scope.documentEntity.serviceDiscAmount = $scope.documentEntity.discAmount / 2;
								$scope.documentEntity.productDiscAmount = $scope.documentEntity.discAmount / 2;
							}
						}

						$scope.documentEntity.discAmount = $scope.documentEntity.serviceDiscAmount
								+ $scope.documentEntity.productDiscAmount;
					}

					$scope.printInvoice = function(quotnId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfQuotation?bid=" + bid
								+ "&quotnId=" + quotnId);
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
									$scope.taxList = taxList;
									if (!$scope.taxList) {
										$scope.taxList = [];
									}
									$scope.taxList.splice(0, 0, null);
								});
					}

					$scope.getInvoiceSettingsByBiz = function() {
						var invoiceService = appEndpointSF.getInvoiceService();
						invoiceService
								.getInvoiceSettingsByBiz(
										$scope.curUser.business.id)
								.then(
										function(settingsList) {
											$scope.settingsObj = settingsList;
											if ($scope.documentEntity.id) {
												if ($scope.documentEntity.serviceLineItemList
														&& $scope.documentEntity.serviceLineItemList.length) {
													$scope.settingsObj.showDefaultServiceItems = true;
												}

												if ($scope.documentEntity.productLineItemList
														&& $scope.documentEntity.productLineItemList.length) {
													$scope.settingsObj.showDefaultProductItems = true;
												}
											} else {
												$scope.documentEntity.noteToCustomer = $scope.settingsObj.noteToCustomer;
												$scope.documentEntity.paymentNotes = $scope.settingsObj.paymentNotes;
												$scope.documentEntity.termsAndConditions = $scope.settingsObj.termsAndConditions;

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

					$scope.querySearch = function(query) {
						var autoCompleteUIService = appEndpointSF
								.getAutoCompleteUIService();
						return autoCompleteUIService.queryCustomerSearch(
								$scope.customerList, query);
					}

					$scope.queryContactSearch = function(query) {
						var autoCompleteUIService = appEndpointSF
								.getAutoCompleteUIService();
						return autoCompleteUIService.queryContactSearch(
								$scope.contactList, query);
					}

					function loadAllCustomers() {
						var customerService = appEndpointSF
								.getCustomerService();
						customerService.getAllCustomersByBusiness(
								$scope.curUser.business.id).then(
								function(custList) {
									$scope.customerList = custList.items;
								});

					}

					$scope.getAllcontact = function() {
						var leadService = appEndpointSF.getleadService();
						leadService.getAllcontact($scope.curUser.business.id)
								.then(function(contacts) {
									$scope.contactList = contacts.items;
								});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							loadAllCustomers();
							$scope.getAllWarehouseByBusiness();
							$scope.getTaxesByVisibility();
							$scope.getInvoiceSettingsByBiz();
							$scope.reCalculateTotal();
							$scope.getAllcontact();
							$scope.loading = false;
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
												invoiceObj : $scope.documentEntity,
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

				});