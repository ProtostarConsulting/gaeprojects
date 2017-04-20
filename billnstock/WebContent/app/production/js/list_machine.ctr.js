var app = angular.module("stockApp")
		app.controller(
				"machineListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams, $mdColors,
						$routeParams, $filter, $location, $anchorScroll,
						objectFactory, appEndpointSF) {

					function reSetQuery() {
						return {
							order : '-machineNo',
							limit : 50,
							page : 1,
							totalSize : 0,
							pagesLoaded : 0
						};
					}
					
					
					$scope.logOrder = function(order) {
						console.log('order: ', order);
					};

					$scope.logPagination = function(page, limit) {
						console.log('page: ', page);						
						console.log('limit: ', limit);
						$location.hash('tp1');
						$anchorScroll();
						if ($scope.query.page > $scope.query.pagesLoaded) {
							$scope.fetchMachineList();
						}
					}	
					$scope.query = reSetQuery();
					
					$scope.machineList = [];
					
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					
					$scope.fetchMachineList = function() {
						
						var productService = appEndpointSF.getProductionService();
						productService.getMachineList($scope.curUser.business.id).then(function(list) {
							$scope.machineList = list;
						});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchMachineList();
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
