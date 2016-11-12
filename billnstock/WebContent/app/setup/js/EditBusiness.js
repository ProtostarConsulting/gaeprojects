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

					$scope.Address = {
						line1 : "",
						line2 : "",
						city : "",
						state : "",
						country : "",
						pin : ""
					}

					$scope.getBusinessById = function() {

						var UserService = appEndpointSF.getUserService();
						UserService.getbusinessById($scope.selectedBusiness.id)
								.then(function(Business) {
									$scope.business = Business;
									$scope.Address = $scope.business.address;

								});

					}
					$scope.business = {};

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {

							$scope.getBusinessById();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.updateBusiness = function() {
						$scope.business.address = $scope.Address;
						var setupService = appEndpointSF.getsetupService();
						setupService.updateBusiness($scope.business).then(
								function(msgBean) {
									$scope.curuser.business = $scope.business;
									appEndpointSF.getLocalUserService()
											.saveLoggedInUser($scope.curuser);
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
