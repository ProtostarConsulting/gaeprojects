angular
		.module("stockApp")
		.controller(
				"userlist",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $mdMedia, $mdDialog, $log,
						$state, objectFactory, appEndpointSF) {

					$scope.loading = true;

					$scope.selecteduserNo = $stateParams.selecteduserNo;
					$scope.businessNo = $stateParams.businessNo;
					$scope.id;

					$scope.fabMenuData = {
						activeUsersIsOpen : [],
						inActiveUsersIsOpen : [],
						suspendedUsersIsOpen : [],
						selectedDirection : 'up',
						selectedMode : 'md-fling md-fab-top-right',
						items : [ {
							name : "Twitter",
							icon : "img/icons/twitter.svg",
							direction : "bottom"
						}, {
							name : "Facebook",
							icon : "img/icons/facebook.svg",
							direction : "top"
						}, {
							name : "Google Hangout",
							icon : "img/icons/hangout.svg",
							direction : "bottom"
						} ]
					};

					$scope.openDialog = function($event, item) {
						// Show the dialog
						$mdDialog.show({
							clickOutsideToClose : true,
							controller : function($mdDialog) {
								// Save the clicked item
								this.item = item;

								// Setup some handlers
								this.close = function() {
									$mdDialog.cancel();
								};
								this.submit = function() {
									$mdDialog.hide();
								};
							},
							controllerAs : 'dialog',
							templateUrl : 'dialog.html',
							targetEvent : $event
						});
					}

					if ($scope.businessNo == undefined) {
						$scope.Bid = $scope.curUser.business.id;
					} else {
						$scope.Bid = $scope.businessNo;
					}

					$scope.changeAuthView = function(params) {
						$state.go("setup.userauth", params);
					}

					$scope.showSimpleToast = function(msgBean) {
						$mdToast.show($mdToast.simple().content(msgBean)
								.position("top").hideDelay(3000));
					};

					$scope.getAllUserOfOrg = function() {
						var setupService = appEndpointSF.getsetupService();
						setupService
								.getAllUserOfOrg($scope.Bid)
								.then(
										function(users) {
											$scope.userslist = users.items;
											$scope.activeUsers = [];
											$scope.inActiveUsers = [];
											$scope.suspendedUsers = [];
											for (var i = 0; i < $scope.userslist.length; i++) {
												if ($scope.userslist[i].status == "active") {
													$scope.activeUsers
															.push($scope.userslist[i]);
													$scope.fabMenuData.activeUsersIsOpen
															.push(false);
												} else if ($scope.userslist[i].status == "inactive") {
													$scope.inActiveUsers
															.push($scope.userslist[i]);
													$scope.fabMenuData.inActiveUsersIsOpen
															.push(false);
												}
												if ($scope.userslist[i].status == "suspended") {
													$scope.suspendedUsers
															.push($scope.userslist[i]);
													$scope.fabMenuData.suspendedUsersIsOpen
															.push(false);
												}
											}
											$scope.loading = false;

											/*
											 * $log .debug("Active Users" +
											 * angular
											 * .toJson($scope.activeUsers));
											 * $log .debug("In-Active Users" +
											 * angular
											 * .toJson($scope.inActiveUsers));
											 * $log .debug("Suspended Users" +
											 * angular
											 * .toJson($scope.suspendedUsers));
											 */
										});

					}

					$scope.activeUsers = [];
					$scope.inActiveUsers = [];
					$scope.suspendedUsers = [];

					$scope.userslist = [];

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAllUserOfOrg();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.suspendUser = function(user) {
						var suspended = "suspended";
						var setupService = appEndpointSF.getsetupService();
						user.status = suspended;
						setupService.updateUserStatus(user).then(
								function(msgBean) {
									$scope.showSimpleToast(msgBean.msg);
									$scope.getAllUserOfOrg();
								});
					}
					$scope.activateUser = function(user) {
						var active = "active";
						var setupService = appEndpointSF.getsetupService();
						user.status = active;
						setupService.updateUserStatus(user).then(
								function(msgBean) {
									$scope.showSimpleToast(msgBean.msg);
									$scope.getAllUserOfOrg();
								});
					}

					$scope.changePassword = function(ev, user) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : DialogController,
											templateUrl : '/app/profile/changepassword.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curuser : $scope.curuser,
												user : user

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
						$scope.updatepass = function() {
							$log.debug("change pass");
						}
						// window.history.back();

					}

					function DialogController($scope, $mdDialog, curuser, user) {

						//alert(angular.toJson(user));
						$scope.hide = function() {
							$mdDialog.hide();
						};
						$scope.cancel = function() {
							$mdDialog.cancel();
						};
						$scope.answer = function(answer) {
							$mdDialog.hide(answer);
						};

						$scope.inputType1 = 'password';
						$scope.inputType2 = 'password';

						$scope.showpass1 = function() {
							if ($scope.inputType1 == 'password') {
								$scope.inputType1 = 'text';
							} else {
								$scope.inputType1 = 'password';
							}

						}
						$scope.showpass2 = function() {
							if ($scope.inputType2 == 'password') {
								$scope.inputType2 = 'text';
							} else {
								$scope.inputType2 = 'password';
							}
						}
						$scope.setpassinput1 = function() {
							$scope.inputType1 = 'password';
						}
						$scope.setpassinput2 = function() {
							$scope.inputType2 = 'password';
						}
						$scope.changepass = function() {

							if ($scope.password == $scope.confirmpassword) {
								$scope.savemsg = true;
								$scope.checkpass = false;
							} else {
								$scope.checkpass = true;
								$scope.savemsg = false;
							}

							if ($scope.savemsg == true) {
								$scope.userL = user;
								/* $scope.userL.modifiedBy=user.email_id; */
								$scope.userL.password = $scope.password;
								var UserService = appEndpointSF
										.getUserService();
								UserService
										.updateUser($scope.userL)
										.then(
												function(msgBean) {
													$scope
															.showSimpleToast(msgBean.msg);

												});

							}
						}
					}

					$scope.query = {
						order : 'id',
						limit : 10,
						page : 1
					};

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
