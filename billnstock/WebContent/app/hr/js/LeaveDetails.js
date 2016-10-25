angular
		.module("stockApp")
		.controller(
				"LeaveDetails",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {
					$scope.Selectedmonth;//+new Date().getFullYear();

					$scope.getEmptyEmployeeLeaveDetails = function(emp,month) {
						return {
							user : emp,
							openingBalance : "",
							mothLeave : "",
							takenmothLeave : "",
							withoutpay : "",
							nextOpeningBalance : "",
							currentMonth:month+"-"+new Date().getFullYear(),
							business : $scope.curUser.business
							

						};
					}
					$scope.month1 = [ "January", "February", "March",
					            					"April", "May", "June", "July","August","September","October","November","December" ];
					
					
					
					
					
					$scope.monthSelectChange=function(selectedMonth){
						$log.debug("selectedMonth" + selectedMonth);
						for(var i=0;i<$scope.month1.length-1;i++)
						{
						if(selectedMonth==$scope.month1[i+1])
						{
							var prevMonth=$scope.month1[i]+"-"+new Date().getFullYear();
							break;

						}
							
							
						}
						$log.debug("$scope.prevMonth" + $scope.prevMonth);
						
						$scope.mon=selectedMonth+"-"+new Date().getFullYear();
						$scope.getEmpLeavList($scope.mon,prevMonth);
					//	$scope.getEmpLeavList(month);
					//	
						
					}
					
					
					$scope.employeeLeaveDetailsList = [];
				
					$scope.list = function() {
						var hrService = appEndpointSF.gethrService();

						hrService
								.getAllemp($scope.curUser.business.id)
								.then(
										function(list) {

											$scope.list2 = list;
											$scope.employeeLeaveDetailsList.length=0;
											for (var i = 0; i < list.length; i++) {
											
												
												$scope.employeeLeaveDetailsList.push($scope.getEmptyEmployeeLeaveDetails(list[i],$scope.Selectedmonth));
												

											}

										});

					}
					
					$scope.getEmpLeavList = function(month,prevMonth) {

						var hrService = appEndpointSF.gethrService();
							hrService.getLeaveListEmp($scope.curUser.business.id,month,prevMonth).then(
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
					

					$scope.addEmpLeav = function() {

						var hrService = appEndpointSF.gethrService();
						// hrService.saveLeaveDetail($scope.employee1).then(function(){

						for (var i = 0; i < $scope.employeeLeaveDetailsList.length; i++) {
							
						//	 $scope.employeeLeaveDetailsList[i].currentMonth=$scope.month+new Date().getFullYear() ;
							
							hrService.saveLeaveDetail($scope.employeeLeaveDetailsList[i]).then(
									function() {

										$scope.showAddToast();

									});
						}
					}

					$scope.calculation = function(index) {
						var cal = $scope.employeeLeaveDetailsList[index].openingBalance
								+ $scope.employeeLeaveDetailsList[index].mothLeave
								- $scope.employeeLeaveDetailsList[index].takenmothLeave;
						
							
						if (cal < 0) {
							$scope.employeeLeaveDetailsList[index].withoutpay = cal;
							$scope.employeeLeaveDetailsList[index].nextOpeningBalance = 0;
						} else {
							$scope.employeeLeaveDetailsList[index].withoutpay = 0;
							$scope.employeeLeaveDetailsList[index].nextOpeningBalance = cal;

						}

					}

					
					
					
				});
