app = angular.module("stockApp");
app
		.controller(
				"requisitionAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $q, $mdMedia, $mdDialog,
						objectFactory, appEndpointSF) {

					$scope.documentStatusList = [ 'DRAFT', 'SUBMITTED',
							'FINALIZED', 'REJECTED' ];

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.documentEntityTemp = {
						requester : $scope.curUser,
						status : 'DRAFT',
						createdBy : $scope.curUser,
						onBehalfOf : "",
						expectedDate : new Date(),
						createdDate : new Date(),
						modifiedDate : new Date(),
						modifiedBy : "",
						business : null,
						serviceLineItemList : []
					};

					$scope.requesterName = $scope.curUser.firstName + " "
							+ $scope.curUser.lastName;

					$scope.documentEntity = $stateParams.selectedRequisition ? $stateParams.selectedRequisition
							: $scope.documentEntityTemp;

					if ($stateParams.selectedRequisition) {
						$scope.documentEntity.expectedDate = new Date(
								$scope.documentEntity.expectedDate);

					}

					$scope.saveDocument = function() {

						if (!$scope.documentEntity.serviceLineItemList) {

							$scope.errorMsg = "Please select atleast one line item.";
						} else {
							$scope.documentEntity.business = $scope.curUser.business;
							$scope.documentEntity.modifiedBy = $scope.curUser.email_id;

							var stockService = appEndpointSF.getStockService();

							stockService.addRequisition($scope.documentEntity)
									.then(function() {
										$scope.showUpdateToast();
										$scope.documentEntity = {};
									});
							$scope.loading = false;
						}
					}

					$scope.submitDocumnent = function(ev) {
						$scope.documentEntity.status = 'SUBMITTED';
						$scope.saveDocument();
					}
					
					$scope.finalizeDocumnent = function(ev) {
						var confirm = $mdDialog
								.confirm()
								.title(
										'Do you want to approve/finalize this Document? Note, after this you will not be able to make any changes in this document.')
								.textContent('').ariaLabel('finalize?')
								.targetEvent(ev).ok('Yes').cancel('No');

						$mdDialog.show(confirm).then(function() {
							$log.debug("Inside Yes, function");
							$scope.documentEntity.status = 'FINALIZED';
							$scope.documentEntity.approvedBy = $scope.curUser;
							$scope.saveDocument();
						}, function() {
							$log.debug("Cancelled...");
						});
					}
					
					$scope.rejectDocumnent = function(ev) {
						var confirm = $mdDialog
								.confirm()
								.title(
										'Do you want to reject this Document? Note, after this you will not be able to make any changes in this document.')
								.textContent('').ariaLabel('finalize?')
								.targetEvent(ev).ok('Yes').cancel('No');

						$mdDialog.show(confirm).then(function() {
							$log.debug("Inside Yes, function");
							$scope.documentEntity.status = 'REJECTED';
							$scope.documentEntity.approvedBy = $scope.curUser;
							$scope.saveDocument();
						}, function() {
							$log.debug("Cancelled...");
						});
					}

					$scope.addLineItem = function() {
						var item = {
							itemName : "",
							qty : 1,
							price : null
						};

						if (!$scope.documentEntity.serviceLineItemList) {
							$scope.documentEntity.serviceLineItemList = [];
						}
						$scope.documentEntity.serviceLineItemList.push(item);
					}

					$scope.removeLineItem = function(index) {
						$scope.documentEntity.serviceLineItemList.splice(index,
								1);
					};

					function getStockSettingsByBiz() {
						var stockService = appEndpointSF.getStockService();
						stockService.getStockSettingsByBiz(
								$scope.curUser.business.id).then(
								function(settingsList) {
									$scope.settingsObj = settingsList;
								});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							getStockSettingsByBiz();

							if (!$scope.documentEntity.id) {
								$scope.addLineItem();
							}

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();
				});