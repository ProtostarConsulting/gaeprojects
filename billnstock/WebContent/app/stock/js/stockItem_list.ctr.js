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
			$scope.stockItemList = [];
			$scope.waitForServiceLoad();

			$scope.filteredStock = [];
			for (var i = 0; i < $scope.stockItemList.length; i++) {
				if ($scope.stockItemList[i].id == $scope.selectedStocksId) {
					$scope.filteredStock.push($scope.stockItemList[i]);
				}
			}
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

			$scope.showSimpleToast = function() {
				$mdToast.show($mdToast.simple().content('Stock Updated....')
						.position("top").hideDelay(3000));
			};

		});