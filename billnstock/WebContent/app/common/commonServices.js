var app = angular.module("stockApp");

app.factory('MathService', function() {
	var factory = {};
	factory.multiply = function(a, b) {
		return a * b
	}
	return factory;
});

app.service('AutoCompleteUIService', function($log, $timeout, $q) {

	this.createContactFilterFor = function(query) {
		var lowercaseQuery = angular.lowercase(query);
		return function filterFn(contact) {
			var a = contact.fName + "" + contact.lName;
			return (angular.lowercase(a).indexOf(lowercaseQuery) >= 0);
		};
	}

	this.queryContactSearch = function(contactList, query) {
		var results = query ? contactList.filter(this
				.createContactFilterFor(query)) : [];
		var deferred = $q.defer();
		$timeout(function() {
			deferred.resolve(results);
		}, Math.random() * 100, false);
		return deferred.promise;
	}

	/**
	 * Create filter function for a query string
	 */
	this.createCustomerFilterFor = function(query) {
		var lowercaseQuery = angular.lowercase(query);
		return function filterFn(cus) {
			var a = cus.isCompany ? cus.companyName
					: (cus.firstName + "" + cus.lastName);
			return (angular.lowercase(a).indexOf(lowercaseQuery) >= 0);
		};
	}

	this.queryCustomerSearch = function(customerList, query) {
		var results = query ? customerList.filter(this
				.createCustomerFilterFor(query)) : [];
		var deferred = $q.defer();
		$timeout(function() {
			deferred.resolve(results);
		}, Math.random() * 100, false);
		return deferred.promise;
	}

	this.createSupplierFilterFor = function(query) {
		var lowercaseQuery = angular.lowercase(query);
		return function filterFn(supp) {

			return (angular.lowercase(supp.supplierName)
					.indexOf(lowercaseQuery) >= 0);
		};
	}

	this.querySupplierSearch = function(supplierList, query) {
		var results = query ? supplierList.filter(this
				.createSupplierFilterFor(query)) : [];
		var deferred = $q.defer();
		$timeout(function() {
			deferred.resolve(results);

		}, Math.random() * 1000, false);
		return deferred.promise;
	}

	this.createStockItemTypeFilterFor = function(query) {
		var lowercaseQuery = angular.lowercase(query);
		return function filterFn(stockItemType) {

			return (angular.lowercase(stockItemType.itemName).indexOf(
					lowercaseQuery) >= 0);
		};
	}

	this.queryStockItemTypeSearch = function(stockItemTypeList, query) {
		var results = query ? stockItemTypeList.filter(this
				.createStockItemTypeFilterFor(query)) : [];
		var deferred = $q.defer();
		$timeout(function() {
			deferred.resolve(results);

		}, Math.random() * 1000, false);
		return deferred.promise;
	}

	this.createStockItemFilterFor = function(query) {
		var lowercaseQuery = angular.lowercase(query);
		return function filterFn(stockItem) {

			return (angular.lowercase(stockItem.stockItemType.itemName)
					.indexOf(lowercaseQuery) >= 0);
		};
	}

	this.queryStockItemSearch = function(stockItemList, query) {
		var results = query ? stockItemList.filter(this
				.createStockItemFilterFor(query)) : [];
		var deferred = $q.defer();
		$timeout(function() {
			deferred.resolve(results);

		}, Math.random() * 1000, false);
		return deferred.promise;
	}
});
