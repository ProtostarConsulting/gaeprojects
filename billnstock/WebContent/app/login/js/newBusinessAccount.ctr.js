angular.module("stockApp").controller(
		"newbusinessCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$log, $q, $location, objectFactory, appEndpointSF,
				$state) {

			$scope.showSimpleToast = function(msgBean) {
				$mdToast.show($mdToast.simple().content(msgBean)
						.position("top").hideDelay(3000));
			};

			//////////////////////////////////////////////////////////////////////////////////

			$scope.curuser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

		
			$scope.business = {
					businessName : "",
					accounttype:""
				}
				$scope.userEntity={
						business:"",
						email_id : "",
						firstName : "",
						lastName : "",	
						authority:[],
						isGoogleUser:true
				}

				$scope.addBusiness = function() {

					var proadminService = appEndpointSF.getproadminService();
					proadminService.getAccountTypeById($scope.accounttype).then(
							function(assetList) {
								$scope.business.accounttype = assetList.result;
					
										var UserService = appEndpointSF.getUserService();
											UserService.addBusiness($scope.business).then(
													function(business) {
														$scope.userEntity.business=business.result;
														$scope.userEntity.authority.push("admin");
														UserService.addUser($scope.userEntity).then(function(msg){
														$scope.showAddToast();
														$state.go("login");

														});
											});
							});
					
				}
			
			
			
			
			$scope.condition = function() {
				if ($scope.userEntity.isGoogleUser == false) {
					return true;
				} else {
					return false
				}
			}
			
			
			
	/* get Account Type */
			
			$scope.getallAccountType = function() {
				var proadminService = appEndpointSF.getproadminService();
				proadminService.getallAccountType().then(function(assetList) {
					$scope.accountlist = assetList.items;
				
				});
			}
			$scope.accountlist = [];
		
			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getallAccountType();
				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();
		
			
//check email already exists
			
			$scope.Checkemail=function(emailid){
				
				var UserService = appEndpointSF	.getUserService();
				UserService.isUserExists(emailid).then(function(responce) {
									if (responce.result.returnBool== true) {
						 $scope.userexists="user already exists"
							 angular.element(document.getElementById('adminFirstName'))[0].disabled = true;
						 angular.element(document.getElementById('adminLastName'))[0].disabled = true;
											
					 }else{
						 $scope.userexists="";
						 angular.element(document.getElementById('adminFirstName'))[0].disabled = false;
						 angular.element(document.getElementById('adminLastName'))[0].disabled = false;

					 }
					
				});
				
				}
			$scope.user11=[];
			$scope.userexist="";
			
			// //////////////////////////////////////////////////////////////////////////////

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