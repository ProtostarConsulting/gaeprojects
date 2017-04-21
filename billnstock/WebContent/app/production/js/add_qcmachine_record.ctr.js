var app = angular.module("stockApp");

app
		.controller(
				"qcmachineAddRecordCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.qcmachineRecord = {
						machine : "",
						recordDate : new Date(),
						parameterValueList : [],
					};

					$scope.parameterValueObj = {
						paramRecordedValues : [],
						time : new Date()
					}

					$scope.paramRecordedValuesObj = {
						parameterName : "",
						recordedValue : ""
					}

					$scope.isTableShow = false;

					$scope.timeArray = [];
					$scope.machineList = [];
					$scope.machineParamList = [];
					$scope.tempMachine = null;

					$scope.parameterValueObj.paramRecordedValues
							.push($scope.paramRecordedValuesObj);
					$scope.qcmachineRecord.parameterValueList
							.push($scope.parameterValueObj);

					$scope.getMachineParamList = function(machine) {

						var productService = appEndpointSF
								.getProductionService();
						productService
								.getQCMachineById($scope.curUser.business.id,
										machine.id)
								.then(
										function(machineObj) {
											$scope.tempMachine = machineObj;
											//$scope.machineParamList = machineObj.parameterList;
										});
					}

					$scope.getQCMachineDailyRecord = function(recordDate) {
						$scope.isTableShow = true;
						var tempDate = new Date(recordDate);
						var productService = appEndpointSF
								.getProductionService();
						productService
								.getQCMachineDailyRecordEntity(
										$scope.tempMachine.id,
										$scope.curUser.business.id,
										tempDate.getTime())
								.then(
										function(qcMachineDailyRecordObj) {
											if (qcMachineDailyRecordObj != null) {
												$scope.tempQCMachineDailyRecordObj = qcMachineDailyRecordObj;
												for (var i = 0; i < $scope.tempQCMachineDailyRecordObj.parameterValueList.length; i++) {
													$scope.timeArray
															.push(new Date(
																	$scope.tempQCMachineDailyRecordObj.parameterValueList[i].time)
																	.toLocaleTimeString(
																			'en-US',
																			{
																				hour12 : true,
																				hour : "numeric",
																				minute : "numeric"
																			})
																	.toString());
												}
												for(var j = 0; j < $scope.tempQCMachineDailyRecordObj.parameterValueList[0].paramRecordedValues.length; j++){
													$scope.machineParamList.push($scope.tempQCMachineDailyRecordObj.parameterValueList[0].paramRecordedValues[j].parameterName);
												}
											}
										});
					}

					$scope.addQCMachineRecord = function() {

						$scope.qcmachineRecord.business = $scope.curUser.business;
						$scope.qcmachineRecord.modifiedBy = $scope.curUser.email_id;

						var productService = appEndpointSF
								.getProductionService();
						productService.addQCMachineRecord(
								$scope.qcmachineRecord).then(
								function(machineObj) {
									if (machineObj.id != undefined) {
										$scope.showAddToast();
									}
								});
					}

					$scope.fetchMachineList = function() {

						var productService = appEndpointSF
								.getProductionService();
						productService.getQCMachineList(
								$scope.curUser.business.id).then(
								function(list) {
									$scope.machineList = list;
								});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchMachineList();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();
				});