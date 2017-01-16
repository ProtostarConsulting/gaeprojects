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
				"accountBalanceSheetCtr",
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
									.getBalanceSheet($scope.curUser.business.id)
									.then(
											function(list) {
												$scope.totalAsset = 0
												$scope.totalLiabilities2 = 0;
												$scope.totalEQUITY = 0;
												$scope.totalLiabilities = 0;
												$scope.accountGroupTypeGroupList = list;
												for (var int = 0; int < list.length; int++) {
													if ((list[int].typeName == "Assets")
															&& (list[int].groupList != undefined)) {
														for (var i = 0; i < list[int].groupList.length; i++) {
															$scope.totalAsset = list[int].groupList[i].groupBalance
																	+ $scope.totalAsset;
														}
														if ($scope.totalAsset < 0) {
															$scope.totalAsset = $scope.totalAsset
																	* (-1);
														}
													}

													if ((list[int].typeName == "Liabilities")
															&& (list[int].groupList != undefined)) {
														for (var i = 0; i < list[int].groupList.length; i++) {
															$scope.totalLiabilities = list[int].groupList[i].groupBalance
																	+ $scope.totalLiabilities;
														}

														if ($scope.totalLiabilities < 0) {
															$scope.totalLiabilities = $scope.totalLiabilities
																	* (-1);
														}

													}
													if ((list[int].typeName == "EQUITY")
															&& (list[int].groupList != undefined)) {
														for (var i = 0; i < list[int].groupList.length; i++) {
															$scope.totalEQUITY = list[int].groupList[i].groupBalance
																	+ $scope.totalEQUITY;
														}

													}
												}
												$scope.totalLiabilities2 = $scope.totalLiabilities
														+ $scope.totalEQUITY;
												$scope.loading = false;

											});

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.load_pdf = function() {
						window.open("PdfBalanceSheet?bid="
								+ $scope.curUser.business.id);
						// document.location.href="PdfBalanceSheet";

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
