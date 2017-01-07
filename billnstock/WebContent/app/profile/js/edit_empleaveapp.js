angular.module("stockApp")
		.controller(
				"editEmployeeLeaveAppsCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

					$scope.curuser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.leaveAppTemp = {
						startDate : new Date(),
						endDate : new Date(),
						approved : false,
						halfDay : false,
						totalDays : 0,
						approvedDate : null,
						user : $scope.curuser,
						manager : "",
						note : null,
					};

					$scope.leaveApp = $stateParams.selectedLeaveAppObj;

					if ($stateParams.selectedLeaveAppObj) {
						$scope.leaveApp.startDate = new Date(
								$scope.leaveApp.startDate);
						$scope.leaveApp.endDate = new Date(
								$scope.leaveApp.endDate);

					}

					$scope.userList = [];

					$scope.getUsersList = function() {
						var UserService = appEndpointSF.getUserService();
						UserService.getUsersByBusinessId(
								$scope.curuser.business.id).then(
								function(list) {
									$scope.userList = list.items;

									$scope.loading = false;
								});
					}

					$scope.showApprovalToast = function() {
						$mdToast.show($mdToast.simple().content(
								'You have approved this leave application.')
								.position("top").hideDelay(3000));
					};

					$scope.isDisabled = false;

					$scope.approveLeaveAppFunc = function() {
						$scope.leaveApp.modifiedBy = $scope.curuser.firstName
								+ ' ' + $scope.curuser.lastName;
						$scope.leaveApp.approvedDate = new Date();

						var hrService = appEndpointSF.gethrService();

						hrService.approveLeaveApp($scope.leaveApp);
						$scope.showApprovalToast();
						$scope.isDisabled = true;
						$scope.loading = false;

					}

					$scope.previousPage = function() {
						window.history.back();
						window.history.back();
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getUsersList();
						} else {
							$log.debug("Services Not Loaded, waiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

				});
