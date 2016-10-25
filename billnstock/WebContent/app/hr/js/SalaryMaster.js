angular
		.module("stockApp")
		.controller(
				"SalaryMaster",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF,$mdDialog, $mdMedia, $state) {
					
					
					$scope.flag=false;
					$scope.neg=false;
					
					$scope.getEmptyEmployeeLeaveDetails = function(emp) {
						return{
							empAccount:emp,
							grosssal:0,
							 basic:0,
							 HRA:0,
							 convence:0,
							 medical:0,
							  education:0,
							  adhocAllow:0,
							  specialAllow:0,
							  calGrossTotal:0,
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
										//if(list[i].grosssal==0)	{$scope.employeeLeaveDetailsList.push($scope.getEmptyEmployeeLeaveDetails(list[i]));}
										$scope.employeeLeaveDetailsList.push(list[i]);
										
										$scope.calculation(i);
										
										}
										
										$log.debug("id///////////////////////////" + $scope.curUser.business.items);

									});
						
					}
	
					
					$scope.calculation=function (index){
						
						if($scope.employeeLeaveDetailsList[index].grosssal!==0 ){
						
						if($scope.employeeLeaveDetailsList[index].basic==0 ){
						$scope.employeeLeaveDetailsList[index].basic =$scope.employeeLeaveDetailsList[index].grosssal*40/100;}
						if( $scope.employeeLeaveDetailsList[index].convence==0)
						{	$scope.employeeLeaveDetailsList[index].convence=2000;}
						if($scope.employeeLeaveDetailsList[index].medical==0)
						{$scope.employeeLeaveDetailsList[index].medical=1250;}
						if($scope.employeeLeaveDetailsList[index].education==0)
						{$scope.employeeLeaveDetailsList[index].education=200;}
						
						$scope.employeeLeaveDetailsList[index].specialAllow=$scope.employeeLeaveDetailsList[index].grosssal-$scope.employeeLeaveDetailsList[index].convence-$scope.employeeLeaveDetailsList[index].basic-$scope.employeeLeaveDetailsList[index].medical-$scope.employeeLeaveDetailsList[index].education;
						
						$scope.employeeLeaveDetailsList[index].calGrossTotal=($scope.employeeLeaveDetailsList[index].convence+$scope.employeeLeaveDetailsList[index].basic+$scope.employeeLeaveDetailsList[index].medical+$scope.employeeLeaveDetailsList[index].education)+$scope.employeeLeaveDetailsList[index].specialAllow;
						
						
						
						
						}
						
							
							
							
						
						}
					
						
						
						
					
					
					$scope.getSAStyle=function(spValue){
						return {color: spValue>0?'':'red'};
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
					$scope.download=function(){
						
					//	window.open("DownloadSalaryMaster?id="+$scope.curUser.business.id+d);
						document.location.href = "DownloadSalaryMaster?id="+$scope.curUser.business.id;
						
						
						
					}
					
					$log.debug("id" + $scope.curUser.business);
				});
