app = angular.module("stockApp");
app.controller("stockReceiptListCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $state, $http, $stateParams, $routeParams,
		$filter, objectFactory, appEndpointSF) {

	$scope.query = {
		order : '-itemNumber',
		limit : 50,
		page : 1
	};

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.selected = [];

	$scope.getStockReceiptList = function() {
		$log.debug("Inside Ctr $scope.getStockReceiptList");
		var stockService = appEndpointSF.getStockService();

		stockService.getStockReceiptList($scope.curUser.business.id).then(
				function(list) {
					$scope.stockReceiptList = list;
					angular.forEach($scope.stockReceiptList, function(stockReceipt) {
						stockReceipt.receiptNumber = parseInt(
								stockReceipt.receiptNumber);
						stockReceipt.createdDate = new Date(stockReceipt.createdDate);
						stockReceipt.stockReceiptDueDate = new Date(
								stockReceipt.stockReceiptDueDate);
					});
				});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getStockReceiptList();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}

	$scope.stockReceiptList = [];
	$scope.selected = [];
	$scope.waitForServiceLoad();
	

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

	$scope.printstockReceipt = function(stockReceiptId) {
		var stockReceiptEntity = "stockReceiptEntity";
		window.open("PrintPdfstockReceipt?stockReceiptNo=" + stockReceiptId
				+ "&entityname=stockReceiptEntity");

	}

	$scope.showSimpleToast = function() {
		$mdToast.show($mdToast.simple().content('StockReceipt Satus Changed!')
				.position("top").hideDelay(3000));
	};

});
