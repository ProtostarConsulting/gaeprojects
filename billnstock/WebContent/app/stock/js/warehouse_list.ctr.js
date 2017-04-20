var app = angular.module("stockApp");

app.controller("warehouseListCtr", function($scope, $window, $mdToast,
		$timeout, $mdSidenav, $mdUtil, $log, $stateParams, objectFactory,
		appEndpointSF) {

	$log.debug("Inside warehouseListCtr");

	$scope.query = {
		order : 'warehouseName',
		limit : 50,
		page : 1,
		totalSize : 0,
		pagesLoaded : 0
	};

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.logOrder = function(order) {
		console.log('order: ', order);
	};

	$scope.logPagination = function(page, limit) {
		console.log('page: ', page);
		console.log('limit: ', limit);
		$location.hash('tp1');
		$anchorScroll();
		if ($scope.query.page > $scope.query.pagesLoaded) {
			$scope.getAllWarehouseByBusiness();
		}
	}

	$scope.getAllWarehouseByBusiness = function() {
		var warehouseService = appEndpointSF.getWarehouseManagementService();
		warehouseService.getAllWarehouseByBusiness($scope.curUser.business.id)
				.then(function(warehouseList) {
					$scope.warehouses = warehouseList;
				});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getAllWarehouseByBusiness();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}
	$scope.warehouses = [];
	$scope.waitForServiceLoad();

	$scope.selected = [];

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
});
