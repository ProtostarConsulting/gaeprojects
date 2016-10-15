angular
		.module("stockApp")
		.controller(
				"SalaryMaster",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF,$mdDialog, $mdMedia, $state) {
					
					
					
					
					$scope.getEmptyEmployeeLeaveDetails = function(emp) {
						return{
							empAccount:emp,
							grosssal:"",
							 basic:"",
							 HRA:"",
							 convence:"",
							 medical:"",
							  education:"",
							  adhocAllow:"",
							  specialAllow:"",
							  business : $scope.curUser.business
							
					};
				}
					$scope.employeeLeaveDetailsList = [];
					$scope.list = function() {
						var hrService = appEndpointSF.gethrService();

						hrService
								.getAllemp($scope.curUser.business.id)
								.then(
										function(list) {

											$scope.employeeLeaveDetailsList.length=0;
											for (var i = 0; i < list.length; i++) {
											
												
												$scope.employeeLeaveDetailsList.push($scope.getEmptyEmployeeLeaveDetails(list[i]));
												

											}

										});

					}
					
					
					$scope.getSalarylist = function() {
						


						var hrService = appEndpointSF.gethrService();
							hrService.getSalaryMasterlist($scope.curUser.business.id).then(
									function(list) {

										if(list.length==0){
											$scope.list();	}
										else
											$scope.employeeLeaveDetailsList.length=0;
										for (var i = 0; i < list.length; i++) {
											
										$scope.employeeLeaveDetailsList.push(list[i]);
										
										$scope.calculation(i);
										
										}
										
									

									});
						
					}
	
					
					$scope.calculation=function (index){
						
						if($scope.employeeLeaveDetailsList[index].grosssal!==undefined){
						var cal;
						
						$scope.employeeLeaveDetailsList[index].basic =$scope.employeeLeaveDetailsList[index].grosssal*(40/100);
						$scope.employeeLeaveDetailsList[index].convence=2000;
						$scope.employeeLeaveDetailsList[index].medical=1250;
						$scope.employeeLeaveDetailsList[index].education=200;
						$scope.employeeLeaveDetailsList[index].specialAllow=$scope.employeeLeaveDetailsList[index].grosssal-($scope.employeeLeaveDetailsList[index].convence+$scope.employeeLeaveDetailsList[index].basic+$scope.employeeLeaveDetailsList[index].medical+$scope.employeeLeaveDetailsList[index].education);
						
						}
					}
					$scope.addEmpLeav=function(){

						var hrService = appEndpointSF.gethrService();
						

						for (var i = 0; i < $scope.employeeLeaveDetailsList.length; i++) {
							
						
							
							hrService.saveSalaryMasterDetail($scope.employeeLeaveDetailsList[i]).then(
									function() {

										$scope.showAddToast();

									});
						}
					}
					
					$scope.getSalarylist ();
					
					
					
				});
