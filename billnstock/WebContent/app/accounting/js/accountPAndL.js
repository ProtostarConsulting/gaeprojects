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

					$scope.totalOperatingRevenue = 0;
					$scope.totalGrossProfit = 0;
					$scope.totalOverhead = 0;
					$scope.totalPurchase = 0;
					$scope.totalSalesList = [];
					$scope.totalPurchaseList = [];
					$scope.totalPaymentList = [];
					// var list=[];

					function getOperatingRevenue() {
						for (var count = 0; count < $scope.list.length; count++) {
							var typeName = $scope.list[count].typeName;

							if ((typeName == "INCOME")
									&& ($scope.list[count].groupList != undefined)) {
								angular
										.forEach(
												$scope.list[count].groupList,
												function(groupInfo) {
													$scope.totalSalesList
															.push(groupInfo);													
												});
								$scope.totalOperatingRevenue = $scope.list[count].typeBalance;

							}

						}

						return $scope.totalOperatingRevenue;
					}
					function getOperatingExpense() {
						for (var count = 0; count < $scope.list.length; count++) {
							var typeName = $scope.list[count].typeName;

							if ((typeName == "EXPENSES")
									&& ($scope.list[count].groupList != undefined)) {
								
								angular
								.forEach(
										$scope.list[count].groupList,
										function(groupInfo) {
											$scope.totalPurchaseList
													.push(groupInfo);													
										});
								$scope.totalPurchase = $scope.list[count].typeBalance;
							}
						}

						return $scope.totalPurchase;
					}

					function getOtherExpense() {
						for (var count = 0; count < $scope.list.length; count++) {
							var typeName = $scope.list[count].typeName;

							if ((typeName == "OTHEREXPENCES")
									&& ($scope.list[count].groupList != undefined)) {
								
								angular
								.forEach(
										$scope.list[count].groupList,
										function(groupInfo) {
											$scope.totalPaymentList
													.push(groupInfo);													
										});
								$scope.totalOverhead = $scope.list[count].typeBalance;
							}
							
						}
						return $scope.totalOverhead;
					}
					
					/*function getOtherIncome() {
						for (var count = 0; count < $scope.list.length; count++) {
							var typeName = $scope.list[count].typeName;

							if ((typeName == "OTHEREXPENCES")
									&& ($scope.list[count].groupList != undefined)) {
								
								angular
								.forEach(
										$scope.list[count].groupList,
										function(groupInfo) {
											$scope.totalPaymentList
													.push(groupInfo);													
										});
								$scope.totalOverhead = $scope.list[count].typeBalance;
							}
							
						}
						return $scope.totalOverhead;
					}
					*/
					
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {

							var AccountGroupService = appEndpointSF
									.getAccountGroupService();
							AccountGroupService
									.getProfitAndLossAcc(
											$scope.curUser.business.id)
									.then(
											function(list) {
												$scope.list = list;
												var operatingRevenue = getOperatingRevenue();
												var operatingExpense = getOperatingExpense();
												$scope.grossProfit = operatingRevenue
														- operatingExpense;
												var otherExpense = getOtherExpense();
												
												$scope.operatingIncome=$scope.grossProfit-otherExpense;
												
												$scope.loading = false;

											});

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.load_pdf = function() {
						window.open("PandL?bid=" + $scope.curUser.business.id);

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
