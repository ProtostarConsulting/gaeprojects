var app = angular.module("stockApp");
app
		.controller(
				"view_prod_requisition",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia) {

					$scope.loading = true;
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

						$scope.getEmptyObj = function() {
							return {
								productLineItemList : [],
								catList : productionRequisition.catList,
								reqItemNumber : productionRequisition.itemNumber,
								createdDate : new Date(),
								createdBy : curUser,
								business : curUser.business,
								status : 'DRAFT'
							};
						}

						$scope.documentEntity = prodStock ? prodStock : $scope
								.getEmptyObj();

						if ($scope.documentEntity.id) {
							$scope.documentEntity.deliveryDateTime = new Date(
									$scope.documentEntity.deliveryDateTime);
						}

						$scope.addCatogory = function() {
							var category = {
								cat : null,
								items : [],
								categoryStockTypeList : []
							};

							if (!$scope.documentEntity.catList) {
								$scope.documentEntity.catList = [];
							}
							$scope.documentEntity.catList.push(category);
							$scope.addLineItem(category);
						}

						$scope.addLineItem = function(category) {
							// $scope.getStockItemTypes (category)

							var itemObj = {
								itemName : "",
								price : 0,
								qty : "",
								currentBudgetBalance : 0
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
										$scope.loading = false;
									});
						}
						$scope.getAllWarehouseByBusiness();

						$scope.draftDocumnent = function(ev) {
							$scope.documentEntity.status = 'DRAFT';
							$scope.saveDocument();
						}

						$scope.submitDocumnent = function(ev) {
							$scope.documentEntity.status = 'SUBMITTED';
							$scope.saveDocument();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};
					}

				});