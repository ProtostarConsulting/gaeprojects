angular
		.module("stockApp")
		.controller(
				"LeaveDetails",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

					$scope.Selectedmonth;// +new Date().getFullYear();

					$scope.query = {
						order : 'firtName',
						limit : 50,
						page : 1
					};

					$scope.getEmptyEmployeeLeaveDetails = function(emp, month) {
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
					}

					$scope.monthList = [ "January", "February", "March",
							"April", "May", "June", "July", "August",
							"September", "October", "November", "December" ];

					$scope.monthSelectChange = function(selectedMonth) {
						$scope.worning=false;
						$log.debug("selectedMonth" + selectedMonth);
						for (var i = 0; i < $scope.monthList.length - 1; i++) {
							if (selectedMonth == $scope.monthList[i + 1]) {
								$scope. prevMonth = $scope.monthList[i] + "-"
										+ new Date().getFullYear();
							
								break;

							}

						}
						$log.debug("e.prevMonth*&*&*&*&" + $scope. prevMonth);
						if($scope. prevMonth==undefined){$scope.prevMonth=null;}
						$log.debug("e.prevMonth*&*&*2222&*&" + $scope. prevMonth);
						$scope.mon = selectedMonth + "-"
								+ new Date().getFullYear();
						$scope.getEmpLeavList($scope.mon, $scope.prevMonth);
						// $scope.getEmpLeavList(month);
						//	

					}

					$scope.employeeLeaveDetailsList = [];

					$scope.getEmpList = function() {
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

					}

					$scope.getEmpLeavList = function(month, prevMonth) {
						$scope.loading = true;
						var hrService = appEndpointSF.gethrService();
						hrService.getLeaveListEmp($scope.curUser.business.id,
								month, prevMonth).then(function(list) {

							if (list.length == 0||list==null) {
								$scope.worning=true;
							} else
								$scope.employeeLeaveDetailsList.length = 0;
							for (var i = 0; i < list.length; i++) {
								$scope.employeeLeaveDetailsList.push(list[i]);
								$scope.calculation(i);
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
