angular
		.module("stockApp")
		.controller(
				"MonthlyPaymentDetail",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

					$scope.month1 = [ "January", "February", "March", "April",
							"May", "June", "July", "August", "September",
							"October", "November", "December" ];
					
					$scope.getEmptyEmployeeLeaveDetails = function(emp) {
						return {

							leaveDetailEntity : emp,
							payableDays : "",
							monthlyGrossSalary : "",
							calculatedGrossSalary : "",
							pfDeductionAmt : "",
							ptDeductionAmt : "",
							canteenDeductionAmt : "",
							itDeductionAmt : "",
							otherDeductionAmt : "",
							netSalaryAmt : "",
							currentMonth:"",
							business : $scope.curUser.business
						};
					}

				
					$scope.employeeLeaveDetailsList2 = [];
					$scope.employeeLeaveDetailsList = [];

					$scope.calculation = function(index) {

						$scope.employeeLeaveDetailsList[index].payableDays = 30 + $scope.employeeLeaveDetailsList[index].leaveDetailEntity.withoutpay;
						$scope.employeeLeaveDetailsList[index].calculatedGrossSalary = $scope.employeeLeaveDetailsList[index].payableDays
								* ($scope.employeeLeaveDetailsList[index].monthlyGrossSalary / 30)

					
						
						$scope.employeeLeaveDetailsList[index].netSalaryAmt=$scope.employeeLeaveDetailsList[index].calculatedGrossSalary- $scope.employeeLeaveDetailsList[index].pfDeductionAmt
										- $scope.employeeLeaveDetailsList[index].ptDeductionAmt
										- $scope.employeeLeaveDetailsList[index].canteenDeductionAmt
										- $scope.employeeLeaveDetailsList[index].itDeductionAmt
										- $scope.employeeLeaveDetailsList[index].otherDeductionAmt
						
					
						

					}
					

					$scope.list = function() {
						var hrService = appEndpointSF.gethrService();

						hrService
								.getMonthlyPayment($scope.curUser.business.id,$scope.mon)
								.then(
										function(list) {
											
											if (list.length==0){ $scope.getEmpLeavList($scope.mon, null);    }
											else{
											$scope.list2 = list;
											$scope.employeeLeaveDetailsList.length = 0;
											for (var i = 0; i < list.length; i++) {

												$scope.employeeLeaveDetailsList
														.push(list[i]);

											}}

										});

					}

					$scope.monthSelectChange = function(selectedMonth) {
						$scope.worning=false;

						$scope.mon = selectedMonth + "-"
								+ new Date().getFullYear();
						$scope.list();
				

					}

					$scope.getEmpLeavList = function(month, prevMonth) {

						var hrService = appEndpointSF.gethrService();
						hrService.getLeaveListEmp($scope.curUser.business.id,
								month, prevMonth).then(function(list) {
									if(list.length==0){	$scope.worning=true;}
									
									
									else{
							$scope.employeeLeaveDetailsList.length = 0;
							for (var i = 0; i < list.length; i++) {
								
								$scope.employeeLeaveDetailsList
								.push($scope.getEmptyEmployeeLeaveDetails(list[i]));
								
								
								
							

							}
									}

						}); 
					}
					
					$scope.addEmpLeav= function() {
						
						var hrService = appEndpointSF.gethrService();
						
						for (var i = 0; i < $scope.employeeLeaveDetailsList.length; i++) {
							
							$scope.	employeeLeaveDetailsList[i].currentMonth=$scope.employeeLeaveDetailsList[i].leaveDetailEntity.currentMonth;
								hrService.saveMonthlyPaymentDetail($scope.employeeLeaveDetailsList[i]).then(
										function() {$scope.showAddToast();});
							}
						
					}
					var MonthlyPaymentDetailEntity="MonthlyPaymentDetailEntity";
					
					$scope.downloadSalarySlip=function(empLeaveDetailObj){
						
						window.open("PdfSales?month="+$scope.mon+"&entityname="+MonthlyPaymentDetailEntity+"&bid="+$scope.curUser.business.id+"&id="+empLeaveDetailObj.id);
						
						
						
					}

					
					
						});
 




