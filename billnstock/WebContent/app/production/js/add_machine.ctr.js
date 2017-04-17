var app = angular.module("stockApp");

app.controller("machineAddCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,
		$mdDialog, $mdMedia) {

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.machine = {
		manifacturerDetail : $scope.manifacturerDetail,
		machineName : "",
		machineNo : "",
		manifacturerName : ""
	};
	
	$scope.machine = $stateParams.machineObj ? $stateParams.machineObj : null;

	$scope.manifacturerDetail = {
		line1 : "",
		line2 : "",
		city : "",
		state : "",
		country : "India",
		pin : "",
		phone1 : "",
		phone2 : ""
	}

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