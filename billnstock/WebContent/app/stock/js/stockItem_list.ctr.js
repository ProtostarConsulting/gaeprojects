angular
		.module("stockApp")
		.controller(
				"stockListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $http, $stateParams, objectFactory,
						appEndpointSF) {

					$log.debug("Inside stockListCtr");

					$scope.query = {
						order : '-itemNumber',
						limit : 50,
						page : 1
					};
					$scope.selectedWarehouse = null;

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.filterStockItemsByWarehouse = function(selectedWarehouse) {
						$scope.loading = true;
						$scope.selectedWarehouse = selectedWarehouse;
						var stockService = appEndpointSF.getStockService();

						stockService.filterStockItemsByWarehouse($scope.selectedWarehouse).then(
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
											if ($scope.warehouses.length > 0) {
												$scope.selectedWarehouse = $scope.warehouses[0];
												$scope
														.filterStockItemsByWarehouse($scope.selectedWarehouse);
											}
											$scope.loading = false;
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
					$scope.stockItemList = [];
					$scope.waitForServiceLoad();

				});