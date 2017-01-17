angular.module("stockApp")
	.controller("accountAddCtr", function($scope, $state, $filter, $mdToast, $stateParams, $log, $timeout, appEndpointSF) {

		$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

		$scope.accountTypeList = [ 'PERSONAL', 'REAL',
			'NOMINAL' ];

		var blankAccount = function() {
			return {
				accountName : "",
				accountNo : "",
				displayOrderNo : "",
				description : "",
				contra : false,
				accountGroup : "",
				modifiedBy : $scope.curUser.email_id,
				business : $scope.curUser.business
			}

		};
		$scope.tempAccount = $stateParams.selectedAccount ? $stateParams.selectedAccount : blankAccount();

		$scope.cancel = function(accountId) {
			if (accountId != undefined) {
				$state.go('accounting.accountlist', {});
			} else {
				$state.go('accounting', {});
			}
		}


		$scope.addAccount = function() {
			var accountservice = appEndpointSF
				.getAccountService();
			$scope.tempAccount.modifiedBy = $scope.curUser.email_id;
			accountservice.addAccount($scope.tempAccount)
				.then(function(acc) {
					if ($stateParams.selectedAccount) {
						$scope.showUpdateToast();
					} else {
						$scope.showAddToast();
						$scope.tempAccount = blankAccount();
						$scope.accountForm.$setPristine();
						$scope.accountForm.$setValidity();
						$scope.accountForm.$setUntouched();
					}
				});

		}

		$scope.accountGroupList = [];
		$scope.getGrouplist = function() {
			$scope.loading = true;
			var listAccountGroupService = appEndpointSF
				.getAccountGroupService();
			listAccountGroupService
				.getAccountGroupList($scope.curUser.business.id)
				.then(
					function(list) {
						$scope.accountGroupList = $filter('proOrderObjectByTextField')(list, "groupName");
						$scope.loading = false;
					});

		}


		$scope.waitForServiceLoad = function() {
			if (appEndpointSF.is_service_ready) {
				$scope.getGrouplist();
			} else {
				$log.debug("Services Not Loaded, watiting...");
				$timeout($scope.waitForServiceLoad, 1000);
			}
		}
		$scope.waitForServiceLoad();


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