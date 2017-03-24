var app = angular.module("stockApp");

app.controller("voucherPaymentListCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,$mdDialog,$mdMedia, $state  ) {
	
$scope.getPaymentvoucherList=function(){
		
		var voucherServiceList=appEndpointSF.getVoucherService();
		voucherServiceList.listvoucherPayment($scope.curUser.business.id).then(function(list){
			$scope.paymentlist=list;
			
		
				});
			}

$scope.waitForServiceLoad = function() {
	if (appEndpointSF.is_service_ready) {
		$scope.getPaymentvoucherList();
	} else {
		$log.debug("Services Not Loaded, watiting...");
		$timeout($scope.waitForServiceLoad, 1000);
	}
}
//$scope.waitForServiceLoad();
$scope.getPaymentvoucherList();

	
});
