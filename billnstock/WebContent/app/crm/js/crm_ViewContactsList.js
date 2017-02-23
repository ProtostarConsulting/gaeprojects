angular.module("stockApp").controller(
		"ViewContactsList",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, appEndpointSF) {

			$scope.showSimpleToast = function(msgBean) {
				$mdToast.show($mdToast.simple().content(msgBean)
						.position("top").hideDelay(3000));
			};
			$scope.selectedCustomerId = $stateParams.selectedCustomerId;

			$scope.curUser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			$scope.query = {
				order : 'name',
				limit : 5,
				page : 1
			};

			$scope.getContactByCustomerId = function() {
				$log.debug("Inside Ctr $scope.getAlllead");
				var leadService = appEndpointSF.getleadService();
				$scope.ctaskid;
				if (typeof $scope.selectedCustomerId != "undefined") {
					leadService.getContactByCustomerId(
							$scope.selectedCustomerId).then(
							function(contactList) {
								$log.debug("Inside Ctr getAllleads");
								$scope.contactL = contactList.items;
							});

				}
			}
			$scope.contactL = [];

			$scope.waitForServiceLoad1 = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getContactByCustomerId();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad1, 1000);
				}
			}
			$scope.waitForServiceLoad1();

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
