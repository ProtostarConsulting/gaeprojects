angular
		.module("stockApp")
		.controller(
				"indexCtr",
				function($scope, $rootScope, $window, $log, $q, $timeout,
						$mdToast, $mdBottomSheet, $state, $http, $location,
						$anchorScroll, $mdMedia, ajsCache, appEndpointSF) {

					$log.log("Inside indexCtr");
					$scope.loading = true;
					$scope.angular = angular;

					$scope.showUpdateToast = function() {
						$mdToast.show($mdToast.simple().content(
								'Changes Saved Successfully.').position("top")
								.hideDelay(3000));
					};

					$scope.showAddToast = function() {
						$mdToast.show($mdToast.simple().content(
								'New Record Saved Successfully.').position(
								"top").hideDelay(3000));
					};

					$scope.showShowCustomToast = function(msg) {
						$mdToast.show($mdToast.simple().content(msg).position(
								"top").hideDelay(3000));
					};

					$scope.curUser = null;
					$scope.googleUserDetails = "";
					$scope.googleUser = null;
					$scope.businessName = "";
					$scope.eid = null;
					$scope.test;
					$scope.loginClick = function() {
						$state.go("login");
					};

					// on page load firs see if user is already logged. if yes
					// getch and set values.
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					// http://localhost:8888/serve?blob-key=sC_5uJK83qYiubjEWAz5zA
					$scope.bizLogoGCSURL = null;
					$scope.logFooterURL;

					$scope.user = {
						business : "",
						email_id : "",
						firstName : "",
						lastName : "",
						password : "",
						isGoogleUser : true,
						theme : "",
						authority : []
					}

					$scope.orderByAuthOrderValue = function(auth) {
						return Number(auth.orderNumber);
					}
					$scope.login = function() {
						var UserService = appEndpointSF.getUserService();
						UserService
								.login($scope.user.email_id,
										$scope.user.password)
								.then(
										function(result) {
											if (result.items) {
												if (result.items.length > 1) {
													$scope.multiUsers = result.items;
												} else {
													var user = result.items[0];
													appEndpointSF
															.getLocalUserService()
															.saveLoggedInUser(
																	user);

													$scope.$emit(
															'customLoginEvent',
															{
																curUser : user
															});
													$scope.$broadcast(
															'customLoginEvent',
															{
																curUser : user
															});
												}

											} else {
												$log.debug("User Not logged  "
														+ $scope.user.email_id);
												$scope.loginMsg = "Authontication failed. Username/Password did not match.";
											}

										});
					}

					$scope.$on('customLoginEvent', function(event, args) {
						$log.debug("In side customLogin");
						$scope.curUser = args.curUser;
						$scope.initCommonSetting();
					});

					$scope.initCommonSetting = function() {
						ajsCache.removeAll();
						$scope.theme = $scope.curUser.business.theme;
						$scope.bizLogoGCSURL = $scope.curUser.business.bizLogoGCSURL;

						getUserAuthTree();
						// finally go to home...
						$log.debug("Forwarding to home page...");
						$state.go("home");
					}
					$scope.multiBizSelectUser = function(multiBizSelectedUser) {
						appEndpointSF.getLocalUserService().saveLoggedInUser(
								multiBizSelectedUser);
						if ($scope.googleUser)
							multiBizSelectedUser.imageURl = $scope.googleUser
									.getImageUrl();

						$scope.curUser = multiBizSelectedUser;
						appEndpointSF.getLocalUserService().saveLoggedInUser(
								multiBizSelectedUser);

						$scope.$emit('customLoginEvent', {
							curUser : multiBizSelectedUser
						});
						$scope.$broadcast('customLoginEvent', {
							curUser : multiBizSelectedUser
						});
						$state.go("home");

					}

					function getUserAuthTree() {
						var authService = appEndpointSF
								.getAuthorizationService();
						authService
								.getAuthorizationMasterEntity()
								.then(
										function(result) {
											/* $log.debug("result:" + result); */
											var authorizationMasterEntity = {
												authorizations : []
											};
											var userAuthMasterEntity = {
												authorizations : []
											};

											var jsonUserAuthObject = angular
													.fromJson($scope.curUser.authorizations);

											if ($scope.curUser.authorizations) {
												var jsonUserAuthObject = angular
														.fromJson($scope.curUser.authorizations);
												$scope.userAuthObject = jsonUserAuthObject;
											} else {
												$scope.userAuthObject = {
													authorizations : []
												};
											}

											if (result
													&& result.authorizations != undefined) {

												authorizationMasterEntity.authorizations = result.authorizations;

												userAuthMasterEntity = authService
														.filterMasterAuthTree(
																authorizationMasterEntity,
																$scope.userAuthObject,
																userAuthMasterEntity);

												userAuthMasterEntity.authorizations
														.sort(function(a, b) {
															return (parseFloat(a.orderNumber) > parseFloat(b.orderNumber)) ? 1
																	: -1
														});

												var curUser = appEndpointSF
														.getLocalUserService()
														.getLoggedinUser();
												curUser.userAuthMasterEntity = userAuthMasterEntity;
												appEndpointSF
														.getLocalUserService()
														.saveLoggedInUser(
																curUser);

												$scope.curUser = curUser;
												// $scope.safeApply();
											}
										});
					}

					$scope
							.$on(
									'event:google-plus-signin-success',
									function(event, authResult) {
										// User successfully authorized the G+
										// App!
										$log.debug('Signed in!');
										var profile = authResult
												.getBasicProfile();
										$scope.googleUser = profile;

										$log.debug('ID: ' + profile.getId());
										// Do not send to your backend! Use an
										// ID token instead.
										/*
										 * $log .debug('Name: ' +
										 * profile.getName()); $log.debug('Image
										 * URL: ' + profile.getImageUrl());
										 * $log.debug('email_id: ' +
										 * profile.getEmail());
										 */
										$scope.googleUserDetails = profile
												.getName()
												+ "<br>" + profile.getEmail()
										if ($scope.curUser) {
											// call is comming here twice. Hence
											// needed
											$log
													.debug("Outside: curUser is alrady init. Returning back....");
											return;
										}
										$log
												.debug("Going ahead with call to getUserByEmailID....");
										appEndpointSF
												.getUserService()
												.getUserByEmailID(
														profile.getEmail(),
														true)
												.then(
														function(
																loggedInUserList) {
															if ($scope.curUser) {
																// call is
																// comming here
																// twice. Hence
																// needed
																$log
																		.debug("Inside: curUser is alrady init. Returning back....");
																return;
															}
															$log
																	.debug("Going ahead with call init inside getUserByEmailID....");
															if (loggedInUserList.items.length > 1) {
																$scope.multiUsers = loggedInUserList.items;
																angular
																		.forEach(
																				$scope.multiUsers,
																				function(
																						fu) {
																					fu.imageURl = $scope.googleUser
																							.getImageUrl();
																				});
																$state
																		.go("selectmultibiz");
																return;
															} else {
																var loggedInUser = loggedInUserList.items[0];
																loggedInUser.imageURl = $scope.googleUser
																		.getImageUrl();
																appEndpointSF
																		.getLocalUserService()
																		.saveLoggedInUser(
																				loggedInUser);
															}

															$scope.curUser = loggedInUser;

															if (loggedInUser.id == undefined) {

																loggedInUser.email_id = profile
																		.getEmail();
																profile
																		.getName()
																		.split(
																				" ")[0];
																loggedInUser.firstName = profile
																		.getName()
																		.split(
																				" ")[0];
																loggedInUser.lastName = profile
																		.getName()
																		.split(
																				" ")[1];

																appEndpointSF
																		.getLocalUserService()
																		.saveLoggedInUser(
																				loggedInUser);

																// $scope.businessName
																// = "XYZ Firm";
																$state
																		.go("needbusiness");
																return;
															}

															$scope
																	.initCommonSetting();

														});

									});

					/*
					 * $log.debug('$scope.curUser' +
					 * angular.toJson($scope.curUser));
					 */

					$scope.signOut = function() {
						if (gapi.auth2 == undefined) {
							$scope.curUser = null;
							$scope.curUser = appEndpointSF
									.getLocalUserService().logout();

							$state.go("home");
							return;
						}
						var auth2 = gapi.auth2.getAuthInstance();
						auth2.signOut().then(
								function() {
									// also remove login details from chrome
									// browser

									$scope.googleUser = null;
									$scope.curUser = null;
									$scope.curUser = appEndpointSF
											.getLocalUserService().logout();

									$state.go("home");
								});
					}

					$scope.$on('event:google-plus-signin-failure', function(
							event, authResult) {
						// User has not authorized the G+ App!
						$log.debug('Not signed into Google Plus.');
						$scope.googleUser = null;
					});
					$rootScope.$on('$stateChangeSuccess', function(event,
							toState, toParams, fromState, fromParams) {
						// On any state change go the the top
						$timeout(function() {
							$location.hash('tp1');
							$anchorScroll();
						}, 10);

					});
					$rootScope.$on('$stateChangeStart', function(e, toState,
							toParams, fromState, fromParams) {
						// check access permission here.
					});

					$scope.initGAPI = function() {
						$log.debug("Came to initGAPI");
						// This will load all server side end points
						// $scope.loadAppGoogleServices();
						$timeout(
								function() {
									appEndpointSF
											.loadAppGoogleServices($q.defer())
											.then(
													function() {
														$log
																.debug("##########Loaded All Google Endpoint Services....#########");
														$scope.loading = false;
													});
								}, 2000);

					};

					$scope.openBottomSheet = function() {
						$mdBottomSheet
								.show({
									template : '<md-bottom-sheet>Hello!</md-bottom-sheet>'
								});
					};
					$scope.getDaysInMonth = function(month, year) {
						// Here January is 1 based
						// Day 0 is the last day in the previous month
						return new Date(year, month, 0).getDate();
						// If you need January is 0 based, use below
						// return new Date(year, month+1, 0).getDate();
					}

					$scope.getHighlightRedStyle = function(numberValue) {
						return {
							color : numberValue < 0 ? 'red' : ''
						};
					}

					// initialize local objects
					/*
					 * $scope.customer = $scope.newCustomer();
					 * $scope.customerList = {};
					 */
					$scope.waitForServiceLoad = function(authResult) {
						if (!appEndpointSF.is_service_ready) {
							$log
									.debug("Index: Services Not Loaded, waiting...");
							$timeout($scope.waitForServiceLoad, 2000);
							return;
						}

						$log
								.debug("####Index: Loaded All Services, Continuing####");
						if ($scope.curUser != undefined
								|| $scope.curUser !== null) {
							$scope.initCommonSetting();
						}
					}

					$scope.initGAPI();
					$scope.waitForServiceLoad();

					$scope.theme = 'default';

					$scope.themeList = [ 'default', 'red', 'pink', 'purple',
							'deep-purple', 'indigo', 'blue', 'light-blue',
							'cyan', 'teal', 'green', 'light-green', 'lime',
							'yellow', 'amber', 'orange', 'deep-orange',
							'brown', 'grey', 'blue-grey' ];

					$scope.changeTheme = function(themeName) {
						$scope.theme = themeName
					}

					$scope.back = function() {
						window.history.back();// for #tp1
						window.history.back(); // to actual view
					};

					$scope.hasUserAuthority = function(user, authorityToCheck) {
						return appEndpointSF.getAuthorizationService()
								.containsInAuthTree(authorityToCheck,
										angular.fromJson(user.authorizations));
					};

				})
		.controller(
				'AppCtrl',
				function($scope, $timeout, $mdSidenav, $mdUtil, $log, $mdMedia) {

					$scope.toggleMainMenuSwitch = $mdMedia('gt-md');

					$scope.toggleMainMenu = function() {
						$scope.toggleMainMenuSwitch = !$scope.toggleMainMenuSwitch;
						$log
								.debug("toggle left menu. $scope.toggleMainMenuSwitch:"
										+ $scope.toggleMainMenuSwitch);
					}

					$scope.close = function() {
						if (!$mdMedia('gt-md'))
							$scope.toggleMainMenu();
					}

					$scope.toggleLeft = buildToggler('left');
					// $scope.toggleRight = buildToggler('right');
					/**
					 * Build handler to open/close a SideNav; when animation
					 * finishes report completion in console
					 */
					function buildToggler(navID) {
						var debounceFn = $mdUtil.debounce(function() {
							$mdSidenav(navID).toggle().then(function() {
								$log.debug("toggle " + navID + " is done");
							});
						}, 200);
						return debounceFn;
					}
				});