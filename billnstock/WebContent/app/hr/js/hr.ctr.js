angular.module("stockApp").controller(
		"hrCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, appEndpointSF) {
			$log.debug("Inside hrCtr");
			var date = new Date();
			var monthNames = [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
					"Aug", "Sep", "Oct", "Nov", "Dec" ];

			$scope.monthNameList = [ "January", "February", "March", "April",
					"May", "June", "July", "August", "September", "October",
					"November", "December" ];

			$scope.monthList = [];
			$scope.prepareMonthList = function() {
				var today = new Date();
				var thisMonth = new Date(today.getFullYear(), today.getMonth(),
						1, 0, 0, 0, 0);
				var now = new Date(thisMonth);
				// 6 months back
				now.setMonth(now.getMonth() - 6);

				$scope.monthList = [];
				for (var i = 0; i < 12; i++) {
					$scope.monthList.push($scope.monthNameList[now.getMonth()]
							+ "-" + now.getFullYear());
					now.setMonth(now.getMonth() + 1);

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
