app = angular.module("stockApp");
app
		.controller(
				"stockShipmentListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams, $mdColors,
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
					$scope.documentStatusList = [ 'ALL', 'STARRED', 'DRAFT',
							'SUBMITTED', 'FINALIZED', 'REJECTED' ];
					$scope.selectedStatus = "";

					$scope.fitlerListByStatus = function(status) {
						status = (status == 'ALL') ? '' : status;
						$scope.selectedStatus = status;
						$scope.stockShipmentList = [];
						$scope.query = reSetQuery();
						$scope.pagingInfoReturned = null;
						if (status == 'STARRED') {
							$scope.getStarredStockShipmentsList();
						} else {
							$scope.fetchEntityListByPaging();
						}
					}

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.selected = [];

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
								.fetchShipmentListByPaging(
										$scope.curUser.business.id,
										$scope.selectedStatus, pagingInfoTemp)
								.then(
										function(pagingInfoReturned) {
											$scope.pagingInfoReturned = pagingInfoReturned;
											if (pagingInfoReturned.entityList) {
												$scope.stockShipmentList = $scope.stockShipmentList
														.concat(pagingInfoReturned.entityList);
											}
											$scope.query.totalSize = pagingInfoReturned.totalEntities;
											$scope.query.pagesLoaded++;
											$scope.loading = false;
										});
					}

					$scope.changeStarredValue = function(stockShipment) {

						stockShipment.starred = !stockShipment.starred;

						var stockService = appEndpointSF.getStockService();
						stockShipment.modifiedBy = $scope.curUser.email_id;
						stockService.addStockShipment(stockShipment).then(
								function() {
								});
					}

					$scope.getStarredStockShipmentsList = function() {

						var stockService = appEndpointSF.getStockService();
						stockService
								.getStarredStockShipments()
								.then(
										function(starredStockShipments) {
											$scope.stockShipmentList = starredStockShipments;
											$scope.loading = false;
										})

					}
					$scope.printstockShipment = function(stShipId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfstockShipment?bid=" + bid
								+ "&stShipId=" + stShipId);

					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchEntityListByPaging();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.stockShipmentList = [];
					$scope.selected = [];
					$scope.waitForServiceLoad();

				});
