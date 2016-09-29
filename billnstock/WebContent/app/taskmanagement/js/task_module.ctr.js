angular.module("stockApp").controller(
		"taskModuleCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$log, objectFactory, appEndpointSF) {

			$log.debug("Inside taskModuleCtr");

			var taskService = appEndpointSF.getTaskService();

			$scope.taskEntity = {
					assignedBy: $scope.curUser,
					assignedTo: $scope.curUser,
					business: $scope.curUser.business
			};

			$scope.taskEntityList = [];

			$scope.saveTask = function() {
				taskService.saveTask($scope.taskEntity).then(function(data) {
					$scope.showAddToast();
				});
			}

			$scope.getAllTasks = function() {
				taskService.getAllTask($scope.curUser.business.id).then(
				function(resp) {
					$scope.taskEntityList = resp.items;
				});
			}

			$scope.selected = [];
			$scope.query = {
				order : 'name',
				limit : 10,
				page : 1
			};

			$scope.onpagechange = function(page, limit) {
				var deferred = $q.defer();

				$timeout(function() {
					deferred.resolve();
				}, 2000);

				return deferred.promise;
			};

			$scope.onorderchange = function(order) {
				var deferred = $q.defer();

				$timeout(function() {
					deferred.resolve();
				}, 2000);

				return deferred.promise;
			};
			
			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getAllTasks();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();
			

			/* Setup menu */
			$scope.toggleRight = buildToggler('right');
			/**
			 * Build handler to open/close a SideNav; when animation finishes
			 * report completion in console
			 */
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