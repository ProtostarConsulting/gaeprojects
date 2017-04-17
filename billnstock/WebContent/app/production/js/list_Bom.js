app = angular.module("stockApp");
app
		.controller(
				"list_bom",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $q, $mdMedia, $mdDialog,
						objectFactory, appEndpointSF, $mdColors) {
					
					
					
					
					$scope.fetchProductionList=function(){
						var productService = appEndpointSF
						.getProductionService();
						
						productService.getlistBomEntity($scope.curUser.business.id)
						.then(function(list) {
						$scope.productList=list;
				
						});
					}
					
					
					
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchProductionList();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					
					/*

					$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();
					
					
					
					$scope.fetchProductionList=function(){
						var productService = appEndpointSF
						.getProductionService();
						
						productService.getProductionList($scope.curUser.business.id)
						.then(function(list) {
						$scope.productList=list;
						
						});
					}
						$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchProductionList();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}$scope.waitForServiceLoad();

				*/});