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

					$scope.getEmptyMonthlyPayDetails = function(emp) {
						var salMasterObj = $scope
								.getEmpSalaryMasterObj(emp.user);
						return {
							salStruct : salMasterObj,
							leaveDetailEntity : emp,
							payableDays : 0,
							monthlyGrossSalary : salMasterObj ? salMasterObj.grosssal
									: 0,
							salStruct : salMasterObj,
							calculatedGrossSalary : 0,
							specialAllow : 0,
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

					$scope.monthlyPayDetailsList = [];

					$scope.totalDaysInSelectedMonth = 0;

					$scope.calculateMonthlyPayment = function(index) {
						monthlyPayDetailObj = $scope.monthlyPayDetailsList[index];

						monthlyPayDetailObj.totalDays = $scope.totalDaysInSelectedMonth;
						monthlyPayDetailObj.payableDays = $scope.totalDaysInSelectedMonth
								+ monthlyPayDetailObj.leaveDetailEntity.withoutpay;
						monthlyPayDetailObj.calculatedGrossSalary = monthlyPayDetailObj.payableDays
								* (monthlyPayDetailObj.monthlyGrossSalary / $scope.totalDaysInSelectedMonth);

						monthlyPayDetailObj.netSalaryAmt = (monthlyPayDetailObj.calculatedGrossSalary + monthlyPayDetailObj.specialAllow)
								- monthlyPayDetailObj.pfDeductionAmt
								- monthlyPayDetailObj.ptDeductionAmt
								- monthlyPayDetailObj.canteenDeductionAmt
								- monthlyPayDetailObj.itDeductionAmt
								- monthlyPayDetailObj.otherDeductionAmt;

						monthlyPayDetailObj.calculatedGrossSalary = monthlyPayDetailObj.calculatedGrossSalary
								.toFixed(2);
						monthlyPayDetailObj.netSalaryAmt = Math.round(monthlyPayDetailObj.netSalaryAmt);

					}

					$scope.getMonthlyPaymentList = function() {
						var hrService = appEndpointSF.gethrService();

						hrService.getMonthlyPayment($scope.curUser.business.id,
								$scope.mon).then(function(list) {

							if (list.length == 0) {
								$scope.getEmpLeavList($scope.mon, "null");
							} else {
								$scope.list2 = list;
								$scope.monthlyPayDetailsList.length = 0;
								for (var i = 0; i < list.length; i++) {

									$scope.monthlyPayDetailsList.push(list[i]);

								}
							}

						});

					}

					$scope.monthSelectChange = function(selectedMonth) {
						$scope.worning = false;
						var selectedMonthIndex = $scope.monthList
								.indexOf(selectedMonth.trim());
						var nowDate = new Date();
						var salaryMonth = new Date(
								nowDate.getFullYear(),
								selectedMonthIndex == 11 ? selectedMonthIndex - 1
										: selectedMonthIndex, 1);
						$scope.totalDaysInSelectedMonth = $scope
								.getDaysInMonth(selectedMonthIndex + 1,
										salaryMonth.getFullYear());
						$scope.mon = selectedMonth + "-"
								+ salaryMonth.getFullYear();
						$scope.getMonthlyPaymentList();

					}

					$scope.getEmpLeavList = function(month, prevMonth) {
						$scope.loading = true;
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
												$scope.monthlyPayDetailsList.length = 0;
												for (var i = 0; i < list.length; i++) {
													$scope.monthlyPayDetailsList
															.push($scope
																	.getEmptyMonthlyPayDetails(list[i]));
													$scope
															.calculateMonthlyPayment(i);
												}
											}
											$scope.loading = false;
										});
					}

					$scope.saveMonthlyPaymentDetailList = function() {

						var hrService = appEndpointSF.gethrService();
						$scope.loading = true;
						for (var i = 0; i < $scope.monthlyPayDetailsList.length; i++) {
							$scope.monthlyPayDetailsList[i].currentMonth = $scope.monthlyPayDetailsList[i].leaveDetailEntity.currentMonth;
						}
						hrService.saveMonthlyPaymentDetailList({
							'list' : $scope.monthlyPayDetailsList
						}).then(function(resp) {
							$scope.monthlyPayDetailsList = resp.items;
							$scope.showAddToast();
							$scope.loading = false;
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
						$scope.loading = true;
						var hrService = appEndpointSF.gethrService();
						hrService.getSalaryMasterlist(
								$scope.curUser.business.id).then(
								function(list) {
									for (var i = 0; i < list.length; i++) {
										// scope.employeeLeaveDetailsList.push(list[i]);
										// $scope.calculation(i);

										$scope.salaryMasterList.push(list[i]);
									}
									$scope.loading = false;
								});

					}
					// get salary master list on page load.
					$scope.getSalaryMasterlist();

					var MonthlyPaymentDetailEntity = "MonthlyPaymentDetailEntity";

					$scope.downloadSalarySlip = function(empLeaveDetailObj) {

						window.open("PrintPdfSalarySlip?month=" + $scope.mon
								+ "&entityname=" + MonthlyPaymentDetailEntity
								+ "&bid=" + $scope.curUser.business.id + "&id="
								+ empLeaveDetailObj.id);

					}

					$scope.highlightRed = function(payableDays) {
						var isTrue = payableDays < $scope.totalDaysInSelectedMonth;
						// $log.debug("payableDays: " + payableDays + " isTrue:"
						// + isTrue);
						return {
							'color' : isTrue ? 'red' : '',
							'padding-left' : '10px'
						};

					}

				});
