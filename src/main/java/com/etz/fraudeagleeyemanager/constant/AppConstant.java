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
	public static final String ERROR_DELETING_FROM_DATABASE = "Error while saving to the database";
	public static final String ERROR_DELETING_FROM_REDIS = "Error while deleting from Redis";

	
	
	private AppConstant() {
		// Just an empty class
	}
    
    
}