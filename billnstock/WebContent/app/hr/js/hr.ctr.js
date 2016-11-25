angular
		.module("stockApp")
		.controller(
				"hrCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF) {
					$log.debug("Inside hrCtr");
					var date = new Date();
					var monthNames = [ "Jan", "Feb", "Mar", "Apr", "May",
							"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ];

					$scope.monthNameList = [ "January", "February", "March",
							"April", "May", "June", "July", "August",
							"September", "October", "November", "December" ];

					$scope.monthList = [];
					$scope.prepareMonthList = function() {
						var today = new Date();
						var thisMonth = new Date(today.getFullYear(), today
								.getMonth(), 1, 0, 0, 0, 0);
						var tempMonth = new Date(thisMonth);
						var counter = -5;
						tempMonth.setMonth(tempMonth.getMonth() + counter);

						$scope.monthList = [];
						for (var i = today.getMonth() + counter; counter <= 6; i++, counter++) {
							i = Math.abs(i);
							$scope.monthList.push($scope.monthNameList[i] + "-"
									+ tempMonth.getFullYear());
							tempMonth.setMonth(tempMonth.getMonth() + 1);
							if (i == 11) {
								i = -1;
							}
						}
					}
					$scope.prepareMonthList();

					$scope.getJson = function(object) {
						return angular.toJson(object);
					};

					$scope.toggleRight = buildToggler('right');

					function buildToggler(navID) {
						var debounceFn = $mdUtil.debounce(function() {
							$mdSidenav(navID).toggle().then(function() {
								$log.debug("toggle " + navID + " is done");
							});
						}, 200);
						return debounceFn;
					}

					$scope.close = function() {
						$mdSidenav('right').close().then(function() {
							$log.debug("close RIGHT is done");
						});
					};

				});
