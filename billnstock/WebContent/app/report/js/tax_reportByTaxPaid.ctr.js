angular
		.module("stockApp")
		.controller(
				"ReportByTaxPaidCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $http, objectFactory, appEndpointSF) {

					$scope.getReportByTaxPaid = function() {
						var stockService = appEndpointSF.getStockService();

						stockService.getAllPurchaseOrder(
								$scope.curUser.business.id).then(
								function(purchaseOrderList) {
									$scope.pOFortaxPaid = purchaseOrderList;
								});
					}

					$scope.taxPaid = [];
					$scope.showDetails = function(fromDate, toDate) {

						$scope.productTaxTotalBtnRange = 0.0;

						for (var i = 0; i <= $scope.pOFortaxPaid.length; i++) {

							$scope.pOFortaxPaid[i].poDate = new Date(
									$scope.pOFortaxPaid[i].poDate);

							if ($scope.pOFortaxPaid[i].poDate >= fromDate
									&& $scope.pOFortaxPaid[i].poDate <= toDate) {
								$scope.taxPaid.push($scope.pOFortaxPaid[i]);

								$scope.productTaxTotalBtnRange += $scope.pOFortaxPaid[i].finalTotal;

							}
						}
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getReportByTaxPaid();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					// Setup menu
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

					$scope.query = {
						order : 'name',
						limit : 5,
						page : 1
					};
				});