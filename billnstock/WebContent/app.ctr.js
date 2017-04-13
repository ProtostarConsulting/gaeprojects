angular
		.module("stockApp")
		.controller(
				"indexCtr",
				function($scope, $rootScope, $window, $log, $q, $timeout,
						$mdToast, $mdBottomSheet, $state, $http, $location,
						$anchorScroll, $mdMedia, ajsCache, appEndpointSF) {

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
						$scope.loading = true;
						$scope.loginMsg = "";
						var UserService = appEndpointSF.getUserService();
						UserService
								.login($scope.user.email_id,
										$scope.user.password)
								.then(
										function(result) {
											if (result.items) {
												$scope.loading = false;
												if (result.items.length > 1) {
													$scope.multiUsers = result.items;
													$state
															.go(
																	"selectmultibiz",
																	{
																		multiUsers : $scope.multiUsers
																	});
													return;
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
												$scope.loading = false;
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

						gapi.auth.setToken({
							access_token : $scope.curUser.accessToken
						});
						getUserAuthTree();
						// finally go to home...
						$log.debug("Forwarding to home page...");
						$scope.loading = false;
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
										$scope.loading = true;
										// User successfully authorized the G+
										// App!
										$log.debug('Signed in!');
										$scope.googleUser = appEndpointSF
												.getLocalUserService()
												.getLoggedinUser()
										if ($scope.googleUser) {
											// call is comming here twice. Hence
											// needed
											$log
													.debug("Outside: curUser is alrady init. Returning back....");
											return;
										}

										var profile = authResult
												.getBasicProfile();
										$scope.googleUser = profile;

										appEndpointSF.getLocalUserService()
												.saveLoggedInUser(profile);

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

										$log
												.debug("Going ahead with call to getUserByEmailID....");
										getUserDetailsFn(profile.getEmail());

									});

					function getUserDetailsFn(emailId) {
						$scope.loading = true;
						if (!appEndpointSF.is_service_ready) {
							$log
									.debug("Index: Services Not Loaded, waiting...");
							$timeout(function() {
								getUserDetailsFn(emailId);
							}, 2000);
							return;
						}

						appEndpointSF
								.getUserService()
								.getUserByEmailID(emailId, true)
								.then(
										function(loggedInUserList) {
											if ($scope.curUser) {
												// call is
												// comming here
												// twice. Hence
												// needed
												$scope.loading = false;
												$log
														.debug("Inside: curUser is alrady init. Returning back....");
												return;
											}
											$log.debug("Going ahead....");
											$scope.loading = false;
											if (loggedInUserList.items.length > 1) {
												$scope.multiUsers = loggedInUserList.items;
												angular
														.forEach(
																$scope.multiUsers,
																function(fu) {
																	fu.imageURl = $scope.googleUser
																			.getImageUrl();
																});
												$state
														.go(
																"selectmultibiz",
																{
																	multiUsers : $scope.multiUsers
																});
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
												profile.getName().split(" ")[0];
												loggedInUser.firstName = profile
														.getName().split(" ")[0];
												loggedInUser.lastName = profile
														.getName().split(" ")[1];

												appEndpointSF
														.getLocalUserService()
														.saveLoggedInUser(
																loggedInUser);

												// $scope.businessName
												// = "XYZ Firm";
												$state.go("needbusiness");
												return;
											}

											$scope.initCommonSetting();

										});
					}

					$scope.signOut = function() {

						var hostBaseUrl = '//' + window.location.host
								+ '/index.html';
						appEndpointSF.getUserService().logout().then(
								function(msg) {
									$log.debug('User signed out:' + msg);
								});
						var auth2 = gapi.auth2.getAuthInstance();
						// try logout 3 times.
						for (var i = 1; i <= 3; i++) {
							auth2
									.signOut()
									.then(
											function() {
												// also remove login details
												// from chrome
												// browser

												$scope.googleUser = null;
												$scope.curUser = null;
												$scope.curUser = appEndpointSF
														.getLocalUserService()
														.logout();

												// $state.go("home");
												$window.location.href = hostBaseUrl;
											});
						}

						if (gapi.auth2 == undefined) {
							$scope.curUser = null;
							$scope.curUser = appEndpointSF
									.getLocalUserService().logout();

							// $state.go("home");
							$window.location.href = hostBaseUrl;
							return;
						}
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

					$rootScope.$on('$stateChangeStart', function(event,
							toState, toParams, fromState, fromParams) {
						// check access permission here.
						// $log.debug('toState: ' + toState);

						if (toState.name == 'initsetup'
								|| toState.name == 'login'
								|| toState.name == 'home') {
							return;
						}
						$scope.curUser = appEndpointSF.getLocalUserService()
								.getLoggedinUser();
						if (toState.name != 'login' && !$scope.curUser) {
							event.preventDefault();
							$state.go('login');
						}
						/*
						 * else if ($scope.curUser &&
						 * !$scope.hasUserAuthority($scope.curUser,
						 * toState.name)) { event.preventDefault();
						 * $state.go('login'); }
						 */
					});

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

					$scope.initGAPI = function() {
						$log.debug("Loading Google client.js...");

						if (gapi && gapi.client && gapi.client.load) {
							appEndpointSF
									.loadAppGoogleServices($q.defer())
									.then(
											function() {
												$log
														.debug("##########Loaded All Google Endpoint Services....#########");
												$scope.loading = false;
											});
							$scope.waitForServiceLoad();
						} else {
							$timeout($scope.initGAPI, 2000);
						}
					}

					$scope.initGAPI();

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
						if (!user || !authorityToCheck)
							return false;

						return appEndpointSF.getAuthorizationService()
								.containsInAuthTree(authorityToCheck,
										angular.fromJson(user.authorizations));
					};

					$scope.isDocumentEditAllowed = function(documentEntity) {
						if (documentEntity
								&& (documentEntity.status == 'FINALIZED' || documentEntity.status == 'REJECTED')) {
							return false;
						}
						return true;
					}

					$scope.gotoPart = function(partId) {
						// $location.hash(partId);
						$anchorScroll(partId);
					}
					$rootScope.scroll = 0;
				})
		.controller(
				'AppCtrl',
				function($scope, $timeout, $mdSidenav, $mdUtil, $log, $mdMedia,
						$rootScope) {

					$rootScope.scroll = 10;

					$scope.$watch('scroll', function() {
						console.log("$scope.scroll: " + $scope.scroll);
						$rootScope.scroll = $scope.scroll;
					});
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