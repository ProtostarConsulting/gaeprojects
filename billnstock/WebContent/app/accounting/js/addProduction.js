var app = angular.module("stockApp");

app.controller("addProduction", function($scope, $window, $mdToast,
		$timeout, $mdSidenav, $mdUtil, $log, $stateParams, objectFactory,
		appEndpointSF, $mdDialog, $mdMedia) {
	
	
	$scope.addBom={
			productName:"",
			business : $scope.curUser.business
			}
	$scope.submitProduction=function(){
		
		var productService = appEndpointSF
		.getProductionService();
		
		productService.addProduction($scope.addBom)
		.then(function() {});
		
	}
	
});