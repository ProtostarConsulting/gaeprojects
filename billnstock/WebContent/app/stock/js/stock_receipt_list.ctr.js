app = angular.module("stockApp");
app
		.controller(
				"stockReceiptListCtr",
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

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.selected = [];
					$scope.documentStatusList = [ 'DRAFT', 'SUBMITTED',
							'FINALIZED', 'REJECTED' ];
					$scope.selectedStatus = "";

					$scope.fitlerListByStatus = function(status) {
						$scope.selectedStatus = status;
						$scope.stockReceiptList = [];
						$scope.query = reSetQuery();
						$scope.pagingInfoReturned = null;
						$scope.fetchEntityListByPaging();
					}

					$scope.getStockReceiptList = function() {
						$log.debug("Inside Ctr $scope.getStockReceiptList");
						$scope.loading = true;
						var stockService = appEndpointSF.getStockService();
						stockService
								.getStockReceiptList($scope.curUser.business.id)
								.then(
										function(list) {
											$scope.stockReceiptList = list;
											angular
													.forEach(
															$scope.stockReceiptList,
															function(
																	stockReceipt) {
																stockReceipt.receiptNumber = parseInt(stockReceipt.receiptNumber);
																stockReceipt.createdDate = new Date(
																		stockReceipt.createdDate);
																stockReceipt.stockReceiptDueDate = new Date(
																		stockReceipt.stockReceiptDueDate);
															});
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

						var stockService = appEndpointSF.getStockService();
						stockService
								.fetchReceiptListByPaging(
										$scope.curUser.business.id,
										$scope.selectedStatus, pagingInfoTemp)
								.then(
										function(pagingInfoReturned) {
											$scope.pagingInfoReturned = pagingInfoReturned;
											$scope.stockReceiptList = $scope.stockReceiptList
													.concat(pagingInfoReturned.entityList);
											$scope.query.totalSize = pagingInfoReturned.totalEntities;
											$scope.query.pagesLoaded++;
											$scope.loading = false;
										});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							//$scope.getStockReceiptList();
							$scope.fetchEntityListByPaging();
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

					$scope.printstockReceipt = function(stockReceiptId) {
						var stockReceiptEntity = "stockReceiptEntity";
						window.open("PrintPdfstockReceipt?stockReceiptNo="
								+ stockReceiptId
								+ "&entityname=stockReceiptEntity");

					}

					$scope.showSimpleToast = function() {
						$mdToast.show($mdToast.simple().content(
								'StockReceipt Satus Changed!').position("top")
								.hideDelay(3000));
					};

				});
