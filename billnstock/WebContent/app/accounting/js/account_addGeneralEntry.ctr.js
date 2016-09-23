var app = angular.module("stockApp");

app.controller("addAccountGeneralEntryCtr", function($scope, $window, $mdToast,
		$timeout, $mdSidenav, $mdUtil, $log, $stateParams, objectFactory,
		appEndpointSF, $mdDialog, $mdMedia) {

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.tempAccountEntity = {
		accountName : "",
		accountType : "",
		accountNo : "",
		description : "",
		displayOrderNo : "",
		contra : "",
		business: $scope.curUser.business
			

	};
	$scope.tempGeneralEntry = {
		date : new Date(),
		narration : "",
		amount : "",
		debitAccount : "",
		creditAccount : ""

	};
	var i, flag;
	$scope.generalAccountDebit = [];
	$scope.generalAccountCredit = [];

	$scope.getAccountList = function() {

		var getlist = appEndpointSF.getAccountService();
		getlist.getAccountList($scope.curUser.business.id).then(function(list) {

			for (i = 0; i < list.length; i++) {
				$scope.generalAccountDebit.push(list[i]);
				$scope.generalAccountCredit.push(list[i]);
			}

		});
	}
	// $scope.getAccountList();

	$scope.callFunction = function(selected) {
		var getlist = appEndpointSF.getAccountService();
		getlist.getAccountList().then(function(list) {
			if (flag != undefined) {
				$scope.generalAccountCredit.push(flag);

			}

			for (i = 0; i < $scope.generalAccountCredit.length; i++) {
		
				if(selected.accountName==$scope.generalAccountCredit[i].accountName){
					$scope.generalAccountCredit.splice(i, 1);
					flag = selected;
				}break;
			}
		});

	}

	$scope.cancelButton = function() {
		window.history.back();

	}
	$scope.addGeneralEntry = function() {

		$scope.tempGeneralEntry.debitAccount = $scope.debitAccount;
		$scope.tempGeneralEntry.creditAccount = $scope.creditAccount;
		var GeneralEntryService = appEndpointSF.getGeneralEntryService();
		GeneralEntryService.addGeneralEntry($scope.tempGeneralEntry).then(
				function(msgBean) {
					$scope.tempGeneralEntry = {};
					$scope.showSimpleToast();
				});
		$scope.debitAccount = "";
		$scope.creditAccount = "";

		$scope.generalEntryForm.$setPristine();
		$scope.generalEntryForm.$setValidity();
		$scope.generalEntryForm.$setUntouched();

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

	$scope.showSimpleToast = function() {
		$mdToast.show($mdToast.simple().content('General Account Entry Saved!')
				.position("top").hideDelay(3000));
	};

	$scope.back = function() {
		window.history.back();
	}
});
