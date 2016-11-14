angular
		.module("stockApp")
		.controller(
				"disclaimer",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF) {
					// ////////////////////////////////////////////////////////////////////////////////////////////////
				


					$scope.curuser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					
					$scope.disclaimer=$scope.curuser.business.disclaimer;
					
					

					$scope.updateBusiness = function() {						
						$scope.curuser.business.disclaimer=$scope.disclaimer;
						
						var UserService = appEndpointSF.getUserService();
						UserService
								.updateBusiness($scope.curuser.business)
								.then(function(business) {
									$scope.curuser.business=business;
									appEndpointSF.getLocalUserService().saveLoggedInUser($scope.curuser);
									$scope.showUpdateToast();
										});
					}
					
					
					
				
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
