angular
		.module("stockApp")
		.controller(
				"probusinessCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF) {

					$scope.businessNo = $stateParams.businessNo;
					$scope.userid = $stateParams.userid;
					$scope.businessName = $stateParams.businessName;
					$scope.BNo = $stateParams.BNo;
					$scope.selecteduserNo = $stateParams.selecteduserNo;

					$scope.showSimpleToast = function(msgBean) {
						$mdToast.show($mdToast.simple().content(msgBean)
								.position("top").hideDelay(3000));
					};

					$scope.getBusinessList = function() {
						$log.debug("Inside Ctr $scope.getuserById");
						var UserService = appEndpointSF.getUserService();
						UserService.getBusinessList().then(
								function(businessList) {
									$log.debug("Inside Ctr getAllleads");
									$scope.businesslist = businessList.items;

								});

					}

					$scope.businesslist = [];
					$scope.getBusinessList();

					$scope.selected = [];
					
					$scope.updateBusiStatus = function(selected,stateValue) {
						
						$scope.update.push(selected);
						$scope.selected.status = stateValue;
						
					//	$scope.update.id = selected.id;
					//	$scope.update.status = stateVale;
						
						var UserService = appEndpointSF.getUserService();

			//			$scope.user.businessAccount = $scope.business;
			//			$scope.user.authority = $scope.selection;
						UserService.updateBusiStatus($scope.selected[0]).then(
								function(msgBean) {
									$scope.showSimpleToast(msgBean.msg);

								});

					}
					$scope.getUsersByBusinessId = function() {
						$log.debug("Inside Ctr $scope.getuserById");
						var UserService = appEndpointSF.getUserService();
						UserService.getUsersByBusinessId($scope.businessNo)
								.then(function(userList) {
									$log.debug("Inside Ctr getAllleads");
									$scope.userlist = userList.items;

								});

					}

					$scope.userlist = [];
					$scope.getUsersByBusinessId();

					// -------------------------------------------------------------------------------------
					$scope.user = {
						businessAccount : "",
						email_id : "",
						firstName : "",
						lastName : "",
						password : "",
						isGoogleUser : true,
						authority : []
					}
					$scope.items = [ "stock", "sales", "hr", "crm", "customer",
							"invoice", "purchase", "employee", "admin" ];
					$scope.selection = [];
					$scope.toggleSelection = function toggleSelection(itemName) {
						var idx = $scope.selection.indexOf(itemName);

						// is currently selected
						if (idx > -1) {
							$scope.selection.splice(idx, 1);
						}

						// is newly selected
						else {
							$scope.selection.push(itemName);
						}

						$log.debug("$scope.selection===" + $scope.selection);

					};

					$scope.getbusinessById = function() {
						var UserService = appEndpointSF.getUserService();
						if (typeof $scope.BNo != 'undefined') {
							UserService
									.getbusinessById($scope.BNo)
									.then(
											function(busi) {
												$scope.business = busi.result;

												if ($scope.business.accounttype.maxuser == $scope.business.totalUser - 1) {
													$("#hideSpan").show();
													$log.debug("#hideSpan");
												} else {
													$("#hideSpan").hide();
													$("#hideDiv").hide();
												}

											});
						}

					}
					$scope.getbusinessById();

					$scope.adduser = function() {
						var UserService = appEndpointSF.getUserService();
						/*
						 * UserService
						 * .getbusinessById($scope.BNo).then(function(busi) {
						 */
						$scope.user.businessAccount = $scope.business;
						$scope.user.authority = $scope.selection;
						UserService.addUser($scope.user).then(
								function(msgBean) {
									$scope.showSimpleToast(msgBean.msg);

								});
						// });

					}

					$scope.getuserById = function() {
						$log.debug("Inside Ctr $scope.getuserById");
						var setupService = appEndpointSF.getsetupService();
						if (typeof $scope.selecteduserNo != 'undefined') {
							setupService.getuserById($scope.selecteduserNo)
									.then(function(userList) {
										$log.debug("Inside Ctr getAllleads");
										$scope.userL = userList.result;

									});
						}
					}

					$scope.userL = [];
					$scope.getuserById();

					$scope.updateuser = function() {
						$scope.userL.authority = $scope.selection;
						// $scope.userL.password= $scope.password;
						var UserService = appEndpointSF.getUserService();
						UserService.updateUser($scope.userL).then(
								function(msgBean) {
									$scope.showSimpleToast(msgBean.msg);
								});
					}

					// -----------------------------------------------------------------------------------------------
					$scope.back = function() {
						window.history.back();
						// $state.go("^", {});
					};
					// ----------hide and show ---------------------------

					$scope.IsHidden = true;
					$scope.ShowHide = function() {
						$scope.IsHidden = $scope.IsHidden ? false : true;
					}
					// -----------------------------------------------------

					$scope.condition = function() {
						if ($scope.user.isGoogleUser == false) {
							return true;
						} else {
							return false
						}
					}

					// check email already exists

					$scope.Checkemail = function(emailid) {
						var proadminService = appEndpointSF
								.getproadminService();
						proadminService
								.getAllemp()
								.then(
										function(empList) {
											$scope.user11 = empList.items;
											for (i = 0; i < $scope.user11.length; i++) {
												if ($scope.user11[i].email_id == emailid) {
													$scope.userexists = "user already exists"
													angular
															.element(document
																	.getElementById('fname'))[0].disabled = true;
													angular
															.element(document
																	.getElementById('lname'))[0].disabled = true;
													break;

												} else {
													$scope.userexists = "";
													angular
															.element(document
																	.getElementById('fname'))[0].disabled = false;
													angular
															.element(document
																	.getElementById('lname'))[0].disabled = false;

												}
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
