angular
		.module("stockApp")
		.controller(
				"addViewEmployeeLeavesCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

					$scope.curuser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.selectedUser = $stateParams.selectedUser;
					
					$scope.query = {
							order : 'leaveApp.startDate',
							limit : 50,
							page : 1
						};


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
						business : $scope.curUser.business
					};

					$scope.leaveApp = $stateParams.selectedLeaveAppObj ? $stateParams.selectedLeaveAppObj
							: $scope.leaveAppTemp;

					if ($stateParams.selectedLeaveAppObj) {
						$scope.leaveApp.startDate = new Date(
								$scope.leaveApp.startDate);
						$scope.leaveApp.endDate = new Date(
								$scope.leaveApp.endDate);

					}

					$scope.employeeLeaveAppList = [];

					$scope.userList = [];

					$scope.myLeaveAppList = [];

					$scope.getUsersList = function() {
						var UserService = appEndpointSF.getUserService();
						UserService.getUsersByBusinessId(
								$scope.curuser.business.id).then(
								function(list) {
									$scope.userList = list.items;

									$scope.loading = false;
								});
					}

					$scope.empLeaveBalance;

					$scope.getLeaveBalanceFn = function() {

						var hrService = appEndpointSF.gethrService();
						hrService.getLeaveMasterListByUser(
								$scope.curuser.business.id, $scope.curuser.id)
								.then(function(list) {
									$scope.empLeaveBalance = list.length > 0? list[0].balance:'0';
								});
						
					};
					$scope.showLeaveAddToast = function() {
						$mdToast.show($mdToast.simple().content(
								'Leave Application Saved Successfully.')
								.position("top").hideDelay(3000));
					};

					$scope.addMoreLeaves = function() {

						var hrService = appEndpointSF.gethrService();

						if ($scope.empLeaveBalance >= $scope.leaveApp.totalDays) {
							hrService.addLeaveApp($scope.leaveApp).then(
									function() {
										$scope.showLeaveAddToast();

									});
						} 
						$scope.loading = false;
					}

					$scope.leaveAppListFilterFunc = function() {
						var hrService = appEndpointSF.gethrService();
						hrService
								.getLeaveAppListByUser(
										$scope.curuser.business.id,
										$scope.curuser.id)
								.then(
										function(list) {
											$scope.myLeaveAppList = list;
											angular
													.forEach(
															$scope.myLeaveAppList,
															function(leaveApp) {
																$scope
																		.calculateTotalNoOfLeaves(leaveApp);
															})
										});
						$scope.loading = false;
					}

					$scope.empLeaveAppListFn = function() {
						var hrService = appEndpointSF.gethrService();
						hrService
								.getLeaveAppListByManager(
										$scope.curuser.business.id,
										$scope.curuser.id)
								.then(
										function(list) {
											$scope.employeeLeaveAppList = list;
											angular
													.forEach(
															$scope.employeeLeaveAppList,
															function(leaveApp) {
																$scope
																		.calculateTotalNoOfLeaves(leaveApp);
															})
										});

					}

					$scope.calculateTotalNoOfLeaves = function(leaveApp) {
						var leaveStartDate = new Date(leaveApp.startDate);
						var leaveEndDate = new Date(leaveApp.endDate);
						var diffInDays = Math
								.ceil((leaveEndDate.getTime() - leaveStartDate
										.getTime())
										/ (1000 * 3600 * 24));
						if (leaveApp.halfDay) {
							var leaveDays = 0.5 + diffInDays
						} else {
							leaveDays = diffInDays + 1;
						}
						leaveApp.totalDays = leaveDays;
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getUsersList();
							$scope.getLeaveBalanceFn();
							$scope.leaveAppListFilterFunc();
							$scope.empLeaveAppListFn();

						} else {
							$log.debug("Services Not Loaded, waiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

				});
