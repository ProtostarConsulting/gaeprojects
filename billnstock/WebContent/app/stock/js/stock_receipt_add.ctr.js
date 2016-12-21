app = angular.module("stockApp");
app
		.controller(
				"stockReceiptAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $mdMedia, $mdDialog, $q,
						objectFactory, appEndpointSF) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.getEmptyStockReceiptObj = function() {
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
											if ($scope.stockReceiptObj.id) {
												$scope.showUpdateToast();
											} else {
												$scope.showAddToast();
												$scope.stockReceiptObj = $scope
														.getEmptyStockReceiptObj();
												$scope.stockReceiptAddForm
														.$setPristine();
												$scope.stockReceiptAddForm
														.$setValidity();
												$scope.stockReceiptAddForm
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
							cost : "",
							stockItem : null,
							selectedTaxItem : null
						};
						if (!$scope.stockReceiptObj.serviceLineItemList) {
							$scope.stockReceiptObj.serviceLineItemList = [];
						}
						$scope.stockReceiptObj.serviceLineItemList.push(item);
					};

					$scope.addProductLineItem = function() {
						var item = {
							isProduct : true,
							itemName : "",
							qty : 1,
							price : "",
							cost : "",							
							stockItem : null,
							selectedTaxItem : null
						};
						if (!$scope.stockReceiptObj.productLineItemList) {
							$scope.stockReceiptObj.productLineItemList = [];
						}
						$scope.stockReceiptObj.productLineItemList.push(item);
					};

					$scope.productLineItemChanged = function(selectedLineItem) {
						selectedLineItem.cost = selectedLineItem.stockItem.cost;
						$scope.calProductSubTotal();
					};

					$scope.calProductSubTotal = function() {
						$log.debug("##Came to calSubTotal...");
						$scope.stockReceiptObj.productSubTotal = 0;

						for (var i = 0; i < $scope.stockReceiptObj.productLineItemList.length; i++) {
							var lineItem = $scope.stockReceiptObj.productLineItemList[i];
							$scope.stockReceiptObj.productSubTotal += (lineItem.qty * lineItem.cost);
						}

						$scope.productTaxChanged();
					}

					$scope.serviceLineItemChanged = function(selectedLineItem) {
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

							if (!$scope.stockReceiptObj.id) {
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