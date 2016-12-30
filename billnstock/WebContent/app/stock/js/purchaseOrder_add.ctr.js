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

					$scope.purchaseOrderObj = $stateParams.purchaseOrderObj ? $stateParams.purchaseOrderObj
							: $scope.getEmptyPurchaseOrderObj();

					$scope.purchaseOrderObj.poDueDate = $scope.purchaseOrderObj.poDueDate ? new Date(
							$scope.purchaseOrderObj.poDueDate)
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

					$scope.removeServices = function(toAddRemove) {
						if (toAddRemove) {
							$scope.settingsObj.showDefaultServiceItems = true;
							$scope.addServiceLineItem();
						} else {
							$scope.settingsObj.showDefaultServiceItems = false;
							$scope.purchaseOrderObj.serviceLineItemList = [];
						}
					};
					$scope.removeProducts = function(toAddRemove) {
						if (toAddRemove) {
							$scope.settingsObj.showDefaultProductItems = true;
							$scope.addProductLineItem();
						} else {
							$scope.settingsObj.showDefaultProductItems = false;
							$scope.purchaseOrderObj.productLineItemList = [];
						}
					};

					$scope.productLineItemChanged = function(selectedLineItem) {
						selectedLineItem.price = selectedLineItem.stockItem.price;
						$scope.calProductSubTotal();
					};

					$scope.calProductSubTotal = function() {
						$scope.purchaseOrderObj.productSubTotal = 0;
						if ($scope.purchaseOrderObj.productLineItemList) {
							for (var i = 0; i < $scope.purchaseOrderObj.productLineItemList.length; i++) {
								var lineItem = $scope.purchaseOrderObj.productLineItemList[i];
								$scope.purchaseOrderObj.productSubTotal += (lineItem.qty * lineItem.price);
							}

							$scope.productTaxChanged();
						}
					}

					$scope.serviceLineItemChanged = function(selectedLineItem) {
						selectedLineItem.price = selectedLineItem.stockItem.price;
						$scope.calServiceSubTotal();
					};

					$scope.calServiceSubTotal = function() {
						$scope.purchaseOrderObj.serviceSubTotal = 0;
						if ($scope.purchaseOrderObj.serviceLineItemList) {
							for (var i = 0; i < $scope.purchaseOrderObj.serviceLineItemList.length; i++) {
								var lineItem = $scope.purchaseOrderObj.serviceLineItemList[i];
								$scope.purchaseOrderObj.serviceSubTotal += (lineItem.qty * lineItem.price);
							}

							$scope.serviceTaxChanged();
						}
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

					$scope.discountType = [ "%", "Fixed" ];
					$scope.lineItemDiscountChange = function(index,
							selectedDiscount) {
						$log.debug("##Came to lineItemStockChange...");
						$scope.lineSelectedDiscount = selectedDiscount;
						$scope.purchaseOrderObj.discount = selectedDiscount;
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
						stockService.getAllStockItems($scope.curUser.business.id)
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
												curUser : $scope.curUser,
												supplierList : $scope.supplierList,
												purchaseOrderObj : $scope.purchaseOrderObj
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
							curBusi, curUser, supplierList, purchaseOrderObj) {

						$scope.addSupplier = function() {
							$scope.supplier.business = curUser.business;
							$scope.supplier.createdDate = new Date();
							$scope.supplier.modifiedBy = curUser.email_id;
							var supplierService = appEndpointSF
									.getSupplierService();

							supplierService.addSupplier($scope.supplier).then(
									function(supplierObj) {
										purchaseOrderObj.supplier = supplierObj
										supplierList.push(supplierObj);
									});
							$scope.cancel();
							// window.history.back();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};
					}

				});
