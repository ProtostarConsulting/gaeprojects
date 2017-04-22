var app = angular.module("stockApp");

app.controller("bom_requisition", function($scope, $window, $mdToast, $timeout,
		$mdSidenav, $mdUtil, $log, $stateParams, objectFactory, appEndpointSF,
		$mdDialog, $mdMedia) {
	$scope.curUser = appEndpointSF.getLocalUserService().getLoggedinUser();
	
	
	$scope.addEmptyProductionRequisition=function(){
		return{
			bomEntity:null,
			productQty:"",
			catList:[]
		}
	};
	$scope.productionRequisition=$scope.addEmptyProductionRequisition();
	
	$scope.query = {
			order : 'firstName',
			limit : 5,
			page : 1,
			totalSize : 0,
			pagesLoaded : 0
		};
	$scope.bomList=[];
	$scope.stockItemCategories =[];
	$scope.stockTypeList=[];
	var dummyStockTypeList=[];
	
	$scope.logOrder = function(order) {
		console.log('order: ', order);
	};

	$scope.logPagination = function(page, limit) {
		console.log('page: ', page);						
		console.log('limit: ', limit);
		$location.hash('tp1');
		$anchorScroll();
		if ($scope.query.page > $scope.query.pagesLoaded) {
			$scope.fetchProductionList();
		}
	}		
	$scope.calculation=function()
	{	
		
		for (var j = 0; j < $scope.stockTypeList.length; j++) 
		{
			if($scope.productionRequisition.productQty!=0||$scope.productionRequisition.productQty!=null){
				
				$scope.stockTypeList[j].qty*=$scope.productionRequisition.productQty;}
			}
		
	};
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
			qty : ""		
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
	
	$scope.fetchProductionList=function(){
		var productService = appEndpointSF
		.getProductionService();
		
		productService.getlistBomEntity($scope.curUser.business.id)
		.then(function(list) {
		$scope.bomList=list;
				});
	}
	$scope.submitPequisition = function() {

		var productService = appEndpointSF.getProductionService();
		productService.addRequisition($scope.productionRequisition).then(function() {
			$scope.showAddToast();
		});

	}
	$scope.selectItems=function(bomEntity)
	{
		var productService = appEndpointSF.getProductionService();
		productService.getEmptyProductionRequisition(bomEntity).then(function(requiList) {
			$scope.requiList=requiList;
			
			$scope.productionRequisition.bomEntity=$scope.requiList.bomEntity;
			$scope.productionRequisition.productQty=$scope.requiList.productQty;
			
			$scope.productionRequisition.catList=$scope.requiList.bomEntity.catList;
			
			
			for (var i = 0; i < $scope.requiList.bomEntity.catList.length; i++) {
				
			$scope.stockItemCategories.push($scope.requiList.bomEntity.catList[i].cat);
			for (var j = 0; j < $scope.requiList.bomEntity.catList[i].items.length; j++) {
				
				$scope.stockTypeList.push($scope.requiList.bomEntity.catList[i].items[j]);
				
			}
			
		}		
			
		});
		
	}
	$scope.printBOM = function(bomId) {
		var bid = $scope.curUser.business.id;
		window.open("PrintPdfBOM?bid=" + bid
				+ "&bomId=" + bomId);
	}
	$scope.waitForServiceLoad = function() {
		if (appEndpointSF.is_service_ready) {
			$scope.fetchProductionList();

		} else {
			$log.debug("Services Not Loaded, watiting...");
			$timeout($scope.waitForServiceLoad, 1000);
		}
	}
	$scope.waitForServiceLoad();

});