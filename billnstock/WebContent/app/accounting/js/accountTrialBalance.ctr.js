angular.module("stockApp")
.controller("trialBalanceCtr", function($scope,$state,appEndpointSF,$mdToast,$stateParams,$log,$timeout){
	
	
	
	$scope.accountGroupTypeList = [ "ASSETS", "EQUITY",
	    							"LIABILITIES", "INCOME", "EXPENSES",
	    							"OTHERINCOMES", "OTHEREXPENCES" ];
	    					
	    					$scope.noOfGroupTypesLoaded = 0;
	    					$scope.noOfGroupTypesCallsDone = 0;

	    					$scope.totalTypeCount = $scope.accountGroupTypeList.length;
	    					$scope.accountGroupTypeGroupList = [];

	    					$scope.getAccountListByGroup = function(groupObj) {
	    						$scope.accountList = [];
	    						$scope.noOfGroupTypesCallsDone += 1;
	    						
	    						var accountEntryService = appEndpointSF.getAccountEntryService();
	    						accountEntryService.getTrialBalance($scope.curUser.business.id)
	    						.then(function(list) {
	    							
	 							
	    							
	    						});
	   					}
	    					
	    					
	    					
	    					$scope.ddd=function(){
	    						
	    						
	    						var accountEntryService = appEndpointSF.getAccountEntryService();
	    						accountEntryService.getTrialBalance($scope.curUser.business.id)
	    						.then(function(list) {
	    							
	    							 $scope.trialBalanceList=list;  
	    							
	    							
	    						});
	    						
	    					}
	    					
	    						
	    						/*var AccountService = appEndpointSF.getAccountService();
	    						
	    						AccountService.getAccountListByGroupId($scope.curUser.business.id,groupObj.id)
	    								.then(function(list) {
	    									$scope.fnReturnCount = 0;
	    									var maxWaitTime = 1000 * 5;
	    									var currentWaitTime = 0;
	     									for (var i = 0; i < list.length; i++) {
	    										getAccountBalance(list[i]);
	    									}
	    									

	    									$scope.waitForAllAccountData = function() {
	    										if ($scope.fnReturnCount != list.length && currentWaitTime < maxWaitTime) {
	    											$log.debug("Wating for account data in a group:" + groupObj.groupName);
	    											currentWaitTime += 1000;
	    											$timeout($scope.waitForAllAccountData, 1000);											
	    										} else{
	    											$scope.getGroupBalance();
	    											
	    											$scope.noOfGroupTypesLoaded += 1;
	    											
	    											if($scope.noOfGroupTypesLoaded == $scope.noOfGroupTypesCallsDone){
	    												$scope.loading = false;
	    											}
	    											
	    											
	    										}
	    									}
	    									$scope.waitForAllAccountData();														

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
	    						AccountService
	    								.getAccountBalance(accounObj.id)
	    								.then(
	    										function(objResp) {
	    											accounObj.balance = objResp.returnBalance;
	    											$scope.fnReturnCount = $scope.fnReturnCount + 1;										
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
	    											$scope.GroupList = list;
	    											$scope.accountList = [];
	    											groupTypeObj.groupList = list;
	    											$scope.totalTypeCount--;											
	    											if (groupTypeObj.groupList != undefined) {
	    												for (var i = 0; i < groupTypeObj.groupList.length; i++) {
	    													$scope
	    															.getAccountListByGroup(groupTypeObj.groupList[i]);

	    												}
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

	    					$scope.getGroupBalance = function() {					
	    						
	    						for (var i = 0; i < $scope.accountGroupTypeGroupList.length; i++) {
	    							if ($scope.accountGroupTypeGroupList[i].groupList.length > 0) {
	    								for (var j = 0; j < $scope.accountGroupTypeGroupList[i].groupList.length; j++) {

	    									if ($scope.accountGroupTypeGroupList[i].groupList[j].accountList != undefined && $scope.accountGroupTypeGroupList[i].groupList[j].accountList.length > 0) {
	    										$scope.totalBalance = 0;
	    										for (var k = 0; k < $scope.accountGroupTypeGroupList[i].groupList[j].accountList.length; k++) {

	    											$scope.totalBalance = $scope.totalBalance+ $scope.accountGroupTypeGroupList[i].groupList[j].accountList[k].balance;

	    										}
	    										$scope.accountGroupTypeGroupList[i].groupList[j].totalBalance = $scope.totalBalance;
	    									}								

	    								}
	    							}
	    						}						
	    					}
*/
	    					$scope.waitForServiceLoad = function() {
	    						if (appEndpointSF.is_service_ready) {

	    							for (var i = 0; i < $scope.accountGroupTypeList.length; i++) {
	    								groupTypeObj = getGroupTypeObject($scope.accountGroupTypeList[i]);
	    								$scope.accountGroupTypeGroupList.push(groupTypeObj);
	    								$scope.getAccountGroupListByType(groupTypeObj);
	    								$scope.getAccountListByGroup(groupTypeObj);
	    							}

	    						} else {
	    							$log.debug("Services Not Loaded, watiting...");
	    							$timeout($scope.waitForServiceLoad, 1000);
	    						}
	    					}
	    				//	$scope.waitForServiceLoad();
	    					$scope.ddd();
})