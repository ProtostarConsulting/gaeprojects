app = angular.module("stockApp");
app.controller("supplierListCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $state, $http, $stateParams, $routeParams,
		$filter, objectFactory, appEndpointSF) {

	$scope.query = {
		order : 'supplierName',
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
			$scope.getAllSuppliersByBusiness();
		}
	}

	$scope.getAllSuppliersByBusiness = function() {
		$scope.loading = true;
		var supplierService = appEndpointSF.getSupplierService();

		supplierService.getAllSuppliersByBusiness($scope.curUser.business.id)
				.then(function(supplierList) {
					$scope.supplierList = supplierList;
					$scope.loading = false;
				});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getAllSuppliersByBusiness();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}

	$scope.supplierList = [];
	// $scope.temppurchaseOrder;
	$scope.waitForServiceLoad();
});
