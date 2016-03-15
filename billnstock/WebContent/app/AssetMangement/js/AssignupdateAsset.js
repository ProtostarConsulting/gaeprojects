angular
		.module("stockApp")
		.controller(
				"AssignupdateAsset",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory, $q,
						appEndpointSF, tableTestDataFactory) {
					$scope.showSimpleToast = function(msgBean) {
						$mdToast.show($mdToast.simple().content(msgBean)
								.position("top").hideDelay(3000));
					};

					$scope.selectedasetNo = $stateParams.selectedasetNo;

					$scope.curuser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.getselectedasset = function() {
						var assetService = appEndpointSF
								.getAssetManagementService();
						if (typeof $scope.selectedasetNo != "undefined") {
							assetService
									.getselectedasset($scope.selectedasetNo)
									.then(function(assetdetail) {
										$scope.asset = assetdetail.result;
									});
						}
					}
					$scope.asset = [];
					$scope.getselectedasset();

					$scope.updateAsset = function() {
						var assetService = appEndpointSF
								.getAssetManagementService();
						assetService.updateAsset($scope.asset).then(
								function(msgBean) {
									$scope.showSimpleToast(msgBean.msg);

								});
					}

					$scope.getAllemps = function() {
						$log.debug("Inside Ctr $scope.getAllemps");
						var hrService = appEndpointSF.gethrService();

						hrService.getAllemp($scope.curUser.businessAccount.id)
								.then(function(empList) {
									$log.debug("Inside Ctr getAllemps");
									$scope.emps = empList.items;

								});
					}

					$scope.emps = [];
					$scope.getAllemps();

					$scope.Assignasset = {
						assetEntity : "",
						userEntity : "",
						status : "inactive"
					}

					$scope.AssignAsset = function() {
						$log.debug("id=======" + $scope.user);
						$scope.Assignasset.assetEntity = $scope.asset;
						var setupService = appEndpointSF.getsetupService();
						setupService
								.getuserById($scope.user)
								.then(
										function(userList) {
											$log
													.debug("Inside Ctr getAllleads");
											$scope.userL = userList.result;
											$scope.Assignasset.userEntity = $scope.userL;

											var assetService = appEndpointSF
													.getAssetManagementService();

											assetService
													.addAssignAsset(
															$scope.Assignasset)
													.then(
															function(msgBean) {
																$scope
																		.showSimpleToast(msgBean.msg);

																$scope
																		.getselectedassetdetail();

															});

										});

					}
					// ----------hide and show ---------------------------
					$scope.IsHidden = true;
					$scope.ShowHide = function() {
						$scope.IsHidden = $scope.IsHidden ? false : true;
						$scope.getselectedassetdetail();
					}
					// -----------------------------------------------------

					$scope.getselectedassetdetail = function() {
						var assetService = appEndpointSF
								.getAssetManagementService();
						if (typeof $scope.selectedasetNo != "undefined") {
							assetService
									.getselectedassetdetail(
											$scope.selectedasetNo)
									.then(
											function(assetdetail) {
												$scope.assetdetail = [];
												$scope.activeassetdetail = [];
												for (i = 0; i < assetdetail.items.length; i++) {
													if (assetdetail.items[i].status == "active") {
														$scope.activeassetdetail
																.push(assetdetail.items[i]);
													} else {
														$scope.assetdetail
																.push(assetdetail.items[i]);
													}
												}
											});
						}
					}
					$scope.assetdetail = [];
					$scope.activeassetdetail = [];
					$scope.getselectedassetdetail();

					$scope.releaseAsset = function(id) {

						var assetService = appEndpointSF
								.getAssetManagementService();
						assetService.releaseAsset(id).then(function(msgBean) {
							$scope.showSimpleToast(msgBean.msg);

							$scope.getselectedassetdetail();
						});

					}

				/*	// ////////////////////////////////////////auto complate
					// ////////////////////

					// list of `state` value/display objects
					$scope.states = loadAll();
					$scope.selectedItem = null;
					$scope.searchText = null;

					// ******************************
					// Internal methods
					// ******************************
					*//**
					 * Search for states... use $timeout to simulate remote
					 * dataservice call.
					 *//*
					$scope.querySearch = function(query) {
						var results = query ? $scope.states
								.filter(createFilterFor(query)) : $scope.states;
						var deferred = $q.defer();
						$timeout(function() {
							deferred.resolve(results);
						}, Math.random() * 1000, false);
						return deferred.promise;
					}
					*//**
					 * Build `states` list of key/value pairs
					 *//*
					function loadAll() {
						
						 * var allStates = 'Alabama, Alaska, Arizona, Arkansas,
						 * California, Colorado, Connecticut, Delaware,\
						 * Florida, Georgia, Hawaii, Idaho, Illinois, Indiana,
						 * Iowa, Kansas, Kentucky, Louisiana,\ Maine, Maryland,
						 * Massachusetts, Michigan, Minnesota, Mississippi,
						 * Missouri, Montana,\ Nebraska, Nevada, New Hampshire,
						 * New Jersey, New Mexico, New York, North Carolina,\
						 * North Dakota, Ohio, Oklahoma, Oregon, Pennsylvania,
						 * Rhode Island, South Carolina,\ South Dakota,
						 * Tennessee, Texas, Utah, Vermont, Virginia,
						 * Washington, West Virginia,\ Wisconsin, Wyoming';
						 
						var hrService = appEndpointSF.gethrService();
						var allStates;
						hrService.getAllemp($scope.curUser.businessAccount.id)
								.then(
										function(empList) {
											$scope.fnames = empList.items;

											for (i = 0; i < $scope.fnames.length; i++) {

												allStates = allStates+ ","+ $scope.fnames[i].firstName;
											}
										
						return allStates.split('/,+/g').map(function(state) {
							return {
								value : state.toLowerCase(),
								display : state
							};
						});
				});

						
						 * if (typeof userList != 'undefined'){
						 *  }
						 
					}
					$scope.fnames = [];

					*//**
					 * Create filter function for a query string
					 *//*
					function createFilterFor(query) {
						var lowercaseQuery = angular.lowercase(query);
						return function filterFn(state) {
							return (state.value.indexOf(lowercaseQuery) === 0);
						};
					}
					// ////////////Auto complete code ends//////////////////////
*/
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
