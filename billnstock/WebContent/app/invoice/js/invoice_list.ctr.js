app = angular.module("stockApp");
app
		.controller(
				"invoiceListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams, $mdColors,
						$routeParams, $filter, $location, $anchorScroll,
						objectFactory, appEndpointSF) {

					function reSetQuery() {
						return {
							order : '-itemNumber',
							limit : 50,
							page : 1,
							totalSize : 0,
							pagesLoaded : 0
						};
					}
					$scope.query = reSetQuery();
					$scope.documentStatusList = [ 'ALL', 'STARRED', 'DRAFT',
							'SUBMITTED', 'FINALIZED', 'SENT', 'PAID', 'UNPAID' ];
					$scope.selectedStatus = "";

					$scope.fitlerListByStatus = function(status) {
						status = (status == 'ALL') ? '' : status;
						$scope.selectedStatus = status;
						$scope.invoiceData = [];
						$scope.query = reSetQuery();
						$scope.pagingInfoReturned = null;
						if (status == 'STARRED') {
							$scope.getStarredInvoices();
						} else {
							$scope.fetchEntityListByPaging();
						}
					}

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.updateInvoiceObj = {
						id : '',
						status : '',
					};
					$scope.selected = [];

					$scope.onpagechange = function() {
						$location.hash('tp1');
						$anchorScroll();
						if ($scope.query.page > $scope.query.pagesLoaded) {
							$scope.fetchEntityListByPaging();
						}
					}

					$scope.fetchEntityListByPaging = function() {
						$scope.loading = true;
						var pagingInfoTemp = {
							entityList : null,
							startPage : $scope.query.page,
							limit : $scope.query.limit,
							totalEntities : 0,
							webSafeCursorString : $scope.pagingInfoReturned ? $scope.pagingInfoReturned.webSafeCursorString
									: null
						};

						var invoiceService = appEndpointSF.getInvoiceService();
						invoiceService
								.fetchInvoiceListByPaging(
										$scope.curUser.business.id,
										$scope.selectedStatus, pagingInfoTemp)
								.then(
										function(pagingInfoReturned) {
											$scope.pagingInfoReturned = pagingInfoReturned;
											if (pagingInfoReturned.entityList) {
												$scope.invoiceData = $scope.invoiceData
														.concat(pagingInfoReturned.entityList);
											}
											$scope.query.totalSize = pagingInfoReturned.totalEntities;
											$scope.query.pagesLoaded++;
											$scope.loading = false;
										});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchEntityListByPaging();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.invoiceData = [];
					$scope.selected = [];
					$scope.waitForServiceLoad();

					$scope.updateInvoice = function(invoiceId, status) {
						$scope.sendToUpdate = [];
						$scope.sendToUpdate.push(invoiceId);
						$scope.sendToUpdate.push(status);
						$scope.valueToUpdate = {
							id : $scope.sendToUpdate[0],
							status : $scope.sendToUpdate[1]
						};

						var invoiceService = appEndpointSF.getInvoiceService();
						invoiceService.updateInvoice($scope.valueToUpdate)
								.then(function() {
								});

						$scope.showSimpleToast();
						window.history.back();
					}

					$scope.selected = [];

					$scope.updatePaidStatus = function() {
						var paid = "Paid"
						$scope.selected[0].status = paid;
						var invoiceService = appEndpointSF.getInvoiceService();
						invoiceService.updateInvoiceStatus($scope.selected[0])
								.then(function(msgBean) {
									$scope.showSimpleToast(msgBean.msg);
								});
					}

					$scope.updateNotPaidStatus = function() {
						var notPaid = "NotPaid"
						$scope.selected[0].status = notPaid;
						var invoiceService = appEndpointSF.getInvoiceService();
						invoiceService.updateInvoiceStatus($scope.selected[0])
								.then(function(msgBean) {
									$scope.showSimpleToast(msgBean.msg);
								});
					}

					$scope.changeStarredValue = function(invoice) {

						invoice.starred = !invoice.starred;

						var invoiceService = appEndpointSF.getInvoiceService();
						invoice.modifiedBy = $scope.curUser.email_id;
						invoiceService.addInvoice(invoice).then(
								function(invoice) {

								});
					}

					$scope.getStarredInvoices = function() {

						var invoiceService = appEndpointSF.getInvoiceService();

						invoiceService.getStarredInvoices().then(
								function(list) {
									$scope.invoiceData = list;
									$scope.loading = false;
								});
					}

					$scope.printInvoice = function(invoiceId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintPdfInvoice?bid=" + bid
								+ "&invoiceId=" + invoiceId);
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
