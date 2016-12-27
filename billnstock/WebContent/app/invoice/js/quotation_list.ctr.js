app = angular.module("stockApp");
app
		.controller(
				"quotationListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, objectFactory, appEndpointSF) {
					$scope.query = {
						order : '-itemNumber',
						limit : 50,
						page : 1
					};

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.getAllQuotationList = function() {
						$scope.loading = true;
						var invoiceService = appEndpointSF.getInvoiceService();
						invoiceService.getAllQuotation(
								$scope.curUser.business.id).then(
								function(quotationList) {
									$scope.quotationList = quotationList;
									$scope.loading = false;
								});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAllQuotationList();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.quotationList = [];
					$scope.waitForServiceLoad();

					/* Setup menu */

					$scope.toggleRight = buildToggler('right');
					/**
					 * Build handler to open/close a SideNav; when animation
					 * finishes report completion in console
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

					var printDivCSS = new String(
							'<link href="/lib/base/css/angular-material.min.css"" rel="stylesheet" type="text/css">'
									+ '<link href="/lib/base/css/bootstrap.min.css"" rel="stylesheet" type="text/css">')
					$scope.printDiv = function(divId) {
						// window.frames["print_frame"].document.body.innerHTML
						// = printDivCSS
						// + document.getElementById(divId).innerHTML;
						window.frames["print_frame"].document.body.innerHTML = document
								.getElementById(divId).innerHTML;
						window.frames["print_frame"].window.focus();
						window.frames["print_frame"].window.print();
					}

					/*
					 * $scope.showSimpleToast = function() {
					 * $mdToast.show($mdToast.simple().content( 'Invoice Satus
					 * Changed!').position("top") .hideDelay(3000)); };
					 */
					$scope.back = function() {
						window.history.back();
					}
				});
