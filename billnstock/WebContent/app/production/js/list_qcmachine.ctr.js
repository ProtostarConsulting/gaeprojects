var app = angular.module("stockApp")
		app.controller(
				"qcmachineListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams, $mdColors,
						$routeParams, $filter, $location, $anchorScroll,
						objectFactory, appEndpointSF) {
					$scope.loading = true;
					function reSetQuery() {
						return {
							order : 'itemNumber',
							limit : $scope.dataTableOptions.limit,
							page : 1,
							totalSize : 0,
							pagesLoaded : 0
						};
					}
					$scope.query = reSetQuery();
					
					$scope.qcmachineList = [];
					
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					
					$scope.fetchQCMachineList = function() {
						
						var productService = appEndpointSF.getProductionService();
						productService.getQCMachineList($scope.curUser.business.id).then(function(list) {
							$scope.qcmachineList = list;
							$scope.loading = false;
						});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchQCMachineList();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					
					$scope.waitForServiceLoad();
				});
