app = angular.module("stockApp");
app.controller("stockShipmentListCtr", function($scope, $window, $mdToast,
		$timeout, $mdSidenav, $mdUtil, $log, $state, $http, $stateParams,
		$routeParams, $filter, objectFactory, appEndpointSF) {

	$scope.query = {
		order : '-itemNumber',
		limit : 50,
		page : 1
	};

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.selected = [];

	$scope.getStockShipmentList = function() {
		$log.debug("Inside Ctr $scope.getStockShipmentList");
		$scope.loading = true;
		var stockService = appEndpointSF.getStockService();
		stockService.getStockShipmentList($scope.curUser.business.id).then(
				function(list) {
					$scope.stockShipmentList = list;
					angular.forEach($scope.stockShipmentList, function(
							stockShipment) {
						stockShipment.shipmentNumber = parseInt(
								stockShipment.shipmentNumber);
						stockShipment.createdDate = new Date(
								stockShipment.createdDate);
						stockShipment.stockShipmentDueDate = new Date(
								stockShipment.stockShipmentDueDate);
					});
					$scope.loading = false;
				});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getStockShipmentList();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}

	$scope.stockShipmentList = [];
	$scope.selected = [];
	$scope.waitForServiceLoad();

	$scope.printstockShipment = function(stockShipmentId) {
		var stockShipmentEntity = "stockShipmentEntity";
		window.open("PrintPdfstockShipment?stockShipmentNo=" + stockShipmentId
				+ "&entityname=stockShipmentEntity");

	}
});
