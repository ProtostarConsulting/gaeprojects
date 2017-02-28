angular
		.module("stockApp")
		.controller(
				"opportunityList",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF) {

					$scope.showSimpleToast = function(msgBean) {
						$mdToast.show($mdToast.simple().content(msgBean)
								.position("top").hideDelay(3000));
					};

					$scope.selectedopportunityNo = $stateParams.selectedopportunityNo;

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.query = {
						order : 'name',
						limit : 5,
						page : 1
					};

					$scope.from = [ "Lead", "Customer" ];
					$scope.taskType = [ "Phone Call", "Email", "Visit" ];

					$scope.opportunity = {
						business : "",
						loggedInUser : "",
						oid : "",
						from : "",
						name : "",
						date : new Date(),
						description : "",
						tasks : []

					}
					$scope.task = [ {
						tid : "",
						description : "",
						type : "",
						date : new Date(),
						note : "",
						status : ""
					} ]

					$scope.taskobj = {
						tid : "",
						description : "",
						type : "",
						date : new Date(),
						note : "",
						status : ""
					}

					$scope.editTask = function(task) {
						$scope.taskobj = task;
						$scope.taskobj.date = new Date(task.date);
					};

					$scope.getAllopportunity = function() {
						$scope.loading = true;
						var opportunityService = appEndpointSF
								.getopportunityService();
						opportunityService
								.getAllopportunity($scope.curUser.business.id)
								.then(
										function(opportunityList) {
											$log
													.debug("Inside Ctr getAllleads");
											$scope.opportunitys = opportunityList.items;
											$scope.loading = false;
											$scope.cleadid = $scope.opportunitys.length + 1;
										});
					}

					$scope.opportunitys = [];

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getAllopportunity();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.getopportunityById = function() {
						$log.debug("Inside Ctr $scope.getAlllead");
						var opportunityService = appEndpointSF
								.getopportunityService();
						if (typeof $scope.selectedopportunityNo != "undefined") {
							opportunityService
									.getopportunityById(
											$scope.curUser.business.id,
											$scope.selectedopportunityNo)
									.then(
											function(opportunity) {
												$log
														.debug("Inside Ctr opportunityList");
												$scope.opportunity = opportunity;
												$scope.opportunity.date = new Date(
														opportunity.date);
												if (!$scope.opportunity.tasks) {
													$scope.opportunity.tasks = [];
												}
												$scope.ctaskid = $scope.opportunity.tasks.length + 1;
												$scope.taskobj.tid = $scope.ctaskid;
												$scope.taskobj.date = new Date();
											});
						}
					}

					$scope.waitForServiceLoad1 = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getopportunityById();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad1, 1000);
						}
					}
					$scope.waitForServiceLoad1();

					$scope.updateopportunity = function() {
						$scope.opportunity.modifiedBy = $scope.curUser.email_id;

						var opportunityService = appEndpointSF
								.getopportunityService();
						opportunityService
								.updateopportunity($scope.opportunity)
								.then(function(msgBean) {
									$log.debug("Inside CtropportunityL");
									$log.debug("msgBean.msg:" + msgBean.msg);
									$scope.showUpdateToast();
								});
					}

					// ----------hide and show ---------------------------
					$scope.IsHidden = true;
					$scope.ShowHide = function() {
						$scope.IsHidden = $scope.IsHidden ? false : true;
					}
					// -----------------------------------------------------

					// ------------------save task----------

					$scope.addupdatetask = function(oppid) {

						$scope.opportunity.modifiedBy = $scope.curUser.email_id;

						if (!$scope.opportunity.tasks) {
							$scope.opportunity.tasks = [];
						}

						if (typeof $scope.taskobj.type != 'undefined'
								&& $scope.taskobj.type != "") {
							var i;
							for (i = 0; i < $scope.opportunity.tasks.length; i++) {
								if ($scope.taskobj.tid == $scope.opportunity.tasks[i].tid) {
									break;
								}
							}

							if (i == $scope.opportunity.tasks.length) {
								$scope.opportunity.tasks.push($scope.taskobj);
							}
						}

						var opportunityService = appEndpointSF
								.getopportunityService();

						opportunityService.addupdatetask($scope.opportunity)// $scope.task,
						// oppid
						.then(function(msgBean) {
							$scope.showUpdateToast();
							$scope.getopportunityById();
						});

						$scope.taskobj = {};
						$scope.task.date = new Date();
					}

					// --------------------------------------

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
