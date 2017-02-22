var app = angular.module("stockApp");

app
		.controller(
				"customerAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $stateParams, objectFactory, $mdMedia,
						$mdDialog, appEndpointSF) {

					$scope.customerId = $stateParams.selectedCustomerId;
					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.customer = {
						isCompany : false,
						createdDate : new Date(),
						modifiedDate : new Date(),
						modifiedBy : ''
					}

					$scope.Address = {
						line1 : "",
						line2 : "",
						city : "",
						state : "",
						country : "",
						pin : ""
					}

					$scope.addCustomer = function() {
						$scope.customer.business = $scope.curUser.business;
						$scope.customer.address = $scope.Address;
						var customerService = appEndpointSF
								.getCustomerService();

						if ($scope.customerId == undefined) {
							$scope.customer.business = $scope.curUser.business;
							$scope.customer.modifiedBy = $scope.curUser.email_id;
							// $scope.customer.createdDate
							// =$scope.tempCustomer.createdDate;
							if ($scope.customerId != undefined) {
								$scope.customer.modifiedDate = $scope.tempCustomer.modifiedDate;
							}

						}
						customerService.addCustomer($scope.customer).then(
								function(msgBean) {
									if ($scope.customerId != "") {
										$scope.showUpdateToast();
									} else {
										$scope.showAddToast();
										$scope.custForm.$setPristine();
										$scope.custForm.$setValidity();
										$scope.custForm.$setUntouched();
										$scope.customer = {};
										$scope.Address = {}
									}
								});
					}

					$scope.getCustomerByID = function() {

						var customerService = appEndpointSF
								.getCustomerService();

						customerService
								.getCustomerByID($scope.customerId)
								.then(
										function(custList) {
											$scope.customer = custList;
											$scope.Address = $scope.customer.address;
											$scope.tempCustomer = $scope.customer;
											$scope.customer.mobile = parseInt($scope.customer.mobile);
											$log
													.debug("Inside Ctr $scope.customers:"
															+ angular
																	.toJson($scope.customers));
										});
					}
					$scope.customer = [];
					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {
							if ($scope.selectedSupplierNo != "") {
								$scope.getCustomerByID();
							}
						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					}
					$scope.waitForServiceLoad();

					$scope.Checkemail = function(emailid) {

						var customerService = appEndpointSF
								.getCustomerService();
						customerService
								.isCustomerExists(emailid)
								.then(
										function(responce) {
											if (responce.result.returnBool == true) {
												$scope.userexists = "customer already exists";
												$scope.user.firstName = "";
												$scope.user.lastName = "";
												angular
														.element(document
																.getElementById('line1'))[0].disabled = true;
												angular
														.element(document
																.getElementById('state'))[0].disabled = true;
												angular
														.element(document
																.getElementById('city'))[0].disabled = true;

											} else {
												if ($scope.customer.isCompany) {
													// alert("check contact
													// also");
													var leadService = appEndpointSF
															.getleadService();
													leadService
															.isContactExists(
																	emailid)
															.then(
																	function(
																			responce) {
																		if (responce.result.returnBool == true) {

																			$scope.userexists = "customer already exists";
																			$scope.user.firstName = "";
																			$scope.user.lastName = "";
																			angular
																					.element(document
																							.getElementById('line1'))[0].disabled = true;
																			angular
																					.element(document
																							.getElementById('state'))[0].disabled = true;
																			angular
																					.element(document
																							.getElementById('city'))[0].disabled = true;
																		} else {
																			$scope.userexists = "";
																			angular
																					.element(document
																							.getElementById('line1'))[0].disabled = false;
																			angular
																					.element(document
																							.getElementById('state'))[0].disabled = false;
																			angular
																					.element(document
																							.getElementById('city'))[0].disabled = false;

																		}
																	});
												} else {
													$scope.userexists = "";
													angular
															.element(document
																	.getElementById('line1'))[0].disabled = false;
													angular
															.element(document
																	.getElementById('state'))[0].disabled = false;
													angular
															.element(document
																	.getElementById('city'))[0].disabled = false;
												}
											}

										});

					}
					$scope.user11 = [];
					$scope.userexist = "";

					// ----------------------UPLODE EXCEL
					// FILE-------------------------------

					$scope.UplodeExcel = function(ev) {
						var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))
								&& $scope.customFullscreen;
						$mdDialog
								.show(
										{
											controller : DialogController,
											templateUrl : '/app/crm/ExcelCustomerUpload.html',
											parent : angular
													.element(document.body),
											targetEvent : ev,
											clickOutsideToClose : true,
											fullscreen : useFullScreen,
											locals : {
												curuser : $scope.curUser

											}
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

					function DialogController($scope, $mdDialog, curuser) {
						$scope.bizID;
						$scope.loding = false;
						$scope.uplodecustomer = function() {
							$scope.loding = true;
							document.excelform.action = $scope.CustomerExcelUploadURL;
							document.excelform.submit();
						}

						$scope.getExcelUploadURL = function() {
							var uploadUrlService = appEndpointSF
									.getuploadURLService();
							uploadUrlService
									.getCustomerExcelUploadURL()
									.then(
											function(url) {
												$scope.CustomerExcelUploadURL = url.msg;
												$scope.bizID = curuser.business.id;
											});

						}
						$scope.CustomerExcelUploadURL;

						$scope.waitForServiceLoad = function() {
							if (appEndpointSF.is_service_ready) {
								$scope.getExcelUploadURL();
							} else {
								$log.debug("Services Not Loaded, watiting...");
								$timeout($scope.waitForServiceLoad, 1000);
							}
						}
						$scope.waitForServiceLoad();
					}

				});
