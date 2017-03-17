app = angular.module("stockApp");
app
		.controller(
				"purchaseOrderAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $q, $mdMedia, $mdDialog,
						objectFactory, appEndpointSF) {

					$scope.documentStatusList = [ 'DRAFT', 'SUBMITTED',
							'FINALIZED', 'REJECTED' ];

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.getEmptyPurchaseOrderObj = function() {
						return {
							warehouse : null,
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
							createdBy : $scope.curUser,
							approvedBy : null,
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
							status : 'DRAFT'
						};
					}

					$scope.createdBy = $scope.curUser.firstName + " "
							+ $scope.curUser.lastName;
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

					$scope.documentEntity = $stateParams.purchaseOrderObj ? $stateParams.purchaseOrderObj
							: $scope.getEmptyPurchaseOrderObj();

					$scope.documentEntity.poDueDate = $scope.documentEntity.poDueDate ? new Date(
							$scope.documentEntity.poDueDate)
							: new Date();

					$scope.saveDocument = function() {
						if (!$scope.documentEntity.serviceLineItemList
								&& !$scope.documentEntity.productLineItemList) {
							$scope.errorMsg = "Please select atleast one item.";
						} else {
							$scope.documentEntity.business = $scope.curUser.business;
							$scope.documentEntity.modifiedBy = $scope.curUser.email_id;
							$scope.documentEntity.discAmount = $scope.discAmount;
							var stockService = appEndpointSF.getStockService();
							stockService
									.addPurchaseOrder($scope.documentEntity)
									.then(
											function(entityObj) {
												if (entityObj.id) {
													$scope.documentEntity.id = entityObj.id;
													$scope.documentEntity.itemNumber = entityObj.itemNumber;
													$scope.showUpdateToast();

												}
											});
						}
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

					$scope.removeServices = function(toAddRemove) {
						if (toAddRemove) {
							$scope.settingsObj.showDefaultServiceItems = true;
							$scope.addServiceLineItem();
						} else {
							$scope.settingsObj.showDefaultServiceItems = false;
							$scope.documentEntity.serviceLineItemList = [];
						}
					};
					$scope.removeProducts = function(toAddRemove) {
						if (toAddRemove) {
							$scope.settingsObj.showDefaultProductItems = true;
							$scope.addProductLineItem();
						} else {
							$scope.settingsObj.showDefaultProductItems = false;
							$scope.documentEntity.productLineItemList = [];
						}
					};

					$scope.productLineItemChanged = function(selectedLineItem) {
						selectedLineItem.price = selectedLineItem.stockItem.price;
						$scope.calProductSubTotal();
					};

					$scope.calProductSubTotal = function() {
						$scope.documentEntity.productSubTotal = 0;
						if ($scope.documentEntity.productLineItemList) {
							for (var i = 0; i < $scope.documentEntity.productLineItemList.length; i++) {
								var lineItem = $scope.documentEntity.productLineItemList[i];
								$scope.documentEntity.productSubTotal += (lineItem.qty * lineItem.price);
							}

							$scope.productTaxChanged();
						}
					}

					$scope.serviceLineItemChanged = function(selectedLineItem) {
						selectedLineItem.cost = selectedLineItem.stockItem.cost;
						$scope.calServiceSubTotal();
					};

					$scope.calServiceSubTotal = function() {
						$scope.documentEntity.serviceSubTotal = 0;
						if ($scope.documentEntity.serviceLineItemList) {
							for (var i = 0; i < $scope.documentEntity.serviceLineItemList.length; i++) {
								var lineItem = $scope.documentEntity.serviceLineItemList[i];
								$scope.documentEntity.serviceSubTotal += (lineItem.qty * lineItem.cost);
							}

							$scope.serviceTaxChanged();
						}
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

					$scope.printPO = function(poId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfPurchaseOrder?bid=" + bid
								+ "&poId=" + poId);
					}

					$scope.showSimpleToastError = function() {
						$mdToast.show($mdToast.simple().content(
								'Stock not sufficient!').position("right")
								.hideDelay(10000));
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

					$scope.getTaxesByVisibility = function() {
						var taxService = appEndpointSF.getTaxService();

						taxService.getTaxesByVisibility(
								$scope.curUser.business.id).then(
								function(taxList) {
									$scope.taxforinvoice = taxList;
								});
					}
					$scope.taxData = [];

					$scope.getStockSettingsByBiz = function() {
						var stockService = appEndpointSF.getStockService();
						stockService
								.getStockSettingsByBiz(
										$scope.curUser.business.id)
								.then(
										function(settingsList) {
											$scope.settingsObj = settingsList;
											$scope.documentEntity.noteToCustomer = $scope.settingsObj.noteToCustomer;
											if ($scope.settingsObj.showDefaultServiceItems
													&& !$scope.documentEntity.id) {
												$scope.addServiceLineItem();
											}
											if ($scope.settingsObj.showDefaultProductItems
													&& !$scope.documentEntity.id) {
												$scope.addProductLineItem();
											}

											if ($scope.settingsObj.linkPOToBudget) {
												$scope.budgetList = [];
												var pagingInfoTemp = {
													entityList : null,
													startPage : 1,
													limit : 1000,
													totalEntities : 0,
													webSafeCursorString : null
												};
												var selectedStatus = "SUBMITTED";
												var stockService = appEndpointSF
														.getStockService();
												stockService
														.fetchBudgetListByPaging(
																$scope.curUser.business.id,
																selectedStatus,
																pagingInfoTemp)
														.then(
																function(
																		pagingInfoReturned) {
																	$scope.budgetList = pagingInfoReturned.entityList;
																});
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

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							loadAllSuppliers();
							$scope.getAllWarehouseByBusiness();
							$scope.getStockSettingsByBiz();
							$scope.getStockItemTypes();
							$scope.getTaxesByVisibility();
							$scope.calProductSubTotal();
							$scope.calServiceSubTotal();

							if (!$scope.documentEntity.id) {
								$scope.addProductLineItem();
							}

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					// For Add Stock from Invoice Page through popup
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

					function addStockItemDialogController($scope, $mdDialog,
							curUser, stock, warehouses, stockItemList,
							lineItem, calProductSubTotalFn) {
						$scope.warehouses = warehouses;
						$scope.addStock = function() {
							$scope.stock.business = curUser.business;
							$scope.stock.createdDate = new Date();
							$scope.stock.modifiedBy = curUser.email_id;
							var stockService = appEndpointSF.getStockService();
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
												purchaseOrderObj : $scope.documentEntity
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