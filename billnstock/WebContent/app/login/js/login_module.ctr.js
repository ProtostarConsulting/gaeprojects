angular
		.module("stockApp")
		.controller(
				"loginModuleCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $q, $location, objectFactory,
						appEndpointSF, $state) {

					$scope.showSavedToast = function() {
						$mdToast.show($mdToast.simple().content(
								'New Teacher Registered!').position("top")
								.hideDelay(3000));
					};

					$scope.user = {
						business : "",
						email_id : "",
						firstName : "",
						lastName : "",
						password : "",
						isGoogleUser : true,
						authority : []
					}

					/*$scope.login = function() {
						var UserService = appEndpointSF.getUserService();
						UserService
								.login($scope.user.email_id,
										$scope.user.password)
								.then(
										function(result) {
											if (result.result.email_id) {
												appEndpointSF
														.getLocalUserService()
														.saveLoggedInUser(
																result.result);
												$log
														.debug("User logged in successfully: "
																+ $scope.user.email_id);

												$state.go("home");
												$scope.loginMsg = "";
											} else {
												$log.debug("Login failed.: "
														+ $scope.user);
												$scope.loginMsg = "Login failed.";
											}
										});

					}*/

					/* Setup page menu */
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