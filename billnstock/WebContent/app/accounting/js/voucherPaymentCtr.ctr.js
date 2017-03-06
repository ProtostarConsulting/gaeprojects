var app = angular.module("stockApp");

app
		.controller(
				"voucherPaymentCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory, $q,
						appEndpointSF, $mdDialog, $mdMedia, $state) {
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					$scope.vouchersPuraview = $stateParams.Account;
					$scope.accountId = $stateParams.AccountId;
					var i, flag, PurchesVoucherEntity = "PurchaseVoucherEntity";
					$scope.loading = true;
					var paymentVouchers = function() {
						return {

							paymentAccount : "",
							creditAccount : "",
							stockReceiptEntity : "",
							amount : null,
							narration : "",
							isCash : true,
							accdetail : "",
							stockAccount : "",
							business : $scope.curUser.business
						}
					};
					$scope.paymentVouchers = paymentVouchers();
					$scope.paymentAccList = [];
					$scope.paymentCreditAcc = [];
					$scope.stockItems = [];
					$scope.getAccountList = function() {
						$scope.loading = true;
						var accountService = appEndpointSF.getAccountService();
						accountService
								.getAccountList($scope.curUser.business.id)
								.then(
										function(list) {

											for (var x = 0; x < list.length; x++) {

												if (list[x].accountGroup.groupName
														.trim() != "Sundry Creditors"
														&& list[x].accountGroup.groupName
																.trim() != "Cash-in-hand"
														&& list[x].accountGroup.groupName
																.trim() != "Bank Accounts") {
													$scope.paymentAccList
															.push(list[x]);
												}
												if (list[x].accountGroup.groupName
														.trim() == "Sundry Creditors"
														|| list[x].accountGroup.groupName
																.trim() == "Cash-in-hand"
														|| list[x].accountGroup.groupName
																.trim() == "Bank Accounts") {
													$scope.paymentCreditAcc
															.push(list[x]);
												}
											}
											$scope.loading = false;

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
					$scope.downloadpdf = function() {
						// window.location.href ="PdfSales";
						window.open("PdfVouchers?id="
								+ $scope.vouchersPuraview.id + "&entityname="
								+ PurchesVoucherEntity + "&bid="
								+ $scope.curUser.business.id);
						// myWindow=window.open('PdfSales','mypage.jsp','width=200,height=100');
						// myWindow.focus();

					}
					$scope.addvoucher = function() {
						if ($scope.paymentVouchers.isCash == true) {
							$scope.paymentVouchers.accdetail = ""
						}

						var voucherService = appEndpointSF.getVoucherService();
						voucherService
								.addvoucherPayment($scope.paymentVouchers)
								.then(function() {

									$scope.showAddToast();
									$scope.debitCurrentBalance = " ";
									$scope.creditCurrentBalance = " ";
									$scope.paymentVouchers = paymentVouchers();
									$scope.voucherPaymentForm.$setPristine();
									$scope.voucherPaymentForm.$setValidity();
									$scope.voucherPaymentForm.$setUntouched();

								});
					}
					$scope.cancle = function() {

						$state.go('accounting', {});

					}

					$scope.back = function() {

						$state.go('accounting.voucherSalesList', {});

					}
					$scope.getAccountEntryByAccountId = function(acc, fl) {

						var accountservice = appEndpointSF.getAccountService();
						if(fl==1)
{
						accountservice
								.getAccountBalance(acc.paymentAccount.id)
								.then(function(balance) {

											if (acc.paymentAccount.accountGroup.groupName
													.trim() == "Purchase Accounts")
												$scope.addStock(acc);
										

											
												$scope.debitCurrentBalance = balance;
											
										});
						}
						
						
						if(fl==2){

							accountservice
									.getAccountBalance(acc.creditAccount.id)
									.then(function(balance) {

											$scope.creditCurrentBalance = balance;

									});
							}
						
						
						
					}

					$scope.addStock = function(acc,ev) {

						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : addStockDialogController,
											templateUrl : '/app/accounting/stock_add_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curBusi : $scope.curUser.business,
												acc : acc,
												curUser : $scope.curUser
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

					function addStockDialogController($scope, $window,
							$mdToast, $timeout, $mdSidenav, $mdUtil, $log,
							$state, $http, $stateParams, $routeParams, $filter,
							$mdMedia, $mdDialog, $q, $rootScope, objectFactory,
							appEndpointSF, acc) {

						$scope.curUser = appEndpointSF.getLocalUserService()
								.getLoggedinUser();

						$scope.getEmptyStockReceiptObj = function() {
							return {
								warehouse : null,
								supplier : null,
								discAmount : 0,
								poNumber : null,
								serviceLineItemList : [],
								productLineItemList : [],
								selectedServiceTax : null,
								selectedProductTax : null,
								finalTotal : 0,
								isPaid : false,
								isDraft : false,
								paidDate : null,
								business : null,
								status : 'DRAFT'
							};
						}

						$scope.stockReceiptObj = $stateParams.stockReceiptObj ? $stateParams.stockReceiptObj
								: $scope.getEmptyStockReceiptObj();

						$scope.stockReceiptObj.receiptDate = $scope.stockReceiptObj.receiptDate ? new Date(
								$scope.stockReceiptObj.receiptDate)
								: new Date();
						$scope.stockReceiptObj.poNumber = $scope.stockReceiptObj.poNumber ? Number($scope.stockReceiptObj.poNumber)
								: '';

						$scope.addStockReceipt = function() {
							$scope.stockReceiptObj.business = $scope.curUser.business;
							$scope.stockReceiptObj.modifiedBy = $scope.curUser.email_id;
							var stockService = appEndpointSF.getStockService();

							stockService
									.addStockReceipt($scope.stockReceiptObj)
									.then(
											function(msgBean) {
												acc.amount = $scope.stockReceiptObj.finalTotal;

												if (msgBean.id) {
													$scope.showUpdateToast();
												}
											});

						}
						$rootScope.$on("CallParentMethod", function() {
							$scope.parentmethod();
						});

						$scope.finalizeStockReceipt = function(ev) {
							var confirm = $mdDialog
									.confirm()
									.title(
											'Do you want to finalize this Receipt? Note, after this you will not be able to make any changes in this document.')
									.textContent('').ariaLabel('finalize?')
									.targetEvent(ev).ok('Yes').cancel('No');

							$mdDialog.show(confirm).then(function() {
								$log.debug("Inside Yes, function");
								$scope.stockReceiptObj.status = 'FINALIZED';
								$scope.addStockReceipt();
							}, function() {
								$log.debug("Cancelled...");
							});
						}

						$scope.addServiceLineItem = function() {
							var item = {
								isProduct : false,
								itemName : "",
								qty : 1,
								price : "",
								cost : "",
								stockItem : null,
								selectedTaxItem : null
							};
							if (!$scope.stockReceiptObj.serviceLineItemList) {
								$scope.stockReceiptObj.serviceLineItemList = [];
							}
							$scope.stockReceiptObj.serviceLineItemList
									.push(item);
						};

						$scope.addProductLineItem = function() {
							var item = {
								isProduct : true,
								itemName : "",
								qty : 1,
								price : "",
								cost : "",
								stockItem : {
									stockItemType : null
								},
								selectedTaxItem : null
							};
							if (!$scope.stockReceiptObj.productLineItemList) {
								$scope.stockReceiptObj.productLineItemList = [];
							}
							$scope.stockReceiptObj.productLineItemList
									.push(item);
						};

						$scope.productLineItemChanged = function(
								selectedLineItem) {
							selectedLineItem.price = selectedLineItem.stockItem.price;
							$scope.calProductSubTotal();
						};

						$scope.calProductSubTotal = function() {
							$log.debug("##Came to calSubTotal...");
							$scope.stockReceiptObj.productSubTotal = 0;

							for (var i = 0; i < $scope.stockReceiptObj.productLineItemList.length; i++) {
								var lineItem = $scope.stockReceiptObj.productLineItemList[i];
								$scope.stockReceiptObj.productSubTotal += (lineItem.qty * lineItem.price);
							}

							$scope.productTaxChanged();
						}

						$scope.serviceLineItemChanged = function(
								selectedLineItem) {
							selectedLineItem.cost = selectedLineItem.stockItem.cost;
							$scope.calServiceSubTotal();
						};

						$scope.calServiceSubTotal = function() {
							$scope.stockReceiptObj.serviceSubTotal = 0;

							for (var i = 0; i < $scope.stockReceiptObj.serviceLineItemList.length; i++) {
								var lineItem = $scope.stockReceiptObj.serviceLineItemList[i];
								$scope.stockReceiptObj.serviceSubTotal += (lineItem.qty * lineItem.cost);
							}

							$scope.serviceTaxChanged();
						}

						$scope.serviceTaxChanged = function() {
							if (!$scope.stockReceiptObj.selectedServiceTax) {
								$scope.stockReceiptObj.serviceTaxTotal = 0;
							} else {
								$scope.stockReceiptObj.serviceTaxTotal = parseFloat(($scope.stockReceiptObj.selectedServiceTax.taxPercenatge / 100)
										* ($scope.stockReceiptObj.serviceSubTotal));
							}
							$scope.stockReceiptObj.serviceTotal = $scope.stockReceiptObj.serviceSubTotal
									+ $scope.stockReceiptObj.serviceTaxTotal;
							$scope.calfinalTotal();
						};

						$scope.productTaxChanged = function() {
							if (!$scope.stockReceiptObj.selectedProductTax) {
								$scope.stockReceiptObj.productTaxTotal = 0;

							} else {
								$scope.stockReceiptObj.productTaxTotal = parseFloat(($scope.stockReceiptObj.selectedProductTax.taxPercenatge / 100)
										* ($scope.stockReceiptObj.productSubTotal));
							}
							$scope.stockReceiptObj.productTotal = $scope.stockReceiptObj.productSubTotal
									+ $scope.stockReceiptObj.productTaxTotal;
							$scope.calfinalTotal();
						}

						$scope.calfinalTotal = function() {
							$scope.tempDiscAmount = 0;
							var finalTotal = 0;
							var disc = 0;
							$scope.stockReceiptObj.finalTotal = 0;

							if ($scope.stockReceiptObj.serviceTotal)
								finalTotal += parseFloat($scope.stockReceiptObj.serviceTotal);

							if ($scope.stockReceiptObj.productTotal)
								finalTotal += parseFloat($scope.stockReceiptObj.productTotal);

							$scope.stockReceiptObj.finalTotal = finalTotal;

							return;

							if ($scope.lineSelectedDiscount != undefined) {
								if ($scope.lineSelectedDiscount == "Fixed") {

									$scope.tempDiscAmount = $scope.discAmount;
									$scope.stockReceiptObj.discAmount = $scope.tempDiscAmount;

									if ($scope.stockReceiptObj.productTotal == undefined) {
										$scope.stockReceiptObj.finalTotal = parseFloat($scope.stockReceiptObj.serviceTotal)
												- parseFloat($scope.discAmount);
									} else {
										$scope.stockReceiptObj.finalTotal = (parseFloat($scope.stockReceiptObj.productTotal) + parseFloat($scope.stockReceiptObj.serviceTotal))
												- parseFloat($scope.discAmount);
									}

								} else {
									disc = parseInt($scope.discAmount);
									finalTotal = parseFloat($scope.stockReceiptObj.productSubTotal)
											+ parseFloat($scope.stockReceiptObj.productTaxTotal)
											+ parseFloat($scope.stockReceiptObj.serviceSubTotal);

									$scope.tempDiscAmount = ((disc / 100) * finalTotal);

									$scope.stockReceiptObj.discAmount = $scope.tempDiscAmount;

								}
								if ($scope.stockReceiptObj.productTotal == undefined) {
									$scope.stockReceiptObj.finalTotal = (parseFloat($scope.stockReceiptObj.serviceTotal))
											- parseFloat($scope.tempDiscAmount);
								} else {
									$scope.stockReceiptObj.finalTotal = (parseFloat($scope.stockReceiptObj.productTotal) + parseFloat($scope.stockReceiptObj.serviceTotal))
											- parseFloat($scope.tempDiscAmount);
								}
							}
						}

						$scope.removeServiceItem = function(index) {
							$scope.stockReceiptObj.serviceLineItemList.splice(
									index, 1);

							if ($scope.stockReceiptObj.serviceLineItemList.length == 0) {
								$scope.stockReceiptObj.serviceSubTotal = 0;
								$scope.stockReceiptObj.serviceTotal = 0;
								$scope.stockReceiptObj.serviceTaxTotal = 0;
								$scope.stockReceiptObj.selectedServiceTax = null;
							}

							$scope.calServiceSubTotal();
							$scope.calfinalTotal();
						};

						$scope.removeProductItem = function(index) {
							$scope.stockReceiptObj.productLineItemList.splice(
									index, 1);

							if ($scope.stockReceiptObj.productLineItemList.length == 0) {
								$scope.stockReceiptObj.productSubTotal = 0;
								$scope.stockReceiptObj.productTotal = 0;
								$scope.stockReceiptObj.productTaxTotal = 0;
								$scope.stockReceiptObj.selectedProductTax = null;
							}

							$scope.calProductSubTotal();
							$scope.calfinalTotal();
						};

						$scope.discountType = [ "%", "Fixed" ];
						$scope.lineItemDiscountChange = function(index,
								selectedDiscount) {
							$log.debug("##Came to lineItemStockChange...");
							$scope.lineSelectedDiscount = selectedDiscount;
							$scope.stockReceiptObj.discount = selectedDiscount;
							// $scope.calSubTotal();
							// $scope.calfinalTotal();
						};

						$scope.getStockItemTypes = function() {
							$log.debug("Inside Ctr $scope.getStockItemTypes");
							var stockService = appEndpointSF.getStockService();

							stockService.getStockItemTypes(
									$scope.curUser.business.id).then(
									function(list) {
										$scope.stockTypeList = list;
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

						$scope.getPOByItemNumber = function(itemNumber) {
							$log
									.debug("Inside function $scope.getPOByItemNumber");
							var stockService = appEndpointSF.getStockService();
							stockService
									.getPOByItemNumber(itemNumber)
									.then(
											function(poObj) {
												$log
														.debug("Returned getPOByItemNumber:"
																+ poObj);
												if (poObj && poObj.id) {
													$log.debug("Found PO");
													$scope.stockReceiptObj.serviceLineItemList = poObj.serviceLineItemList;
													$scope.stockReceiptObj.productLineItemList = poObj.productLineItemList;
													$scope.stockReceiptObj.selectedServiceTax = poObj.selectedServiceTax;
													$scope.stockReceiptObj.selectedProductTax = poObj.selectedProductTax;
													$scope.stockReceiptObj.productTaxTotal = poObj.productTaxTotal;
													$scope.stockReceiptObj.finalTotal = poObj.finalTotal;
													$scope.stockReceiptObj.supplier = poObj.supplier;
													if ($scope.stockReceiptObj.warehouse.id !== poObj.warehouse.id)
														$scope.stockReceiptObj.warehouse = poObj.warehouse;
												}
											});
						}

						$scope.getAllWarehouseByBusiness = function() {
							$log
									.debug("Inside function $scope.getAllWarehouseByBusiness");
							var warehouseService = appEndpointSF
									.getWarehouseManagementService();

							warehouseService.getAllWarehouseByBusiness(
									$scope.curUser.business.id).then(
									function(warehouseList) {
										$scope.warehouses = warehouseList;
									});
						}

						$scope.supplierList = [];
						$scope.searchTextInput = null;

						$scope.querySearch = function(query) {
							var results = query ? $scope.supplierList
									.filter(createFilterFor(query)) : [];
							var deferred = $q.defer();
							$timeout(function() {
								deferred.resolve(results);

							}, Math.random() * 1000, false);
							return deferred.promise;
						}

						function loadAllSuppliers() {
							var supplierService = appEndpointSF
									.getSupplierService();

							supplierService.getAllSuppliersByBusiness(
									$scope.curUser.business.id).then(
									function(supplierList) {

										$scope.supplierList = supplierList;

									});
						}
						function createFilterFor(query) {
							var lowercaseQuery = angular.lowercase(query);
							return function filterFn(supp) {

								return (angular.lowercase(supp.supplierName)
										.indexOf(lowercaseQuery) >= 0);
							};
						}
						$scope.waitForServiceLoad = function() {
							if (appEndpointSF.is_service_ready) {
								loadAllSuppliers();
								$scope.getStockItemTypes();
								$scope.getTaxesByVisibility();
								$scope.getAllWarehouseByBusiness();
								$scope.calProductSubTotal();
								$scope.calServiceSubTotal();

								if (!$scope.stockReceiptObj.id) {
									$scope.addProductLineItem();
								}

							} else {
								$log.debug("Services Not Loaded, watiting...");
								$timeout($scope.waitForServiceLoad, 1000);
							}
						}

						$scope.waitForServiceLoad();

						$scope.addSerialNumbers = function(ev, stockLineItem) {
							var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
									&& $scope.customFullscreen;
							$mdDialog
									.show(
											{
												controller : addSerialNumbersDialogController,
												templateUrl : '/app/stock/add_stock_serialnumbers_dialog.html',
												parent : angular
														.element(document.body),
												targetEvent : ev,
												clickOutsideToClose : true,
												fullscreen : useFullScreen,
												locals : {
													curBusi : $scope.curUser.business,
													curUser : $scope.curUser,
													stockLineItem : stockLineItem
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

						function addSerialNumbersDialogController($scope,
								$mdDialog, curBusi, curUser, stockLineItem) {
							$scope.stockLineItem = stockLineItem;
							function getOrCreateStockItemInstance(index, list) {
								if (index < list.length) {
									return list[index];
								} else {
									return {
										business : curBusi,
										stockItem : stockLineItem.stockItem,
										modifiedBy : curUser.email_id,
										serialNumber : ''
									};
								}
							}

							var tempList = [];
							var existingList = [];
							if ($scope.stockLineItem.stockItemInstanceList
									&& $scope.stockLineItem.stockItemInstanceList.length > 0) {
								existingList = $scope.stockLineItem.stockItemInstanceList;
							}

							for (var i = 0; i < $scope.stockLineItem.qty; i++) {
								tempList.push(getOrCreateStockItemInstance(i,
										existingList));
							}

							$scope.stockLineItem.stockItemInstanceList = tempList;

							$scope.updateSerialNumbers = function() {
								$mdDialog.hide();
							}

							$scope.cancel = function() {
								$mdDialog.cancel();
							};
						}

						$scope.addSupplier = function(ev) {
							var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
									&& $scope.customFullscreen;
							$mdDialog
									.show(
											{
												controller : addSupplierDialogController,
												templateUrl : '/app/stock/supplier_add_dialog.html',
												parent : angular
														.element(document.body),
												targetEvent : ev,
												clickOutsideToClose : true,
												fullscreen : useFullScreen,
												locals : {
													curBusi : $scope.curUser.business,
													supplier : $scope.supplier,
													curUser : $scope.curUser
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

						function addSupplierDialogController($scope, $mdDialog,
								curBusi, curUser, supplier) {
							$scope.addSupplier = function() {
								$scope.supplier.business = curUser.business;
								$scope.supplier.createdDate = new Date();
								$scope.supplier.modifiedBy = curUser.email_id;
								var supplierService = appEndpointSF
										.getSupplierService();

								supplierService.addSupplier($scope.supplier)
										.then(function(msgBean) {
										});
								$scope.cancel();
								// window.history.back();
							}

							$scope.cancel = function() {
								$mdDialog.hide();
							};
						}
						$scope.addStockItem = function(ev, lineItem) {
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
													stockItemList : $scope.stockTypeList,
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

						function addStockItemDialogController($scope,
								$mdDialog, curUser, stock, warehouses,
								stockItemList, lineItem, calProductSubTotalFn) {
							$scope.warehouses = warehouses;
							$scope.addStock = function() {
								$scope.stock.business = curUser.business;
								$scope.stock.createdDate = new Date();
								$scope.stock.modifiedBy = curUser.email_id;
								// /////////////////////////////////////////////////////////////////////////////////////////////////
								var stockService = appEndpointSF
										.getStockService();
								stockService
										.addStockItemType($scope.stock)
										.then(
												function(addedItem) {
													if (addedItem.id) {
														lineItem.stockItem.stockItemType = addedItem;
														lineItem.price = addedItem.price;
														stockItemList
																.push(addedItem);
														calProductSubTotalFn();
													}
												});
								$scope.cancel();
							}
							$scope.cancel = function() {
								$mdDialog.cancel();
							};
						}

					}
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAccountList();
							$scope.loading = false;

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

				});
