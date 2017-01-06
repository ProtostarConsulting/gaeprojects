angular.module("stockApp").controller(
		"setup.setLogo",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $mdMedia, $mdDialog, $log, objectFactory,
				appEndpointSF, Upload) {

			$scope.loading = true;
			$scope.businessNo = $stateParams.businessNo;
			$scope.selectedBizId = null;

			$scope.fileObject;
			$scope.uploadProgressMsg = null;
			$scope.existingLogURL = '';
			$scope.uploadFile = function() {
				$scope.loading = true;
				Upload.upload({
					url : '/UploadBusinessLogoServlet',
					data : {
						'file' : $scope.fileObject,
						'username' : $scope.curUser.email_id,
						'businessId' : $scope.selectedBizId
					}
				}).then(
						function(resp) {
							$log.debug('Successfully uploaded '
									+ resp.config.data.file.name + '.'
									+ angular.toJson(resp.data));
							$scope.uploadProgressMsg = 'Successfully uploaded '
									+ resp.config.data.file.name + '.';
							$mdToast.show($mdToast.simple().content(
									'File Uploaded Sucessfully.').position(
									"top").hideDelay(3000));

							$scope.fileObject = null;
							$scope.loading = false;
							// Load the logo again in the end
							$scope.getBusinessById($scope.selectedBizId);							
						},
						function(resp) {
							$log.debug('Error Ouccured, Error status: '
									+ resp.status);
							$scope.uploadProgressMsg = 'Error: ' + resp.status;
						},
						function(evt) {
							var progressPercentage = parseInt(100.0
									* evt.loaded / evt.total);
							$log.debug('Upload progress: ' + progressPercentage
									+ '% ' + evt.config.data.file.name);
							$scope.uploadProgressMsg = 'Upload progress: '
									+ progressPercentage + '% '
									+ evt.config.data.file.name;
							+'...'

							/*
							 * if(progressPercentage == 100%){ }
							 */
						});
			};

			$scope.business = {};
			// use to update localdatabase business value
			$scope.getBusinessById = function(bizId) {
				$scope.selectedBizId = $scope.businessNo;
				var UserService = appEndpointSF.getUserService();
				UserService.getbusinessById(bizId).then(function(businessObj) {
					$scope.business = businessObj;
					setExistingLogURL($scope.business.bizLogoGCSURL);
					$scope.loading = false;
				});
			}

			function setExistingLogURL(bizLogoGCSURL) {
				if (bizLogoGCSURL) {
					$scope.existingLogURL = bizLogoGCSURL;	
					if(!$scope.businessNo){
						//to change log on index page if it is not pro-admin
						//$scope.initCommonSetting();
					}
				}
			}

			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					if (!$scope.businessNo) {
						// it for setup of own business. not by pro-admin
						$scope.selectedBizId = $scope.curUser.business.id;
						$scope.business = $scope.curUser.business;
						setExistingLogURL($scope.business.bizLogoGCSURL);
					} else {
						$scope.selectedBizId = $scope.businessNo;
						$scope.getBusinessById($scope.selectedBizId);
					}

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
