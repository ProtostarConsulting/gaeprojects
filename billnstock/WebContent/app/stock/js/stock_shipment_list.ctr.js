app = angular.module("stockApp");
app
		.controller(
				"stockShipmentListCtr",
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
						$scope.stockShipmentList = [];
						$scope.query = reSetQuery();
						$scope.pagingInfoReturned = null;
						$scope.fetchEntityListByPaging();
					}

					$scope.getStockShipmentList = function() {
						$log.debug("Inside Ctr $scope.getStockShipmentList");
						$scope.loading = true;
						var stockService = appEndpointSF.getStockService();
						stockService
								.fetchShipmentListByPaging(
										$scope.curUser.business.id)
								.then(
										function(list) {
											$scope.stockShipmentList = list;
											angular
													.forEach(
															$scope.stockShipmentList,
															function(
																	stockShipment) {
																stockShipment.shipmentNumber = parseInt(stockShipment.shipmentNumber);
																stockShipment.createdDate = new Date(
																		stockShipment.createdDate);
																stockShipment.stockShipmentDueDate = new Date(
																		stockShipment.stockShipmentDueDate);
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
											$scope.stockShipmentList = $scope.stockShipmentList
													.concat(pagingInfoReturned.entityList);
											$scope.query.totalSize = pagingInfoReturned.totalEntities;
											$scope.query.pagesLoaded++;
											$scope.loading = false;
										});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							//$scope.getStockShipmentList();
							$scope.fetchEntityListByPaging();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.stockShipmentList = [];
					$scope.selected = [];
					$scope.waitForServiceLoad();

					$scope.printstockShipment = function(stockShipmentId) {
						var stockShipmentEntity = "stockShipmentEntity";
						window.open("PrintPdfstockShipment?stockShipmentNo="
								+ stockShipmentId
								+ "&entityname=stockShipmentEntity");

					}
				});
