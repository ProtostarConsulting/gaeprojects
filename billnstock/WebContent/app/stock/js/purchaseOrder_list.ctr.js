app = angular.module("stockApp");
app
		.controller(
				"purchaseOrderListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams, $mdColors,
						$routeParams, $filter, $location, $anchorScroll,
						objectFactory, appEndpointSF) {

					$scope.purchaseOrderList = [];

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
							'SUBMITTED', 'FINALIZED', 'REJECTED', 'CLOSED' ];
					$scope.selectedStatus = "";

					$scope.purchaseOrderList = [];

					$scope.fitlerListByStatus = function(status) {
						status = (status == 'ALL') ? '' : status;
						$scope.selectedStatus = status;
						$scope.purchaseOrderList = [];
						$scope.query = reSetQuery();
						$scope.pagingInfoReturned = null;
						if (status == 'STARRED') {
							$scope.getStarredPOList();
						} else {
							$scope.fetchEntityListByPaging();
						}
					}

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

					$scope.today = new Date();

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
											if (pagingInfoReturned.entityList) {
												$scope.purchaseOrderList = $scope.purchaseOrderList
														.concat(pagingInfoReturned.entityList);
												for (var i = 0; i < $scope.purchaseOrderList.length; i++) {
													var poDueDate = new Date(
															$scope.purchaseOrderList[i].poDueDate);
													if (poDueDate < $scope.today
															&& $scope.purchaseOrderList[i].status != "CLOSED") {
														$scope.purchaseOrderList[i].highLight = true;
													}
												}

											}
											$scope.query.totalSize = pagingInfoReturned.totalEntities;
											$scope.query.pagesLoaded++;
											$scope.loading = false;
										});
					}

					$scope.changeStarredValue = function(purchaseOrder) {

						purchaseOrder.starred = !purchaseOrder.starred;

						var stockService = appEndpointSF.getStockService();
						purchaseOrder.modifiedBy = $scope.curUser.email_id;
						stockService.addPurchaseOrder(purchaseOrder).then(
								function() {
								});
					}

					$scope.getStarredPOList = function() {

						var stockService = appEndpointSF.getStockService();
						stockService.getStarredPurchaseOrders(
								$scope.curUser.business.id).then(
								function(starredPOs) {
									$scope.purchaseOrderList = starredPOs;
									$scope.loading = false;
								})
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
