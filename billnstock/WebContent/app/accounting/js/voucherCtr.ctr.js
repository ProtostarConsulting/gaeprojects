var app = angular.module("stockApp");

app.controller("voucherCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,
		$mdDialog, $mdMedia, $state) {

	$scope.vouchers1 = $stateParams.Account;
	$scope.accountId = $stateParams.AccountId;
	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();
	var i,flag;// = 0;

	var blankVouchers = function(){
		return{
		accountType1 : "",
		accountType2 : "",
		amount : "",
		narration : "",
		business: $scope.curUser.business
		}
	};
	$scope.vouchers = blankVouchers();

	$scope.vaccounts1 = [];
	$scope.vaccounts2 = [];
	$scope.getAccountList = function() {

		var accountService = appEndpointSF.getAccountService();
		accountService.getAccountList().then(function(list) {
			for (var x = 0; x < list.length; x++) {
				$scope.vaccounts1.push(list[x]);

				$scope.vaccounts2.push(list[x]);

			}

		});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getAccountList();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}
	$scope.waitForServiceLoad();

	

	$scope.addvoucher = function() {

		var voucherService = appEndpointSF.getVoucherService();
		voucherService.addvoucher($scope.vouchers).then(function() {

			$scope.showAddToast();

			$scope.vouchers = blankVouchers();
			$scope.voucherForm.$setPristine();
			$scope.voucherForm.$setValidity();
			$scope.voucherForm.$setUntouched();

		});

	}

	$scope.cancle = function(accountId) {

		if (accountId != undefined) {
			$state.go('accounting', {});
		} else {
			$state.go('accounting', {});
		}

	}
	
	$scope.remSelected = function(selected) {
		
			var accountService = appEndpointSF.getAccountService();
			accountService.getAccountList().then(function(list) {
				
				if(flag!=undefined)
				$scope.vaccounts2.push(flag);
				
				
				
				for (i = 0; i < $scope.vaccounts2.length; i++) {
					if (selected.accountName == $scope.vaccounts2[i].accountName) {
						

						$scope.vaccounts2.splice(i, 1);
						flag=selected;
						break;
						

					}
				}
			});
	}

});
