angular.module("stockApp").controller(
		"ListAccountType",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, appEndpointSF) {
						
			$scope.curuser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			$scope.getallAccountType = function() {
				$scope.loading = true;
				var proadminService = appEndpointSF.getproadminService();
				proadminService.getallAccountType().then(function(assetList) {
					$scope.accountlist = assetList.items;
					$scope.loading = false;
				});
			}
			$scope.accountlist = [];

			$scope.updateAccountType = function() {
				/* $scope.AccountType.loggedInUser = $scope.curUser; */

				var proadminService = appEndpointSF.getproadminService();
				proadminService.updateAccountType($scope.accounttype).then(
						function(msgBean) {
							$scope.showUpdateToast();

						});
			}

			// -------------------use to lode
			// service------------------------------

			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getallAccountType();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();

			// -----------------------------------------------------------------------

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
