app = angular.module("stockApp");
app
		.controller(
				"stockSettingsCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $mdMedia, $mdDialog, $q,
						objectFactory, appEndpointSF) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.settingsObj = {}

					$scope.stockItemUnit = {
						unitName : ""
					}

					$scope.stockItemTypeCategory = {
						catName : ""
					}

					$scope.stockItemOrderType = {
						typeName : ""
					}

					$scope.stockItemBrand = {
						brandName : ""
					}

					$scope.stockItemProductType = {
						typeName : ""
					}
					$scope.stockItemUnitList = [];

					$scope.stockItemCategories = [];

					$scope.stockItemOrderTypes = [];

					$scope.stockItemBrands = [];

					$scope.stockItemProductTypes = [];

					$scope.editMode = false;

					$scope.addSettings = function() {

						var StockService = appEndpointSF.getStockService();
						$scope.settingsObj.business = $scope.curUser.business;
						$scope.settingsObj.modifiedBy = $scope.curUser.email_id;

						StockService.addStockSettings($scope.settingsObj).then(
								function(savedRecoed) {
									$scope.settingsObj = savedRecoed;
									$scope.showUpdateToast();
								});
					}

					$scope.getStockSettingsByBiz = function() {

						var invoiceService = appEndpointSF.getStockService();

						invoiceService.getStockSettingsByBiz(
								$scope.curUser.business.id).then(
								function(settingsList) {

									$scope.settingsObj = settingsList;
									$log.debug("Inside Ctr $scope.settingsObj:"
											+ $scope.settingsObj);
									return $scope.settingsObj;
								});
					}

					$scope.addStockItemUnit = function() {

						if (!$scope.stockItemUnit.unitName) {
							return;
						}

						$scope.stockItemUnit.business = $scope.curUser.business;
						$scope.stockItemUnit.modifiedBy = $scope.curUser.email_id;
						var stockService = appEndpointSF.getStockService();

						stockService.addStockItemUnit($scope.stockItemUnit)
								.then(function() {
									$scope.stockItemUnit = {};
									$scope.getStockItemUnitsList();
								})

					}

					$scope.editStockItemUnit = function(ev, stockItemUnit) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : editStockItemUnitDialogCtr,
											templateUrl : '/app/stock/stock_item_unit_edit_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curBusi : $scope.curUser.business,
												curUser : $scope.curUser,
												curStockItemUnit : stockItemUnit,
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

					function editStockItemUnitDialogCtr($scope, $mdDialog,
							curBusi, curUser, curStockItemUnit) {

						$scope.stockItemUnit = angular.copy(curStockItemUnit);
						$scope.stockItemUnit.modifiedBy = curUser.email_id;

						$scope.addStockItemUnit = function() {

							var stockService = appEndpointSF.getStockService();

							stockService
									.addStockItemUnit($scope.stockItemUnit)
									.then(
											function() {
												curStockItemUnit.unitName = $scope.stockItemUnit.unitName;
												curStockItemUnit.note = $scope.stockItemUnit.note;
											})

							$scope.cancel();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};
					}

					$scope.getStockItemUnitsList = function() {

						var stockService = appEndpointSF.getStockService();

						stockService
								.getStockItemUnits($scope.curUser.business.id)
								.then(
										function(stockItemUnitList) {
											$scope.stockItemUnitList = stockItemUnitList;
										})
					}

					$scope.addStockItemTypeCategory = function() {

						if (!$scope.stockItemTypeCategory.catName) {
							return;
						}

						$scope.stockItemTypeCategory.business = $scope.curUser.business;
						$scope.stockItemTypeCategory.modifiedBy = $scope.curUser.email_id;

						var stockService = appEndpointSF.getStockService();

						stockService.addStockItemTypeCategory(
								$scope.stockItemTypeCategory).then(function() {
							$scope.stockItemTypeCategory = {};
							$scope.getStockItemCategoryTypeList();
						})

					}

					$scope.editStockItemCategory = function(ev,
							stockItemTypeCategory) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : editStockItemTypeCategoryDialogCtr,
											templateUrl : '/app/stock/stock_item_type_category_edit_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curBusi : $scope.curUser.business,
												curUser : $scope.curUser,
												curStockItemTypeCategory : stockItemTypeCategory,
												stockItemCategories : $scope.stockItemCategories
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

					function editStockItemTypeCategoryDialogCtr($scope,
							$mdDialog, curBusi, curUser,
							curStockItemTypeCategory, stockItemCategories) {

						$scope.stockItemTypeCategory = angular
								.copy(curStockItemTypeCategory);
						$scope.stockItemCategories = stockItemCategories;
						$scope.stockItemTypeCategory.modifiedBy = curUser.email_id;

						$scope.addStockItemTypeCategory = function() {

							var stockService = appEndpointSF.getStockService();

							stockService
									.addStockItemTypeCategory(
											$scope.stockItemTypeCategory)
									.then(
											function() {
												curStockItemTypeCategory.catName = $scope.stockItemTypeCategory.catName;
												curStockItemTypeCategory.note = $scope.stockItemTypeCategory.note;
												curStockItemTypeCategory.parent = $scope.stockItemTypeCategory.parent;
											})

							$scope.cancel();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};
					}

					$scope.getStockItemCategoryTypeList = function() {

						var stockService = appEndpointSF.getStockService();

						stockService
								.getStockItemTypeCategories(
										$scope.curUser.business.id)
								.then(
										function(stockItemCategories) {
											$scope.stockItemCategories = stockItemCategories;
										})
					}

					$scope.addStockItemOrderType = function() {

						if (!$scope.stockItemOrderType.typeName) {
							return;
						}

						$scope.stockItemOrderType.business = $scope.curUser.business;
						$scope.stockItemOrderType.modifiedBy = $scope.curUser.email_id;
						var stockService = appEndpointSF.getStockService();

						stockService.addStockItemOrderType(
								$scope.stockItemOrderType).then(function() {
							$scope.stockItemOrderType = {};
							$scope.getStockItemOrderTypeList();
						})

					}

					$scope.editStockItemOrderType = function(ev,
							stockItemOrderType) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : editStockItemOrderTypeDialogCtr,
											templateUrl : '/app/stock/stock_item_order_type_edit_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curBusi : $scope.curUser.business,
												curUser : $scope.curUser,
												curStockItemOrderType : stockItemOrderType,
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

					function editStockItemOrderTypeDialogCtr($scope, $mdDialog,
							curBusi, curUser, curStockItemOrderType) {

						$scope.stockItemOrderType = angular
								.copy(curStockItemOrderType);
						$scope.stockItemOrderType.modifiedBy = curUser.email_id;

						$scope.addStockItemOrderType = function() {

							var stockService = appEndpointSF.getStockService();

							stockService
									.addStockItemOrderType(
											$scope.stockItemOrderType)
									.then(
											function() {
												curStockItemOrderType.typeName = $scope.stockItemOrderType.typeName;
												curStockItemOrderType.note = $scope.stockItemOrderType.note;
											})

							$scope.cancel();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};
					}

					$scope.getStockItemOrderTypeList = function() {

						var stockService = appEndpointSF.getStockService();

						stockService
								.getStockItemOrderTypes(
										$scope.curUser.business.id)
								.then(
										function(stockItemOrderTypes) {
											$scope.stockItemOrderTypes = stockItemOrderTypes;
										})
					}

					$scope.addStockItemBrand = function() {

						if (!$scope.stockItemBrand.brandName) {
							return;
						}

						$scope.stockItemBrand.business = $scope.curUser.business;
						$scope.stockItemBrand.modifiedBy = $scope.curUser.email_id;
						var stockService = appEndpointSF.getStockService();

						stockService.addStockItemProductBrand(
								$scope.stockItemBrand).then(function() {
							$scope.stockItemBrand = {};
							$scope.getStockItemBrands();
						})

					}

					$scope.editStockItemBrand = function(ev, stockItemBrand) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : editStockItemBrandDialogCtr,
											templateUrl : '/app/stock/stock_item_brand_edit_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curBusi : $scope.curUser.business,
												curUser : $scope.curUser,
												curStockItemBrand : stockItemBrand,
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

					function editStockItemBrandDialogCtr($scope, $mdDialog,
							curBusi, curUser, curStockItemBrand) {

						$scope.stockItemBrand = angular.copy(curStockItemBrand);
						$scope.stockItemBrand.modifiedBy = curUser.email_id;

						$scope.addStockItemBrand = function() {

							var stockService = appEndpointSF.getStockService();

							stockService
									.addStockItemProductBrand(
											$scope.stockItemBrand)
									.then(
											function() {
												curStockItemBrand.brandName = $scope.stockItemBrand.brandName;
												curStockItemBrand.note = $scope.stockItemBrand.note;
											})

							$scope.cancel();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};
					}

					$scope.getStockItemBrands = function() {

						var stockService = appEndpointSF.getStockService();

						stockService.getStockItemBrands(
								$scope.curUser.business.id).then(
								function(stockItemBrands) {
									$scope.stockItemBrands = stockItemBrands;
								})
					}

					$scope.addStockItemProductType = function() {

						if (!$scope.stockItemProductType.typeName) {
							return;
						}

						$scope.stockItemProductType.business = $scope.curUser.business;
						$scope.stockItemProductType.modifiedBy = $scope.curUser.email_id;
						var stockService = appEndpointSF.getStockService();

						stockService.addStockItemProductType(
								$scope.stockItemProductType).then(function() {
							$scope.stockItemProductType = {};
							$scope.getStockItemProductTypes();
						})

					}

					$scope.editStockItemProductType = function(ev,
							stockItemProductType) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : editStockItemProductTypeDialogCtr,
											templateUrl : '/app/stock/stock_item_product_type_edit_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curBusi : $scope.curUser.business,
												curUser : $scope.curUser,
												curStockItemProductType : stockItemProductType,
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

					function editStockItemProductTypeDialogCtr($scope,
							$mdDialog, curBusi, curUser,
							curStockItemProductType) {

						$scope.stockItemProductType = angular
								.copy(curStockItemProductType);
						$scope.stockItemProductType.modifiedBy = curUser.email_id;

						$scope.addStockItemProductType = function() {

							var stockService = appEndpointSF.getStockService();

							stockService
									.addStockItemProductType(
											$scope.stockItemProductType)
									.then(
											function() {
												curStockItemProductType.typeName = $scope.stockItemProductType.typeName;
												curStockItemProductType.note = $scope.stockItemProductType.note;
											})

							$scope.cancel();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};
					}

					$scope.getStockItemProductTypes = function() {

						var stockService = appEndpointSF.getStockService();

						stockService
								.getStockItemProductTypes(
										$scope.curUser.business.id)
								.then(
										function(stockItemProductTypes) {
											$scope.stockItemProductTypes = stockItemProductTypes;
										})
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getStockSettingsByBiz();
							$scope.getStockItemUnitsList();
							$scope.getStockItemCategoryTypeList();
							$scope.getStockItemOrderTypeList();
							$scope.getStockItemBrands();
							$scope.getStockItemProductTypes();
						} else {
							$log.debug("Services Not Loaded, waiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

				});
