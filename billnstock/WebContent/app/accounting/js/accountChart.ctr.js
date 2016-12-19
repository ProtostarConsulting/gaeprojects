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
					$scope.accountGroupTypeGroupList = [];

					
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
						window.open("DownloadAccountChartServlet?bid="+$scope.curUser.business.id);
					}
					
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							var AccountGroupService = appEndpointSF
							.getAccountGroupService();
					AccountGroupService.getChartSheet(
									$scope.curUser.business.id).then(
							function(list) {
								$scope.accountGroupTypeGroupList=list;
								$scope.loading = false;
								
							});
							
							
						
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
