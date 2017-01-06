var app = angular.module("stockApp");

app
		.controller(
				"voucherPurchesCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {
					$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();
					$scope.vouchersPuraview = $stateParams.Account;
					$scope.accountId = $stateParams.AccountId;
					var i, flag,PurchesVoucherEntity="PurchaseVoucherEntity";
					$scope.loading = true;
					var purchesVouchers = function(){
						return{
						accountType1 : "",
						accountType2 : "",
						amount : "",
						narration : "",
						isCash:true,
						accdetail:"",
						item:"",
						business: $scope.curUser.business}
					};
					$scope.purchesVouchers = purchesVouchers();

					$scope.vaccounts1 = [];
					$scope.vaccounts2 = [];
					$scope.getAccountList = function() {
						$scope.loading = true;
						var accountService = appEndpointSF.getAccountService();
						accountService.getAccountList($scope.curUser.business.id).then(function(list) {
							for (var x = 0; x < list.length; x++) {
								
								if(list[x].accountgroup.groupName.trim()!="PurchaseAccounts")
								{
								$scope.vaccounts1.push(list[x]);
								}
									if(list[x].accountgroup.groupName.trim()=="PurchaseAccounts")
										{
								$scope.vaccounts2.push(list[x]);}
									
									$scope.loading = false;
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
							window.open("PdfVouchers?id="+	$scope.vouchersPuraview.id+"&entityname="+PurchesVoucherEntity+"&bid="+$scope.curUser.business.id);
						//	myWindow=window.open('PdfSales','mypage.jsp','width=200,height=100'); myWindow.focus();
							
						}
					
					
					
					
					
					
					
					
					
					

					$scope.addvoucher = function() {
						if($scope.purchesVouchers.isCash==true){$scope.purchesVouchers.accdetail=""}

						var voucherService = appEndpointSF.getVoucherService();
						voucherService.addvoucherPurches($scope.purchesVouchers).then(
								function() {

									$scope.showAddToast();
									$scope.debitCurrentBalance=" ";	
									$scope.creditCurrentBalance=" ";
									$scope.purchesVouchers = purchesVouchers();
									$scope.voucherPurchesForm.$setPristine();
									$scope.voucherPurchesForm.$setValidity();
									$scope.voucherPurchesForm.$setUntouched();

								});

					}
				

					$scope.cancle = function() {

						$state.go('accounting', {});

					}

					$scope.back = function() {

						$state.go('accounting.voucherSalesList', {});

					}

					$scope.remSelected = function(selected, fl) {

						$scope.getAccountEntryByAccountId(selected, fl);

						
						var accountService = appEndpointSF.getAccountService();
						accountService
								.getAccountList($scope.curUser.business.id)
								.then(
										function(list) {

											if (flag != undefined) {
												$scope.vaccounts2.push(flag);

											}

											for (i = 0; i < $scope.vaccounts2.length; i++) {

												if (selected.accountName == $scope.vaccounts2[i].accountName) {

													$scope.vaccounts2.splice(i,	1);

													flag = selected;
													if (selected == $scope.purchesVouchers.accountType2) {
														$scope.debitCurrentBalance=" ";	
														$scope.creditCurrentBalance=" ";
														$scope.purchesVouchers.accountType2 =" ";
														$scope.voucherPurchesForm.Account2.$touched=true;														
													}

													break;

												}
											}
										});

					}

				
					$scope.getAccountEntryByAccountId = function(accId, fl) {
						
						var accountservice=appEndpointSF.getAccountService();
						
						accountservice.getAccountBalance(accId.id).then(function(balance){
							
							
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
					
					
					
					$scope.waitForServiceLoad();

				});
