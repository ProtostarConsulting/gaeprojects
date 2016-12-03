var app = angular.module("stockApp");

app.controller("voucherRecieptListCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,$mdDialog,$mdMedia, $state  ) {
	
	


	$scope.voucherList=function(){
		$scope.loading = true;
		var voucherServiceList=appEndpointSF.getVoucherService();
		voucherServiceList.listVoucherReciept($scope.curUser.business.id).then(function(list){
			$scope.VoucherReaccounts=list;
			
			$scope.loading = false;
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
	$scope.waitForServiceLoad();
	

});
