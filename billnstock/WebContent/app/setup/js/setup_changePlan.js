angular
		.module("stockApp")
		.controller(
				"setup.changeplan",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory, $mdDialog,
						$mdMedia, $state, appEndpointSF) {

					$scope.curuser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.selectedBusiness = $stateParams.selectedBusiness ? $stateParams.selectedBusiness
							: $scope.curuser.business;

					$scope.getBusinessById = function() {
						var UserService = appEndpointSF.getUserService();
						UserService.getbusinessById($scope.selectedBusiness.id)
								.then(function(Business) {
									$scope.business = Business;
								});

					}

					$scope.business = {};

					/* get Account Type */

					$scope.getallAccountType = function() {
						var proadminService = appEndpointSF
								.getproadminService();
						proadminService
								.getallAccountType()
								.then(
										function(assetList) {
											$scope.accountlist1 = assetList.items;
											for (i = 0; i < $scope.accountlist1.length; i++) {
												if ($scope.business.accounttype.maxuser <= $scope.accountlist1[i].maxuser) {
													$scope.accountlist
															.push($scope.accountlist1[i]);
												}
											}

										});
					}
					$scope.accountlist = [];

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getBusinessById();
							$scope.getallAccountType();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.updatePlan = function() {

						var proadminService = appEndpointSF
								.getproadminService();
						proadminService
								.getAccountTypeById($scope.accounttype)
								.then(
										function(accountType) {
											$scope.business.accounttype = accountType.result;
											var UserService = appEndpointSF
													.getUserService();
											// addbusiness use in number of
											// place don't update service method
											UserService
													.addBusiness(
															$scope.business)
													.then(
															function(business) {
																$scope
																		.showUpdateToast();
															});
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
