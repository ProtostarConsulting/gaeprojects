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

							if (!$scope.productionRequisition.id) {
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