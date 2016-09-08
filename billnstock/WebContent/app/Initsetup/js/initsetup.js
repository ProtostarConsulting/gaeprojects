angular
		.module("stockApp")
		.controller(
				"initsetup",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF) {

					$scope.showSimpleToast = function(msgBean) {
						$mdToast.show($mdToast.simple().content(msgBean)
								.position("top").hideDelay(3000));
					};
					$scope.step1;
					$scope.step2;
					$scope.step3;
					$scope.step4=false;
					$scope.initsetup = function() {

						var proadminService = appEndpointSF
								.getproadminService();
						proadminService
								.getallAccountType()
								.then(
										function(assetList) {

											$scope.accountlist = (assetList == undefined || assetList.items == undefined) ? 0
													: assetList.items.length;
											if ($scope.accountlist == 0) {
												proadminService
														.initsetup()
														.then(
																function(
																		msgBean) {
																	// $scope.showSimpleToast("setup
																	// 1 accout
																	// type
																	// created");
																	$scope.step1 = "Account Type Is Created Click Next To Add User ";
																	/*
																	 * angular.element(document.getElementById('addemp'))[0].disabled =
																	 * false;
																	 */

																});
											} else {
												// $scope.showSimpleToast("setup
												// allredy done");
												$scope.step1 = "Setup Already  Done";
												/*
												 * angular.element(document.getElementById('addemp'))[0].disabled =
												 * true;
												 */
											}

										});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.initsetup();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();
					$scope.addemp = function() {
						var proadminService = appEndpointSF
								.getproadminService();
						proadminService
								.getallAccountType()
								.then(
										function(assetList) {
											$scope.accountlist =  (assetList == undefined || assetList.items == undefined) ? 0: assetList.items.length;
											
											if ($scope.accountlist == 4) {
												proadminService
														.getAllemp()
														.then(
																function(
																		empList) {
																	$scope.emps = (empList == undefined || empList.items == undefined) ? 0: empList.items.length;
																	
																	if ($scope.emps == 0) {
																		proadminService
																				.initsetupnext()
																				.then(
																						function(
																								msgBean) {
																							$scope.step2 = "Add User Successfully ";

																						});
																	} else {
																		$scope.step2 = "Setup Is Finish";
																	}

																});
											} else {
												$scope.step2 = "Account Type Is Not In Daabase Agin Click On Next Button";
											}

										});
					}

					
					
					
					
					$scope.addgroup = function() {
						
						var proadminAddGroup= appEndpointSF
						.getproadminService();
						
						proadminAddGroup.creatAccountAndGroup().then(function(resp){
							
						if(resp!=undefined)
							{
							
							
							
							$scope.step3="Settup of Group List is Done........."; 
							$scope.step4=true;
							}
						
							
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
