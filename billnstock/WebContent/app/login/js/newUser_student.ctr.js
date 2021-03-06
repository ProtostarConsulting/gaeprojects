angular.module("stockApp").controller(
		"newUserStudentCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$log, $q, $location, objectFactory, appEndpointSF,
				$state) {

			$scope.showSavedToast = function(msg) {
				$mdToast.show($mdToast.simple().content(msg)
						.position("top").hideDelay(3000));
			};
			
			//$scope.userRole = [admin,student,teacher]

			$scope.tempUser = {
				userId : "",
				name : "",
				userName : "",
				email_id : "",
				address : "",
				contact : "",
				pwd : "",
				role : "pimpale",
				book:""
				//book : [],
				//exam : []
			};
			$scope.loginMsg  = "";
			$scope.users = [];

			$scope.addUser = function() {
				$log.debug("No1");
				var UserService = appEndpointSF.getUserService();
				UserService.addUser($scope.tempUser).then(function(msgBean) {
					$log.debug("No6");
					$log.debug("Inside Ctr addStudent");
					$log.debug("msgBean.msg:" + msgBean.msg);
					$scope.showAddToast();
					$scope.tempUser = {
													
						};
				});
				$log.debug("No4");
				
			}

			$scope.getUser = function() {
				var UserService = appEndpointSF.getUserService();

				UserService.getUsers().then(function(userList) {
					$log.debug("Inside Ctr getLogin");
					$scope.users = userList;
				});
			}

			/*$scope.login = function() {
				appEndpointSF.getUserService().login($scope.tempUser.userName, $scope.tempUser.pwd).then(
						function(result) {
							if (result){
								$log.debug("User logged in successfully: "
										+ $scope.tempUser.userName);
								$window.location.reload();
								$state.go("home");
								$scope.loginMsg  = "";
							}
							else {
								$log.debug("User loggin falied:"
										+ $scope.tempUser.userName);
								$scope.loginMsg="Login failed.";
							}
						}

				)

			}*/

			/* Setup page menu */
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