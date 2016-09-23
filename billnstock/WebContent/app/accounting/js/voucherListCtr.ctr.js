var app = angular.module("stockApp");

app.controller("voucherListCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,$mdDialog,$mdMedia, $state  ) {
	
	
	$scope.vouchersSaview1 = $stateParams.Account;
	
	
	
	
	
	$scope.vouchersSaview1 = {
			accountType1 : "",
			accountType2 : "",
			amount : "",
			narration : ""
		};
	
	

	$scope.voucherList=function(){
		
		var voucherServiceList=appEndpointSF.getVoucherService();
		voucherServiceList.listVoucher($scope.curUser.business.id).then(function(list){
			$scope.Voucheraccounts=list;
		
				});
			}
	
	
/*	$scope.voucherList();
	*/

});
