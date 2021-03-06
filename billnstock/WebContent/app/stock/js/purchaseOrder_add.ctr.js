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
					$scope.isSaving = false;
					$scope.getEmptyPurchaseOrderObj = function() {
						return {
							warehouse : null,
							supplier : '',
							to : '',
							shipTo : '',
							poDate : new Date(),
							poDueDate : new Date(),
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
							indiviualServiceLineItemTax : false,
							indiviualProductLineItemTax : false,
							paidDate : null,
							business : null,
							status : 'DRAFT'
						};
					}

					$scope.createdBy = $scope.curUser.firstName + " "
							+ $scope.curUser.lastName;
					// list of `state` value/display objects
					$scope.supplierList = [];
					$scope.stockItemOrderTypes = [];
					$scope.searchTextInput = null;

					$scope.querySearch = function(query) {
						var autoCompleteUIService = appEndpointSF
								.getAutoCompleteUIService();
						return autoCompleteUIService.querySupplierSearch(
								$scope.supplierList, query);
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
							$scope.isSaving = true;
							$scope.documentEntity.business = $scope.curUser.business;
							$scope.documentEntity.modifiedBy = $scope.curUser.email_id;
							$scope.documentEntity.discAmount = $scope.discAmount;
							var stockService = appEndpointSF.getStockService();
							stockService
									.addPurchaseOrder($scope.documentEntity)
									.then(
											function(entityObj) {
												if (entityObj.id) {
													$scope.isSaving = false;
													$scope.documentEntity.id = entityObj.id;
													$scope.documentEntity.itemNumber = entityObj.itemNumber;
													$scope.showUpdateToast();

												}
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

					$scope.today = new Date();

					$scope.toggleCloseBtn = function() {

						if ($scope.documentEntity.poDueDate.getTime() === $scope.today
								.getTime()
								|| $scope.documentEntity.poDueDate.getTime() > $scope.today
										.getTime()) {
							$scope.closeBtnHide = true;
						} else if ($scope.documentEntity.poDueDate.getTime() < $scope.today
								.getTime()) {
							$scope.closeBtnHide = false;
						}

					}

					$scope.closeDocument = function(ev) {

						$scope.documentEntity.status = 'CLOSED';
						var stockService = appEndpointSF.getStockService();
						stockService.updatePurchaseOrder($scope.documentEntity)
								.then(function() {
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
						// There is no discount in PO.
						$scope.documentEntity.serviceDiscAmount = 0;
						$scope.documentEntity.productDiscAmount = 0

						$scope.serviceTaxChanged();
						$scope.productTaxChanged();
						$scope.calServiceSubTotal();
						$scope.calProductSubTotal();

						// calculateDiscountAmount();
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
								if (lineItem.stockItem.stockItemType.withAdditionalExciseTax) {
									$scope.documentEntity.productSubTotal += (lineItem.qty * lineItem.price)
											+ ((lineItem.qty * lineItem.price) * (lineItem.stockItem.stockItemType.exciseTaxPercenatge / 100));
								} else {
									$scope.documentEntity.productSubTotal += (lineItem.qty * lineItem.price);
								}

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
								$scope.documentEntity.serviceSubTotal += (lineItem.qty * lineItem.cost);
							}
						}
					}

					$scope.serviceTaxChanged = function() {

						$scope.taxAmtsForServiceLineItems = [];
						if ($scope.documentEntity.indiviualServiceLineItemTax) {
							$scope.documentEntity.selectedServiceTax = null;
							if ($scope.documentEntity.serviceLineItemList
									&& $scope.documentEntity.serviceLineItemList.length > 0) {
								for (var i = 0; i < $scope.documentEntity.serviceLineItemList.length; i++) {
									var lineItem = $scope.documentEntity.serviceLineItemList[i];
									if (lineItem.selectedTaxItem) {
										lineItem.taxAmt = (lineItem.qty * lineItem.cost)
												* (lineItem.selectedTaxItem.taxPercenatge / 100);
										$scope.taxAmtsForServiceLineItems
												.push(lineItem.taxAmt);
									}

								}
								$scope.documentEntity.serviceTaxTotal = 0;
								for (var j = 0; j < $scope.taxAmtsForServiceLineItems.length; j++) {
									$scope.documentEntity.serviceTaxTotal += parseFloat($scope.taxAmtsForServiceLineItems[j]);
								}
								$scope.documentEntity.serviceTotal = ($scope.documentEntity.serviceSubTotal - $scope.documentEntity.serviceDiscAmount)
										+ $scope.documentEntity.serviceTaxTotal;
							}

						} else {
							if ($scope.documentEntity.serviceLineItemList
									&& $scope.documentEntity.serviceLineItemList.length > 0) {
								for (var i = 0; i < $scope.documentEntity.serviceLineItemList.length; i++) {
									$scope.documentEntity.serviceLineItemList[i].selectedTaxItem = null;
								}
							}
							if (!$scope.documentEntity.selectedServiceTax) {
								$scope.documentEntity.serviceTaxTotal = 0;
							} else {
								$scope.documentEntity.serviceTaxTotal = parseFloat(($scope.documentEntity.selectedServiceTax.taxPercenatge / 100)
										* ($scope.documentEntity.serviceSubTotal - $scope.documentEntity.serviceDiscAmount));
							}
							$scope.documentEntity.serviceTotal = ($scope.documentEntity.serviceSubTotal - $scope.documentEntity.serviceDiscAmount)
									+ $scope.documentEntity.serviceTaxTotal;
						}
					};

					$scope.productTaxChanged = function() {

						$scope.taxAmtsForProductLineItems = [];
						if ($scope.documentEntity.indiviualProductLineItemTax) {
							$scope.documentEntity.selectedProductTax = null;
							if ($scope.documentEntity.productLineItemList
									&& $scope.documentEntity.productLineItemList.length > 0) {
								for (var i = 0; i < $scope.documentEntity.productLineItemList.length; i++) {
									var lineItem = $scope.documentEntity.productLineItemList[i];
									lineItem.taxAmt = 0;
									if (lineItem.stockItem.stockItemType.withAdditionalExciseTax) {
										if (lineItem.selectedTaxItem)
											lineItem.taxAmt = ((lineItem.qty * lineItem.price) + ((lineItem.stockItem.stockItemType.exciseTaxPercenatge / 100)
													* lineItem.qty * lineItem.price))
													* (lineItem.selectedTaxItem.taxPercenatge / 100);
									} else {
										if (lineItem.selectedTaxItem)
											lineItem.taxAmt = (lineItem.qty * lineItem.price)
													* (lineItem.selectedTaxItem.taxPercenatge / 100);

									}

									$scope.taxAmtsForProductLineItems
											.push(lineItem.taxAmt);
								}
								$scope.documentEntity.productTaxTotal = 0;
								for (var j = 0; j < $scope.taxAmtsForProductLineItems.length; j++) {
									$scope.documentEntity.productTaxTotal += parseFloat($scope.taxAmtsForProductLineItems[j]);
								}
								$scope.documentEntity.productTotal = ($scope.documentEntity.productSubTotal - $scope.documentEntity.productDiscAmount)
										+ $scope.documentEntity.productTaxTotal;
							}

						} else {
							if ($scope.documentEntity.productLineItemList
									&& $scope.documentEntity.productLineItemList.length > 0) {
								for (var i = 0; i < $scope.documentEntity.productLineItemList.length; i++) {
									$scope.documentEntity.productLineItemList[i].selectedTaxItem = null;
								}
							}
							if (!$scope.documentEntity.selectedProductTax) {
								$scope.documentEntity.productTaxTotal = 0;

							} else {
								$scope.documentEntity.productTaxTotal = parseFloat(($scope.documentEntity.selectedProductTax.taxPercenatge / 100)
										* ($scope.documentEntity.productSubTotal - $scope.documentEntity.productDiscAmount));
							}
							$scope.documentEntity.productTotal = ($scope.documentEntity.productSubTotal - $scope.documentEntity.productDiscAmount)
									+ $scope.documentEntity.productTaxTotal;
						}
					};

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

					$scope.stockItemTypeQuerySearch = function(query) {
						var autoCompleteUIService = appEndpointSF
								.getAutoCompleteUIService();
						return autoCompleteUIService.queryStockItemTypeSearch(
								$scope.stockTypeList, query);
					}

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
									$scope.taxList = taxList;
									if (!$scope.taxList) {
										$scope.taxList = [];
									}
									$scope.taxList.splice(0, 0, null);
								});
					}
					$scope.taxData = [];

					$scope.getStockSettingsByBiz = function() {
						var stockService = appEndpointSF.getStockService();
						stockService
								.getStockSettingsByBiz(
										$scope.curUser.business.id)
								.then(
										function(settingsObj) {
											$scope.settingsObj = settingsObj;
											if (!$scope.documentEntity.id) {
												$scope.documentEntity.billTo = $scope.settingsObj.poBillTo;
												$scope.documentEntity.shipTo = $scope.settingsObj.poShipTo;
												$scope.documentEntity.termsAndConditions = $scope.settingsObj.poTermsAndConditions;
											}
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

					$scope.getStockItemOrderTypeList = function() {

						var stockService = appEndpointSF.getStockService();

						stockService
								.getStockItemOrderTypes(
										$scope.curUser.business.id)
								.then(
										function(stockItemOrderTypes) {
											$scope.stockItemOrderTypes = stockItemOrderTypes;
											if ($scope.stockItemOrderTypes.length > 0
													&& !$scope.documentEntity.orderType) {
												$scope.documentEntity.orderType = $scope.stockItemOrderTypes[0];
											}
										})
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
							$scope.getStockItemOrderTypeList();
							$scope.toggleCloseBtn();
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
												documentEntity : $scope.documentEntity
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
							curBusi, curUser, supplierList, documentEntity) {
						$scope.addSupplier = function() {
							$scope.supplier.business = curUser.business;
							$scope.supplier.createdDate = new Date();
							$scope.supplier.modifiedBy = curUser.email_id;
							var supplierService = appEndpointSF
									.getSupplierService();

							supplierService.addSupplier($scope.supplier).then(
									function(supplierObj) {
										supplierList.push(supplierObj);
										documentEntity.supplier = supplierObj;
									});
							$scope.cancel();
							// window.history.back();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};
					}

					$scope.addDocumentComment = function(ev, documentEntity,
							editComment) {

						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : addDocumentCommentCtr,
											templateUrl : '/app/stock/purchase_order_add_comment_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
												documentEntity : $scope.documentEntity,
											// taskService : taskService
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

						function addDocumentCommentCtr($scope, $mdDialog,
								curUser, documentEntity) {

							if (!documentEntity.documentComments) {
								documentEntity.documentComments = [];
							}

							$scope.documentComment = {
								addedBy : curUser,
								date : new Date(),
								commentText : ''
							};

							if (editComment) {
								$scope.documentComment = angular
										.copy(editComment);
							}

							$scope.addComment = function() {

								var stockService = appEndpointSF
										.getStockService();
								stockService
										.updatePurchaseOrder(documentEntity)
										.then(
												function(returnedObj) {
													if (returnedObj.id) {
														if (editComment)
															editComment.commentText = $scope.documentComment.commentText;
														else
															documentEntity.documentComments
																	.push($scope.documentComment);
													}
													$mdDialog.cancel();
												});

							}

							$scope.cancel = function() {
								$mdDialog.cancel();
							};
						}

					};

					$scope.viewReceiptsAgainstPo = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : viewReceiptsAgainstPOCtr,
											templateUrl : '/app/stock/view_stock_receipts_against_po_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												purchaseOrder : $scope.documentEntity
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

					function viewReceiptsAgainstPOCtr($scope, $mdDialog,
							purchaseOrder) {

						$scope.purchaseOrder = purchaseOrder;
						$scope.pOItemNo = $scope.purchaseOrder.itemNumber;
						$scope.stockReceipts = [];

						$scope.viewStockReceipts = function() {

							var stockService = appEndpointSF.getStockService();

							stockService.getStockReceiptsAgainstPO(
									$scope.purchaseOrder).then(
									function(stockReceipts) {

										$scope.stockReceipts = stockReceipts;
									});

							// $scope.cancel();
							// window.history.back();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};

						$scope.viewStockReceipts();

					}

					$scope.viewPurchaseOrderReport = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : viewPurchaseOrderReportCtr,
											templateUrl : '/app/stock/view_po_report_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												purchaseOrder : $scope.documentEntity
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

					function viewPurchaseOrderReportCtr($scope, $mdDialog,
							purchaseOrder) {

						$scope.purchaseOrder = purchaseOrder;
						$scope.pOItemNo = $scope.purchaseOrder.itemNumber;

						$scope.getReport = function() {

							var stockService = appEndpointSF.getStockService();

							stockService
									.getPOReport($scope.purchaseOrder)
									.then(
											function(purchaseOrderReport) {

												$scope.purchaseOrderReport = purchaseOrderReport;

											});

							// $scope.cancel();
							// window.history.back();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};

						$scope.getReport();

						$scope.printPOReport = function(divId) {

							var printDivCSS = new String(
									'<link href="/lib/base/css/angular-material.min.css"" rel="stylesheet" type="text/css">'
											+ '<link href="/lib/base/css/bootstrap.min.css"" rel="stylesheet" type="text/css">')

							window.frames["print_frame"].document.body.innerHTML = printDivCSS
									+ document.getElementById(divId).innerHTML;
							window.frames["print_frame"].window.focus();

							window.frames["print_frame"].window.print();

						};

					}
				});