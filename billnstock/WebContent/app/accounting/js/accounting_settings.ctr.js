app = angular.module("stockApp");
app.controller("accountingSettingsCtr", function($scope, $window, $mdToast,
		$timeout, $mdSidenav, $mdUtil, $log, $state, $http, $stateParams,
		$routeParams, $filter, $mdMedia, $mdDialog, $q, objectFactory,
		appEndpointSF) {

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.settingsObj = {
		business : $scope.curUser.business
	};

	$scope.currentFY = {
		business : $scope.curUser.business,
		fromDate : null,
		toDate : null
	};

	$scope.addCurrentFinancialYear = function() {
		var accountService = appEndpointSF.getAccountService();
		$scope.currentFY.modifiedBy = $scope.curUser.email_id;
		accountService.addCurrentFinancialYear($scope.currentFY).then(
				function(savedRecord) {
					$scope.currentFY = savedRecord;
					$scope.showUpdateToast();
				});
	}

	$scope.addAccountingSettingsEntity = function() {
		var accountService = appEndpointSF.getAccountService();
		$scope.settingsObj.modifiedBy = $scope.curUser.email_id;

		accountService.addAccountingSettingsEntity($scope.settingsObj).then(
				function(savedRecord) {
					$scope.settingsObj = savedRecord;
					$scope.showUpdateToast();
				});

	}

	$scope.getAccountingSettingsEntity = function() {
		var accountService = appEndpointSF.getAccountService();
		accountService.getCurrentFinancialYear($scope.curUser.business.id)
				.then(function(currentFY) {
					if (currentFY.id){
						currentFY.fromDate = new Date(currentFY.fromDate);
						currentFY.toDate = new Date(currentFY.toDate);
						$scope.currentFY = currentFY;
					}
				});
		accountService.getAccountingSettingsEntity($scope.curUser.business.id)
				.then(function(settingsObj) {
					if (settingsObj.id)
						$scope.settingsObj = settingsObj;
				});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getAccountingSettingsEntity();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}
	$scope.waitForServiceLoad();

});
