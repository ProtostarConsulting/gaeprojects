app = angular.module("stockApp");
app.controller("stockSettingsCtr", function($scope, $window, $mdToast,
		$timeout, $mdSidenav, $mdUtil, $log, $state, $http, $stateParams,
		$routeParams, $filter, $mdMedia, $mdDialog, $q, objectFactory,
		appEndpointSF) {

	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();

	$scope.settingsObj = {}

	$scope.stockItemUnit = {
		unitName : ""
	}

	$scope.stockItemTypeCategory = {
		catName : ""
	}

	$scope.stockItemOrderType = {
		orderType : ""
	}

	$scope.stockItemUnitList = [];

	$scope.stockItemCategories = [];

	$scope.stockItemOrderTypes = [];

	$scope.addSettings = function() {

		var StockService = appEndpointSF.getStockService();
		$scope.settingsObj.business = $scope.curUser.business;
		$scope.settingsObj.modifiedBy = $scope.curUser.email_id;

		StockService.addStockSettings($scope.settingsObj).then(
				function(savedRecoed) {
					$scope.settingsObj = savedRecoed;
					$scope.showUpdateToast();
				});
	}

	$scope.getStockSettingsByBiz = function() {

		var invoiceService = appEndpointSF.getStockService();

		invoiceService.getStockSettingsByBiz($scope.curUser.business.id).then(
				function(settingsList) {

					$scope.settingsObj = settingsList;
					$log.debug("Inside Ctr $scope.settingsObj:"
							+ $scope.settingsObj);
					return $scope.settingsObj;
				});
	}

	$scope.addStockItemUnit = function() {

		$scope.stockItemUnit.business = $scope.curUser.business;
		$scope.stockItemUnit.modifiedBy = $scope.curUser.email_id;
		var stockService = appEndpointSF.getStockService();

		stockService.addStockItemUnit($scope.stockItemUnit).then(
				function() {
					$scope.showUpdateToast();
					$scope.stockItemUnit = {};
					stockService.getStockItemUnits($scope.curUser.business.id)
							.then(function(list) {
								$scope.stockUnitList = list;
							})
				})
	}

	$scope.editStockUnit = function(stockItemUnit) {
		$scope.stockItemUnit = stockItemUnit;
	}

	$scope.addStockItemTypeCategory = function() {

		$scope.stockItemTypeCategory.business = $scope.curUser.business;
		$scope.stockItemTypeCategory.modifiedBy = $scope.curUser.email_id;

		var stockService = appEndpointSF.getStockService();

		stockService.addStockItemTypeCategory($scope.stockItemTypeCategory)
				.then(
						function() {
							$scope.showUpdateToast();
							$scope.stockItemTypeCategory = {};
							stockService.getStockItemTypeCategories(
									$scope.curUser.business.id).then(
									function(list) {
										$scope.stockItemCategories = list;
									})
						})
	}

	$scope.editStockItemCategory = function(stockItemTypeCategory) {
		$scope.stockItemTypeCategory = stockItemTypeCategory;
	}

	$scope.addStockItemOrderType = function() {

		$scope.stockItemOrderType.business = $scope.curUser.business;
		$scope.stockItemOrderType.modifiedBy = $scope.curUser.email_id;
		var stockService = appEndpointSF.getStockService();

		stockService.addStockItemOrderType($scope.stockItemOrderType).then(
				function() {
					$scope.showUpdateToast();
					$scope.stockItemOrderType = {};
					stockService.getStockItemOrderTypes(
							$scope.curUser.business.id).then(function(list) {
						$scope.stockItemOrderTypes = list;
					})
				})
	}

	$scope.editStockItemOrderType = function(stockItemOrderType) {
		$scope.stockItemOrderType = stockItemOrderType;
	}

	$scope.getStockItemUnitsList = function() {

		var stockService = appEndpointSF.getStockService();

		stockService.getStockItemUnits($scope.curUser.business.id).then(
				function(stockItemUnitList) {
					$scope.stockItemUnitList = stockItemUnitList;
				})
	}

	$scope.getStockItemCategoryTypeList = function() {

		var stockService = appEndpointSF.getStockService();

		stockService.getStockItemTypeCategories($scope.curUser.business.id)
				.then(function(stockItemCategories) {
					$scope.stockItemCategories = stockItemCategories;
				})
	}

	$scope.getStockItemOrderTypeList = function() {

		var stockService = appEndpointSF.getStockService();

		stockService.getStockItemOrderTypes($scope.curUser.business.id).then(
				function(stockItemOrderTypes) {
					$scope.stockItemOrderTypes = stockItemOrderTypes;
				})
	}

	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.getStockSettingsByBiz();
			$scope.getStockItemUnitsList();
			$scope.getStockItemCategoryTypeList();
			$scope.getStockItemOrderTypeList();
		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}
	$scope.waitForServiceLoad();

});
