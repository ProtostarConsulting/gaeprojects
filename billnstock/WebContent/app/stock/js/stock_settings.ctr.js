app = angular.module("stockApp");
app.controller("stockSettingsCtr", function($scope, $window, $mdToast,
		$timeout, $mdSidenav, $mdUtil, $log, $state, $http, $stateParams,
		$routeParams, $filter, $mdMedia, $mdDialog, $q, objectFactory,
		appEndpointSF) {

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.settingsObj = {}

	$scope.addSettings = function() {

		var StockService = appEndpointSF.getStockService();
		$scope.settingsObj.business = $scope.curUser.business;
		$scope.settingsObj.modifiedBy = $scope.curUser.email_id;

		StockService.addStockSettings($scope.settingsObj).then(
				function(savedRecoed) {
					$scope.settingsObj = savedRecoed;
					$scope.showUpdateToast();
				});
	}

	$scope.getStockSettingsByBiz = function() {

		var invoiceService = appEndpointSF.getStockService();

		invoiceService.getStockSettingsByBiz($scope.curUser.business.id).then(
				function(settingsList) {

					$scope.settingsObj = settingsList;
					$log.debug("Inside Ctr $scope.settingsObj:"
							+ $scope.settingsObj);
					return $scope.settingsObj;
				});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getStockSettingsByBiz();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}
	$scope.waitForServiceLoad();
	
});
