var app = angular.module("stockApp");

app.controller("warehouseAddCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF) {

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$log.debug("$stateParams.selectedWarehouseId:",
			$stateParams.selectedWarehouse);

	$scope.selectedWarehouse = $stateParams.selectedWarehouse;

	function getEmptyWarehourse() {
		return {
			warehouseName : "",
			description : "",
			address : {},
			createdDate : new Date(),
			modifiedDate : new Date(),
			modifiedBy : $scope.curUser.email_id,
			business : $scope.curUser.business
		};
	}

	$scope.warehouse = $scope.selectedWarehouse ? $scope.selectedWarehouse
			: getEmptyWarehourse();
	
	$scope.addWarehouse = function() {

		var WarehouseManagementService = appEndpointSF
				.getWarehouseManagementService();

		$scope.warehouse.modifiedBy = $scope.curUser.email_id;

		WarehouseManagementService.addWarehouse($scope.warehouse).then(
				function(msgBean) {
					if ($scope.selectedWarehouse) {
						$scope.showUpdateToast();
					} else {
						$scope.showAddToast();
						$scope.warehouse = getEmptyWarehourse();
						$scope.warehouseForm.$setPristine();
						$scope.warehouseForm.$setValidity();
						$scope.warehouseForm.$setUntouched();
						
					}
				});

	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {

		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}

	$scope.waitForServiceLoad();

	$scope.toggleRight = buildToggler('right');

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

	$scope.showSimpleToast = function() {
		$mdToast.show($mdToast.simple().content('Customer Data Saved!')
				.position("top").hideDelay(3000));
	}
});
