app = angular.module("stockApp");
app.controller("list_product_requisition", function($scope, $window, $mdToast,
		$timeout, $mdSidenav, $mdUtil, $log, $state, $http, $stateParams,
		$routeParams, $filter, $q, $mdMedia, $mdDialog, objectFactory,
		appEndpointSF, $mdColors) {
	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();
	$scope.query = {
		order : 'firstName',
		limit : 5,
		page : 1,
		totalSize : 0,
		pagesLoaded : 0
	};

	$scope.logOrder = function(order) {
		console.log('order: ', order);
	};

	$scope.logPagination = function(page, limit) {
		console.log('page: ', page);
		console.log('limit: ', limit);
		$location.hash('tp1');
		$anchorScroll();
		if ($scope.query.page > $scope.query.pagesLoaded) {
			$scope.fetchProductionList();
		}
	}

	$scope.fetchBomRequisitionList = function() {
		var productService = appEndpointSF.getProductionService();

		productService.listProductionRequisitionEntity(
				$scope.curUser.business.id).then(function(list) {
			$scope.bomRequiList = list;

		});
	}
	$scope.printProductRequisition = function(proId) {
		var bid = $scope.curUser.business.id;
		window
				.open("PrintPdfProductRequisition?bid=" + bid + "&proId="
						+ proId);
	}
	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.fetchBomRequisitionList();

		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}
	$scope.waitForServiceLoad();
});