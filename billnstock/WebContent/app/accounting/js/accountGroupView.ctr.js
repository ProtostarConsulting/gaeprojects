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
				"accountGroupViewCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory, $state,
						appEndpointSF, $mdDialog, $mdMedia, ajsCache) {

					$scope.loading = true;

					$scope.accountList = [];
					$scope.entryList = [];
					$scope.flag = $stateParams.flag;

					$scope.getAccountListByGroupId = function(groupId) {
						$scope.loading = true;
						$scope.wait = true;
						$scope.accountList = [];

						var AccountService = appEndpointSF.getAccountService();
						AccountService
								.getGroupViewtByGroupId($scope.curUser.business.id,groupId)
								.then(
										function(list) {
												
											$scope.grandDebitTotal = 0;
											$scope.grandCreditTotal = 0;
											$scope.accountList = list;
											
											for (var i = 0; i < list.length; i++) {
												$scope.getAccountEntryByAccountId(list[i].id, i);
											}
										
											var currentWaitTime = 0;
											var maxWaitTime = 10000;
											$scope.waitFn = function() {
												if (currentWaitTime < maxWaitTime) {
													$log
															.debug("Wating for account  Data ...");
													currentWaitTime += 1000;
													$timeout($scope.waitFn,
															1000);
												} else if (currentWaitTime == maxWaitTime) {
													$scope.loading = false;
													$scope.wait = false;

												}
											}
											$scope.waitFn();
											if (!$scope.flag) {
											   $scope.getSelectedGroupName(groupId);
											}

										})

					};

					$scope.getSelectedGroupName = function(groupId) {
						for (var i = 0; i < $scope.GroupList.length; i++) {
							if ($scope.GroupList[i].id == groupId) {
								$scope.selectedGroupName = $scope.GroupList[i].groupName;
							}
						}
					}
					$scope.getAccountGroupList = function() {

						var AccountGroupService = appEndpointSF
								.getAccountGroupService();
						AccountGroupService
								.getAccountGroupList($scope.curUser.business.id)
								.then(
										function(list) {

											$scope.GroupList = list;
											$scope.accountList = [];

											if ($scope.flag) {
												$scope.groupId = $stateParams.selectdAccount.accountgroup.id;
												$scope.selectedGroupName = $stateParams.selectdAccount.accountgroup.groupName;
												$scope.fromDate = $stateParams.fromDate;
												$scope.toDate = $stateParams.toDate;
												//$scope.getAccountListByGroupId($scope.groupId);
											}

										})

					};

					$scope.getAccountEntryByAccountId = function(accId, acIndex) {

						var AccountEntryService = appEndpointSF
								.getAccountEntryService();
						AccountEntryService
								.getAccountEntryByAccountId(accId)
								.then(
										function(list) {

											$scope.entryList = list.items;

											$scope.totaldebit = 0;
											$scope.totalcredit = 0;

											for (var i = 0; i < $scope.entryList.length; i++) {
												$scope.accountList[acIndex].totaldebit = 0;
												$scope.accountList[acIndex].totalcredit = 0;
												
												if (new Date($scope.entryList[i].date) >= new Date(
														$scope.fromDate)
														&& new Date(
																$scope.entryList[i].date) <= new Date(
																$scope.toDate)) {

													if ($scope.accountList.length > 0) {
														if (angular
																.isNumber($scope.entryList[i].debit)) {

															$scope.totaldebit = $scope.totaldebit
																	+ parseFloat($scope.entryList[i].debit);

														}
													}
													if ($scope.accountList.length > 0) {
														if (angular
																.isNumber($scope.entryList[i].credit)) {
															$scope.totalcredit = $scope.totalcredit
																	+ parseFloat($scope.entryList[i].credit);
														}
													}

												}

												if ($scope.accountList.length > 0
														&& $scope.accountList[acIndex] != undefined) {
													$scope.accountList[acIndex].totaldebit = $scope.totaldebit;
													$scope.accountList[acIndex].totalcredit = $scope.totalcredit;
												}

											}
											if ($scope.accountList.length > 0
													&& $scope.accountList[acIndex] != undefined) {
												$scope.grandCreditTotal = $scope.grandCreditTotal
														+ $scope.accountList[acIndex].totalcredit;
												$scope.grandDebitTotal = $scope.grandDebitTotal
														+ $scope.accountList[acIndex].totaldebit;
											}
										})

					};

					$scope.clear = function() {
						$scope.loading = true;

						$scope.toDate = "";
						$scope.fromDate = "";
						$scope.groupId = "";

						$scope.searchForm.$setPristine();
						$scope.searchForm.$setUntouched();

					}

					var printDivCSS = new String(
							'<link href="/lib/base/css/angular-material.min.css"" rel="stylesheet" type="text/css">'
									+ '<link href="/lib/base/css/bootstrap.min.css"" rel="stylesheet" type="text/css">')
					$scope.printDiv = function(divId) {

						window.frames["print_frame"].document.body.innerHTML = document
								.getElementById(divId).innerHTML;
						window.frames["print_frame"].window.focus();
						window.frames["print_frame"].window.print();
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAccountGroupList();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();
				});
