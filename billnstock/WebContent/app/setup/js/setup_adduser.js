angular
		.module("stockApp")
		.controller(
				"setup.adduser",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, $filter, objectFactory,
						$mdMedia, $mdDialog, Upload, appEndpointSF) {

					$scope.selectedBusiness = $stateParams.selectedBusiness ? $stateParams.selectedBusiness
							: $scope.curuser.business;

					// ------UPLODE EXCEL FILE----
					$scope.UplodeExcel = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : DialogController,
											templateUrl : '/app/setup/setup_UploadExcelAddUsers.html',
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

					};

					function DialogController($scope, $mdDialog, curUser) {

						$scope.fileObject;
						$scope.uploadProgressMsg = null;

						$scope.uploadFile = function() {
							$scope.loading = true;
							var fileObject = $scope.fileObject;
							Upload
									.upload({
										url : '/UploadUsersServlet',
										data : {
											'file' : fileObject,
											'username' : curUser.email_id,
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

					// -------------------------------------------------------
					// ----------hide and show ---------------------------

					$scope.IsHidden = true;
					$scope.ShowHide = function() {
						$scope.IsHidden = $scope.IsHidden ? false : true;
					}
					// -----------------end send mail-------------------

					$scope.curuser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							// $scope.getBusinessById();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					// set toggled value onclick on check box and push in
					// selection array only tru or false value
					$scope.toggleSelection = function toggleSelection(index) {
						$scope.selection[index] = !$scope.selection[index];
					};

					$scope.condition = function() {
						if ($scope.user.isGoogleUser == false) {
							return true;
						} else {
							return false
						}
					}
					// -------------------------------------------------------------------------

					$scope.user = {
						business : $scope.user.business = $scope.selectedBusiness,
						email_id : "",
						firstName : "",
						lastName : "",
						password : "",
						isGoogleUser : true,
						isLoginAllowed : true,
						authority : [],
						employeeDetail : {
							department : null,
							phone1 : "",
							phone2 : "",
							bankDetail : {
								bankName : "",
								bankIfscCode : "",
								branchName : "",
								bankAccountNo : ""
							}
						}
					}

					$scope.adduser = function() {
						$scope.user.business = $scope.selectedBusiness;

						var UserService = appEndpointSF.getUserService();
						UserService.addUser($scope.user).then(
								function(msgBean) {
									if (msgBean.id) {
										$scope.showAddToast();

										$scope.addform.$setPristine();
										$scope.addform.$setValidity();
										$scope.addform.$setUntouched();
									}
								});

					}

					$scope.getDepartmentList = function() {
						var UserService = appEndpointSF.getUserService();
						UserService.getEmpDepartments(
								$scope.selectedBusiness.id).then(
								function(list) {
									if (list.items) {
										$scope.departmentList = list.items;
										$scope.departmentList = $filter(
												'proOrderObjectByTextField')(
												$scope.departmentList, "name");
									}
								});
					}

					$scope.getDepartmentList();
					$scope.user11 = [];
					$scope.userexist = "";
				});
