app = angular.module("stockApp");
app.controller("bizSettingsCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $state, $http, $stateParams, $routeParams,
		$filter, $mdMedia, $mdDialog, $q, objectFactory, appEndpointSF) {

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	function defaultActionProcessing() {
		return {
			saving : false,
			saveButtonText : "Save Changes",
			savingButtonText : "Saving..."
		};
	}
	
	$scope.actionProcessing = defaultActionProcessing();

	$scope.settingsObj = {}

	$scope.addSettings = function() {

		var userService = appEndpointSF.getUserService();
		if (!$scope.settingsObj.business)
			$scope.settingsObj.business = $scope.curUser.business;

		$scope.settingsObj.modifiedBy = $scope.curUser.email_id;
		$scope.actionProcessing.saving = true;
		userService.addBusinessSettingsEntity($scope.settingsObj).then(
				function(savedRecoed) {
					$scope.settingsObj = savedRecoed;
					$scope.showUpdateToast();
					$scope.actionProcessing = defaultActionProcessing();
				});
	}

	$scope.getSettingsByBiz = function() {
		var userService = appEndpointSF.getUserService();
		userService.getBusinessSettingsEntity($scope.curUser.business.id).then(
				function(settingsList) {
					$scope.settingsObj = settingsList;
				});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getSettingsByBiz();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}
	$scope.waitForServiceLoad();

});
