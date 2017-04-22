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
						page : 1,
						totalSize : 0,
						pagesLoaded : 0
					};

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.stockItemList = [];

					$scope.stockItemCategories = [];

					$scope.stockItemFilterWrapper = {
						categoryList : []
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
							$scope.getStockItemTypes();
						}
					}

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

					$scope.toggle = function(item, list) {
						var idList = [];
						angular.forEach(list, function(item) {
							idList.push(item.id);
						});
						var idx = idList.indexOf(item.id);
						if (idx > -1) {
							list.splice(idx, 1);
						} else {
							list.push(item);
						}
						$scope.stockItemFilterWrapper.categoryList = list;
						$scope
								.filterStockItemTypesByCat($scope.stockItemFilterWrapper);
					};

					$scope.filterStockItemTypesByCat = function(
							stockItemFilterWrapper) {

						$scope.stockItemFilterWrapper = stockItemFilterWrapper;

						var stockService = appEndpointSF.getStockService();
						stockService
								.filterStockItemTypesByCategories(
										$scope.stockItemFilterWrapper)
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

					$scope.getStockItemTypeCategoryList = function() {

						var stockService = appEndpointSF.getStockService();

						stockService
								.getStockItemTypeCategories(
										$scope.curUser.business.id)
								.then(
										function(stockItemCategories) {
											$scope.stockItemCategories = stockItemCategories;

										})
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getStockItemTypes();
							$scope.getStockItemTypeCategoryList();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.stockItemList = [];
					$scope.waitForServiceLoad();

				});