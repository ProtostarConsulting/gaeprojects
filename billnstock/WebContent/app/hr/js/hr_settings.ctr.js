app = angular.module("stockApp");
app.controller("hrSettingsCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $state, $http, $stateParams, $routeParams,
		$filter, $mdMedia, $mdDialog, $q, objectFactory, appEndpointSF) {

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.settingsObj = {
		monthlySalaryStructureRules : [],
		monthlySalaryDeductionRules : [],
		emailNotification : false,
		emailNotificationDL : "",
	};

	$scope.salaryHeadTypes = [ 'FIXED', 'PERCENTAGE' ];

	$scope.salaryHeadNames = [];

	$scope.deductionHeadNames = [];

	$scope.addSalaryStructRule = function(salaryStruct) {
		var salaryStructObj = {
			headName : "",
			fixedValue : 0,
			percentageValue : 0,
			percentageOfHeadName : ""
		}

		if (!$scope.settingsObj.monthlySalaryStructureRules) {
			$scope.settingsObj.monthlySalaryStructureRules = [];
		}
		$scope.settingsObj.monthlySalaryStructureRules.push(salaryStructObj);
	}

	$scope.removeSalaryStructRule = function(index) {
		$scope.settingsObj.monthlySalaryStructureRules.splice(index, 1);
	}

	$scope.addDeductionRule = function() {

		var deductionObj = {
			headName : "",
			fixedValue : 0,
			percentageValue : 0,
			percentageOfHeadName : ""
		}
		if (!$scope.settingsObj.monthlySalaryDeductionRules) {
			$scope.settingsObj.monthlySalaryDeductionRules = [];
		}
		$scope.settingsObj.monthlySalaryDeductionRules.push(deductionObj);
	};

	$scope.removeDeductionRule = function(index) {
		$scope.settingsObj.monthlySalaryDeductionRules.splice(index, 1);
	}

	$scope.addSettings = function() {

		var hrService = appEndpointSF.gethrService();
		$scope.settingsObj.business = $scope.curUser.business;
		$scope.settingsObj.modifiedBy = $scope.curUser.email_id;

		hrService.addHRSettings($scope.settingsObj).then(function(savedRecord) {
			$scope.settingsObj = savedRecord;
			$scope.showUpdateToast();
			$scope.settingsObj = {};
		});
	}

	$scope.getSalHeadNames = function() {
		var hrService = appEndpointSF.gethrService();
		hrService.getSalaryHeadNames().then(function(list) {
			$scope.salaryHeadNames = list;
		})
	}

	$scope.getDeductionHeads = function() {
		var hrService = appEndpointSF.gethrService();
		hrService.getDeductionHeadNames().then(
				function(deductHeads) {
					$scope.deductionHeadNames = deductHeads;

					hrService.getSalaryHeadNames().then(function(list) {
						$scope.salaryHeadNames = list;
					});

					$scope.deductionHeads = $scope.deductionHeadNames
							.concat($scope.salaryHeadNames);

				})

	}

	$scope.getHRSettings = function() {
		var hrService = appEndpointSF.gethrService();
		hrService.getHRSettingsByBiz($scope.curUser.business.id).then(
				function(settingsList) {

					$scope.settingsObj = settingsList;

					$log.debug("Inside Ctr $scope.settingsObj:"
							+ $scope.settingsObj);
					return $scope.settingsObj;
				});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getHRSettings();
			$scope.getSalHeadNames();
			$scope.getDeductionHeads();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}
	$scope.waitForServiceLoad();

});
