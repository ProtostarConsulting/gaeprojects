angular
		.module("stockApp")
		.controller(
				"leavemasterCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

					$scope.curuser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.leaveMasterTemp = {
						user : "",
						balance : 0,
						business : $scope.curUser.business
					};

					$scope.query = {
						order : 'balance',
						limit : 50,
						page : 1,
						totalSize : 0,
						pagesLoaded : 0
					};

					$scope.leaveMaster = $stateParams.selectedLeaveMasterObj ? $stateParams.selectedLeaveMasterObj
							: $scope.leaveMasterTemp;

					$scope.userList = [];
					$scope.leaveMasterList = [];

					$scope.getUsersList = function() {
						var UserService = appEndpointSF.getUserService();
						UserService.getUsersByBusinessId(
								$scope.curuser.business.id).then(
								function(list) {
									$scope.userList = list.items;

									$scope.loading = false;
								});
					}

					$scope.addLeaveBalance = function() {

						var hrService = appEndpointSF.gethrService();

						hrService.addLeaveMaster($scope.leaveMaster).then(
								function() {
									$scope.showAddToast();
								});
						$scope.loading = false;
					}

					$scope.logOrder = function(order) {
						console.log('order: ', order);
					};

					$scope.logPagination = function(page, limit) {
						console.log('page: ', page);
						console.log('limit: ', limit);
						$location.hash('tp1');
						$anchorScroll();
						if ($scope.query.page > $scope.query.pagesLoaded) {
							$scope.getEmpLeaveMasters();
						}
					}

					$scope.getEmpLeaveMasters = function() {
						var hrService = appEndpointSF.gethrService();

						hrService.getLeaveMasterList().then(function(list) {
							$scope.leaveMasterList = list;

						});

					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getUsersList();
							$scope.getEmpLeaveMasters();

						} else {
							$log.debug("Services Not Loaded, waiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

				});
