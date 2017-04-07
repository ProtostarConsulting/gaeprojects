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
				"accountEntryListController",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory, $state,
						appEndpointSF, $mdDialog, $mdMedia,$q,ajsCache,				
						Upload) {
					$scope.loading = true;
					$scope.accountList = [];
					var entryList = [];
					$scope.closingBalance = 0;
					$scope.entries = [];
					$scope.openingBalance = 0;

					if ($stateParams.selectdAccount) {
						$scope.selectdAccount = $stateParams.selectdAccount;
					}

					if ($stateParams.selectdAccount) {
						$scope.searchAccId = $scope.selectdAccount.id;
						$scope.fromDate = $stateParams.fromDate;
						$scope.toDate = $stateParams.toDate;
					}

					$scope.getAccountEntryByAccountId = function(accId,
							fromDate, toDate) {
						$scope.loading = true;
						$scope.wait = true;
						var Ac = appEndpointSF.getAccountService();
					Ac.getAccountBalance(accId,$scope.curUser.business.id).then(function(list1){
					var AccountEntryService = appEndpointSF.getAccountEntryService();
					var fromDate=new Date($scope.fromDate).getTime();
					var toDate=new Date($scope.toDate).getTime();
					
						AccountEntryService.getAccountViewEntryByAccountId(fromDate,toDate,accId,$scope.curUser.business.id)
								.then(
										function(list) {
											list = list.items; 
											$scope.totaldebit = 0;
											$scope.totalcredit = 0;
											$log.debug("list:" + list);
											entryList = [];
											for (var i = 0; i < list.length; i++) {
												entryList.push(list[i]);
													
													if (angular	.isNumber(list[i].debit)) {
														$scope.totaldebit = $scope.totaldebit
																+ parseFloat(list[i].debit);
													}
													if (angular
															.isNumber(list[i].credit)) {
														$scope.totalcredit = $scope.totalcredit
																+ parseFloat(list[i].credit);
													}
													}
											$scope.entries = [];
											$scope.closingBalance = 0;
											$scope.closingBalance=list1.returnBalance;
											$log.debug("BALANCE"+list1.returnBalance);
											$scope.entries = entryList;
											if ($scope.entries.length == 0) {
												$scope.showAlert();
											}var maxWaitTime = 1000 * 5;
											var currentWaitTime = 0;
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
											$scope.getselectdAccountName(accId);
										})
						})
						};
					$scope.getselectdAccountName = function(accId) {
						for (var i = 0; i < $scope.accountList.length; i++) {
							if ($scope.accountList[i].id == accId) {
								$scope.selectdAccountName = $scope.accountList[i].accountName;
							}
						}
					}
					$scope.showAlert = function(ev) {
						$scope.wait = true ;
						$mdDialog
								.show($mdDialog
										.alert()
										.parent(
												angular
														.element(document
																.querySelector('#popupContainer')))
										.clickOutsideToClose(true)
										.title('No Data Found')
										.textContent(
												'Related Account Data with in Date not Found.')
										.ariaLabel('Alert Dialog Demo').ok(
												'Got it!').targetEvent(ev));
					};
					$scope.getAccountList = function() {
						var AccountService = appEndpointSF.getAccountService();
						AccountService
								.getAccountList($scope.curUser.business.id)
								.then(
										function(list) {
											$scope.accountList = list;
											if ($scope.selectdAccount) {
												$scope
														.getAccountEntryByAccountId(
																$scope.searchAccId,
																$scope.fromDate,
																$scope.toDate);
											}
									});
					}
					$scope.cancelButton = function() {
						$state.go("accounting.accountGroupView", {
							selectdAccount : $scope.selectdAccount,
							flag : true,
							fromDate : $scope.fromDate,
							toDate : $scope.toDate
						});
					}
					$scope.clear = function() {
						$scope.loading = true;
						if ($scope.toDate != undefined) {
							$scope.toDate = "";
						}
						if ($scope.fromDate != undefined) {
							$scope.fromDate = "";
						}
						if ($scope.searchAccId != undefined) {
							$scope.searchAccId = "";
						}
						$scope.searchForm.$setPristine();
						$scope.searchForm.$setUntouched();
					}
					$scope.downloadData=function(searchAccId){
						
						
						var fromDate=new Date($scope.fromDate).getTime();
						var toDate=new Date($scope.toDate).getTime();
						
						document.location.href="DownloadAccountViewServlet?id="+ $scope.curUser.business.id+"&fromDate="+fromDate+"&toDate="+toDate+"&searchAccId="+searchAccId;
						}
					$scope.printDiv = function(divId) {
						window.frames["print_frame"].document.body.innerHTML = document
								.getElementById(divId).innerHTML;
						window.frames["print_frame"].window.focus();
						window.frames["print_frame"].window.print();
					}
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAccountList();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();
				});
