angular
		.module("stockApp")
		.controller(
				"MonthlyPaymentDetail",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, $filter, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {
					$scope.query = {
						order : 'leaveDetailEntity.user.employeeDetail.empId',
						limit : 50,
						page : 1
					};

					$scope.monthlyPayDetailsList = [];

					$scope.selectedMonth = $stateParams.selectedMonth;

					$scope.totalDaysInSelectedMonth = 0;

					$scope.calculateMonthlyPayment = function(
							monthlyPayDetailObj) {

						monthlyPayDetailObj.totalDays = $scope.totalDaysInSelectedMonth;
						monthlyPayDetailObj.payableDays = $scope.totalDaysInSelectedMonth
								+ monthlyPayDetailObj.leaveDetailEntity.withoutpay;

						monthlyPayDetailObj.overtimeAmt = monthlyPayDetailObj.leaveDetailEntity.overtimeDays
								* (monthlyPayDetailObj.monthlyGrossSalary / $scope.totalDaysInSelectedMonth);
						
						monthlyPayDetailObj.overtimeAmt = Math
						.round(monthlyPayDetailObj.overtimeAmt);

						monthlyPayDetailObj.calculatedGrossSalary = monthlyPayDetailObj.payableDays
								* (monthlyPayDetailObj.monthlyGrossSalary / $scope.totalDaysInSelectedMonth);
						monthlyPayDetailObj.calculatedGrossSalary = Math
						.round(monthlyPayDetailObj.calculatedGrossSalary);

						// Manual entry takes priority
						if (!(monthlyPayDetailObj.esiDeductionAmt)) {
							monthlyPayDetailObj.esiDeductionAmt = 0;
						}
						if (monthlyPayDetailObj.calculatedGrossSalary <= 25000
								&& monthlyPayDetailObj.esiDeductionAmt == 0) {
							// ESI is 1.75% of monthly salary
							monthlyPayDetailObj.esiDeductionAmt = monthlyPayDetailObj.calculatedGrossSalary
									* (1 / 100);
							monthlyPayDetailObj.esiDeductionAmt = Math
									.round(monthlyPayDetailObj.esiDeductionAmt
											.toFixed(2));
						}
						if (monthlyPayDetailObj.calculatedGrossSalary > 0
								&& monthlyPayDetailObj.pfDeductionAmt == 0) {
							// PF is 12% of monthly salary
							monthlyPayDetailObj.pfDeductionAmt = monthlyPayDetailObj.payableDays
									* (monthlyPayDetailObj.salStruct.monthlyBasic / $scope.totalDaysInSelectedMonth)
									* (12 / 100);
							monthlyPayDetailObj.pfDeductionAmt = Math
									.round(monthlyPayDetailObj.pfDeductionAmt
											.toFixed(2));
						}
						if (monthlyPayDetailObj.ptDeductionAmt == 0) {
							if (monthlyPayDetailObj.calculatedGrossSalary <= 10000)
								monthlyPayDetailObj.ptDeductionAmt = 175;
							else
								monthlyPayDetailObj.ptDeductionAmt = 200;
						}

						monthlyPayDetailObj.netSalaryAmt = (monthlyPayDetailObj.calculatedGrossSalary
								+ monthlyPayDetailObj.overtimeAmt + monthlyPayDetailObj.specialAllow)
								- monthlyPayDetailObj.pfDeductionAmt
								- monthlyPayDetailObj.ptDeductionAmt
								- monthlyPayDetailObj.canteenDeductionAmt
								- monthlyPayDetailObj.itDeductionAmt
								- monthlyPayDetailObj.esiDeductionAmt
								- monthlyPayDetailObj.otherDeductionAmt;

						monthlyPayDetailObj.netSalaryAmt = Math
								.round(monthlyPayDetailObj.netSalaryAmt);

					}

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
											for (var i = 0; i < $scope.monthlyPayDetailsList.length; i++) {
												$scope
														.calculateMonthlyPayment($scope.monthlyPayDetailsList[i]);
											}
											$scope.monthlyPayDetailsListBackup = $scope.monthlyPayDetailsList;
											$scope.loading = false;
											$scope.getEmpDepartments();
										});

					}
					
					$scope.fitlerUserListByDept = function(deptName) {
						if (deptName == 'ALL') {
							$scope.monthlyPayDetailsList = $scope.monthlyPayDetailsListBackup;
						} else {
							$scope.monthlyPayDetailsList = [];
							angular
									.forEach(
											$scope.monthlyPayDetailsListBackup,
											function(monthPay) {
												if (monthPay.empAccount
														&& monthPay.empAccount.employeeDetail.department
														&& monthPay.empAccount.employeeDetail.department.name == deptName)
													$scope.monthlyPayDetailsList
															.push(monthPay);
											});
						}

					}

					$scope.getEmpDepartments = function() {
						var userService = appEndpointSF.getUserService();
						userService.getEmpDepartments(
								$scope.curUser.business.id).then(
								function(list) {
									if (list.items) {
										$scope.departmentList = list.items;
										$scope.departmentList = $filter(
												'proOrderObjectByTextField')(
												$scope.departmentList, "name");
										$scope.departmentList.splice(0, 0, {
											name : 'ALL'
										});
									}
								});
					}
					

					$scope.monthSelectChange = function(selectedMonth) {
						$log.debug("selectedMonth" + selectedMonth);

						var currentMonthNameIndex = $scope.monthNameList
								.indexOf(selectedMonth.split("-")[0]);

						var currentMonthYear = Number(selectedMonth.split("-")[1]);

						var nowDate = new Date();
						var salaryMonth = new Date(currentMonthYear,
								currentMonthNameIndex, 1);
						$scope.totalDaysInSelectedMonth = $scope
								.getDaysInMonth(currentMonthNameIndex + 1,
										currentMonthYear);
						$scope.selectedMonth = selectedMonth;
						$scope.getMonthlyPaymentList();

					}

					$scope.saveMonthlyPaymentDetailList = function(finalized) {
						$scope.loading = true;
						var hrService = appEndpointSF.gethrService();
						for (var i = 0; i < $scope.monthlyPayDetailsList.length; i++) {
							$scope.monthlyPayDetailsList[i].finalized = finalized ? true
									: false;
						}
						hrService.saveMonthlyPaymentDetailList({
							'list' : $scope.monthlyPayDetailsList
						}).then(function(resp) {
							$scope.monthlyPayDetailsList = resp.items;
							$scope.showAddToast();
							$scope.loading = false;
						});

					}
					$scope.indvIt = function() {
						var hrService = appEndpointSF.gethrService();

						$scope.totalIT = 0;
						hrService.getpayRollReport($scope.curUser.business.id,
								$scope.selectedMonth).then(function(list) {
							$scope.list = list;
							for (var i = 0; i < list.length; i++) {
								if ($scope.selectedMonth == list[i].month) {
									$scope.totalIT += list[i].totalIT;
								}

							}
						});

					}
					$scope.finalizeMonthlySalaryForTheMonth = function(ev) {
						var confirm = $mdDialog
								.confirm()
								.title(
										'Do you want to finalize Salary Details for this month. Note, after this you will not be able to Edit Salary Details.')
								.textContent('').ariaLabel('finalize?')
								.targetEvent(ev).ok('Okay').cancel('cancel');

						$mdDialog.show(confirm).then(saveFinalize, function() {
							$log.debug("Cancelled...");
						});
					}

					function saveFinalize() {
						$log.debug("Saved.....");
						$scope.saveMonthlyPaymentDetailList(true);
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							//$scope.getMonthlyPaymentList();
							$scope.indvIt();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.openDialog = function($event, monthlyPayObj,
							allowanceOrDeductionFlag) {
						// Show the dialog
						$mdDialog
								.show({
									clickOutsideToClose : true,
									controller : function($mdDialog) {
										// Save the clicked item
										// this.item = item;
										this.item = monthlyPayObj;
										this.allowanceOrDeductionFlag = allowanceOrDeductionFlag;
										// Setup some handlers
										this.close = function() {
											$mdDialog.cancel();
										};
										this.submit = function() {
											$mdDialog.hide();
										};
									},
									autoWrap : false,
									controllerAs : 'dialog',
									templateUrl : 'dialog.html',
									targetEvent : $event
								});
					}

					$scope.downloadSalarySlip = function(monthlyPayObj) {
						var MonthlyPaymentDetailEntity = "MonthlyPaymentDetailEntity";
						window.open("PrintPdfSalarySlip?month="
								+ $scope.selectedMonth + "&entityname="
								+ MonthlyPaymentDetailEntity + "&bid="
								+ $scope.curUser.business.id + "&id="
								+ monthlyPayObj.id);
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

					$scope.downloadData = function() {
						// window.open("DownloadSalaryMaster?id="+$scope.curUser.business.id+d);
						document.location.href = "DownloadMonthlypayment?id="
								+ $scope.curUser.business.id + "&month="
								+ $scope.selectedMonth;
					}
					$scope.previousPage = function() {
						window.history.back();
					};

				});
