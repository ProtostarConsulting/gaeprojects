app = angular.module("stockApp");
app
		.controller(
				"listBOM",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $q, $mdMedia, $mdDialog,
						objectFactory, appEndpointSF, $mdColors) {
					
					
					
					
					$scope.fetchProductionList=function(){
						var productService = appEndpointSF
						.getProductionService();
						
						productService.getProductionList($scope.curUser.business.id)
						.then(function(list) {
						$scope.productList=list;
				
						
			for (var i = 0; i < $scope.productList.length; i++) 
				
			{
				for (var j = 0; j < $scope.productList[i].catList.length; j++) 
					
				{
					$scope.productList[i].catList[j].itemNames="";		

					for (var k = 0; k < $scope.productList[i].catList[j].items.length; k++) 
						
					{
						$scope.productList[i].catList[j].itemNames += $scope.productList[i].catList[j].items[k].itemName;
								
							if(k !=$scope.productList[i].catList[j].items.length-1)	{
								$scope.productList[i].catList[j].itemNames +=","
							}	
									
					}		
								
								
				}
								
						
							
							
			}
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