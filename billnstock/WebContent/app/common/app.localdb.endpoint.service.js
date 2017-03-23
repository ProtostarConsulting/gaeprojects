angular.module("stockApp").factory('localDBServiceFactory',
		localDBServiceFactory);

function localDBServiceFactory($log, $q, $timeout, $localStorage) {

	var serviceFactory = {};

	var UserService = {};

	serviceFactory.getUserService = function() {
		return UserService;
	}

	UserService.saveLoggedInUser = function(user) {
		$localStorage.loggedinUser = user;

	}

	UserService.logout = function() {
		$localStorage.loggedinUser = null;
	}

	UserService.getLoggedinUser = function() {
		var user = $localStorage.loggedinUser;
		if (user == 'undefined' || user == null)
			return null;
		else
			return $localStorage.loggedinUser;
	}

	return serviceFactory;
}
