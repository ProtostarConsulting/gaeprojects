angular.module("stockApp").controller(
		"setup.departmentList",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, $mdMedia, $mdDialog, Upload,
				appEndpointSF) {
			$scope.newDept = false;
			$scope.getdepartment = function() {
				return {
					name : $scope.department2.name,
					parentDept : $scope.department2,
					business : $scope.curUser.business
				}

			};

			$scope.department2 = {
				name : "",
				pname : "",
				business : $scope.curUser.business
			};

			$scope.Parentdepartment = [ "HR", "Support", "Technical", "Admin",
					"Production", "Office" ];

			$scope.saveDepartment = function() {
				$scope.deptment = $scope.getdepartment();

				var userService = appEndpointSF.getUserService();

				userService.addEmpDepartment($scope.department2).then(
						function() {
							$scope.showAddToast();
						});
			}
			$scope.dept = function() {
				$scope.newDept = true;
			}
			$scope.back = function() {
				$scope.getDepartmentList();

			}

			$scope.getDepartmentList = function() {
				$scope.newDept = false;
				$scope.deptment = $scope.getdepartment();

				var userService = appEndpointSF.getUserService();

				userService.getDepartmentList($scope.curUser.business.id).then(
						function(list) {
							$scope.departmentList = list;
						});

			}
			$scope.getDepartmentList();

		});
