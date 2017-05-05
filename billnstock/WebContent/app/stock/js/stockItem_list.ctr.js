angular
		.module("stockApp")
		.controller(
				"stockListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $http, $stateParams, $mdMedia,
						$mdDialog, objectFactory, appEndpointSF) {

					$log.debug("Inside stockListCtr");
					$scope.loading = true;
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.query = {
						order : '-itemNumber',
						limit : 50,
						page : 1,
						totalSize : 0,
						pagesLoaded : 0
					};

					$scope.logOrder = function(order) {
						console.log('order: ', order);
					};

					$scope.logPagination = function(page, limit) {
						console.log('page: ', page);
						console.log('limit: ', limit);
						$location.hash('tp1');
						$anchorScroll();
						if ($scope.query.page > $scope.query.pagesLoaded) {
							$scope.filterStockItems();
						}
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
						warehouse : $scope.dummyWarehouse,
						brand : $scope.dummyStockItemBrand,
						productType : $scope.dummyStockItemProductType
					};

					$scope.filterStockItems = function() {
						$scope.loading = true;
						var stockItemsFilterData = {
							warehouse : $scope.stockItemTypeFilterWrapper.warehouse.warehouseName == 'ALL' ? null
									: $scope.stockItemTypeFilterWrapper.warehouse,
							brand : $scope.stockItemTypeFilterWrapper.brand.brandName == 'ALL' ? null
									: $scope.stockItemTypeFilterWrapper.brand,
							productType : $scope.stockItemTypeFilterWrapper.productType.typeName == 'ALL' ? null
									: $scope.stockItemTypeFilterWrapper.productType
						}
						var stockService = appEndpointSF.getStockService();

						stockService
								.filterStockItemsByBrandAndProductTypeAndWH(
										$scope.curUser.business.id,
										stockItemsFilterData)
								.then(
										function(resp) {
											$scope.stockItemList = resp;
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

					$scope.getAllWarehouseByBusiness = function() {
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
							$scope.filterStockItems();
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