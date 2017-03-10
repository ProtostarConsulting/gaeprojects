angular.module("stockApp").controller(
		"counterList",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $mdMedia, $mdDialog, $log, $state, $filter,
				objectFactory, appEndpointSF) {

			$scope.loading = true;
			$scope.counterlist = [];

			$scope.query = {
				order : 'counterName',
				limit : 50,
				page : 1
			};

			$scope.getBusinessCounterList = function() {
				var UserService = appEndpointSF.getUserService();
				var bizId = Number($scope.curUser.business.id);
				UserService.getBusinessCounterList(bizId).then(
						function(result) {

							if (result.items) {
								$scope.counterlist = result.items;
							}
							$scope.loading = false;
						});
			}

			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getBusinessCounterList();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();

		});
