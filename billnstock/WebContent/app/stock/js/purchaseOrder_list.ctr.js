app = angular.module("stockApp");
app
		.controller(
				"purchaseOrderListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,$mdColors,
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
					$scope.purchaseOrderList = [];

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.getAllPurchaseOrder = function() {
						$scope.loading = true;
						var stockService = appEndpointSF.getStockService();
						stockService
								.getAllPurchaseOrder($scope.curUser.business.id)
								.then(
										function(purchaseOrderList) {
											$scope.purchaseOrderList = purchaseOrderList;
											query.totalSize = purchaseOrderList.length;
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
								.fetchPOListByPaging(
										$scope.curUser.business.id,
										$scope.selectedStatus, pagingInfoTemp)
								.then(
										function(pagingInfoReturned) {
											$scope.pagingInfoReturned = pagingInfoReturned;
											$scope.purchaseOrderList = $scope.purchaseOrderList
													.concat(pagingInfoReturned.entityList);
											$scope.query.totalSize = pagingInfoReturned.totalEntities;
											$scope.query.pagesLoaded++;
											$scope.loading = false;
										});
					}
					$scope.documentStatusList = [ 'DRAFT', 'SUBMITTED',
							'FINALIZED', 'REJECTED' ];
					$scope.selectedStatus = "";

					$scope.fitlerListByStatus = function(status) {
						$scope.selectedStatus = status;
						$scope.purchaseOrderList = [];
						$scope.query = reSetQuery();
						$scope.pagingInfoReturned = null;
						$scope.fetchEntityListByPaging();
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							// $scope.getAllPurchaseOrder();
							$scope.fetchEntityListByPaging();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					$scope.printPO = function(poId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfPurchaseOrder?bid=" + bid
								+ "&poId=" + poId);
					}

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

					$scope.showSimpleToast = function() {
						$mdToast.show($mdToast.simple().content(
								'Purchase Order Saved!').position("top")
								.hideDelay(3000));
					};

					$scope.back = function() {
						window.history.back();
					}
				});
