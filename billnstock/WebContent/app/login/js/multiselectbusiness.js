angular.module("stockApp").controller("multiSelectBusinessCtr",
		function($scope, $stateParams) {

			$scope.multiUsers = $stateParams.multiUsers;
		});