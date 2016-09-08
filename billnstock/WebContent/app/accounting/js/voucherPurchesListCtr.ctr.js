var app = angular.module("stockApp");

app.controller("voucherPurchesListCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,$mdDialog,$mdMedia, $state  ) {
	
	
	
	
	
$scope.getPurchesvoucherList=function(){
		
		var voucherServiceList=appEndpointSF.getVoucherService();
		voucherServiceList.listVoucherPurches().then(function(list){
			$scope.purcheslist=list;
			
		
				});
			}


$scope.getPurchesvoucherList();
	
});
