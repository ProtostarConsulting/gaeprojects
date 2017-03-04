angular.module("stockApp").controller(
		"payRollReports",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, appEndpointSF, $mdDialog,
				$mdMedia, $state) {

			$scope.yearFilterList = [ String(new Date().getFullYear()),
					String(new Date().getFullYear() - 1) ];

			$scope.getPayRollReport = function(year) {
				$scope.loading = true;
				var hrService = appEndpointSF.gethrService();

				$scope.totalSal = 0;
				$scope.totalPF = 0;
				$scope.totalPT = 0;
				$scope.totalCanteen = 0;
				$scope.totalIT = 0;
				$scope.totalOther = 0;
				$scope.totalESI = 0;

				hrService.getpayRollReport($scope.curUser.business.id, year)
						.then(function(list) {
							$scope.list = list;
							$scope.payRollReportList = list;
							for (var i = 0; i < list.length; i++) {
								$scope.totalSal += list[i].totalSal;
								$scope.totalPF += list[i].totalPF;
								$scope.totalPT += list[i].totalPT;
								$scope.totalCanteen += list[i].totalCanteen;
								$scope.totalIT += list[i].totalIT;
								$scope.totalOther += list[i].totalOther;
								$scope.totalESI += list[i].totalESI;
							}
							$scope.loading = false;
						});

			}

			$scope.waitForServiceLoad = function() {
				if ($stateParams.payRollReportList) {
					$scope.list = $stateParams.payRollReportList
				} else if (appEndpointSF.is_service_ready) {
					$scope.getPayRollReport($scope.yearFilterList[0]);
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();

		});
