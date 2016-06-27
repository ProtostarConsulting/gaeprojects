angular
		.module("prostudyApp")
		.controller(
				"printBookDtailCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, appEndpointSF, $state, $sce,
						$stateParams, $q, $mdDialog, $mdMedia) {

					$scope.selectedPSchoolId = $stateParams.PSchoolId;
					$scope.yearOfExam = $stateParams.yearOfExam;
					$scope.date = new Date();

					$log.debug("$scope.yearOfExam=" + $scope.yearOfExam);

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					var printDivCSS = new String(
							'<link href="/lib/base/css/angular-material.min.css"" rel="stylesheet" type="text/css">'
									+ '<link href="/lib/base/css/bootstrap.min.css"" rel="stylesheet" type="text/css">')

					$scope.printSalSlipDiv = function(bookDetailDiv) {

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

					$scope.getPSchoolByPSID = function() {
						var PartnerSchoolService = appEndpointSF
								.getPartnerSchoolService();
						if ($scope.selectedPSchoolId != "") {
							PartnerSchoolService
									.getPSchoolByPSID($scope.selectedPSchoolId)
									.then(
											function(pSchool) {
												$scope.examList = pSchool.examDetailList;
												$scope.add = pSchool.address;
												$scope.ContactDetail = pSchool.contactDetail;
												$scope.school = pSchool;
												$scope.getPrintDetail();
											});
						}
					}
					$scope.examList = [];
					$scope.ContactDetail;
					$scope.add;
					$scope.school;

					$scope.getPrintDetail = function() {

						for (i = 0; i < $scope.examList.length; i++) {
							if ($scope.examList[i].yearOfExam == $scope.yearOfExam) {

								$scope.bookSummary = $scope.examList[i].bookSummary;
								$scope.BookDetail = $scope.examList[i].bookSummary.bookDetail;
								$scope.PaymentDet = $scope.examList[i].paymentDetail;
							}
						}

					}
					$scope.bookSummary;
					$scope.BookDetail = [];
					$scope.PaymentDet = [];

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							if ($scope.selectedPSchoolId != undefined) {
								$scope.getPSchoolByPSID();
								$scope.getPrintDetail();
							}

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					$scope.getGFBookStockByInstituteId = function() {
						var gfBookStockService = appEndpointSF
								.getGFBookStockService();
						gfBookStockService.getGFBookByInstituteId(
								$scope.curUser.instituteID).then(
								function(tempBooks) {
									$scope.bookStocks = tempBooks;
								});
					}
					$scope.bookStocks = [];

					$scope.waitForServiceLoad1 = function() {
						if (appEndpointSF.is_service_ready) {

							$scope.getGFBookStockByInstituteId();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad1, 1000);
						}
					}

					$scope.waitForServiceLoad1();
					
					
					
					

				});