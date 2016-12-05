angular.module("stockApp").controller(
		"taxCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$log, objectFactory, appEndpointSF) {

			$log.debug("Inside taxCtr");

			$scope.query = {
				order : 'name',
				limit : 5,
				page : 1
			};

			$scope.curUser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();
			$log.debug("$scope.curUser++++++++"
					+ angular.toJson($scope.curUser));

			$scope.tax = {
				taxCodeName : '',
				taxPercenatge : '',
				active : true,
				business : ""
			}

			$scope.addTax = function() {
				$log.debug("No1");
				$scope.tax.business = $scope.curUser.business;

				var taxService = appEndpointSF.getTaxService();

				taxService.addTax($scope.tax).then(function(msgBean) {
					
				});
				$scope.showAddToast();

				$scope.taxForm.$setPristine();
				$scope.taxForm.$setValidity();
				$scope.taxForm.$setUntouched();
				$scope.tax = {};
			}

			$scope.getAllTaxes = function() {
				$log.debug("Inside Ctr $scope.getAllTaxes");
				var taxService = appEndpointSF.getTaxService();

				taxService.getAllTaxes($scope.curUser.business.id).then(
						function(taxList) {
							$scope.taxData = taxList;
							$log.debug("Inside Ctr $scope.taxData:"
									+ angular.toJson($scope.taxData));
						});
			}

			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getAllTaxes();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.taxData = [];
			$scope.waitForServiceLoad();

			$scope.selected = [];

			$scope.updateTax = function() {
				var taxService = appEndpointSF.getTaxService();

				taxService.updateTax($scope.selected[0]).then(
						function(msgBean) {
							$scope.showUpdateToast();
							$scope.getAllTaxes();
						});
				$scope.taxForm.$setPristine();
				$scope.taxForm.$setValidity();
				$scope.taxForm.$setUntouched();
				$scope.selected[0] = "";
			}

			$scope.cancelUpdate = function() {
				$scope.selected = [];
			}

			// Setup menu
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
