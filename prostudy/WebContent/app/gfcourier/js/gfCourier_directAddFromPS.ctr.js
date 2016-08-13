angular
		.module("prostudyApp")
		.controller(
				"gfCourierDirectAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $q, appEndpointSF, $state, $stateParams,
						$mdDialog, objectFactory, logisticsList,
						courierTypelist) {

					$scope.logisticsList = logisticsList;
					$scope.courierTypelist = courierTypelist;
					$scope.partnerSchool = {
						courierType : '',
						logistics : '',
						registrationID : '',
						weight : '',
						courierFrom : "Gandhi Foundation",
						courierTo : '',
						schoolName : '',
						courierDispatchDate : new Date(),
						bookQty : 0,
						bookLineItemList : [],
						otherLineItemList : []
					}
					$scope.partnerSchoolNew = {
						bookLineItemList : []
					};
					$scope.tempPartnerSchool = {
						examDetailList : ''
					};

					$scope.yearOfExam = $stateParams.yearOfExam;
					$scope.partnerSchool = $stateParams.partnerSchool;
					$log.debug("$scope.partnerSchool=="
							+ angular.toJson($scope.partnerSchool));

					if ($scope.yearOfExam != undefined
							|| $scope.partnerSchool != undefined) {
						$scope.partnerSchool.courierTo = $scope.partnerSchool.schoolName
								+ ", "
								+ $scope.partnerSchool.address.line1
								+ ", "
								+ $scope.partnerSchool.address.city
								+ ", "
								+ $scope.partnerSchool.address.state
								+ ", "
								+ "PIN-"
								+ $scope.partnerSchool.address.pin;

						$scope.partnerSchool.courierFrom = $scope.curUser.instituteObj.name
								+ ", "
								+ $scope.curUser.instituteObj.address.line1;
					}

					$scope.addGFCourier = function() {
						$scope.partnerSchoolNew.instituteID = $scope.curUser.instituteID;

						$scope.partnerSchoolNew.schoolName = $scope.pSchool;
						$scope.partnerSchoolNew.govRegisterno = $scope.partnerSchool.govRegisterno;
						$scope.partnerSchoolNew.autoGenerated = $scope.partnerSchool.autoGenerated;
						$scope.partnerSchoolNew.courierDispatchDate = new Date();
						$scope.partnerSchoolNew.courierFrom = $scope.partnerSchool.courierFrom;
						$scope.partnerSchoolNew.courierTo = $scope.partnerSchool.courierTo;
						$scope.partnerSchoolNew.courierType = $scope.partnerSchool.courierType;
						$scope.partnerSchoolNew.logistics = $scope.partnerSchool.logistics;

						for (var i = 0; i < $scope.tempPartnerSchool.examDetailList.bookSummary.bookDetail.length; i++) {
							for (var j = 0; j < $scope.bookStocks.length; j++) {
								if ($scope.tempPartnerSchool.examDetailList.bookSummary.bookDetail[i].bookName == $scope.bookStocks[j].id) {
									$scope.bookStocks[j].bookQty = $scope.tempPartnerSchool.examDetailList.bookSummary.bookDetail[i].totalStud;
									$scope.weight = $scope.weight
											+ $scope.bookStocks[j].weight
											* $scope.tempPartnerSchool.examDetailList.bookSummary.bookDetail[i].totalStud;
									$scope.partnerSchoolNew.bookLineItemList
											.push($scope.bookStocks[j]);

								}
							}
						}

						var gfCourierService = appEndpointSF
								.getGFCourierService();

						gfCourierService.addGFCourier($scope.partnerSchoolNew)
								.then(function() {
								});
						$scope.showAddToast();

						$state.go('courierModule.list', {});
					}

					$scope.getSchoolByAutoGeneratedID = function() {

						var PartnerService = appEndpointSF
								.getPartnerSchoolService();
						PartnerService
								.getSchoolByAutoGeneratedID(
										$scope.partnerSchool.autoGenerated)
								.then(
										function(pSchool) {
											$scope.pSchool = pSchool;

											$scope.partnerSchool.courierFrom = $scope.curUser.instituteObj.name
													+ ", "
													+ $scope.curUser.instituteObj.address.line1;

											if ($scope.partnerSchool.examDetailList != undefined) {

												for (var i = 0; i < $scope.partnerSchool.examDetailList.length; i++) {

													if ($scope.partnerSchool.examDetailList[i].yearOfExam == $scope.yearOfExam) {
														$scope.tempPartnerSchool.examDetailList = $scope.partnerSchool.examDetailList[i];
													}
												}

												$scope.partnerSchoolNew.totalFees = $scope.tempPartnerSchool.examDetailList.bookSummary.amtForGRF80per;
												$scope.partnerSchoolNew.totalWeight = 0;

												for (var i = 0; i < $scope.tempPartnerSchool.examDetailList.bookSummary.bookDetail.length; i++) {
													for (var j = 0; j < $scope.bookStocks.length; j++) {

														if ($scope.tempPartnerSchool.examDetailList.bookSummary.bookDetail[i].bookName == $scope.bookStocks[j].id) {

															$scope.partnerSchoolNew.totalWeight = ($scope.partnerSchoolNew.totalWeight)
																	+ ($scope.bookStocks[j].weight * $scope.tempPartnerSchool.examDetailList.bookSummary.bookDetail[i].totalStud);

															/*
															 * $scope.partnerSchoolNew.totalFees =
															 * $scope.partnerSchoolNew.totalFees +
															 * $scope.tempPartnerSchool.examDetailList.bookSummary.bookDetail[i].totalFees;
															 */
														}
													}
												}
											}
										});
					}

					$scope.getGFBookStockByInstituteId = function() {
						var gfBookStockService = appEndpointSF
								.getGFBookStockService();
						gfBookStockService.getGFBookByInstituteId(
								$scope.curUser.instituteID).then(
								function(tempBooks) {
									$scope.bookStocks = tempBooks;
									$scope.getSchoolByAutoGeneratedID();
								});
					}
					$scope.bookStocks = [];

					$scope.waitForServiceLoad1 = function() {
						if (appEndpointSF.is_service_ready) {
							if ($scope.yearOfExam != undefined
									|| $scope.partnerSchool != undefined) {
								$scope.getGFBookStockByInstituteId();
							}
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad1, 1000);
						}
					}

					$scope.waitForServiceLoad1();

				});
