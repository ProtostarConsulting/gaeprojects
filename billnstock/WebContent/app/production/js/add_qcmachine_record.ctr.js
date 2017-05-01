var app = angular.module("stockApp");

app
		.controller(
				"qcmachineAddRecordCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia) {
					$scope.loading = true;
				
					$scope.isTableShow = false;
					$scope.timeArray = [];
					$scope.machineList = [];
					$scope.tempMachine = null;				
					

					$scope.qcRecordObj = $stateParams.qcmachineRecordObj ? $stateParams.qcmachineRecordObj
							: null;

					$scope.getMachineParamList = function(machine) {
						var productService = appEndpointSF
								.getProductionService();
						productService.getQCMachineById(
								$scope.curUser.business.id, machine.id).then(
								function(machineObj) {
									$scope.tempMachine = machineObj;
								});
					}

					$scope.getQCMachineDailyRecord = function(recordDate) {
						$scope.isTableShow = true;
						$scope.tempDate = new Date(recordDate);
						var productService = appEndpointSF
								.getProductionService();
						productService
								.getQCMachineDailyRecordEntity(
										$scope.tempMachine.id,
										$scope.curUser.business.id,
										$scope.tempDate.getTime())
								.then(
										function(qcMachineDailyRecordObj) {
												$scope.qcRecordObj = qcMachineDailyRecordObj;
												$scope
														.getTimeArrayFromRecordObj($scope.qcRecordObj);
											
										});
					}

					$scope.addQCMachineRecord = function() {

						$scope.qcRecordObj.createdBy = $scope.curUser;
						$scope.qcRecordObj.business = $scope.curUser.business;
						$scope.qcRecordObj.modifiedBy = $scope.curUser.email_id;

						$scope.qcRecordObj.machineQc = $scope.tempMachine;
						$scope.qcRecordObj.recordDate = $scope.recordDate;

						var productService = appEndpointSF
								.getProductionService();
						productService.addQCMachineRecord(
								$scope.qcRecordObj).then(
								function(machineObj) {
									if (machineObj.id != undefined) {
										$scope.qcRecordObj = machineObj;
										$scope.showAddToast();
									}
								});
					}

					$scope.getTimeArrayFromRecordObj = function(recordObj) {
						for (var i = 0; i < recordObj.parameterValueList.length; i++) {
							$scope.timeArray.push(new Date(
									recordObj.parameterValueList[i].time)
									.toLocaleTimeString('en-US', {
										hour12 : true,
										hour : "numeric",
										minute : "numeric"
									}));
						}
					}

					$scope.initLoad = function(qcmachine_record) {
						$scope.machineQc = qcmachine_record.machineQc;
						$scope.recordDate = new Date(
								qcmachine_record.recordDate);
						$scope.isTableShow = true;
					}

					$scope.fetchMachineList = function() {

						var productService = appEndpointSF
								.getProductionService();
						productService.getQCMachineList(
								$scope.curUser.business.id).then(
								function(list) {
									$scope.machineList = list;
									$scope.loading = false;
								});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchMachineList();
							if ($scope.qcRecordObj) {
								$scope
										.initLoad($scope.qcRecordObj);
								$scope
										.getTimeArrayFromRecordObj($scope.qcRecordObj);
							}
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();
				});