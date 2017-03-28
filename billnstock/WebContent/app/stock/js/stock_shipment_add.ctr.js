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

					$scope.shipmentTypes = [ 'TO_CUSTOMER',
							'TO_OTHER_WAREHOUSE', 'TO_PARTNER' ];

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
							business : null,
							status : 'DRAFT',
							createdBy : $scope.curUser,
							createdDate : new Date(),
							modifiedDate : new Date()
						};
					}

					$scope.documentEntity = $stateParams.stockShipmentObj ? $stateParams.stockShipmentObj
							: $scope.getEmptystockShipmentObj();

					$scope.documentEntity.shipmentDate = $scope.documentEntity.shipmentDate ? new Date(
							$scope.documentEntity.shipmentDate)
							: new Date();
					$scope.documentEntity.poNumber = $scope.documentEntity.poNumber ? Number($scope.documentEntity.poNumber)
							: '';

					$scope.saveDocument = function() {
						$scope.documentEntity.business = $scope.curUser.business;
						$scope.documentEntity.modifiedBy = $scope.curUser.email_id;
						// $scope.documentEntity.createdBy = $scope.curUser;
						// Commenting this line. It will override the createdBy
						// when someone approves/rejects it.

						var stockService = appEndpointSF.getStockService();

						stockService
								.addStockShipment($scope.documentEntity)
								.then(
										function(entityObj) {
											if (entityObj.id) {
												$scope.documentEntity.id = entityObj.id;
												$scope.documentEntity.itemNumber = entityObj.itemNumber;
												$scope.showUpdateToast();
											}
										});

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
							stockItem : null,
							selectedTaxItem : null
						};
						if (!$scope.documentEntity.productLineItemList) {
							$scope.documentEntity.productLineItemList = [];
						}
						$scope.documentEntity.productLineItemList.push(item);
					};

					$scope.productLineItemChanged = function(selectedLineItem) {
						selectedLineItem.price = selectedLineItem.stockItem.price;
						$scope.calProductSubTotal();
					};

					$scope.calProductSubTotal = function() {
						$log.debug("##Came to calSubTotal...");
						$scope.documentEntity.productSubTotal = 0;

						for (var i = 0; i < $scope.documentEntity.productLineItemList.length; i++) {
							var lineItem = $scope.documentEntity.productLineItemList[i];
							$scope.documentEntity.productSubTotal += (lineItem.qty * lineItem.price);
						}

						$scope.productTaxChanged();
					}

					$scope.serviceLineItemChanged = function(selectedLineItem) {
						selectedLineItem.price = selectedLineItem.stockItem.price;
						$scope.calServiceSubTotal();
					};

					$scope.calServiceSubTotal = function() {
						$scope.documentEntity.serviceSubTotal = 0;

						for (var i = 0; i < $scope.documentEntity.serviceLineItemList.length; i++) {
							var lineItem = $scope.documentEntity.serviceLineItemList[i];
							$scope.documentEntity.serviceSubTotal += (lineItem.qty * lineItem.price);
						}

						$scope.serviceTaxChanged();
					}

					$scope.serviceTaxChanged = function() {
						if (!$scope.documentEntity.selectedServiceTax) {
							$scope.documentEntity.serviceTaxTotal = 0;
						} else {
							$scope.documentEntity.serviceTaxTotal = parseFloat(($scope.documentEntity.selectedServiceTax.taxPercenatge / 100)
									* ($scope.documentEntity.serviceSubTotal));
						}
						$scope.documentEntity.serviceTotal = $scope.documentEntity.serviceSubTotal
								+ $scope.documentEntity.serviceTaxTotal;
						$scope.calfinalTotal();
					};

					$scope.productTaxChanged = function() {
						if (!$scope.documentEntity.selectedProductTax) {
							$scope.documentEntity.productTaxTotal = 0;

						} else {
							$scope.documentEntity.productTaxTotal = parseFloat(($scope.documentEntity.selectedProductTax.taxPercenatge / 100)
									* ($scope.documentEntity.productSubTotal));
						}
						$scope.documentEntity.productTotal = $scope.documentEntity.productSubTotal
								+ $scope.documentEntity.productTaxTotal;
						$scope.calfinalTotal();
					}

					$scope.calfinalTotal = function() {
						$scope.tempDiscAmount = 0;
						var finalTotal = 0;
						var disc = 0;
						$scope.documentEntity.finalTotal = 0;

						if ($scope.documentEntity.serviceTotal)
							finalTotal += parseFloat($scope.documentEntity.serviceTotal);

						if ($scope.documentEntity.productTotal)
							finalTotal += parseFloat($scope.documentEntity.productTotal);

						$scope.documentEntity.finalTotal = finalTotal;

						return;

						if ($scope.lineSelectedDiscount != undefined) {
							if ($scope.lineSelectedDiscount == "Fixed") {

								$scope.tempDiscAmount = $scope.discAmount;
								$scope.documentEntity.discAmount = $scope.tempDiscAmount;

								if ($scope.documentEntity.productTotal == undefined) {
									$scope.documentEntity.finalTotal = parseFloat($scope.documentEntity.serviceTotal)
											- parseFloat($scope.discAmount);
								} else {
									$scope.documentEntity.finalTotal = (parseFloat($scope.documentEntity.productTotal) + parseFloat($scope.documentEntity.serviceTotal))
											- parseFloat($scope.discAmount);
								}

							} else {
								disc = parseInt($scope.discAmount);
								finalTotal = parseFloat($scope.documentEntity.productSubTotal)
										+ parseFloat($scope.documentEntity.productTaxTotal)
										+ parseFloat($scope.documentEntity.serviceSubTotal);

								$scope.tempDiscAmount = ((disc / 100) * finalTotal);

								$scope.documentEntity.discAmount = $scope.tempDiscAmount;

							}
							if ($scope.documentEntity.productTotal == undefined) {
								$scope.documentEntity.finalTotal = (parseFloat($scope.documentEntity.serviceTotal))
										- parseFloat($scope.tempDiscAmount);
							} else {
								$scope.documentEntity.finalTotal = (parseFloat($scope.documentEntity.productTotal) + parseFloat($scope.documentEntity.serviceTotal))
										- parseFloat($scope.tempDiscAmount);
							}
						}
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

						$scope.calServiceSubTotal();
						$scope.calfinalTotal();
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

						$scope.calProductSubTotal();
						$scope.calfinalTotal();
					};

					$scope.discountType = [ "%", "Fixed" ];
					$scope.lineItemDiscountChange = function(index,
							selectedDiscount) {
						$log.debug("##Came to lineItemStockChange...");
						$scope.lineSelectedDiscount = selectedDiscount;
						$scope.documentEntity.discount = selectedDiscount;
						// $scope.calSubTotal();
						// $scope.calfinalTotal();
					};

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
											if ($scope.documentEntity.fromWH == null
													&& $scope.warehouses.length > 0) {
												$scope.documentEntity.fromWH = $scope.warehouses[0];
											}
											$scope
													.filterStockItemsByWarehouse($scope.documentEntity.fromWH);
											$scope.loading = false;
										});
					}

					$scope.querySearch = function(query) {
						var autoCompleteUIService = appEndpointSF
								.getAutoCompleteUIService();
						return autoCompleteUIService.queryCustomerSearch(
								$scope.customerList, query);
					}
					/**
					 * Build `states` list of key/value pairs
					 */
					function loadAllCustomers() {
						$scope.loading = true;
						var customerService = appEndpointSF
								.getCustomerService();
						customerService.getAllCustomersByBusiness(
								$scope.curUser.business.id).then(
								function(custList) {
									$scope.customerList = custList.items;
									$scope.loading = false;
								});

					}
					

					function getStockSettingsByBiz() {
						var stockService = appEndpointSF.getStockService();
						stockService.getStockSettingsByBiz(
								$scope.curUser.business.id).then(
								function(settingsList) {
									$scope.settingsObj = settingsList;
								});
					}

					$scope.printstockShipment = function(stShipId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfstockShipment?bid=" + bid
								+ "&stShipId=" + stShipId);

					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							getStockSettingsByBiz();
							loadAllCustomers();
							$scope.getTaxesByVisibility();
							$scope.getAllWarehouseByBusiness();
							$scope.calProductSubTotal();
							if ($scope.documentEntity.serviceLineItemList) {
								$scope.calServiceSubTotal();
							}
							if (!$scope.documentEntity.id) {
								$scope.addProductLineItem();
							}

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					function callMe() {
						$log.debug("called me...");
					}

					$scope.addSerialNumbers = function(ev, stockLineItem) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : addSerialNumbersDialogController,
											templateUrl : '/app/stock/shipment_stock_serialnumbers_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curBusi : $scope.curUser.business,
												curUser : $scope.curUser,
												stockLineItem : stockLineItem,
												callMe : callMe
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
							$mdDialog, curBusi, curUser, stockLineItem, callMe) {
						Array.prototype.diff = function(a) {
							return this.filter(function(i) {
								return a.indexOf(i) < 0;
							});
						};

						$scope.stockLineItem = stockLineItem;
						function getOrCreateStockItemInstance(index, list) {
							if (index < list.length) {
								return list[index];
							} else {
								return {
									business : curBusi,
									stockItem : stockLineItem.stockItem,
									modifiedBy : curUser.email_id,
									serialNumber : '',
									note : ''
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

						$scope.instanceListByStockItem = [];
						var stockService = appEndpointSF.getStockService();
						stockService
								.getStockItemInstancesList(
										stockLineItem.stockItem)
								.then(
										function(stockItemInstanceList) {
											$scope.instanceListByStockItem = stockItemInstanceList;
										});

						$scope.updateSerialNumbers = function() {
							$scope.cancel();
						}

						$scope.querySearch = function(query) {
							var diffArray = $scope.instanceListByStockItem
									.diff($scope.stockLineItem.stockItemInstanceList);
							var results = query ? diffArray
									.filter(createFilterFor(query)) : [];
							var deferred = $q.defer();
							$timeout(function() {
								deferred.resolve(results);
							}, Math.random() * 1000, false);
							return deferred.promise;
						}

						/**
						 * Create filter function for a query string
						 */
						function createFilterFor(query) {
							var lowercaseQuery = angular.lowercase(query);
							return function filterFn(instanceItem) {
								var a = instanceItem.serialNumber;
								return (angular.lowercase(a).indexOf(
										lowercaseQuery) >= 0);
							};
						}

						$scope.cancel = function() {
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