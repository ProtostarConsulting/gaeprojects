angular
		.module("stockApp")
		.controller(
				"LeaveDetails",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

					$scope.Selectedmonth;// +new Date().getFullYear();

					$scope.query = {
						order : 'user.empId',
						limit : 50,
						page : 1
					};

					/*$scope.getEmptyEmployeeLeaveDetails = function(emp, month) {
						return {
							user : emp,
							openingBalance : "",
							mothLeave : "",
							takenmothLeave : "",
							withoutpay : "",
							nextOpeningBalance : "",
							currentMonth : month + "-"
									+ new Date().getFullYear(),
							business : $scope.curUser.business

						};
					}*/

					

					$scope.monthSelectChange = function(selectedMonth) {
						$scope.worning = false;
						$log.debug("selectedMonth" + selectedMonth);

						var currentMonthNameIndex = $scope.monthNameList
								.indexOf(selectedMonth.split("-")[0]);

						var currentMonthYear = Number(selectedMonth.split("-")[1]);

						if (currentMonthNameIndex == 0) {
							$scope.prevMonth = "December-"
									+ (currentMonthYear - 1);
						} else {
							$scope.prevMonth = $scope.monthNameList[currentMonthNameIndex - 1]
									+ "-" + currentMonthYear;
						}
						$log.debug("prevMonth:" + $scope.prevMonth);
						$scope.selectedMonth = selectedMonth;
						$scope.getEmpLeavList($scope.selectedMonth,
								$scope.prevMonth);
					}

					$scope.employeeLeaveDetailsList = [];

					/*$scope.getEmpList = function() {
						var hrService = appEndpointSF.gethrService();

						hrService
								.getAllemp($scope.curUser.business.id)
								.then(
										function(list) {
											$scope.employeeLeaveDetailsList.length = 0;

											for (var i = 0; i < list.length; i++) {

												$scope.employeeLeaveDetailsList
														.push($scope
																.getEmptyEmployeeLeaveDetails(
																		list[i],
																		$scope.Selectedmonth));

											}

										});

					}*/

					$scope.getEmpLeavList = function(month, prevMonth) {
						$scope.loading = true;
						$scope.employeeLeaveDetailsList = [];
						var hrService = appEndpointSF.gethrService();
						hrService
								.getLeaveListEmp($scope.curUser.business.id,
										month, prevMonth)
								.then(
										function(list) {

											if (list.length == 0
													|| list == null) {
												$scope.worning = true;
											} else
												$scope.employeeLeaveDetailsList = list;
											for (var i = 0; i < $scope.employeeLeaveDetailsList.length; i++) {
												$scope
														.calculation($scope.employeeLeaveDetailsList[i]);
											}
											$scope.loading = false;

										});
					}

					$scope.saveLeaveDetailList = function() {

						var hrService = appEndpointSF.gethrService();
						// hrService.saveLeaveDetail($scope.employee1).then(function(){
						hrService.saveLeaveDetailList({
							'list' : $scope.employeeLeaveDetailsList
						}).then(function() {
							$scope.showAddToast();
						});
					}

					$scope.calculation = function(empLeaveDetailObj) {
						var cal = empLeaveDetailObj.openingBalance
								+ empLeaveDetailObj.mothLeave
								- empLeaveDetailObj.takenmothLeave;

						if (cal < 0) {
							empLeaveDetailObj.withoutpay = cal;
							empLeaveDetailObj.nextOpeningBalance = 0;
						} else {
							empLeaveDetailObj.withoutpay = 0;
							empLeaveDetailObj.nextOpeningBalance = cal;

						}

					}

					$scope.getStandardLabelStyle = function() {
						return {
							'padding-top' : '5px'
						};

					}

					$scope.getFormRowStyle = function() {
						return {
							'margin' : '0px',
							'padding-left' : '0',
							'padding-right' : '5px'
						};

					}

				});
