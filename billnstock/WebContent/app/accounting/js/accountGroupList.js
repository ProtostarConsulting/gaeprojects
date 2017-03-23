angular
	.module("stockApp")
	.controller('accountGrpListCtr', function($scope, $log, appEndpointSF, $mdToast, $timeout, $mdMedia, Upload, $mdDialog, $filter, ajsCache) {

		$scope.query = {
			order : 'groupName',
			limit : 50,
			page : 1
		};

		$scope.getList = function(refresh) {

			var AccountGroupServiceCacheKey = "getAccountGroupByName";
				if (!angular.isUndefined(ajsCache.get(AccountGroupServiceCacheKey)) && !refresh) {
				$log.debug("Found List in Cache, return it.");
				$scope.List = ajsCache.get(AccountGroupServiceCacheKey);
				return;
			}
			$scope.loading = true;
			var listAccountGroupService = appEndpointSF.getAccountGroupService();
			listAccountGroupService.getAccountGroupList($scope.curUser.business.id)
				.then(
					function(list) {
						$scope.accountGroupList = list;
						$scope.loading = false;
						ajsCache.put(AccountGroupServiceCacheKey, list);
					})

		};		

		$scope.waitForServiceLoad = function() {
			if (appEndpointSF.is_service_ready) {
				$scope.getList();
			} else {
				$log.debug("Services Not Loaded, watiting...");
				$timeout($scope.waitForServiceLoad, 1000);
			}
		}
		$scope.waitForServiceLoad();

		$scope.uploadExcel = function(ev) {
			var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
			&& $scope.customFullscreen;
			$mdDialog.show(
				{
					controller : DialogController,
					templateUrl : '/app/accounting/UploadGroupList.html',
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

			$scope.uploadGrpListCSV = function() {
				var csvFile = $scope.csvFile;
				Upload
					.upload(
						{
							url : 'UploadGroupsListServlet',
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
								.content('GroupList Uploaded Sucessfully.')
								.position("top")
								.hideDelay(3000));

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

		$scope.downloadData = function() {
			document.location.href = "DownloadGroupListServlet?id="+$scope.curUser.business.id;
		}

	})