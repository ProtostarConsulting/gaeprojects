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

					$scope.statusList = [ "DRAFT", "INPROGRESS", "COMPLETED" ];

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
					$scope.totalProducedQty = 0;

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
					$scope.printstockShipment = function(stShipId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfProdShipment?bid=" + bid
								+ "&stShipId=" + stShipId);

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

					$scope.calculateProducedQty = function() {
						$scope.totalProducedQty = 0;
						$scope.totalProducedPerc = 0;
						if ($scope.documentEntity.planDailyReport) {
							for (var i = 0; i < $scope.documentEntity.planDailyReport.length; i++) {
								$scope.totalProducedQty += $scope.documentEntity.planDailyReport[i].bomProducedQty;
							}
							$scope.totalProducedPerc = ($scope.totalProducedQty * 100)
									/ $scope.documentEntity.plannedQty;
						}
					};

					$scope.getFlexPerc = function() {
						return {
							'width' : $scope.totalProducedPerc - 4 + '%'
						};
					}

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
												bomList : $scope.bomList,
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

					$scope.addProdReport = function(ev, prodctionReport) {
						var useFullScreen = $mdMedia('xs');
						$mdDialog
								.show(
										{
											controller : addProdReportCtr,
											templateUrl : '/app/production/add_prod_report_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
												prodctionReport : prodctionReport,
												prodPlan : $scope.documentEntity,
												bomList : $scope.bomList,
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
											$scope.calculateProducedQty();
										});
					};

					// production shipment List
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

					}

					function addProdReqCtr($scope, $mdDialog, curUser,
							productionRequisition, prodPlan, bomList,
							showAddToast) {

						$scope.getEmptyObj = function() {
							return {
								createdDate : new Date(),
								createdBy : curUser,
								business : curUser.business,
								status : 'DRAFT'
							};
						}

						$scope.todaysDate = new Date();
						$scope.productionRequisition = productionRequisition ? productionRequisition
								: $scope.getEmptyObj();

						if ($scope.productionRequisition.id) {
							$scope.productionRequisition.deliveryDateTime = new Date(
									$scope.productionRequisition.deliveryDateTime);
						} else {
							$scope.productionRequisition.deliveryDateTime = new Date();
						}

						$scope.productionRequisitionBackup = angular
								.copy($scope.productionRequisition);

						$scope.bomList = bomList;
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

						$scope.filterStockItemsByBomTypes = function(bomEntity) {
							var stockService = appEndpointSF.getStockService();
							stockService
									.filterStockItemsByBomTypes({
										'bomEntity' : bomEntity
									})
									.then(
											function(stockList) {
												$scope.firmStockItemList = stockList;

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
											});
						}

						$scope.getCurrentWHStockItemQty = function(
								stockItemType) {
							var totalStockQty = 0;
							if (!stockItemType)
								return 'NA';
							angular
									.forEach(
											$scope.firmStockItemList,
											function(stockItem) {
												if (stockItem.stockItemType.id == stockItemType.id)
													totalStockQty += stockItem.qty;
											});
							return totalStockQty;
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

							var wasUpdate = null;
							if ($scope.productionRequisition.id) {
								wasUpdate = true;
							} else {
								wasUpdate = false;
							}

							productService
									.addRequisition(
											$scope.productionRequisition)
									.then(
											function(savedObj) {
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

											});
						}

						$scope.draftDocumnent = function(ev) {
							$scope.productionRequisition.status = 'DRAFT';
							$scope.addRequisition();
						}

						$scope.submitDocumnent = function(ev) {
							$scope.productionRequisition.status = 'SUBMITTED';
							$scope.addRequisition();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};

						$scope.reqBomItemChanged = function(bomEntity) {

							if (!$scope.productionRequisition.id
									|| $scope.productionRequisitionBackup.bomEntity.id !== bomEntity.id) {

								$scope.filterStockItemsByBomTypes(bomEntity);

							}

						}

						if ($scope.productionRequisitionBackup.bomEntity
								&& $scope.productionRequisitionBackup.bomEntity.id) {
							$scope
									.filterStockItemsByBomTypes($scope.productionRequisitionBackup.bomEntity);
						}
					}

					function addProdReportCtr($scope, $mdDialog, curUser,
							prodctionReport, prodPlan, bomList, showAddToast) {

						$scope.getEmptyObj = function() {
							return {
								prodPlanItemNumber : prodPlan.itemNumber,
								createdDate : new Date(),
								createdBy : curUser,
								business : curUser.business,
								status : 'DRAFT'
							};
						}

						$scope.todaysDate = new Date()
								.setHours(00, 00, 00, 000);
						$scope.prodctionReport = prodctionReport ? prodctionReport
								: $scope.getEmptyObj();

						if ($scope.prodctionReport.id) {
							$scope.prodctionReport.reportDate = new Date(
									$scope.prodctionReport.reportDate);
						} else {
							$scope.prodctionReport.reportDate = new Date();
						}

						$scope.prodctionReportBackup = angular
								.copy($scope.prodctionReport);

						$scope.bomList = bomList;

						$scope.addProductionReport = function() {

							var productService = appEndpointSF
									.getProductionService();

							$scope.prodctionReport.modifiedBy = curUser.email_id;

							if ($scope.prodctionReportBackup.id) {
								// if edit changed the value it should edit
								// existing record, not create new one
								$scope.prodctionReport.id = $scope.prodctionReportBackup.id;

							}

							var wasUpdate = null;
							if ($scope.prodctionReport.id) {
								wasUpdate = true;
							} else {
								wasUpdate = false;
							}

							productService
									.addProductionReport($scope.prodctionReport)
									.then(
											function(savedObj) {
												if (savedObj.id) {
													if (wasUpdate) {
														showAddToast();
														$mdDialog.cancel();
													} else {
														if (!prodPlan.planDailyReport)
															prodPlan.planDailyReport = [];

														prodPlan.planDailyReport
																.push(savedObj)
														if (savedObj.bomProducedQty > 0
																&& prodPlan.status == "DRAFT") {
															prodPlan.status = "INPROGRESS";
														}
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
					}

					function addProdShipCtr($scope, $mdDialog, curUser,
							productionShipment, prodPlan, showAddToast) {

						$scope.getEmptyObj = function() {
							return {
								productLineItemList : [],
								shipmentDate : new Date(),
								createdDate : new Date(),
								createdBy : curUser,
								business : curUser.business,
								status : 'DRAFT'
							};
						}

						$scope.warehouses = [];
						$scope.stockItemList = [];
						$scope.documentEntity = productionShipment ? productionShipment
								: $scope.getEmptyObj();

						if ($scope.documentEntity.id) {
							$scope.documentEntity.shipmentDate = new Date(
									productionShipment.shipmentDate);
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};

						$scope.filterStockItemsByWarehouse = function(
								selectedWarehouse) {
							$scope.loading = true;
							$scope.selectedWarehouse = selectedWarehouse;
							var stockService = appEndpointSF.getStockService();

							stockService.filterStockItemsByWarehouse(
									$scope.selectedWarehouse).then(
									function(stockList) {
										$scope.stockItemList = stockList;
										$scope.loading = false;
									});
							if (!$scope.documentEntity.id) {
								$scope.addProductLineItem();
							}
						}

						$scope.getAllWarehouseByBusiness = function() {
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

							$scope.calProductSubTotal();
							$scope.calfinalTotal();
						};

						$scope.finalizeDocumnent = function(ev) {
							var confirm = $mdDialog
									.confirm()
									.title(
											'Do you want to approve/finalize this Document? Note, after this you will not be able to make any changes in this document.')
									.textContent('').ariaLabel('finalize?')
									.targetEvent(ev).ok('Yes').cancel('No');

							$mdDialog
									.show(confirm)
									.then(
											function() {
												$log
														.debug("Inside Yes, function");
												$scope.documentEntity.status = 'FINALIZED';
												$scope.documentEntity.approvedBy = $scope.curUser;
												$scope.saveDocument();
											}, function() {
												$log.debug("Cancelled...");
											});
						}

						$scope.saveDocument = function() {

							$scope.documentEntity.modifiedBy = curUser.email_id;

							var wasUpdate = null;
							if ($scope.documentEntity.id) {
								wasUpdate = true;
							} else {
								wasUpdate = false;
							}

							var stockService = appEndpointSF.getStockService();

							stockService.addProductionStockShipment(
									$scope.documentEntity).then(
									function(savedObj) {
										if (wasUpdate) {
											showAddToast();
											$mdDialog.cancel();
										} else {
											if (!prodPlan.prodShipmentList)
												prodPlan.prodShipmentList = [];

											prodPlan.prodShipmentList
													.push(savedObj)
											var productService = appEndpointSF
													.getProductionService();

											productService.addProdPlanEntity(
													prodPlan).then(
													function(savedObj) {
														if (savedObj.id)
															showAddToast();

														$mdDialog.cancel();
													});
										}

									});

						}

						$scope.getAllWarehouseByBusiness();

					}

					$scope.fetchBomList = function() {
						var productService = appEndpointSF
								.getProductionService();

						productService.getlistBomEntity(
								$scope.curUser.business.id).then(
								function(list) {
									$scope.bomList = list;
								});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchBomList();
							$scope.calculateProducedQty();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

				});