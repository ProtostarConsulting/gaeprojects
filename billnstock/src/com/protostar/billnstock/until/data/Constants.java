package com.protostar.billnstock.until.data;

public class Constants {

	// Endpoint API Access Related
	public static final String WEB_CLIENT_ID = "87556941487-36krkq9mr6iltvqpjd3k0r0il83h0b76.apps.googleusercontent.com";
	public static final String ANDROID_CLIENT_ID = "";
	public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;
	public static final String API_EXPLORER_CLIENT_ID = com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID;
	public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
	public static final String PROFILE_SCOPE = "https://www.googleapis.com/auth/userinfo.profile";

	// GCS bucket names
	// public static final String PROERP_IMAGES_BUCKET = "proerp_image_files";
	public static final String BUCKET_POSTFIX = ".appspot.com";

	// Counter/Sequence Names
	public static final String EMP_NO_COUNTER = "EMP_NO_COUNTER";
	public static final String ACC_ACCOUNT_NO_COUNTER = "ACC_ACCOUNT_NO_COUNTER";
	public static final String INVOICE_NO_COUNTER = "INVOICE_NO_COUNTER";
	public static final String STOCKITEMTYPE_NO_COUNTER = "STOCKITEMTYPE_NO_COUNTER";
	public static final String STOCKRECEIPT_NO_COUNTER = "STOCKRECEIPT_NO_COUNTER";
	public static final String STOCKSHIPMENT_NO_COUNTER = "STOCKSHIPMENT_NO_COUNTER";
	public static final String PURCHASE_ORDER_NO_COUNTER = "PURCHASE_ORDER_NO_COUNTER";
	public static final String QUOTATION_NO_COUNTER = "QUOTATION_NO_COUNTER";
	public static final String VOUCHER_NO_COUNTER = "VOUCHER_NO_COUNTER";
	public static final String GE_NO_COUNTER = "GE_NO_COUNTER";
	public static final String AE_NO_COUNTER = "AE_NO_COUNTER";
	public static final String AEntry_NO_COUNTER = "AEntry_NO_COUNTER";
	public static final String RE_NO_COUNTER = "RE_NO_COUNTER";
	public static final String SV_NO_COUNTER = "SV_NO_COUNTER";
	public static final String VE_NO_COUNTER = "VE_NO_COUNTER";
	public static final String PE_NO_COUNTER = "PE_NO_COUNTER";
	public static final String PV_NO_COUNTER = "PV_NO_COUNTER";
	public static final String GJE_NO_COUNTER = "GJE_NO_COUNTER";
	public static final String AFY_NO_COUNTER = "AFY_NO_COUNTER";
	public static final String AGE_NO_COUNTER = "AGE_NO_COUNTER";
	public static final String RV_NO_COUNTER = "RV_NO_COUNTER";

	// Default Authorizations
	public static final String PROTOSTAR_DEFAULT_AUTHS = "{\"authorizations\":[{\"authName\":\"More Actions\",\"authorizations\":[{\"authName\":\"proadmin\",\"authorizations\":[]},{\"authName\":\"setup\",\"authorizations\":[]},{\"authName\":\"updatemyprofile\",\"authorizations\":[]}]}]}";
	public static final String NEW_BIZ_DEFAULT_AUTHS = "{\"authorizations\":[{\"authName\":\"More Actions\",\"authorizations\":[{\"authName\":\"setup\",\"authorizations\":[]},{\"authName\":\"updatemyprofile\",\"authorizations\":[]}]}]}";
	public static final String NEW_BIZ_ADMIN_USER_DEFAULT_AUTHS = "{\"authorizations\":[{\"authName\":\"More Actions\",\"authorizations\":[{\"authName\":\"setup\",\"authorizations\":[]},{\"authName\":\"updatemyprofile\",\"authorizations\":[]}]}]}";
	public static final String NEW_BIZ_USER_DEFAULT_AUTHS = "{\"authorizations\":[{\"authName\":\"More Actions\",\"authorizations\":[{\"authName\":\"proadmin\",\"authorizations\":[]},{\"authName\":\"setup\",\"authorizations\":[]},{\"authName\":\"HRMS\",\"authorizations\":[{\"authName\":\"SalaryMaster\",\"authorizations\":[]},{\"authName\":\"LeaveDetail\",\"authorizations\":[]},{\"authName\":\"Monthly Salary\",\"authorizations\":[]},{\"authName\":\"PayrollReports\",\"authorizations\":[]}]},{\"authName\":\"Accounting\",\"authorizations\":[{\"authName\":\"accounting\",\"authorizations\":[]}]},{\"authName\":\"updatemyprofile\",\"authorizations\":[]}]}]}";

	// Misc
	public static final String DEFAULT_EMP_DEPT = "Default";

	public enum DocumentStatus {
		DRAFT, SUBMITTED, FINALIZED, REJECTED, APPROVED, SENT, PAID
	};

	public static enum DiscountType {
		NA, Fixed, Percentage
	};

	public enum AccountingAccountType {
		PERSONAL, REAL, NOMINAL
	};

	public enum AccountGroupType {
		ASSETS, EQUITY, LIABILITIES, INCOME, EXPENSES, OTHERINCOMES, OTHEREXPENCES, NA,PANDL
	};
}
