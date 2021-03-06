var app = angular.module("stockApp");

app.controller("voucherRecieptCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,
		$mdDialog, $mdMedia, $state) {


	$scope.vouchersReview1 = $stateParams.Account;
	$scope.accountId = $stateParams.AccountId;
	var i,flag,ReceiptVoucherEntity="ReceiptVoucherEntity";// = 0;
	$scope.loading = true;
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
				
				if(list[x].accountGroup.groupName.trim()!="Sundry Creditors"&&list[x].accountGroup.groupName.trim()!="Cash-in-hand"&&list[x].accountGroup.groupName.trim()!="Bank Accounts")
				{
				$scope.vaccounts2.push(list[x]);
				}
					if(list[x].accountGroup.groupName.trim()=="Sundry Creditors"||list[x].accountGroup.groupName.trim()=="Cash-in-hand"||list[x].accountGroup.groupName.trim()=="Bank Accounts")
						{
				$scope.vaccounts1.push(list[x]);
				}
		}

		});
	}

	
	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getAccountList();
			$scope.loading = false;
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
		
		accountservice.getAccountBalance(accId.id,$scope.curUser.business.id).then(function(balance){
			
			
			if(fl==2)
				 $scope.debitCurrentBalance=balance;     
			else
				$scope.creditCurrentBalance=balance;
			
			
		});					
		}
	
	
	

	
});
