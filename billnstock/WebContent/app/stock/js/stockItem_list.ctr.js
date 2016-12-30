angular.module("stockApp").controller(
		"stockListCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$log, $http, $stateParams, objectFactory, appEndpointSF) {

			$log.debug("Inside stockListCtr");

			$scope.query = {
				order : '-itemNumber',
				limit : 50,
				page : 1
			};

			$scope.curUser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			$scope.getAllStockItems = function() {
				$log.debug("Inside Ctr $scope.getAllStock");
				var stockService = appEndpointSF.getStockService();

				stockService.getAllStockItems($scope.curUser.business.id).then(
						function(stockList) {
							$scope.stockItemList = stockList;
							$scope.stockItemListBackup = stockList;
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
					$scope.getAllStockItems();
					$scope.getAllWarehouseByBusiness();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.stockItemList = [];			
			$scope.waitForServiceLoad();
			
			$scope.warehouseDDLChange = function(index,
					selectedWarehouse) {
				$log.debug("##Came to warehouseDDLChange...");
				$scope.filteredWarehouseData = [];				
				$scope.stockItemList = [];
				for (var i = 0; i < $scope.stockItemListBackup.length; i++) {
					if ($scope.stockItemListBackup[i].warehouse
							&& ($scope.stockItemListBackup[i].warehouse.id == selectedWarehouse.id)) {
						$scope.stockItemList
								.push($scope.stockItemListBackup[i]);
					}
				}

			}
		});