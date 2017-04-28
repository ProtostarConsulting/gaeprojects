var app = angular.module("stockApp");

app
		.controller(
				"bom_requisition",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia) {
					
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.productionRequisition = $stateParams.productionRequisition ? $stateParams.productionRequisition
							: null;
				
					$scope.addProdstock=function(ev, ProdStock){
						

						var useFullScreen = $mdMedia('xs');
						$mdDialog
								.show(
										{
											controller : requisition_stock,
											templateUrl : '/app/production/add_prod_stock_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
												ProdStock : ProdStock,
												productionRequisition : $scope.productionRequisition,
												showAddToast : $scope.showAddToast
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

					
						
						
						
					}
					function requisition_stock($scope, $mdDialog, curUser,
							productionRequisition, ProdStock, showAddToast) {
						
						
						$scope.getCategoryBynum=function(){
							
							
							
							
							
							
						}
						
						
						$scope.getAllWarehouseByBusiness = function() {
							$log
									.debug("Inside function $scope.getAllWarehouseByBusiness");
							$scope.loading = true;
							var warehouseService = appEndpointSF
									.getWarehouseManagementService();

							warehouseService.getAllWarehouseByBusiness(
									curUser.business.id).then(
									function(warehouseList) {
										$scope.warehouses = warehouseList;

										$scope.loading = false;
									});
						}
					}
					$scope.addProductLineItem = function() {
						var item = {
							isProduct : true,
							itemName : "",
							qty : 1,
							price : "",
							stockItem : null,
							selectedTaxItem : null
						};
						if (!$scope.documentEntity.productLineItemList) {
							$scope.documentEntity.productLineItemList = [];
						}
						$scope.documentEntity.productLineItemList
								.push(item);
					};
					$scope.draftDocumnent = function(ev) {
						$scope.documentEntity.status = 'DRAFT';
						$scope.saveDocument();
					}

					$scope.submitDocumnent = function(ev) {
						$scope.documentEntity.status = 'SUBMITTED';
						$scope.saveDocument();
					}
					$scope.removeProductItem = function(index) {
						$scope.documentEntity.productLineItemList.splice(
								index, 1);

						if ($scope.documentEntity.productLineItemList.length == 0) {
							$scope.documentEntity.productSubTotal = 0;
							$scope.documentEntity.productTotal = 0;
							$scope.documentEntity.productTaxTotal = 0;
							$scope.documentEntity.selectedProductTax = null;
						}
					if ($stateParams.productionRequisition != null) {
						$scope.productionRequisition.deliveryDate = new Date(
								$stateParams.productionRequisition.deliveryDateTime);
						$scope.productionRequisition.deliverytime = new Date(
							$stateParams.productionRequisition.deliveryDateTime);
				}
					
			};

					$scope.query = {
						order : 'firstName',
						limit : 5,
						page : 1,
						totalSize : 0,
						pagesLoaded : 0
					};

					/*$scope.bomList = [];
					$scope.stockItemCategories = [];
					$scope.stockTypeList = [];
					var dummyStockTypeList = [];
*/
					/*$scope.calculation = function() {
						if ($scope.productionRequisition.productQty != 0
								&& $scope.productionRequisition.productQty != null) {

							for (var i = 0; i < $scope.productionRequisition.bomEntity.catList.length; i++) {
								for (var j = 0; j < $scope.productionRequisition.catList[i].items.length; j++) {

									var qty = parseInt($scope.productionRequisition.bomEntity.catList[i].items[j].qty);
									$scope.productionRequisition.catList[i].items[j].qty = qty
											* $scope.productionRequisition.productQty;
								}
							}
						}
					}*/
/*
					$scope.fetchProductionList = function() {
						var productService = appEndpointSF
								.getProductionService();

						productService.getlistBomEntity(
								$scope.curUser.business.id).then(
								function(list) {
									$scope.bomList = list;
								});
					}*/

					$scope.submitPequisition = function() {

						var productService = appEndpointSF
								.getProductionService();
						$scope.productionRequisition.business = $scope.curUser.business;
						$scope.productionRequisition.createdBy = $scope.curUser;
						$scope.productionRequisition.modifiedBy = null;
						productService.addRequisition(
								$scope.productionRequisition).then(function() {
							$scope.showAddToast();
						});

					}
				/*	$scope.selectItems = function(bomEntity) {

						if (!$scope.productionRequisition.id) {
							var productService = appEndpointSF
									.getProductionService();
							productService
									.getEmptyProductionRequisition(bomEntity)
									.then(
											function(productionRequisitionTemp) {
												$scope.productionRequisition = productionRequisitionTemp;
											});
						}

					}
					*/
					
					
					$scope.printBOM = function(bomId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfBOM?bid=" + bid + "&bomId="
								+ bomId);
					}
					
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
						
							
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

				});