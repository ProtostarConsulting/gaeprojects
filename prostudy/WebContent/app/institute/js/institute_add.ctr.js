angular
		.module("prostudyApp")
		.controller(
				"instituteAddCtr",
						function($scope, $window, $mdToast, $timeout, $mdSidenav,
								$mdUtil, $log, $q, tableTestDataFactory) {
					
							
							
							$scope.loadInstituteList = function() 
							{
								console.log("inside loadInstituteList")
								$scope.institutes = [];
								$scope.selected = [];
							tableTestDataFactory.getInstituteList().then(
									function(data) {
										$scope.institutes = data;
										$log.debug("inside ctr then $scope.institutes"
												+ $scope.institutes);
										console.log("inside institute")
							});
							
							
							
							
							$scope.editingData = [];

							 
							   $scope.addInstitute = function()
							   {
								    var institutes = 
								    {
								    	name: $scope.name,
								        city: $scope.city,
								        state: $scope.state,
								        
							        };
							       $scope.institutes.push(institutes);
								}; 


							$scope.removeInstitute = function(index)
							{
								$scope.institutes.splice(index, 1);
							}; // end of removeQuestion

							

						
							}//end of loadInstituteList load

							

						$scope.loadInstituteList();

						} );
