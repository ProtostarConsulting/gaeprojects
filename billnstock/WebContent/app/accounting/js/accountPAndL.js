var app = angular.module("stockApp");
app.filter('positive', function() {
	return function(input) {
		if (!input) {
			return 0;
		}

		return Math.abs(input);
	};
});
app
		.controller(
				"accountPAndL",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory, $state,
						appEndpointSF, $mdDialog, $mdMedia) {

					$scope.loading = true;
					$scope.totalBalance;

					$scope.accountGroupTypeList = [ "Assets", "EQUITY",
							"Liabilities", "Incomes", "Expenses",
							"OTHERINCOMES", "OTHEREXPENCES" ];

					$scope.noOfGroupTypesLoaded = 0;
					$scope.noOfGroupTypesCallsDone = 0;

					$scope.saveToPDF = function() {
						window.open("DownloadBalanceSheetServlet");
					}

					$scope.accountGroupTypeGroupList = [];

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {

							var AccountGroupService = appEndpointSF
									.getAccountGroupService();
							AccountGroupService
									.getProfitAndLossAcc(
											$scope.curUser.business.id)
									.then(
											function(list) {
												$scope.totalSales = 0
												$scope.totalIndirectExpences2 = 0;
												$scope.totalPurches = 0;
												$scope.totalIndirectExpences = 0;
												$scope.totalGrossProfit = 0;
												$scope.totalGrossLoss = 0;
												$scope.totalLeft = 0;
												$scope.totalRight = 0;
												$scope.accountGroupTypeGroupList = list;
												$scope.totalOtherExp = 0;
												$scope.nettProfit = 0;
												for (var int = 0; int < list.length; int++) {
													var typeName = list[int].typeName;
													if ((typeName == "INCOME")
															&& (list[int].groupList != undefined)) {
														for (var i = 0; i < list[int].groupList.length; i++) {
															$scope.totalSales = list[int].groupList[i].groupBalance
																	+ $scope.totalSales;
														}
														if ($scope.totalSales < 0) {
															$scope.totalSales = $scope.totalSales
																	* (-1);
														}
													}

													if ((typeName == "OTHEREXPENCES")
															&& (list[int].groupList != undefined)) {
														for (var i = 0; i < list[int].groupList.length; i++) {
															$scope.totalIndirectExpences = list[int].groupList[i].groupBalance
																	+ $scope.totalIndirectExpences;

														}

														if ($scope.totalIndirectExpences < 0) {
															$scope.totalIndirectExpences = $scope.totalIndirectExpences
																	* (-1);
														}

													}
													if ((typeName == "EXPENSES")
															&& (list[int].groupList != undefined)) {
														for (var i = 0; i < list[int].groupList.length; i++) {
															$scope.totalPurches = list[int].groupList[i].groupBalance
																	+ $scope.totalPurches;
														}
														if($scope.totalPurches<0){$scope.totalPurches=$scope.totalPurches*-1;}

													}

												}

												$scope.totalIndirectExpences2 = $scope.totalIndirectExpences
														+ $scope.totalPurches;
												if($scope.totalIndirectExpences2<0){$scope.totalIndirectExpences2=$scope.totalIndirectExpences2*(-1);}
												
												$scope.totalGrossProfit = $scope.totalSales
														- $scope.totalPurches;
												
												if($scope.totalGrossProfit<0){$scope.totalGrossLoss=$scope.totalGrossProfit;}
												$scope.totalLeft = $scope.totalPurches
														+ $scope.totalGrossProfit;
												if($scope.totalLeft<0){$scope.totalLeft=$scope.totalLeft*(-1);}
												$scope.totalRight = $scope.totalSales;
												$scope.nettProfit=$scope.totalGrossProfit-$scope.totalIndirectExpences;
											//	if($scope.nettProfit<0){$scope.nettProfit=$scope.nettProfit*(-1);}
												$scope.loading = false;

											});

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.load_pdf = function() {
						window.open("PandL?bid="
								+ $scope.curUser.business.id);

					}

					$scope.waitForServiceLoad();

					var printDivCSS = new String(
							'<link href="/lib/base/css/angular-material.min.css"" rel="stylesheet" type="text/css">'
									+ '<link href="/lib/base/css/bootstrap.min.css"" rel="stylesheet" type="text/css">')
					$scope.printDiv = function(divId) {

						window.frames["print_frame"].document.body.innerHTML = document
								.getElementById(divId).innerHTML;
						window.frames["print_frame"].window.focus();
						window.frames["print_frame"].window.print();
					}
				});
