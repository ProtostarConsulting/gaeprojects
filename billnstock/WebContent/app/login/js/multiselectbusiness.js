angular.module("stockApp").controller("multiSelectBusinessCtr",
		function($scope, $localStorage) {

			// $scope.multiUsers = $stateParams.multiUsers;
			$scope.multiUsers = $localStorage.multiUsers
			$localStorage.multiUsers = null;
		});