  var app = angular.module("stockApp");

app
		.controller(
				"voucherSalesCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

					$scope.vouchersSaview1 = $stateParams.Account;
					$scope.accountId = $stateParams.AccountId;
					var i, flag,SalesVoucherEntity="SalesVoucherEntity",div;
					$scope.loading = true;
					var blankSalesvouchers = function(){
						return{
						accountType1 : "",
						accountType2 : "",
						amount : "",
						narration : "",
						date: new Date(),
						business: $scope.curUser.business
						}
					};
$scope.vouchers=blankSalesvouchers();
					$scope.vaccounts1 = [];
					$scope.vaccounts2 = [];
				

					var printDivCSS = new String(
							'<link href="/lib/base/css/angular-material.min.css"" rel="stylesheet" type="text/css">'
									+ '<link href="/lib/base/css/bootstrap.min.css"" rel="stylesheet" type="text/css">')
					$scope.printDiv = function(divId) {
						// window.frames["print_frame"].document.body.innerHTML
						// = printDivCSS
						// + document.getElementById(divId).innerHTML;
						div=divId;
						window.frames["print_frame"].document.body.innerHTML = document
								.getElementById(divId).innerHTML;
						window.frames["print_frame"].window.focus();
						window.frames["print_frame"].window.print();
					}

					$scope.addvoucher = function() {

						var voucherService = appEndpointSF.getVoucherService();
						voucherService.addvoucher($scope.vouchers).then(
								function() {

									$scope.showAddToast();
									$scope.debitCurrentBalance="";	
									$scope.creditCurrentBalance="";
									$scope.vouchers=blankSalesvouchers();
									$scope.voucherSalesForm.$setPristine();
									$scope.voucherSalesForm.$setValidity();
									$scope.voucherSalesForm.$setUntouched();

								});

					}
				

					$scope.cancle = function() {

						$state.go('accounting', {});

					}

					$scope.back = function() {

						$state.go('accounting.voucherSalesList', {});

					}
					
					
					$scope.getAccountList=function(){
						$scope.loading = true;
					
					var accountService = appEndpointSF.getAccountService();
					accountService.getAccountList($scope.curUser.business.id).then(
									function(list) {
							for(var x=0;x<list.length;x++){
								
								
								if(list[x].accountGroup.groupName.trim()=="Sundry Debtors"||list[x].accountGroup.groupName.trim()=="Cash-in-hand"||list[x].accountGroup.groupName.trim()=="Bank Accounts")
								{
						$scope.vaccounts1.push(list[x]);
						}
								
								if(list[x].accountGroup.groupName.trim()=="Sales Accounts")
								{
						$scope.vaccounts2.push(list[x]);
						}
								
								
								
								}		
						
							$scope.loading = false;
									});
					}
					
					
										
					$scope.getAccountEntryByAccountId = function(accId, fl) {
						
						var accountservice=appEndpointSF.getAccountService();
						
						accountservice.getAccountBalance(accId.id,$scope.curUser.business.id).then(function(balance){
							
							
							if(fl==1)
								 $scope.debitCurrentBalance=balance;     
							else
								$scope.creditCurrentBalance=balance;
							
							
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
			
					$scope.waitForServiceLoad ();
					
					
					$scope.downloadpdf=function(){
					
						window.open("PdfVouchers?id="+$scope.vouchersSaview1.id+"&entityname="+SalesVoucherEntity+"&bid="+$scope.curUser.business.id);
					}

				});
