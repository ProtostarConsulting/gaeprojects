angular
		.module("stockApp")
		.controller(
				"manageUserAuthCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $stateParams, objectFactory,
						appEndpointSF) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					$log.debug("Inside manageUserAuthCtr");

					$scope.selectedUser = $stateParams.selectedUser;

					if (!$scope.selectedUser) {
						return;
					}

					$scope.authorizationMasterEntity = {
						authorizations : []
					};
					$scope.instituteAuthMasterEntity = {
						authorizations : []
					};

					$scope.business = $scope.curUser.business;

					if ($scope.curUser.business.authorizations) {
						$scope.existingInstituteAuthObject = angular
								.fromJson($scope.curUser.business.authorizations);
					} else {
						$scope.existingInstituteAuthObject = {
							authorizations : []
						};
					}

					$log.debug("$scope.curUser.business.authorizations: "
							+ $scope.curUser.business.authorizations);

					$scope.topAuthChange = function(auth) {
						var childAuths = auth.authorizations;
						if(childAuths && childAuths.length && childAuths.length > 0){
							angular.forEach(childAuths, function(childAuth){
								childAuth.selected = auth.selected;
							});
						}
					}
					
					$scope.userAuthObject = null;

					function getAuthorizationMasterEntity() {
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

												$scope.instituteAuthMasterEntity = authService
														.filterMasterAuthTree(
																$scope.authorizationMasterEntity,
																$scope.existingInstituteAuthObject,
																$scope.instituteAuthMasterEntity);

												var jsonUserAuthObject = angular
														.fromJson($scope.selectedUser.authorizations);
												$scope.userAuthObject = jsonUserAuthObject;
												if ($scope.selectedUser.authorizations) {
													$scope.instituteAuthMasterEntity = authService
															.markSelectedAuthorizations(
																	$scope.instituteAuthMasterEntity,
																	$scope.userAuthObject);
												}

											}
											$scope.mode = "list";
										});

						$log.debug("End of getAuthorizationMasterEntity...");

					}

					$scope.saveAuthorization = function() {

						$log.debug("Called saveAuthorization...");
						var authService = appEndpointSF
								.getAuthorizationService();

						var toSaveJsonString = angular
								.toJson(authService
										.getCurrentSelectedAuthorizations($scope.instituteAuthMasterEntity));

						$log.debug("toSaveJsonString: " + toSaveJsonString);

						$scope.selectedUser.authorizations = toSaveJsonString;

						var UserService = appEndpointSF.getUserService();
						UserService.updateUser($scope.selectedUser).then(
								function(user) {
									$scope.showUpdateToast();
								});

					}
					
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							getAuthorizationMasterEntity();

						} else {
							$log
									.debug("proAdminManageInstituteAuth: Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.selected = []
					$scope.query = {
						order : 'id',
						limit : 5,
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