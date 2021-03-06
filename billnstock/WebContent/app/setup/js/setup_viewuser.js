angular
		.module("stockApp")
		.controller(
				"setup.viewuser",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, $filter, objectFactory,
						$mdDialog, $mdMedia, appEndpointSF) {

					function defaultActionProcessing() {
						return {
							saving : false,
							saveButtonText : "Save Changes",
							savingButtonText : "Saving..."
						};
					}
					
					$scope.actionProcessing = defaultActionProcessing();

					$scope.selectedUser = $stateParams.selectedUser;

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.condition = function() {
						if ($scope.user.isGoogleUser == false) {
							return true;
						} else {
							return false;
						}
					}

					$scope.getEmpDepartments = function() {
						$scope.newDept = false;

						var userService = appEndpointSF.getUserService();
						userService.getEmpDepartments(
								$scope.selectedUser.business.id).then(
								function(list) {
									if (list.items) {
										$scope.departmentList = list.items;
										$scope.departmentList = $filter(
												'proOrderObjectByTextField')(
												$scope.departmentList, "name");
									}
								});

					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getEmpDepartments();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.updateuser = function() {
						$scope.actionProcessing.saving = true;
						
						$scope.selectedUser.modifiedBy = $scope.curUser.email_id;
						var UserService = appEndpointSF.getUserService();
						UserService
								.addUser($scope.selectedUser)
								.then(
										function(msgBean) {
											$scope.showUpdateToast();
											$scope.actionProcessing = defaultActionProcessing();
										});
					}

				});
