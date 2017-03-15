var app = angular.module("stockApp");

app.controller("voucherPurchesListCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,$mdDialog,$mdMedia, $state  ) {
	
	
	
	
	
$scope.getPurchesvoucherList=function(){
		
		var voucherServiceList=appEndpointSF.getVoucherService();
		voucherServiceList.listVoucherPurches($scope.curUser.business.id).then(function(list){
			$scope.purcheslist=list;
			
		
				});
			}

$scope.waitForServiceLoad = function() {
	if (appEndpointSF.is_service_ready) {
		$scope.getPurchesvoucherList();
	} else {
		$log.debug("Services Not Loaded, watiting...");
		$timeout($scope.waitForServiceLoad, 1000);
	}
}
$scope.waitForServiceLoad();

	
});
