package com.etz.fraudeagleeyemanager.constant;

public final class AppConstant {
	public static final int PAGE_SIZE = 50;
	public static final String DEFAULT_PAGE_LIMIT = "50";
	public static final String PAGE = "page";
	public static final String PAGE_LIMIT = "limit";

	public static final String CREATEDAT = "createdAt";
	public static final String UPDATEDAT = "updatedAt";
	public static final String CREATED_AT = "created_at";
	public static final String UPDATED_AT = "updated_at";

	public static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    
	public static final String ALL = "*";
	public static final String ALL_LINKS = "/**";
	
	public static final String ERROR_SETTING_PROPERTY = "Error while setting entity properties";
	public static final String ERROR_SAVING_TO_DATABASE = "Error while saving to the database";
	public static final String ERROR_SAVING_TO_REDIS = "Error while saving to Redis";
	public static final String ERROR_DELETING_FROM_DATABASE = "Error while deleting from database";
	public static final String ERROR_DELETING_FROM_REDIS = "Error while deleting from Redis";
	public static final String AUTHORIZATION = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String USERNAME = "username";
	
	public static final String USER_ID = "userId";
	public static final String ROLE_ID = "roleId";
	
	public static final String APPROVE_ACTION = "APPROVE";
	public static final String REJECT_ACTION = "REJECT";
	
	public static final String OFAC_WATCHLIST = "OFAC";
	public static final String INTERNAL_WATCHLIST = "WATCHLIST_INTERNAL";
	public static final String NOTIFICATION_GROUPS = "NOTIFICATION_GROUP";
	public static final String RULE = "RULE";
	public static final String SERVICE_RULE = "RULE.PRODUCT";
	public static final String PARAMETER = "PARAMETER";
	public static final String SERVICE_DATASET = "SERVICE.DATASET";
	public static final String USER = "USER";
	public static final String ROLE = "ROLE";
	public static final String DOT_APPROVE = ".APPROVE";
	
	
	public static final String ACCESS_TOKEN_CLAIM = "access_token_claim";
	public static final String ACCESS_TOKEN = "token";

	
	
	private AppConstant() {
		// Just an empty class
	}
    
    
}