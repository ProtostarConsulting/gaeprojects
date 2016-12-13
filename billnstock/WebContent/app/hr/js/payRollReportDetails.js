angular
		.module("stockApp")
		.controller(
				"payrollReportDetailsCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

					$scope.query = {
						order : 'leaveDetailEntity.user.employeeDetail.empId',
						limit : 50,
						page : 1
					};

					$scope.monthlyPayDetailsList = [];
					$scope.selectedMonth = $stateParams.selectedMonth;
					$scope.toShow = $stateParams.toShow;

					$scope.getMonthlyPaymentList = function() {
						$scope.loading = true;
						$scope.monthlyPayDetailsList = [];
						var hrService = appEndpointSF.gethrService();
						hrService
								.getMonthlyPayment($scope.curUser.business.id,
										$scope.selectedMonth)
								.then(
										function(list) {
											$scope.monthlyPayDetailsList = list;

											$scope.totalSal = 0;
											$scope.totalPF = 0;
											$scope.totalPT = 0;
											$scope.totalCanteen = 0;
											$scope.totalIT = 0;
											$scope.totalOther = 0;
											$scope.totalESI = 0;
											for (var i = 0; i < $scope.monthlyPayDetailsList.length; i++) {
												$scope.totalSal += $scope.monthlyPayDetailsList[i].calculatedGrossSalary;
												$scope.totalPF += $scope.monthlyPayDetailsList[i].pfDeductionAmt;
												$scope.totalPT += $scope.monthlyPayDetailsList[i].ptDeductionAmt;
												$scope.totalCanteen += $scope.monthlyPayDetailsList[i].canteenDeductionAmt;
												$scope.totalIT += $scope.monthlyPayDetailsList[i].itDeductionAmt;
												$scope.totalOther += $scope.monthlyPayDetailsList[i].otherDeductionAmt;
												$scope.totalESI += $scope.monthlyPayDetailsList[i].esiDeductionAmt;

											}
											$scope.loading = false;
										});

					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getMonthlyPaymentList();
							// $scope.indvIt();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					$scope.previousPage = function() {
						window.history.back();
					};

				});
