angular
		.module("stockApp")
		.controller(
				"monthlyTax",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

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
													$scope.monthlyPayDetailsList = list;
													$scope.total = 0;
													$scope.totalPF = 0;
													$scope.totalPT = 0;
													$scope.totalCanteen = 0;
													$scope.totalIT = 0;
													$scope.totalOther = 0;

													for (var i = 0; i < $scope.monthlyPayDetailsList.length; i++) {
														$scope.totalPF += list[i].pfDeductionAmt;
														$scope.totalPT += list[i].ptDeductionAmt;
														$scope.totalCanteen += list[i].canteenDeductionAmt;
														$scope.totalIT += list[i].itDeductionAmt;
														$scope.totalOther += list[i].otherDeductionAmt;

													}
												});
							}
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
