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

					$scope.requisitionTemp = {
						requester : $scope.curUser,
						status : 'DRAFT',
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

					$scope.requisition = $stateParams.selectedRequisition ? $stateParams.selectedRequisition
							: $scope.requisitionTemp;

					if ($stateParams.selectedRequisition) {
						$scope.requisition.expectedDate = new Date(
								$scope.requisition.expectedDate);

					}

					$scope.addReq = function() {

						if (!$scope.requisition.serviceLineItemList) {

							$scope.errorMsg = "Please select atleast one line item.";
						} else {
							$scope.requisition.business = $scope.curUser.business;
							$scope.requisition.modifiedBy = $scope.curUser.email_id;

							var stockService = appEndpointSF.getStockService();

							stockService.addRequisition($scope.requisition)
									.then(function() {
										$scope.showUpdateToast();
									});
							$scope.loading = false;
						}
					}

					$scope.submitReq = function(ev) {
						$scope.requisition.status = 'SUBMITTED';
						$scope.addReq();
					}

					$scope.addLineItem = function() {

						var item = {
							itemName : "",
							qty : 1,
							price : null
						};

						if (!$scope.requisition.serviceLineItemList) {
							$scope.requisition.serviceLineItemList = [];
						}
						$scope.requisition.serviceLineItemList.push(item);
					}

					$scope.removeLineItem = function(index) {
						$scope.requisition.serviceLineItemList.splice(index, 1);
					};
				});