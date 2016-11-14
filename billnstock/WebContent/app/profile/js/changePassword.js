angular
		.module("stockApp")
		.controller(
				"changepass",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF) {
					
					$scope.showSimpleToast = function(msgBean) {
						$mdToast.show($mdToast.simple().content(msgBean)
								.position("top").hideDelay(3000));
					};
					
					$scope.printempidsalslip = $stateParams.printempidsalslip;
					$scope.viewsalslips = $stateParams.viewsalslips;
					$scope.curuser = appEndpointSF.getLocalUserService().getLoggedinUser();
	
					$scope.getuserById = function() {
						$log.debug("Inside Ctr $scope.getuserById");
						var UserService = appEndpointSF.getUserService();
						if (typeof $scope.curuser.id != 'undefined') {
							UserService.getUserByID($scope.curuser.id).then(
									function(userList) {
										$log.debug("Inside Ctr getAllleads");
										$scope.userL = userList.result;

									});
						}
					}

					$scope.userL = [];
					

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.getuserById();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();
				
					$scope.changepass=function(){
					
							if ($scope.password == $scope.confirmpassword) {
								$scope.savemsg=true;
								$scope.checkpass=false;
							} else {
								$scope.checkpass=true;
								$scope.savemsg=false;
							}
							
							if($scope.savemsg==true){
							/*$scope.userL.modifiedBy=$scope.curUser.email_id;*/
							$scope.userL.password=$scope.password;
							var UserService = appEndpointSF.getUserService();
							UserService.updateUser($scope.userL).then(function(msgBean) {
								$scope.showSimpleToast(msgBean.msg); 
															
							});
							}
					}
					
					
			
					$scope.inputType1 = 'password';      
					$scope.inputType2 = 'password'; 
					
					$scope.showpass1=function(){
						if($scope.inputType1=='password'){
						  $scope.inputType1 = 'text';
						}
						else{
							$scope.inputType1 = 'password';
						}
						
				}
					$scope.showpass2=function(){
						if($scope.inputType2=='password'){
							  $scope.inputType2 = 'text';
							}
							else{
								$scope.inputType2 = 'password';
							}
					}
					$scope.setpassinput1=function(){
						$scope.inputType1 = 'password';
					}
					$scope.setpassinput2=function(){
						$scope.inputType2 = 'password';
					}
					
					
					//----------------------------------------------
					
					$scope.toggleRight = buildToggler('right');

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
