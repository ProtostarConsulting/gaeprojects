angular.module("stockApp").controller(
		"taxCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$log, objectFactory, appEndpointSF) {

			$log.debug("Inside taxCtr");

			$scope.curUser = appEndpointSF.getLocalUserService()
			.getLoggedinUser();
			$log.debug("$scope.curUser++++++++"+angular.toJson($scope.curUser));
			
			$scope.showSimpleToast = function() {
				$mdToast.show($mdToast.simple().content('Tax Data Saved!')
						.position("top").hideDelay(3000));
			};

			$scope.tax = {
					taxCodeName:'',
					taxPercenatge:'',
					taxVisibility:true
			}
			
			$scope.addTax = function() {
				$log.debug("No1");
				$scope.tax.loggedInUser =$scope.curUser;
				
				var taxService = appEndpointSF.getTaxService();

				taxService.addTax($scope.tax).then(function(msgBean) {
					$log.debug("No6");
					$log.debug("Inside Ctr addTax");
					$log.debug("msgBean.msg:" + msgBean.msg);
					$scope.showSimpleToast();

				});
				$log.debug("No4");
				$scope.tax = {};
			}

			$scope.getAllTaxes = function() {
				$log.debug("Inside Ctr $scope.getAllTaxes");
				var taxService = appEndpointSF.getTaxService();

				taxService.getAllTaxes($scope.curUser.businessAccount.id).then(
						function(taxList) {
							$log.debug("Inside Ctr getAllTaxes");
							$scope.taxData = taxList;
							$log.debug("Inside Ctr $scope.taxData:"
									+ angular.toJson($scope.taxData));
						});
			}

			$scope.taxData = [];
			$scope.getAllTaxes();

			$scope.selected = [];

			$scope.updateTax = function() {
				$log.debug("Inside Ctr $scope.updateTax");
				var taxService = appEndpointSF.getTaxService();

				taxService.updateTax($scope.selected[0]).then(
						function(msgBean) {
							$log.debug("Inside Ctr updateTax");
							$log.debug("msgBean.msg:" + msgBean.msg);
							$scope.showSimpleToast();
							$scope.getAllTaxes();
						});
			}
			
/*			$scope.disableTax = function () {
				$log.debug("Inside Ctr $scope.updateTax");
				var taxService = appEndpointSF.getTaxService();
				
				taxService.disableTax($scope.tax.taxVisibility).then(
						function(msgBean){
							$log.debug("Inside Ctr disableTax");
							$log.debug("msgBean.msg:" + msgBean.msg);
							$scope.showSimpleToast();
						})
				
			}
*/			
			
			var lastValue;

			$("#changer").bind("click", function(e){
			    lastValue = $(this).val();
			}).bind("change", function(e){
			    changeConfirmation = confirm("Really?");
			    if (changeConfirmation) {
			        // Proceed as planned
			    } else {
			        $(this).val(lastValue);
			    }
			});
			
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
