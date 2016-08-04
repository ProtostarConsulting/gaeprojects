angular.module("prostudyApp").factory('objectFactory', objectFactoryFn);

function objectFactoryFn($log) {

	var objectFactory = {};

	objectFactory.newCustomer = function() {
		return {
			firstName : '',
			lastName : '',
			mobileNo : '',
			email : '',
			address : {
				line1 : '',
				line2 : '',
				city : '',
				state : '',
				pin : '',
			}
		}
	};

	objectFactory.newStudent = function() {
		return {
			firstName : '',
			lastName : '',
			mobileNo : '',
			email : '',
			address : {
				line1 : '',
				line2 : '',
				city : '',
				state : '',
				pin : '',
			}
		}
	};
	
	objectFactory.newInstituteUser = function(role,instituteID,isGoogleUser) {
		var auths = {"authorizations":[{"authName":"myprofile","authorizations":[]}]};
		if(role == "Admin"){
			auths.authorizations.push({"authName":"setup","authorizations":[]});
		}
		
		return {
			'instituteID' : instituteID,
			'institute' : '',
			'firstName' : '',
			'lastName' : '',
			'email_id' : '',
			'address' : '',
			'contact' :'',
			'role' : role,
			'standard' : '',
			'division' : '',
			'subject' : '',
			'password' : '',
			'isGoogleUser' : isGoogleUser,
			'authorizations': angular.toJson(auths) 
		}
	}

	return objectFactory;
}
