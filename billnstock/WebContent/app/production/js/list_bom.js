app = angular.module("stockApp");
app.controller("list_bom", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $state, $http, $stateParams, $routeParams,
		$filter, $q, $mdMedia, $mdDialog, objectFactory, appEndpointSF,
		$mdColors) {

	$scope.loading = true;
	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();
	$scope.query = {
		order : 'firstName',
		limit : $scope.dataTableOptions.limit,
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

	$scope.fetchProductionList = function() {
		var productService = appEndpointSF.getProductionService();
		$scope.loading = true;
		productService.getlistBomEntity($scope.curUser.business.id).then(
				function(list) {
					$scope.bomList = list;
					$scope.loading = false;
				});
	}
	$scope.printBOM = function(bomId) {
		var bid = $scope.curUser.business.id;
		window.open("PrintPdfBOM?bid=" + bid + "&bomId=" + bomId);
	}
	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.fetchProductionList();

		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}
	$scope.waitForServiceLoad();
});