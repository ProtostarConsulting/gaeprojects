var app = angular.module("stockApp");

app.controller("qcmachineAddRecordCtr", function($scope, $window, $mdToast,
		$timeout, $mdSidenav, $mdUtil, $log, $stateParams, objectFactory,
		appEndpointSF, $mdDialog, $mdMedia) {

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.qcmachineRecord = {
		machine : "",
		recordDate : new Date()
	};

	$scope.isTableShow = false;

	$scope.machineList = [];
	$scope.machineParamList = [];
	$scope.tempMachine = null;
	$scope.timeArray = [ "00:00 AM", "1:00 AM", "2:00 AM", "3:00 AM",
			"4:00 AM", "5:00 AM", "6:00 AM", "7:00 AM", "8:00 AM", "9:00 AM",
			"10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM",
			"3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM",
			"9:00 PM", "10:00 PM", "11:00 PM" ];

	$scope.getMachineParamList = function(machine) {

		$scope.isTableShow = true;
		var productService = appEndpointSF.getProductionService();
		productService.getMachineById($scope.curUser.business.id, machine.id)
				.then(
						function(machineObj) {
							$scope.tempMachine = machineObj;
							$scope.scheduleFromTime = new Date(
									machineObj.startFromTime)
									.toLocaleTimeString('en-US', {
										hour12 : true,
										hour : "numeric",
										minute : "numeric"
									}).toString();
							$scope.scheduleTillTime = new Date(
									machineObj.tillTime).toLocaleTimeString(
									'en-US', {
										hour12 : true,
										hour : "numeric",
										minute : "numeric"
									}).toString();
							$scope.machineParamList = machineObj.parameterList;
							console.log("schedule FromTime ----"
									+ $scope.scheduleFromTime);
							console.log("schedule TillTime ----"
									+ $scope.scheduleTillTime);
						});
	}

	$scope.fetchMachineList = function() {

		var productService = appEndpointSF.getProductionService();
		productService.getQCMachineList($scope.curUser.business.id).then(
				function(list) {
					$scope.machineList = list;
				});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.fetchMachineList();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}

	$scope.waitForServiceLoad();
});