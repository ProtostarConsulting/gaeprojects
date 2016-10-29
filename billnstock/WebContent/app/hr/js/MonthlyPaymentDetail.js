angular
		.module("stockApp")
		.controller(
				"MonthlyPaymentDetail",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

					$scope.monthList = [ "January", "February", "March",
							"April", "May", "June", "July", "August",
							"September", "October", "November", "December" ];

					$scope.salaryMasterList = [];

					$scope.getEmptyEmployeeLeaveDetails = function(emp) {
						var salMasterObj = $scope
								.getEmpSalaryMasterObj(emp.user);
						return {

							leaveDetailEntity : emp,
							payableDays : "",
							monthlyGrossSalary : salMasterObj ? salMasterObj.grosssal
									: 0,
							calculatedGrossSalary : 0,
							pfDeductionAmt : 0,
							ptDeductionAmt : 0,
							canteenDeductionAmt : 0,
							itDeductionAmt : 0,
							otherDeductionAmt : 0,
							netSalaryAmt : 0,
							currentMonth : 0,
							business : $scope.curUser.business
						};
					}

					$scope.employeeLeaveDetailsList2 = [];
					$scope.employeeLeaveDetailsList = [];

					$scope.calculation = function(index) {

						$scope.employeeLeaveDetailsList[index].payableDays = 30 + $scope.employeeLeaveDetailsList[index].leaveDetailEntity.withoutpay;
						$scope.employeeLeaveDetailsList[index].calculatedGrossSalary = $scope.employeeLeaveDetailsList[index].payableDays
								* ($scope.employeeLeaveDetailsList[index].monthlyGrossSalary / 30)

						$scope.employeeLeaveDetailsList[index].netSalaryAmt = $scope.employeeLeaveDetailsList[index].calculatedGrossSalary
								- $scope.employeeLeaveDetailsList[index].pfDeductionAmt
								- $scope.employeeLeaveDetailsList[index].ptDeductionAmt
								- $scope.employeeLeaveDetailsList[index].canteenDeductionAmt
								- $scope.employeeLeaveDetailsList[index].itDeductionAmt
								- $scope.employeeLeaveDetailsList[index].otherDeductionAmt

					}

					$scope.list = function() {
						var hrService = appEndpointSF.gethrService();

						hrService
								.getMonthlyPayment($scope.curUser.business.id,
										$scope.mon)
								.then(
										function(list) {

											if (list.length == 0) {
												$scope.getEmpLeavList(
														$scope.mon, null);
											} else {
												$scope.list2 = list;
												$scope.employeeLeaveDetailsList.length = 0;
												for (var i = 0; i < list.length; i++) {

													$scope.employeeLeaveDetailsList
															.push(list[i]);

												}
											}

										});

					}

					$scope.monthSelectChange = function(selectedMonth) {
						$scope.worning = false;

						$scope.mon = selectedMonth + "-"
								+ new Date().getFullYear();
						$scope.list();

					}

					$scope.getEmpLeavList = function(month, prevMonth) {

						var hrService = appEndpointSF.gethrService();
						hrService
								.getLeaveListEmp($scope.curUser.business.id,
										month, prevMonth)
								.then(
										function(list) {
											if (list.length == 0) {
												$scope.worning = true;
											}

											else {
												$scope.employeeLeaveDetailsList.length = 0;
												for (var i = 0; i < list.length; i++) {
													$scope.employeeLeaveDetailsList
															.push($scope
																	.getEmptyEmployeeLeaveDetails(list[i]));
													$scope.calculation(i);
												}
											}

										});
					}

					$scope.saveMonthlyPaymentDetailList = function() {

						var hrService = appEndpointSF.gethrService();

						for (var i = 0; i < $scope.employeeLeaveDetailsList.length; i++) {
							$scope.employeeLeaveDetailsList[i].currentMonth = $scope.employeeLeaveDetailsList[i].leaveDetailEntity.currentMonth;
						}
						hrService.saveMonthlyPaymentDetailList({
							'list' : $scope.employeeLeaveDetailsList
						}).then(function() {
							$scope.showAddToast();
						});

					}

					$scope.getEmpSalaryMasterObj = function(empObj) {
						var foundSalMaster = null;
						angular.forEach($scope.salaryMasterList, function(
								empSalMaterObj) {
							if (empSalMaterObj.empAccount.id == empObj.id) {
								foundSalMaster = empSalMaterObj;
							}
						});
						return foundSalMaster;
					}

					$scope.getSalaryMasterlist = function() {
						var hrService = appEndpointSF.gethrService();
						hrService.getSalaryMasterlist(
								$scope.curUser.business.id).then(
								function(list) {
									$scope.salaryMasterList = list;
								});

					}
					// get salary master list on page load.
					$scope.getSalaryMasterlist();

					var MonthlyPaymentDetailEntity = "MonthlyPaymentDetailEntity";

					$scope.downloadSalarySlip = function(empLeaveDetailObj) {

						window.open("PdfSales?month=" + $scope.mon
								+ "&entityname=" + MonthlyPaymentDetailEntity
								+ "&bid=" + $scope.curUser.business.id + "&id="
								+ empLeaveDetailObj.id);

					}

					$scope.highlightRed = function(payableDays) {
						var isTrue = payableDays < 30;
						// $log.debug("payableDays: " + payableDays + " isTrue:"
						// + isTrue);
						return {
							'color' : isTrue ? 'red' : ''
						};

					}

				});
