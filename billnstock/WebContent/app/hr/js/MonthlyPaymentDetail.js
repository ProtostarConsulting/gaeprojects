angular
		.module("stockApp")
		.controller(
				"MonthlyPaymentDetail",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {
					$scope.query = {
							order : 'leaveDetailEntity.user.empId',
							limit : 50,
							page : 1
						};

					$scope.monthlyPayDetailsList = [];

					$scope.totalDaysInSelectedMonth = 0;

					$scope.calculateMonthlyPayment = function(monthlyPayDetailObj) {

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
											$scope.loading = false;
										});

					}

					$scope.monthSelectChange = function(selectedMonth) {
						$log.debug("selectedMonth" + selectedMonth);

						var currentMonthNameIndex = $scope.monthNameList
								.indexOf(selectedMonth.split("-")[0]);

						var currentMonthYear = Number(selectedMonth.split("-")[1]);

						var nowDate = new Date();
						var salaryMonth = new Date(
								currentMonthYear,
								currentMonthNameIndex, 1);
						$scope.totalDaysInSelectedMonth = $scope
								.getDaysInMonth(currentMonthNameIndex + 1,
										currentMonthYear);
						$scope.selectedMonth = selectedMonth;
						$scope.getMonthlyPaymentList();

					}

					$scope.saveMonthlyPaymentDetailList = function() {
						$scope.loading = true;
						var hrService = appEndpointSF.gethrService();
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
					
					$scope.finalizeMonthlySalaryForTheMonth = function() {
						
					}
					

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							// get salary master list on page load.
							// $scope.getSalaryMasterlist();
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
								+ $scope.curUser.business.id+"&month="
								+ $scope.selectedMonth;
					}
					
					

				});
