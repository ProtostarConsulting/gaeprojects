angular
		.module("stockApp")
		.controller(
				"monthlyTax",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

					$scope.yearFilterList = [ String(new Date().getFullYear()),
							String(new Date().getFullYear() - 1) ];

					$scope.monthlyPayDetailsList = [];

					$scope.empIdChange = function(empId) {
						$scope.userEntity = null
						for (var int = 0; int < $scope.listUser.length; int++) {
							var employeeDetail = $scope.listUser[int].employeeDetail;
							if (employeeDetail && employeeDetail.empId == empId) {
								$scope.userEntity = $scope.listUser[int];
								var hrService = appEndpointSF.gethrService();
								hrService
										.getMonthlyPaymentByUser(
												$scope.curUser.business.id,
												$scope.userEntity)
										.then(
												function(list) {
													$scope.monthlyPayDetailsListBackup = list;

													$scope
															.fitlerListByYear($scope.yearFilterList[0]);
												});
								break;
							}
						}
					}

					$scope.fitlerListByYear = function(year) {
						$scope.monthlyPayDetailsList = [];
						angular
								.forEach(
										$scope.monthlyPayDetailsListBackup,
										function(item) {
											if (item.leaveDetailEntity.currentMonth
													.indexOf(year) > -1) {
												$scope.monthlyPayDetailsList
														.push(item);
											}
										});
						$scope.total = 0;
						$scope.totalPF = 0;
						$scope.totalPT = 0;
						$scope.totalCanteen = 0;
						$scope.totalIT = 0;
						$scope.totalOther = 0;

						var list = $scope.monthlyPayDetailsList;
						for (var i = 0; i < list.length; i++) {
							$scope.totalPF += list[i].pfDeductionAmt;
							$scope.totalPT += list[i].ptDeductionAmt;
							$scope.totalCanteen += list[i].canteenDeductionAmt;
							$scope.totalIT += list[i].itDeductionAmt;
							$scope.totalOther += list[i].otherDeductionAmt;
						}
					}

					$scope.getUserList = function() {

						var hrService = appEndpointSF.gethrService();
						hrService.getAllemp($scope.curUser.business.id).then(
								function(list) {
									$scope.listUser = list;
								});
					}

					$scope.getUserList();

				});
