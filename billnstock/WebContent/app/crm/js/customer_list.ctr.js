var app = angular.module("stockApp");

app.controller("customerListCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF) {

	$log.debug("Inside customerListCtr");
	$scope.loading = true;
	$scope.query = {
		order : 'name',
		limit : 5,
		page : 1
	};
	$scope.selected = [];
		
	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();
	$log.debug("$scope.curUser++++++++" + angular.toJson($scope.curUser));

	$scope.getAllCustomersByBusiness = function() {
		$scope.loading = true;
		var customerService = appEndpointSF.getCustomerService();

		customerService.getAllCustomersByBusiness(
				$scope.curUser.business.id).then(
				function(custList) {
					$log.debug("Inside Ctr getAllCustomers");
					$scope.customers = custList.items;					
					$log.debug("Inside Ctr $scope.customers:"
							+ angular.toJson($scope.customers));
					$scope.loading = false;
				});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getAllCustomersByBusiness();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}
	

	$scope.customers = [];
	$scope.selected = [];	
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
