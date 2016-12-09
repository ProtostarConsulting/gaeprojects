
var app = angular.module("stockApp");

app.controller("addAccountEntryController", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,$mdDialog,$mdMedia ) {

	$scope.query = {
		order : 'name',
		limit : 5,
		page : 1
	};

	

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();
	$log.debug("$scope.curUser++++++++" + angular.toJson($scope.curUser));

	$scope.account = {
		accountName : "",
		description : "",
		createdDate : new Date(),
		modifiedDate : new Date(),
		modifiedBy : $scope.curUser,
		business : ""
	};

			
		$scope.waitForServiceLoad = function() {
			if (appEndpointSF.is_service_ready) {
				
			} else {
				$log.debug("Services Not Loaded, watiting...");
				$timeout($scope.waitForServiceLoad, 1000);
			}
		}
		$scope.waitForServiceLoad();
		

	$scope.close = function() {
		$mdSidenav('right').close().then(function() {
			$log.debug("close RIGHT is done");
		});
	};

	$scope.showSimpleToast = function() {
		$mdToast.show($mdToast.simple().content('Account Data Saved!')
				.position("top").hideDelay(3000));
	};

	$scope.back = function() {
		window.history.back();
	}
});
