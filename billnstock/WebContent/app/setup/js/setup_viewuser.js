angular.module("stockApp").controller(
		"setup.viewuser",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, $mdDialog, $mdMedia,
				appEndpointSF) {

			$scope.showSimpleToast = function(msgBean) {
				$mdToast.show($mdToast.simple().content(msgBean)
						.position("top").hideDelay(3000));
			};
			// $scope.businessNo = $stateParams.businessNo;
			// $scope.selecteduserNo = $stateParams.selecteduserNo;
			$scope.userL = $stateParams.selectedUser;

			$scope.items = [ "customer", "account", "stock", "salesOrder",
					"purchaseOrder", "invoice", "warehouse", "hr", "crm",
					"employee", "admin" ];
			$scope.selection = [];

			$scope.curuser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			// set toggled value onclick on check box and push in
			// selection array only tru or false value
			$scope.toggleSelection = function toggleSelection(index) {
				$scope.selection[index] = !$scope.selection[index];
			};

			$scope.BankDetail = {
				bankName : "",
				bankIfscCode : "",
				bankMircCode : "",
				bankAccountNo : "",

			}
			$scope.condition = function() {
				if ($scope.user.isGoogleUser == false) {
					return true;
				} else {
					return false;
				}
			}

			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {

				} else {
					$log.debug("Services Not Loaded, watiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();

			$scope.updateuser = function() {
				$scope.userL.modifiedBy = $scope.curUser.email_id;
				$scope.userL.bankDetail = $scope.BankDetail;

				var UserService = appEndpointSF.getUserService();
				UserService.updateUser($scope.userL).then(function(msgBean) {
					$scope.showUpdateToast();
				});
			}
			// ------------------------------------------------------------
			$scope.back = function() {
				window.history.back();
				// $state.go("^", {});
			};
			// ----------hide and show ---------------------------

			$scope.IsHidden = true;
			$scope.ShowHide = function() {
				$scope.IsHidden = $scope.IsHidden ? false : true;
			}
			// -----------------------------------------------------

			$scope.showAdvanced = function(ev) {
				var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
						&& $scope.customFullscreen;
				$mdDialog.show({
					controller : DialogController,
					templateUrl : '/app/profile/changepassword.html',
					parent : angular.element(document.body),
					targetEvent : ev,
					clickOutsideToClose : true,
					fullscreen : useFullScreen,
					locals : {
						curuser : $scope.userL
					}
				}).then(
						function(answer) {
							$scope.status = 'You said the information was "'
									+ answer + '".';
						}, function() {
							$scope.status = 'You cancelled the dialog.';
						});
				$scope.updatepass = function() {
					$log.debug("change pass");
				}
			};

			function DialogController($scope, $mdDialog, curuser) {

				// alert(angular.toJson(curuser));
				$scope.hide = function() {
					$mdDialog.hide();
				};
				$scope.cancel = function() {
					$mdDialog.cancel();
				};
				$scope.answer = function(answer) {
					$mdDialog.hide(answer);
				};

				$scope.inputType1 = 'password';
				$scope.inputType2 = 'password';

				$scope.showpass1 = function() {
					if ($scope.inputType1 == 'password') {
						$scope.inputType1 = 'text';
					} else {
						$scope.inputType1 = 'password';
					}

				}
				$scope.showpass2 = function() {
					if ($scope.inputType2 == 'password') {
						$scope.inputType2 = 'text';
					} else {
						$scope.inputType2 = 'password';
					}
				}
				$scope.setpassinput1 = function() {
					$scope.inputType1 = 'password';
				}
				$scope.setpassinput2 = function() {
					$scope.inputType2 = 'password';
				}
				$scope.changepass = function() {
					if ($scope.password == $scope.confirmpassword) {
						$scope.savemsg = true;
						$scope.checkpass = false;
					} else {
						$scope.checkpass = true;
						$scope.savemsg = false;
					}

					if ($scope.savemsg == true) {
						$scope.updateuser = curuser;
						/* $scope.updateuser.modifiedBy=curuser.email_id; */
						$scope.updateuser.password = $scope.password;
						var UserService = appEndpointSF.getUserService();
						UserService.updateUser($scope.updateuser).then(
								function(msgBean) {
									$scope.showSimpleToast(msgBean.msg);

								});
					}
				}
			}

			// -------------------------------------------------------
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
