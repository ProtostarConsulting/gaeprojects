angular
		.module("stockApp")
		.controller(
				"viewProfile",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF) {
					
					$scope.showSimpleToast = function(msgBean) {
						$mdToast.show($mdToast.simple().content(msgBean)
								.position("top").hideDelay(3000));
					};
					
					$scope.printempidsalslip = $stateParams.printempidsalslip;
					$scope.viewsalslips = $stateParams.viewsalslips;
					$scope.curuser = appEndpointSF.getLocalUserService().getLoggedinUser();
	
					$scope.getuserById = function() {
						$log.debug("Inside Ctr $scope.getuserById");
						var UserService = appEndpointSF.getUserService();
						if (typeof $scope.curuser.id != 'undefined') {
							UserService.getUserByID($scope.curuser.id).then(
									function(userList) {
										$log.debug("Inside Ctr getAllleads");
										$scope.userL = userList.result;

									});
						}
					}

					$scope.userL = [];
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getuserById();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();
					$scope.updateuser = function() {
						var UserService = appEndpointSF.getUserService();
						UserService.updateUser($scope.userL).then(function(msgBean) {
							$scope.showUpdateToast();
						});
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
