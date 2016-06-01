angular.module("prostudyApp").controller(
		"gfBookAddCtr",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$log, $q, appEndpointSF, $state, $stateParams, $mdDialog,
				objectFactory) {
			$scope.answerOfMediumList = [ "Marathi", "Hindi", "English", ];
			$scope.tempBookStock = {
					bookName : '',
					bookAuther : '',
					weight : '',
					bookPrice : '',
					bookPublication : '',
					bookFeedDate : new Date(),
			}

			$scope.selectedGFBookStockID = $stateParams.selectedGFBookStockID;

			$scope.addGFBook = function() {
				$scope.tempBookStock.instituteID = $scope.curUser.instituteID;

				var gfBookStockService = appEndpointSF.getGFBookStockService();

				gfBookStockService.addGFBook($scope.tempBookStock).then(
						function() {

							$scope.gfBookStockForm.$setPristine();
							$scope.gfBookStockForm.$setValidity();
							$scope.gfBookStockForm.$setUntouched();
							

						});
				if ($scope.selectedGFStudID == "") {
					$scope.showAddToast();
				} else {
					$scope.showUpdateToast();
				}
				$scope.tempBookStock  = {};
			}

			$scope.getGFBookStockById = function() {

				var gfCourierService = appEndpointSF.getGFCourierService();
				gfCourierService.getGFBookStockById($scope.selectedGFBookStockID)
						.then(function(tempCourier) {
							
							$scope.tempCourier = tempCourier;
						
						});
			}

			$scope.getPartnerByInstitute = function() {

				var PartnerSchoolService = appEndpointSF
						.getPartnerSchoolService();
				PartnerSchoolService.getPartnerByInstitute(
						$scope.curUser.instituteID).then(function(pSchoolList) {
					$scope.pSchoolList = pSchoolList;

				});
			}

			$scope.cancel = function() {
				$state.go('gandhifoundation');
			}

			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {

					if ($scope.selectedGFBookStockID != "") {
						$scope.getGFBookStockById();
					}
					$scope.getPartnerByInstitute();

				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}

			$scope.waitForServiceLoad();

			$scope.query = {
				order : 'description',
				limit : 5,
				page : 1
			};

		});