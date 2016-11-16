angular
		.module("stockApp")
		.controller(
				"SalaryMaster",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, Upload,$mdDialog, $mdMedia, $state) {

					$scope.flag = false;
					$scope.neg = false;

					$scope.getEmptySalaryMaster = function(emp) {
						return {
							empAccount : emp,
							grosssal : 0,
							basic : 0,
							hramonthly : 0,
							convence : 0,
							medical : 0,
							education : 0,
							adhocAllow : 0,
							specialAllow : 0,
							calGrossTotal : 0,
							business : $scope.curUser.business

						};
					}

					$scope.empSalaryMasterList = [];
					$scope.getEmpList = function() {						
						$scope.loading = true;
						var hrService = appEndpointSF.gethrService();
						hrService
								.getAllemp($scope.curUser.business.id)
								.then(
										function(list) {
											
											$scope.empSalaryMasterList.length = 0;
											for (var i = 0; i < list.length; i++) {

												$scope.empSalaryMasterList
														.push($scope
																.getEmptySalaryMaster(list[i]));

											}
											$scope.loading = false;

										});

					}

					$scope.getSalaryMasterlist = function() {
						$scope.loading = true;	
						var hrService = appEndpointSF.gethrService();
						hrService.getSalaryMasterlist(
								$scope.curUser.business.id).then(
								function(list) {
									if (list.length == 0) {
										$scope.getEmpList();
									} else
										$scope.empSalaryMasterList.length = 0;
									for (var i = 0; i < list.length; i++) {
										// if(list[i].grosssal==0)
										// {$scope.empSalaryMasterList.push($scope.getEmptySalaryMaster(list[i]));}
										$scope.empSalaryMasterList
												.push(list[i]);
										// $scope.calculation( i );
									}
									$log.debug("items: "
											+ $scope.curUser.business.items);
									$scope.loading = false;	

								});

					}

					$scope.calSpecialAllow = function(index) {
						var currEmpSalMasterObj = $scope.empSalaryMasterList[index];
						if (currEmpSalMasterObj.grosssal !== 0) {
							currEmpSalMasterObj.specialAllow = currEmpSalMasterObj.grosssal
									- currEmpSalMasterObj.basic
									- currEmpSalMasterObj.hramonthly
									- currEmpSalMasterObj.convence
									- currEmpSalMasterObj.medical
									- currEmpSalMasterObj.education
									- currEmpSalMasterObj.adhocAllow;
							currEmpSalMasterObj.specialAllow = currEmpSalMasterObj.specialAllow.toFixed(2);
						}
					}

					$scope.grossSalaryChanged = function(index) {
						var currEmpSalMasterObj = $scope.empSalaryMasterList[index];
						if (currEmpSalMasterObj.grosssal >= 11650) {
							// because all below are equal to 11650, which is
							// standard for suruchi dairy
							currEmpSalMasterObj.basic = 5200;
							currEmpSalMasterObj.hramonthly = 3000;
							currEmpSalMasterObj.convence = 2000;
							currEmpSalMasterObj.medical = 1250;
							currEmpSalMasterObj.education = 200;

							$scope.calSpecialAllow(index);
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

					$scope.getSalaryMasterlist();
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
												curUser : $scope.curUser
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

					};function DialogController($scope, $mdDialog, curUser) {

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
																		'User List Uploaded Sucessfully.')
																.position("top")
																.hideDelay(3000));

												$scope.fileObject = null;
												$timeout(function() {
													$scope.cancel();
												}, 3000);
												// Load the books again in the
												// end
												// getFreshBooks(true);
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
