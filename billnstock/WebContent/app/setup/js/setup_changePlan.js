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

					/* get Account Type */

					$scope.getBusinessPlans = function() {
						var proadminService = appEndpointSF
								.getproadminService();
						proadminService
								.getBusinessPlans()
								.then(
										function(planList) {
											$scope.planList = planList.items;
										});
					}
					$scope.accountlist = [];
					$scope.newSelectedPlan = $scope.selectedBusiness.businessPlan;

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getBusinessPlans();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.updatePlan = function() {
						$scope.selectedBusiness.businessPlan = $scope.newSelectedPlan;
						var UserService = appEndpointSF.getUserService();
						// addbusiness use in number of
						// place don't update service method
						UserService.updateBusiness($scope.selectedBusiness)
								.then(function(business) {
									$scope.showUpdateToast();
								});
					}

				});
