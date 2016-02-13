angular
		.module("prostudyApp")
		.controller(
				"attendanceAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, objectFactory, appEndpointSF, $filter,
						standardList, $state, subjectList) {

					$log.debug("Inside attendanceAddCtr");
					$scope.showSavedToast = function() {
						$mdToast.show($mdToast.simple().content(
								'attendance Saved!').position("top").hideDelay(
								3000));
					};

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.standards = [ {} ];
					$scope.standards = standardList;

					$scope.subjects = [ {} ];
					$scope.subjects = subjectList;

					$scope.students = [];
					$scope.attendanceRecordList = [];
					$scope.isActive = false;
					$scope.selectedSubject;
					$scope.selectedStandard;
					$scope.firstName;
					$scope.lastName;

					$scope.getStudentsByInstitute = function() {

						var UserService = appEndpointSF.getUserService();
						UserService
								.getUserByInstitute($scope.curUser.instituteID)
								.then(
										function(studentList) {
											$scope.newStudents = studentList;

											for (var i = 0; i < $scope.newStudents.length; i++) {
												if ($scope.newStudents[i].role == "Student") {
													if ($scope.newStudents[i].standard == $scope.selectedStandard) {
														$scope.students
																.push($scope.newStudents[i]);

													}
												}
											}

											for (var i = 0; i < $scope.students.length; i++) {
												$scope.attendanceRecordList
														.push($scope.getAttendanceRecord($scope.students[i].id));
												$scope.firstName = $scope.students[i].firstName;
												$scope.lastName = $scope.students[i].lastName;

											}

										});
					}

					$scope.getAttendanceRecord = function(studID) {

						return {
							studID : studID,
							instituteID : $scope.curUser.instituteID,
							firstName : $scope.firstName,
							lastName : $scope.lastName,
							standard : $scope.selectedStandard,
							subject : $scope.selectedSubject,
							date : new Date(),
							attendance : true,

						};
					}

					$scope.toggleAttendance = function(index) {
						$scope.attendanceRecordList[index].attendance = !$scope.attendanceRecordList[index].attendance;
						$log.debug("$scope.attendanceRecordList :"+ angular.toJson($scope.attendanceRecordList));
					}

					$scope.submitAttendance = function() {

						var AttendanceService = appEndpointSF
								.getAttendanceService();
						for (i = 0; i < $scope.attendanceRecordList.length; i++) {
							AttendanceService.addAttendance(
									$scope.attendanceRecordList[i]).then(
									function(msgBean) {

										
									});
						}
						$scope.showSavedToast();

					}

					$scope.isActive = false;

					$scope.toggleActive = function() {
						$scope.isActive = !$scope.isActive;
					};

					$scope.cancel = function() {
						$state.go('attendance');
					}

				});