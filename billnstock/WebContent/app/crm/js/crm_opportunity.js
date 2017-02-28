angular.module("stockApp").controller(
		"opportunity",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, appEndpointSF) {

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
				opName : "",
				date : new Date(),
				description : "",
				tasks : []

			}
			$scope.task = [];

			$scope.taskobj = {
				tid : "",
				description : "",
				type : "",
				date : new Date(),
				note : "",
				status : ""
			}

			$scope.getleadcust = function(from) {
				var temp = from;
				$log.debug("============" + temp);
				if (temp.trim() == "Lead") {
					$log.debug("============" + temp);
					var leadService = appEndpointSF.getleadService();
					leadService.getAllleads($scope.curUser.business.id).then(
							function(leadList) {
								$scope.leadorcustlist = leadList.items;
							});

				}

				if (temp.trim() == "Customer") {
					$log.debug("============" + temp);
					var customerService = appEndpointSF.getCustomerService();
					customerService.getAllCustomersByBusiness(
							$scope.curUser.business.id).then(
							function(custList) {
								$scope.leadorcustlist = custList.items;
							});

				}

			}
			$scope.leadorcustlist = [];

			$scope.addopportunity = function() {

				$scope.opportunity.loggedInUser = $scope.curUser;
				$scope.opportunity.from = $scope.f;
				$scope.opportunity.tasks = $scope.task;
				$scope.opportunity.business = $scope.curUser.business;

				var opportunityService = appEndpointSF.getopportunityService();

				opportunityService.addopportunity($scope.opportunity).then(
						function(msgBean) {

							$log.debug("Inside Ctr addlead");
							$log.debug("msgBean.msg:" + msgBean.msg);
							$scope.showAddToast();
							$scope.getAllopportunity();
						});
				$scope.oppform.$setPristine();
				$scope.oppform.$setValidity();
				$scope.oppform.$setUntouched();
				$scope.opportunity = {};
				$scope.opportunity.date = new Date();
			}

			$scope.getAllopportunity = function() {
				var opportunityService = appEndpointSF.getopportunityService();
				opportunityService
						.getAllopportunity($scope.curUser.business.id)
						.then(function(opportunityList) {
							$log.debug("Inside Ctr getAllleads");
							$scope.opportunitys = opportunityList.items;
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
