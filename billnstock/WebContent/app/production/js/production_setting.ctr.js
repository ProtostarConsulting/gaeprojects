app = angular.module("stockApp");
app
		.controller(
				"productionSettingsCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $mdMedia, $mdDialog, $q,
						objectFactory, appEndpointSF) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.settingsObj = {}
					
					$scope.qcMachineUnitMeasure = {
							unitName : ""
						}
					$scope.qcMachineUnitMeasureList = [];
					
					$scope.addSettings = function() {

						var productService = appEndpointSF.getProductionService();
						$scope.settingsObj.business = $scope.curUser.business;
						$scope.settingsObj.modifiedBy = $scope.curUser.email_id;

						productService.addProductionSettings($scope.settingsObj).then(
								function(savedRecoed) {
									$scope.settingsObj = savedRecoed;
									$scope.showUpdateToast();
								});
					}
					
					$scope.getProductionSettingsByBiz = function() {

						var productService = appEndpointSF.getProductionService();

						productService.getProductionSettingsByBiz(
								$scope.curUser.business.id).then(
								function(settingsList) {

									$scope.settingsObj = settingsList;
									$log.debug("Inside Ctr $scope.settingsObj:"
											+ $scope.settingsObj);
									return $scope.settingsObj;
								});
					}
					
					$scope.addMachineQCUnitMeasure = function() {

						if (!$scope.qcMachineUnitMeasure.unitName) {
							return;
						}

						$scope.qcMachineUnitMeasure.business = $scope.curUser.business;
						$scope.qcMachineUnitMeasure.modifiedBy = $scope.curUser.email_id;
						var productService = appEndpointSF.getProductionService();

						productService.addQCMachineUnitMeasure($scope.qcMachineUnitMeasure)
								.then(function() {
									$scope.qcMachineUnitMeasure = {};
									$scope.getMachineQCUnitsMeasureList();
								})

					}
					
					$scope.editMachineQCUnitMeasure = function(ev, qcMachineUnit) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : editQCMachineUnitMeasureDialogCtr,
											templateUrl : 'app/production/production_qcmachine_unitmeasure_edit_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curBusi : $scope.curUser.business,
												curUser : $scope.curUser,
												curStockItemUnit : qcMachineUnit,
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
					
					function editQCMachineUnitMeasureDialogCtr($scope, $mdDialog,
							curBusi, curUser, curStockItemUnit) {

						$scope.qcMachineUnitMeasure = angular.copy(curStockItemUnit);
						$scope.qcMachineUnitMeasure.modifiedBy = curUser.email_id;

						$scope.addqcMachineUnitMeasure = function() {

							var productService = appEndpointSF.getProductionService();

							productService
									.addQCMachineUnitMeasure($scope.qcMachineUnitMeasure)
									.then(
											function() {
												curStockItemUnit.unitName = $scope.qcMachineUnitMeasure.unitName;
												curStockItemUnit.note = $scope.qcMachineUnitMeasure.note;
											})

							$scope.cancel();
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};
					}
					
					$scope.getMachineQCUnitsMeasureList = function() {

						var productService = appEndpointSF.getProductionService();
						productService
								.getMachineQCUnitsMeasureList($scope.curUser.business.id)
								.then(
										function(qcMachineUnitMeasureList) {
											$scope.qcMachineUnitMeasureList = qcMachineUnitMeasureList;
										})
					}



					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getProductionSettingsByBiz();
							$scope.getMachineQCUnitsMeasureList();
						} else {
							$log.debug("Services Not Loaded, waiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

				});