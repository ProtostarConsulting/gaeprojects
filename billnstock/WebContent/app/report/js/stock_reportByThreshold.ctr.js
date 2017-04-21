angular
		.module("stockApp")
		.controller(
				"stockReportByThresholdCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $http, objectFactory, appEndpointSF) {

					$log.debug("Inside stockReportByThresholdCtr");

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.query = {
						order : '-itemNumber',
						limit : 50,
						page : 1,
						totalSize : 0,
						pagesLoaded : 0
					};

					$scope.logOrder = function(order) {
						console.log('order: ', order);
					};

					$scope.logPagination = function(page, limit) {
						console.log('page: ', page);
						console.log('limit: ', limit);
						$location.hash('tp1');
						$anchorScroll();
						if ($scope.query.page > $scope.query.pagesLoaded) {
							$scope.getReportByThreshold();
						}
					};

					$scope.getReportByThreshold = function() {
						var stockService = appEndpointSF.getStockService();

						stockService
								.getReportByThreshold(
										$scope.curUser.business.id)

								.then(
										function(stockByThresholdList) {

											$scope.stockByThresholdList = stockByThresholdList;
											if ($scope.stockByThresholdList
													&& $scope.stockByThresholdList.length > 0) {
												for (var i = 0; i < $scope.stockByThresholdList.length; i++) {
													$scope.stockByThresholdList[i].stockItemType.catNames = "";
													if ($scope.stockByThresholdList[i].stockItemType.categoryList
															&& $scope.stockByThresholdList[i].stockItemType.categoryList.length > 0) {
														for (var j = 0; j < $scope.stockByThresholdList[i].stockItemType.categoryList.length; j++) {
															$scope.stockByThresholdList[i].stockItemType.catNames += $scope.stockByThresholdList[i].stockItemType.categoryList[j].catName;
															if (j != $scope.stockByThresholdList[i].stockItemType.categoryList.length - 1) {
																$scope.stockByThresholdList[i].stockItemType.catNames += ",";
															}
														}
													}
												}
											}
										})

					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getReportByThreshold();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.thresholdStock = [];
					$scope.waitForServiceLoad();

					// Setup menu
					$scope.toggleRight = buildToggler('right');

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
				});