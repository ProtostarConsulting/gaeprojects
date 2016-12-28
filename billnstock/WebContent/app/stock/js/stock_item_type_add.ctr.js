angular
		.module("stockApp")
		.controller(
				"stockItemTypeAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $http, $stateParams, $mdMedia,
						$mdDialog, objectFactory, appEndpointSF) {

					$scope.selectedStockItem = $stateParams.selectedStockItem;

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					function getEmptyStockItemType() {
						return {
							id : "",
							itemName : "",
							category : "",						
							createdDate : new Date(),
							modifiedDate : new Date(),
							modifiedBy : $scope.curUser.email_id,
							business : $scope.curUser.business
						};
					}
					$scope.stock = getEmptyStockItemType();

					$scope.stock = $scope.selectedStockItem ? $scope.selectedStockItem
							: $scope.stock;
					
					$scope.addStockItemType = function() {
						var stockService = appEndpointSF.getStockService();
						$scope.stock.modifiedBy = $scope.curUser.email_id;
						stockService.addStockItemType($scope.stock).then(
								function(msgBean) {
									if ($scope.selectedStockItem) {
										$scope.showUpdateToast();										
									} else {
										$scope.showAddToast();
										$scope.stock = getEmptyStockItemType();
										$scope.stockForm.$setPristine();
										$scope.stockForm.$setValidity();
										$scope.stockForm.$setUntouched();
									}
								});
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
							
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

				});