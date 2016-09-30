angular
		.module("stockApp")
		.controller(
				"taskModuleCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $q, $location, $anchorScroll, $state, $stateParams, objectFactory,
						appEndpointSF) {

					$log.debug("Inside taskModuleCtr");
					$location.hash('topRight');
					$anchorScroll();
					var taskService = appEndpointSF.getTaskService();
					var setupService = appEndpointSF.getsetupService();

					$scope.taskStatusList = [ 'OPEN', 'INPROGRESS', 'COMPLETED' ];

					$scope.taskObj = $stateParams.taskObj;
					$scope.action = $stateParams.action;
					// action value can be: listmytask, add, edit, listall

					if ($scope.taskObj) {
						$scope.taskEntity = $scope.taskObj;
						$scope.taskEntity.assignedDate = new Date($scope.taskEntity.assignedDate);
						$scope.taskEntity.estCompletionDate = new Date($scope.taskEntity.estCompletionDate);
					} else {
						$scope.taskEntity = {
							business : $scope.curUser.business,
							assignedBy : $scope.curUser,
							assignedTo : null,
							taskStatus : 'OPEN',
							estCompletionDate: null
						};
					}
					$scope.taskEntityList = [];
					$scope.userList = [];

					$scope.saveTask = function() {
						taskService.saveTask($scope.taskEntity).then(
								function(data) {
									if ($scope.taskObj) {
										$scope.showUpdateToast();
									} else {
										$scope.showAddToast();
									}
									$location.hash('topRight');
									$anchorScroll();
								});
					}

					$scope.changeEditView = function(params) {
						$state.go("taskmanagement.add", params);
					}

					$scope.getAllTasks = function() {
						taskService.getAllTask($scope.curUser.business.id)
								.then(function(resp) {
									$scope.taskEntityList = resp.items;
								});
					}
					
					$scope.getMyAllTask = function() {
						taskService.getMyAllTask($scope.curUser.email_id)
								.then(function(resp) {
									$scope.taskEntityList = resp.items;
								});
					}

					$scope.getUserList = function() {
						setupService
								.getAllUserOfOrg($scope.curUser.business.id)
								.then(
										function(users) {
											$scope.userList = users.items;
											if ($scope.taskObj) {
												$scope.userList
														.forEach(function(user) {
															if (user.id == $scope.taskObj.assignedTo.id) {
																$scope.taskObj.assignedTo = user;
															}
														});
											}
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
							$scope.getUserList();
							if($scope.action == 'listmytask'){
								$scope.getMyAllTask();
							} else if($scope.action == 'listall'){
								$scope.getAllTasks();
							}
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					/* Setup menu */
					$scope.toggleRight = buildToggler('right');
					/**
					 * Build handler to open/close a SideNav; when animation
					 * finishes report completion in console
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