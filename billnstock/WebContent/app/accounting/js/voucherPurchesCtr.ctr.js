var app = angular.module("stockApp");

app
		.controller(
				"voucherPurchesCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

					$scope.vouchersSaview1 = $stateParams.Account;
					$scope.accountId = $stateParams.AccountId;
					var i, flag;

					$scope.purchesVouchers = {
						accountType1 : "",
						accountType2 : "",
						amount : "",
						narration : ""
					};

					$scope.vaccounts1 = [];
					$scope.vaccounts2 = [];
					$scope.getAccountList = function() {

						var accountService = appEndpointSF.getAccountService();
						accountService.getAccountList().then(function(list) {
							for (var x = 0; x < list.length; x++) {
								
								if(list[x].accountgroup.groupName.toUpperCase()!="PURCHES")
								{
								$scope.vaccounts1.push(list[x]);
								}
									if(list[x].accountgroup.groupName.toUpperCase()=="PURCHES")
										{
								$scope.vaccounts2.push(list[x]);}

							}

						});
					}

					

					$scope.addvoucher = function() {

						var voucherService = appEndpointSF.getVoucherService();
						voucherService.addvoucherPurches($scope.purchesVouchers).then(
								function() {

									$scope.showAddToast();
									$scope.debitCurrentBalance=" ";	
									$scope.creditCurrentBalance=" ";
									$scope.purchesVouchers = "";
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
								.getAccountList()
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

						} else {
							// $log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();
					
					
					

				});
