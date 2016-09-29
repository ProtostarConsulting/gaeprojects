angular
		.module("stockApp")
		.controller(
				"indexCtr",
				function($scope, $window, $log, $q, $timeout, $mdToast,
						$mdBottomSheet, $state, $http, appEndpointSF) {

					$log.log("Inside indexCtr");
					$scope.loading = true;

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

					$scope.showDelToast = function() {
						$mdToast.show($mdToast.simple().content(
								'Account  Deleted ...!').position("top")
								.hideDelay(3000));
					};

					// use popover
					/*
					 * $('[data-toggle="popover"]').popover({ trigger: 'manual',
					 * placement: 'bottom', html: true }).click(function (e) {
					 * e.preventDefault(); // Exibe o popover.
					 * $(this).popover('show');
					 * 
					 * $('.popover').css('left', '63px'); });
					 */
					/*
					 * $(document).ready(function(){
					 * $('[data-toggle="popover"]').popover(); });
					 */
					// end popover
					// ------------------------------------------login
					// user---------------------
					$scope.curUser = null;
					$scope.googleUserDetails = "";
					$scope.googleUser = 'null';
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
					$scope.logBaseURL;
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

					$scope.login = function() {
						var UserService = appEndpointSF.getUserService();
						UserService
								.login($scope.user.email_id,
										$scope.user.password)
								.then(
										function(result) {
											if (result.result.email_id) {
												appEndpointSF
														.getLocalUserService()
														.saveLoggedInUser(
																result.result);
												// $scope.curUser =
												// result.result;
												// $window.location.reload();

												$log
														.debug("#### Now without reload.......");

												$scope
														.$emit(
																'customLoginEvent',
																{
																	curUser : result.result
																});
												$scope
														.$broadcast(
																'customLoginEvent',
																{
																	curUser : result.result
																});
												$state.go("home");

											} else {
												$log.debug("User Not logged  "
														+ $scope.user.email_id);
												$scope.loginMsg = "Authontication failed. username password not correct";
											}

										});
					}

					$scope.$on('customLoginEvent', function(event, args) {
						$log.debug("In side customLogin");
						$scope.curUser = args.curUser;
						$scope.initCommonSetting();
					});

					$scope.initCommonSetting = function() {
						$scope.theme = $scope.curUser.business.theme;
						$scope.logBaseURL = '//' + window.location.host
								+ '/serve?blob-key='
								+ $scope.curUser.business.logBlobKey;
						$scope.logFooterURL = '//' + window.location.host
								+ '/serve?blob-key='
								+ $scope.curUser.business.footerBlobKey;
						getUserAuthTree();
					}

					function getUserAuthTree() {
						var authService = appEndpointSF
								.getAuthorizationService();
						authService
								.getAuthorizationMasterEntity()
								.then(
										function(result) {
											$log.debug("result:" + result);
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

												$log
														.debug("userAuthMasterEntity:"
																+ angular
																		.toJson(userAuthMasterEntity));

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
										$log
												.debug('Name: '
														+ profile.getName());
										$log.debug('Image URL: '
												+ profile.getImageUrl());
										$log.debug('email_id: '
												+ profile.getEmail());
										$scope.googleUserDetails = profile
												.getName()
												+ "<br>" + profile.getEmail()

										appEndpointSF
												.getUserService()
												.getUserByEmailID(
														profile.getEmail())
												.then(
														function(loggedInUser) {

															loggedInUser.imageURl = $scope.googleUser
																	.getImageUrl();
															appEndpointSF
																	.getLocalUserService()
																	.saveLoggedInUser(
																			loggedInUser);
															$log
																	.debug("loggedInUser:"
																			+ angular
																					.toJson(loggedInUser));

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

															var auth = $scope.curUser.authority;
															$scope.test = $scope.curUser.email_id;
															$scope.eid = $scope.test
																	.substring($scope.test
																			.indexOf("@") + 1);
															if ($scope.eid == "protostarcs.com"
																	|| $scope.eid == "protostar.co.in")
																if (auth
																		.indexOf("protostarcs.com") != -1
																		|| auth
																				.indexOf("protostar.co.in") != -1) {
																} else {
																	$scope.curUser.authority
																			.push($scope.eid);
																}

														});

										$log
												.debug("Forwarding to home state...");
										$state.go("home");

									});

					/*
					 * $log.debug('$scope.curUser' +
					 * angular.toJson($scope.curUser));
					 */

					$scope.signOut = function() {
						$log.debug('signOut1');
						if (gapi.auth2 == undefined) {
							$scope.curUser = null;
							$scope.curUser = appEndpointSF
									.getLocalUserService().logout();

							$state.go("home");
							return;
						}
						$log.debug('signOut2');
						var auth2 = gapi.auth2.getAuthInstance();
						auth2.signOut().then(
								function() {
									$log.debug('User signed out.');
									// also remove login details from chrome
									// browser

									$scope.googleUser = 'null';
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
						$scope.googleUser = 'null';
					});

					// ----------------------------------------------------------------

					/*
					 * $scope.showListBottomSheet=function(){
					 * document.getElementById("myDropdown").classList.toggle("show");
					 * var dropdowns =
					 * document.getElementsByClassName("dropdown-content"); var
					 * i; for (i = 0; i < dropdowns.length; i++) { var
					 * openDropdown = dropdowns[i]; if
					 * (openDropdown.classList.contains('show')) { //
					 * openDropdown.classList.remove('show'); } } }
					 */
					// -----------------------------------------------------------------
					// $window.initGAPI = function() {}
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

					// initialize local objects
					/*
					 * $scope.customer = $scope.newCustomer();
					 * $scope.customerList = {};
					 */
					$scope.waitForServiceLoad = function(authResult) {
						if (!appEndpointSF.is_service_ready) {
							$log
									.debug("Index: Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
							return;
						}

						$log
								.debug("####Index: Loaded All Services, Continuing####");
						/*
						 * if (authResult) { continueGoogleLogin(authResult); }
						 * if (!$scope.initDone) { $scope.initCommonSetting(); }
						 */
					}

					$scope.initGAPI();
					$scope.waitForServiceLoad();

					$scope.theme = 'default';
					if ($scope.curUser != undefined || $scope.curUser !== null) {
						$scope.theme = $scope.curUser.business.theme;
						$scope.logBaseURL = '//' + window.location.host
								+ '/serve?blob-key='
								+ $scope.curUser.business.logBlobKey;
						$scope.logFooterURL = '//' + window.location.host
								+ '/serve?blob-key='
								+ $scope.curUser.business.footerBlobKey;
					}

					$scope.themeList = [ 'default', 'red', 'pink', 'purple',
							'deep-purple', 'indigo', 'blue', 'light-blue',
							'cyan', 'teal', 'green', 'light-green', 'lime',
							'yellow', 'amber', 'orange', 'deep-orange',
							'brown', 'grey', 'blue-grey' ];

					$scope.changeTheme = function(themeName) {
						$scope.theme = themeName
					}

					$scope.back = function() {
						window.history.back();
					};

				}).controller(
				'AppCtrl',
				function($scope, $timeout, $mdSidenav, $mdUtil, $log,
						$stateParams) {

					// $scope.userauthoritys=[];
					$scope.userauthoritys = $stateParams.userauthoritys;
					$log.debug("userid========="
							+ angular.toJson($scope.userauthoritys));

					/*
					 * var str = $scope.userauthoritys; var arr =
					 * str.split(','); //Note the space as well
					 * console.log(arr); //Yields ["Apples", "Bananas",
					 * "Cherries"]
					 */
					$scope.loader = {
						invoice : "true",
						hr : "true",
						sales : "true",
						crm : "true"

					};
					/*
					 * for(i=0;i<$scope.userid.length;i++){
					 * if($scope.userL.authority[i]=='invoice')
					 * $scope.loader.invoice = true ;
					 * if($scope.userL.authority[i]=='hr') $scope.loader.hr =
					 * true ; if($scope.userL.authority[i]=='sales')
					 * $scope.loader.sales = true ;
					 * if($scope.userL.authority[i]=='crm') $scope.loader.crm =
					 * true ; if($scope.userL.authority[i]=='setup')
					 * $scope.loader.setup = true ; }
					 */

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
				}).controller('LeftCtrl',
				function($scope, $timeout, $mdSidenav, $log) {
					$scope.close = function() {
						$mdSidenav('left').close().then(function() {
							$log.debug("close LEFT is done");
						});
					};
				});