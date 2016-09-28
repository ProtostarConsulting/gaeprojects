var app = angular.module("stockApp");
app.filter('positive', function() {
    return function(input) {
        if (!input) {
            return 0;
        }

        return Math.abs(input);
    };
});

app.controller(
				"accountChartCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory, $state,
						appEndpointSF, $mdDialog, $mdMedia) {

					$scope.loading = true;
						var flag=0;
					$scope.accountGroupTypeList = [ "Assets", "EQUITY",
							"Liabilities", "Incomes", "Expenses",
							"OTHERINCOMES", "OTHEREXPENCES" ];

					$scope.totalTypeCount = $scope.accountGroupTypeList.length;
					$scope.accountGroupTypeGroupList = [];

					$scope.getAccountListByGroup = function(groupObj) {
						$scope.accountList = [];

						var AccountService = appEndpointSF.getAccountService();
						AccountService.getAccountListByGroupId(groupObj.id,$scope.curUser.business.id)
								.then(function(list) {

									for (var i = 0; i < list.length; i++) {
										getAccountBalance(list[i]);

									}

									$scope.accountList = list;
									if (list == null) {
										groupObj.accountList = [];
									} else {
										groupObj.accountList = list;
									}

								})

					};

					function getAccountBalance(accounObj) {
						var AccountService = appEndpointSF.getAccountService();
						AccountService.getAccountBalance(accounObj.id).then(
								function(objResp) {
									accounObj.balance = objResp.returnBalance;
								});
					}

					$scope.getAccountGroupListByType = function(groupTypeObj) {

						var AccountGroupService = appEndpointSF
								.getAccountGroupService();
						AccountGroupService
								.getAccountGroupListByType(
										groupTypeObj.groupType,$scope.curUser.business.id)
								.then(
										
										function(list) {
											flag=0;
											$scope.GroupList = list;
											$scope.accountList = [];
											groupTypeObj.groupList = list;
											$scope.totalTypeCount--;

											if (groupTypeObj.groupList != undefined) {
												for (var i = 0; i < groupTypeObj.groupList.length; i++) {
													$scope.getAccountListByGroup(groupTypeObj.groupList[i]);
												}
											}

											if ($scope.totalTypeCount == 0) {
												$scope.loading = false;
											}

										})

					};

					function getGroupTypeObject(groupTypeValue) {
						return {
							groupType : groupTypeValue,
							groupList : []
						};
					}
					function getGroupAccObject(groupObj) {
						return {
							groupObj : groupObj,
							accountList : []
						};
					}

					
					var printDivCSS = new String(
							'<link href="/lib/base/css/angular-material.min.css"" rel="stylesheet" type="text/css">'
									+ '<link href="/lib/base/css/bootstrap.min.css"" rel="stylesheet" type="text/css">')
					$scope.printDiv = function(divId) {
						
						window.frames["print_frame"].document.body.innerHTML = document
								.getElementById(divId).innerHTML;
						window.frames["print_frame"].window.focus();
						window.frames["print_frame"].window.print();
					}				
				
					
				$scope.saveToPDF = function() {
						document.location.href = "DownloadAccountChartServlet";
					}
					
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {

							
							
							for (var i = 0; i < $scope.accountGroupTypeList.length; i++) {
								
								groupTypeObj = getGroupTypeObject($scope.accountGroupTypeList[i]);
								$scope.accountGroupTypeGroupList
										.push(groupTypeObj);
								$scope.getAccountGroupListByType(groupTypeObj);
								//flag=1;
							
							}

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					
					
				//	$scope.getAccountGroupListByType("Assets");
					
					$scope.waitForServiceLoad();
					
			$scope.capture=function(accountChartDiv) {
				
				/*html2canas(document.getElementById(accountChartDiv),{
					
					onrendered:function canvas(){
						
						var img= canvas.toDataURL("image/png");
						var  doc= new jspdf();
						doc.addImage(img,'JPEG',20,20);
						doc.save('test.pdf');
						
					}
						
				});*/
				
			}
					
				});
