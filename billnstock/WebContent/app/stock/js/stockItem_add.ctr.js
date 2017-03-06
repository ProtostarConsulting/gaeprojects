angular
		.module("stockApp")
		.controller(
				"stockAddCtr",
				function($scope, $window, $mdToast, $q, $timeout, $mdSidenav,
						$mdUtil, $log, $http, $stateParams, $mdMedia,
						$mdDialog, objectFactory, appEndpointSF) {

					$log.debug("Inside customerCtr");

					$log.debug("$stateParams.selectedStockItem:",
							$stateParams.selectedStockItem);
					$scope.selectedStockItem = $stateParams.selectedStockItem;

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					function getEmptyStockItem() {
						return {
							id : "",
							warehouse : null,
							stockItemType: null,
							qty : "",
							price : "",
							cost:0,
							movingAvgCost:0,
							thresholdValue : '',
							notes : '',
							createdDate : new Date(),
							modifiedDate : new Date(),
							modifiedBy : $scope.curUser.email_id,
							business : $scope.curUser.business
						};
					}
					$scope.stock = getEmptyStockItem();

					$scope.stock = $scope.selectedStockItem ? $scope.selectedStockItem
							: $scope.stock;
					
					$scope.addStock = function() {
						var stockService = appEndpointSF.getStockService();
						if(!$scope.stock.movingAvgCost){
							$scope.stock.movingAvgCost = $scope.stock.cost;
						}
						$scope.stock.modifiedBy = $scope.curUser.email_id;
						stockService.addStockItem($scope.stock).then(
								function(msgBean) {
									if ($scope.selectedStockItem) {
										$scope.showUpdateToast();										
									} else {
										$scope.showAddToast();
										$scope.stock = getEmptyStockItem();
										$scope.stockForm.$setPristine();
										$scope.stockForm.$setValidity();
										$scope.stockForm.$setUntouched();
									}
								});
					}

					$scope.getAllWarehouseByBusiness = function() {
						var warehouseService = appEndpointSF
								.getWarehouseManagementService();
						warehouseService.getAllWarehouseByBusiness(
								$scope.curUser.business.id).then(
								function(warehouseList) {
									$scope.warehouses = warehouseList;
								});
					}					
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

					function getStockItemTypes() {
						var stockService = appEndpointSF.getStockService();
						stockService.getStockItemTypes($scope.curUser.business.id).then(
								function(list) {
									$scope.stockItemTypeList = list;
								});
					}

					function createFilterFor(query) {
						var lowercaseQuery = angular.lowercase(query);
						return function filterFn(item) {
							return (angular.lowercase(item.itemName)
									.indexOf(lowercaseQuery) >= 0);
						};
					}
					// End Stock Type

					$scope.addWarehouse = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : DialogController,
											templateUrl : '/app/stock/warehouse_add.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curBusi : $scope.curUser.business,
												warehouse : $scope.warehouse,
												curUser : $scope.curUser
											}
										})
								.then(
										function(answer) {
											$scope.status = 'You said the information was "'
													+ answer + '".';
										},
										function() {
											$scope.status = 'You cancelled the dialog.';
										});

					};

					function DialogController($scope, $mdDialog, curBusi,
							curUser, $state, warehouse) {

						$scope.addWarehouse = function() {
							$scope.warehouse.business = curUser.business;
							$scope.warehouse.createdDate = new Date();
							$scope.warehouse.modifiedBy = curUser.email_id;
							var warehouseService = appEndpointSF
									.getWarehouseManagementService();

							warehouseService
									.addWarehouse($scope.warehouse)
									.then(
											function(warehouse) {
												$scope.selectedWarehouse = warehouse.warehouseName;
												$log
														.debug("$scope.selectedWarehouse"
																+ $scope.selectedWarehouse);
												console
														.log("####################");
												$state.reload();
											});
							$scope.hide();
						}
						$scope.hide = function() { 
							$mdDialog.hide();
						};
					}

					// ----------------------UPLODE EXCEL FILE-------------------------------

					$scope.UplodeExcel = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : ExcelController,
											templateUrl : '/app/stock/stock_UploadStockExcel.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curuser : $scope.curUser

											}
										})
								.then(
										function(answer) {
											$scope.status = 'You said the information was "'
													+ answer + '".';
										},
										function() {
											$scope.status = 'You cancelled the dialog.';
										});

					};

					function ExcelController($scope, $mdDialog, curuser) {

						$scope.loding = false;
						$scope.bizID;
						$scope.uplodeimage = function() {
							// $scope.WareHouseID = $scope.warehouseNmae.id;
							document.excelform.action = $scope.ExcelStockUploadURL;
							document.excelform.submit();
							$scope.loding = true;
						}
						$scope.getAllWarehouseByBusiness = function() {
							$log
									.debug("Inside function $scope.getAllWarehouseByBusiness");
							var warehouseService = appEndpointSF
									.getWarehouseManagementService();

							warehouseService
									.getAllWarehouseByBusiness(
											curuser.business.id)
									.then(
											function(warehouseList) {
												$scope.warehouses = warehouseList;
												$scope.bizID = curuser.business.id;
												$log
														.debug("$scope.warehouses:"
																+ angular
																		.toJson($scope.warehouses));
											});

						}

						$scope.getExcelStockUploadURL = function() {
							var uploadUrlService = appEndpointSF
									.getuploadURLService();
							uploadUrlService.getExcelStockUploadURL().then(
									function(url) {
										$scope.ExcelStockUploadURL = url.msg;

									});

						}
						$scope.ExcelStockUploadURL;
						$scope.getExcelStockUploadURL();

					}

					// -------------------------------------------------------
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAllWarehouseByBusiness();
							getStockItemTypes();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

				});