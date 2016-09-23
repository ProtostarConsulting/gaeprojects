var app = angular.module("stockApp");

app.controller("generalListCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,
		$mdDialog, $mdMedia, $state) {

	$scope.query = {
		order : 'name',
		limit : 5,
		page : 1
	};

	$scope.getGeneralEntryList = function() {
		var getlist = appEndpointSF.getGeneralEntryService();
		getlist.getGeneralEntryList($scope.curUser.business.id).then(function(list) {
			$scope.journalEntries = list;
			$scope.total = 0;
			for (var i = 0; i < $scope.journalEntries.length; i++) {

				$scope.total += $scope.journalEntries[i].amount;
			}

		});

	}

	
	
	
	$scope.waitForServiceLoad = function() {
	      if (appEndpointSF.is_service_ready) {
	       
	    		$scope.getGeneralEntryList();
	       

	      } else {
	       $log.debug("Services Not Loaded, watiting...");
	       $timeout($scope.waitForServiceLoad, 1000);
	      }
	     }

	

});
