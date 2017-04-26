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
					$scope.tempStockReceiptQC = null;
					
					$scope.tempStockReceiptQCDailyRecordObj = $stateParams.stockReceiptQCRecordObj ? $stateParams.stockReceiptQCRecordObj
							: $scope.tempStockReceiptQCDailyRecordObj;
					
					if($scope.tempStockReceiptQCDailyRecordObj != undefined){
						$scope.isTableShow = true;
						$scope.stockReceiptQc = $scope.tempStockReceiptQCDailyRecordObj.qcStockReceipt;
						$scope.recordDate = new Date($scope.tempStockReceiptQCDailyRecordObj.recordDate);
					}

					$scope.getStockReceiptQCParamList = function(qcStockReceipt) {
						var stockService = appEndpointSF.getStockService();
						stockService.getStockReceiptQCById(
								$scope.curUser.business.id, qcStockReceipt.id).then(
								function(qcStockReceiptObj) {
									$scope.tempStockReceiptQC = qcStockReceiptObj;
								});
					}

					$scope.getStockReceiptQCDailyRecord = function(recordDate) {
						$scope.isTableShow = true;
						$scope.tempDate = new Date(recordDate);
						var stockService = appEndpointSF.getStockService();
						stockService.getStockReceiptQCDailyRecordEntity($scope.tempStockReceiptQC.id,
										$scope.curUser.business.id,$scope.tempDate.getTime())
								.then(function(qcMachineDailyRecordObj) {
												$scope.tempStockReceiptQCDailyRecordObj = qcMachineDailyRecordObj;											
										});
					}

					$scope.addStockReceiptQCRecord = function() {

						$scope.tempStockReceiptQCDailyRecordObj.createdBy = $scope.curUser;
						$scope.tempStockReceiptQCDailyRecordObj.business = $scope.curUser.business;
						$scope.tempStockReceiptQCDailyRecordObj.modifiedBy = $scope.curUser.email_id;

						$scope.tempStockReceiptQCDailyRecordObj.qcStockReceipt = $scope.tempStockReceiptQC;
						$scope.tempStockReceiptQCDailyRecordObj.recordDate = $scope.recordDate;

						var stockService = appEndpointSF.getStockService();
						stockService.addStockReceiptQCRecord(
								$scope.tempStockReceiptQCDailyRecordObj).then(
								function(qcRecordObj) {
									if (qcRecordObj.id != undefined) {
										$scope.showAddToast();
									}
								});
					}

					$scope.fetchStockReceiptQCList = function() {
						var stockService = appEndpointSF.getStockService();
						stockService.getStockReceiptQCList($scope.curUser.business.id).then(function(list) {
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