angular
		.module("prostudyApp")
		.controller(
				"courierDailyDispatchReportCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $q, $mdDialog, $mdMedia, $state,
						appEndpointSF) {
					console.log("Inside courierDailyDispatchReportCtr");
					
					$scope.loading = true;
					$scope.courierDispatchDate;
					$scope.couriertFilteredList = [];
					$scope.query = {
						order : 'description',
						limit : 5,
						page : 1
					};
					var printDivCSS = new String(
							'<link href="/lib/base/css/angular-material.min.css"" rel="stylesheet" type="text/css">'
									+ '<link href="/lib/base/css/bootstrap.min.css"" rel="stylesheet" type="text/css">')
					
					$scope.printDiv = function(bookDetailDiv) {

						/*
						 * document.getElementById('hidetr').style.display =
						 * 'block';
						 */
						window.frames["print_frame"].document.body.innerHTML = printDivCSS
								+ document.getElementById(bookDetailDiv).innerHTML;
						window.frames["print_frame"].window.focus();
						/*
						 * document.getElementById('hidetr').style.display =
						 * 'none';
						 */
						window.frames["print_frame"].window.print();

					}

					$scope.getGFCourierByInstitute = function(refresh) {

						var gfCourierService = appEndpointSF
								.getGFCourierService();
						gfCourierService.getGFCourierByInstitute(
								$scope.curUser.instituteID).then(
								function(gfCouriertList) {
									$scope.gfCouriertList = gfCouriertList;

								});
					}

					$scope.getCuriertFilteredList = function() {

						$scope.loading = true;
						$scope.couriertFilteredList = [];
						$scope.couriertFilteredList = $scope.gfCouriertList
						$scope.loading = false;

						
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getGFCourierByInstitute();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					$scope.refresh = function() {
						$state.reload();
					}

				});