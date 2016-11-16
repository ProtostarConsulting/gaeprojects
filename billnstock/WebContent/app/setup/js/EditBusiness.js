angular
		.module("stockApp")
		.controller(
				"editBusiness",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $mdMedia, $mdDialog, $log,
						objectFactory, appEndpointSF) {

					$scope.curuser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.selectedBusiness = $stateParams.selectedBusiness ? $stateParams.selectedBusiness
							: $scope.curuser.business;
					
					$scope.Address = $scope.selectedBusiness.address;

					$scope.updateBusiness = function() {
						$scope.selectedBusiness.address = $scope.Address;
						var UserService = appEndpointSF.getUserService();
						UserService
								.updateBusiness($scope.selectedBusiness)
								.then(
										function(msgBean) {
											if ($scope.curuser.business.id == $scope.selectedBusiness.id) {
												$scope.curuser.business = $scope.selectedBusiness;
												appEndpointSF
														.getLocalUserService()
														.saveLoggedInUser(
																$scope.curuser);
											}
											$scope.showUpdateToast();
										});
					}

					// ----------hide and show ---------------------------

					$scope.IsHidden = true;
					$scope.ShowHide = function() {
						$scope.IsHidden = $scope.IsHidden ? false : true;
					}
					// -----------------------------------------------------

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
