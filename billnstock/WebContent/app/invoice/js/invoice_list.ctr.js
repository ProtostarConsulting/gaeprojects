app = angular.module("stockApp");
app.controller("invoiceListCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $state, $http, $stateParams, $routeParams,
		$filter, objectFactory, appEndpointSF) {

	$scope.query = {
		order : '-itemNumber',
		limit : 50,
		page : 1
	};

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.updateInvoiceObj = {

		id : '',
		status : '',
	};
	$scope.selected = [];

	$scope.getAllInvoice = function() {
		$log.debug("Inside Ctr $scope.getAllInvoice");
		$scope.loading = true;
		var invoiceService = appEndpointSF.getInvoiceService();
		invoiceService.getAllInvoice($scope.curUser.business.id).then(
				function(invoiceList) {
					$scope.invoiceData = invoiceList;
					$scope.loading = false;
				});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getAllInvoice();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}

	$scope.invoiceData = [];
	$scope.selected = [];
	$scope.waitForServiceLoad();

	$scope.stutusValues = [ "Paid", "NotPaid" ];

	$scope.updateInvoice = function(invoiceId, status) {
		$scope.sendToUpdate = [];
		$scope.sendToUpdate.push(invoiceId);
		$scope.sendToUpdate.push(status);
		$scope.valueToUpdate = {
			id : $scope.sendToUpdate[0],
			status : $scope.sendToUpdate[1]
		};

		var invoiceService = appEndpointSF.getInvoiceService();
		invoiceService.updateInvoice($scope.valueToUpdate).then(function() {
		});

		$scope.showSimpleToast();
		window.history.back();
	}

	$scope.selected = [];

	$scope.updatePaidStatus = function() {
		var paid = "Paid"
		$scope.selected[0].status = paid;
		var invoiceService = appEndpointSF.getInvoiceService();
		invoiceService.updateInvoiceStatus($scope.selected[0]).then(
				function(msgBean) {
					$scope.showSimpleToast(msgBean.msg);
				});
	}

	$scope.updateNotPaidStatus = function() {
		var notPaid = "NotPaid"
		$scope.selected[0].status = notPaid;
		var invoiceService = appEndpointSF.getInvoiceService();
		invoiceService.updateInvoiceStatus($scope.selected[0]).then(
				function(msgBean) {
					$scope.showSimpleToast(msgBean.msg);
				});
	}

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

	$scope.printInvoice = function(invoiceId) {
		var bid = $scope.curUser.business.id;
		window.open("PrintPdfInvoice?bid=" + bid + "&invoiceId=" + invoiceId);
	}

	$scope.showSimpleToast = function() {
		$mdToast.show($mdToast.simple().content('Invoice Satus Changed!')
				.position("top").hideDelay(3000));
	};

	$scope.back = function() {
		window.history.back();
	}
});
