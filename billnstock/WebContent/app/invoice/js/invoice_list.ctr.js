app = angular.module("stockApp");
app
		.controller(
				"invoiceListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, objectFactory, appEndpointSF) {

					
					  $scope.query = {
						         order: 'name',
						         limit: 5,
						         page: 1
						       };
					  
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					$log.debug("$scope.curUser++++++++"
							+ angular.toJson($scope.curUser));

					$scope.updateInvoiceObj = {

						id : '',
						status : '',
					};
					$scope.selected = [];

					$scope.getAllInvoice = function() {
						$log.debug("Inside Ctr $scope.getAllInvoice");
						var invoiceService = appEndpointSF.getInvoiceService();

						invoiceService
								.getAllInvoice(
										$scope.curUser.business.id)
								.then(
										function(invoiceList) {
											$log
													.debug("Inside Ctr getAllInvoice");
											$scope.invoiceData = invoiceList;
											$log
													.debug("Inside Ctr $scope.invoiceData:"
															+ angular
																	.toJson($scope.invoiceData));
										});
					}

					$scope.invoiceData = [];
					$scope.getAllInvoice();

					$scope.stutusValues = [ "Paid", "NotPaid" ];
					$log.debug("$scope.sendToUpdate:"
							+ angular.toJson($scope.sendToUpdate));

					$scope.updateInvoice = function(invoiceId,status) {						
						$scope.sendToUpdate = [];
						$scope.sendToUpdate.push(invoiceId);
						$scope.sendToUpdate.push(status);
						$scope.valueToUpdate = {
								id : $scope.sendToUpdate[0],
								status:$scope.sendToUpdate[1]
						};
						
						var invoiceService = appEndpointSF.getInvoiceService();
						invoiceService.updateInvoice($scope.valueToUpdate).then(function() {
						});
						
						$scope.showSimpleToast();
						window.history.back();
					}

					$scope.selected = [];

					$scope.updatePaidStatus = function() {
						var paid = "Paid"
						$scope.selected[0].status = paid;
						var invoiceService = appEndpointSF.getInvoiceService();
						invoiceService.updateInvoiceStatus($scope.selected[0]).then(
								function(msgBean) {
									$scope.showSimpleToast(msgBean.msg);
								});
					}
					
					$scope.updateNotPaidStatus = function() {
						var notPaid = "NotPaid"
						$scope.selected[0].status = notPaid;
						var invoiceService = appEndpointSF.getInvoiceService();
						invoiceService.updateInvoiceStatus($scope.selected[0]).then(
								function(msgBean) {
									$scope.showSimpleToast(msgBean.msg);
								});
					}
					
					
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

					var printDivCSS = new String(
							'<link href="/lib/base/css/angular-material.min.css"" rel="stylesheet" type="text/css">'
									+ '<link href="/lib/base/css/bootstrap.min.css"" rel="stylesheet" type="text/css">')
					$scope.printDiv = function(divId) {
						// window.frames["print_frame"].document.body.innerHTML
						// = printDivCSS
						// + document.getElementById(divId).innerHTML;
						window.frames["print_frame"].document.body.innerHTML = document
								.getElementById(divId).innerHTML;
						window.frames["print_frame"].window.focus();
						window.frames["print_frame"].window.print();
					}
					
					
					$scope.showSimpleToast = function() {
						$mdToast.show($mdToast.simple().content(
								'Invoice Satus Changed!').position("top")
								.hideDelay(3000));
					};
					
					$scope.back = function() {
						 window.history.back();
					}
				});
