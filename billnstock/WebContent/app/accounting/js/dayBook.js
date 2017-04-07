var app = angular.module("stockApp");

app.controller("dayBook", function($scope, $window, $mdToast,
		$timeout, $mdSidenav, $mdUtil, $log, $stateParams, objectFactory,
		appEndpointSF, $mdDialog, $mdMedia) {

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();
$scope.loading=true;
$scope.dayBooklist=[];
var blankDayBookEntry =function() {
	return{
	date : new Date(),
	modifiedBy :$scope.curUser.email_id,
	business: $scope.curUser.business
	}
};
$scope.blankDayBookEntry = blankDayBookEntry();
$scope.getDayBookList = function() {
 var date=$scope.blankDayBookEntry.date.getTime(); 
	var getlist = appEndpointSF.getAccountEntryService();
	getlist.getDayBookList($scope.curUser.business.id,date).then(function(list) {
		$scope.dayBooklist=list;
		$scope.loading=false;

	});
}
$scope.waitForServiceLoad = function() {
	if (appEndpointSF.is_service_ready) {
		$scope.getDayBookList();
		
	} else {
		$log.debug("Services Not Loaded, watiting...");
		$timeout($scope.waitForServiceLoad, 1000);
	}
}
$scope.waitForServiceLoad();



});