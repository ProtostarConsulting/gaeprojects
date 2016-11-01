var module = angular.module( 'stockApp', [] );

module.service( 'ContactService', function () {
	// to create unique contact id
	var uid = 1;

	// contacts array to hold list of all contacts
	var contacts = [
		{
			id : 0,
			'name' : 'Viral',
			'email' : 'hello@gmail.com',
			'phone' : '123-2343-44'
		}
	];

	// save method create a new contact if not already exists
	// else update the existing object
	this.save = function ( contact ) {
		if ( contact.id == null ) {
			// if this is new contact, add it in contacts array
			contact.id = uid++;
			contacts.push( contact );
		} else {
			// for existing contact, find this contact using id
			// and update it.
			for ( i in contacts ) {
				if ( contacts[ i ].id == contact.id ) {
					contacts[ i ] = contact;
				}
			}
		}

	}

	// simply search contacts list for given id
	// and returns the contact object if found
	this.get = function ( id ) {
		for ( i in contacts ) {
			if ( contacts[ i ].id == id ) {
				return contacts[ i ];
			}
		}

	}

	// iterate through contacts list and delete
	// contact if found
	this.delete1 = function ( id ) {
		for ( i in contacts ) {
			if ( contacts[ i ].id == id ) {
				contacts.splice( i, 1 );
			}
		}
	}

	// simply returns the contacts list
	this.list = function () {
		return contacts;
	}
} );

module.controller( 'ContactController', function ( $scope, ContactService ) {

	$scope.contacts = ContactService.list();

	$scope.saveContact = function () {
		ContactService.save( $scope.newcontact );
		$scope.newcontact = {};
	}

	$scope.delete1 = function ( id ) {

		ContactService.delete1( id );
		if ( $scope.newcontact.id == id )
			$scope.newcontact = {};
	}

	$scope.edit = function ( id ) {
		$scope.newcontact = angular.copy( ContactService.get( id ) );
	}
} )
/*
 * angular.module("stockApp").controller( "taxCtr", function($scope, $window, $mdToast, $timeout, $mdSidenav, $mdUtil, $log, objectFactory, appEndpointSF) {
 * 
 * $log.debug("Inside taxCtr");
 * 
 * 
 * $scope.showSimpleToast = function() { $mdToast.show($mdToast.simple().content('Tax Data Saved!').position( "top").hideDelay(3000)); };
 * 
 * $scope.tax = {}; $scope.addTax = function() { $log.debug("No1"); var taxService = appEndpointSF.getTaxService();
 * 
 * taxService.addTax($scope.tax).then( function(msgBean) { $log.debug("No6"); $log.debug("Inside Ctr addTax"); $log.debug("msgBean.msg:" + msgBean.msg); $scope.showSimpleToast();
 * 
 * }); $log.debug("No4"); }
 * 
 * $scope.getAllTaxes = function() { $log.debug("Inside Ctr $scope.getAllTaxes"); var taxService = appEndpointSF.getTaxService();
 * 
 * taxService.getAllTaxes().then( function(taxList) { $log.debug("Inside Ctr getAllTaxes"); $scope.taxData = taxList; $log.debug("Inside Ctr $scope.taxData:" + angular.toJson($scope.taxData)); }); }
 * 
 * $scope.taxData = []; $scope.getAllTaxes(); // Setup menu $scope.toggleRight = buildToggler('right');
 * 
 * function buildToggler(navID) { var debounceFn = $mdUtil.debounce(function() { $mdSidenav(navID).toggle().then(function() { $log.debug("toggle " + navID + " is done"); }); }, 200); return debounceFn; }
 * 
 * $scope.close = function() { $mdSidenav('right').close().then(function() { $log.debug("close RIGHT is done"); }); }; });
 */