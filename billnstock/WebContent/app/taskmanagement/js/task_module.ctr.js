angular
		.module("stockApp")
		.controller(
				"taskModuleCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $q, $location, $anchorScroll, $state,
						$stateParams, $filter, $mdDialog, $mdMedia,
						objectFactory, appEndpointSF, ajsCache) {

					$log.debug("Inside taskModuleCtr");
					$scope.loading = true;

					var taskService = appEndpointSF.getTaskService();
					var UserService = appEndpointSF.getUserService();

					$scope.taskStatusList = [ 'OPEN', 'INPROGRESS', 'COMPLETED' ];
					// $scope.taskStatusList = [ 'OPEN', 'COMPLETED' ];

					$scope.taskObj = $stateParams.taskObj;
					$scope.action = $stateParams.action;
					// action value can be: listmytask, add, edit, listall

					var sinceDate = new Date();
					sinceDate.setDate(sinceDate.getDate() - 90);
					// $log.debug("30 Days Before local Date:"
					// + sinceDate.toLocaleString())
					var allUserDummy = {
						firstName : 'ALL',
						id : -1
					}
					$scope.selectFilterData = {
						assignedDate : sinceDate,
						taskStatus : 'ALL',
						assignedBy : allUserDummy,
						assignedTo : allUserDummy,
						taskStatusList : angular.copy($scope.taskStatusList),
						userList : []
					}
					$scope.selectFilterData.taskStatusList.unshift('ALL');

					if ($scope.taskObj) {
						$scope.taskEntity = $scope.taskObj;
						$scope.taskEntity.assignedDate = new Date(
								$scope.taskEntity.assignedDate);
						$scope.taskEntity.estCompletionDate = $scope.taskEntity.estCompletionDate ? new Date(
								$scope.taskEntity.estCompletionDate)
								: null;
						$scope.taskEntity.completionDate = $scope.taskEntity.completionDate ? new Date(
								$scope.taskEntity.completionDate)
								: null;

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
					$scope.taskAssignedByMeList = [];
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

					$scope.getDelayInDays = function(task) {
						var today = new Date();
						var taskCompletionDate = task.completionDate? new Date(task.completionDate) : today;
						var taskAssignedDate = new Date(task.assignedDate);
						var diffInDays = (taskCompletionDate.getTime() - taskAssignedDate
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
						$scope.loading = true;
						taskService.saveTask($scope.taskEntity).then(
								function(data) {
									if ($scope.taskObj) {
										$scope.showUpdateToast();
									} else {
										$scope.showAddToast();
										$state.reload();
									}
									$location.hash('tp1');
									$anchorScroll();
									$scope.loading = false;
								});
					}

					$scope.getAllTasks = function() {
						taskService.getAllTask($scope.curUser.business.id)
								.then(function(resp) {
									$scope.taskEntityList = resp.items;
									$scope.loading = false;
								});
					}
					$scope.filterTasksByFitlerData = function() {
						$scope.loading = true;
						var taskEntityFilterData = {
							businessId : $scope.curUser.business.id,
							sinceAssignedDate : $scope.selectFilterData.assignedDate,
							assignedBy : $scope.selectFilterData.assignedBy.firstName == 'ALL' ? null
									: $scope.selectFilterData.assignedBy,
							assignedTo : $scope.selectFilterData.assignedTo.firstName == 'ALL' ? null
									: $scope.selectFilterData.assignedTo,
							taskStatus : $scope.selectFilterData.taskStatus == 'ALL' ? null
									: $scope.selectFilterData.taskStatus,
						}

						taskService.filterTasksByFitlerData(
								taskEntityFilterData).then(
								function(resp) {
									$scope.taskEntityList = resp.items;
									$scope.taskEntityList = $filter(
											'proOrderObjectBySubTextField')(
											$scope.taskEntityList,
											"assignedTo", "firstName");
									$scope.loading = false;
								});
					}

					$scope.getTaskAssignedByMeList = function() {
						$scope.loading = true;
						var taskEntityFilterData = {
							businessId : $scope.curUser.business.id,
							sinceAssignedDate : $scope.selectFilterData.assignedDate,
							assignedBy : $scope.curUser,
							assignedTo : null,
							taskStatus : null
						}

						taskService.filterTasksByFitlerData(
								taskEntityFilterData).then(
								function(resp) {
									$scope.taskAssignedByMeList = resp.items;
									$scope.taskAssignedByMeList = $filter(
											'proOrderObjectBySubTextField')(
											$scope.taskAssignedByMeList,
											"assignedTo", "firstName");
									$scope.loading = false;
								});
					}

					$scope.getUserList = function() {
						var refresh = false;
						var getAllUserOfOrgCacheKey = "all-task-users";
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
						UserService.getUsersByLoginAllowed(
								$scope.curUser.business.id, true).then(
								function(users) {
									var activeUserList = [];
									angular.forEach(users.items,
											function(user) {
												if (user.isActive)
													activeUserList.push(user);
											});
									activeUserList = $filter('orderBy')(
											activeUserList, 'firstName');
									ajsCache.put(getAllUserOfOrgCacheKey,
											activeUserList);
									postProcessGetUserList(activeUserList);
								});
					}

					function postProcessGetUserList(userList) {
						$scope.userList = userList;
						$scope.selectFilterData.userList = angular
								.copy($scope.userList);

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

					$scope.settingsObj = {};

					$scope.addTaskSettings = function() {

						$scope.settingsObj.business = $scope.curUser.business;
						$scope.settingsObj.modifiedBy = $scope.curUser.email_id;

						taskService.addTaskSettings($scope.settingsObj).then(
								function(savedRecoed) {
									$scope.settingsObj = savedRecoed;
									$scope.showUpdateToast();
								});

					}

					$scope.getTaskSettings = function() {

						taskService.getTaskSettingsByBiz(
								$scope.curUser.business.id).then(
								function(settingsList) {

									$scope.settingsObj = settingsList;
									$log.debug("Inside Ctr $scope.settingsObj:"
											+ $scope.settingsObj);
									return $scope.settingsObj;
								});
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getUserList();
							$scope.getTaskSettings();
							if ($scope.action == 'listmytask') {
								// $scope.getMyAllTask();
								$scope.selectFilterData.assignedTo = $scope.curUser;
								$scope.filterTasksByFitlerData();
								$scope.getTaskAssignedByMeList();
							} else if ($scope.action == 'listall') {
								$scope.filterTasksByFitlerData();
							} else if ($scope.action == 'tasklistreport') {
								// $scope.filterTasksByFitlerData();
							}
						} else {
							$log.debug("Services Not Loaded, waiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.addDocumentComment = function(ev, taskEntity,
							editComment) {

						function addDocumentCommentCtr($scope, $mdDialog,
								curUser, taskEntity, taskService) {

							if (!taskEntity.documentComments) {
								taskEntity.documentComments = [];
							}

							$scope.documentComment = {
								addedBy : curUser,
								date : new Date(),
								commentText : ''
							};

							if (editComment) {
								$scope.documentComment = angular
										.copy(editComment);
							}

							$scope.addComment = function() {
								if (!editComment)
									taskEntity.documentComments
											.push($scope.documentComment);

								taskService
										.saveTask(taskEntity)
										.then(
												function(data) {
													if (editComment)
														editComment.commentText = $scope.documentComment.commentText;

													$mdDialog.cancel();
												});

							}

							$scope.cancel = function() {
								$mdDialog.cancel();
							};
						}

						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : addDocumentCommentCtr,
											templateUrl : '/app/taskmanagement/add_comment_dialog.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curUser : $scope.curUser,
												taskEntity : $scope.taskEntity,
												taskService : taskService
											}
										})
								.then(
										function(answer) {
											$scope.status = 'You said the information was "'
													+ answer + '".';
										},
										function() {
											$scope.status = 'You cancelled the dialog.';
										});

					};

					
					$scope.query = {
						order : '-taskStatus',
						limit : 50,
						page : 1
					};
					$scope.query2 = {
						order : '-taskStatus',
						limit : 50,
						page : 1
					};
					$scope.reportQuery = {
						order : 'assignedTo.firstName',
						limit : 100,
						page : 1
					};
					

					$scope.loadStuff = function() {
						$scope.promise = $timeout(function() {
							// loading
						}, 2000);
					}

					$scope.logOrder = function(order) {
						console.log('order: ', order);
					};

					$scope.logPagination = function(page, limit) {
						console.log('page: ', page);
						console.log('limit: ', limit);
					}

				});