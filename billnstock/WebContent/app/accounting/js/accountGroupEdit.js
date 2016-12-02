angular
.module("stockApp")
.controller('accountGrpEditCtr',function  ($scope,$log,appEndpointSF,$mdToast,$state,$stateParams/*,$injector,unpr*/){
	
	//$scope.tempAccountGrp={"groupName":"","description":"","displayOrderNo":""};
//	$injector:unpr;
	$scope.tempAccountGrp=$stateParams.record;
	$scope.accountGroupTypeList=["ASSETS", "EQUITY", "LIABILITIES","INCOME","EXPENSES","OTHERINCOMES","OTHEREXPENCES"];
	
	$scope.updateAccountGrp=function(){
		var AccountGroupService=appEndpointSF.getAccountGroupService();
		$log.debug("list:"+angular.toJson($scope.tempAccountGrp));
		AccountGroupService.updateAccountGrp($scope.tempAccountGrp).then( function(s1){
			
				$scope.showSavedToast();
				$scope.tempAccountGrp = {};
		});
		
	};
	$scope.getGroupList=function(){
		
		var AccountGroupService= appEndpointSF.getAccountGroupService();
		AccountGroupService.getAccountGroupList($scope.curUser.business.id).then(function(list){
			$scope.accountGroupList=list;
			
		});
		
	}
	$scope.getGroupList();
	$scope.showSavedToast = function() {
		$mdToast.show($mdToast.simple().content(
				'Account Group Edited ...!').position("top").hideDelay(
				3000));
	};
	$scope.cancelBtn=function(){
		$state.go("accounting.accountGroupList", {  });
		
	}
	
});