angular
		.module("stockApp")
		.controller(
				"ReportByTaxReceivedCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $q, $mdMedia, $mdDialog,
						objectFactory, appEndpointSF, $mdColors) {

					$scope.query = {
						order : 'name',
						limit : 5,
						page : 1
					};

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.taxRcvData = [];

					$scope.getReportByTaxReceived = function(selectedFromDate,
							selectedToDate) {

						$scope.selectedFromDate = new Date(selectedFromDate);
						$scope.selectedToDate = new Date(selectedToDate);

						var invoiceService = appEndpointSF.getInvoiceService();

						invoiceService
								.getInvoicesForTaxCollection(
										$scope.curUser.business.id,
										$scope.selectedFromDate.getTime(),
										$scope.selectedToDate.getTime())
								.then(
										function(receivedTaxList) {
											$scope.taxRcvData = receivedTaxList;
											for (var i = 0; i < $scope.taxRcvData.length; i++) {
												$scope.taxRcvData[i].serviceSubTotal = 0;
												$scope.taxRcvData[i].serviceTaxTotal = 0;
												$scope.taxRcvData[i].productSubTotal = 0;
												$scope.taxRcvData[i].productTaxTotal = 0;
												if ($scope.taxRcvData[i].serviceLineItemList) {
													for (var j = 0; j < $scope.taxRcvData[i].serviceLineItemList.length; j++) {
														var lineItem = $scope.taxRcvData[i].serviceLineItemList[j];
														$scope.taxRcvData[i].serviceSubTotal += (lineItem.qty * lineItem.price);
														if (!$scope.taxRcvData[i].selectedServiceTax) {
															$scope.taxRcvData[i].serviceTaxTotal = 0;
														} else {
															$scope.taxRcvData[i].serviceTaxTotal = parseFloat(($scope.taxRcvData[i].selectedServiceTax.taxPercenatge / 100)
																	* ($scope.taxRcvData[i].serviceSubTotal));
														}
													}
												}
												if ($scope.taxRcvData[i].productLineItemList) {
													for (var k = 0; k < $scope.taxRcvData[i].productLineItemList.length; k++) {
														var lineItem = $scope.taxRcvData[i].productLineItemList[k];
														$scope.taxRcvData[i].productSubTotal += (lineItem.qty * lineItem.price);
														if (!$scope.taxRcvData[i].selectedProductTax) {
															$scope.taxRcvData[i].productTaxTotal = 0;
														} else {
															$scope.taxRcvData[i].productTaxTotal = parseFloat(($scope.taxRcvData[i].selectedProductTax.taxPercenatge / 100)
																	* ($scope.taxRcvData[i].productSubTotal));
														}
													}
												}

											}

										});

					}

					// Setup menu
					$scope.toggleRight = buildToggler('right');

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