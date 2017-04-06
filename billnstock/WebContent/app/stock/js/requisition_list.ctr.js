app = angular.module("stockApp");
app
		.controller(
				"requisitionListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $q, $mdMedia, $mdDialog,
						objectFactory, appEndpointSF, $mdColors) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

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
						$scope.requisitionList = [];
						$scope.query = reSetQuery();
						$scope.pagingInfoReturned = null;
						if (status == 'STARRED') {
							$scope.getStarredRequisitionList();
						} else {
							$scope.fetchEntityListByPaging();
						}
					}

					$scope.requisitionList = [];

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
								.fetchRequisitionListByPaging(
										$scope.curUser.business.id,
										$scope.selectedStatus, pagingInfoTemp)
								.then(
										function(pagingInfoReturned) {
											$scope.pagingInfoReturned = pagingInfoReturned;
											if (pagingInfoReturned.entityList) {
												$scope.requisitionList = $scope.requisitionList
														.concat(pagingInfoReturned.entityList);
											}
											$scope.query.totalSize = pagingInfoReturned.totalEntities;
											$scope.query.pagesLoaded++;
											$scope.loading = false;
										});
					}

					$scope.changeStarredValue = function(requisition) {

						requisition.starred = !requisition.starred;

						var stockService = appEndpointSF.getStockService();
						requisition.modifiedBy = $scope.curUser.email_id;
						stockService.addRequisition(requisition).then(
								function() {
								});
					}

					$scope.getStarredRequisitionList = function() {

						var stockService = appEndpointSF.getStockService();
						stockService
								.getStarredRequisitions(
										$scope.curUser.business.id)
								.then(
										function(starredRequisitions) {
											$scope.requisitionList = starredRequisitions;
											$scope.loading = false;
										})

					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchEntityListByPaging();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					$scope.printAsPdf = function(id) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfRequisition?bid=" + bid + "&id="
								+ id);
					}
				});