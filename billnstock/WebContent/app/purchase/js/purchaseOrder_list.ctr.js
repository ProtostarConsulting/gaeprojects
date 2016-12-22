app = angular.module("stockApp");
app
		.controller(
				"purchaseOrderListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $location, $anchorScroll,
						objectFactory, appEndpointSF) {

					$scope.query = {
						order : '-itemNumber',
						limit : 50,
						page : 1,
						totalSize : 0,
						pagesLoaded : 0
					};
					$scope.purchaseOrderList = [];

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.getAllPurchaseOrder = function() {
						var purchaseService = appEndpointSF
								.getPurchaseOrderService();

						purchaseService
								.getAllPurchaseOrder($scope.curUser.business.id)
								.then(
										function(purchaseOrderList) {
											$scope.purchaseOrderList = purchaseOrderList;
											query.totalSize = purchaseOrderList.length;
										});
					}
					$scope.onpagechange = function() {
						$scope.loading = true;
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

						var purchaseService = appEndpointSF
								.getPurchaseOrderService();

						purchaseService
								.fetchEntityListByPaging(
										$scope.curUser.business.id,
										pagingInfoTemp)
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

					$scope.showSimpleToast = function() {
						$mdToast.show($mdToast.simple().content(
								'Purchase Order Saved!').position("top")
								.hideDelay(3000));
					};

					$scope.back = function() {
						window.history.back();
					}
				});
