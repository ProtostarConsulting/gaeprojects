angular.module("stockApp")
		.controller(
				"ReportByTaxReceivedCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $q, $mdMedia, $mdDialog,
						objectFactory, appEndpointSF, $mdColors) {

					$scope.query = {
						order : 'name',
						limit : 5,
						page : 1
					};

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.printBtnHide = true;

					$scope.taxRcvData = [];

					$scope.taxList = [];

					$scope.taxTotal = 0;

					$scope.getTaxes = function() {

						var taxService = appEndpointSF.getTaxService();

						taxService.getTaxesByVisibility(
								$scope.curUser.business.id).then(
								function(taxList) {
									$scope.taxList = taxList;
								})

					}

					$scope.getDates = function() {
						var date = new Date();
						var firstDay = new Date(date.getFullYear(), date
								.getMonth(), 1);
						var lastDay = new Date(date.getFullYear(), date
								.getMonth() + 1, 0);
						$scope.selectedFromDate = firstDay;
						$scope.selectedToDate = lastDay;
					}

					$scope.getTaxCollReport = function(selectedTax,
							selectedFromDate, selectedToDate) {

						$scope.selectedTax = selectedTax;
						$scope.selectedFromDate = new Date(selectedFromDate);
						$scope.selectedToDate = new Date(selectedToDate);

						var invoiceService = appEndpointSF.getInvoiceService();

						invoiceService.getTaxReport($scope.selectedTax,
								$scope.curUser.business.id,
								$scope.selectedFromDate.getTime(),
								$scope.selectedToDate.getTime()).then(
								function(taxReport) {
									$scope.taxRcvData = taxReport.itemList;
									$scope.taxTotal = taxReport.taxTotal;
									$scope.printBtnHide = false;
								})

					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getTaxes();
							$scope.getDates();
							$scope.loading = false;
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

				});