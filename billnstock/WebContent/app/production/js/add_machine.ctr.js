var app = angular.module("stockApp");

app.controller("machineAddCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,
		$mdDialog, $mdMedia) {

	function getEmptyObj() {
		return {
			manifacturerDetail : {
				line1 : "",
				line2 : "",
				city : "",
				state : "",
				country : "",
				pin : "",
				phone1 : "",
				phone2 : ""
			},
			machineName : "",
			manifacturerName : '',
			manifacturerRefNumber : "",
			createdDate : new Date(),
			createdBy : $scope.curUser,
			business : $scope.curUser.business,
			status : 'DRAFT'
		}
	}

	$scope.machine = $stateParams.machineObj ? $stateParams.machineObj
			: getEmptyObj();

	$scope.addMachine = function() {
		$scope.machine.business = $scope.curUser.business;
		$scope.machine.modifiedBy = $scope.curUser.email_id;

		var productService = appEndpointSF.getProductionService();
		productService.addMachine($scope.machine).then(function(machineObj) {
			if (machineObj.id != undefined) {
				$scope.machine = machineObj;
				$scope.showAddToast();
			}
		});

	}
});