angular.module("stockApp").controller(
		"leadList",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, appEndpointSF) {

			$scope.curUser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			$scope.selected = [];
			
			
			$scope.logOrder = function(order) {
				console.log('order: ', order);
			};

			$scope.logPagination = function(page, limit) {
				console.log('page: ', page);						
				console.log('limit: ', limit);
				$location.hash('tp1');
				$anchorScroll();
				if ($scope.query.page > $scope.query.pagesLoaded) {
					$scope.getAllleads();
				}
			}
			

			$scope.query = {
				order : 'name',
				limit : 5,
				page : 1
			};

			$scope.lead = {
				business : "",
				loggedInUser : "",
				lid : "",
				name : "",
				company : "",
				phone : null,
				email : "",
				designation : "",
				address : "",
				tasks : []
			}

			$scope.task = [ {
				id : "",
				description : "",
				type : "",
				date : new Date(),
				note : "",
				status : ""
			} ]

			$scope.getAllleads = function() {
				$scope.loading = true;
				var leadService = appEndpointSF.getleadService();

				leadService.getAllleads($scope.curUser.business.id).then(
						function(leadList) {
							$log.debug("Inside Ctr getAllleads");
							$scope.leads = leadList.items;
							$scope.loading = false;

							$scope.cleadid = $scope.leads.length + 1;
							$scope.lead.lid = $scope.cleadid;

						});
			}

			$scope.leads = [];

			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getAllleads();

				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();

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
