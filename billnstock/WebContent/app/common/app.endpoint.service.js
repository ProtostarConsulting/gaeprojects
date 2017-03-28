angular.module("stockApp").factory('appEndpointSF', appEndpointSFFn);

function appEndpointSFFn($log, localDBServiceFactory, googleEndpointSF, AutoCompleteUIService) {

	// When app is in test mode, it will return service from local db store.
	// Else actual google end points.

	// var isTestMode = true;
	var isTestMode = false;

	var endpointFactory = {};
	endpointFactory.is_service_ready = false;
	// This will call the function to load services
	
	// -----------------------------------user login-----------------
	endpointFactory.getAutoCompleteUIService = function() {
		return AutoCompleteUIService;
	};

	// -----------------------------------user login-----------------
	endpointFactory.getTaskService = function() {
		if (isTestMode)
			return localDBServiceFactory.getTaskService();
		else
			return googleEndpointSF.getTaskService();
	};
	// -----------------------------------AuthorizationService-----------------
	endpointFactory.getAuthorizationService = function() {
		if (isTestMode)
			return localDBServiceFactory.getAuthorizationService();
		else
			return googleEndpointSF.getAuthorizationService();
	};

	// -----------------------------------user login-----------------

	endpointFactory.getLocalUserService = function() {
		return localDBServiceFactory.getUserService();
	};// end of getUserService

	endpointFactory.getUserService = function() {

		if (isTestMode)
			return localDBServiceFactory.getUserService();
		else
			return googleEndpointSF.getUserService();
	};// end of getUserService

	// ------------------------------------------------
	// accGroupService
	endpointFactory.getAccountGroupService = function() {
		if (isTestMode)
			return localDBServiceFactory.getAccountGroupService();
		else
			return googleEndpointSF.getAccountGroupService();
	};
	// ============================================

	endpointFactory.getCustomerService = function() {
		if (isTestMode)
			return localDBServiceFactory.getCustomerService();
		else
			return googleEndpointSF.getCustomerService();
	};

	// -----------------------------------Supplier-----------------

	endpointFactory.getSupplierService = function() {
		if (isTestMode)
			return localDBServiceFactory.getSupplierService();
		else
			return googleEndpointSF.getSupplierService();
	};

	// ----------------------------------------------------
	endpointFactory.getAccountService = function() {
		if (isTestMode)
			return localDBServiceFactory.getAccountService();
		else
			return googleEndpointSF.getAccountService();
	};
	// ------------------------------------------------------------------
	endpointFactory.getVoucherService = function() {
		if (isTestMode)
			return localDBServiceFactory.getVoucherService();
		else
			return googleEndpointSF.getVoucherService();
	};
	// ----------------------------------------------------
	endpointFactory.getAccountEntryService = function() {
		if (isTestMode)
			return localDBServiceFactory.getAccountEntryService();
		else
			return googleEndpointSF.getAccountEntryService();
	};

	// ----------------------------------------------------
	endpointFactory.getGeneralJournalService = function() {
		if (isTestMode)
			return localDBServiceFactory.getGeneralJournalService();
		else
			return googleEndpointSF.getGeneralJournalService();
	};
	// ----------------------------------------------------
	endpointFactory.getGeneralEntryService = function() {
		if (isTestMode)
			return localDBServiceFactory.getGeneralEntryService();
		else
			return googleEndpointSF.getGeneralEntryService();
	};
	// ----------------------------------------------------
	endpointFactory.gethrService = function() {
		if (isTestMode)
			return localDBServiceFactory.gethrService();
		else
			return googleEndpointSF.gethrService();
	};
	// ----------------------------------------------------

	endpointFactory.getleadService = function() {
		if (isTestMode)
			return localDBServiceFactory.getleadService();
		else
			return googleEndpointSF.getleadService();
	}

	// -----------------------------------------------------
	endpointFactory.getloginService = function() {
		if (isTestMode)
			return localDBServiceFactory.getloginService();
		else
			return googleEndpointSF.getloginService();
	}

	// -----------------------------------------------------

	endpointFactory.getopportunityService = function() {
		if (isTestMode)
			return localDBServiceFactory.getopportunityService();
		else
			return googleEndpointSF.getopportunityService();
	}

	// -----------------------------------------------------

	endpointFactory.getStockService = function() {
		if (isTestMode)
			return localDBServiceFactory.getStockService();
		else
			return googleEndpointSF.getStockService();
	};
	// ----------------------------------------------------
	endpointFactory.getTaxService = function() {
		if (isTestMode)
			return localDBServiceFactory.getTaxService();
		else
			return googleEndpointSF.getTaxService();
	};
	// ----------------------------------------------------
	endpointFactory.getInvoiceService = function() {
		if (isTestMode)
			return localDBServiceFactory.getInvoiceService();
		else
			return googleEndpointSF.getInvoiceService();
	};

	// ----------------------------------------------------

	endpointFactory.getInternetService = function() {
		if (isTestMode)
			return localDBServiceFactory.getInternetService();
		else
			return googleEndpointSF.getInternetService();
	};
	// ----------------------------------------------------
	endpointFactory.getSalesOrderService = function() {
		if (isTestMode)
			return localDBServiceFactory.getSalesOrderService();
		else
			return googleEndpointSF.getSalesOrderService();
	};

	// ----------------------------------------------------

	endpointFactory.getAssetManagementService = function() {
		if (isTestMode)
			return localDBServiceFactory.getAssetManagementService();
		else
			return googleEndpointSF.getAssetManagementService();
	};
	// ----------------------------------------------------
	endpointFactory.getWarehouseManagementService = function() {
		if (isTestMode)
			return localDBServiceFactory.getWarehouseManagementService();
		else
			return googleEndpointSF.getWarehouseManagementService();
	};
	// ----------------------------------------------------
	endpointFactory.getproadminService = function() {
		if (isTestMode)
			return localDBServiceFactory.getproadminService();
		else
			return googleEndpointSF.getproadminService();
	};
	// ----------------------------------------------------
	endpointFactory.getuploadURLService = function() {
		if (isTestMode)
			return localDBServiceFactory.getuploadURLService();
		else
			return googleEndpointSF.getuploadURLService();
	};
	// ----------------------------------------------------

	endpointFactory.loadAppGoogleServices = function(deferred) {
		$log.debug("###Inside Google appEndpointSF.loadAppGoogleServices###");

		if (isTestMode) {
			$log.debug("isTestMode: " + isTestMode);
			deferred.resolve();
			return deferred.promise;
		}

		if (endpointFactory.is_service_ready) {
			$log.debug("Already Initialized returning back...");
			deferred.resolve();
			return deferred.promise;
		}

		var apiRoot = '//' + window.location.host + '/_ah/api';

		var apisToLoad;

		apisToLoad = 4; // must match number of calls to
		// gapi.client.load()

		gapi.client.load('customerService', 'v0.1', function() {
			$log.debug("customerService Loaded....");
		}, apiRoot);

		gapi.client.load('supplierService', 'v0.1', function() {
			$log.debug("supplierService Loaded....");
		}, apiRoot);

		gapi.client.load('warehouseManagementService', 'v0.1', function() {
			$log.debug("warehouseManagementService Loaded....");
		}, apiRoot);

		gapi.client.load('stockService', 'v0.1', function() {
			$log.debug("stockService Loaded....");
		}, apiRoot);

		gapi.client.load('taxService', 'v0.1', function() {
			$log.debug("taxService Loaded....");
		}, apiRoot);

		gapi.client.load('invoiceService', 'v0.1', function() {
			$log.debug("invoiceService Loaded....");
		}, apiRoot);

		gapi.client.load('accountService', 'v0.1', function() {
			$log.debug("accountService Loaded....");
		}, apiRoot);

		gapi.client.load('accountGroupService', 'v0.1', function() {
			$log.debug("accountGroupService Loaded....");
		}, apiRoot);

		gapi.client.load('voucherService', 'v0.1', function() {
			$log.debug("voucherService Loaded....");
		}, apiRoot);

		gapi.client.load('accountEntryService', 'v0.1', function() {
			$log.debug("accountEntryService Loaded....");
		}, apiRoot);

		gapi.client.load('hrService', 'v0.1', function() {
			$log.debug("hr Loaded....");
		}, apiRoot);

		gapi.client.load('crmService', 'v0.1', function() {
			$log.debug("CRM Loaded.(lead services)...");
		}, apiRoot);

		gapi.client
				.load(
						'opportunityService',
						'v0.1',
						function() {
							$log
									.debug("opportunityService Loaded.(opportunityService services)...");
						}, apiRoot);

		gapi.client.load('salesOrderService', 'v0.1', function() {
			$log.debug("salesOrderService Loaded....");
		}, apiRoot);

		gapi.client.load('userService', 'v0.1', function() {
			$log.debug("userService Loaded......");
		}, apiRoot);

		gapi.client.load('assetService', 'v0.1', function() {
			$log.debug("assetService Loaded......");
		}, apiRoot);

		gapi.client.load('uploadUrlService', 'v0.1', function() {
			$log.debug("uploadUrlService Loaded......");
		}, apiRoot);

		gapi.client.load('authorizationService', 'v0.1', function() {
			$log.debug("authorizationService Loaded......");
		}, apiRoot);

		gapi.client.load('taskService', 'v0.1', function() {
			$log.debug("taskService Loaded......");
		}, apiRoot);

		gapi.client.load('proadminService', 'v0.1', function() {
			$log.debug("proadminService Loaded......");
			endpointFactory.is_service_ready = true;
			deferred.resolve();
		}, apiRoot);

		gapi.load('auth', {
			'callback' : function() {
				$log.debug("Google Auth API Loaded......");
			}
		});
		gapi.load('picker', {
			'callback' : function() {
				$log.debug("Google Auth API Loaded......");
			}
		});

		return deferred.promise;
	};

	return endpointFactory;
}
