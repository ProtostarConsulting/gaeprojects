angular.module("stockApp").controller(
		"payRollReports",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, appEndpointSF, $mdDialog,
				$mdMedia, $state) {

			$scope.getPayRollReport = function() {
				$scope.loading = true;
				var hrService = appEndpointSF.gethrService();

				$scope.totalSal = 0;
				$scope.totalPF = 0;
				$scope.totalPT = 0;
				$scope.totalCanteen = 0;
				$scope.totalIT = 0;
				$scope.totalOther = 0;
				$scope.totalESI = 0;
				
				hrService.getpayRollReport($scope.curUser.business.id).then(
						function(list) {
							$scope.list = list;
							for (var i = 0; i < list.length; i++) {
								$scope.totalSal += list[i].total;
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
				if (appEndpointSF.is_service_ready) {
					$scope.getPayRollReport();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();

		});
