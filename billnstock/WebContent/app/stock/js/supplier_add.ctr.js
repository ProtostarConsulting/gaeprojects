app = angular.module("stockApp");
app
		.controller(
				"supplierAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $q, $mdMedia, $mdDialog,
						objectFactory, appEndpointSF) {

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					$scope.isSaving = false;
					$scope.getEmptySupplier = function() {
						return {
							supplierName : '',
							contactFName : '',
							contactLName : '',
							phone1 : '',
							mobile : '',
							address : {},
							email : '',
							business : ''
						}
					};

					$scope.addSupplier = function() {
						$scope.isSaving = true;
						$scope.supplier.business = $scope.curUser.business;
						$scope.supplier.modifiedBy = $scope.curUser.email_id;

						var supplierService = appEndpointSF
								.getSupplierService();
						supplierService
								.addSupplier($scope.supplier)
								.then(
										function(msgBean) {
											$scope.showUpdateToast();
											$scope.supplierForm.$setPristine();
											$scope.supplierForm.$setValidity();
											$scope.supplierForm.$setUntouched();
											$scope.supplier = $scope
													.getEmptySupplier();
										});
						$scope.isSaving = false;
					}

					$scope.supplier = $stateParams.selectedSupplier ? $stateParams.selectedSupplier
							: $scope.getEmptySupplier();

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							// do nothing...
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					/* Setup menu */
					$scope.toggleRight = buildToggler('right');
					/**
					 * Build handler to open/close a SideNav; when animation
					 * finishes report completion in console
					 */
					function buildToggler(navID) {
						var debounceFn = $mdUtil.debounce(function() {
							$mdSidenav(navID).toggle().then(function() {
								$log.debug("toggle " + navID + " is done");
							});
						}, 200);
						return debounceFn;
					}

					$scope.close = function() {
						$mdSidenav('right').close().then(function() {
							$log.debug("close RIGHT is done");
						});
					};

				});
