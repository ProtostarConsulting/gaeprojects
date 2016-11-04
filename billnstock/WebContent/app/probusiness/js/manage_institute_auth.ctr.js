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

					$scope.selectedbusinessID = $stateParams.selectedbusinessID;

					if (!$scope.selectedbusinessID) {
						return;
					}

					$scope.authorizationMasterEntity = {
						authorizations : []
					};
					$scope.business = null;
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

												var userService = appEndpointSF
														.getUserService();

												userService
														.getbusinessById(
																$scope.selectedbusinessID)
														.then(
																function(
																		business) {
																	$scope.business = business;
																	var jsonString = $scope.business.authorizations;

																	$log
																			.debug("$scope.business.authorizations:Json: "
																					+ jsonString);

																	if (jsonString) {
																		var jsonObject = angular
																				.fromJson($scope.business.authorizations);
																		$scope.existingbusinessAuthObject = jsonObject;

																		$scope.authorizationMasterEntity = authService
																				.markSelectedAuthorizations(
																						$scope.authorizationMasterEntity,
																						$scope.existingbusinessAuthObject);
																	}
																	$scope.loading = false;
																});

											}
											$scope.mode = "list";
										});

						$log.debug("Called getAllAuthorizations...");

					}

					// for testing only. This will be fetched from server
					var selectedbusinessTestAuths = {
						name : "MBIS",
						'authorizations' : [ {
							'authName' : 'gfe',
							'authorizations' : [ {
								'authName' : 'list'
							}, {
								'authName' : 'edit',
								'authorizations' : ''
							} ]
						}, {
							'authName' : 'exams',
							'authorizations' : ''
						}, {
							'authName' : 'setup',
							'authorizations' : ''
						} ]

					};

					

					$scope.saveAuthorization = function() {
						$log.debug("Called saveAuthorization...");
						var authService = appEndpointSF
								.getAuthorizationService();

						var toSaveJsonString = angular
								.toJson(authService
										.getCurrentSelectedAuthorizations($scope.authorizationMasterEntity));

						$log.debug("toSaveJsonString: " + toSaveJsonString);

						$scope.business.authorizations = toSaveJsonString;
						var userService = appEndpointSF
						.getUserService();
						userService.updateBusiStatus($scope.business)
								.then(function(msgBean) {
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