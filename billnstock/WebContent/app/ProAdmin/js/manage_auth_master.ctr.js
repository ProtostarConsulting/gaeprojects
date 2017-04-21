angular
		.module("stockApp")
		.controller(
				"proAdminManageAuth",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $q, objectFactory, appEndpointSF) {
					$log.debug("Inside proAdminManageAuth");
					$scope.mode = "list";
					$scope.tempAuth = getEmptyAuth();
					$scope.selectedAuthStack = [];
					$scope.selectedAuth = {
						authorizations : []
					};
					;
					// $scope.mode = "add";
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					

					var authService = appEndpointSF.getAuthorizationService();
					$scope.authorizationMasterEntity = {
						authorizations : []
					};
					function getAllAuthorizations() {
						$scope.loading = true;
						authService
								.getAuthorizationMasterEntity()
								.then(
										function(result) {
											$log.debug("result:" + result);
											if (result
													&& result.authorizations != undefined) {
												angular
														.forEach(
																result.authorizations,
																function(auth) {
																	auth.orderNumber = parseFloat(auth.orderNumber);
																});

												$scope.authorizationMasterEntity.authorizations = result.authorizations;
												$log
														.debug("angular.toJson(result.authorizations):"
																+ angular
																		.toJson(result.authorizations));
												// initially selected auth is
												// master list
												$scope.selectedAuth = $scope.authorizationMasterEntity;
												$scope.selectedAuthStack
														.push($scope.authorizationMasterEntity);
											} else {
												// It is empty.
												$scope.selectedAuth = $scope.authorizationMasterEntity;
												$scope.selectedAuthStack
														.push($scope.authorizationMasterEntity);
											}
											$scope.mode = "list";
											$scope.loading = false;
										});

						$log.debug("Called getAllAuthorizations...");

					}

					$scope.saveAuthorization = function() {
						authService.saveAuthorizationMasterEntity(
								$scope.authorizationMasterEntity).then(
								function(result) {
									$log.debug("result:" + result);
									$scope.showUpdateToast();
									$scope.mode = "list";
								});
						$log.debug("Called saveAuthorization...");

					}

					$scope.editAuthorization = function(auth) {
						$log.debug("Called editAuthorization...");
						$scope.selectedAuth = auth ? auth : $scope.selected[0];
						if (!$scope.selectedAuth.authorizations) {
							$scope.selectedAuth.authorizations = [];
						}
						$scope.selectedAuthStack.push($scope.selectedAuth);
						$scope.query.page = 1;
					}

					$scope.deleteAuthorization = function(auth) {
						$log.debug("Called deleteAuthorization...Auth:" + auth.authDisplayName);
						$log.debug("$scope.selectedAuth.authorizations.indexOf:" + $scope.selectedAuth.authorizations.indexOf(auth));
						var indexToDle = $scope.selectedAuth.authorizations.indexOf(auth);
						var deletedAuth = $scope.selectedAuth.authorizations.splice(indexToDle, 1);						
						$log.debug("deletedAuth:" + deletedAuth[0].authDisplayName);
						$scope.query.page = 1;
					}

					$scope.jumpToAuth = function(index) {
						$log.debug("Called jumpToAuth...");
						$scope.selectedAuthStack.splice(index + 1,
								$scope.selectedAuthStack.length - index);
						$scope.selectedAuth = $scope.selectedAuthStack[index];
					}

					$scope.showAddAuthorization = function() {
						$scope.tempAuth = getEmptyAuth();
						$scope.mode = "add";
					}

					$scope.addAuthorization = function() {
						// Save newly added auth to server an then add it at
						// current auth level and show msg.
						$scope.selectedAuth.authorizations
								.push($scope.tempAuth);
						$scope.tempAuth = getEmptyAuth();
						$scope.saveAuthorization();
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							getAllAuthorizations();

						} else {
							$log
									.debug("proAdminManageAuth: Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					function getEmptyAuth() {
						return {
							id : '',
							authName : '',
							authDisplayName : '',
							uiStateName : '',
							orderNumber : '',
							authorizations : []
						};
					}

					$scope.selected = []
					$scope.query = {
						order : 'orderNumber',
						limit : 50,
						page : 1
					};
					$scope.logOrder = function(order) {
						console.log('order: ', order);
					};

					$scope.logPagination = function(page, limit) {
						console.log('page: ', page);						
						console.log('limit: ', limit);
						$location.hash('tp1');
						$anchorScroll();
						if ($scope.query.page > $scope.query.pagesLoaded) {
							getAllAuthorizations();
						}
					}

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

					$scope.backButton = function() {
						$scope.tempAuth = getEmptyAuth();
						$scope.selectedAuthStack.pop();
						if ($scope.selectedAuthStack.length > 1) {
							$scope.selectedAuth = $scope.selectedAuthStack[$scope.selectedAuthStack.length - 1];
						} else {
							$scope.selectedAuth = $scope.authorizationMasterEntity;
						}

						$scope.mode = "list";

					}

				});