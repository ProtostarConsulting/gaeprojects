var app = angular.module("stockApp");

app.controller("qcmachineAddCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,
		$mdDialog, $mdMedia, $mdpDatePicker, $mdpTimePicker) {

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.qcmachine = {
		qcName : "",
		validFrom : new Date(),
		validTill : new Date(),
		parameterList : [],
		note : "",
		machine : "",
		schedule : "",
		scheduleTime : "",
		startFromTime : moment('3:50 PM', "hh:mm A"),
		tillTime : moment('3:50 PM', "hh:mm A"),
	};

	$scope.qcmachine = $stateParams.qcmachineObj ? $stateParams.qcmachineObj
			: $scope.qcmachine;

	$scope.machineList = [];

	$scope.qcparameterType = [ "NUMBER", "NUMBERRANGE", "YESNO", "TEXT" ];
	$scope.tempScheduler = [ "DAILY", "WEEKLY", "MONTHLY", "YEARLY" ];
	$scope.tempSchedulerTime = [ "HOURS", "MINTUES" ];
	$scope.unitMeasure = [ "dB", "joule", "fahrenheit", "celsius" ];

	$scope.qcparameter = {
		name : "",
		parameterType : "",
		measureUnit : "",
		numberIdealValue : "",
		numberIdealValueValidDeviationPerc : ""
	}

	$scope.getEmptyMachineObj = function() {
		return {
			manifacturerDetail : $scope.manifacturerDetail,
			machineName : "",
			machineNo : ""
		}
	};

	$scope.addQCMachine = function() {

		$scope.qcmachine.business = $scope.curUser.business;
		$scope.qcmachine.modifiedBy = $scope.curUser.email_id;

		var productService = appEndpointSF.getProductionService();
		productService.addQCMachine($scope.qcmachine).then(
				function(machineObj) {
					if (machineObj.id != undefined) {
						$scope.showAddToast();
					}
				});

		$scope.getEmptyMachineObj();
	}

	$scope.addNewQcParameter = function() {
		$scope.qcparameter = {
			name : "",
			parameterType : "",
			numberIdealValue : "",
			numberIdealValueValidDeviationPerc : ""
		}
		$scope.qcmachine.parameterList.push($scope.qcparameter);
	};

	$scope.removeParamterType = function(index) {
		$scope.qcmachine.parameterList.splice(index, 1);
	}

	$scope.initLoad = function(qcmachine) {
		$scope.qcmachine = qcmachine;
		$scope.qcmachine.validFrom = new Date(qcmachine.validFrom);
		$scope.qcmachine.validTill = new Date(qcmachine.validTill);
		$scope.qcmachine.startFromTime = new Date(qcmachine.startFromTime);
		$scope.qcmachine.tillTime = new Date(qcmachine.tillTime);
	}

	$scope.fetchMachineList = function() {

		var productService = appEndpointSF.getProductionService();
		productService.getMachineList($scope.curUser.business.id).then(
				function(list) {
					$scope.machineList = list;
				});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.fetchMachineList();
			if ($scope.qcmachine) {
				$scope.initLoad($scope.qcmachine);
			}
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}

	$scope.waitForServiceLoad();
});