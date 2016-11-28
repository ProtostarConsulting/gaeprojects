angular
		.module("stockApp")
		.controller(
				"proAdminManageBizAuth",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $stateParams, objectFactory,
						appEndpointSF) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					$log.debug("Inside proAdminManageBizAuth");

					$scope.selectedBusiness = $stateParams.selectedBusiness;

					if (!$scope.selectedBusiness) {
						return;
					}

					$scope.authorizationMasterEntity = {
						authorizations : []
					};

					$scope.existingbusinessAuthObject = null;

					function getAuthorizationMasterEntity() {
						$scope.loading = true;
						var authService = appEndpointSF
								.getAuthorizationService();
						authService
								.getAuthorizationMasterEntity()
								.then(
										function(result) {
											$log.debug("result:" + result);
											if (result
													&& result.authorizations != undefined) {
												$scope.authorizationMasterEntity.authorizations = result.authorizations;

												var jsonString = $scope.selectedBusiness.authorizations;

												/*
												 * $log
												 * .debug("$scope.selectedBusiness.authorizations:Json: " +
												 * jsonString);
												 */
												if (jsonString) {
													var jsonObject = angular
															.fromJson($scope.selectedBusiness.authorizations);
													$scope.existingbusinessAuthObject = jsonObject;

													$scope.authorizationMasterEntity = authService
															.markSelectedAuthorizations(
																	$scope.authorizationMasterEntity,
																	$scope.existingbusinessAuthObject);
												}
												$scope.loading = false;

											}
											$scope.mode = "list";
										});

						$log.debug("Called getAllAuthorizations...");

					}

					
					$scope.topAuthChange = function(auth) {
						var childAuths = auth.authorizations;
						if(childAuths && childAuths.length && childAuths.length > 0){
							angular.forEach(childAuths, function(childAuth){
								childAuth.selected = auth.selected;
							});
						}
					}

					$scope.saveAuthorization = function() {
						$log.debug("Called saveAuthorization...");
						var authService = appEndpointSF
								.getAuthorizationService();

						var toSaveJsonString = angular
								.toJson(authService
										.getCurrentSelectedAuthorizations($scope.authorizationMasterEntity));

						//$log.debug("toSaveJsonString: " + toSaveJsonString);

						$scope.selectedBusiness.authorizations = toSaveJsonString;
						var userService = appEndpointSF.getUserService();
						userService
								.updateBusiness($scope.selectedBusiness)
								.then(
										function(msgBean) {
											if ($scope.selectedBusiness.id == $scope.curUser.business.id) {
												$scope.curUser.business = $scope.selectedBusiness;
												appEndpointSF
														.getLocalUserService()
														.saveLoggedInUser(
																$scope.curUser);
											}
											$scope.showUpdateToast();
										});
					}

					$scope.cancelButton = function() {
						$state.go("^", {});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							getAuthorizationMasterEntity();

						} else {
							$log
									.debug("proAdminManageBizAuth: Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.selected = []
					$scope.query = {
						order : 'id',
						limit : 50,
						page : 1
					};

					$scope.onpagechange = function(page, limit) {
						var deferred = $q.defer();

						$timeout(function() {
							deferred.resolve();
						}, 2000);

						return deferred.promise;
					};

					$scope.onorderchange = function(order) {
						var deferred = $q.defer();

						$timeout(function() {
							deferred.resolve();
						}, 2000);

						return deferred.promise;
					};

				});