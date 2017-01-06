var app = angular.module("stockApp");

app.controller("voucherRecieptCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,
		$mdDialog, $mdMedia, $state) {


	$scope.vouchersReview1 = $stateParams.Account;
	$scope.accountId = $stateParams.AccountId;
	var i,flag,ReceiptVoucherEntity="ReceiptVoucherEntity";// = 0;

	var vouchersRe = function(){
		return{
		accountType1 : "",
		accountType2 : "",
		amount : "",
		narration : "",
		business: $scope.curUser.business}
	};
	$scope.vouchersRe = vouchersRe();
	$scope.vaccounts1 = [];
	$scope.vaccounts2 = [];
	$scope.getAccountList = function() {

		var accountService = appEndpointSF.getAccountService();
		accountService.getAccountList($scope.curUser.business.id).then(function(list) {
			for (var x = 0; x < list.length; x++) {
				$scope.vaccounts1.push(list[x]);
				$scope.vaccounts2.push(list[x]);
$scope.s=x;
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
		voucherService.addvoucherReciept($scope.vouchersRe).then(function() {

			$scope.showAddToast();

			$scope.vouchersRe = vouchersRe();
			$scope.debitCurrentBalance="";	
			$scope.creditCurrentBalance="";
			$scope.voucherRecieptForm.$setPristine();
			$scope.voucherRecieptForm.$setValidity();
			$scope.voucherRecieptForm.$setUntouched();

		});

	}

	$scope.cancle = function() {

		 
			$state.go('accounting', {});
		

	}
	
	$scope.remSelected = function(selected,fl) {
		$scope.loading = true;
		$scope.getAccountEntryByAccountId(selected, fl);
		
			var accountService = appEndpointSF.getAccountService();
			accountService.getAccountList($scope.curUser.business.id).then(function(list) {
				
				if(flag!=undefined)
				$scope.vaccounts2.push(flag);
				
				
				
				for (i = 0; i < $scope.vaccounts2.length; i++) {
					if (selected.accountName == $scope.vaccounts2[i].accountName) {
						// vaccounts2.splice(i,1);

						$scope.vaccounts2.splice(i, 1);
						flag=selected;
						if (selected == $scope.vouchersRe.accountType2) {
							$scope.debitCurrentBalance="";	
							$scope.creditCurrentBalance="";
								$scope.vouchersRe.accountType2 = "";
							$scope.voucherRecieptForm.Account.$touched = true;
						}$scope.loading = false;
						break;
						// $log.debug(value);

					}
				}
			});
	}

	
	
	
	var printDivCSS = new String(
			'<link href="/lib/base/css/angular-material.min.css"" rel="stylesheet" type="text/css">'
					+ '<link href="/lib/base/css/bootstrap.min.css"" rel="stylesheet" type="text/css">')
	$scope.printDiv = function(divId) {
		// window.frames["print_frame"].document.body.innerHTML
		// = printDivCSS
		// + document.getElementById(divId).innerHTML;
		window.frames["print_frame"].document.body.innerHTML = document
				.getElementById(divId).innerHTML;
		window.frames["print_frame"].window.focus();
		window.frames["print_frame"].window.print();
	}

	
	$scope.downloadpdf=function(){
		//	window.location.href ="PdfSales";
			window.open("PdfVouchers?id="+$scope.vouchersReview1.id+"&entityname="+ReceiptVoucherEntity+"&bid="+$scope.curUser.business.id);
		//	myWindow=window.open('PdfSales','mypage.jsp','width=200,height=100'); myWindow.focus();
			
		}
	
	
	
	
	$scope.getAccountEntryByAccountId = function(accId, fl) {
		
		var accountservice=appEndpointSF.getAccountService();
		
		accountservice.getAccountBalance(accId.id).then(function(balance){
			
			
			if(fl==2)
				 $scope.debitCurrentBalance=balance;     
			else
				$scope.creditCurrentBalance=balance;
			
			
		});					
		}
	
	
	

	
});
