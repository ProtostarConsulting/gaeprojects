app = angular.module("stockApp");
app
		.controller(
				"stockShipmentAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $mdMedia, $mdDialog, $q,
						objectFactory, appEndpointSF) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.getEmptystockShipmentObj = function() {
						return {
							customer : null,
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
							business : null
						};
					}

					$scope.stockShipmentObj = $stateParams.stockShipmentObj ? $stateParams.stockShipmentObj
							: $scope.getEmptystockShipmentObj();

					$scope.stockShipmentObj.shipmentDate = $scope.stockShipmentObj.shipmentDate ? new Date(
							$scope.stockShipmentObj.shipmentDate)
							: new Date();
					$scope.stockShipmentObj.poNumber = $scope.stockShipmentObj.poNumber ? Number($scope.stockShipmentObj.poNumber)
							: '';
					$scope.addStockShipment = function() {
						$scope.stockShipmentObj.business = $scope.curUser.business;
						$scope.stockShipmentObj.modifiedBy = $scope.curUser.email_id;
						var stockService = appEndpointSF.getStockService();

						stockService
								.addStockShipment($scope.stockShipmentObj)
								.then(
										function(msgBean) {
											if ($scope.stockShipmentObj.id) {
												$scope.showUpdateToast();
											} else {
												$scope.showAddToast();
												$scope.stockShipmentObj = $scope
														.getEmptystockShipmentObj();
												$scope.stockShipmentAddForm
														.$setPristine();
												$scope.stockShipmentAddForm
														.$setValidity();
												$scope.stockShipmentAddForm
														.$setUntouched();
											}
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
						if (!$scope.stockShipmentObj.serviceLineItemList) {
							$scope.stockShipmentObj.serviceLineItemList = [];
						}
						$scope.stockShipmentObj.serviceLineItemList.push(item);
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
						if (!$scope.stockShipmentObj.productLineItemList) {
							$scope.stockShipmentObj.productLineItemList = [];
						}
						$scope.stockShipmentObj.productLineItemList.push(item);
					};

					$scope.productLineItemChanged = function(selectedLineItem) {
						selectedLineItem.price = selectedLineItem.stockItem.price;
						$scope.calProductSubTotal();
					};

					$scope.calProductSubTotal = function() {
						$log.debug("##Came to calSubTotal...");
						$scope.stockShipmentObj.productSubTotal = 0;

						for (var i = 0; i < $scope.stockShipmentObj.productLineItemList.length; i++) {
							var lineItem = $scope.stockShipmentObj.productLineItemList[i];
							$scope.stockShipmentObj.productSubTotal += (lineItem.qty * lineItem.price);
						}

						$scope.productTaxChanged();
					}

					$scope.serviceLineItemChanged = function(selectedLineItem) {
						selectedLineItem.price = selectedLineItem.stockItem.price;
						$scope.calServiceSubTotal();
					};

					$scope.calServiceSubTotal = function() {
						$scope.stockShipmentObj.serviceSubTotal = 0;

						for (var i = 0; i < $scope.stockShipmentObj.serviceLineItemList.length; i++) {
							var lineItem = $scope.stockShipmentObj.serviceLineItemList[i];
							$scope.stockShipmentObj.serviceSubTotal += (lineItem.qty * lineItem.price);
						}

						$scope.serviceTaxChanged();
					}

					$scope.serviceTaxChanged = function() {
						if (!$scope.stockShipmentObj.selectedServiceTax) {
							$scope.stockShipmentObj.serviceTaxTotal = 0;
						} else {
							$scope.stockShipmentObj.serviceTaxTotal = parseFloat(($scope.stockShipmentObj.selectedServiceTax.taxPercenatge / 100)
									* ($scope.stockShipmentObj.serviceSubTotal));
						}
						$scope.stockShipmentObj.serviceTotal = $scope.stockShipmentObj.serviceSubTotal
								+ $scope.stockShipmentObj.serviceTaxTotal;
						$scope.calfinalTotal();
					};

					$scope.productTaxChanged = function() {
						if (!$scope.stockShipmentObj.selectedProductTax) {
							$scope.stockShipmentObj.productTaxTotal = 0;

						} else {
							$scope.stockShipmentObj.productTaxTotal = parseFloat(($scope.stockShipmentObj.selectedProductTax.taxPercenatge / 100)
									* ($scope.stockShipmentObj.productSubTotal));
						}
						$scope.stockShipmentObj.productTotal = $scope.stockShipmentObj.productSubTotal
								+ $scope.stockShipmentObj.productTaxTotal;
						$scope.calfinalTotal();
					}

					$scope.calfinalTotal = function() {
						$scope.tempDiscAmount = 0;
						var finalTotal = 0;
						var disc = 0;
						$scope.stockShipmentObj.finalTotal = 0;

						if ($scope.stockShipmentObj.serviceTotal)
							finalTotal += parseFloat($scope.stockShipmentObj.serviceTotal);

						if ($scope.stockShipmentObj.productTotal)
							finalTotal += parseFloat($scope.stockShipmentObj.productTotal);

						$scope.stockShipmentObj.finalTotal = finalTotal;

						return;

						if ($scope.lineSelectedDiscount != undefined) {
							if ($scope.lineSelectedDiscount == "Fixed") {

								$scope.tempDiscAmount = $scope.discAmount;
								$scope.stockShipmentObj.discAmount = $scope.tempDiscAmount;

								if ($scope.stockShipmentObj.productTotal == undefined) {
									$scope.stockShipmentObj.finalTotal = parseFloat($scope.stockShipmentObj.serviceTotal)
											- parseFloat($scope.discAmount);
								} else {
									$scope.stockShipmentObj.finalTotal = (parseFloat($scope.stockShipmentObj.productTotal) + parseFloat($scope.stockShipmentObj.serviceTotal))
											- parseFloat($scope.discAmount);
								}

							} else {
								disc = parseInt($scope.discAmount);
								finalTotal = parseFloat($scope.stockShipmentObj.productSubTotal)
										+ parseFloat($scope.stockShipmentObj.productTaxTotal)
										+ parseFloat($scope.stockShipmentObj.serviceSubTotal);

								$scope.tempDiscAmount = ((disc / 100) * finalTotal);

								$scope.stockShipmentObj.discAmount = $scope.tempDiscAmount;

							}
							if ($scope.stockShipmentObj.productTotal == undefined) {
								$scope.stockShipmentObj.finalTotal = (parseFloat($scope.stockShipmentObj.serviceTotal))
										- parseFloat($scope.tempDiscAmount);
							} else {
								$scope.stockShipmentObj.finalTotal = (parseFloat($scope.stockShipmentObj.productTotal) + parseFloat($scope.stockShipmentObj.serviceTotal))
										- parseFloat($scope.tempDiscAmount);
							}
						}
					}

					$scope.removeServiceItem = function(index) {
						$scope.stockShipmentObj.serviceLineItemList.splice(
								index, 1);

						if ($scope.stockShipmentObj.serviceLineItemList.length == 0) {
							$scope.stockShipmentObj.serviceSubTotal = 0;
							$scope.stockShipmentObj.serviceTotal = 0;
							$scope.stockShipmentObj.serviceTaxTotal = 0;
							$scope.stockShipmentObj.selectedServiceTax = null;
						}

						$scope.calServiceSubTotal();
						$scope.calfinalTotal();
					};

					$scope.removeProductItem = function(index) {
						$scope.stockShipmentObj.productLineItemList.splice(
								index, 1);

						if ($scope.stockShipmentObj.productLineItemList.length == 0) {
							$scope.stockShipmentObj.productSubTotal = 0;
							$scope.stockShipmentObj.productTotal = 0;
							$scope.stockShipmentObj.productTaxTotal = 0;
							$scope.stockShipmentObj.selectedProductTax = null;
						}

						$scope.calProductSubTotal();
						$scope.calfinalTotal();
					};

					$scope.discountType = [ "%", "Fixed" ];
					$scope.lineItemDiscountChange = function(index,
							selectedDiscount) {
						$log.debug("##Came to lineItemStockChange...");
						$scope.lineSelectedDiscount = selectedDiscount;
						$scope.stockShipmentObj.discount = selectedDiscount;
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

					$scope.getAllStock = function() {
						$log.debug("Inside Ctr $scope.getAllStock");
						var stockService = appEndpointSF.getStockService();

						stockService.getAllStock($scope.curUser.business.id)
								.then(function(stockList) {
									$scope.stockItemList = stockList;
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

					$scope.getAllSalesOrder = function() {
						var salesService = appEndpointSF.getSalesOrderService();

						salesService.getAllSalesOrder(
								$scope.curUser.business.id).then(
								function(salesOrderList) {
									$scope.SOforinvoice = salesOrderList;
								});
					}

					$scope.SOforinvoice = [];

					$scope.getAllAccountsByBusiness = function() {
						var accountService = appEndpointSF.getAccountService();

						accountService.getAllAccountsByBusiness(
								$scope.curUser.business.id).then(
								function(accountList) {
									$scope.accountforinvoice = accountList;
								});
					}
					$scope.accountforinvoice = [];

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

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAllStock();
							$scope.getTaxesByVisibility();
							$scope.getAllWarehouseByBusiness();

							if (!$scope.stockShipmentObj.id) {
								$scope.addProductLineItem();
							}

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

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
