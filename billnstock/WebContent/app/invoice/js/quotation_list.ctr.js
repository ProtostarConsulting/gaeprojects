app = angular.module("stockApp");
app
		.controller(
				"quotationListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, objectFactory, appEndpointSF) {
					function reSetQuery() {
						return {
							order : '-itemNumber',
							limit : 50,
							page : 1,
							totalSize : 0,
							pagesLoaded : 0
						};
					}
					$scope.query = reSetQuery();
					$scope.documentStatusList = [ 'ALL', 'DRAFT', 'SUBMITTED',
							'FINALIZED', 'SENT', 'PAID', 'UNPAID' ];
					$scope.selectedStatus = "";

					$scope.fitlerListByStatus = function(status) {
						status = (status == 'ALL') ? '' : status;
						$scope.selectedStatus = status;
						$scope.quotationList = [];
						$scope.query = reSetQuery();
						$scope.pagingInfoReturned = null;
						$scope.fetchEntityListByPaging();
					}

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.fetchEntityListByPaging = function() {
						$scope.loading = true;
						var pagingInfoTemp = {
							entityList : null,
							startPage : $scope.query.page,
							limit : $scope.query.limit,
							totalEntities : 0,
							webSafeCursorString : $scope.pagingInfoReturned ? $scope.pagingInfoReturned.webSafeCursorString
									: null
						};

						var invoiceService = appEndpointSF.getInvoiceService();
						invoiceService
								.fetchQuotationListByPaging(
										$scope.curUser.business.id,
										$scope.selectedStatus, pagingInfoTemp)
								.then(
										function(pagingInfoReturned) {
											$scope.pagingInfoReturned = pagingInfoReturned;
											if (pagingInfoReturned.entityList) {
												$scope.quotationList = $scope.quotationList
														.concat(pagingInfoReturned.entityList);
											}
											$scope.query.totalSize = pagingInfoReturned.totalEntities;
											$scope.query.pagesLoaded++;
											$scope.loading = false;
										});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							// $scope.getAllQuotationList();
							$scope.fetchEntityListByPaging();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.quotationList = [];
					$scope.waitForServiceLoad();

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

					$scope.printQuotation = function(quotnId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfQuotation?bid=" + bid
								+ "&quotnId=" + quotnId);
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
