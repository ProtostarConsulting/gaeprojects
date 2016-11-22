angular.module("stockApp").controller(
		"setup.departmentList",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, $mdMedia, $mdDialog, Upload,
				appEndpointSF) {
			$scope.newDept = false;
			$scope.getdepartment = function() {
				return {
					name : $scope.newDepartment.name,
					parentDept : $scope.newDepartment,
					business : $scope.curUser.business
				}

			};

			$scope.newDepartment = {
				name : "",
				parentDept : null,
				business : $scope.curUser.business
			};

			

			$scope.saveDepartment = function() {
				$scope.deptment = $scope.getdepartment();

				var userService = appEndpointSF.getUserService();

				userService.addEmpDepartment($scope.newDepartment).then(
						function() {
							$scope.showAddToast();
							$scope.newDepartment="";
						});
			}
			$scope.dept = function() {
				$scope.newDept = true;
			}
			$scope.back = function() {
				$scope.getEmpDepartments();
				$scope.newDepartment="";

			}

			$scope.getEmpDepartments = function() {
				$scope.newDept = false;
				$scope.deptment = $scope.getdepartment();

				var userService = appEndpointSF.getUserService();
				userService.getEmpDepartments($scope.curUser.business.id)
						.then(function(list) {
							$scope.departmentList = list.items;
						});

			}			

			$scope.editDepartment = function(editListobj) {
				$scope.newDept = true;
				$scope.newDepartment=editListobj;
				
			}
			
			
			
			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getEmpDepartments();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();

		});
