angular
		.module("stockApp")
		.controller(
				"stockListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $http, $stateParams, $mdMedia,
						$mdDialog, objectFactory, appEndpointSF) {

					$log.debug("Inside stockListCtr");

					$scope.query = {
						order : '-itemNumber',
						limit : 50,
						page : 1
					};
					$scope.dummyWarehouse = {
						warehouseName : "ALL",
					}

					$scope.dummyStockItemProductType = {
						typeName : "ALL",
					}
					$scope.dummyStockItemBrand = {
						brandName : "ALL",
					}

					$scope.stockItemList = [];

					$scope.warehouses = [];

					$scope.stockItemBrands = [];

					$scope.stockItemProductTypes = [];

					$scope.stockItemTypeFilterWrapper = {
						productType : null,
						brand : null,
						warehouse : null
					};

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					/*
					 * $scope.filterStockItemsByWarehouse = function(
					 * selectedWarehouse) { $scope.loading = true;
					 * $scope.selectedWarehouse = selectedWarehouse; var
					 * stockService = appEndpointSF.getStockService();
					 * 
					 * stockService.filterStockItemsByWarehouse(
					 * $scope.selectedWarehouse).then( function(stockList) {
					 * $scope.stockItemList = stockList; $scope.loading = false;
					 * }); }
					 */
					$scope.filterStockItems = function(
							stockItemTypeFilterWrapper) {

						$scope.loading = true;

						$scope.stockItemTypeFilterWrapper = stockItemTypeFilterWrapper;

						var stockService = appEndpointSF.getStockService();

						stockService
								.filterStockItemsByBrandAndProductTypeAndWH(
										$scope.curUser.business.id,
										$scope.stockItemTypeFilterWrapper)
								.then(
										function(stockList) {
											$scope.stockItemList = stockList;
											if ($scope.stockItemList
													&& $scope.stockItemList.length > 0) {
												for (var i = 0; i < $scope.stockItemList.length; i++) {
													$scope.stockItemList[i].stockItemType.catNames = "";
													if ($scope.stockItemList[i].stockItemType.categoryList
															&& $scope.stockItemList[i].stockItemType.categoryList.length > 0) {
														for (var j = 0; j < $scope.stockItemList[i].stockItemType.categoryList.length; j++) {
															$scope.stockItemList[i].stockItemType.catNames += $scope.stockItemList[i].stockItemType.categoryList[j].catName;
															if (j != $scope.stockItemList[i].stockItemType.categoryList.length - 1) {
																$scope.stockItemList[i].stockItemType.catNames += ",";
															}
														}
													}
												}
											}
											$scope.loading = false;
										});

					}

					$scope.filterByWarehouse = function() {
						if ($scope.stockItemTypeFilterWrapper.warehouse.warehouseName != "ALL") {
							$scope.stockItemTypeFilterWrapper.productType = null;
							$scope.stockItemTypeFilterWrapper.brand = null;

						}
						$scope
								.filterStockItems($scope.stockItemTypeFilterWrapper);
					}

					$scope.filterByProductType = function() {
						if ($scope.stockItemTypeFilterWrapper.productType.typeName != "ALL") {

							$scope.stockItemTypeFilterWrapper.brand = null;
							$scope.stockItemTypeFilterWrapper.warehouse = null;

						}
						$scope
								.filterStockItems($scope.stockItemTypeFilterWrapper);
					}

					$scope.filterByBrand = function() {
						if ($scope.stockItemTypeFilterWrapper.brand.brandName != "ALL") {
							$scope.stockItemTypeFilterWrapper.productType = null;
							$scope.stockItemTypeFilterWrapper.warehouse = null;

						}
						$scope
								.filterStockItems($scope.stockItemTypeFilterWrapper);
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
											if ($scope.warehouses.length > 0) {
												$scope.warehouses
														.unshift($scope.dummyWarehouse);
												$scope.stockItemTypeFilterWrapper.warehouse = $scope.warehouses[0];

											}
											$scope.loading = false;
										});
					}

					$scope.getStockItemBrands = function() {

						var stockService = appEndpointSF.getStockService();

						stockService
								.getStockItemBrands($scope.curUser.business.id)
								.then(
										function(stockItemBrands) {
											$scope.stockItemBrands = stockItemBrands;
											if ($scope.stockItemBrands.length > 0) {
												$scope.stockItemBrands
														.unshift($scope.dummyStockItemBrand);
												$scope.stockItemTypeFilterWrapper.brand = $scope.stockItemBrands[0];

											}
										})
					}

					$scope.getStockItemProductTypes = function() {

						var stockService = appEndpointSF.getStockService();

						stockService
								.getStockItemProductTypes(
										$scope.curUser.business.id)
								.then(
										function(stockItemProductTypes) {
											$scope.stockItemProductTypes = stockItemProductTypes;
											if ($scope.stockItemProductTypes.length > 0) {
												$scope.stockItemProductTypes
														.unshift($scope.dummyStockItemProductType);
												$scope.stockItemTypeFilterWrapper.productType = $scope.stockItemProductTypes[0];

											}
										})
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAllWarehouseByBusiness();
							$scope.getStockItemProductTypes();
							$scope.getStockItemBrands();
							$scope
									.filterStockItems($scope.stockItemTypeFilterWrapper);
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					// $scope.stockItemList = [];
					$scope.waitForServiceLoad();

					$scope.viewSerialNumbers = function(ev, stockItem) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : viewSerialNumbersDialogController,
											templateUrl : '/app/stock/add_stock_serialnumbers_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curBusi : $scope.curUser.business,
												curUser : $scope.curUser,
												stockItem : stockItem
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

					function viewSerialNumbersDialogController($scope,
							$mdDialog, curBusi, curUser, stockItem) {
						$scope.stockLineItem = {
							stockItem : stockItem,
							view : true
						};

						var stockService = appEndpointSF.getStockService();
						stockService
								.getStockItemInstancesList(stockItem)
								.then(
										function(stockItemInstanceList) {
											$scope.stockLineItem.stockItemInstanceList = stockItemInstanceList;
											$scope.loading = false;
										});

						$scope.cancel = function() {
							$mdDialog.hide();
						};
					}

				});