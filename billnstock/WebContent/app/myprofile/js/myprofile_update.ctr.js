angular.module("stockApp").controller(
		"updateMyProfileCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$log, $q, appEndpointSF,$state) {
			
			$scope.showSavedToast = function() {
				$mdToast.show($mdToast.simple().content('Profile Updated!')
						.position("top").hideDelay(3000));
			};
	
			$scope.curuser=appEndpointSF.getLocalUserService().getLoggedinUser();
			$log.debug("$scope.curuser : "+angular.toJson($scope.curuser));
			
			$scope.tempUser =$scope.curuser;
		
			$scope.updateUser = function() {
				$log.debug("No1");
				var UserService = appEndpointSF.getUserService();
				UserService.updateUser($scope.tempUser).then(function(msgBean) {
					$log.debug("No6");
					$log.debug("Inside Ctr updateUser");
					$log.debug("msgBean.msg:" + msgBean.msg);
					$scope.showUpdateToast();
					$scope.tempUser = {
						
						};
					$state.go("home");
				});
				$log.debug("No4");
			}

			$scope.skip = function() {
				
				$state.go("home");

			}
		
		});
