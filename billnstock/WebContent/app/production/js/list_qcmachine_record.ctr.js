var app = angular.module("stockApp")
		app.controller(
				"qcmachineRecordListCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams, $mdColors,
						$routeParams, $filter, $location, $anchorScroll,
						objectFactory, appEndpointSF) {

					$scope.loading = true;
					function reSetQuery() {
						return {
							order : '-qcName',
							limit : 50,
							page : 1,
							totalSize : 0,
							pagesLoaded : 0
						};
					}
					$scope.query = reSetQuery();
					
					$scope.qcmachine_recordList = [];
					
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
					
					$scope.fetchQCMachineRecordList = function() {
						
						var productService = appEndpointSF.getProductionService();
						productService.getQCMachineRecordList($scope.curUser.business.id).then(function(list) {
							$scope.qcmachine_recordList = list;
							$scope.loading = false;
						});
					}
					
					$scope.printQCMachineRecord = function(qcRecordId) {
						var bid = $scope.curUser.business.id;
						window.open("PrintQCMachineRecordPdf?bid=" + bid + "&qcRecordId=" + qcRecordId);
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchQCMachineRecordList();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					
					$scope.waitForServiceLoad();
				});
