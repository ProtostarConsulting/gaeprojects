var app = angular.module("stockApp")
		app.controller(
				"listStockReceiptQCRecordCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams, $mdColors,
						$routeParams, $filter, $location, $anchorScroll,
						objectFactory, appEndpointSF) {

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
					
					$scope.stockreceipt_qcrecordList = [];
					
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();
			
					$scope.tempStockReceiptObj = $stateParams.stockReceiptObj ? $stateParams.stockReceiptObj : null;
					if($stateParams.tempObj){
						$scope.tempStockReceiptObj = $stateParams.tempObj ? $stateParams.tempObj : null;
					}
					
					$scope.fetchStockReceiptQCRecordList = function() {
						$scope.stockreceipt_qcrecordList = $scope.tempStockReceiptObj.recepitQCList;
					}
					
					$scope.printStockReceiptQCRecord = function() {
						var bid = $scope.curUser.business.id;
						window.open("PrintStockReceiptQCRecordPdf?bid=" + bid + "&receiptId=" + $scope.tempStockReceiptObj.id);
					}

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							$scope.fetchStockReceiptQCRecordList();
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					
					$scope.waitForServiceLoad();

					$scope.back = function() {
						window.history.back();
					}
				});
