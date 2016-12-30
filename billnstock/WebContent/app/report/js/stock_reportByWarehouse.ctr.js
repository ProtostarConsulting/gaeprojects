angular
		.module("stockApp")
		.controller(
				"stockReportByWarehouseCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $http, objectFactory, appEndpointSF) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					$scope.query = {
						order : '-itemNumber',
						limit : 50,
						page : 1
					};

					$scope.getAllStockItems = function() {
						var stockService = appEndpointSF.getStockService();
						stockService.getAllStockItems($scope.curUser.business.id)
								.then(function(stockList) {
									$scope.stockData = stockList;
								});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAllStockItems();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.stockData = [];
					$scope.waitForServiceLoad();

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
							$scope.getAllWarehouseByBusiness();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.warehouses = [];
					$scope.waitForServiceLoad();

					$scope.warehouseDDLChange = function(index,
							selectedWarehouse) {
						$log.debug("##Came to warehouseDDLChange...");
						$scope.filteredWarehouseData = [];
						// $scope.stock.warehouseId = selectedWarehouse;

						for (var i = 0; i < $scope.stockData.length; i++) {
							if ($scope.stockData[i].warehouse
									&& ($scope.stockData[i].warehouse.id == selectedWarehouse.id)) {
								$scope.filteredWarehouseData
										.push($scope.stockData[i]);
							}
						}

					};

					// Setup menu
					$scope.toggleRight = buildToggler('right');

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
				});