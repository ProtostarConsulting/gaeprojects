angular
		.module("stockApp")
		.controller(
				"login",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF,$state) {
					
					$scope.loading = false;
					
					$scope.login=function(userid){
						$scope.loading = true;
						 var loginService = appEndpointSF.getloginService();
							
						 loginService.getuserById(userid).then(
								function(userList) {
									$scope.loading = false;
									$log.debug("Inside Ctr getAllleads");
									$scope.userL = userList[0];
									if (typeof $scope.userL === 'undefined')
									{	alert("undefined user name");
									}else if($scope.password==$scope.userL.password){
										alert("login successfully");
										$state.go('homecall',{userauthoritys:$scope.userL.authority});
										 
										/*for(i=0;i<$scope.userL.authority.length;i++){
											if($scope.userL.authority[i]=='invoice')
												$scope.loader.invoice = true ;
											if($scope.userL.authority[i]=='hr')
												$scope.loader.hr = true ;
											if($scope.userL.authority[i]=='sales')
												$scope.loader.sales = true ;
											if($scope.userL.authority[i]=='crm')
												$scope.loader.crm = true ;
											if($scope.userL.authority[i]=='setup')
												$scope.loader.setup = true ;
											if($scope.userL.authority[i]=='login')
												$scope.loader.login = true ;
										}*/
									//angular.element(document.getElementById('hr'))[0].disabled = false;
								}else{
										alert("invalid password");
										
									}
								});
						 
						 
						
					}
					$scope.userL = [];
					
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

				})/*.controller('AppCtrl',
						function($scope, $timeout, $mdSidenav, $mdUtil, $log) {
					
					$scope.loader = {
							invoice: true,
							hr:false,
							sales:false,
							crm:false,
							setup:false,
							login:false
							 };
					
					
					$scope.toggleLeft = buildToggler('left');
					// $scope.toggleRight = buildToggler('right');
					*//**
					 * Build handler to open/close a SideNav; when animation finishes
					 * report completion in console
					 *//*
					function buildToggler(navID) {
						var debounceFn = $mdUtil.debounce(function() {
							$mdSidenav(navID).toggle().then(function() {
								$log.debug("toggle " + navID + " is done");
							});
						}, 200);
						return debounceFn;
					}
				})*/;
