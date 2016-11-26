angular
		.module("stockApp")
		.controller(
				"myMonthlyDeduction",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {
					
					
					$scope.monthlyPayDetailsList = [];
					
					$scope.getDeductionList = function() {
								var hrService = appEndpointSF.gethrService();
								hrService.getMonthlyPaymentByUser($scope.curUser.business.id,$scope.curUser)
										.then(function(list) {
											
											
											$scope.monthlyPayDetailsList=list;
											$scope.total=0;
											$scope.totalPF=0;
											$scope.totalPT=0;
											$scope.totalCanteen=0;
											$scope.totalIT=0;
											$scope.totalOther=0;
											$scope.totalNetsalary=0;
											for (var i = 0; i < $scope.monthlyPayDetailsList.length; i++) {
												$scope.totalPF+=list[i].pfDeductionAmt;
												$scope.totalPT+=list[i].ptDeductionAmt;
												$scope.totalCanteen+=list[i].canteenDeductionAmt;
												$scope.totalIT+=list[i].itDeductionAmt;
												$scope.totalOther+=list[i].otherDeductionAmt;
												$scope.totalNetsalary+=list[i].netSalaryAmt;
												
												
											}});}
					$scope.downloadSalarySlip = function(monthlyPayObj) {
						var MonthlyPaymentDetailEntity = "MonthlyPaymentDetailEntity";
						window.open("PrintPdfSalarySlip?month="
								+ monthlyPayObj.leaveDetailEntity.currentMonth + "&entityname="
								+ MonthlyPaymentDetailEntity + "&bid="
								+ $scope.curUser.business.id + "&id="
								+ monthlyPayObj.id);
					}
					
					
					
					$scope.getDeductionList();
					
					
				});
