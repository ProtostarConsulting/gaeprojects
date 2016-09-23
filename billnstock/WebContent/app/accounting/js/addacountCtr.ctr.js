var app = angular.module("stockApp");

app
		.controller(
				"addacountCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

					$scope.query = {
						order : 'name',
						limit : 5,
						page : 1
					};
					$scope.accountId = $stateParams.AccountId;

					$scope.account = {
						accountName : "",
						accountNo : "",
						displayOrderNo : "",
						description : "",
						contra : false,
						accountgroup : "",
						business: $scope.curUser.business

					};
					$scope.cancle = function(accountId) {

						if (accountId != undefined) {
							$state.go('accounting.accountlist', {});
						} else {
							$state.go('accounting', {});
						}

					}

				$scope.accountType = [ 'PERSONAL', 'REAL',
						'NOMINAL' ],

				$scope.addAccount = function() {

					var accountservice = appEndpointSF
							.getAccountService();

					for (var i = 0; i < $scope.accountgroupList.length; i++) {
						if ($scope.account.accountgroup.groupName == $scope.accountgroupList[i].groupName) {
							$scope.account.accountgroup = $scope.accountgroupList[i];
						}
					}

					accountservice.addAccount1($scope.account)
							.then(function() {
								if ($stateParams.AccountId == "")
									$scope.showAddToast();
								else
									$scope.showUpdateToast();

							});

					/*
					 * if ($scope.accountId == "") {
					 * $scope.showAddToast(); } else {
					 * $scope.showUpdateToast(); }
					 */
					$scope.account = "";
					$scope.accountForm.$setPristine();
					$scope.accountForm.$setValidity();
					$scope.accountForm.$setUntouched();
				}

					$scope.accountgroupList = [];
					$scope.getGrouplist = function() {

						var listAccountGroupService = appEndpointSF
								.getAccountGroupService();
						listAccountGroupService
								.getAccountGroupList($scope.curUser.business.id)
								.then(
										function(list) {
											/* $log.debug("list:"+angular.toJson(list)); */
											$scope.accountgroupList = list;
											$log
													.debug("accountgroup: "
															+ angular
																	.toJson($scope.accountgroup));
											/* ajsCache.put(AccountGroupServiceCacheKey,list); */
										});

					}
/*
					$scope.getAccByid = function() {

						var getrecord = appEndpointSF.getAccountService();
						getrecord.getAccountById($scope.accountId).then(
								function(account) {
									$scope.account = account;

								});
					}*/

				$scope.getGrouplist();
				//	$scope.getAccByid();

				});

angular
		.module("stockApp")
		.directive(
				'accountUserexists',
				function($log, $q, appEndpointSF) {
					return {
						restrict : 'A',
						require : 'ngModel',
						link : function($scope, $element, $attrs, ngModel) {
							$log.debug("Inside of accountUserexists....");
							ngModel.$asyncValidators.userexists = function(
									accountValue) {
								var deferred = $q.defer();
								var AccountService = appEndpointSF
										.getAccountService();
								$log.debug("accountValue:" + accountValue);
								AccountService
										.checkAccountAlreadyExist(accountValue)
										.then(
												function(response) {
													$log
															.debug("Inside of userexists validator fn: "
																	+ response.returnBool);
													if (response.returnBool == true) {
														deferred.reject();
													} else {
														deferred.resolve();
													}
												});
								return deferred.promise;
							}

						}
					};
				});
