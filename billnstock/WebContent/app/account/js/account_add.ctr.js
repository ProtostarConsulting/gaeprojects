var app = angular.module("stockApp");

app.controller("bizPlanAddCtr", function($scope, $window, $mdToast, $timeout,
	$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF, $mdDialog, $mdMedia) {

	$log.debug("Inside accountAddCtr");

	$scope.query = {
		order : 'name',
		limit : 50,
		page : 1
	};

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.account = {
		accountName : "",
		description : "",
		createdDate : new Date(),
		modifiedDate : new Date(),
		modifiedBy : '',
		business : ""
	};

	$log.debug("$stateParams.selectedAccountId:",
		$stateParams.selectedAccountId);

	$scope.selectedAccountId = $stateParams.selectedAccountId;

	$scope.getAccountById = function() {
		var accountService = appEndpointSF.getAccountService();
		accountService.getAccountById($scope.selectedAccountId).then(
			function(account) {

				$scope.account = account;
				$scope.tempAccount = account;
				$log.debug("$scope.account" + $scope.account);

			});
	}

	$scope.addAccount = function() {
		$scope.account.business = $scope.curUser.business;
		$scope.account.modifiedBy = $scope.curUser.email_id;
		var accountService = appEndpointSF.getAccountService();
		accountService.addAccount($scope.account).then(function(msgBean) {});
		if ($scope.selectedAccountId == "") {
			$scope.showAddToast();
		} else {
			$scope.showUpdateToast();
		}
		$scope.accountForm.$setPristine();
		$scope.accountForm.$setValidity();
		$scope.accountForm.$setUntouched();
		$scope.account = {};
	}

	$scope.getAllAccountsByBusiness = function() {
		var accountService = appEndpointSF.getAccountService();

		accountService.getAllAccountsByBusiness($scope.curUser.business.id)
			.then(
				function(accountList) {
					$log.debug("Inside Ctr getAllAccountsByBusiness");
					$scope.accounts = accountList;
					$log.debug("Inside Ctr $scope.accounts:"
						+ angular.toJson($scope.accounts));
				});
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getAllAccountsByBusiness();
			if ($scope.selectedAccountId != undefined) {
				$scope.getAccountById();
			}
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}

	$scope.accounts = [];
	$scope.waitForServiceLoad();

	$scope.toggleRight = buildToggler('right');

	function buildToggler(navID) {
		var debounceFn = $mdUtil.debounce(function() {
			$mdSidenav(navID).toggle().then(function() {
				$log.debug("toggle " + navID + " is done");
			});
		}, 200);
		return debounceFn;
	}



	// ----------------------UPLODE EXCEL FILE-------------------------------

	$scope.UplodeExcel = function(ev) {
		var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
		&& $scope.customFullscreen;
		$mdDialog
			.show(
				{
					controller : DialogController,
					templateUrl : '/app/account/UploadExcelAddAccount.html',
					parent : angular
						.element(document.body),
					targetEvent : ev,
					clickOutsideToClose : true,
					fullscreen : useFullScreen,
					locals : {
						curuser : $scope.curUser
					}
				})
			.then(
				function(answer) {
					$scope.status = 'You said the information was "'
						+ answer + '".';
				},
				function() {
					$scope.status = 'You cancelled the dialog.';
				});

	};

	function DialogController($scope, $mdDialog, curuser) {
		$scope.bizID;
		$scope.loding = false;
		$scope.uplodeimage = function() {
			$scope.loding = true;
			document.excelform.action = $scope.AccountsExcelUploadURL;
			document.excelform.submit();
		}


		$scope.getExcelUploadURL = function() {
			var uploadUrlService = appEndpointSF.getuploadURLService();
			uploadUrlService.getAccountExcelUploadURL()
				.then(function(url) {
					$scope.AccountsExcelUploadURL = url.msg;
					$scope.bizID = curuser.business.id;
				});


		}
		$scope.AccountsExcelUploadURL;

		$scope.waitForServiceLoad = function() {
			if (appEndpointSF.is_service_ready) {
				$scope.getExcelUploadURL();
			} else {
				$log.debug("Services Not Loaded, watiting...");
				$timeout($scope.waitForServiceLoad, 1000);
			}
		}
		$scope.waitForServiceLoad();
	}

	// -------------------------------------------------------




	$scope.close = function() {
		$mdSidenav('right').close().then(function() {
			$log.debug("close RIGHT is done");
		});
	};

	$scope.showSimpleToast = function() {
		$mdToast.show($mdToast.simple().content('Account Data Saved!')
			.position("top").hideDelay(3000));
	};

});