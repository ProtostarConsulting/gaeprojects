var app = angular.module("stockApp");

app.controller("customerViewCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF) {

	$log.debug("Inside customerViewCtr");

	$scope.showSimpleToast = function() {
		$mdToast.show($mdToast.simple().content('Customer Data Saved!')
				.position("top").hideDelay(3000));
	};

	$log.debug("$stateParams:", $stateParams);
	$log.debug("$stateParams.selectedCustomerId:",
			$stateParams.selectedCustomerId);

	$scope.customerId = $stateParams.selectedCustomerId;

	$scope.getAllInvoiceByCustId = function() {
		$log.debug("Inside Ctr $scope.getAllInvoiceByCustId");
		var invoiceService = appEndpointSF.getInvoiceService();

		invoiceService.getAllInvoiceByCustId($scope.customerId).then(
				function(custInvoiveList) {
					$log.debug("Inside Ctr getAllInvoiceByCustId");
					$scope.custInvoiceData = custInvoiveList;
					$log.debug("Inside Ctr $scope.custInvoiceData:"
							+ angular.toJson($scope.custInvoiceData));
				});
	}

	$scope.custInvoiceData = [];
	$scope.getAllInvoiceByCustId();

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
