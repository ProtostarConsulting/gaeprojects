angular
		.module("stockApp")
		.controller(
				"setup.adduser",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory, $mdMedia,
						$mdDialog, Upload, appEndpointSF) {
					// ////////////////////////////////////////////////////////////////////////////////////////////////
					$scope.showSimpleToast = function(msgBean) {
						$mdToast.show($mdToast.simple().content(msgBean)
								.position("top").hideDelay(3000));
					};

					$scope.businessNo = $stateParams.businessNo;
					$scope.id;

					// ----------------------UPLODE EXCEL
					// FILE-------------------------------

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

					$scope.items = [ "customer", "account", "stock",
							"salesOrder", "purchaseOrder", "invoice",
							"warehouse", "hr", "crm", "employee", "admin" ];
					$scope.selection = [];

					$scope.curuser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.BankDetail = {
						bankName : "",
						bankIfscCode : "",
						branchName : "",
						bankAccountNo : "",

					}
					// use to set all item false to set
					for ( var item in $scope.items) {
						$scope.selection.push($scope.curuser.authority[0]
								.indexOf(item) > -1);
					}

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
						bankDetail : "",
						business : "",
						email_id : "",
						firstName : "",
						lastName : "",
						password : "",
						isGoogleUser : true,
						authority : []
					}

					$scope.checkDuplicateAndAddUser = function() {
						var UserService = appEndpointSF.getUserService();
						UserService
								.isUserExists($scope.curuser.business.id,
										emailid)
								.then(
										function(responce) {
											if (responce.returnBool == false) {
												$scope.adduser();
											} else {
												$scope.userexists = "This user already exists.";
											}
										});
					}

					$scope.adduser = function() {
						$scope.user.business = $scope.curuser.business;
						$scope.user.bankDetail = $scope.BankDetail;
						// use selection array true false value and push that
						// numbered item on authority
						$scope.user.authority = [];
						for (var i = 0; i < $scope.selection.length; i++) {
							if ($scope.selection[i])
								$scope.user.authority.push($scope.items[i]);
						}

						var setupService = appEndpointSF.getsetupService();
						if (typeof $scope.curuser.business.id != 'undefined') {
							setupService
									.getAllUserOfOrg($scope.curuser.business.id)
									.then(
											function(users) {
												$scope.userslist = users.items.length;
												if ($scope.userslist < $scope.curuser.business.accounttype.maxuser) {

													var UserService = appEndpointSF
															.getUserService();
													UserService
															.addUser(
																	$scope.user)
															.then(
																	function(
																			msgBean) {
																		$scope
																				.showAddToast();

																	});

													$scope.user = {};
												} else {
													$scope
															.showSimpleToast("userlimit is low");
												}

											});

						}

						$scope.addform.$setPristine();
						$scope.addform.$setValidity();
						$scope.addform.$setUntouched();

					}

					// -------------------Check
					// email------------------------------------
					$scope.Checkemail = function(emailid) {

						var UserService = appEndpointSF.getUserService();
						UserService
								.isUserExists($scope.curuser.business.id,
										emailid)
								.then(
										function(responce) {
											if (responce.returnBool == true) {
												$scope.userexists = "This user already exists.";
												$scope.user.firstName = "";
												$scope.user.lastName = "";
												angular
														.element(document
																.getElementById('fname'))[0].disabled = true;
												angular
														.element(document
																.getElementById('lname'))[0].disabled = true;
											} else {
												$scope.userexists = "";
												angular
														.element(document
																.getElementById('fname'))[0].disabled = false;
												angular
														.element(document
																.getElementById('lname'))[0].disabled = false;

											}

										});

					}
					$scope.user11 = [];
					$scope.userexist = "";

					$scope.toggleRight = buildToggler('right');

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
