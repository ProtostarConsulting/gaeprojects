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
					if($stateParams.productionRequisition!=null){
					$scope.productionRequisition.deliveryDate=new Date($stateParams.productionRequisition.deliveryDate);
					$scope.productionRequisition.deliverytime=new Date($stateParams.productionRequisition.deliverytime);
					}

					$scope.query = {
						order : 'firstName',
						limit : 5,
						page : 1,
						totalSize : 0,
						pagesLoaded : 0
					};

					$scope.bomList = [];
					$scope.stockItemCategories = [];
					$scope.stockTypeList = [];
					var dummyStockTypeList = [];


					$scope.calculation = function() {
						if ($scope.productionRequisition.productQty != 0
								&& $scope.productionRequisition.productQty != null) {

							for (var i = 0; i < $scope.productionRequisition.bomEntity.catList.length; i++) {
						for (var j = 0; j < $scope.productionRequisition.catList[i].items.length; j++) {
							
				var qty= parseInt($scope.productionRequisition.bomEntity.catList[i].items[j].qty);
				$scope.productionRequisition.catList[i].items[j].qty = qty
					* $scope.productionRequisition.productQty;
								}
							}
						}
					}

					$scope.fetchProductionList = function() {
						var productService = appEndpointSF
								.getProductionService();

						productService
								.getlistBomEntity($scope.curUser.business.id)
								.then(
										function(list) {
											$scope.bomList = list;
															});
					}

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
					$scope.selectItems = function(bomEntity) {
						
						if(!$scope.productionRequisition.id){
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

					$scope.printBOM = function(bomId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfBOM?bid=" + bid + "&bomId="
								+ bomId);
					}
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchProductionList();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

				});