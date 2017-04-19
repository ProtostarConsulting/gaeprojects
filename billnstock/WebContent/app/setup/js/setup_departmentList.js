angular.module("stockApp").controller(
		"setup.departmentList",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, $mdMedia, $mdDialog, Upload,
				appEndpointSF) {
			$scope.newDept = false;
			
			$scope.query = {
					order : 'name',
					limit : 50,
					page : 1
				};

			function getEmptyDepartment() {
				return {
					name : "",
					parentDept : null,
					business : $scope.curUser.business
				};
			}

			$scope.newDepartment = getEmptyDepartment()

			$scope.saveDepartment = function() {

				var userService = appEndpointSF.getUserService();

				userService.addEmpDepartment($scope.newDepartment).then(
						function() {
							$scope.showAddToast();
							$scope.newDepartment = getEmptyDepartment()
						});
			}

			$scope.dept = function() {
				$scope.newDept = true;
			}

			$scope.back = function() {
				$scope.getEmpDepartments();
				$scope.newDepartment = getEmptyDepartment();

			}

			$scope.getEmpDepartments = function() {
				$scope.newDept = false;

				var userService = appEndpointSF.getUserService();
				userService.getEmpDepartments($scope.curUser.business.id).then(
						function(list) {
							$scope.departmentList = list.items;
						});

			}

			$scope.editDepartment = function(editListobj) {
				$scope.newDept = true;
				$scope.newDepartment = editListobj;

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
