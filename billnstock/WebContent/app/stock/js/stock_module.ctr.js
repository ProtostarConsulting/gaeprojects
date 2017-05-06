angular
		.module("stockApp")
		.controller(
				"stockModuleCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, objectFactory, appEndpointSF, $mdMedia,
						$mdDialog) {
					$log.debug("Inside stockModuleCtr");

					var originatorEv;
					$scope.openPurchaseMenu = function($mdOpenMenu, ev) {
						originatorEv = ev;
						$mdOpenMenu(ev);
					};

					$scope.openSalesMenu = function($mdOpenMenu, ev) {
						originatorEv = ev;
						$mdOpenMenu(ev);
					};

					$scope.viewPOsDueToday = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : viewPOsDueTodayCtr,
											templateUrl : '/app/stock/view_purchase_orders_due_today_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
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

					function viewPOsDueTodayCtr($scope, $mdDialog, curUser) {

						$scope.purchaseOrders = [];

						$scope.today = new Date();

						$scope.purchaseOrderFilterWrapper = {
							businessId : curUser.business.id,
							toDate : $scope.today,
						}

						$scope.viewPurchaseOrders = function() {

							var stockService = appEndpointSF.getStockService();

							stockService.getPurchaseOrdersDueToday(
									$scope.purchaseOrderFilterWrapper).then(
									function(purchaseOrders) {
										$scope.purchaseOrders = purchaseOrders;
									});

						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};

						$scope.viewPurchaseOrders();

					}

					$scope.viewPOsDueInThreeDays = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : viewPOsDueInThreeDaysCtr,
											templateUrl : '/app/stock/view_po_by_next_three_days_due_date_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
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

					function viewPOsDueInThreeDaysCtr($scope, $mdDialog,
							curUser) {

						$scope.purchaseOrders = [];

						$scope.today = new Date();

						$scope.threeDaysFromNow = new Date();

						$scope.threeDaysFromNow
								.setDate($scope.today.getDate() + 3);

						$scope.purchaseOrderFilterWrapper = {
							businessId : curUser.business.id,
							fromDate : $scope.today,
							toDate : $scope.threeDaysFromNow,
						}

						$scope.viewPurchaseOrders = function() {

							var stockService = appEndpointSF.getStockService();

							stockService.getPurchaseOrdersBetweenDates(
									$scope.purchaseOrderFilterWrapper).then(
									function(purchaseOrders) {
										$scope.purchaseOrders = purchaseOrders;
									});
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};

						$scope.viewPurchaseOrders();

					}

					$scope.viewPOsDueInSevenDays = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : viewPOsDueInSevenDaysCtr,
											templateUrl : '/app/stock/view_po_by_next_seven_days_due_date_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
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

					function viewPOsDueInSevenDaysCtr($scope, $mdDialog,
							curUser) {

						$scope.purchaseOrders = [];

						$scope.today = new Date();

						$scope.sevenDaysFromNow = new Date();

						$scope.sevenDaysFromNow
								.setDate($scope.today.getDate() + 7);

						$scope.purchaseOrderFilterWrapper = {
							businessId : curUser.business.id,
							fromDate : $scope.today,
							toDate : $scope.sevenDaysFromNow,
						}

						$scope.viewPurchaseOrders = function() {

							var stockService = appEndpointSF.getStockService();

							stockService.getPurchaseOrdersBetweenDates(
									$scope.purchaseOrderFilterWrapper).then(
									function(purchaseOrders) {
										$scope.purchaseOrders = purchaseOrders;
									});
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};

						$scope.viewPurchaseOrders();

					}

					$scope.viewTotalPOsFinalized = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : viewPOsNotClosedCtr,
											templateUrl : '/app/stock/view_total_po_finalized_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
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

					function viewPOsNotClosedCtr($scope, $mdDialog, curUser) {

						$scope.purchaseOrders = [];
						$scope.viewPurchaseOrders = function() {

							var stockService = appEndpointSF.getStockService();

							stockService
									.getFinalizedPurchaseOrdersForDashboard(
											curUser.business.id)
									.then(function(purchaseOrders) {
										$scope.purchaseOrders = purchaseOrders;
									});
						}

						$scope.cancel = function() {
							$mdDialog.cancel();
						};

						$scope.viewPurchaseOrders();

					}

					$scope.announceClick = function(index) {
						$log.debug('You clicked the menu item at index '
								+ index);
						originatorEv = null;
					};

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