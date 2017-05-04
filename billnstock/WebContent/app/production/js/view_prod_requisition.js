var app = angular.module("stockApp");
app
		.controller(
				"view_prod_requisition",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia) {

					function reSetQuery() {
						return {
							order : '-itemNumber',
							limit : 50,
							page : 1,
							totalSize : 0,
							pagesLoaded : 0
						};
					}
					$scope.query = reSetQuery();
					$scope.isActionhidden = false;
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.productionRequisition = $stateParams.productionRequisition ? $stateParams.productionRequisition
							: null;

					if ($stateParams.productionRequisition != null) {
						$scope.productionRequisition.deliveryDate = new Date(
								$stateParams.productionRequisition.deliveryDateTime);
						$scope.productionRequisition.deliverytime = new Date(
								$stateParams.productionRequisition.deliveryDateTime);
					}

					$scope.stockItemCategories = [];
					$scope.stockTypeList = [];
					var dummyStockTypeList = [];

					$scope.addProdStockIsssueRecord = function(ev, prodStock) {

						var useFullScreen = $mdMedia('xs');
						$mdDialog
								.show(
										{
											controller : stockIssueCtr,
											templateUrl : '/app/production/add_prod_stock_issue_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
												prodStock : prodStock,
												productionRequisition : $scope.productionRequisition,
												showAddToast : $scope.showAddToast
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

					}

					function stockIssueCtr($scope, $mdDialog, curUser,
							prodStock, productionRequisition, showAddToast) {

						$scope.loading = true;
						
						$scope.getEmptyObj = function() {
							return {
								productLineItemList : [],
								catList : angular
										.copy(productionRequisition.catList),
								reqItemNumber : productionRequisition.itemNumber,
								createdDate : new Date(),
								createdBy : curUser,
								business : curUser.business,
								status : 'DRAFT'
							};
						}

						$scope.productionRequisition = productionRequisition;

						$scope.documentEntity = prodStock ? prodStock : $scope
								.getEmptyObj();

						if ($scope.documentEntity.id) {
							$scope.documentEntity.deliveryDateTime = new Date(
									$scope.documentEntity.deliveryDateTime);
						}

						$scope.removeWarehouseLineItem = function(index) {
							$scope.documentEntity.fromWarehouseList.splice(
									index, 1);
						}

						$scope.addWarehouseLineItem = function() {
							var fromWarehouse = {
								fromWH : null,
								catList : angular
										.copy(productionRequisition.catList),
							};

							if (!$scope.documentEntity.fromWarehouseList) {
								$scope.documentEntity.fromWarehouseList = [];
							}
							$scope.documentEntity.fromWarehouseList
									.push(fromWarehouse);

							// $scope.addCatogory(fromWarehouse);
						};

						$scope.addCatogory = function(fromWarehouse) {
							var category = {
								cat : null,
								items : []
							};

							if (!$scope.documentEntity.catList) {
								fromWarehouse.catList = [];
							}
							fromWarehouse.catList.push(category);
							$scope.addLineItem(category);
						}

						$scope.addLineItem = function(category) {
							// $scope.getStockItemTypes (category)

							var itemObj = {
								itemName : "",
								qty : ""
							};

							if (!category.items) {
								category.items = [];
							}
							category.items.push(itemObj);
						};

						$scope.saveDocument = function() {
							$scope.documentEntity.business = curUser.business;
							$scope.documentEntity.modifiedBy = curUser.email_id;

							var wasUpdate = null;
							if ($scope.documentEntity.id) {
								$scope.isActionhidden = true;
								wasUpdate = true;
							} else {
								wasUpdate = false;
							}

							var stockService = appEndpointSF.getStockService();

							stockService
									.addProductionStockIssueList(
											$scope.documentEntity)
									.then(
											function(savedObj) {
												
												if (wasUpdate) {
													$mdDialog.cancel();
													showAddToast();
												} else {
													$scope.isActionhidden = true;
													if (!productionRequisition.stockShipmentList) {
														productionRequisition.stockShipmentList = [];
													}
													productionRequisition.stockShipmentList
															.push(savedObj);

													var productService = appEndpointSF
															.getProductionService();

													productService
															.addRequisition(
																	productionRequisition)
															.then(
																	function() {
																		$mdDialog
																				.cancel();
																		showAddToast();
																	});
												}
											});

						}

						$scope.getAllWarehouseByBusiness = function() {
							$scope.loading = true;
							var warehouseService = appEndpointSF
									.getWarehouseManagementService();

							warehouseService.getAllWarehouseByBusiness(
									curUser.business.id).then(
									function(warehouseList) {
										$scope.warehouses = warehouseList;
										if (!$scope.documentEntity.id) {
											$scope.addWarehouseLineItem();
										}
										$scope.loading = false;
									});
						}

						$scope.filterStockItemsByBomTypes = function(
								warehouseLineItem) {
							if (warehouseLineItem.fromWH
									&& $scope.productionRequisition.bomEntity
									&& !warehouseLineItem.fromWH.warehouseStockItemList) {
								var stockService = appEndpointSF
										.getStockService();
								stockService
										.filterStockItemsByBomTypes(
												{
													'warehouse' : warehouseLineItem.fromWH,
													'bomEntity' : $scope.productionRequisition.bomEntity
												})
										.then(
												function(stockList) {
													warehouseLineItem.fromWH.warehouseStockItemList = stockList;
												});
							}
						}

						$scope.getCurrentWHStockItemQty = function(
								warehouseLineItem, stockItemType) {
							var totalStockQty = 0;
							if (!stockItemType || !warehouseLineItem.fromWH) {
								return 'NA';
							}

							if (!warehouseLineItem.fromWH.warehouseStockItemList) {
								return totalStockQty;
							}
							angular
									.forEach(
											warehouseLineItem.fromWH.warehouseStockItemList,
											function(stockItem) {
												if (stockItem.stockItemType.id == stockItemType.id)
													totalStockQty += stockItem.qty;
											});
							return totalStockQty;
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
							$scope.documentEntity.status = 'FINALIZED';
							$scope.saveDocument();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};
						$scope.getAllWarehouseByBusiness();
					}
					
					$scope.printStockIssueList = function(prodRequisitionId,prodStockIssueId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintStockIssuedListPdf?bid=" + bid + "&prodRequisitionId=" + prodRequisitionId + "&prodStockIssueId=" + prodStockIssueId);
					}
				});