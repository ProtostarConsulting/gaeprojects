angular.module("stockApp").controller(
	"accountGroupCtr",
	function($scope, $log, $mdToast, $timeout, $state, $filter, $stateParams, appEndpointSF) {

		var blankTempAccountGroup = function() {
			return {
				groupName : "",
				description : "",
				displayOrderNo : "",
				isPrimary : false,
				accountGroupType : "NA",
				parent : null,
				business : $scope.curUser.business
			}
		};

		$scope.tempAccountGroup = $stateParams.selectedAccountGroup ? $stateParams.selectedAccountGroup : blankTempAccountGroup();
		$scope.accountGroupTypeList = [ "ASSETS", "EQUITY", "LIABILITIES",
			"INCOME", "EXPENSES", "OTHERINCOMES", "OTHEREXPENCES" ];

		$scope.addAccountGroup = function() {
			$scope.loading = true;
			var addAccountGroupService = appEndpointSF
				.getAccountGroupService();
			addAccountGroupService.addAccountGroup($scope.tempAccountGroup)
				.then(function(msgbean) {					
					if ($stateParams.selectedAccountGroup) {
						$scope.showUpdateToast();
					} else {
						$scope.showAddToast();
						$scope.tempAccountGroup = blankTempAccountGroup();
						$scope.accGroupForm.$setPristine();
						$scope.accGroupForm.$setValidity();
						$scope.accGroupForm.$setUntouched();
					}
					$scope.loading = false;
				}

			)
		};
		$scope.accountGroupList = [];
		$scope.getGroupList = function() {
			$scope.loading = true;
			var AccountGroupService = appEndpointSF
				.getAccountGroupService();
			AccountGroupService.getAccountGroupList($scope.curUser.business.id).then(function(list) {
				$scope.accountGroupList = $filter('proOrderObjectByTextField')(list, "groupName");
				$scope.loading = false;

			});

		}

		$scope.waitForServiceLoad = function() {
			if (appEndpointSF.is_service_ready) {
				$scope.getGroupList();
			} else {
				$log.debug("Services Not Loaded, watiting...");
				$timeout($scope.waitForServiceLoad, 1000);
			}
		}
		$scope.waitForServiceLoad();

		$scope.cancelBtn = function() {
			$state.go("accounting", {});

		}

	});
angular
	.module("stockApp")
	.directive(
		'accountGroupExists',
		function($log, $q, appEndpointSF) {
			return {
				restrict : 'A',
				require : 'ngModel',
				link : function($scope, $element, $attrs, ngModel) {
					$log.debug("Inside of accountGroupUserexists....");
					ngModel.$asyncValidators.accountGroupExists = function(
						accountGroupValue) {
						var deferred = $q.defer();
						var AccountGroupService = appEndpointSF
							.getAccountGroupService();
						$log.debug("accountValue:" + accountGroupValue);
						AccountGroupService
							.checkAccountGroupAlreadyExist(
								accountGroupValue)
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