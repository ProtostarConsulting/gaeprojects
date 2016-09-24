angular.module("stockApp").controller(
		"hrCtr.empsalslipstruct",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, appEndpointSF) {
			$scope.selectedempstructno = $stateParams.selectedempstructno;
			$scope.curUser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			$scope.query = {
				order : 'name',
				limit : 5,
				page : 1
			};

			$scope.selected = [];
			$scope.salstruct = {
				business : $scope.curUser.business,
				empAccount : "",
				grosssal : "",
				monthly : "",
				byearly : "",
				bmonthly : "",
				hrayearly : "",
				hramonthly : "",
				ccayearly : "",
				ccamonthly : "",
				ec12Byearly : "",
				convyearly : "",
				convmonthly : "",
				sayearly : "",
				grandtotal : "",
				samonthly : "",
				bgrandtotal : "",
				ptaxyearly : "",
				pf1 : 0,
				pf2 : 0,
				ptaxgrandtotal : "",
				netsalgrandtotalmonthly : "",
				netsalgrandtotal : "",
				addprobonus : "",
				ctc : "",
				mctc : "",
				ldother1dis : "",
				ldother2dis : "",
				ldother1amt : "",
				ldother2amt : ""
			};

			$scope.viewsalstruct = $scope.salstruct;
			$scope.printsalstruct = $scope.salstruct;

			$scope.getAllempsSalStruct = function() {
				$log.debug("Inside Ctr $scope.getAllempsSalStruct");
				var hrService = appEndpointSF.gethrService();

				hrService.getAllempsSalStruct($scope.curUser.business.id).then(
						function(empSalstructList) {
							$log.debug("Inside Ctr getAllemps");
							$scope.empSalStruct = empSalstructList.items;

						});
			}
			$scope.empSalStruct = [];

			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getAllempsSalStruct();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();

			$scope.viewfindsalstruct = function() {
				var hrService = appEndpointSF.gethrService();
				if (typeof $scope.selectedempstructno != 'undefined') {
					hrService.viewfindsalstruct($scope.selectedempstructno)
							.then(function(structList) {
								$log.debug("Inside Ctr getsalstruct");
								$scope.viewslist = structList;
								$scope.viewsalstruct = $scope.viewslist;

							});
				}
			}

			$scope.viewslist = [];

			$scope.waitForServiceLoad1 = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.viewfindsalstruct();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad1, 1000);
				}
			}
			$scope.waitForServiceLoad1();

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
