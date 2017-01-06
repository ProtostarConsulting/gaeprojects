app = angular.module("stockApp");
app.controller("invoiceSettingsCtr", function($scope, $window, $mdToast,
		$timeout, $mdSidenav, $mdUtil, $log, $state, $http, $stateParams,
		$routeParams, $filter, $mdMedia, $mdDialog, $q, objectFactory,
		appEndpointSF) {

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.settingsObj = {};

	$scope.addInvoiceSettings = function() {

		var InvoiceService = appEndpointSF.getInvoiceService();
		$scope.settingsObj.business = $scope.curUser.business;
		$scope.settingsObj.modifiedBy = $scope.curUser.email_id;

		InvoiceService.addInvoiceSettings($scope.settingsObj).then(
				function(savedRecoed) {
					$scope.settingsObj = savedRecoed;
					$scope.showUpdateToast();

				});

	}

	$scope.getInvoiceSettingsByBiz = function() {
		var invoiceService = appEndpointSF.getInvoiceService();
		invoiceService.getInvoiceSettingsByBiz($scope.curUser.business.id)
				.then(
						function(settingsList) {

							$scope.settingsObj = settingsList;
							$log.debug("Inside Ctr $scope.settingsObj:"
									+ $scope.settingsObj);
							return $scope.settingsObj;
						});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getInvoiceSettingsByBiz();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}
	$scope.waitForServiceLoad();

});
