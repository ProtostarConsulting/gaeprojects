angular.module("stockApp").controller(
		"changepass",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, appEndpointSF) {

			$scope.curUser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			$scope.changePassword = function() {

				if ($scope.password == $scope.confirmpassword) {
					$scope.savemsg = true;
					$scope.checkpass = false;
				} else {
					$scope.checkpass = true;
					$scope.savemsg = false;
				}

				if ($scope.savemsg == true) {
					/* $scope.userL.modifiedBy=$scope.curUser.email_id; */
					$scope.curUser.password = $scope.password;
					var UserService = appEndpointSF.getUserService();
					UserService.addUser($scope.curUser).then(function(user) {
						if (user.id)
							$scope.savemsg = true;
					});
				}
			}

			$scope.inputType1 = 'password';
			$scope.inputType2 = 'password';

			$scope.showpass1 = function() {
				if ($scope.inputType1 == 'password') {
					$scope.inputType1 = 'text';
				} else {
					$scope.inputType1 = 'password';
				}

			}
			$scope.showpass2 = function() {
				if ($scope.inputType2 == 'password') {
					$scope.inputType2 = 'text';
				} else {
					$scope.inputType2 = 'password';
				}
			}
			$scope.setpassinput1 = function() {
				$scope.inputType1 = 'password';
			}
			$scope.setpassinput2 = function() {
				$scope.inputType2 = 'password';
			}

		});
