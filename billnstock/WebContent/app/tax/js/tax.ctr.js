angular
		.module("stockApp")
		.controller(
				"taxCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory, $state,
						appEndpointSF) {

					$scope.query = {
						order : 'taxCodeName',
						limit : 50,
						page : 1,
						totalSize : 0,
						pagesLoaded : 0
					};
					$scope.isSaving = false;
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					function newTax() {
						return {
							taxCodeName : '',
							taxPercenatge : 0,
							active : true,
							withSubHeads : false,
							withAdditionalExciseTax : false,
							exciseTaxPercenatge : 0,
							business : "",
							subHeads : []
						}
					}

					$scope.taxList = [];

					$scope.selectedTax = $stateParams.selectedTax ? $stateParams.selectedTax
							: newTax();

					$scope.addTax = function() {
						$scope.isSaving = true;
						$scope.selectedTax.business = $scope.curUser.business;
						var taxService = appEndpointSF.getTaxService();
						taxService.addTax($scope.selectedTax).then(
								function(msgBean) {
									if ($scope.selectedTax.id) {
										$scope.isSaving = false;
										$scope.showUpdateToast();
									} else {
										$scope.isSaving = false;
										$scope.showAddToast();
										$scope.selectedTax = newTax();
										$state.reload();
									}
								});

					}

					$scope.addTaxLineItem = function() {

						var taxLineItem = {
							taxSubCodeName : "",
							taxPercenatge : 0,
						};

						if (!$scope.selectedTax.subHeads) {
							$scope.selectedTax.subHeads = [];
						}

						$scope.selectedTax.subHeads.push(taxLineItem);

					};

					$scope.removeTaxLineItem = function(index) {
						$scope.selectedTax.subHeads.splice(index, 1);
						$scope.calSubheadsTaxPercTotal();
					};

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

					$scope.calSubheadsTaxPercTotal = function() {

						$scope.selectedTax.taxPercenatge = 0;
						if ($scope.selectedTax.subHeads
								&& $scope.selectedTax.subHeads.length > 0) {

							for (var i = 0; i < $scope.selectedTax.subHeads.length; i++) {

								var taxPerc = $scope.selectedTax.subHeads[i].taxPercenatge;

								$scope.selectedTax.taxPercenatge += taxPerc;

							}
						}

					};

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAllTaxes();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					$scope.cancelUpdate = function() {
						$scope.back();
					}
				});
