angular
		.module("prostudyApp")
		.controller(
				"classroomCourseListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,$state,$mdDialog, $mdMedia,Upload,
						$mdUtil, $log, $q, tableTestDataFactory, appEndpointSF) {
										
					
					$scope.courseStateList	=["ACTIVE","ARCHIVED","PROVISIONED","DECLINED" ];
					$scope.courseState="ACTIVE";
					$scope.classroomCourses = [];
					$scope.courseList=[];				
					$scope.teacherList=[];				
					$scope.tempCourse = {
							'name' : "",
							'section' : "",
							'descriptionHeading' : "",
							'description' : "",
							'room' : "",
							'ownerId' : "me",
							'enrollmentCode' : "",
							'courseState': " ",
							'alternateLink' : ""
						};
					
					$log.debug("$scope.$parent.courseListBackup: " + $scope.$parent.courseListBackup);
					$scope.listCourses = function() {
						$log.debug("Inside listCourses..");						
						$scope.loading = true;
						//Empty data first, needed for refresh button
						$scope.classroomCourses = [];
						$scope.courseList=[];				
						$scope.teacherList=[];	
						
						
						var request = gapi.client.classroom.courses.list({
							pageSize : 500
						});

						request.execute(function(resp) {
							var courses = resp.courses;						

							if (courses.length > 0) {
								$scope.classroomCourses = courses;
								$scope.$parent.courseListBackup = courses;
								var tempCount = 0;
								for (i = 0; i < courses.length; i++) {
									var request = gapi.client.classroom.courses.teachers
									.list({
										courseId : courses[i].id,
										pageSize : 3
									});

									request.execute(function(resp) {
										var teachers = resp.result.teachers?resp.result.teachers:[];
										/*if(teachers.length ==0)
											return;*/
										
										$scope.teacherList = $scope.teacherList.concat(teachers);
										tempCount++;
										// if this is the last course teachers
										// we
										// got
										if(courses.length == tempCount){
											$scope.$apply(function(){
												$scope.$parent.teacherListBackup = $scope.teacherList;
												$scope.loading = false;
											});
										}
									});						
																	
								}
								
								$scope.selectedCourseList();
							} else {
								$log.debug('No courses found.');
							}
							
							// $scope.loading = false;
							$scope.$apply(function(){
								$scope.loading = false;
							});
							
							$log.debug("$scope.courseListBackup: " + $scope.courseListBackup);
							$log.debug("Inside listCourses...Done loading...");

						});
						
					}	
					
					/* This method filters the course by selected state */
					$scope.selectedCourseList = function() {					
						$scope.courseList=[];
							if ($scope.classroomCourses.length > 0) {
								for (i = 0; i < $scope.classroomCourses.length; i++) {
									if($scope.classroomCourses[i].courseState===$scope.courseState)
									{									
									$scope.courseList.push($scope.classroomCourses[i]);
									}
								}								
							} else {
								$log.debug('No courses found.');
							}		

					}				
					
					/* This method filters the course by selected state */
					$scope.getTeacherNamesByCourse = function(courseId) {					
							var courseTeachers = [];							
								for (i = 0; i < $scope.teacherList.length; i++) {									
									if($scope.teacherList[i].courseId == courseId)
									{									
										courseTeachers.push($scope.teacherList[i].profile.name.fullName);
									}
								}
							return courseTeachers.join();	
					}
					
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							if($scope.$parent.courseListBackup === null){
								$scope.listCourses();
							}
							else{
								$log.debug('Using Cached List of Courses...');
								$scope.classroomCourses = $scope.$parent.courseListBackup;
								$scope.teacherList = $scope.$parent.teacherListBackup;
								$scope.selectedCourseList();
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

					$scope.deleteCourse = function(courseId,ev) {							
						
						var confirm = $mdDialog.confirm().title(
						'Are you sure you want to delete this course?').ariaLabel('Lucky day')
						.targetEvent(ev).ok('YES').cancel('NO');
						$mdDialog.show(confirm).then(function() {
					

					$scope.loading = true;
					$scope.selected = [];
					$scope.deleting = true;
					var request = gapi.client.classroom.courses.delete({id:courseId});

					request.execute(function(resp) {
						$log.debug("resp:" + angular.toJson(resp));
						$scope.showCourseDeletedToast();
					
						$scope.deleting = false;
						$scope.classroomCourses=[];
						
						$scope.searchName="";
						$scope.listCourses();
					});
					
					
					
				}, function() {							
					
				});
				
						
					}				
									
					$scope.changeCourseState = function(courseState,ev) {
						
						var confirm = $mdDialog.confirm().title(
						'Are you sure you want to change course state?').ariaLabel('Lucky day')
						.targetEvent(ev).ok('YES').cancel('NO');
						$mdDialog.show(confirm).then(function() {
					
					$scope.selected[0].courseState=courseState;
					$scope.tempCourse=angular.toJson($scope.selected[0]);
				
					var request = gapi.client.classroom.courses.update({id:$scope.selected[0].id},$scope.tempCourse);
					
					request.execute(function(resp) {
						
						$scope.showCourseStateChangedToast();
						$state.go("gfe.classroomCourseList",{});
						
					});
					
					
				}, function() {							
					
				});										
				
					}						
					
					$scope.showCourseStateChangedToast = function() {
						$mdToast.show($mdToast.simple().content(
								'Selected Course State Changed!').position("top").hideDelay(
								3000));
					};
					$scope.showCourseDeletedToast = function() {
						$mdToast.show($mdToast.simple().content(
								'Selected Course Deleted!').position("top").hideDelay(
								3000));
					};
					$scope.showSavedToast = function() {
						$mdToast.show($mdToast.simple().content(
								'New Course Saved!').position("top").hideDelay(
								3000));
					};					
					$scope.createCourse = function(tempCourse) {
						
						$scope.creating = true;
						var request = gapi.client.classroom.courses
								.create(tempCourse);

						request.execute(function(resp) {
							
							$scope.showSavedToast();
							// $state.go("gfe.classroomCourseList",{});
							// $scope.sendEmailMessage();
						});
					}
						$scope.UplodeExcel = function(ev) {
							var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
									&& $scope.customFullscreen;
							$mdDialog
									.show(
											{
												controller : DialogController,
												templateUrl : '/app/gfe/classroom_uploadcourselist.html',
												parent : angular.element(document.body),
												targetEvent : ev,
												clickOutsideToClose : true,
												fullscreen : useFullScreen,
												locals : {
													createCourseRef: $scope.createCourse
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

						function DialogController($scope, $mdDialog, createCourseRef) {

							$scope.csvFile;
							$scope.uploadProgressMsg = null;
							
							$scope.uploadCourseListCSV = function() {
								var csvFile = $scope.csvFile;
								Upload
										.upload(
												{
													url : 'UploadCourseListServlet',
													data : {
														file : csvFile,
													}
												})
										.then(
												function(resp) {
													$log.debug('Successfully uploaded '
																	+ resp.config.data.file.name
																	+ '.'
																	+ angular
																			.toJson(resp.data));
													$scope.uploadProgressMsg = 'Successfully uploaded '
															+ resp.config.data.file.name
															+ '.';
													$mdToast.show($mdToast.simple()
																	.content('Course List Uploaded Sucessfully. The uploaded courses will be listed here after sometime. Please refresh the list.')
																	.position("top")
																	.hideDelay(6000));
													$scope.courseList=resp.data;
								                    console.log('Success '+angular.toJson($scope.courseList));
								                  			                    
								                    
								                    for(var i=0; i< $scope.courseList.length;i++)
								                    	{
								                    	   console.log('Success '+angular.toJson($scope.courseList[i]));
								                    	   $scope.courseList[i].ownerId = 'me';
								                    	   $scope.courseList[i].courseState = 'ACTIVE';	
								                    	   
								                    	   var request = gapi.client.classroom.courses
															.create($scope.courseList[i]);

															request.execute(function(resp) {
																console.log('Added Course: ' + angular.toJson(resp));																
															});
								                    	   // createCourseRef($scope.courseList[i]);
								                    	}
								                    $mdDialog.hide();			                    
													$scope.csvFile = null;				
													
												},
												function(resp) {
													$log.debug('Error Ouccured, Error status: '
																	+ resp.status);
													$scope.uploadProgressMsg = 'Error: '
															+ resp.status;
												},
												function(evt) {
													var progressPercentage = parseInt(100.0
															* evt.loaded
															/ evt.total);
													$log.debug('Upload progress: '
																	+ progressPercentage
																	+ '% '
																	+ evt.config.data.file.name);
													$scope.uploadProgressMsg = 'Upload progress: '
															+ progressPercentage
															+ '% '
															+ evt.config.data.file.name;
													+'...'
												});
							};

						}
						
					
				});