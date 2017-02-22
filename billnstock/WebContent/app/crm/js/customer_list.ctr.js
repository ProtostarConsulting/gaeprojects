var app = angular.module("stockApp");
app.controller("customerListCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF) {

	$log.debug("Inside customerListCtr");
	$scope.loading = true;
	$scope.query = {
		order : 'firstName',
		limit : 50,
		page : 1
	};
	$scope.selected = [];

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.getAllCustomersByBusiness = function() {
		$scope.loading = true;
		var customerService = appEndpointSF.getCustomerService();

		customerService.getAllCustomersByBusiness($scope.curUser.business.id)
				.then(function(custList) {
					$scope.customers = custList.items;
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
	$scope.waitForServiceLoad();
});
