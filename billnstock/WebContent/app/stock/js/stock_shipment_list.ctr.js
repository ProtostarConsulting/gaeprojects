app = angular.module("stockApp");
app.controller("stockShipmentListCtr", function($scope, $window, $mdToast,
		$timeout, $mdSidenav, $mdUtil, $log, $state, $http, $stateParams,
		$routeParams, $filter, objectFactory, appEndpointSF) {

	$scope.query = {
		order : '-shipmentNumber',
		limit : 50,
		page : 1
	};

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.selected = [];

	$scope.getStockShipmentList = function() {
		$log.debug("Inside Ctr $scope.getStockShipmentList");
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

	/* Setup menu */
	$scope.toggleRight = buildToggler('right');
	/**
	 * Build handler to open/close a SideNav; when animation finishes report
	 * completion in console
	 */
	function buildToggler(navID) {
		var debounceFn = $mdUtil.debounce(function() {
			$mdSidenav(navID).toggle().then(function() {
				$log.debug("toggle " + navID + " is done");
			});
		}, 200);
		return debounceFn;
	}

	$scope.close = function() {
		$mdSidenav('right').close().then(function() {
			$log.debug("close RIGHT is done");
		});
	};

	$scope.printstockShipment = function(stockShipmentId) {
		var stockShipmentEntity = "stockShipmentEntity";
		window.open("PrintPdfstockShipment?stockShipmentNo=" + stockShipmentId
				+ "&entityname=stockShipmentEntity");

	}

	$scope.showSimpleToast = function() {
		$mdToast.show($mdToast.simple().content('StockShipment Satus Changed!')
				.position("top").hideDelay(3000));
	};

});
