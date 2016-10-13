angular
		.module("stockApp")
		.controller(
				"LeaveDetailsList",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF,$mdDialog, $mdMedia, $state) {
				
					$scope.list=function()
					{
						var hrService=appEndpointSF.gethrService();
					hrService.getLeaveList($scope.curUser.business.id).then(function(list)
								{
							$scope.leaveList=list;
							
						});
						
					}
					
					
					$scope.list();
					
					
					
					
					
				});
