angular
		.module("stockApp")
		.controller(
				"stockItemTypeListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $http, $stateParams, objectFactory,
						appEndpointSF) {

					$scope.query = {
						order : '-itemNumber',
						limit : 50,
						page : 1
					};

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.stockItemList = [];

					$scope.getStockItemTypes = function() {
						$scope.loading = true;
						var stockService = appEndpointSF.getStockService();
						stockService
								.getStockItemTypes($scope.curUser.business.id)
								.then(
										function(stockList) {
											$scope.stockItemList = stockList;
											if ($scope.stockItemList
													&& $scope.stockItemList.length > 0) {
												for (var i = 0; i < $scope.stockItemList.length; i++) {
													$scope.stockItemList[i].catNames = "";
													if ($scope.stockItemList[i].categoryList
															&& $scope.stockItemList[i].categoryList.length > 0) {
														for (var j = 0; j < $scope.stockItemList[i].categoryList.length; j++) {
															$scope.stockItemList[i].catNames += $scope.stockItemList[i].categoryList[j].catName;
															if (j != $scope.stockItemList[i].categoryList.length - 1) {
																$scope.stockItemList[i].catNames += ",";

															}
														}
													}
												}
											}
											$scope.loading = false;
										});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getStockItemTypes();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.stockItemList = [];
					$scope.waitForServiceLoad();

				});