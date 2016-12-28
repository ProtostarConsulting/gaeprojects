angular.module("stockApp").controller(
		"stockItemTypeListCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$log, $http, $stateParams, objectFactory, appEndpointSF) {

			$scope.query = {
				order : '-itemNumber',
				limit : 50,
				page : 1
			};

			$scope.curUser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			$scope.getStockItemTypes = function() {
				var stockService = appEndpointSF.getStockService();

				stockService.getStockItemTypes($scope.curUser.business.id)
						.then(function(stockList) {
							$scope.stockItemList = stockList;
						});
			}

			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getStockItemTypes();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.stockItemList = [];
			$scope.waitForServiceLoad();

		});