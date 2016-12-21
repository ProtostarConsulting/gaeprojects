app = angular.module("stockApp");
app
		.controller(
				"purchaseOrderAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $q, $mdMedia, $mdDialog,
						objectFactory, appEndpointSF) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.getEmptyPurchaseOrderObj = function() {
						return {
							supplier : '',
							to : '',
							shipTo : '',
							poDate : new Date(),
							poDueDate : "",
							requisitioner : '',
							shippedVia : '',
							fOBPoint : '',
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

					// list of `state` value/display objects
					$scope.supplierList = [];

					$scope.supplier = null;
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

					$scope.tempSupp = [];

					function createFilterFor(query) {
						var lowercaseQuery = angular.lowercase(query);
						return function filterFn(supp) {

							return (angular.lowercase(supp.supplierName)
									.indexOf(lowercaseQuery) >= 0);
						};
					}

					$scope.purchaseOrderObj = $stateParams.purchaseOrderObj ? $stateParams.purchaseOrderObj
							: $scope.getEmptyPurchaseOrderObj();

					$scope.purchaseOrderObj.invoiceDueDate = $scope.purchaseOrderObj.invoiceDueDate ? new Date(
							$scope.purchaseOrderObj.invoiceDueDate)
							: new Date();

					$scope.addPurchaseOrder = function() {
						if (!$scope.purchaseOrderObj.serviceLineItemList
								&& !$scope.purchaseOrderObj.productLineItemList) {

							$scope.errorMsg = "Please select atleast one item.";
						} else {
							$scope.purchaseOrderObj.business = $scope.curUser.business;
							$scope.purchaseOrderObj.modifiedBy = $scope.curUser.email_id;
							$scope.purchaseOrderObj.discAmount = $scope.discAmount;
							var purchaseService = appEndpointSF
									.getPurchaseOrderService();

							purchaseService.addPurchaseOrder(
									$scope.purchaseOrderObj).then(
									function(msgBean) {
										$scope.showAddToast();
										$scope.pOForm.$setPristine();
										$scope.pOForm.$setValidity();
										$scope.pOForm.$setUntouched();

										$scope.purchaseOrderObj = $scope
												.getEmptyPurchaseOrderObj();
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
						if (!$scope.purchaseOrderObj.serviceLineItemList) {
							$scope.purchaseOrderObj.serviceLineItemList = [];
						}
						$scope.purchaseOrderObj.serviceLineItemList.push(item);
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
						if (!$scope.purchaseOrderObj.productLineItemList) {
							$scope.purchaseOrderObj.productLineItemList = [];
						}
						$scope.purchaseOrderObj.productLineItemList.push(item);
					};

					$scope.productLineItemChanged = function(selectedLineItem) {
						selectedLineItem.price = selectedLineItem.stockItem.price;
						$scope.calProductSubTotal();
					};

					$scope.calProductSubTotal = function() {
						$log.debug("##Came to calSubTotal...");
						$scope.purchaseOrderObj.productSubTotal = 0;

						for (var i = 0; i < $scope.purchaseOrderObj.productLineItemList.length; i++) {
							var lineItem = $scope.purchaseOrderObj.productLineItemList[i];
							$scope.purchaseOrderObj.productSubTotal += (lineItem.qty * lineItem.price);
						}

						$scope.productTaxChanged();
					}

					$scope.serviceLineItemChanged = function(selectedLineItem) {
						selectedLineItem.price = selectedLineItem.stockItem.price;
						$scope.calServiceSubTotal();
					};

					$scope.calServiceSubTotal = function() {
						$scope.purchaseOrderObj.serviceSubTotal = 0;

						for (var i = 0; i < $scope.purchaseOrderObj.serviceLineItemList.length; i++) {
							var lineItem = $scope.purchaseOrderObj.serviceLineItemList[i];
							$scope.purchaseOrderObj.serviceSubTotal += (lineItem.qty * lineItem.price);
						}

						$scope.serviceTaxChanged();
					}

					$scope.serviceTaxChanged = function() {
						if (!$scope.purchaseOrderObj.selectedServiceTax) {
							$scope.purchaseOrderObj.serviceTaxTotal = 0;
						} else {
							$scope.purchaseOrderObj.serviceTaxTotal = parseFloat(($scope.purchaseOrderObj.selectedServiceTax.taxPercenatge / 100)
									* ($scope.purchaseOrderObj.serviceSubTotal));
						}
						$scope.purchaseOrderObj.serviceTotal = $scope.purchaseOrderObj.serviceSubTotal
								+ $scope.purchaseOrderObj.serviceTaxTotal;
						$scope.calfinalTotal();
					};

					$scope.productTaxChanged = function() {
						if (!$scope.purchaseOrderObj.selectedProductTax) {
							$scope.purchaseOrderObj.productTaxTotal = 0;

						} else {
							$scope.purchaseOrderObj.productTaxTotal = parseFloat(($scope.purchaseOrderObj.selectedProductTax.taxPercenatge / 100)
									* ($scope.purchaseOrderObj.productSubTotal));
						}
						$scope.purchaseOrderObj.productTotal = $scope.purchaseOrderObj.productSubTotal
								+ $scope.purchaseOrderObj.productTaxTotal;
						$scope.calfinalTotal();
					}

					$scope.calfinalTotal = function() {
						$scope.tempDiscAmount = 0;
						var finalTotal = 0;
						var disc = 0;
						$scope.purchaseOrderObj.finalTotal = 0;

						if ($scope.purchaseOrderObj.serviceTotal)
							finalTotal += parseFloat($scope.purchaseOrderObj.serviceTotal);

						if ($scope.purchaseOrderObj.productTotal)
							finalTotal += parseFloat($scope.purchaseOrderObj.productTotal);

						$scope.purchaseOrderObj.finalTotal = finalTotal;

						return;

						if ($scope.lineSelectedDiscount != undefined) {
							if ($scope.lineSelectedDiscount == "Fixed") {

								$scope.tempDiscAmount = $scope.discAmount;
								$scope.purchaseOrderObj.discAmount = $scope.tempDiscAmount;

								if ($scope.purchaseOrderObj.productTotal == undefined) {
									$scope.purchaseOrderObj.finalTotal = parseFloat($scope.purchaseOrderObj.serviceTotal)
											- parseFloat($scope.discAmount);
								} else {
									$scope.purchaseOrderObj.finalTotal = (parseFloat($scope.purchaseOrderObj.productTotal) + parseFloat($scope.purchaseOrderObj.serviceTotal))
											- parseFloat($scope.discAmount);
								}

							} else {
								disc = parseInt($scope.discAmount);
								finalTotal = parseFloat($scope.purchaseOrderObj.productSubTotal)
										+ parseFloat($scope.purchaseOrderObj.productTaxTotal)
										+ parseFloat($scope.purchaseOrderObj.serviceSubTotal);

								$scope.tempDiscAmount = ((disc / 100) * finalTotal);

								$scope.purchaseOrderObj.discAmount = $scope.tempDiscAmount;

							}
							if ($scope.purchaseOrderObj.productTotal == undefined) {
								$scope.purchaseOrderObj.finalTotal = (parseFloat($scope.purchaseOrderObj.serviceTotal))
										- parseFloat($scope.tempDiscAmount);
							} else {
								$scope.purchaseOrderObj.finalTotal = (parseFloat($scope.purchaseOrderObj.productTotal) + parseFloat($scope.purchaseOrderObj.serviceTotal))
										- parseFloat($scope.tempDiscAmount);
							}
						}
					}

					$scope.removeServiceItem = function(index) {
						$scope.purchaseOrderObj.serviceLineItemList.splice(
								index, 1);

						if ($scope.purchaseOrderObj.serviceLineItemList.length == 0) {
							$scope.purchaseOrderObj.serviceSubTotal = 0;
							$scope.purchaseOrderObj.serviceTotal = 0;
							$scope.purchaseOrderObj.serviceTaxTotal = 0;
							$scope.purchaseOrderObj.selectedServiceTax = null;
						}

						$scope.calServiceSubTotal();
						$scope.calfinalTotal();
					};

					$scope.removeProductItem = function(index) {
						$scope.purchaseOrderObj.productLineItemList.splice(
								index, 1);

						if ($scope.purchaseOrderObj.productLineItemList.length == 0) {
							$scope.purchaseOrderObj.productSubTotal = 0;
							$scope.purchaseOrderObj.productTotal = 0;
							$scope.purchaseOrderObj.productTaxTotal = 0;
							$scope.purchaseOrderObj.selectedProductTax = null;
						}

						$scope.calProductSubTotal();
						$scope.calfinalTotal();
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
									$scope.stockItemList = stockList;
								});
					}

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
											$scope.purchaseOrderObj.noteToCustomer = $scope.settingsObj.noteToCustomer;
											if ($scope.settingsObj.showDefaultServiceItems
													&& !$scope.purchaseOrderObj.id) {
												$scope.addServiceLineItem();
											}
											if ($scope.settingsObj.showDefaultProductItems
													&& !$scope.purchaseOrderObj.id) {
												$scope.addProductLineItem();
											}
										});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							loadAllSuppliers();
							// loadContacts();
							$scope.getAllStock();
							// $scope.getAllSalesOrder();
							$scope.getTaxesByVisibility();
							// $scope.getAllAccountsByBusiness();
							$scope.getInvoiceSettingsByBiz();
							// $scope.getAllWarehouseByBusiness();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					$scope.addSupplier = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : addSupplierDialogController,
											templateUrl : '/app/purchase/supplier_add.html',
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

							supplierService.addSupplier($scope.supplier).then(
									function(msgBean) {

									});
							$scope.hide();
							// window.history.back();
						}

						$scope.hide = function() {
							$mdDialog.hide();
						};
					}

					// For Add Stock from Invoice Page through popup
					$scope.addStock = function(ev, lineItem) {
						var useFullScreen = $mdMedia('xs');
						$mdDialog
								.show(
										{
											controller : addStockItemDialogController,
											templateUrl : '/app/stock/stockItem_add.html',
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
												lineItem : lineItem
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
							curUser, stock, warehouses, stockItemList, lineItem) {

						$scope.addStock = function() {
							$scope.stock.business = curUser.business;
							$scope.stock.createdDate = new Date();
							$scope.stock.modifiedBy = curUser.email_id;
							var stockService = appEndpointSF.getStockService();
							stockService.addStock($scope.stock).then(
									function(addedItem) {
										if (addedItem.id) {
											lineItem.stockItem = addedItem;
											stockItemList.push(addedItem);
										}
									});
							$scope.hide();
						}

						$scope.hide = function() {
							$mdDialog.hide();
						};
					}

				});
