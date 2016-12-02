angular.module("stockApp").controller(
		"needbusinessCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$log, $q, $location, objectFactory, appEndpointSF,
				$state) {

			$scope.showSavedToast = function(msg) {
				$mdToast.show($mdToast.simple().content(msg).position("top")
						.hideDelay(3000));
			};

			// ////////////////////////////////////////////////////////////////////////////////

			$scope.curuser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			$scope.business = {
				businessName : "",
				accounttype:""
			}
			$scope.userEntity={
					business:"",
					email_id : $scope.curuser.email_id,
					firstName : $scope.curuser.firstName,
					lastName : $scope.curuser.lastName,	
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

			/* get Account Type */
			
			$scope.getallAccountType = function() {
				var proadminService = appEndpointSF.getproadminService();
				proadminService.getallAccountType().then(function(assetList) {
					$scope.accountlist = assetList.items;
				});
			}
			$scope.accountlist = [];
			$scope.getallAccountType();
			

			
	
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