angular
		.module("stockApp")
		.controller(
				"setup.adduser",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,$mdMedia,$mdDialog,
						appEndpointSF) {
					// ////////////////////////////////////////////////////////////////////////////////////////////////
					$scope.showSimpleToast = function(msgBean) {
						$mdToast.show($mdToast.simple().content(msgBean)
								.position("top").hideDelay(3000));
					};

					$scope.businessNo = $stateParams.businessNo;
					$scope.id;				
					

					
					// ----------------------UPLODE EXCEL FILE-------------------------------

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
												curuser : $scope.curuser,
												business:$scope.business
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

					function DialogController($scope, $mdDialog, curuser,business) {
						$scope.bizID;
						$scope.loding=false;
						$scope.uplodeimage=function(){
							$scope.loding=true;
							 document.excelform.action = $scope.ExcelUploadURL;
						      document.excelform.submit();
						}
						
						
						$scope.getExcelUploadURL=function(){
							var uploadUrlService = appEndpointSF.getuploadURLService();
							uploadUrlService.getExcelUploadURL()
									.then(function(url) {
										$scope.ExcelUploadURL=url.msg;
										$scope.bizID = business.id;
									});
							
							
						}
						$scope.ExcelUploadURL;
						
						$scope.waitForServiceLoad = function() {
							if (appEndpointSF.is_service_ready) {
								$scope.getExcelUploadURL();
							} else {
								$log.debug("Services Not Loaded, watiting...");
								$timeout($scope.waitForServiceLoad, 1000);
							}
						}
						$scope.waitForServiceLoad();
						}

					// -------------------------------------------------------
					
					
					
					//----------hide and show ---------------------------

					$scope.IsHidden = true;
					$scope.ShowHide = function() {
						$scope.IsHidden = $scope.IsHidden ? false : true;
					}
				      //-----------------end send mail-------------------

					$scope.items = [ "customer", "account", "stock",
							"salesOrder", "purchaseOrder", "invoice",
							"warehouse", "hr", "crm", "employee", "admin" ];
					$scope.selection = [];

		
					$scope.curuser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

		
					$scope.BankDetail={
						bankName:"",
						bankIfscCode:"",	
						branchName:"",
						bankAccountNo:"",
								
					}
					// use to set all item false to set
					for ( var item in $scope.items) {
						$scope.selection.push($scope.curuser.authority[0]
								.indexOf(item) > -1);
					}

					$scope.getBusinessById = function() {
						if (typeof $scope.businessNo == "undefined") {
							$scope.Bid = $scope.curuser.business.id;
						} else {
							$scope.Bid = $scope.businessNo;
						}
						var UserService = appEndpointSF.getUserService();
						UserService
								.getbusinessById($scope.Bid)
								.then(
										function(Business) {
											$scope.business = Business;
											$scope.id = $scope.business.id;
											if ($scope.business.accounttype.maxuser == $scope.business.totalUser - 1) {
												$("#hideSpan").show();
												$log.debug("#hideSpan");
											} else {
												$("#hideSpan").hide();
												$("#hideDiv").hide();
											}

										});

					}
					$scope.business = {};
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getBusinessById();
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
						bankDetail:"",
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
								.isUserExists(emailid)
								.then(
										function(responce) {
											if (responce.result.returnBool == false) {
												$scope.adduser();
											} else {
												$scope.userexists = "user already exists";
											}
										});
					}

					$scope.adduser = function() {
						$scope.user.business = $scope.business;
						$scope.user.bankDetail=$scope.BankDetail;
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

													var UserService = appEndpointSF.getUserService();
													UserService.addUser($scope.user)
															.then(function(msgBean) {
																$scope.showAddToast();
																
													
																
																
																
																	});
													
													$scope.user = {};
												} else {
													$scope.showSimpleToast("userlimit is low");
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
								.isUserExists(emailid)
								.then(
										function(responce) {
											if (responce.result.returnBool == true) {
												$scope.userexists = "user already exists";
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
