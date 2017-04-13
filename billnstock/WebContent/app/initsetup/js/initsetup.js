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
					$scope.createDefaultBiz_Bool = false;
					$scope.createAuthMaster_Bool = true;
					$scope.creatAccountAndGroup_Bool = true;

					$scope.createDefaultBusinessPlans = function() {

						var protostarAdminService = appEndpointSF
								.getproadminService();
						protostarAdminService
								.getBusinessPlans()
								.then(
										function(gotAccountList) {

											var noOfBusinessPlans = (gotAccountList == undefined || gotAccountList.items == undefined) ? 0
													: gotAccountList.items.length;
											if (noOfBusinessPlans == 0) {
												protostarAdminService
														.createDefaultBusinessPlans()
														.then(
																function(
																		msgBean) {
																	$scope.createAccountTypes_Bool = false;
																	$scope.createDefaultBiz_Bool = true;
																	$scope.userMsg = "Default Business Plans created. ";
																});
											} else {
												$scope.createAccountTypes_Bool = false;
												$scope.createDefaultBiz_Bool = true;
												$scope.userMsg = "Default Business Plans are already created. No action was taken.";
											}

										});
					}

					$scope.createDefaultBusiness = function() {
						var protostarAdminService = appEndpointSF
								.getproadminService();
						protostarAdminService
								.getBusinessList()
								.then(
										function(bizList) {
											$scope.count = (bizList == undefined || bizList.items == undefined) ? 0
													: bizList.items.length;
											if ($scope.count == 0) {
												protostarAdminService
														.createDefaultBusiness()
														.then(
																function(
																		msgBean) {
																	$scope.userMsg = "Default Biz and Users created successfully.";
																	$scope.createDefaultBiz_Bool = false;
																});
											} else {
												$scope.userMsg = "Default Biz was already created. No action was taken.";
												$scope.createDefaultBiz_Bool = false;
											}
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
														.createAccountingGroups(
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
							gapi.auth.setToken({
								access_token : "InitSetup"
							});
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();
				});
