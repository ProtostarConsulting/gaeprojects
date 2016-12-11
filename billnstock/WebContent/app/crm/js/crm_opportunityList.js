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
					         order: 'name',
					         limit: 5,
					         page: 1
					       };
					
					var d = new Date();
					var year = d.getFullYear();
					var month = d.getMonth() + 1;
					if (month < 10) {
						month = "0" + month;
					}
					var day = d.getDate();
					$scope.curdate = year + "-" + month + "-" + day;

					$scope.from = [ "Lead", "Customer" ];
					$scope.taskType = [ "Phone Call", "Email", "Visit" ];

					$scope.opportunity = {
							business:"",
						loggedInUser : "",
						oid : "",
						from : "",
						name : "",
						date : $scope.curdate,
						description : "",
						tasks : []

					}
					$scope.task = [ {
						tid : "",
						description : "",
						type : "",
						date : $scope.curdate,
						note : "",
						status : ""
					} ]

					$scope.taskobj = {
						tid : "",
						description : "",
						type : "",
						date : $scope.curdate,
						note : "",
						status : ""
					}

				

					$scope.getAllopportunity = function() {
						$scope.loading = true;
						var opportunityService = appEndpointSF
								.getopportunityService();
						opportunityService
								.getAllopportunity(
										$scope.curUser.business.id)
								.then(
										function(opportunityList) {
											$log
													.debug("Inside Ctr getAllleads");
											$scope.opportunitys = opportunityList.items;
											$scope.loading = false;
											$scope.cleadid = $scope.opportunitys.length + 1;
											$scope.opportunity.oid = $scope.cleadid;

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
										$scope.selectedopportunityNo)
								.then(
										function(opportunityList) {
											$log
													.debug("Inside Ctr opportunityList");
											$scope.opportunityL = opportunityList.result;
											$scope.ctaskid = $scope.opportunityL.tasks.length + 1;
											$scope.taskobj.id = $scope.ctaskid;
											$scope.taskobj.date = $scope.curdate;
										});
					}
					}

					$scope.opportunityL = [];
					
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
						$scope.opportunityL.modifiedBy=$scope.curUser.email_id;
						
						var opportunityService = appEndpointSF
								.getopportunityService();	
						opportunityService.updateopportunity(
								$scope.opportunityL).then(function(msgBean) {
							$log.debug("Inside CtropportunityL");
							$log.debug("msgBean.msg:" + msgBean.msg);
							$scope.showUpdateToast();
							// $scope.empDetail =[];
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
						
						$scope.opportunityL.modifiedBy=$scope.curUser.email_id;
						
						var opportunityService = appEndpointSF
								.getopportunityService();
						$scope.opportunityL.tasks.push($scope.taskobj);

						opportunityService.addupdatetask($scope.opportunityL)// $scope.task,
																				// oppid
						.then(function(msgBean) {
							$scope.showUpdateToast();
							$scope.getopportunityById();
						});

						$scope.taskobj = {};
						$scope.task.date = $scope.curdate;
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
