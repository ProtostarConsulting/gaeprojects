var app = angular.module("stockApp");

app.controller("qcmachineAddCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,
		$mdDialog, $mdMedia) {

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.qcmachine = {
			qcName : "",
			validFrom : new Date(),
			validTill : new Date(),
			schedule : "",
			scheduleTime : "",
			startFromTime : new Date(),
			tillTime : new Date(),
			parameterList : [],
			note : ""
	};
	
	$scope.machine = $stateParams.machineObj ? $stateParams.machineObj : null;

	$scope.qcparameter = {
			name : "",
			parameterType : "",
			numberIdealValue : "",
			numberIdealValueValidDeviationPerc : ""
	}
	
	$scope.qcmachine.parameterList.push($scope.qcparameter);

	$scope.getEmptyMachineObj = function() {
		return {
			manifacturerDetail : $scope.manifacturerDetail,
			machineName : "",
			machineNo : ""
		}
	};

	$scope.addMachine = function() {

		$scope.machine.business = $scope.curUser.business;
		$scope.machine.modifiedBy = $scope.curUser.email_id;

		var productService = appEndpointSF.getProductionService();
		productService.addMachine($scope.machine).then(function(machineObj) {
			if(machineObj.id != undefined){
				$scope.showAddToast();
			}
		});
		
		$scope.getEmptyMachineObj();
	}
});