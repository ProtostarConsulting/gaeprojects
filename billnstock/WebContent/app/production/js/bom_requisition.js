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
					
					
					
					
					
					$scope.fetchlistStockIssue = function() {
						var productService = appEndpointSF.getProductionService();
						$scope.loading = true;
						productService.listStockIssue($scope.curUser.business.id).then(
								function(list) {
									$scope.listStockIssue = list;
									$scope.loading = false;
								});
					}
					
					if ($stateParams.productionRequisition != null) {
						$scope.productionRequisition.deliveryDate = new Date(
								$stateParams.productionRequisition.deliveryDateTime);
						$scope.productionRequisition.deliverytime = new Date(
								$stateParams.productionRequisition.deliveryDateTime);
					}

					$scope.bomList = [];
					$scope.stockItemCategories = [];
					$scope.stockTypeList = [];
					var dummyStockTypeList = [];
					

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
						

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.print_prod_stock = function(bomId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfBOM?bid=" + bid + "&bomId="
								+ bomId);
					}

					$scope.addProdstock = function(ev, prodStock) {

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
												prodStock : prodStock,
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
							productionRequisition, prodStock, showAddToast) {

						$scope.documentEntity = prodStock ?prodStock: {};

								
								
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
								
								
								
								
						if (!$scope.documentEntity.id) {
							$scope.documentEntity.createdBy = curUser;
							$scope.documentEntity.createdDate = new Date();
							$scope.documentEntity.modifiedDate = new Date();
							$scope.documentEntity.shipmentDate = new Date();
							$scope.documentEntity.status = 'DRAFT';
							$scope.documentEntity.productLineItemList = [];
							$scope.documentEntity.catList=productionRequisition.catList;
							$scope.documentEntity.reqItemNumber =productionRequisition.itemNumber;

						}
						else {

							$scope.documentEntity.deliveryDateTime = new Date(
									prodStock.deliveryDateTime);

						}
						
						
						

						$scope.saveDocument = function() {
							$scope.documentEntity.business = curUser.business;
							$scope.documentEntity.modifiedBy = curUser.email_id;

							var stockService = appEndpointSF.getStockService();

							stockService.addProductionStockIssueList(
									$scope.documentEntity).then(
									function(savedObj) {
										if (!productionRequisition.stockShipmentList){
											productionRequisition.stockShipmentList = [];
											}
										productionRequisition.stockShipmentList.push(savedObj);
										
										$mdDialog.cancel();
										showAddToast();

									});	
							
							var productService = appEndpointSF
							.getProductionService();
							productService
							.addRequisition(
									productionRequisition).then({});
							
						
						
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

						$scope.draftDocumnent = function(ev) {
							$scope.documentEntity.status = 'DRAFT';
							$scope.saveDocument();
						}

						$scope.submitDocumnent = function(ev) {
							$scope.documentEntity.status = 'SUBMITTED';
							$scope.saveDocument();
						}
						$scope.cancel = function() {
							$mdDialog.cancel();
						};
						
						$scope.getAllWarehouseByBusiness();

					}

				});