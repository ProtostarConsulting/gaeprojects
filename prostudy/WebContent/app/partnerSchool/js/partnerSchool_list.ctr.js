angular
		.module("prostudyApp")
		.controller(
				"partnerSchoolListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, appEndpointSF, $state, $sce,
						$stateParams, $q, $mdDialog, $mdMedia,Upload,ajsCache) {

					$scope.selectedChapterId = $stateParams.selectedChapterId;
					$scope.chapter = [];

					$scope.query = {
						order : 'name',
						limit : 5,
						page : 1
					};

					$scope.showSavedToast = function() {
						$mdToast.show($mdToast.simple().content(
								'Chapter Updated!').position("top").hideDelay(
								3000));
					};

					$scope.getNextYears = function() {
						var date = new Date();
					
						for (var i = 0; i < 3; i++) {
							var year = date.getFullYear();
							year = year.toString().substr(2, 2);

							$scope.Years.push(date.getFullYear() + "-"
									+ (Number(year) + 1));
							date.setYear(date.getFullYear() + 1);
						}	
					}

					$scope.Years = [];
					$scope.getNextYears();
					
					

					$scope.getPrvYears = function() {
						var date = new Date();
					
						for (var i = 0; i < 3; i++) {
							var year = date.getFullYear();
							year = year.toString().substr(2, 2);

							$scope.Years.push((date.getFullYear()-1) + "-"
									+ (Number(year) ));
							date.setYear(date.getFullYear() -1);
						}
						
						
						
					}
					$scope.getPrvYears();
					
					
					$scope.pSchoolList = [];
					$scope.getPartnerSchoolByInstitute = function(refresh) {

						var schoolListCacheKey = "getPartnerByInstitute";
						// Note this key has to be unique across application
						// else it will return unexpected result.
						if (!angular
								.isUndefined(ajsCache.get(schoolListCacheKey)) && !refresh) {
							$log.debug("Found List in Cache, return it.")
							$scope.schools = ajsCache
									.get(schoolListCacheKey);
							return;
						}
						
						var PartnerService = appEndpointSF
								.getPartnerSchoolService();
						PartnerService.getPartnerByInstitute(
								$scope.curUser.instituteID).then(
								function(pSchoolList) {
									$scope.pSchoolList = pSchoolList;
									
									ajsCache.put(schoolListCacheKey,
											pSchoolList);
								
									$scope.changeSchoolList();
								});
					}

					$scope.changeSchoolList = function(selectedyear) {
						$scope.schools = [];
						if (selectedyear != undefined) {
							/*
							 * var date1 = new Date(); var selectedyear =
							 * date1.getFullYear(); selectedyear =
							 * selectedyear.toString().substr(2, 2);
							 * selectedyear = date1.getFullYear() + "-" +
							 * (Number(selectedyear) + 1);
							 */

							if ($scope.pSchoolList != undefined) {
								for (p = 0; p < $scope.pSchoolList.length; p++) {
									for (q = 0; q < $scope.pSchoolList[p].examDetailList.length; q++) {

										if ($scope.pSchoolList[p].examDetailList[q].yearOfExam == selectedyear) {
											$scope.schools
													.push($scope.pSchoolList[p]);

										}
									}

								}
							}
						} else {
							if ($scope.pSchoolList != undefined) {
								for (p = 0; p < $scope.pSchoolList.length; p++) {
									$scope.schools.push($scope.pSchoolList[p]);

								}
							}

						}
					}
					$scope.schools = [];

					$scope.cancel = function() {
						$state.go('partnerSchool.listPartnerSchool');
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {

							$scope.getPartnerSchoolByInstitute();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}

					$scope.waitForServiceLoad();

					$scope.query = {
						order : 'description',
						limit : 5,
						page : 1,

					};

					$scope.downloadData = function() {

						document.location.href = "DownloadPartnerSchools?InstituteId="
								+ $scope.curUser.instituteID;

					}
	
					// ----------------------UPLODE EXCEL
					// FILE-------------------------------
					$scope.UplodeExcel = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : DialogController,
											templateUrl : '/app/partnerSchool/gfBulkSchoolsAdd.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
												getFreshScools: $scope.getPartnerSchoolByInstitute
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
					
					
					function DialogController($scope, $mdDialog, curUser, getFreshScools) {

						$scope.csvFile;
						$scope.uploadProgressMsg = null;
						$scope.uploadSchoolsCSV = function() {
							var csvFile = $scope.csvFile;
							Upload
									.upload(
											{
												url : 'UplodePartnerSchoolsExcel',
												data : {
													file : csvFile,
													'instituteId' : curUser.instituteID
												}
											})
									.then(
											function(resp) {
												$log.debug('Successfully uploaded '
																+ resp.config.data.file.name
																+ '.'
																+ angular
																		.toJson(resp.data));
												$scope.uploadProgressMsg = 'Successfully uploaded '
														+ resp.config.data.file.name
														+ '.';
												$mdToast
														.show($mdToast
																.simple()
																.content(
																		'Students Data Uploaded Sucessfully.')
																.position("top")
																.hideDelay(3000));
												
												$scope.csvFile = null;
												$timeout(function() {
													$scope.cancel();
													},3000);
												//Load the books again in the end
												
											},
											function(resp) {
												$log.debug('Error Ouccured, Error status: '
																+ resp.status);
												$scope.uploadProgressMsg = 'Error: '
														+ resp.status;
											},
											function(evt) {
												var progressPercentage = parseInt(100.0
														* evt.loaded
														/ evt.total);
												$log.debug('Upload progress: '
																+ progressPercentage
																+ '% '
																+ evt.config.data.file.name);
												$scope.uploadProgressMsg = 'Upload progress: '
														+ progressPercentage
														+ '% '
														+ evt.config.data.file.name;
												+'...'
											});
						};
						
						$scope.cancel = function() {
						    $mdDialog.cancel();
						  };

					}
					
					
					

/*					function DialogController($scope, $mdDialog, curuser) {

						$scope.insId = curuser.instituteID;
						$scope.loding = false;
						$scope.uplodeExcel = function() {
							$scope.loding = true;
							document.excelform.action = $scope.PartnerSchoolsUploadURL;
							// calling servlet action
							document.excelform.submit();
						}

						$scope.getLogUploadURL = function() {
							var uploadUrlService = appEndpointSF
									.getuploadURLService();
							uploadUrlService
									.getPartnerSchoolsUploadURL()
									.then(
											function(url) {
												$scope.PartnerSchoolsUploadURL = url.msg;
											});

						}
						$scope.PartnerSchoolsUploadURL;

						$scope.waitForServiceLoad = function() {
							if (appEndpointSF.is_service_ready) {
								$scope.getLogUploadURL();
							} else {
								$log.debug("Services Not Loaded, watiting...");
								$timeout($scope.waitForServiceLoad, 1000);
							}
						}
						$scope.waitForServiceLoad();

					}

					// -------------------------------------------------------
*/
				});