var app = angular.module("stockApp");

app
		.controller(
				"addProdPlanCtr",
				function($scope, $window, $mdToast, $q, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia) {

					$scope.loading = true;
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.getEmptyProdPlan = function() {
						return {
							title : '',
							note : '',
							salesOrderNumber : '',
							customer : null,
							prodRequisitionList : [],
							prodShipmentList : [],
							business : $scope.curUser.business,
							createdBy : $scope.curUser,
							modifiedBy : null
						}
					};

					$scope.documentEntity = $stateParams.prodPlan ? $stateParams.prodPlan
							: $scope.getEmptyProdPlan();

					if ($scope.documentEntity.fromDateTime)
						$scope.documentEntity.fromDateTime = new Date(
								$scope.documentEntity.fromDateTime);

					if ($scope.documentEntity.toDateTime)
						$scope.documentEntity.toDateTime = new Date(
								$scope.documentEntity.toDateTime);

					$scope.saveEntity = function() {
						var productService = appEndpointSF
								.getProductionService();
						productService.addProdPlanEntity($scope.documentEntity)
								.then(function(savedObj) {
									if (savedObj.id)
										$scope.documentEntity.id = savedObj.id;

									$scope.showAddToast();
								});

					}

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

					// End Stock Type

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.loading = false;
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.printProductRequisition = function(proId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfProductRequisition?bid=" + bid
								+ "&proId=" + proId);
					}

					$scope.addProdReq = function(ev, productionRequisition) {
						var useFullScreen = $mdMedia('xs');
						$mdDialog
								.show(
										{
											controller : addProdReqCtr,
											templateUrl : '/app/production/add_prod_requisition_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
												productionRequisition : productionRequisition,
												prodPlan : $scope.documentEntity,
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

					};
					$scope.addProdShip = function(ev, productionShipment) {
						var useFullScreen = $mdMedia('xs');
						$mdDialog
								.show(
										{
											controller : addProdShipCtr,
											templateUrl : '/app/production/add_prod_shipment_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
												productionShipment : productionShipment,
												prodPlan : $scope.documentEntity,
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

					};
					function addProdShipCtr($scope, $mdDialog, curUser,
							productionShipment, prodPlan, showAddToast) {
						$scope.warehouses=[];
						$scope.stockItemList=[];
						$scope.documentEntity=productionShipment?productionShipment:{};
						if(!$scope.documentEntity.id){
							
							$scope.documentEntity.createdBy = curUser;
							$scope.documentEntity.createdDate = new Date();
							$scope.documentEntity.modifiedDate = new Date();
							$scope.documentEntity.shipmentDate=new Date();
							$scope.documentEntity.status = 'DRAFT';
							$scope.documentEntity.productLineItemList = [];
							$scope.stockItemList=prodPlan.prodRequisitionList;
							$scope.documentEntity.fromWH=null;
							
							
						}
						
						$scope.getAllWarehouseByBusiness = function() {
							$log
									.debug("Inside function $scope.getAllWarehouseByBusiness");
							$scope.loading = true;
							var warehouseService = appEndpointSF
									.getWarehouseManagementService();

							warehouseService
									.getAllWarehouseByBusiness(
											curUser.business.id)
									.then(
											function(warehouseList) {
												$scope.warehouses = warehouseList;
																							
												$scope.loading = false;
											});
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
							$scope.documentEntity.productLineItemList.push(item);
						};
						$scope.draftDocumnent = function(ev) {
							$scope.documentEntity.status = 'DRAFT';
							$scope.saveDocument();
						}

						$scope.submitDocumnent = function(ev) {
							$scope.documentEntity.status = 'SUBMITTED';
							$scope.saveDocument();
						}

						$scope.finalizeDocumnent = function(ev) {
							var confirm = $mdDialog
									.confirm()
									.title(
											'Do you want to approve/finalize this Document? Note, after this you will not be able to make any changes in this document.')
									.textContent('').ariaLabel('finalize?')
									.targetEvent(ev).ok('Yes').cancel('No');

							$mdDialog.show(confirm).then(function() {
								$log.debug("Inside Yes, function");
								$scope.documentEntity.status = 'FINALIZED';
								$scope.documentEntity.approvedBy = $scope.curUser;
								$scope.saveDocument();
							}, function() {
								$log.debug("Cancelled...");
							});
						}
						$scope.saveDocument = function() {
							$scope.documentEntity.business = curUser.business;
							$scope.documentEntity.modifiedBy =curUser.email_id;
							var stockService = appEndpointSF.getStockService();

							stockService
									.addStockShipment($scope.documentEntity)
									.then(
											function(savedObj) {
												if(savedObj){
												

												var wasUpdate = false;
												angular
														.forEach(
																prodPlan.prodShipmentList,
																function(
																		req) {
																	if (req.id == savedObj.id) {
																		wasUpdate = true;
																	}
																});

												if (wasUpdate) {
													showAddToast();
													$mdDialog.cancel();
												} else {
													if (!prodPlan.prodShipmentList)
														prodPlan.prodShipmentList = [];

													prodPlan.prodShipmentList
															.push(savedObj)

													productService
															.addProdPlanEntity(
																	prodPlan)
															.then(
																	function(
																			savedObj) {
																		if (savedObj.id)
																			showAddToast();

																		$mdDialog
																				.cancel();
																	});
												}
																							
											}
												});

						}
						
						$scope.getAllWarehouseByBusiness();
						
						
					}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////					
					function addProdReqCtr($scope, $mdDialog, curUser,
							productionRequisition, prodPlan, showAddToast) {

						$scope.productionRequisition = productionRequisition ? productionRequisition
								: {};

						if ($scope.productionRequisition.id) {
							$scope.productionRequisition.deliveryDateTime = new Date(
									$scope.productionRequisition.deliveryDateTime);
						} else {
							$scope.productionRequisition.deliveryDateTime = new Date();
						}

						$scope.productionRequisitionBackup = angular
								.copy($scope.productionRequisition);

						$scope.bomList = [];
						$scope.stockItemCategories = [];
						$scope.stockTypeList = [];
						var dummyStockTypeList = [];

						$scope.calculation = function() {
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
						}

						$scope.fetchBomList = function() {
							var productService = appEndpointSF
									.getProductionService();

							productService
									.getlistBomEntity(curUser.business.id)
									.then(function(list) {
										$scope.bomList = list;
									});
						}

						$scope.addRequisition = function() {

							var productService = appEndpointSF
									.getProductionService();
							$scope.productionRequisition.business = curUser.business;
							$scope.productionRequisition.createdBy = curUser;
							$scope.productionRequisition.modifiedBy = curUser.email_id;
							$scope.productionRequisition.prodPlanItemNumber = prodPlan.itemNumber;

							if ($scope.productionRequisitionBackup.id) {
								// if edit changed the value it should edit
								// existing record, not create new one
								$scope.productionRequisition.id = $scope.productionRequisitionBackup.id;

							}
							productService
									.addRequisition(
											$scope.productionRequisition)
									.then(
											function(savedObj) {
												if (savedObj.id) {
													var wasUpdate = false;
													angular
															.forEach(
																	prodPlan.prodRequisitionList,
																	function(
																			req) {
																		if (req.id == savedObj.id) {
																			wasUpdate = true;
																		}
																	});

													if (wasUpdate) {
														showAddToast();
														$mdDialog.cancel();
													} else {
														if (!prodPlan.prodRequisitionList)
															prodPlan.prodRequisitionList = [];

														prodPlan.prodRequisitionList
																.push(savedObj)

														productService
																.addProdPlanEntity(
																		prodPlan)
																.then(
																		function(
																				savedObj) {
																			if (savedObj.id)
																				showAddToast();

																			$mdDialog
																					.cancel();
																		});
													}
												}
											});
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};

						$scope.reqBomItemChanged = function(bomEntity) {

							if (!$scope.productionRequisition.id
									|| $scope.productionRequisitionBackup.bomEntity.id !== bomEntity.id) {
								var productService = appEndpointSF
										.getProductionService();
								productService
										.getEmptyProductionRequisition(
												bomEntity)
										.then(
												function(
														productionRequisitionTemp) {
													$scope.productionRequisition = productionRequisitionTemp;
													$scope.productionRequisition.bomEntity = bomEntity;
													$scope.productionRequisition.deliveryDateTime = new Date();
												});
							}

						}

						$scope.fetchBomList();
					}

				});