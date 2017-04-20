var app = angular.module("stockApp");

app.controller("customerSOListCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF) {

	$log.debug("Inside customerListCtr");

	$scope.query = {
		order : 'name',
		limit : 5,
		page : 1
	};
	$scope.selected = [];
	
	
	$scope.logOrder = function(order) {
		console.log('order: ', order);
	};

	$scope.logPagination = function(page, limit) {
		console.log('page: ', page);						
		console.log('limit: ', limit);
		$location.hash('tp1');
		$anchorScroll();
		if ($scope.query.page > $scope.query.pagesLoaded) {
			$scope.getSOListByID();
		}
	}
	$log.debug("$stateParams:", $stateParams);
	$log.debug("$stateParams.selectedCustomerId:",
			$stateParams.selectedCustomerId);

	$scope.customerId = $stateParams.selectedCustomerId;
	
	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();
	$log.debug("$scope.curUser++++++++" + angular.toJson($scope.curUser));

	
	$scope.getSOListByID = function() {
		var salesOrderService = appEndpointSF.getSalesOrderService();

		salesOrderService.getSOListByID($scope.customerId).then(
				function(sOListByID) {
					$scope.sOListByID = sOListByID;
					$scope.CustomerName = $scope.sOListByID[0].customer.firstName;
					$log.debug("$scope.getSOByID:"
							+ angular.toJson($scope.sOListByID));
				});
	}
	
	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			if ($scope.customerId != undefined) {
				$scope.getSOListByID();
			}
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}

	$scope.sOListByID = [];
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

	

	$scope.back = function() {
		window.history.back();
	}


});
