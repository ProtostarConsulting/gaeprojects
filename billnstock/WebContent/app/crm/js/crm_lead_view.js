angular.module("stockApp").controller(
		"lead_view",
		function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil,
				$stateParams, $log, objectFactory, appEndpointSF) {

			$scope.showSimpleToast = function(msgBean) {
				$mdToast.show($mdToast.simple().content(msgBean)
						.position("top").hideDelay(3000));
			};
			$scope.selectedleadNo = $stateParams.selectedleadNo;

			$log
					.debug("$scope.selectedleadNo========="
							+ $scope.selectedleadNo);

			$scope.curUser = appEndpointSF.getLocalUserService()
					.getLoggedinUser();

			$scope.query = {
				order : 'name',
				limit : 5,
				page : 1
			};

			$scope.Address = {
				line1 : "",
				line2 : "",
				city : "",
				state : "",
				country : "",
				pin : ""
			}

			$scope.taskType = [ "Phone Call", "Email", "Visit" ];
			var d = new Date();
			var year = d.getFullYear();
			var month = d.getMonth() + 1;
			if (month < 10) {
				month = "0" + month;
			}

			var day = d.getDate();

			$scope.lead = {
				lid : "",
				name : "",
				company : "",
				phone : null,
				email : "",
				designation : "",
				address : "",
				tasks : []
			}
			$scope.objlead = $scope.lead;

			$scope.task = {
				tid : "",
				description : "",
				type : "",
				date : new Date(),
				note : "",
				status : ""
			}

			$scope.converttocustomer = {
				customerId : "",
				customerName : "",
				mobile : "",
				email : "",
				customerAddress : ""
			}

			$scope.getLeadById = function() {
				$log.debug("Inside Ctr $scope.getAlllead");
				var leadService = appEndpointSF.getleadService();
				$scope.ctaskid = [];
				leadService.getLeadById($scope.curUser.business.id,
						$scope.selectedleadNo).then(function(lead) {
					$log.debug("Inside Ctr getAllleads");
					$scope.lead = $scope.initDateFields(lead);
					$scope.lead.phone = Number(lead.phone);
					$scope.Address = $scope.lead.address;
					if (!$scope.lead.tasks) {
						$scope.lead.tasks = [];
					}
					$scope.ctaskid = $scope.lead.tasks;
					$scope.task.tid = $scope.ctaskid.length + 1;
					// $scope.task.date= $scope.curdate;
				});

			}

			$scope.initDateFields = function(leadList) {
				for ( var lead in leadList.tasks) {
					lead.date = new Date(lead.date);

				}
				return leadList;
			}

			$scope.activetask = [];

			$scope.waitForServiceLoad = function() {
				if (appEndpointSF.is_service_ready) {
					$scope.getLeadById();

				} else {
					$log.debug("Services Not Loaded, waiting...");
					$timeout($scope.waitForServiceLoad, 1000);
				}
			}
			$scope.waitForServiceLoad();

			// ------------------save task----------

			$scope.updateLead = function() {
				$scope.lead.address = $scope.Address;
				$scope.lead.modifiedBy = $scope.curUser.email_id;
				var leadService = appEndpointSF.getleadService();
				leadService.addupdatetask($scope.lead).then(function(msgBean) {

					$log.debug("Inside Ctr addlead");
					$log.debug("msgBean.msg:" + msgBean.msg);
					$scope.showUpdateToast();
					$scope.getLeadById();
				});
			}

			$scope.editTask = function(task) {
				$scope.task = task;
				$scope.task.date = new Date(task.date);
			};

			$scope.addEditTask = function(leadid) {
				/*
				 * $scope.objlead=$scope.leads;
				 * $scope.objlead.tasks.push($scope.task);
				 */
				if (!$scope.lead.tasks) {
					$scope.lead.tasks = [];
				}
				$scope.lead.modifiedBy = $scope.curUser.email_id;

				if (typeof $scope.task.type != 'undefined'
						&& $scope.task.type != "") {
					var i;
					for( i = 0; i<$scope.lead.tasks.length; i++)	{
						if($scope.task.tid== $scope.lead.tasks[i].tid){
							break;
						}
					}
					 
					if(i == $scope.lead.tasks.length){
					  $scope.lead.tasks.push($scope.task);
					}
				}
				var leadService = appEndpointSF.getleadService();

				leadService.addupdatetask($scope.lead).then(function(msgBean) {

					$log.debug("Inside Ctr addlead");
					$log.debug("msgBean.msg:" + msgBean.msg);
					$scope.showUpdateToast();
					$scope.getLeadById();
				});

				$scope.task = {};

			}

			// --------------------------------------
			// ----------hide and show ---------------------------

			$scope.IsHidden = true;
			$scope.ShowHide = function() {
				$scope.IsHidden = $scope.IsHidden ? false : true;
			}
			/*
			 * //-----------------------------------------------------
			 * 
			 * $scope.convertocustomer = function(leadid) {
			 * $scope.converttocustomer.customerId = $scope.leads.id;
			 * $scope.converttocustomer.customerName = $scope.leads.name;
			 * $scope.converttocustomer.mobile = $scope.leads.phone;
			 * $scope.converttocustomer.email = $scope.leads.email;
			 * $scope.converttocustomer.customerAddress = $scope.leads.address;
			 * 
			 * var customerService = appEndpointSF.getCustomerService();
			 * customerService.addCustomer($scope.converttocustomer).then(
			 * function(msgBeanz) {
			 * 
			 * });
			 * 
			 * 
			 * 
			 * $scope.converttocustomer = {}; var leadService =
			 * appEndpointSF.getleadService();
			 * leadService.deletelead(leadid).then(function(msgBean) {
			 * $scope.showSimpleToast(msgBean); }); $scope.leads={};
			 * $scope.task={}; }
			 */

			$scope.convertocustomer = function(id) {

				alert($scope.lead.id);
			}

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
