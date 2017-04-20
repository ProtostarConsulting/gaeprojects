angular
		.module("stockApp")
		.controller(
				"taxCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF) {

					$scope.query = {
						order : 'taxCodeName',
						limit : 50,
						page : 1,
						totalSize : 0,
						pagesLoaded : 0
					};

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					function newTax() {
						return {
							taxCodeName : '',
							taxPercenatge : '',
							active : true,
							business : ""
						}
					}

					$scope.taxList = [];
					$scope.selectedTax = $stateParams.selectedTax ? $stateParams.selectedTax
							: newTax();

					$scope.addTax = function() {
						$scope.selectedTax.business = $scope.curUser.business;
						var taxService = appEndpointSF.getTaxService();
						taxService.addTax($scope.selectedTax).then(
								function(msgBean) {
									if ($scope.selectedTax.id) {
										$scope.showUpdateToast();
									} else {
										$scope.showAddToast();
										$scope.selectedTax = newTax();
										resetTaxForm();
									}
								});

					}

					$scope.logOrder = function(order) {
						console.log('order: ', order);
					};

					$scope.logPagination = function(page, limit) {
						console.log('page: ', page);
						console.log('limit: ', limit);
						$location.hash('tp1');
						$anchorScroll();
						if ($scope.query.page > $scope.query.pagesLoaded) {
							$scope.getAllTaxes();
						}
					}

					$scope.getAllTaxes = function() {
						var taxService = appEndpointSF.getTaxService();
						taxService.getAllTaxes($scope.curUser.business.id)
								.then(function(taxList) {
									$scope.taxList = taxList;
								});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAllTaxes();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					function resetTaxForm() {
						$scope.taxForm.$setPristine();
						$scope.taxForm.$setValidity();
						$scope.taxForm.$setUntouched();
						$scope.selected = [];
					}

					$scope.cancelUpdate = function() {
						$scope.back();
					}
				});
