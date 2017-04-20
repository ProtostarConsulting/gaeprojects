var app = angular.module("stockApp");

app
		.controller(
				"customerInvoiceListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF) {

					$log.debug("Inside customerListCtr");

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
							$scope.getopportunityById();
						}
					}
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
							$scope.getInvoiceListByCustId();
						}
					}

					$log.debug("$stateParams:", $stateParams);
					$log.debug("$stateParams.selectedCustomerId:",
							$stateParams.selectedCustomerId);

					$scope.customerId = $stateParams.selectedCustomerId;

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					$log.debug("$scope.curUser++++++++"
							+ angular.toJson($scope.curUser));

					$scope.getInvoiceListByCustId = function() {
						var invoiceService = appEndpointSF.getInvoiceService();

						invoiceService
								.getInvoiceListByCustId($scope.customerId)
								.then(
										function(invoiceListByID) {
											$scope.invoiceListByID = invoiceListByID;
											$scope.CustomerName = $scope.invoiceListByID[0].customer.firstName;
											$log
													.debug("$scope.invoiceListByID:"
															+ angular
																	.toJson($scope.invoiceListByID));

										})
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							if ($scope.customerId != undefined) {
								$scope.getInvoiceListByCustId();
							}
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.invoiceListByID = [];
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

					$scope.showSimpleToast = function() {
						$mdToast.show($mdToast.simple().content(
								'Customer Data Saved!').position("top")
								.hideDelay(3000));
					};

					$scope.back = function() {
						window.history.back();
					}
				});
