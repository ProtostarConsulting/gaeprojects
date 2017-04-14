angular
		.module("stockApp")
		.controller(
				"stockItemTypeAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $http, $stateParams, $mdMedia,
						$mdDialog, objectFactory, appEndpointSF) {

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
					};

					$scope.exists = function(item, list) {
						var idList = [];
						angular.forEach(list, function(item) {
							idList.push(item.id);
						});
						return idList.indexOf(item.id) > -1;
					};

					$scope.selectedStockItem = $stateParams.selectedStockItem;

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					function getEmptyStockItemType() {
						return {
							itemName : "",
							tags : "",
							productType : null,
							brand : null,
							categoryList : []
						};
					}
					$scope.stock = getEmptyStockItemType();

					$scope.stock = $scope.selectedStockItem ? $scope.selectedStockItem
							: $scope.stock;

					if (!$scope.stock.categoryList)
						$scope.stock.categoryList = [];

					$scope.stockUnitList = [];

					$scope.stockItemCategories = [];

					$scope.addStockItemType = function() {

						$scope.stock.modifiedBy = $scope.curUser.email_id;
						$scope.stock.business = $scope.curUser.business;

						var stockService = appEndpointSF.getStockService();
						stockService.addStockItemType($scope.stock).then(
								function(msgBean) {
									if ($scope.selectedStockItem) {
										$scope.showUpdateToast();
									} else {
										$scope.showAddToast();
										$scope.stock = getEmptyStockItemType();
									}
								});
					}

					$scope.getStockItemUnitsList = function() {

						var stockService = appEndpointSF.getStockService();

						stockService
								.getStockItemUnits($scope.curUser.business.id)
								.then(
										function(stockUnitList) {
											$scope.stockUnitList = stockUnitList;
											for (var i = 0; i < $scope.stockUnitList.length; i++) {
												if ($scope.stock.unitOfMeasure
														&& $scope.stock.unitOfMeasure.id == $scope.stockUnitList[i].id) {
													$scope.stock.unitOfMeasure = $scope.stockUnitList[i];
												}
											}
										})
					}

					$scope.getStockItemCategoryTypeList = function() {

						var stockService = appEndpointSF.getStockService();

						stockService
								.getStockItemTypeCategories(
										$scope.curUser.business.id)
								.then(
										function(stockItemCategories) {
											$scope.stockItemCategories = stockItemCategories;
											if ($scope.stockItemCategories.length > 0
													&& !$scope.stock.cat) {
												$scope.stock.cat = $scope.stockItemCategories[0];
											}

										})
					}

					// ----------------------UPLODE EXCEL
					// FILE-------------------------------

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
							$scope.getStockItemUnitsList();
							$scope.getStockItemCategoryTypeList();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

				});