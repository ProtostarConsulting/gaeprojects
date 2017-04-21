angular.module("stockApp")
		.controller(
				"ReportByTaxReceivedCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $q, $mdMedia, $mdDialog,
						objectFactory, appEndpointSF, $mdColors) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.printBtnHide = true;

					$scope.query = {
						order : 'name',
						limit : 50,
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
							$scope.getTaxCollReport();
						}
					};

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

					$scope.printTaxCollectionReport = function() {

						var bid = $scope.curUser.business.id;
						var taxId = $scope.selectedTax.id;

						window.open("PrintPdfTaxCollectionReport?bid=" + bid
								+ "&fromDate="
								+ $scope.selectedFromDate.getTime()
								+ "&toDate=" + $scope.selectedToDate.getTime()
								+ "&taxId=" + taxId);

					}

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