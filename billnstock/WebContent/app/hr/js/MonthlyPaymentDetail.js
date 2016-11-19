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

					$scope.monthList = [ "January", "February", "March",
							"April", "May", "June", "July", "August",
							"September", "October", "November", "December" ];

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
						monthlyPayDetailObj.netSalaryAmt = Math
								.round(monthlyPayDetailObj.netSalaryAmt);

					}

					$scope.getMonthlyPaymentList = function() {
						$scope.loading = true;
						var hrService = appEndpointSF.gethrService();

						hrService
								.getMonthlyPayment($scope.curUser.business.id,
										$scope.selectedMonth)
								.then(
										function(list) {
											$scope.monthlyPayDetailsList = list;
											for (var i = 0; i < $scope.monthlyPayDetailsList.length; i++) {
												$scope
														.calculateMonthlyPayment(i);
											}
											$scope.loading = false;
										});

					}

					$scope.monthSelectChange = function(selectedMonth) {
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
						$scope.selectedMonth = selectedMonth + "-"
								+ salaryMonth.getFullYear();
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

				});
