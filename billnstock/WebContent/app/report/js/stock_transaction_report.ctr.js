angular.module("stockApp").controller(
		"stockTxnReportCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$log, $state, $http, $stateParams, $routeParams, $filter, $q,
				$mdMedia, $mdDialog, objectFactory, appEndpointSF, $mdColors) {

			$scope.curUser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			$scope.query = {
				order : '-itemNumber',
				limit : 50,
				page : 1
			};

			$scope.stockItemList = [];
			$scope.warehouses = [];
			$scope.stockTxnList = [];
			$scope.stockTxnTypes = [ 'CR', 'DR' ];

			$scope.getAllWarehouseByBusiness = function() {
				$log.debug("Inside function $scope.getAllWarehouseByBusiness");
				var warehouseService = appEndpointSF
						.getWarehouseManagementService();

				warehouseService.getAllWarehouseByBusiness(
						$scope.curUser.business.id).then(
						function(warehouseList) {
							$scope.warehouses = warehouseList;
						});
			}

			$scope.filterStockItemsByWarehouse = function(selectedWarehouse) {
				$scope.selectedWarehouse = selectedWarehouse;
				var stockService = appEndpointSF.getStockService();
				stockService.filterStockItemsByWarehouse(
						$scope.selectedWarehouse).then(function(stockList) {
					$scope.stockItemList = stockList;
				});
			}

			$scope.getStockTxnList = function(stockItem, selectedTxnType,
					selectedFromDate, selectedToDate) {

				$scope.selectedStockItem = stockItem;
				$scope.selectedTxnType = selectedTxnType;
				$scope.selectedFromDate = new Date(selectedFromDate);
				$scope.selectedToDate = new Date(selectedToDate);

				if ($scope.selectedTxnType == "CR") {
					var stockService = appEndpointSF.getStockService();
					stockService.getCRStockTxnByStockItem(
							$scope.selectedStockItem,
							$scope.selectedStockItem.business.id,
							$scope.selectedFromDate.getTime(),
							$scope.selectedToDate.getTime()).then(
							function(list) {
								$scope.stockTxnList = list;

							});
				} else if ($scope.selectedTxnType == "DR") {
					var stockService = appEndpointSF.getStockService();
					stockService.getDRStockTxnByStockItem(
							$scope.selectedStockItem,
							$scope.selectedStockItem.business.id,
							$scope.selectedFromDate.getTime(),
							$scope.selectedToDate.getTime()).then(
							function(list) {
								$scope.stockTxnList = list;

							});
				}
			}

			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getAllWarehouseByBusiness();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}

			$scope.waitForServiceLoad();

		});