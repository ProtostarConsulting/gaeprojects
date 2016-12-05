angular
		.module("stockApp")
		.controller(
				"initsetup",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, $http, objectFactory,
						appEndpointSF) {

					$scope.showSimpleToast = function(msgBean) {
						$mdToast.show($mdToast.simple().content(msgBean)
								.position("top").hideDelay(3000));
					};

					$scope.userMsg;
					$scope.step2;
					$scope.createAccountTypes_Bool = true;
					$scope.createUsers_Bool = false;
					$scope.createAuthMaster_Bool = true;
					$scope.creatAccountAndGroup_Bool = true;

					$scope.createAccountTypes = function() {

						var protostarAdminService = appEndpointSF
								.getproadminService();
						protostarAdminService
								.getallAccountType()
								.then(
										function(gotAccountList) {

											$scope.accountlist = (gotAccountList == undefined || gotAccountList.items == undefined) ? 0
													: gotAccountList.items.length;
											if ($scope.accountlist == 0) {
												protostarAdminService
														.initsetup()
														.then(
																function(
																		msgBean) {
																	$scope.createAccountTypes_Bool = false;
																	$scope.createUsers_Bool = true;
																	$scope.userMsg = "Account types are created. ";
																});
											} else {
												$scope.createAccountTypes_Bool = false;
												$scope.createUsers_Bool = true;
												$scope.userMsg = "Account types are already created. No action was taken.";
											}

										});
					}

					$scope.createUsers = function() {
						var protostarAdminService = appEndpointSF
								.getproadminService();
						protostarAdminService
								.getallAccountType()
								.then(
										function(gotAccountList) {
											$scope.accountlist = (gotAccountList == undefined || gotAccountList.items == undefined) ? 0
													: gotAccountList.items.length;

											protostarAdminService
													.getAllemp()
													.then(
															function(gotEmpList) {
																$scope.emps = (gotEmpList == undefined || gotEmpList.items == undefined) ? 0
																		: gotEmpList.items.length;

																if ($scope.emps == 0) {
																	protostarAdminService
																			.initsetupnext()
																			.then(
																					function(
																							msgBean) {
																						$scope.userMsg = "Users created successfully.";
																						$scope.createUsers_Bool = false;
																					});
																} else {
																	$scope.userMsg = "Users are already created. No action was taken.";
																	$scope.createUsers_Bool = false;
																}

															});

										});
					}

					$scope.creatAccountAndGroup = function() {

						var proadminAddGroup = appEndpointSF
								.getAccountGroupService();

						proadminAddGroup
								.getAllBusiness()
								.then(
										function(list) {
											if (list == undefined
													|| list.length == 0) {
												$scope.userMsg = "Can't Create Group...Business is Not Created !!!!!!";
												$scope.step4 = true;
											} else {
												var protostarAdminService = appEndpointSF
														.getproadminService();
												protostarAdminService
														.creatAccountAndGroup(
																list[0].id)
														.then(
																function(resp) {

																	if (resp != undefined) {

																		$scope.userMsg = "Settup of Group List is Done.........";
																		$scope.step4 = true;
																		$scope.creatAccountAndGroup_Bool = false;
																	}

																});
											}

										});

					}

					$scope.createAuthMaster = function() {

						$scope.authorizationMasterEntity = {
							authorizations : []
						};

						var authService = appEndpointSF
								.getAuthorizationService();

						authService
								.getAuthorizationMasterEntity()
								.then(
										function(result) {
											$log.debug("get result:"
													+ angular.toJson(result));
											if (result.authorizations == undefined
													|| result.authorizations.length == 0) {

												$http
														.get(
																"/app/initsetup/js/masterauth.json")
														.then(
																function(
																		response) {
																	$log
																			.debug("http response:"
																					+ angular
																							.toJson(response));
																	if (response.data.authorizations
																			&& response.data.authorizations.length > 0) {
																		$scope.authorizationMasterEntity = response.data;
																		authService
																				.saveAuthorizationMasterEntity(
																						$scope.authorizationMasterEntity)
																				.then(
																						function(
																								result) {
																							$log
																									.debug("save result:"
																											+ angular
																													.toJson(result));
																							$scope.userMsg = "Auth master created successfully.";
																							$scope.createAuthMaster_Bool = false;
																						});
																	} else {
																		$log
																				.debug("ERROR: no valid data found in masterauth.json.");
																		$scope.userMsg = "ERROR: no valid data found in masterauth.json.";
																	}
																});

											} else {
												$scope.userMsg = "Auth master is already created. No action was taken.";
												$scope.createAuthMaster_Bool = false;
											}

										});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							// $scope.initsetup();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

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
