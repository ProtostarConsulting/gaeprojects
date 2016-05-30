angular.module("stockApp").controller(
		"stockReportByThresholdCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$log,$http, objectFactory, appEndpointSF) {

			$log.debug("Inside customerCtr");
			
			$scope.curUser = appEndpointSF.getLocalUserService()
			.getLoggedinUser();
	$log.debug("$scope.curUser++++++++"
			+ angular.toJson($scope.curUser));
	
			
			$scope.getReportByThreshold = function(){
				$log.debug("Inside Ctr $scope.getReportByThreshold");
				
				var stockService = appEndpointSF.getStockService();
				
				stockService.getReportByThreshold($scope.curUser.business.id).then(
						function(stockByThreshold) {
							$scope.thresholdStock = stockByThreshold;
							$log.debug("$scope.thresholdStock:"
									+ angular.toJson($scope.thresholdStock));							
						})
				
			}
			
			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getReportByThreshold();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.thresholdStock = [];
			$scope.waitForServiceLoad();
			
					
//			 Setup menu 
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
		});