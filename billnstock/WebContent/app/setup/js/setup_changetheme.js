angular.module("stockApp").controller(
		"setup.changetheme",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, $mdDialog, $mdMedia, $state,
				appEndpointSF) {

			$log.debug("Inside setup.changetheme Ctr...");

			$scope.curUser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			$scope.checkTheme = function(themeName) {
				//var change = confirm("Are you sure to chane thim");
				$scope.changeTheme(themeName);
			
			}
			
			
				$scope.saveTheme=function(themeName){
					$scope.curUser.business.theme=themeName;
						var UserService = appEndpointSF.getUserService();
						UserService.updateBusiness($scope.curUser.business).then(
								function(msgBean) {
									$scope.showUpdateToast();
								});
										
					
					}			

		});
