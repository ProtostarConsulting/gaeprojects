app = angular.module("stockApp");
app
		.controller(
				"budgetAddCtr",
				function($scope, $window, $mdToast, $timeout, $mdSidenav,
						$mdUtil, $log, $state, $http, $stateParams,
						$routeParams, $filter, $q, $mdMedia, $mdDialog,
						objectFactory, appEndpointSF, $mdColors) {

					$scope.documentStatusList = [ 'DRAFT', 'SUBMITTED',
							'FINALIZED', 'REJECTED' ];

					$scope.budgetTypes = [ 'OPEX', 'CAPEX', 'NA' ];

					$scope.curUser = appEndpointSF.getLocalUserService()
							.getLoggedinUser();

					$scope.budgetObj = {
						status : 'DRAFT',
						period : "",
						createdDate : new Date(),
						modifiedDate : new Date(),
						modifiedBy : "",
						business : null,
						categoryList : []
					};

					$scope.sectionTitle = "Budget Period";

					$scope.budgetObj = $stateParams.selectedBudgetObj ? $stateParams.selectedBudgetObj
							: $scope.budgetObj;

					$scope.saveBudgetRecord = function() {

						if (!$scope.budgetObj.categoryList) {

							$scope.errorMsg = "Please select atleast one category.";
						} else {

							$scope.budgetObj.business = $scope.curUser.business;
							$scope.budgetObj.modifiedBy = $scope.curUser.email_id;

							var stockService = appEndpointSF.getStockService();
							stockService.addBudget($scope.budgetObj).then(
									function() {
										$scope.showUpdateToast();
										$scope.budgetObj = {};
									});

							$scope.loading = false;
						}
					};

					$scope.submitBudgetRecord = function(ev) {
						$scope.budgetObj.status = 'SUBMITTED';
						$scope.saveBudgetRecord();
					};

					$scope.addCategory = function() {

						var categories = {
							categoryName : "",
							items : [],
							itemSubTotal : 0
						};

						if (!$scope.budgetObj.categoryList) {
							$scope.budgetObj.categoryList = [];
						}
						$scope.budgetObj.categoryList.push(categories);
					};

					$scope.removeCategoryItem = function(index) {
						$scope.budgetObj.categoryList.splice(index, 1);
					};

					$scope.addLineItem = function(category) {

						var itemObj = {
							itemName : "",
							price : 0,
							qty:0,
							currentBudgetBalance : 0
						};

						if (!category.items) {
							category.items = [];
						}
						category.items.push(itemObj);
					};

					$scope.removeLineItem = function(category, index) {
						category.items.splice(index, 1);
						$scope.calItemSubTotal();
					};

					$scope.calItemSubTotal = function(category, item) {
						if (item.currentBudgetBalance == 0)
							item.currentBudgetBalance = item.price;
						category.itemSubTotal = 0;
						category.itemBalanceSubTotal = 0;

						if (category.items) {
							for (var i = 0; i < category.items.length; i++) {
								var budgetItem = category.items[i];
								category.itemSubTotal += budgetItem.price;
								category.itemBalanceSubTotal += budgetItem.currentBudgetBalance;
							}
						}
						
						$scope.calFinalTotal();
					};

					$scope.calFinalTotal = function() {
						$scope.budgetObj.finalTotal = 0;

						if ($scope.budgetObj.categoryList) {
							for (var i = 0; i < $scope.budgetObj.categoryList.length; i++) {
								var item = $scope.budgetObj.categoryList[i];
								$scope.budgetObj.finalTotal += item.itemSubTotal;
							}
						} else if (!$scope.budgetObj.categoryList) {
							$scope.budgetObj.finalTotal = 0;
						}
					};

					$scope.waitForServiceLoad = function() {
						if (appEndpointSF.is_service_ready) {

							if ($scope.budgetObj.id) {
								angular.forEach($scope.budgetObj.categoryList,
										function(cat) {
											$scope.calItemSubTotal(cat,
													cat.items[0]);
										});
							}
							$scope.calFinalTotal();

						} else {
							$log.debug("Services Not Loaded, watiting...");
							$timeout($scope.waitForServiceLoad, 1000);
						}
					};

					$scope.waitForServiceLoad();
				});
