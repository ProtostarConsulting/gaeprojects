angular.module("stockApp").controller(
		"viewMyLeaveAppsCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, $mdDialog, $mdMedia,
				appEndpointSF, $state) {

			$scope.curuser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			$scope.selectedUser = $stateParams.selectedUser;

			$scope.myLeaveAppList = [];
			
			$scope.query = {
					order : 'leaveApp.startDate',
					limit : 50,
					page : 1
				};

			$scope.empLeaveBalance;
			
			$scope.getLeaveBalanceFn = function() {

				var hrService = appEndpointSF.gethrService();
				hrService.getLeaveMasterListByUser(
						$scope.curuser.business.id, $scope.curuser.id)
						.then(function(list) {
							$scope.empLeaveBalance = list[0].balance;
						});
				
			}
			
			$scope.leaveAppListFilterFunc = function() {
				var hrService = appEndpointSF.gethrService();
				hrService.getLeaveAppListByUser($scope.curuser.business.id,
						$scope.curuser.id).then(function(list) {
					$scope.myLeaveAppList = list;
					angular.forEach($scope.myLeaveAppList, function(leaveApp) {
						$scope.calculateTotalNoOfLeaves(leaveApp);
					})
				});
				$scope.loading = false;
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
					$scope.getLeaveBalanceFn();
					$scope.leaveAppListFilterFunc();

				} else {
					$log.debug("Services Not Loaded, waiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();

		});
