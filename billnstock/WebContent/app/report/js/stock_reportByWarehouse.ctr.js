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
					
					$scope.stockData = [];
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
							$scope.getAllStockItems();
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

					}					
				});