angular
		.module("stockApp")
		.controller(
				"SalaryMaster",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $stateParams, $log, objectFactory,
						appEndpointSF, $mdDialog, $mdMedia, $state) {

					$scope.flag = false;
					$scope.neg = false;

					$scope.getEmptySalaryMaster = function(emp) {
						return {
							empAccount : emp,
							grosssal : 0,
							basic : 0,
							hramonthly : 0,
							convence : 0,
							medical : 0,
							education : 0,
							adhocAllow : 0,
							specialAllow : 0,
							calGrossTotal : 0,
							business : $scope.curUser.business

						};
					}

					$scope.empSalaryMasterList = [];
					$scope.getEmpList = function() {						
						$scope.loading = true;
						var hrService = appEndpointSF.gethrService();
						hrService
								.getAllemp($scope.curUser.business.id)
								.then(
										function(list) {
											
											$scope.empSalaryMasterList.length = 0;
											for (var i = 0; i < list.length; i++) {

												$scope.empSalaryMasterList
														.push($scope
																.getEmptySalaryMaster(list[i]));

											}
											$scope.loading = false;

										});

					}

					$scope.getSalaryMasterlist = function() {
						$scope.loading = true;	
						var hrService = appEndpointSF.gethrService();
						hrService.getSalaryMasterlist(
								$scope.curUser.business.id).then(
								function(list) {
									if (list.length == 0) {
										$scope.getEmpList();
									} else
										$scope.empSalaryMasterList.length = 0;
									for (var i = 0; i < list.length; i++) {
										// if(list[i].grosssal==0)
										// {$scope.empSalaryMasterList.push($scope.getEmptySalaryMaster(list[i]));}
										$scope.empSalaryMasterList
												.push(list[i]);
										// $scope.calculation( i );
									}
									$log.debug("items: "
											+ $scope.curUser.business.items);
									$scope.loading = false;	

								});

					}

					$scope.calSpecialAllow = function(index) {
						var currEmpSalMasterObj = $scope.empSalaryMasterList[index];
						if (currEmpSalMasterObj.grosssal !== 0) {
							currEmpSalMasterObj.specialAllow = currEmpSalMasterObj.grosssal
									- currEmpSalMasterObj.basic
									- currEmpSalMasterObj.hramonthly
									- currEmpSalMasterObj.convence
									- currEmpSalMasterObj.medical
									- currEmpSalMasterObj.education
									- currEmpSalMasterObj.adhocAllow;
							currEmpSalMasterObj.specialAllow = currEmpSalMasterObj.specialAllow.toFixed(2);
						}
					}

					$scope.grossSalaryChanged = function(index) {
						var currEmpSalMasterObj = $scope.empSalaryMasterList[index];
						if (currEmpSalMasterObj.grosssal >= 11650) {
							// because all below are equal to 11650, which is
							// standard for suruchi dairy
							currEmpSalMasterObj.basic = 5200;
							currEmpSalMasterObj.hramonthly = 3000;
							currEmpSalMasterObj.convence = 2000;
							currEmpSalMasterObj.medical = 1250;
							currEmpSalMasterObj.education = 200;

							$scope.calSpecialAllow(index);
						}
					}

					$scope.getHighlightRedStyle = function(spValue) {
						return {
							color : spValue > 0 ? '' : 'red'
						};
					}

					$scope.saveSalaryMasterDetailList = function() {
						var hrService = appEndpointSF.gethrService();
						hrService.saveSalaryMasterDetailList({
							'list' : $scope.empSalaryMasterList
						}).then(function() {
							$scope.showAddToast();
						});
					}

					$scope.getSalaryMasterlist();
					$scope.download = function() {
						// window.open("DownloadSalaryMaster?id="+$scope.curUser.business.id+d);
						document.location.href = "DownloadSalaryMaster?id="
								+ $scope.curUser.business.id;
					}
					$log.debug("id" + $scope.curUser.business);
				});
