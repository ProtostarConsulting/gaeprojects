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

					$scope.tempStockReceiptQCDailyRecordObj = $stateParams.stockReceiptQCRecordObj ? $stateParams.stockReceiptQCRecordObj
							: $scope.tempStockReceiptQCDailyRecordObj;

					$scope.tempStockReceiptObj = $stateParams.stockReceiptObj ? $stateParams.stockReceiptObj
							: null;

					$scope.tempRecepitQCObj = {
						qcStockReceipt : "",
						paramRecordedValues : []
					};

					if ($scope.tempStockReceiptQCDailyRecordObj != undefined) {
						$scope.isTableShow = true;
						$scope.stockReceiptQc = $scope.tempStockReceiptQCDailyRecordObj.qcStockReceipt;
						$scope.recordDate = new Date(
								$scope.tempStockReceiptQCDailyRecordObj.recordDate);
					}

					$scope.getStockReceiptQCDailyRecord = function(
							qcStockReceiptObj) {
						$scope.isTableShow = true;
						var stockService = appEndpointSF.getStockService();
						stockService
								.getStockReceiptQCDailyRecordEntity(
										qcStockReceiptObj,
										$scope.curUser.business.id)
								.then(
										function(qcMachineDailyRecordObj) {
											$scope.tempStockReceiptQCDailyRecordObj = qcMachineDailyRecordObj;
											$scope.tempRecepitQCObj.paramRecordedValues = $scope.tempStockReceiptQCDailyRecordObj.paramRecordedValues;
										});
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