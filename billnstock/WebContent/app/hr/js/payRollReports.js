angular
		.module("stockApp")
		.controller(
				"payRollReports",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {
					
					
					
					
					
					$scope.getdummy = function() {

						var hrService = appEndpointSF.gethrService();
					
						$scope.total=0;
						$scope.totalPF=0;
						$scope.totalPT=0;
						$scope.totalCanteen=0;
						$scope.totalIT=0;
						$scope.totalOther=0;
						hrService.getpayRollReport($scope.curUser.business.id).then(function(list) {
							$scope.list=list;
							for(var i=0;i<list.length;i++){
								$scope.total+=list[i].total;
								$scope.totalPF+=list[i].totalPF;
								$scope.totalPT+=list[i].totalPT;
								$scope.totalCanteen+=list[i].totalCanteen;
								$scope.totalIT+=list[i].totalIT;
								$scope.totalOther+=list[i].totalOther;
								
								
							}
							
							
						});

					}
					
					
					
					
					$scope.getdummy();
					
					
					
					
				});
