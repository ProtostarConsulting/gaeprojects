angular
		.module("stockApp")
		.controller(
				"ListAccountType",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF) {

					$scope.curuser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.query = {
							order : 'planName',
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
							$scope.getallAccountType();
						}
					}
					
					
					
					$scope.accountType = {
						/* loggedInUser : "", */
						planName:"",
						description : "",
						maxuser : "",
						paymentDesc : ""
					}
					$scope.selectedAccountType = $stateParams.selectedAccountType;

					$scope.accountType = $scope.selectedAccountType ? $scope.selectedAccountType
							: $scope.accountType;

					$scope.getallAccountType = function() {
						$scope.loading = true;
						var proadminService = appEndpointSF
								.getproadminService();
						proadminService.getBusinessPlans().then(
								function(assetList) {
									$scope.accountlist = assetList.items;
									$scope.loading = false;
								});
					}
					$scope.accountlist = [];

					$scope.updateAccountType = function() {
						/* $scope.AccountType.loggedInUser = $scope.curUser; */

						var proadminService = appEndpointSF
								.getproadminService();
						proadminService.addBusinessPlan($scope.accountType)
								.then(function(msgBean) {
									$scope.showUpdateToast();

								});
					}

					// -------------------use to lode
					// service------------------------------

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getallAccountType();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					// -----------------------------------------------------------------------

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
