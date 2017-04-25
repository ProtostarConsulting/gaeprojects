app = angular.module("stockApp");
app
		.controller(
				"stockReceiptQCAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $mdMedia, $mdDialog, $q,
						$rootScope, objectFactory, appEndpointSF) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					console.log("inside stock receipt add****");
					
					$scope.qcstockreceipt = {
							qcno : "",
							qcName : "",
							validFrom : new Date(),
							validTill : new Date(),
							parameterList : [],
							note : "",
						};
					
					$scope.qcparameter = {
							name : "",
							parameterType : "",
							numberIdealValue : "",
							numberIdealValueValidDeviationPerc : ""
						}
					
					$scope.qcstockreceipt = $stateParams.qcstockReceiptObj ? $stateParams.qcstockReceiptObj
							: $scope.qcstockreceipt;
					
					if($stateParams.qcstockReceiptObj != null){
						$scope.qcstockreceipt.validFrom = new Date($stateParams.qcstockReceiptObj.validFrom);
						$scope.qcstockreceipt.validTill = new Date($stateParams.qcstockReceiptObj.validTill);
					}
					
					$scope.qcparameterType = [ "NUMBER", "NUMBERRANGE", "YESNO", "TEXT" ];
					$scope.stockItemUnitList = [];
					
					$scope.addStockReceiptQC = function() {
						
						$scope.qcstockreceipt.business = $scope.curUser.business;
						$scope.qcstockreceipt.modifiedBy = $scope.curUser.email_id;
						var stockService = appEndpointSF.getStockService();
						stockService.addStockReceiptQC($scope.qcstockreceipt).then(function(qcstockreceiptobj){
							if (qcstockreceiptobj.id != undefined) {
								$scope.showAddToast();
							}
						});
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
					
					$scope.addNewQcParameter = function() {
						$scope.qcparameter = {
							name : "",
							parameterType : "",
							numberIdealValue : "",
							numberIdealValueValidDeviationPerc : ""
						}
						$scope.qcstockreceipt.parameterList.push($scope.qcparameter);
					};
					
					$scope.removeParamterType = function(index) {
						$scope.qcstockreceipt.parameterList.splice(index, 1);
					}
					
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getStockItemUnitsList();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();
				});