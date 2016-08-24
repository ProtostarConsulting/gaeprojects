angular.module("stockApp")
.controller("accountEditCtr", function($scope,$state,appEndpointSF,$mdToast,$stateParams,$log){
	
	$scope.tempAccount=$stateParams.selectedAccount;
	
	$scope.accountType = [ 'PERSONAL', 'REAL','NOMINAL' ];
	$scope.getGrouplist = function() {

		var listAccountGroupService = appEndpointSF.getAccountGroupService();
		listAccountGroupService.getAccountGroupList()
				.then(
						function(list) {
							/* $log.debug("list:"+angular.toJson(list)); */
							$scope.accountgroupList = list;
							$log.debug("accountgroup: "+ angular
													.toJson($scope.accountgroup));
							/* ajsCache.put(AccountGroupServiceCacheKey,list); */
						});

	}
	$scope.getGrouplist();
	
	$scope.updateAcc=function(){
		var AccountService=appEndpointSF.getAccountService();
		$log.debug("list:"+angular.toJson($scope.tempAccount));
		AccountService.updateAccount($scope.tempAccount).then( function(s1){
			
				$scope.showSavedToast();
				$scope.tempAccount = {};
				$scope.accountForm.$setPristine();
				$scope.accountForm.$setValidity();
				$scope.accountForm.$setUntouched();
		});
		
	};
	$scope.showSavedToast = function() {
		$mdToast.show($mdToast.simple().content(
				'Account Edited ...!').position("top").hideDelay(
				3000));
	};
	$scope.cancelBtn=function(){
		$state.go("accounting.accountlist", {  });
		
	}
	
	
})