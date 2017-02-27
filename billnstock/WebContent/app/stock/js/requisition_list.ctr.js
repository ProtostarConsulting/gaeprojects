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

					$scope.query = {
						order : 'requisition.expectedDate',
						limit : 50,
						page : 1,
						totalSize : 0,
						pagesLoaded : 0
					};

					$scope.documentStatusList = [ 'DRAFT', 'SUBMITTED',
							'FINALIZED', 'REJECTED' ];

					$scope.selectedStatus = "";

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
											$scope.requisitionList = $scope.requisitionList
													.concat(pagingInfoReturned.entityList);
											$scope.query.totalSize = pagingInfoReturned.totalEntities;
											$scope.query.pagesLoaded++;
											$scope.loading = false;
										});
					}

					$scope.fitlerListByStatus = function(status) {
						$scope.selectedStatus = status;
						$scope.requisitionList = [];
						$scope.pagingInfoReturned = null;
						$scope.fetchEntityListByPaging();
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
				});