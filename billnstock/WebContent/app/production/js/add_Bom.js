var app = angular.module("stockApp");

app.controller("add_bom", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,
		$mdDialog, $mdMedia) {

	$scope.getEmptyBomObjadd = function() {
		return {
			productName :"",
			catList : [],
			business : $scope.curUser.business,
			modifiedBy :$scope.curUser.email_id
		}
	};
	$scope.dummyCatList=[];
	$scope.stockItemCategories =[];
	$scope.addBom = $scope.getEmptyBomObjadd();

	$scope.addCatogory = function() {
		var category = {
			cat : null,
			items : []
		};
		$scope.addServiceLineItem()
		if (!$scope.addBom.catList)
		{
			$scope.addBom.catList = [];
		}
		$scope.addBom.catList.push(category);
	}
			
	$scope.addServiceLineItem = function() {
		return item = {
			itemName : "",
			price : "",
			qty : 1
		
		};
		if (!$scope.addBom.items) {
			$scope.addBom.items = [];
		}
		$scope.addBom.items.push(item);
	}

	$scope.addLineItem = function(category) {
		
		var itemObj = {
			itemName : "",
			price : 0,
			qty:"",
			currentBudgetBalance : 0				
		};

		if (!category.items) {
			category.items = [];
		}
		category.items.push(itemObj);
	};
	
	$scope.submitProduction = function() {

		var productService = appEndpointSF.getProductionService();
		productService.addBomEntity($scope.addBom).then(function() {
			$scope.showAddToast();
		});

	}
	$scope.fetchCatogoryList = function() {
		var stockService = appEndpointSF.getStockService();

		stockService.getStockItemTypeCategories($scope.curUser.business.id)
				.then(function(list) {
					$scope.stockItemCategories = list;
				})
	};

	$scope.getStockItemTypes = function() {
		$log.debug("Inside Ctr $scope.getStockItemTypes");
		var stockService = appEndpointSF.getStockService();

		stockService.getStockItemTypes(
				$scope.curUser.business.id).then(
				function(list) {
					$scope.stockTypeList = list;
				});
	}
	
	$scope.removeCategoryItem = function(index) {
		$scope.addBom.catList.splice(index, 1);
	};
	$scope.removeLineItem = function(category, index) {
		category.items.splice(index, 1);
		/*$scope.calItemSubTotal();*/
	};
	
	
	$scope.calItemSubTotal = function(category, item) {
		
		item.currentBudgetBalance=item.price*item.qty;
		
		
		
	};

	var productService = appEndpointSF.getProductionService();
	$scope.fetchCatogoryList();
	$scope.getStockItemTypes();
	$scope.getStockItemTypes();
	$scope.addCatogory();

});