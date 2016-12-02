angular
		.module("stockApp")
		.controller(
				"AssignupdateAsset",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory, $q,
						appEndpointSF) {
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
									.then(
											function(assetdetail) {
												$scope.asset = assetdetail.result;
												$scope.asset.purchasedate = new Date(
														$scope.asset.purchasedate);
												$scope.asset.expirydate = new Date(
														$scope.asset.expirydate);
											});
						}
					}

					$scope.asset = [];
			

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getselectedasset();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();
					
					$scope.updateAsset = function() {
						var assetService = appEndpointSF
								.getAssetManagementService();
						assetService.updateAsset($scope.asset).then(
								function(msgBean) {
									$scope.showUpdateToast();

								});
					}

					$scope.getAllemps = function() {
						$log.debug("Inside Ctr $scope.getAllemps");
						var hrService = appEndpointSF.gethrService();

						hrService.getAllemp($scope.curUser.business.id)
								.then(function(empList) {
									$log.debug("Inside Ctr getAllemps");
									$scope.emps = empList.items;

								});
					}

					$scope.emps = [];
					
					$scope.waitForServiceLoad1 = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAllemps();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad1, 1000);
						}
					}
					$scope.waitForServiceLoad1();

					$scope.Assignasset = {
						assetEntity : "",
						userEntity : "",
						status : "inactive"
					}

					$scope.AssignAsset = function() {
						$log.debug("id=======@@@@@@@@@@@@"
								+ angular.toJson($scope.selectedItem));
						$scope.Assignasset.assetEntity = $scope.asset;
						$scope.Assignasset.userEntity = $scope.selectedItem;
						var assetService = appEndpointSF
								.getAssetManagementService();
						assetService.addAssignAsset($scope.Assignasset).then(
								function(msgBean) {
									$scope.showSimpleToast(msgBean.msg);
									$scope.getselectedassetdetail();
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
			
					
					$scope.waitForServiceLoad2 = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getselectedassetdetail();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad2, 1000);
						}
					}
					$scope.waitForServiceLoad2();
					
					
					$scope.releaseAsset = function(id) {

						var assetService = appEndpointSF
								.getAssetManagementService();
						assetService.releaseAsset(id).then(function(msgBean) {
							$scope.showSimpleToast(msgBean.msg);

							$scope.getselectedassetdetail();
						});

					}

					//auto complate
					// list of `state` value/display objects
					$scope.employees = [];
					
					$scope.waitForServiceLoad3 = function() {
						if (appEndpointSF.is_service_ready) {
							loadAll();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad3, 2000);
						}
					}
					$scope.waitForServiceLoad3();
					
					
					$scope.selectedItem = null;
					$scope.searchText = null;

					// ******************************
					// Internal methods
					// ******************************
					/**
					 * Search for states... use $timeout to simulate remote
					 * dataservice call.
					 */
					$scope.querySearch = function(query) {
						var results = query ? $scope.employees
								.filter(createFilterFor(query))
								: $scope.employees;
						var deferred = $q.defer();
						$timeout(function() {
							deferred.resolve(results);
						}, Math.random() * 1000, false);
						return deferred.promise;
					}
					/**
					 * Build `states` list of key/value pairs
					 */
					function loadAll() {

						var hrService = appEndpointSF.gethrService();
						var allStates;
						hrService.getAllemp($scope.curUser.business.id)
								.then(function(empList) {
									$scope.employees = empList.items;

								});

					}
					$scope.fnames = [];

					/**
					 * Create filter function for a query string
					 */
					function createFilterFor(query) {
						var lowercaseQuery = angular.lowercase(query);
						return function filterFn(emp) {
							return (angular.lowercase(emp.firstName).indexOf(lowercaseQuery) === 0);
						};
					}
					// ////////////Auto complete code ends//////////////////////

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
