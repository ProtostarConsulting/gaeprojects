var app = angular.module("stockApp");

app
		.controller(
				"addStockReceiptQCRecordCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.isTableShow = false;
					$scope.qcstockReceiptList = [];
					$scope.tempRecepitQCObj = {
							qcStockReceipt : "",
							paramRecordedValues : [],
							createdDate : new Date(),
							createdBy : $scope.curUser,
							qcResult : "",
							note : ""
						};
										
					$scope.tempStockReceiptObj = $stateParams.stockReceiptObj ? $stateParams.stockReceiptObj
								: $scope.tempStockReceiptObj;
					if ($scope.tempStockReceiptObj == undefined) {
						$scope.isTableShow = true;
						$scope.tempRecepitQCObj = $stateParams.stockReceiptQCRecordObj ? $stateParams.stockReceiptQCRecordObj
								: $scope.tempRecepitQCObj;
					}
					//this temp obj
					if($stateParams.tempReceiptObj){
						$scope.tempReceipt = $stateParams.tempReceiptObj;
					}	
														
					$scope.getStockReceiptQCDailyRecord = function(
							qcStockReceiptObj) {
						$scope.isTableShow = true;
						if($scope.tempStockReceiptObj != undefined){
							var stockService = appEndpointSF.getStockService();
							stockService
									.getStockReceiptQCDailyRecordEntity(
											qcStockReceiptObj,
											$scope.curUser.business.id)
									.then(
											function(qcRecordObj) {
												$scope.tempStockReceiptQCDailyRecordObj = qcRecordObj;
												$scope.tempRecepitQCObj.paramRecordedValues = $scope.tempStockReceiptQCDailyRecordObj.paramRecordedValues;
											});
						}
					}

					$scope.addStockReceiptQCRecord = function() {

						if (!$scope.tempStockReceiptObj.recepitQCList) {
							$scope.tempStockReceiptObj.recepitQCList = [];
							$scope.tempStockReceiptObj.recepitQCList.push($scope.tempRecepitQCObj);
						} else {
							$scope.tempStockReceiptObj.recepitQCList
									.push($scope.tempRecepitQCObj);
						}
						var stockService = appEndpointSF.getStockService();
						stockService
								.addStockReceipt($scope.tempStockReceiptObj)
								.then(function(qcRecordObj) {
									if (qcRecordObj.id != undefined) {
										$scope.showAddToast();
										// $state.go('stock.stockReceiptAdd',{'stockReceiptObj':
										// qcRecordObj});
									}
								});
					}

					$scope.fetchStockReceiptQCList = function() {
						var stockService = appEndpointSF.getStockService();
						stockService.getStockReceiptQCList(
								$scope.curUser.business.id).then(
								function(list) {
									$scope.qcstockReceiptList = list;
								});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchStockReceiptQCList();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();
				});