var app = angular.module("stockApp");

app.controller("voucherSalesListCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,$mdDialog,$mdMedia, $state  ) {
	
	


	$scope.voucherList=function(){
		
		var voucherServiceList=appEndpointSF.getVoucherService();
		voucherServiceList.getlistSalesVoucher($scope.curUser.business.id).then(function(list){
			$scope.Voucheraccounts=list;
			
		
				});
			}
	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.voucherList();

		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}
//	$scope.waitForServiceLoad();

	$scope.voucherList();
	

});
