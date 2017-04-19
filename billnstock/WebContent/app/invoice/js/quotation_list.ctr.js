app = angular.module("stockApp");
app
		.controller(
				"quotationListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $location, $anchorScroll,
						objectFactory, appEndpointSF) {
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
					$scope.documentStatusList = [ 'ALL', 'STARRED', 'DRAFT',
							'SUBMITTED', 'FINALIZED', 'SENT'];
					$scope.selectedStatus = "";
					
					$scope.logOrder = function(order) {
						console.log('order: ', order);
					};

					$scope.logPagination = function(page, limit) {
						console.log('page: ', page);						
						console.log('limit: ', limit);
						$location.hash('tp1');
						$anchorScroll();
						if ($scope.query.page > $scope.query.pagesLoaded) {
							$scope.fetchEntityListByPaging();
						}
					}

					$scope.fitlerListByStatus = function(status) {
						status = (status == 'ALL') ? '' : status;
						$scope.selectedStatus = status;
						$scope.quotationList = [];
						$scope.query = reSetQuery();
						$scope.pagingInfoReturned = null;
						if (status == 'STARRED') {
							$scope.getStarredQuotations();
						} else {
							$scope.fetchEntityListByPaging();
						}
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

					$scope.onpagechange = function() {
						$location.hash('tp1');
						$anchorScroll();
						if ($scope.query.page > $scope.query.pagesLoaded) {
							$scope.fetchEntityListByPaging();
						}
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

					$scope.changeStarredValue = function(quotation) {

						quotation.starred = !quotation.starred;

						var invoiceService = appEndpointSF.getInvoiceService();
						quotation.modifiedBy = $scope.curUser.email_id;
						invoiceService.addQuotation(quotation).then(function() {

						});
					}

					$scope.getStarredQuotations = function() {

						var invoiceService = appEndpointSF.getInvoiceService();

						invoiceService.getStarredQuotations(
								$scope.curUser.business.id).then(
								function(starredQuotations) {
									$scope.quotationList = starredQuotations;
									$scope.loading = false;
								});
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
