var app = angular.module("stockApp")
		app.controller(
				"qcstockReceiptListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams, $mdColors,
						$routeParams, $filter, $location, $anchorScroll,
						objectFactory, appEndpointSF) {

					function reSetQuery() {
						return {
							order : '-qcno',
							limit : 50,
							page : 1,
							totalSize : 0,
							pagesLoaded : 0
						};
					}
					$scope.query = reSetQuery();
					
					$scope.qcstockReceiptList = [];
					
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					
					$scope.fetchStockReceiptQCList = function() {
						
						var stockService = appEndpointSF.getStockService();
						stockService.getStockReceiptQCList($scope.curUser.business.id).then(function(list) {
							$scope.qcstockReceiptList = list;
						});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchStockReceiptQCList();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					
					$scope.waitForServiceLoad();

					$scope.back = function() {
						window.history.back();
					}
				});
