angular.module("stockApp").controller(
		"contactsList",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, appEndpointSF) {

			$scope.showSimpleToast = function(msgBean) {
				$mdToast.show($mdToast.simple().content(msgBean)
						.position("top").hideDelay(3000));
			};
			$scope.selectedcontactNo = $stateParams.selectedcontactNo;
			$scope.curUser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();
			
			$scope.query = {
			         order: 'name',
			         limit: 5,
			         page: 1
			       };
			
			$scope.contact = {
					business:"",
				loggedInUser : "",
				cid : "",
				fName : "",
				lName : "",
				status : "",
				phone : "",
				email : "",
				uid : "",
				supp : "",
				cust : "",
				salespartner : ""
			}

			
			$scope.getAllcontact = function() {
				var leadService = appEndpointSF.getleadService();
				leadService.getAllcontact($scope.curUser.business.id).then(function(contactList) {
					$log.debug("Inside Ctr getAllleads");
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

			$scope.getContactById = function() {
				$log.debug("Inside Ctr $scope.getAlllead");
				var leadService = appEndpointSF.getleadService();
				$scope.ctaskid;
				if (typeof $scope.selectedcontactNo != "undefined") {
				leadService.getContactById($scope.selectedcontactNo).then(
						function(contactList) {
							$log.debug("Inside Ctr getAllleads");
							$scope.contactL = contactList.result;

						});	

			}
			}
			$scope.contactL = [];
			
			$scope.waitForServiceLoad1 = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getContactById();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad1, 1000);
				}
			}
			$scope.waitForServiceLoad1();

			$scope.updatecontact = function() {

				var leadService = appEndpointSF.getleadService();
				leadService.updatecontact($scope.contactL).then(
						function(msgBean) {
							$log.debug("Inside Ctr updateemp");
							$log.debug("msgBean.msg:" + msgBean.msg);
							$scope.showUpdateToast();
							// $scope.empDetail = [];
						});
			}

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
