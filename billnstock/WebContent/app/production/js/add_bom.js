var app = angular.module("stockApp");

app
		.controller(
				"add_bom",
				function($scope, $window, $mdToast, $q, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia) {
					
					$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();
					
					$scope.getEmptyBomObjadd = function() {
						return {
							productName : "",
							catList : [],
							business : $scope.curUser.business,
							createdBy : $scope.curUser,
							modifiedBy : null
						}
					};

					$scope.dummyCatList = [];
					$scope.stockItemCategories = [];

					$scope.documentEntity = $stateParams.bomCategory ? $stateParams.bomCategory
							: $scope.getEmptyBomObjadd();

					$scope.addCatogory = function() {
						var category = {
							cat : null,
							items : [],
							categoryStockTypeList : []
						};

						if (!$scope.documentEntity.catList) {
							$scope.documentEntity.catList = [];
						}
						$scope.documentEntity.catList.push(category);
						$scope.addLineItem(category);
					}

					$scope.addLineItem = function(category) {
						// $scope.getStockItemTypes (category)

						var itemObj = {
							itemName : "",
							price : 0,
							qty : "",
							currentBudgetBalance : 0
						};

						if (!category.items) {
							category.items = [];
						}
						category.items.push(itemObj);
					};

					$scope.submitBom = function() {

						var productService = appEndpointSF
								.getProductionService();
						productService.addBomEntity($scope.documentEntity)
								.then(function(bom) {
									if (bom.id)
										$scope.documentEntity.id = bom.id;

									$scope.showAddToast();
								});

					}
					$scope.fetchCatogoryList = function() {
						var stockService = appEndpointSF.getStockService();

						stockService.getStockItemTypeCategories(
								$scope.curUser.business.id).then(
								function(list) {
									$scope.stockItemCategories = list;
								})
					};

					$scope.getStockItemTypes = function(category, index) {
						$log.debug("Inside Ctr $scope.getStockItemTypes");
						var stockService = appEndpointSF.getStockService();

						stockService.filterStockItemTypesByCategory(
								category.cat).then(function(list) {
							category.categoryStockTypeList = list;
						});
					}

					$scope.removeCategoryItem = function(index) {
						$scope.documentEntity.catList.splice(index, 1);
					};
					$scope.removeLineItem = function(category, index) {
						category.items.splice(index, 1);
						/* $scope.calItemSubTotal(); */
					};

					// Select Stock Type
					$scope.stockItemTypeList = [];
					$scope.searchTextInput = null;

					$scope.querySearch = function(query) {
						var results = query ? $scope.stockItemTypeList
								.filter(createFilterFor(query)) : [];
						var deferred = $q.defer();
						$timeout(function() {
							deferred.resolve(results);
						}, Math.random() * 1000, false);
						return deferred.promise;
					}

					function createFilterFor(query) {
						var lowercaseQuery = angular.lowercase(query);
						return function filterFn(item) {
							return (angular.lowercase(item.itemName).indexOf(
									lowercaseQuery) >= 0);
						};
					}
					function getStockItemTypes() {
						var stockService = appEndpointSF.getStockService();
						stockService.getStockItemTypes(
								$scope.curUser.business.id).then(
								function(list) {
									$scope.stockItemTypeList = list;
								});
					}
					// End Stock Type

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							getStockItemTypes();
							$scope.fetchCatogoryList();
							if (!$scope.documentEntity.id) {
								$scope.addCatogory();
							}

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

				});