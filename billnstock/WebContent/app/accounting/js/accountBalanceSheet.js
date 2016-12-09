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

					$scope.totalTypeCount = $scope.accountGroupTypeList.length;
					$scope.accountGroupTypeGroupList = [];

					$scope.getAccountListByGroup = function(groupObj) {
						$scope.accountList = [];
						$scope.noOfGroupTypesCallsDone += 1;
						var AccountService = appEndpointSF.getAccountService();
						AccountService
								.getAccountListByGroupId(groupObj.id)
								.then(
										function(list) {
											$scope.fnReturnCount = 0;
											var maxWaitTime = 1000 * 5;
											var currentWaitTime = 0;

											for (var i = 0; i < list.length; i++) {
												getAccountBalance(list[i]);
											}

											$scope.waitForAllAccountData = function() {
												if ($scope.fnReturnCount != list.length
														&& currentWaitTime < maxWaitTime) {
													$log
															.debug("Wating for account data in a group:"
																	+ groupObj.groupName);
													currentWaitTime += 1000;
													$timeout(
															$scope.waitForAllAccountData,
															1000);
												} else {
													$scope.getGroupBalance();

													$scope.noOfGroupTypesLoaded += 1;

													if ($scope.noOfGroupTypesLoaded == $scope.noOfGroupTypesCallsDone) {
														$scope.loading = false;
													}

												}
											}
											$scope.waitForAllAccountData();

											$scope.accountList = list;
											if (list == null) {
												groupObj.accountList = [];
											} else {
												groupObj.accountList = list;
											}
										})

					};

					function getAccountBalance(accounObj) {
						var AccountService = appEndpointSF.getAccountService();
						AccountService
								.getAccountBalance(accounObj.id)
								.then(
										function(objResp) {
											accounObj.balance = objResp.returnBalance;
											$scope.fnReturnCount = $scope.fnReturnCount + 1;
										});
					}

					$scope.getAccountGroupListByType = function(groupTypeObj) {

						var AccountGroupService = appEndpointSF
								.getAccountGroupService();
						AccountGroupService
								.getAccountGroupListByType(
										groupTypeObj.groupType,
										$scope.curUser.business.id)
								.then(
										function(list) {
											$scope.GroupList = list;
											$scope.accountList = [];
											groupTypeObj.groupList = list;
											$scope.totalTypeCount--;
											if (groupTypeObj.groupList != undefined) {
												for (var i = 0; i < groupTypeObj.groupList.length; i++) {
													$scope
															.getAccountListByGroup(groupTypeObj.groupList[i]);

												}
											}
										})

					};

					function getGroupTypeObject(groupTypeValue) {
						return {
							groupType : groupTypeValue,
							groupList : []
						};
					}
					function getGroupAccObject(groupObj) {
						return {
							groupObj : groupObj,
							accountList : []
						};
					}

					$scope.getGroupBalance = function() {

						for (var i = 0; i < $scope.accountGroupTypeGroupList.length; i++) {
							if ($scope.accountGroupTypeGroupList[i].groupList.length > 0) {
								for (var j = 0; j < $scope.accountGroupTypeGroupList[i].groupList.length; j++) {

									if ($scope.accountGroupTypeGroupList[i].groupList[j].accountList != undefined
											&& $scope.accountGroupTypeGroupList[i].groupList[j].accountList.length > 0) {
										$scope.totalBalance = 0;
										for (var k = 0; k < $scope.accountGroupTypeGroupList[i].groupList[j].accountList.length; k++) {

											$scope.totalBalance = $scope.totalBalance
													+ $scope.accountGroupTypeGroupList[i].groupList[j].accountList[k].balance;

										}
										$scope.accountGroupTypeGroupList[i].groupList[j].totalBalance = $scope.totalBalance;
									}

								}
							}
						}
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {

							for (var i = 0; i < $scope.accountGroupTypeList.length; i++) {
								groupTypeObj = getGroupTypeObject($scope.accountGroupTypeList[i]);
								$scope.accountGroupTypeGroupList
										.push(groupTypeObj);
								$scope.getAccountGroupListByType(groupTypeObj);// ,$scope.curUser.business.id);
							}

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
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
