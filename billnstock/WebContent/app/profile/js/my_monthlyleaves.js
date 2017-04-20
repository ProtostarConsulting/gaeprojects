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
				page : 1,
				totalSize : 0,
				pagesLoaded : 0
			};

			$scope.empLeaveBalance;

			$scope.logOrder = function(order) {
				console.log('order: ', order);
			};

			$scope.logPagination = function(page, limit) {
				console.log('page: ', page);
				console.log('limit: ', limit);
				$location.hash('tp1');
				$anchorScroll();
				if ($scope.query.page > $scope.query.pagesLoaded) {
					$scope.getLeaveAppsByUser();
				}
			}

			$scope.getLeaveBalanceFn = function() {

				var hrService = appEndpointSF.gethrService();
				hrService.getLeaveMasterListByUser($scope.curuser.business.id,
						$scope.curuser.id).then(function(leaveMastersList) {
					if (leaveMastersList && leaveMastersList.length > 0) {
						$scope.empLeaveBalance = leaveMastersList[0].balance;
					} else {
						$scope.empLeaveBalance = 0;
					}

				});

			}

			$scope.getLeaveAppsByUser = function() {
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
					$scope.getLeaveAppsByUser();

				} else {
					$log.debug("Services Not Loaded, waiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();

		});
