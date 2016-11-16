angular.module("stockApp").controller(
		"probusinessCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $mdMedia, $mdDialog, $log, $state, objectFactory,
				appEndpointSF) {
			$scope.loading = true;
			$scope.query = {
				order : 'id',
				limit : 50,
				page : 1
			};

			$scope.showSimpleToast = function(msgBean) {
				$mdToast.show($mdToast.simple().content(msgBean)
						.position("top").hideDelay(3000));
			};

			$scope.changeAuthView = function(params) {
				$state.go("proAdmin.managebizauth", params);
			}

			$scope.getBusinessList = function() {
				$scope.loading = true;
				$log.debug("Inside Ctr $scope.getBusinessList");
				var UserService = appEndpointSF.getUserService();
				UserService.getBusinessList().then(function(businessList) {
					$scope.businesslist = businessList.items;
					$scope.loading = false;
				});

			}

			$scope.businesslist = [];

			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getBusinessList();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();

			$scope.selected = [];

			$scope.suspendUserStatus = function() {
				var suspended = "suspended"
				$scope.selected[0].status = suspended;
				var UserService = appEndpointSF.getUserService();
				UserService.updateBusiness($scope.selected[0]).then(
						function(msgBean) {
							$scope.showSimpleToast(msgBean.msg);
							$scope.getBusinessList();
						});
			}
			$scope.activeUserStatus = function() {
				var active = "active"
				$scope.selected[0].status = active;
				var UserService = appEndpointSF.getUserService();
				UserService.updateBusiness($scope.selected[0]).then(
						function(msgBean) {
							$scope.showSimpleToast(msgBean.msg);
							$scope.getBusinessList();
						});
			}

			// -----------------------------------------------------------------------------------------------
			$scope.back = function() {
				window.history.back();
				// $state.go("^", {});
			};
			// ----------hide and show ---------------------------

			$scope.IsHidden = true;
			$scope.ShowHide = function() {
				$scope.IsHidden = $scope.IsHidden ? false : true;
			}
			// -----------------------------------------------------

			$scope.condition = function() {
				if ($scope.user.isGoogleUser == false) {
					return true;
				} else {
					return false
				}
			}

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
