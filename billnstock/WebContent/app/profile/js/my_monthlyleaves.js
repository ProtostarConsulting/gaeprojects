angular.module("stockApp").controller(
		"view_monthlyleaveApps",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, $mdDialog, $mdMedia,
				appEndpointSF, $state) {

			$scope.curuser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			$scope.leaveApp = {
				startDate : new Date(),
				endDate : new Date(),
				approved : false,
				halfDay : false,
				totalDays : 0,
				approvedDate : null,
				user : $scope.curuser,
				manager : null,
				note : null
			};

			$scope.myLeaveAppList = [];

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

					$scope.leaveAppListFilterFunc();

				} else {
					$log.debug("Services Not Loaded, waiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();

		});
