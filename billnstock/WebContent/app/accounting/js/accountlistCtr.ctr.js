/*var app = angular.module("stockApp");

app.controller("accountlistCtr", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,$mdDialog,$mdMedia, $state  ) {

	$scope.query = {
		order : 'name',
		limit : 5,
		page : 1
	};


	$scope.getAccountList=function(){
		
	var AccountService=appEndpointSF.getAccountService();
	AccountService.getAccountList().then(function(list){
		$scope.accounts=list;
	
			});
		}

	$scope.getAccountList();

	
	
	$scope.delAccByid=function(daccountid){
		
		var delrecord=appEndpointSF.getAccountService();
		
		delrecord.deleteaccByid(daccountid).then(function(){
			$scope.showDelToast();
			$scope.getAccountList();
			
			
		});
		
		
		
	}
	  $scope.getaccountlist();
	
	
	$scope.waitForServiceLoad = function() {
	      if (appEndpointSF.is_service_ready) {
	       
	    	  $scope.getaccountlist();
	       

	      } else {
	       $log.debug("Services Not Loaded, watiting...");
	       $timeout($scope.waitForServiceLoad, 1000);
	      }
	     }
});
*/
var app = angular.module("stockApp");

app.controller("accountlistCtr", function($scope, $window, $mdToast, $timeout,Upload,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,$mdDialog,$mdMedia, $state  ) {

	$scope.query = {
		order : 'name',
		limit : 5,
		page : 1
	};
	$scope.selected=[];

	$scope.getAccountList=function(){
		
	var AccountService=appEndpointSF.getAccountService();
	AccountService.getAccountList().then(function(list){
		$scope.accounts=list;
	
			});
		}
	$scope.getAccountList();
	
	$scope.delAccByid=function(daccountid){
		
		var delrecord=appEndpointSF.getAccountService();
		
		delrecord.deleteaccByid(daccountid).then(function(){
			$scope.showDelToast();
			$scope.getAccountList();
			
			
		});
		
		
		
	}
	
		 $scope.uploadExcel = function(ev) {
			    var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
			      && $scope.customFullscreen;
			    $mdDialog.show(
			        {
			         controller : DialogController,
			         templateUrl : '/app/accounting/UploadAccountList.html',
			         parent : angular.element(document.body),
			         targetEvent : ev,
			         clickOutsideToClose : true,
			         fullscreen : useFullScreen,
			         locals : {}
			                   
			        })
			        .then(
			                function(answer) {
			                 $scope.status = 'You said the information was "'
			                   + answer + '".';
			                },
			                function() {
			                 $scope.status = 'You cancelled the dialog.';
			                });

			           };

			           function DialogController($scope, $mdDialog) {

			            $scope.csvFile;
			            $scope.uploadProgressMsg = null;
			            
			            $scope.uploadAccListCSV = function() {
			             var csvFile = $scope.csvFile;
			             Upload
			               .upload(
			                 {
			                  url : 'UploadAccountsServlet',
			                  data : {
			                   file : csvFile			                 
			                  }
			                 })
			               .then(
			                 function(resp) {
			                  $log.debug('Successfully uploaded '
			                      + resp.config.data.file.name
			                      + '.'
			                      + angular
			                        .toJson(resp.data));
			                  $scope.uploadProgressMsg = 'Successfully uploaded '
			                    + resp.config.data.file.name
			                    + '.';
			                  $mdToast.show($mdToast.simple()
			                      .content('AccountList Uploaded Sucessfully.')
			                      .position("top")
			                      .hideDelay(3000));
			                  $scope.AccountList=resp.data;
			                                 console.log('Success '+angular.toJson($scope.AccountList));
			                                                      
			                                 
			                                 for(var i=0; i< $scope.AccountList.length;i++)
			                                  {
			                                     console.log('Success '+angular.toJson($scope.AccountList[i]));
			                                     addAccount($scope.AccountList[i]);
			                                  }
			                                 $mdDialog.hide();                       
			                  $scope.csvFile = null;    
			                  
			                 },
			                 function(resp) {
			                  $log.debug('Error Ouccured, Error status: '
			                      + resp.status);
			                  $scope.uploadProgressMsg = 'Error: '
			                    + resp.status;
			                 },
			                 function(evt) {
			                  var progressPercentage = parseInt(100.0
			                    * evt.loaded
			                    / evt.total);
			                  $log.debug('Upload progress: '
			                      + progressPercentage
			                      + '% '
			                      + evt.config.data.file.name);
			                  $scope.uploadProgressMsg = 'Upload progress: '
			                    + progressPercentage
			                    + '% '
			                    + evt.config.data.file.name;
			                  +'...'
			                 });
			            };

			           }
			           
			           $scope.downloadExcel= function() {

								document.location.href = "DownloadAccountsServlet";
									

							}
			           
	
	
});
