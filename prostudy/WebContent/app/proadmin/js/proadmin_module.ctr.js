angular.module("prostudyApp").controller(
		"proAdminModuleCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$log, objectFactory, appEndpointSF) {
		
		     
			$scope.curUser = appEndpointSF.getLocalUserService()
			.getLoggedinUser();
			$log.debug("Inside proAdminModuleCtr");
			//$scope.loginCheck();
			
			$scope.check = function()
			{
				 var str = $scope.curUser.email_id;
				 var val1 = str.search("protostarcs.com");
				 var val2 = str.search("protostar.co.in");
				 if(val1 > 0 || val2 > 0)
					{
					 	return true;
					}
				 else
					 {
					 	return false;
					 }
				
			}
		
			$scope.back = function() {
				window.history.back();
				// $state.go("^", {});
			};
		
			/* Setup page menu */
			$scope.toggleRight = buildToggler('right');
			/**
			 * Build handler to open/close a SideNav; when animation finishes
			 * report completion in console
			 */
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