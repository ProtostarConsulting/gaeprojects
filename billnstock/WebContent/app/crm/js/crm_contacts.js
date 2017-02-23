angular
		.module("stockApp")
		.controller(
				"contacts",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $q) {

					$scope.showSimpleToast = function(msgBean) {
						$mdToast.show($mdToast.simple().content(msgBean)
								.position("top").hideDelay(3000));
					};
					$scope.selectedcontactNo = $stateParams.selectedcontactNo;

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.query = {
						order : 'name',
						limit : 5,
						page : 1
					};

					$scope.contact = {
						business : "",
						loggedInUser : "",
						fName : "",
						lName : "",
						status : "",
						phone : null,
						email : "",
						uid : "",
						supp : "",
						cust : "",
						salespartner : ""
					}

					$scope.addcontact = function() {
						$scope.contact.loggedInUser = $scope.curUser;
						$scope.contact.business = $scope.curUser.business;
						var leadService = appEndpointSF.getleadService();
						leadService.addcontact($scope.contact).then(
								function(msgBean) {
									$log.debug("Inside Ctr addlead");
									$log.debug("msgBean.msg:" + msgBean.msg);
									$scope.showAddToast();
									$scope.getAllcontact();
								});
						$scope.contactform.$setPristine();
						$scope.contactform.$setValidity();
						$scope.contactform.$setUntouched();
						$scope.contact = {};
					}

					$scope.getAllcontact = function() {
						var leadService = appEndpointSF.getleadService();
						leadService
								.getAllcontact($scope.curUser.business.id)
								.then(
										function(contactList) {
											$log
													.debug("Inside Ctr getAllleads");
											$scope.contacts = contactList.items;

											$scope.cleadid = $scope.contacts.length + 1;
											$scope.contact.cid = $scope.cleadid;

										});
					}

					$scope.contacts = [];

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAllcontact();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					// auto complate
					// list of `state` value/display objects
					$scope.companyName = [];
					$scope.companies = [];

					$scope.waitForServiceLoad3 = function() {
						if (appEndpointSF.is_service_ready) {
							loadAll();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad3, 2000);
						}
					}
					$scope.waitForServiceLoad3();

					$scope.selectedItem;
					$scope.searchText = null;

					// ******************************
					// Internal methods
					// ******************************
					/**
					 * Search for states... use $timeout to simulate remote
					 * dataservice call.
					 */
					$scope.querySearch = function(query) {
						var results = query ? $scope.companyName
								.filter(createFilterFor(query))
								: $scope.companyName;
						var deferred = $q.defer();
						$timeout(function() {
							deferred.resolve(results);
						}, Math.random() * 1000, false);
						return deferred.promise;
					}
					/**
					 * Build `states` list of key/value pairs
					 */
					function loadAll() {
						var hrService = appEndpointSF.gethrService();
						var allStates;
						hrService
								.getAllcompany($scope.curUser.business.id)
								.then(
										function(companies) {
											$scope.companies = companies.items;
											for (i = 0; i < $scope.companies.length; i++) {
												if ($scope.companies[i].isCompany == true)
													$scope.companyName
															.push($scope.companies[i]);
											}
										});

					}

					/**
					 * Create filter function for a query string /
					 */
					function createFilterFor(query) {
						var lowercaseQuery = angular.lowercase(query);
						return function filterFn(emp) {
							return (angular.lowercase(emp.companyName).indexOf(
									lowercaseQuery) >= 0);
						};
					}
					// ////////////Auto complete code ends//////////////////////

					$scope.Checkemail = function(emailid) {

						var leadService = appEndpointSF.getleadService();
						leadService
								.isContactExists(emailid)
								.then(
										function(responce) {
											if (responce.result.returnBool == true) {
												$scope.userexists = "Contact already exists";
												$scope.user.firstName = "";
												$scope.user.lastName = "";
												angular.element(document
														.getElementById('uid'))[0].disabled = true;
												angular
														.element(document
																.getElementById('supp'))[0].disabled = true;
												angular
														.element(document
																.getElementById('cust'))[0].disabled = true;
												angular
														.element(document
																.getElementById('salespartner'))[0].disabled = true;
											} else {
												$scope.userexists = "";
												angular.element(document
														.getElementById('uid'))[0].disabled = false;
												angular
														.element(document
																.getElementById('supp'))[0].disabled = false;
												angular
														.element(document
																.getElementById('cust'))[0].disabled = false;
												angular
														.element(document
																.getElementById('salespartner'))[0].disabled = false;
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
