angular.module("stockApp").controller(
		"contactsList",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, appEndpointSF) {

			$scope.showSimpleToast = function(msgBean) {
				$mdToast.show($mdToast.simple().content(msgBean)
						.position("top").hideDelay(3000));
			};

			$scope.selectedContactNo = $stateParams.selectedContactNo;

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
				cid : "",
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

			$scope.getAllcontact = function() {
				$scope.loading = true;
				var leadService = appEndpointSF.getleadService();
				leadService.getAllcontact($scope.curUser.business.id).then(
						function(contactList) {
							$log.debug("Inside Ctr getAllleads");
							$scope.contacts = contactList.items;
							$scope.loading = false;
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
				if (typeof $scope.selectedContactNo != "undefined") {
					leadService.getContactById($scope.curUser.business.id,
							$scope.selectedContactNo).then(function(contact) {
						$log.debug("Inside Ctr getAllleads");
						$scope.contact = contact;
						$scope.contact.phone = Number(contact.phone);// else
						// throws
						// error
						// while
						// editing

					});

				}
			}

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
				$scope.contact.modifiedBy = $scope.curUser.email_id;
				var leadService = appEndpointSF.getleadService();
				leadService.updatecontact($scope.contact).then(
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
