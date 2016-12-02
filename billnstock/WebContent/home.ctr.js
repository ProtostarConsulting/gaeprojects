angular
		.module("stockApp")
		.controller(
				"homeCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $q, $location, objectFactory,
						appEndpointSF) {
					console.log("Inside homeCtr");

					$scope.showSimpleToast = function() {
						$mdToast.show($mdToast.simple().content(
								'Customer Saved!').position("top").hideDelay(
								3000));
					};

					$scope.loadCustomerList = function() {
						console.log("loadCustomerList");
						gapi.client.customerservice.getAllCustomers().execute(
								function(resp) {
									console.log(resp);
								});
					};

					$scope.addCustomer = function() {
						console.log("in side addCustomer");
						gapi.client.customerservice
								.addCustomer($scope.cust)
								.execute(
										function(resp) {
											console
													.log("Add Customer Response: "
															+ resp.msg);
											$scope.showSimpleToast();
											$scope.cust = $scope.newCustomer();

										})
					};// end of call to addCustomer

					$scope.newCustomer = function() {
						return {
							firstName : '',
							lastName : '',
							mobileNo : '',
							email : '',
							address : {
								line1 : '',
								line2 : '',
								city : '',
								state : '',
								pin : '',
							}
						};
					}

					/*
					 * $scope.cust = $scope.newCustomer(); // initialize local
					 * objects $scope.customer = $scope.newCustomer();
					 * $scope.customerList = {};
					 */
					$scope.myDate = new Date();
					$scope.showDateValue = function() {
						console.log("in side showDateValue");
						$log.debug("$scope.myDate:" + $scope.myDate);

						/*
						 * $scope.myDate = new Date();
						 * 
						 * $scope.showDateValue = function() { console.log("in
						 * side showDateValue"); $log.debug("$scope.myDate:" +
						 * $scope.myDate); }
						 * 
						 */};

					// //////Auto Complete Test Code

					// list of `state` value/display objects
					$scope.states = loadAll();
					$scope.selectedItem = null;
					$scope.searchText = null;

					// ******************************
					// Internal methods
					// ******************************
					/**
					 * Search for states... use $timeout to simulate remote
					 * dataservice call.
					 */
					$scope.querySearch = function(query) {
						var results = query ? $scope.states
								.filter(createFilterFor(query)) : $scope.states;
						var deferred = $q.defer();
						$timeout(function() {
							deferred.resolve(results);
						}, Math.random() * 1000, false);
						return deferred.promise;
					}
					/**
					 * Build `states` list of key/value pairs
					 */
					function loadAll() {
						var allStates = 'Alabama, Alaska, Arizona, Arkansas, California, Colorado, Connecticut, Delaware,\
          Florida, Georgia, Hawaii, Idaho, Illinois, Indiana, Iowa, Kansas, Kentucky, Louisiana,\
          Maine, Maryland, Massachusetts, Michigan, Minnesota, Mississippi, Missouri, Montana,\
          Nebraska, Nevada, New Hampshire, New Jersey, New Mexico, New York, North Carolina,\
          North Dakota, Ohio, Oklahoma, Oregon, Pennsylvania, Rhode Island, South Carolina,\
          South Dakota, Tennessee, Texas, Utah, Vermont, Virginia, Washington, West Virginia,\
          Wisconsin, Wyoming';
						return allStates.split(/, +/g).map(function(state) {
							return {
								value : state.toLowerCase(),
								display : state
							};
						});
					}
					/**
					 * Create filter function for a query string
					 */
					function createFilterFor(query) {
						var lowercaseQuery = angular.lowercase(query);
						return function filterFn(state) {
							return (state.value.indexOf(lowercaseQuery) === 0);
						};
					}

					// ////Auto complete code ends

				});