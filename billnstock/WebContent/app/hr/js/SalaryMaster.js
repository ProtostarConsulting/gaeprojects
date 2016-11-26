angular
		.module("stockApp")
		.controller(
				"SalaryMaster",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, Upload, $mdDialog, $mdMedia, $state) {

					$scope.query = {
						order : 'empAccount.employeeDetail.empId',
						limit : 50,
						page : 1
					};
					$scope.empSalaryMasterList = [];
					$scope.getEmpList = function() {
						$scope.loading = true;
						var hrService = appEndpointSF.gethrService();
						hrService.getAllemp($scope.curUser.business.id).then(
								function(list) {
									$scope.empSalaryMasterList = list;
									$scope.loading = false;
								});

					}

					$scope.getSalaryMasterlist = function() {
						$scope.loading = true;
						var hrService = appEndpointSF.gethrService();
						hrService.getSalaryMasterlist(
								$scope.curUser.business.id).then(
								function(list) {
									$scope.empSalaryMasterList = list;
									$scope.loading = false;
								});

					}

					$scope.calSpecialAllow = function(currEmpSalMasterObj) {
						if (currEmpSalMasterObj.monthlyGrossSal !== 0) {
							currEmpSalMasterObj.monthlySpecialAllow = currEmpSalMasterObj.monthlyGrossSal
									- currEmpSalMasterObj.monthlyBasic
									- currEmpSalMasterObj.monthlyHra
									- currEmpSalMasterObj.monthlyConvence
									- currEmpSalMasterObj.monthlyMedical
									- currEmpSalMasterObj.monthlyEducation
									- currEmpSalMasterObj.monthlyAdhocAllow;
							currEmpSalMasterObj.monthlySpecialAllow = currEmpSalMasterObj.monthlySpecialAllow
									.toFixed(2);
							currEmpSalMasterObj.monthlySpecialAllow = Number(currEmpSalMasterObj.monthlySpecialAllow);
						}
					}

					$scope.grossSalaryChanged = function(currEmpSalMasterObj) {
						if (currEmpSalMasterObj.monthlyGrossSal >= 11650) {
							// because all below are equal to 11650, which is
							// standard for suruchi dairy
							currEmpSalMasterObj.monthlyBasic = 5200;
							currEmpSalMasterObj.monthlyHra = 3000;
							currEmpSalMasterObj.monthlyConvence = 2000;
							currEmpSalMasterObj.monthlyMedical = 1250;
							currEmpSalMasterObj.monthlyEducation = 200;
							currEmpSalMasterObj.monthlyAdhocAllow = 0;

							$scope.calSpecialAllow(currEmpSalMasterObj);
						}
					}

					$scope.getHighlightRedStyle = function(spValue) {
						return {
							color : spValue > 0 ? '' : 'red'
						};
					}

					$scope.saveSalaryMasterDetailList = function() {
						var hrService = appEndpointSF.gethrService();
						hrService.saveSalaryMasterDetailList({
							'list' : $scope.empSalaryMasterList
						}).then(function() {
							$scope.showAddToast();
						});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getSalaryMasterlist();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.download = function() {
						// window.open("DownloadSalaryMaster?id="+$scope.curUser.business.id+d);
						document.location.href = "DownloadSalaryMaster?id="
								+ $scope.curUser.business.id;
					}
					$log.debug("id" + $scope.curUser.business);

					$scope.UplodeExcel = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : DialogController,
											templateUrl : 'app/hr/setup_UploadExcelSalaryMaster.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
												refreshSalaryMasterlist : $scope.getSalaryMasterlist
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
					function DialogController($scope, $mdDialog, curUser, refreshSalaryMasterlist) {

						$scope.fileObject;
						$scope.uploadProgressMsg = null;

						$scope.uploadFile = function() {
							$scope.loading = true;
							var fileObject = $scope.fileObject;
							Upload
									.upload({
										url : '/UploadSalaryMasterServlet',
										data : {
											'file' : fileObject,
											'businessId' : curUser.business.id
										}
									})
									.then(
											function(resp) {
												$log
														.debug('Successfully uploaded '
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
																		'Salary Master Uploaded Sucessfully.')
																.position("top")
																.hideDelay(3000));
												// Load the list again in the
												// end
												refreshSalaryMasterlist();

												$scope.fileObject = null;
												$timeout(function() {
													$scope.cancel();
												}, 1000);
												
												
											},
											function(resp) {
												$log
														.debug('Error Ouccured, Error status: '
																+ resp.status);
												$scope.uploadProgressMsg = 'Error: '
														+ resp.status;
											},
											function(evt) {
												var progressPercentage = parseInt(100.0
														* evt.loaded
														/ evt.total);
												$log
														.debug('Upload progress: '
																+ progressPercentage
																+ '% '
																+ evt.config.data.file.name);
												$scope.uploadProgressMsg = 'Upload progress: '
														+ progressPercentage
														+ '% '
														+ evt.config.data.file.name;
												+'...'

												/*
												 * if(progressPercentage ==
												 * 100%){ }
												 */
											});
						};

						$scope.cancel = function() {
							$mdDialog.cancel();
						};
					}

					$scope.Refreshpage = function() {

						$scope.getSalaryMasterlist();
					}

				});
