angular
		.module("stockApp")
		.controller(
				"taskModuleCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $q, $location, $anchorScroll, $state,
						$stateParams, objectFactory, appEndpointSF, ajsCache) {

					$log.debug("Inside taskModuleCtr");
					$scope.loading = true;

					var taskService = appEndpointSF.getTaskService();
					var setupService = appEndpointSF.getsetupService();

					$scope.taskStatusList = [ 'OPEN', 'INPROGRESS', 'COMPLETED' ];

					$scope.taskObj = $stateParams.taskObj;
					$scope.action = $stateParams.action;
					// action value can be: listmytask, add, edit, listall

					var aMonthBeforeDate = new Date();
					aMonthBeforeDate.setDate(aMonthBeforeDate.getDate() - 30);
					// $log.debug("30 Days Before local Date:"
					// + aMonthBeforeDate.toLocaleString())
					$scope.selectFilterData = {
						assignedDate : aMonthBeforeDate,
						taskStatus : 'ALL',
						assignedBy : null,
						assignedTo : null,
						taskStatusList : angular.copy($scope.taskStatusList),
						userList : []
					}
					$scope.selectFilterData.taskStatusList.unshift('ALL');

					if ($scope.taskObj) {
						$scope.taskEntity = $scope.taskObj;
						$scope.taskEntity.assignedDate = new Date(
								$scope.taskEntity.assignedDate);
						$scope.taskEntity.estCompletionDate = new Date(
								$scope.taskEntity.estCompletionDate);
					} else {
						$scope.taskEntity = {
							business : $scope.curUser.business,
							assignedBy : $scope.curUser,
							assignedTo : null,
							taskStatus : 'OPEN',
							assignedDate : new Date(),
							estCompletionDate : null,
							completionDate : null
						};
					}
					$scope.taskEntityList = [];
					$scope.userList = [];

					$scope.changeEditView = function(params) {
						$state.go("taskmanagement.add", params);
					}
					$scope.printDiv = function(divId) {

						var printDivCSS = new String(
								'<link href="/lib/base/css/angular-material.min.css"" rel="stylesheet" type="text/css">'
										+ '<link href="/lib/base/css/bootstrap.min.css"" rel="stylesheet" type="text/css">')
						/*
						 * document.getElementById('hidetr').style.display =
						 * 'block';
						 */
						window.frames["print_frame"].document.body.innerHTML = printDivCSS
								+ document.getElementById(divId).innerHTML;
						window.frames["print_frame"].window.focus();
						/*
						 * document.getElementById('hidetr').style.display =
						 * 'none';
						 */
						window.frames["print_frame"].window.print();

					}

					$scope.filterTaskList = function(task) {
						return ($scope.selectFilterData.assignedDate == '' || new Date(
								task.assignedDate) >= $scope.selectFilterData.assignedDate)
								&& ($scope.selectFilterData.assignedBy.firstName == 'ALL' || task.assignedBy.id == $scope.selectFilterData.assignedBy.id)
								&& ($scope.selectFilterData.assignedTo.firstName == 'ALL' || task.assignedTo.id == $scope.selectFilterData.assignedTo.id)
								&& ($scope.selectFilterData.taskStatus == 'ALL' || task.taskStatus == $scope.selectFilterData.taskStatus);
					}

					$scope.getDelayInDays = function(assignedDate) {
						var today = new Date();
						var taskAssignedDate = new Date(assignedDate);
						var diffInDays = (today.getTime() - taskAssignedDate
								.getTime())
								/ (1000 * 3600 * 24);
						return Math.ceil(diffInDays);
					}

					$scope.statusChanged = function() {
						if ($scope.taskEntity.taskStatus == 'COMPLETED') {
							$scope.taskEntity.completionDate = new Date()
						} else {
							$scope.taskEntity.completionDate = null;
						}
					}

					$scope.saveTask = function() {
						taskService.saveTask($scope.taskEntity).then(
								function(data) {
									if ($scope.taskObj) {
										$scope.showUpdateToast();
									} else {
										$scope.showAddToast();
										$state.reload();
									}
									$location.hash('topRight');
									$anchorScroll();
								});
					}

					$scope.getAllTasks = function() {
						taskService.getAllTask($scope.curUser.business.id)
								.then(function(resp) {
									$scope.taskEntityList = resp.items;
									$scope.loading = false;
								});
					}

					$scope.getMyAllTask = function() {
						taskService.getMyAllTask($scope.curUser.email_id).then(
								function(resp) {
									$scope.taskEntityList = resp.items;
									$scope.loading = false;
								});
					}

					$scope.getUserList = function() {
						var refresh = false;
						var getAllUserOfOrgCacheKey = "getAllUserOfOrg";
						// Note this key has to be unique across application
						// else it will return unexpected result.
						if (!angular.isUndefined(ajsCache
								.get(getAllUserOfOrgCacheKey))
								&& !refresh) {
							$log.debug("Found List in Cache, return it.")
							postProcessGetUserList(ajsCache
									.get(getAllUserOfOrgCacheKey));
							return;
						} else {
							$log
									.debug("Not Found List in Cache, need to fetch from Server.")
						}
						setupService
								.getAllUserOfOrg($scope.curUser.business.id)
								.then(
										function(users) {
											ajsCache.put(
													getAllUserOfOrgCacheKey,
													users.items);
											postProcessGetUserList(users.items);
										});
					}

					function postProcessGetUserList(userList) {
						$scope.userList = userList;
						$scope.selectFilterData.userList = angular
								.copy($scope.userList);
						var allUserDummy = {
							firstName : 'ALL',
							id : -1
						}
						$scope.selectFilterData.userList.unshift(allUserDummy);
						$scope.selectFilterData.assignedBy = allUserDummy;
						$scope.selectFilterData.assignedTo = allUserDummy;

						if ($scope.taskObj) {
							$scope.userList.forEach(function(user) {
								if (user.id == $scope.taskObj.assignedTo.id) {
									$scope.taskObj.assignedTo = user;
								}
							});
						}

						$scope.loading = false;
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getUserList();
							if ($scope.action == 'listmytask') {
								$scope.getMyAllTask();
							} else if ($scope.action == 'listall'
									|| $scope.action == 'tasklistreport') {
								$scope.getAllTasks();
							}
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

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